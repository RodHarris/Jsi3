package jsi3.util.netlayer;

import org.apache.xmlrpc.*;
import org.apache.xmlrpc.secure.*;

import javax.net.ssl.*;

import java.util.*;
import java.net.*;
import java.beans.*;
import java.io.*;

import static jsi3.lib.system.Statics.*;
import static jsi3.lib.console.Statics.*;


public abstract class XmlRpc2Ghost
{
	private XmlRpcClient conn;
	
	private final Vector<Object> params = new Vector<Object>();
	
	private URL url;
	
	private String remote_object_name;
	
	private boolean secure;
	
	//private static int rpc_stacktrace = -1;
	
	private final static String trustStoreFilename = "trustStoreClient";
	
	static
	{
		//XmlRpc.setKeepAlive( true );
		
		//rpc_stacktrace  = calibrate_stacktrace();
		
		//affirm( rpc_stacktrace != -1, "Unable to calibrate the stacktrace for XML-RPC" );
		
		System.setProperty( "javax.net.ssl.trustStore", trustStoreFilename );
		
		setHostNameVerifier();
	}
	
	
	private static void setHostNameVerifier()
	{
		HostnameVerifier hv = new HostnameVerifier()
		{
			public boolean verify( String urlHostName, SSLSession session )
			{
				//System.out.println( "Warning: URL Host: "+urlHostName+" vs. "+session.getPeerHost() );
				
				return true;
			}
		}
		
		;
		
		HttpsURLConnection.setDefaultHostnameVerifier( hv );
	}
	
	
	public void connect( String url_str, String remote_object_name ) throws MalformedURLException
	{
		boolean secure = false;
		
		if( url_str.startsWith( "https" ) ) secure = true;
		
		this.remote_object_name = remote_object_name;
		
		this.secure = secure;
		
		if( secure )
		{
			connect_secure( new URL( url_str ) );
		}
		
		else
		{
			connect_unsecure( new URL( url_str ) );
		}
	}
	
	
	private void connect_unsecure( URL url )
	{
		this.url = url;
		
		conn = new XmlRpcClientLite( url );
		
		CommonsXmlRpcTransportFactory tf = new CommonsXmlRpcTransportFactory( url );
		
		tf.setConnectionTimeout( 2000 );
		
		conn = new XmlRpcClient( url, tf );
		
		cverbose.println( conn );
	}
	
	
	private void connect_secure( URL url )
	{
		this.url = url;
		
		conn = new SecureXmlRpcClient( url );
		
		cverbose.println( conn );
	}
	
	
	public void reconnect() throws MalformedURLException
	{
		if( secure )
		{
			connect_secure( url );
		}
		
		else
		{
			connect_unsecure( url );
		}
	}
	
	
	public boolean connected()
	{
		if( conn == null ) return false;
		
		return true;
	}
	
	
	public Object invoke( String method, Object... args )// throws XmlRpcException, IOException
	{
		method = remote_object_name + "." + method;

		params.clear();
		
		for( Object o : args ) params.addElement( o );
		
		String rpc = method + "( ";
		
		for( int i=0; i<args.length; i++ ) rpc += "[%s], ";

		if( args.length > 0 )
		{
			rpc = rpc.substring( 0, rpc.length() - 2 ) + " ";
		}
		
		rpc += ")";
		
		cverbose.println( conn.getURL() + ":" + rpc, args );
		
		Object r = null;
		
		try
		{
			r = conn.execute( method, params );

			cverbose.println( r );
		}
		catch( Exception ex )
		{
			throw new RuntimeException( ex.getMessage(), ex );
		}
		
		return r;
	}
	
	
// 	protected Object rpc( Object... args )// throws XmlRpcException, IOException
// 	{
// 		return invoke( remote_object_name + "." + Thread.currentThread().getStackTrace()[ rpc_stacktrace ].getMethodName(), args );
// 	}
// 	
// 	
// 	private static int calibrate_stacktrace()
// 	{
// 		return method1();
// 	}
// 	
// 	
// 	private static int method1()
// 	{
// 		int i=0;
// 		
// 		for( StackTraceElement ste : Thread.currentThread().getStackTrace() )
// 		{
// 			cdebug.println( "level: %d : method %s", i, ste.getMethodName() );
// 			
// 			if( ste.getMethodName().equals( "calibrate_stacktrace" ) )
// 			{
// 				return i;
// 			}
// 			
// 			i++;
// 		}
// 		
// 		return -1;
// 	}
}

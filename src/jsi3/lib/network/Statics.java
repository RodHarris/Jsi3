package jsi3.lib.network;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.jmdns.*;

import org.apache.commons.io.*;

import static jsi3.lib.console.Statics.*;

public class Statics
{
	private static JmDNS jmdns;

	/**
	modified this code from some I found at www.kodejava.org
	*/
	public static String[] list_MAC_addresses() throws UnknownHostException, SocketException
	{
		ArrayList<String> mac_addr_list = new ArrayList<String>();

		Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

		String mac_addr = null;

		while( nis.hasMoreElements() )
		{
			NetworkInterface ni = nis.nextElement();

			byte[] mac = ni.getHardwareAddress();

			if( mac == null ) continue;

			String str = "";

			for ( int i = 0; i < mac.length; i++ )
			{
				str += String.format( "%02X%s", mac[ i ], ( i < mac.length - 1 ) ? "-" : "" );
			}

			mac_addr_list.add( str );
		}

		return mac_addr_list.toArray( new String[ mac_addr_list.size() ] );
	}

	/**
	*  Returns the unqualified hostname of the local machine via the InetAddress.getLocalHost().getHostName() method
	*  ie. may not do a DNS lookup of the local host to find this
	*/
	public static String hostname() throws UnknownHostException
	{
		String hostname = InetAddress.getLocalHost().getHostName();

		int dot_index = hostname.indexOf( "." );

		if( dot_index == -1 ) return hostname;

		return hostname.substring( 0, dot_index );
	}


	/**
	*  Register a service via multidast dns (Avahi, Bonjour, etc)
	*  Opens a multicast socket to the mdns group if needed
	*/
	public static ServiceInfo register_service_via_mdns( String type, String name, int port, String text ) throws IOException
	{
		if( jmdns == null )
		{
			jmdns = JmDNS.create();
		}

		ServiceInfo service_info = ServiceInfo.create( type, name, port, text );

		jmdns.registerService( service_info );

		return service_info;
	}


	/**
	*  UnRegister a service via multidast dns (Avahi, Bonjour, etc)
	*  does not close the port if there are no more services being advertised
	*  the calling application must keep track of when to close the socket
	*/
	public static void unregister_service_via_mdns( ServiceInfo service_info )
	{
		jmdns.unregisterService( service_info );
	}


	/**
	*  Closes the multicast socket to the mdns group
	*/
	public void close_mdns()
	{
		jmdns.close();
	}


	/**
	 * get the data specified in this url
	 */
	public static byte[] wget( String url ) throws Exception
	{
		URL _url = null;
		
		InputStream is = null;

		byte[] bytes = null;

		try
		{
			_url = new URL( url );
			
			is = _url.openStream ();

			bytes = IOUtils.toByteArray( is );
		}
		catch (IOException e)
		{
			cerr.println( "Failed while reading bytes from %s: %s", _url.toExternalForm(), e.getMessage() );
			
			throw e;
		}
		finally
		{
			if (is != null)
			{
				is.close();
			}
		}
		
		return bytes;
	}



	private Statics(){};
}

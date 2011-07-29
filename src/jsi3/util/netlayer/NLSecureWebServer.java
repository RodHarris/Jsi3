package jsi3.util.netlayer;


import org.apache.xmlrpc.secure.*;

import java.net.*;
import java.io.*;
import java.security.*;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;


/**
  A webserver for encrypted xml-rpc communication
  I'm not sure how it works, just found this code in some Sun tutorial
*/
class NLSecureWebServer extends SecureWebServer
{
	private static final String SECURE_ALGORITHM = "SunX509";
	
	private static final String SECURE_PROTOCOL = "TLS";
	
	private static final String KEYSTORE_TYPE = "JKS";
	
	private static final boolean requireClientAuth = false;
	
	private int serverPort;
	
	private String keyStoreFilename;
	
	private String keyStorePassword;
	
	private String keyPassword;
	
	public NLSecureWebServer( int serverPort, String keyStoreFilename, String keyStorePassword, String keyPassword )
	{
		super( serverPort );
		
		this.serverPort = serverPort;
		
		this.keyStoreFilename = keyStoreFilename;
		
		this.keyStorePassword = keyStorePassword;
		
		this.keyPassword = keyPassword;
	}
	
	protected java.net.ServerSocket createServerSocket( int port, int backlog, java.net.InetAddress add ) throws java.lang.Exception
	{
		return getServerSocket( port, keyStoreFilename, keyStorePassword, keyPassword, requireClientAuth );
	}
	
	ServerSocket getServerSocket( int serverPort, String keyStoreFilename, String keyStorePassword, String keyPassword, boolean requireClientAuth ) throws Exception
	{
		// Make sure that JSSE is available
		
		Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider() );
		
		// A keystore is where keys and certificates are kept
		
		// Both the keystore and individual private keys should be password protected
		
		KeyStore keystore = KeyStore.getInstance( KEYSTORE_TYPE );
		
		keystore.load( new FileInputStream( keyStoreFilename ), keyStorePassword.toCharArray() );
		
		// A KeyManagerFactory is used to create key managers
		
		KeyManagerFactory kmf = KeyManagerFactory.getInstance( SECURE_ALGORITHM );
		
		// Initialize the KeyManagerFactory to work with our keystore
		
		kmf.init( keystore, keyPassword.toCharArray() );
		
		// An SSLContext is an environment for implementing JSSE
		
		// It is used to create a ServerSocketFactory
		
		SSLContext sslc = SSLContext.getInstance( SECURE_PROTOCOL );
		
		// Initialize the SSLContext to work with our key managers
		
		sslc.init( kmf.getKeyManagers(), null, null );
		
		// Create a ServerSocketFactory from the SSLContext
		
		ServerSocketFactory ssf = sslc.getServerSocketFactory();
		
		// Socket to me
		
		SSLServerSocket serverSocket = ( SSLServerSocket ) ssf.createServerSocket( serverPort );
		
		// Authenticate the client?
		
		serverSocket.setNeedClientAuth( requireClientAuth );
		
		// Return a ServerSocket on the desired port
		
		return serverSocket;
	}
}

package jsi3.lib.network;

import java.util.*;
import java.net.*;
import java.io.*;

/**
@author Rod Harris
*/


public abstract class UdpListener implements Runnable
{
	public int so_rcvbuf_size = 64 * 1024;
	
	public int so_timeout = 50;
	
	public int max_packet_length = 1536;
	
	
	private Thread reciever_thread;
	
	private boolean listening;
	
	private byte[] packet_data;
	
	private DatagramPacket rcv_packet;
	
	private DatagramSocket rcv_socket;
	
	
	protected UdpListener()
	{
	}
	
	
	private void clear()
	{
		so_rcvbuf_size = 64 * 1024;
		
		so_timeout = 50;
		
		max_packet_length = 1536;
		
		reciever_thread = null;
		
		listening = false;
		
		packet_data = null;
		
		rcv_packet = null;
		
		rcv_socket = null;
	}
	
	
	private void init()
	{
		packet_data = new byte[ max_packet_length ];
		
		rcv_packet = new DatagramPacket( packet_data, max_packet_length );
	}
	
	
	public void open_socket( int port ) throws SocketException
	{
		if( rcv_socket != null ) throw new RuntimeException( "Socket already open" );
		
		init();
		
		rcv_socket = new DatagramSocket( port );
		
		open_socket();
	}
	
	
	public void open_socket( String addr, int port ) throws SocketException, UnknownHostException, IOException
	{
		if( rcv_socket != null ) throw new SocketException( "Socket already open" );
		
		init();
		
		InetAddress group = InetAddress.getByName( addr );
		
		if( ! group.isMulticastAddress() ) throw new SocketException( addr + " is not a multicast address" );
		
		MulticastSocket mc_rcv_socket = new MulticastSocket( port );
		
		mc_rcv_socket.joinGroup( group );
		
		rcv_socket = mc_rcv_socket;
		
		open_socket();
	}
	
	
	private void open_socket() throws SocketException
	{
		set_socket_options();
		
		reciever_thread = new Thread( this );
		
		listening = true;
		
		reciever_thread.start();
	}
	
	
	private void set_socket_options() throws SocketException
	{
		rcv_socket.setSoTimeout( so_timeout );
		
		if( rcv_socket.getSoTimeout() != so_timeout )
		{
			throw new SocketException( "SO_TIMEOUT (" + rcv_socket.getSoTimeout() + ") != requested SO_TIMEOUT (" + so_timeout + ")" );
		}
		
		rcv_socket.setReceiveBufferSize( so_rcvbuf_size );
		
		if( rcv_socket.getReceiveBufferSize() != so_rcvbuf_size )
		{
			throw new SocketException( "SO_RCVBUF_SIZE (" + rcv_socket.getReceiveBufferSize() + ") != requested SO_RCVBUF_SIZE (" + so_rcvbuf_size + ")" );
		}
		
		rcv_socket.setReuseAddress( true );
		
		if( ! rcv_socket.getReuseAddress() )
		{
			throw new SocketException( "SO_REUSEADDR = false" );
		}
	}
	
	
	public void close_socket() throws IOException
	{
		if( rcv_socket == null ) throw new SocketException( "Socket not open" );
		
		listening = false;
		
		try
		{
			reciever_thread.join();
		}
		catch( InterruptedException ex )
		{
			//LibSys.nonfatal( ex );
		}
		
		rcv_socket.close();
		
		clear();
	}
	
	
	public void run()
	{
		while( listening )
		{
			try
			{
				rcv_socket.receive( rcv_packet );
				
				// thread may have been stopped while blocking on the receive
				
				if( listening ) process_packet( rcv_packet );
			}
			catch( SocketTimeoutException ex )
			{
			}
			catch( IOException ex )
			{
				io_receive_error( ex );
			}
		}
	}
	
	/**
	over ride this error to be notified of IOExceptions in the receive thread
	*/
	protected void io_receive_error( IOException ex )
	{
	}
	
	
	protected abstract void process_packet( DatagramPacket packet );
	
	
}

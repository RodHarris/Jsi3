package jsi3.lib.network;

import java.util.*;
import java.net.*;
import java.io.*;

/**
@author Rod Harris
*/


public class UdpSender
{
	public static final int DEFAULT_SO_SNDBUF_SIZE = 64 * 1024;
	
	public static int SO_SNDBUF_SIZE = DEFAULT_SO_SNDBUF_SIZE;
	
	private DatagramSocket snd_socket;
	
	private EndPoint target;
	
	private int local_port;
	
	enum States
	{
		open, closed
	}
	
	private States state;
	
	
	public UdpSender()
	{
	}
	
	
	public void open_socket() throws SocketException
	{
		snd_socket = new DatagramSocket();
		
		this.local_port = snd_socket.getPort();
		
		set_socket_options();
		
		state = States.open;
	}
	
	
	public void open_socket( int local_port ) throws UnknownHostException, SocketException
	{
		this.local_port = local_port;
		
		snd_socket = new DatagramSocket( local_port );
		
		set_socket_options();
		
		state = States.open;
	}
	
	
	public void open_socket( String snd_addr, int snd_port ) throws UnknownHostException, SocketException
	{
		target = new EndPoint( snd_addr, snd_port );
		
		snd_socket = new DatagramSocket();
		
		this.local_port = snd_socket.getPort();
		
		set_socket_options();
		
		state = States.open;
	}
	
	
	public void open_socket( String snd_addr, int snd_port, int local_port ) throws UnknownHostException, SocketException
	{
		target = new EndPoint( snd_addr, snd_port );
		
		this.local_port = local_port;
		
		snd_socket = new DatagramSocket( local_port );
		
		set_socket_options();
		
		state = States.open;
	}
	
	
	private void set_socket_options() throws SocketException
	{
		snd_socket.setSendBufferSize( SO_SNDBUF_SIZE );
		
		if( snd_socket.getSendBufferSize() != SO_SNDBUF_SIZE )
		{
			throw new SocketException( "SO_SNDBUF_SIZE (" + snd_socket.getSendBufferSize() + ") != requested SO_SNDBUF_SIZE (" + SO_SNDBUF_SIZE + ")" );
		}
		
		snd_socket.setReuseAddress( true );
		
		if( ! snd_socket.getReuseAddress() )
		{
			throw new SocketException( "SO_REUSEADDR = false" );
		}
	}
	
	
	public void send_packet( DatagramPacket packet ) throws IOException
	{
		packet.setAddress( target.inet );
		
		packet.setPort( target.port );
		
		snd_socket.send( packet );
	}
	
	
	public void send_packet( InetAddress inet, int port, DatagramPacket packet ) throws IOException
	{
		packet.setAddress( inet );
		
		packet.setPort( port );
		
		snd_socket.send( packet );
	}
	
	
	public void send_packet( EndPoint target, DatagramPacket packet ) throws IOException
	{
		packet.setAddress( target.inet );
		
		packet.setPort( target.port );
		
		snd_socket.send( packet );
	}
}

package jsi3.lib.network;

import java.net.*;
import java.io.*;

public class EndPoint extends NetAddr
{
	public final int port;
	
	
	public EndPoint( String addr, int port ) throws UnknownHostException
	{
		super( addr );
		
		this.port = port;
	}
	
	
	public EndPoint( InetAddress inet, int port )// throws UnknownHostException
	{
		super( inet );
		
		this.port = port;
	}
	
	
	public EndPoint( int ip4, int port ) throws UnknownHostException
	{
		super( ip4 );
		
		this.port = port;
	}
	
	
	public EndPoint( byte[] quads, int port ) throws UnknownHostException
	{
		super( quads );
		
		this.port = port;
	}
	
	
	public boolean equals( Object o )
	{
		if( ! ( o instanceof EndPoint ) ) return false;
		
		EndPoint other = ( EndPoint ) o;
		
		return this.ip4 == other.ip4 && this.port == other.port;
	}
	
	
	public static EndPoint create( String group ) throws UnknownHostException
	{
		String[] tokens = group.split( "/" );
		
		if( tokens.length != 2 ) throw new IllegalArgumentException( group + " must be in the format [addr]/[port]" );
		
		String addr = tokens[ 0 ];
		
		int port = Integer.parseInt( tokens[ 1 ] );
		
		return new EndPoint( addr, port );
	}
}

package jsi3.lib.network;

import java.net.*;
import java.io.*;

public class NetAddr
{
	public final InetAddress inet;
	
	public final String name;
	
	public final int ip4;
	
	public final byte[] quads;
	
	public final String dotted_quads;
	
	
	public NetAddr( String addr ) throws UnknownHostException
	{
		inet = InetAddress.getByName( addr );
		
		name = inet.getHostName();
		
		quads = inet.getAddress();
		
		ip4 = i4( quads[ 0 ], quads[ 1 ], quads[ 2 ], quads[ 3 ] );
		
		dotted_quads = inet.getHostAddress();
	}
	
	
	public NetAddr( InetAddress inet )// throws UnknownHostException
	{
		this.inet = inet;
		
		name = inet.getHostName();
		
		quads = inet.getAddress();
		
		ip4 = i4( quads[ 0 ], quads[ 1 ], quads[ 2 ], quads[ 3 ] );
		
		dotted_quads = inet.getHostAddress();
	}
	
	
	public NetAddr( int ip4 ) throws UnknownHostException
	{
		this.ip4 = ip4;
		
		quads = new byte[]
		{
			 b0( ip4 ), b1( ip4 ), b2( ip4 ), b3( ip4 ) 
		}
		;
		
		inet = InetAddress.getByAddress( quads );
		
		name = inet.getHostName();
		
		dotted_quads = inet.getHostAddress();
	}
	
	
	public NetAddr( byte[] quads ) throws UnknownHostException
	{
		this.quads = quads;
		
		inet = InetAddress.getByAddress( quads );
		
		name = inet.getHostName();
		
		ip4 = i4( quads[ 0 ], quads[ 1 ], quads[ 2 ], quads[ 3 ] );
		
		dotted_quads = inet.getHostAddress();
	}
	
	/**
	*	stole these for jsi3.lib.maths.LibMaths
	*/
	private static byte b0( int i )
	{
		return ( byte ) ( ( i >> 24 ) & 0xff );
	}
	
	
	private static byte b1( int i )
	{
		return ( byte ) ( ( i >> 16 ) & 0xff );
	}
	
	
	private static byte b2( int i )
	{
		return ( byte ) ( ( i >> 8 ) & 0xff );
	}
	
	
	private static byte b3( int i )
	{
		return ( byte ) ( ( i >> 0 ) & 0xff );
	}
	
	private static int i4( byte b0, byte b1, byte b2, byte b3 )
	{
		return ( b0 & 0xff ) << 24 | ( b1 & 0xff ) << 16 | ( b2 & 0xff ) << 8 | ( b3 & 0xff ) << 0;
	}
}

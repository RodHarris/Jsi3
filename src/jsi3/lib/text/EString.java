package jsi3.lib.text;

import java.io.*;

import static jsi3.lib.text.Statics.*;

public class EString extends OutputStream
{
	final StringBuilder back;
	
	public EString()
	{
		back = new StringBuilder();
	}
	
	
	public EString( String s )
	{
		back = new StringBuilder( s );
	}
	
	
	public EString( EString s )
	{
		back = new StringBuilder( s.toString() );
	}
	
	
	public String toString()
	{
		return back.toString();
	}
	
	
	public void print( Object arg )
	{
		back.append( arg );
	}
	
	
	public void print( String fmt, Object... args )
	{
		back.append( sprintf( fmt, args ) );
	}
	
	
	public void println()
	{
		back.append( "\n" );
	}
	
	
	public void println( Object arg )
	{
		back.append( arg );
		
		back.append( "\n" );
	}
	
	
	public void println( String fmt, Object... args )
	{
		back.append( sprintf( fmt, args ) );
		
		back.append( "\n" );
		
	}
	
	
	public void mult( int n )
	{
		String s = back.toString();
		
		for( int i=0; i<n-1; i++ ) print( s );
	}
	
	
	public void close()
	{
	}
	
	
	public void flush()
	{
	}
	
	
	public void write( byte[] b )
	{
		back.append( new String( b ) );
	}
	
	
	public void write( byte[] b, int off, int len )
	{
		back.append( new String( b, off, len ) );
	}
	
	
	public void write( int b )
	{
		//LibDebug.not_implemented();
		
		back.append( new byte[]
		{
			 ( byte ) ( b & 0xFF ) 
		}
		);
	}
	
	
	public void read_from( InputStream in ) throws IOException
	{
		byte[] data = new byte[ in.available() ];
		
		in.read( data );
		
		back.append( new String( data ) );
	}
	
	
	public void space( int num )
	{
		for( int i=0; i<num; i++ )
		{
			back.append( " " );
		}
	}
	
	/*
	
	Closes this output stream and releases any system resources associated with this stream.
	void	flush()
	Flushes this output stream and forces any buffered output bytes to be written out.
	void	write( byte[] b )
	Writes b.length bytes from the specified byte array to this output stream.
	void	write( byte[] b, int off, int len )
	Writes len bytes from the specified byte array starting at offset off to this output stream.
	abstract  void	write( int b )
	Writes the specified byte to this output stream.*/
}

package jsi3.lib.text;

import jsi3.lib.console.*;

import java.io.*;

public class LString extends EString
{
	public static final OStream clog = new OStream( System.err );

	static
	{
		clog.add_decorators( new NameDecorator( "LOG: " ), new TraceDecorator( "%f:%l: ", 1 ) );
	}

	public LString()
	{
		super();
	}


	public LString( String s )
	{
		super( s );
	}


	public LString( EString s )
	{
		super( s );
	}


	public String toString()
	{
		return super.toString();
	}


	public void print( Object arg )
	{
		clog.print( arg );

		super.print( arg );
	}


	public void print( String fmt, Object... args )
	{
		clog.print( fmt, args );

		super.print( fmt, args );
	}


	public void println()
	{
		clog.println();

		super.println();
	}


	public void println( Object arg )
	{
		clog.println( arg );

		super.println( arg );
	}


	public void println( String fmt, Object... args )
	{
		clog.println( fmt, args );

		super.println( fmt, args );
	}


	public void mult( int n )
	{
		String s = super.toString();

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
		print( new String( b ) );
	}


	public void write( byte[] b, int off, int len )
	{
		print( new String( b, off, len ) );
	}


	public void write( int b )
	{
		//LibDebug.not_implemented();

		write( new byte[] { ( byte ) ( b & 0xFF ) });
	}


	public void read_from( InputStream in ) throws IOException
	{
		byte[] data = new byte[ in.available() ];

		in.read( data );

		print( new String( data ) );
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

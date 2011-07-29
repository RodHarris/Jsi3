package jsi3.lib.system;

import java.io.*;

public class Pipe implements Runnable
{
	private boolean looping = true;
	
	private InputStream in;
	
	private OutputStream out;
	
	
	/**
	*  Constructs (and starts) a pipe between the input and output streams
	*/
	public Pipe( InputStream in, OutputStream out ) throws Exception
	{
		this.in = in;
		
		this.out = out;
		
		new Thread( this ).start();
	}
	
	
	/**
	*  Stops the pipe
	*/
	public void break_pipe()
	{
		looping = false;
	}
	
	
	/**
	*  Don't call this directly - its automatically called in the constructor
	*/
	public void run()
	{
		byte[] buf = new byte[ 4096 ];
		
		int avail = 0;
		
		do
		{
			try
			{
				//avail = in.available();
				
				avail = in.read( buf );
				
				if( avail != -1 ) out.write( buf, 0, avail );
				
				Thread.sleep( 10 );
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
			
		}while( looping );
	}
}

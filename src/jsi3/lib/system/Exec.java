package jsi3.lib.system;

import java.io.*;

import jsi3.lib.text.*;

import static jsi3.lib.system.Statics.*;

public class Exec
{
	public String[] cmds;

	public Exec( String... cmds )
	{
		this.cmds = cmds;
	}
	

	public String toString()
	{
		EString es = new EString();
		
		es.print( cmds[ 0 ] );
		
		for( int i=1; i<cmds.length; i++ )
		{
			es.print( " \"%s\"", cmds[ i ] );
		}
		
		return es.toString().trim();
	}
	

	public ProcessResults run() throws IOException, InterruptedException
	{
		return run( null );
	}
	
	
	public ProcessResults run( File directory ) throws IOException, InterruptedException
	{
		ProcessBuilder pb = new ProcessBuilder( cmds );
		
		if( directory != null )
		{
			pb.directory( directory );
		}
		
		return handle_process( pb.start() );
	}
}

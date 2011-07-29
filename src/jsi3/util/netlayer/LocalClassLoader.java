package jsi3.util.netlayer;


import java.io.*;

import static jsi3.lib.filesystem.Statics.*;


class LocalClassLoader extends ClassLoader
{
	public Class findClass( String name ) throws ClassNotFoundException
	{
		try
		{
			byte[] b = loadClassData( name );
		
			return defineClass( name, b, 0, b.length );
		}
		catch( IOException ex ) // also catches FileNotFoundException
		{
			throw new ClassNotFoundException( "Couldn't load " + name, ex );
		}
	}

	private byte[] loadClassData( String name ) throws FileNotFoundException, IOException
	{
		return load_file( name + ".class" );
	}
}

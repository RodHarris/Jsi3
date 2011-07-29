package jsi3.util.netlayer;


import javax.tools.*;

import java.util.*;
import java.io.*;
import java.net.*;


class RAMResidentFileManager implements JavaFileManager
{
	StandardJavaFileManager sfm;
	
	RAMResidentFileManager()
	{
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		if( compiler != null )
		{
			sfm = compiler.getStandardFileManager( null, null, null );
		}
	}
	
	public void 	close()
	{
		throw new UnsupportedOperationException( "close()" );
	}
	
	
	public void 	flush()
	{
		
	}
	
	
	public ClassLoader 	getClassLoader(JavaFileManager.Location location)
	{
		return sfm.getClassLoader( location );
	}
	
	
	public FileObject 	getFileForInput(JavaFileManager.Location location, String packageName, String relativeName)
	{
		throw new UnsupportedOperationException( "getFileForInput()" );
	}
	
	
	public FileObject 	getFileForOutput(JavaFileManager.Location location, String packageName, String relativeName, FileObject sibling)
	{
		throw new UnsupportedOperationException( "getFileForOutput()" );
	}
	
	
	public JavaFileObject 	getJavaFileForInput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind)
	{
		throw new UnsupportedOperationException( "getJavaFileForInput()" );
	}
	
	
	public JavaFileObject 	getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
	{
		try
		{
			RAMResidentClassFileObject rrcfo = NetLayer.new_class_file( className );
			
			return rrcfo;
		}
		catch( URISyntaxException ex )
		{
			throw new RuntimeException( "couldn't getJavaFileForOutput()", ex );
		}
	}
	
	
	public boolean 	handleOption(String current, Iterator<String> remaining)
	{
		throw new UnsupportedOperationException( "handleOption()" );
	}
	
	
	public boolean 	hasLocation(JavaFileManager.Location location)
	{
		return sfm.hasLocation( location );
	}
	
	
	public String 	inferBinaryName(JavaFileManager.Location location, JavaFileObject file)
	{
		return sfm.inferBinaryName( location, file );
	}
	
	
	public boolean 	isSameFile(FileObject a, FileObject b)
	{
		throw new UnsupportedOperationException( "isSameFile()" );
	}
	
	
	public Iterable<JavaFileObject> 	list(JavaFileManager.Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) 
	{
		try
		{
			return sfm.list( location, packageName, kinds, recurse );
		}
		catch( IOException ex )
		{
			throw new RuntimeException( "couldn't list()", ex );
		}
	}
	
	
	public int 	isSupportedOption(String option) 
	{
		throw new UnsupportedOperationException( "isSupportedOption()" );
	}
	
}

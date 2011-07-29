package jsi3.util.netlayer;


import javax.tools.*;

import java.io.*;
import java.net.*;


class RAMResidentJavaFileObject extends SimpleJavaFileObject
{
	private final String programText;
	
	public RAMResidentJavaFileObject( String className, String programText ) throws URISyntaxException
	{
		super( new URI( className + ".java" ), Kind.SOURCE );
		
		this.programText = programText;
	}
	
	public CharSequence getCharContent( boolean ignoreEncodingErrors ) throws IOException
	{
		return programText;
	}
}

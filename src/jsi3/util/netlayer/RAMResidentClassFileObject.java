package jsi3.util.netlayer;


import javax.tools.*;

import java.io.*;
import java.net.*;


class RAMResidentClassFileObject extends SimpleJavaFileObject
{
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	private byte[] bytes;
	
	public byte[] getBytes()
	{
		if( bytes == null )
		{
			bytes = baos.toByteArray();
		}
		
		return bytes;
	}
	
	
	public RAMResidentClassFileObject( String className ) throws URISyntaxException
	{
		super( new URI( className + ".class" ), Kind.CLASS );
	}
	
		
	public boolean 	delete()
	{
		throw new UnsupportedOperationException( "delete()" );
	}


	public javax.lang.model.element.Modifier 	getAccessLevel()
	{
		throw new UnsupportedOperationException( "getAccessLevel()" );
	}


	public CharSequence 	getCharContent(boolean ignoreEncodingErrors)
	{
		throw new UnsupportedOperationException( "getCharContent()" );
	}


	public JavaFileObject.Kind 	getKind()
	{
		throw new UnsupportedOperationException( "getKind()" );
	}


	public long 	getLastModified()
	{
		throw new UnsupportedOperationException( "getLastModified()" );
	}


	public String 	getName()
	{
		throw new UnsupportedOperationException( "getName()" );
	}


	public javax.lang.model.element.NestingKind 	getNestingKind()
	{
		throw new UnsupportedOperationException( "getNestingKind()" );
	}


	public boolean 	isNameCompatible(String simpleName, JavaFileObject.Kind kind)
	{
		throw new UnsupportedOperationException( "isNameCompatible()" );
	}


	public InputStream 	openInputStream()
	{
		throw new UnsupportedOperationException( "openInputStream()" );
	}


	public OutputStream 	openOutputStream()
	{
		return baos;
	}


	public Reader 	openReader(boolean ignoreEncodingErrors)
	{
		throw new UnsupportedOperationException( "openReader()" );
	}


	public Writer 	openWriter()
	{
		throw new UnsupportedOperationException( "openWriter()" );
	}


	public String 	toString()
	{
		throw new UnsupportedOperationException( "toString()" );
	}


	public URI 	toUri() 
	{
		throw new UnsupportedOperationException( "toUri()" );
	}
 
}

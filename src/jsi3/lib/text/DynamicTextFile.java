package jsi3.lib.text;

import java.io.*;

import jsi3.lib.filesystem.*;

public class DynamicTextFile extends DynamicFile
{
	String text;
	
	public DynamicTextFile( String filename ) throws FileNotFoundException, IOException
	{
		super( filename );
	}
	
	public void load() throws FileNotFoundException, IOException
	{
		super.load();
		
		text = new String( data );
	}
	
	public String get_text()
	{
		return text;
	}
}

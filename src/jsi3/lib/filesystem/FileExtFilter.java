package jsi3.lib.filesystem;

import java.io.*;


public class FileExtFilter implements FileFilter
{
	private final String[] extensions;
	
	public FileExtFilter( String... extensions )
	{
		this.extensions = extensions;
	}
	
	public boolean accept( File pathname )
	{
		if( pathname.isDirectory() ) return false;
		
		for( String ext : extensions )
		{
			if( pathname.getName().endsWith( ext ) ) return true;
		}
		
		return false;
	}
}

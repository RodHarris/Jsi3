package jsi3.lib.filesystem;

import java.io.*;

import static jsi3.lib.filesystem.Statics.*;

public class DynamicFile
{
	protected byte[] data;
	
	protected long last_modified;
	
	protected File file;
	
	protected String filename;
	
	
	public DynamicFile( String filename ) throws FileNotFoundException, IOException
	{
		this.filename = filename;
		
		file = new File( filename );
		
		load();
	}
	
	
	public void load() throws FileNotFoundException, IOException
	{
		last_modified = file.lastModified();
		
		data = load_file( file );
	}
	
	
	public boolean needs_update()
	{
		return last_modified != file.lastModified();
	}
	
	
	public boolean update() throws FileNotFoundException, IOException
	{
		if( ! needs_update() ) return false;
		
		load();
		
		return true;
	}
	
	
	public byte[] get_data()
	{
		return data;
	}
}

package jsi3.lib.text;

import java.io.*;
import java.util.*;


public class DynamicTemplate extends Template
{
	DynamicTextFile skin_data;
	
	
	public DynamicTemplate( String skin_filename ) throws FileNotFoundException, IOException
	{
		super( "" );
		
		skin_data = new DynamicTextFile( skin_filename );
		
		change_format( skin_data.get_text() );
	}
	
	
	public void update() throws FileNotFoundException, IOException
	{
		if( skin_data.update() )
		{
			change_format( skin_data.get_text() );
		}
	}
}

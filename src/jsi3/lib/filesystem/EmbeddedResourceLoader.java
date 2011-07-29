package jsi3.lib.filesystem;

import java.io.*;

import static jsi3.lib.console.Statics.*;

public class EmbeddedResourceLoader
{
	private Class base_class;

	public EmbeddedResourceLoader( Object base_object )
	{
		this( base_object.getClass() );
	}


	public EmbeddedResourceLoader( Class base_class )
	{
		this.base_class = base_class;
	}


	public InputStream get_resource_stream( String file_name ) throws FileNotFoundException, IOException
	{
		cverbose.println( "get resoure: %s", file_name );
		
		InputStream in = base_class.getResourceAsStream( file_name );
		
		cverbose.println( "stream: %s", in );
		
		return in;
	}


	public byte[] load_resource( String file_name ) throws FileNotFoundException, IOException
	{
		InputStream in = get_resource_stream( file_name );

		byte[] data = new byte[ in.available() ];

		int bytes_read = in.read( data );

		return data;
	}


	public String load_text_resource( String file_name ) throws FileNotFoundException, IOException
	{
		return new String( load_resource( file_name ) );
	}
}

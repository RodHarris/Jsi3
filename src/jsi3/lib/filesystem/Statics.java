package jsi3.lib.filesystem;

import java.io.*;
import java.util.*;

import static jsi3.lib.console.Statics.*;

public class Statics
{
	public static final DirFilter dir_filter = new DirFilter();

	public static enum FileType{ directory, file, link, any };


	private static Class base_jar_class;

	/**
	*	64 meg
	*/
	public static final int MAX_FILE_SIZE = 67108864;


	private Statics(){};
	

	public static byte[] load_file( String filename ) throws FileNotFoundException, IOException
	{
		return load_file( new File( filename ) );
	}


	public static byte[] load_file( File file ) throws FileNotFoundException, IOException
	{
		FileInputStream fin = null;

		try
		{
			fin = new FileInputStream( file );

			if( fin.available() > MAX_FILE_SIZE )
			{
				throw new IOException( "file size > " + MAX_FILE_SIZE + " bytes" );
			}

			byte[] data = new byte[ fin.available() ];

			int bytes_read = fin.read( data );

			if( bytes_read != data.length )
			{
				throw new IOException( "Incomplete read of file" );
			}

			return data;
		}
		finally
		{
			if( fin != null ) fin.close();
		}
	}


	public static String load_text_file( String filename ) throws FileNotFoundException, IOException
	{
		return new String( load_file( filename ) );
	}


	public static String load_text_file( File file ) throws FileNotFoundException, IOException
	{
		return new String( load_file( file ) );
	}


	public static void write_file( File file, byte[] data ) throws FileNotFoundException, IOException
	{
		FileOutputStream out = new FileOutputStream( file );

		out.write( data );

		out.close();
	}


	public static void write_file( String filename, byte[] data ) throws FileNotFoundException, IOException
	{
		write_file( new File( filename ), data );
	}


	public static void write_text_file( File file, String data ) throws FileNotFoundException, IOException
	{
		write_file( file, data.getBytes() );
	}


	public static void write_text_file( String filename, String data ) throws FileNotFoundException, IOException
	{
		write_file( filename, data.getBytes() );
	}


	public static PrintStream write_to_file( File file ) throws FileNotFoundException
	{
		return new PrintStream( new FileOutputStream( file ), true );
	}


	public static String ext( File file )
	{
		return ext( file.getName() );
	}


	public static String ext( String file_name )
	{
		int i = file_name.lastIndexOf( "." );

		if( i == -1 ) return "";

		return file_name.substring( i + 1 );
	}


	public static String name( File file )
	{
		return name( file.getName() );
	}


	public static String name( String file_name )
	{
		cverbose.println( "filename: %s", file_name );
		
		int i0 = file_name.lastIndexOf( File.separatorChar );

		cverbose.println( "i0: %d", i0 );

		i0 ++;

		cverbose.println( "i0: %d", i0 );

		int i1 = file_name.lastIndexOf( ".", file_name.length() );

		cverbose.println( "i1: %d", i1 );

		if( i1 == -1 ) i1 = file_name.length();

		cverbose.println( "i0: %d", i1 );

		return file_name.substring( i0, i1 );
	}


	public static String name_ext( File file )
	{
		return name_ext( file.getName() );
	}


	public static String name_ext( String file_name )
	{
		int i0 = file_name.lastIndexOf( File.separatorChar );

		return file_name.substring( i0 + 1 );
	}

	public static String path( File file )
	{
		return path( file.getPath() );
	}


	public static String path( String file_name )
	{
		int i = file_name.lastIndexOf( File.separatorChar );

		if( i == -1 ) return "";

		return file_name.substring( 0, i );
	}


	public static File[] find( File dir, String... matchers )
	{
		ArrayList<File> files = new ArrayList<File>();
		
		if( ! dir.isDirectory() ) throw new IllegalArgumentException( dir + " is not a directory" );
		
		MatcherFilenameFilter filter = new MatcherFilenameFilter( matchers );

		_find( dir, filter, files );

		return files.toArray( new File[ files.size() ] );
	}


	private static void _find( File base_dir, FilenameFilter filter, ArrayList<File> files )
	{
		for( File file : base_dir.listFiles( filter ) )
		{
			files.add( file );
		}

		for( File dir : base_dir.listFiles( dir_filter ) )
		{
			_find( dir, filter, files );
		}
	}


	public static File[] find( File base_dir, FileFilter filter, boolean recurse )
	{
		ArrayList<File> files = new ArrayList<File>();

		if( ! base_dir.isDirectory() ) throw new IllegalArgumentException( base_dir + " is not a directory" );

		_find( base_dir, filter, files, recurse );

		return files.toArray( new File[ files.size() ] );
	}


	private static void _find( File base_dir, FileFilter filter, ArrayList<File> files, boolean recurse )
	{
		for( File file : base_dir.listFiles( filter ) )
		{
			files.add( file );
		}

		if( recurse )
		{
			for( File dir : base_dir.listFiles( dir_filter ) )
			{
				_find( dir, filter, files, recurse );
			}
		}
	}


	public static void delete_file( File file )
	{
		if( ! file.exists() ) return;

		file.delete();
	}


	public static File[] list( File dir, String... matchers )
	{
		MatcherFilenameFilter filter = new MatcherFilenameFilter( matchers );

		return dir.listFiles( filter );
	}

	public static File[] list( File dir, String matcher )
	{
		MatcherFilenameFilter filter = new MatcherFilenameFilter( matcher );

		return dir.listFiles( filter );
	}


	/*
	public static File get_resource( File dir, String filename_fmt, Object... args )
	{
		return new File( dir.getPath() + String.format( filename_fmt, args ) );
	}


	public static File get_resource( String dir, String filename_fmt, Object... args )
	{
		return new File( new File( dir ).getPath() + String.format( filename_fmt, args ) );
	}

	*/

	
	public static InputStream get_resource_stream( Class base_jar_class, String file_name ) throws FileNotFoundException, IOException
	{
		return base_jar_class.getResourceAsStream( file_name );
	}
	
	
	public static InputStream get_resource_stream( Object base_jar_object, String file_name ) throws FileNotFoundException, IOException
	{
		return base_jar_object.getClass().getResourceAsStream( file_name );
	}


	public static byte[] load_resource( Class base_jar_class, String file_name ) throws FileNotFoundException, IOException
	{
		InputStream in = get_resource_stream( base_jar_class, file_name );

		byte[] data = new byte[ in.available() ];

		int bytes_read = in.read( data );

		return data;
	}
	
	
	public static byte[] load_resource( Object base_jar_object, String file_name ) throws FileNotFoundException, IOException
	{
		InputStream in = get_resource_stream( base_jar_object.getClass(), file_name );

		byte[] data = new byte[ in.available() ];

		int bytes_read = in.read( data );

		return data;
	}


	public static String load_text_resource( Class base_jar_class, String file_name ) throws FileNotFoundException, IOException
	{
		byte[] data = load_resource( base_jar_class, file_name );

		return new String( data );
	}
	
	
	public static String load_text_resource( Object base_jar_object, String file_name ) throws FileNotFoundException, IOException
	{
		byte[] data = load_resource( base_jar_object.getClass(), file_name );

		return new String( data );
	}
	

	public static File next_file( String path )
	{
		String dir = path( path );

		cverbose.println( "dir: %s", dir );

		if( "".equals( dir ) ) dir = ".";

		dir += "/";

		cverbose.println( "dir: %s", dir );

		String name = name( path );

		cverbose.println( "name: %s", name );

		String ext = ext( path );

		cverbose.println( "ext: %s", ext );

		if( ! "".equals( ext ) ) ext = "." + ext;

		cverbose.println( "ext: %s", ext );

		File f = new File( path );

		cverbose.println( "file: %s", f );

		int i = 1;
		
		while( f.exists() )
		{
			cverbose.println( "exists" );
			
			i++;
			
			f = new File( dir + name + "-" + i + ext );
		}

		cverbose.println( "file: %s", f );

		return f;
	}
}



class MatcherFilenameFilter implements FilenameFilter
{
	String[] matchers;

	public MatcherFilenameFilter( String... matchers )
	{
		this.matchers = matchers;
	}

	public boolean accept( File dir, String name )
	{
		for( String matcher : matchers )
		{
			if( name.matches( matcher ) ) return true;
		}

		return false;
	}
}

package jsi3.util.configloader;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import jsi3.lib.system.*;

import static jsi3.lib.system.Statics.*;


public class ConfigLoader extends ObjectReflector
{
	private Map<String,String> config;


	/**
	*	This method will load and parse the given file(name) into [key = value] pairs with each pair on a new line (while ignoring lines starting with # or ;)
	*	then set the correspondingly names members in o (primitives and Strings only)
	*/
	public void load_object_data( Object o, String filename ) throws FileNotFoundException, NoSuchFieldException, IllegalAccessException
	{
		set_state( o, parse_file( filename ) );
	}


	/**
	*	This method will load and parse the given file(name) into [key = value] pairs with each pair on a new line (while ignoring lines starting with # or ;)
	*/
	public Map<String,String> parse_file( String filename ) throws FileNotFoundException
	{
		return parse_scanner( new Scanner( new File( filename ) ) );
	}


	/**
	*	This method will load and parse the given file(name) into [key = value] pairs with each pair on a new line (while ignoring lines starting with # or ;)
	*/
	public Map<String,String> parse_map( Map<String,String> map ) throws FileNotFoundException
	{
		config = map;
		
		return config;
	}
	

	/**
	*	This method will load and parse the given file into [key = value] pairs with each pair on a new line (while ignoring lines starting with # or ;)
	*/
	public Map<String,String> parse_file( File file ) throws FileNotFoundException
	{
		return parse_scanner( new Scanner( file ) );
	}


	/**
	*	This method will parse the given data into [key = value] pairs with each pair on a new line (while ignoring lines starting with # or ;)
	*/
	public Map<String,String> parse_data( String data )
	{
		return parse_scanner( new Scanner( data ) );
	}


	public boolean has_str( String key )
	{
		if( config == null ) throw new IllegalStateException( "no config has been parsed" );

		String value = config.get( key );

		if( value == null || "".equals( value.trim() ) ) return false;

		return true;
	}


	public boolean has_int( String key )
	{
		if( config == null ) throw new IllegalStateException( "no config has been parsed" );

		String value = config.get( key );

		if( value == null || "".equals( value.trim() ) ) return false;

		try
		{
			_int( value );
		}
		catch( NumberFormatException ex )
		{
			return false;
		}

		return true;
	}


	public boolean has_boolean( String key )
	{
		if( config == null ) throw new IllegalStateException( "no config has been parsed" );

		String value = config.get( key );

		if( value == null || "".equals( value.trim() ) ) return false;

		try
		{
 			_boolean( value );
		}
		catch( IllegalArgumentException ex )
		{
			return false;
		}

		return true;
	}


	public boolean has_double( String key )
	{
		if( config == null ) throw new IllegalStateException( "no config has been parsed" );

		String value = config.get( key );

		if( value == null || "".equals( value.trim() ) ) return false;

		try
		{
			_double( value );
		}
		catch( NumberFormatException ex )
		{
			return false;
		}

		return true;
	}


	public String get_str( String key )
	{
		return config.get( key );
	}


	public int get_int( String key )
	{
		return _int( config.get( key ) );
	}


	public boolean get_boolean( String key )
	{
		return _boolean( config.get( key ) );
	}


	public double get_double( String key )
	{
		return _double( config.get( key ) );
	}


	private Map<String,String> parse_scanner( Scanner scanner )
	{
		HashMap<String,String> map = new HashMap<String,String>();

		while( scanner.hasNextLine() )
		{
			String line = scanner.nextLine();

			if( line.startsWith( "#" ) || line.startsWith( ";" ) ) continue;

			if( line.indexOf( "=" ) == -1 ) continue;

			String[] var_val = line.split( "=", 2 );

			String var = var_val[ 0 ].trim();

			String val = var_val[ 1 ].trim();

			map.put( var, val );
		}

		config = map;

		return map;
	}
}


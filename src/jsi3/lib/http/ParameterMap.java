package jsi3.lib.http;

import java.util.*;
import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import jsi3.lib.text.*;

import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;
import static jsi3.lib.system.Statics.*;

public class ParameterMap
{
	private Map<String,String[]> map = new HashMap<String,String[]>();

	private boolean _no_case;
	
	public void no_case()
	{
		if( _no_case ) return;
		
		_no_case = true;
		
		ArrayList<String> keys = new ArrayList<String>();
		
		for( String key : map.keySet() )
		{
			keys.add( key );
		}
		
		for( String key : keys )
		{
			map.put( key.toLowerCase(), map.get( key ) );
		}
	}


	public String get( String key )
	{
		if( _no_case ) key = key.toLowerCase() ;
		
		String[] values = map.get( key );

		if( values == null || values.length == 0 ) return null;

		return values[ 0 ];
	}


	public String[] mget( String key )
	{
		if( _no_case ) key = key.toLowerCase() ;
		
		return map.get( key );
	}


	public ParameterMap( HttpServletRequest req ) throws IOException
	{
		if( req.getMethod().equals( "GET" ) )
		{
			if( req.getQueryString() != null )
			{
				String q = req.getQueryString();

				cverbose.println( "parsing GET query string %s", q );

				String[] tokens = q.split( "&" );

				for( String token : tokens )
				{
					cverbose.println( "parsing GET query token %s", token );

					int eq_index = token.indexOf( "=" );

					if( eq_index == -1 )
					{
						cwarn.println( "Token passed via HTTP GET doesn't have a key=value format" );

						continue;
					}

					String key = token.substring( 0, eq_index );

					cverbose.println( "got key: %s", key );

					String value = token.substring( eq_index + 1 );

					cverbose.println( "got value: %s", value );

					value = url2text( value );

					cverbose.println( "url decoded value: %s", value );

					map.put( key, new String[]{ value } );
				}
			}
		}
		else if( req.getMethod().equals( "POST" ) )
		{
			for( Enumeration e = req.getParameterNames(); e.hasMoreElements(); )
			{
				String key = (String) e.nextElement();

				String values[] = req.getParameterValues( key );

// 				for( int i=0; i<values.length; i++ )
// 				{
// 					values[ i ] = url2text( values[ i ] );
// 				}

				map.put( key, values );
			}
		}
		else
		{
			throw new IllegalArgumentException( "method is not GET or POST" );
		}
	}

	public String toString()
	{
		EString es = new EString();

		for( String key : map.keySet() )
		{
			if( _no_case ) key = key.toLowerCase() ;
			
			String s = sprintf( "%s = ", key );

			boolean first = true;

			es.print( s );

			for( String value : mget( key ) )
			{
				if( first )
				{
					es.println( value );

					first = false;
				}
				else
				{
					es.space( s.length() );

					es.println( value );
				}
			}
		}

		return es.toString();
	}
}

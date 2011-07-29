package jsi3.lib.http;

import java.util.*;
import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;
import static jsi3.lib.system.Statics.*;

public class QueryHandler
{
	private String[] tokens;

	public QueryHandler( HttpServletRequest req )
	{
		if( req.getMethod().equals( "GET" ) )
		{
			if( req.getQueryString() != null )
			{
				String q = req.getQueryString();

				tokens = q.split( "&" );
			}
		}
		else if( req.getMethod().equals( "POST" ) )
		{
			ArrayList<String> tokens_list = new ArrayList<String>();

			for( Enumeration e = req.getParameterNames(); e.hasMoreElements(); )
			{
				String s = (String) e.nextElement();

				cout.println( "parameter name = %s", s );

				for( String p : req.getParameterValues( s ) )
				{
					cout.println( "parameter value = %s", p );
				}

				tokens_list.add( String.format( "%s=%s", s, req.getParameter( s ) ) );
			}

			tokens = (String[]) tokens_list.toArray( new String[ tokens_list.size() ] );

			for( String token : tokens )
			{
				cout.println( "token: %s", token );
			}
		}
		else
		{
			throw new IllegalArgumentException( "method is not GET or POST" );
		}
	}

	public String get( String key ) throws IOException
	{
		if( tokens == null || tokens.length == 0 ) return null;

		String s = key + "=";

		for( String token : tokens )
		{
			if( token.startsWith( s ) )
			{
				String val = token.substring( s.length() );

				//if( val.length() == 0 ) throw new IllegalArgumentException( token + " has no value" );

				if( val.length() == 0 ) return null;

				return url2text( val );
			}
		}

		return null;
	}
}

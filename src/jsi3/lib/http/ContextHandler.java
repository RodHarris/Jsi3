package jsi3.lib.http;

import java.io.*;
import java.util.*;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.*;
import javax.servlet.*;

import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;
import static jsi3.lib.system.Statics.*;

public abstract class ContextHandler
{
	private String base_url;

	private String[] contexts;

	
	public ContextHandler( String base_url, String... contexts )
	{
		this.base_url = base_url;
		
		this.contexts = contexts;
	}


	public boolean should_handle_request( HttpServletRequest req )
	{
		cverbose.println( "num contexts = %s", contexts.length );

		for( String s : contexts )
		{
			if( s.indexOf( "*" ) == -1 )
			{
				cout.println( "checking %s for match against %s", base_url + s, req.getRequestURI() );

				if( req.getRequestURI().equals( base_url + s ) )
				{
					cverbose.println( "found" );

					return true;
				}
			}
			else
			{
				s = s.substring( 0, s.indexOf( "*" ) );

				cverbose.println( "checking %s for match against %s", base_url + s, req.getRequestURI() );

				if( req.getRequestURI().startsWith( base_url + s ) )
				{
					cverbose.println( "found" );

					return true;
				}
			}
		}

		return false;
	}


	public String get_releative_context( HttpServletRequest req )
	{
		if( ! req.getRequestURI().startsWith( base_url ) ) throw new IllegalArgumentException( "This ContextHandler is only registered to handle request URIs starting with " + base_url );

		return req.getRequestURI().substring( base_url.length() + 1 );
	}


	public abstract void handle_transaction( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException;
}

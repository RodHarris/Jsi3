package jsi3.lib.http;

import java.io.*;
import java.util.*;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.*;
import javax.servlet.*;

import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;
import static jsi3.lib.system.Statics.*;

@SuppressWarnings(value={"all"})

public class JsiHttpServlet extends HttpServlet
{
	private ArrayList<ContextHandler> context_handlers_list = new ArrayList<ContextHandler>();

	private HashMap<String,String> allowed_users = new HashMap<String,String>();

	private String secret;


	public void add_context_handler( ContextHandler handler )
	{
		context_handlers_list.add( handler );
	}


	public void set_secret( String secret )
	{
		this.secret = secret;
	}


	public void allow_user( String username, String token )
	{
		allowed_users.put( username, token );
	}


	public boolean doAuth( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
	{
		String auth = req.getHeader( "Authorization" );

		if( ! check_allow_user( auth ) )
		{
			resp.setHeader( "WWW-Authenticate", "BASIC realm=\"users\"" );

			resp.sendError( resp.SC_UNAUTHORIZED );

			return false;
		}

		return true;
	}


	public void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
	{
		for( ContextHandler context_handler : context_handlers_list )
		{
			if( context_handler.should_handle_request( req ) )
			{
				context_handler.handle_transaction( req, resp );

				return;
			}
		}

		throw new ServletException( "Not found" );
	}


// 	public void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
// 	{
// 		doGet( req, resp );
// 	}


	public boolean check_allow_user( String auth ) throws ServletException, IOException
	{
		if( auth == null ) return false;  // no auth

		if( ! auth.toUpperCase().startsWith( "BASIC " ) )

		return false;  // we only do BASIC

		// Get encoded user and password, comes after "BASIC "
		String userpassEncoded = auth.substring( 6 );

		// Decode it, using any base 64 decoder

		sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();

		String userpassDecoded = new String( dec.decodeBuffer( userpassEncoded ) );

		String[] user_pass = userpassDecoded.split( "[:]" );

		if( user_pass.length != 2 ) throw new ServletException( "Username and password not separated by a colon" );

		// Check our user list to see if that user and password are "allowed"

		try
		{
			return generate_token( user_pass[ 1 ] ).equals( allowed_users.get( user_pass[ 0 ] ) );
		}
		catch( Exception ex )
		{
			throw new ServletException( ex );
		}
	}


	public String generate_token( String pass ) throws NoSuchAlgorithmException
	{
		return md5( pass + secret );
	}
}

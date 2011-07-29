package jsi3.lib.http;

import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.*;
import javax.servlet.*;

import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;
import static jsi3.lib.system.Statics.*;

public class MethodContextHandler extends ContextHandler
{
	private  Map<String,String> mappings = new HashMap<String,String>();

	private Object target;

	private Method[] methods;


	public MethodContextHandler( String base_url, Object target )
	{
		super( base_url, "/*" );

		this.target = target;

		methods = target.getClass().getDeclaredMethods();

		AccessibleObject.setAccessible( methods, true );
	}


	public boolean should_handle_request( HttpServletRequest req )
	{
		if( ! super.should_handle_request( req ) ) return false;

		return get_target_method_by_name( get_mapped_context( req ) ) != null;
	}


	public void handle_transaction( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
	{
		cwarn.println( "number of paramaters passed to the target method has changed" );
		
		cverbose.println( "Request : %s", req );
		
		Method m = get_target_method_by_name( get_mapped_context( req ) );

		if( m == null ) throw new ServletException( "method does't exist" ); // this shouldn't ever occur if the should_handle_request method works and is called

		ParameterMap map = new ParameterMap( req );
		
		cverbose.println( "Parameters : %s", map );
		
		try
		{
			m.invoke( target, req, resp, map );
		}
		catch( IllegalAccessException ex )
		{
			ex.printStackTrace();
			
			throw new ServletException( ex );
		}
		catch( InvocationTargetException ex )
		{
			ex.printStackTrace();
			
			throw new ServletException( ex );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			
			if( ex instanceof IOException ) throw (IOException) ex;
			
			if( ex instanceof ServletException ) throw (ServletException) ex;
			
			throw new ServletException( ex );
		}
	}


	private Method get_target_method_by_name( String name )
	{
		cverbose.println( "searching methods in %s for %s", target.getClass().getName(), name );
		
		for( Method m : methods )
		{
			cverbose.println( "checking %s", m.getName() );
			
			if( m.getName().equals( name ) )
			{
				cverbose.println( "found" );
				
				return m;
			}
			
			cverbose.println( "not this one" );
		}

		cverbose.println( "not found at all" );

		return null;
	}


	private String get_mapped_context( HttpServletRequest req )
	{
		String rc = get_releative_context( req );
		
		rc = rc.replace( '-', '_' );

		cverbose.println( "searching mappings for context = %s", rc );

		for( String s : mappings.keySet() )
		{
			cverbose.println( "checking %s",s );
			
			if( rc.matches( s ) )
			{
				cverbose.println( "found: returning %s", mappings.get( s ) );
				
				return mappings.get( s );
			}

			cverbose.println( "not this one" );
		}
		
		cverbose.println( "not found at all" );

		return rc;
	}



	public void add_mapping( String mapping, String... matches )
	{
		cwarn.println( "order of paramaters in the add mapping method changed" );
		
		for( String match : matches )
		{
			cverbose.println( "mapping method %s.%s to context %s", target.getClass().getName(), mapping, match );
			
			mappings.put( match, mapping );
		}
	}
}

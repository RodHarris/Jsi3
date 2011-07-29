package jsi3.lib.system;


import java.lang.reflect.*;

import java.util.*;


public class MethodComparator implements Comparator<Object>
{
	final String method_name;
	
	final int order;
	
	public MethodComparator( String method_name )
	{
		this.method_name = method_name;
		
		this.order = 1;
	}
	
	public MethodComparator( String method_name, int order )
	{
		this.method_name = method_name;
		
		this.order = order;
	}

	public int compare( Object o1, Object o2 )
 	{
 		boolean c1_err = false;
 		
 		Comparable c1 = null;
 		
 		try
 		{
			Method m1 = o1.getClass().getMethod( method_name );
			
			c1 = (Comparable) m1.invoke( o1 );
		}
		catch( Exception ex )
		{
			handle_error( ex );
			
			c1_err = true;
		}
		
		boolean c2_err = false;
		
		Comparable c2 = null;
 		
 		try
 		{
			Method m2 = o2.getClass().getMethod( method_name );
			
			c2 = (Comparable) m2.invoke( o2 );
		}
		catch( Exception ex )
		{
			handle_error( ex );
			
			c2_err = true;
		}
		
		if( c1 == null ) c1_err = true;
		
		if( c2 == null ) c2_err = true;
		
		if( c1_err && c2_err ) return 0;
		
		if( c1_err && ! c2_err ) return 1 * order;
		
		if( c2_err && ! c1_err ) return -1 * order;
		
		return c1.compareTo( c2 ) * order;
 	}


	public boolean equals( Object obj ) 
	{
		return obj instanceof MethodComparator;
	}
	
	
	/**
	*	override this method to deal with errors
	*/
	public void handle_error( Exception ex )
	{
		ex.printStackTrace( System.err );
	}
}

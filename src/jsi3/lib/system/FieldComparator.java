package jsi3.lib.system;

import java.lang.reflect.*;

import java.util.*;


@SuppressWarnings(value={"unchecked"})
public class FieldComparator implements Comparator<Object>
{
	String field_name;
	
	public FieldComparator( String field_name )
	{
		this.field_name = field_name;
	}
	
 	public int compare( Object o1, Object o2 )
 	{
 		boolean c1_err = false;
 		
 		Comparable c1 = null;
 		
 		try
 		{
			Field f1 = o1.getClass().getField( field_name );
			
			c1 = (Comparable) f1.get( o1 );
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
			Field f2 = o2.getClass().getField( field_name );
			
			c2 = (Comparable) f2.get( o2 );
		}
		catch( Exception ex )
		{
			handle_error( ex );
			
			c2_err = true;
		}
		
		if( c1_err && c2_err ) return 0;
		
		if( c1_err && ! c2_err ) return 1;
		
		if( c2_err && ! c1_err ) return -1;
		
		return c1.compareTo( c2 );
 	}


	public boolean equals( Object obj ) 
	{
		return obj instanceof FieldComparator;
	}
	
	
	/**
	*	override this method to deal with errors
	*/
	public void handle_error( Exception ex )
	{
		ex.printStackTrace( System.err );
	}
}

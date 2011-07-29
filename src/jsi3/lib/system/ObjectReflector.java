package jsi3.lib.system;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import static jsi3.lib.system.Statics.*;

public class ObjectReflector
{
	public static enum Mode { push, pull };
	
	private Mode mode = Mode.push;
	
	
	public void set_state( Object member, Map<String, String> map )
	{
		if( mode == Mode.push )
		{
			push_data( member, map );
		}
		else if( mode == Mode.pull )
		{
			pull_data( member, map );
		}
		else
		{
			affirm( false, "ObjectReflector mode is neither push nor pull" );
		}
	}
	
	
	/**
	*	Inspects the objects members and sees if the map contains any mappings with the members name as the key - and sets the value of the member accordingly
	*/
	private void pull_data( Object member, Map<String, String> map )
	{
		Field[] fields = member.getClass().getDeclaredFields();
		
		AccessibleObject.setAccessible( fields, true );
		
		for( Field field : fields )
		{
			String val = map.get( field.getName() );
			
			if( val == null ) continue;
			
			try
			{
				set_data( member, field, val );
			}
			catch( Exception ex )
			{
				handle_pull_data_error( ex );
			}
		}
	}
	
	
	public void handle_pull_data_error( Exception ex )
	{
		ex.printStackTrace( System.err );
	}
	
	
	/**
	*	Goes through each mapping in the map and sees if the object contains any members that have the same name as the keys - and sets the value of the member accordingly
	*/
	private void push_data( Object member, Map<String, String> map )
	{
		Field[] fields = member.getClass().getDeclaredFields();
		
		AccessibleObject.setAccessible( fields, true );
		
		for( String key : map.keySet() )
		{
			try
			{
				Field field = member.getClass().getField( key );
				
				String value = map.get( key );
				
				if( value == null ) continue;

				set_data( member, field, value );
			}
			catch( Exception ex )
			{
				handle_push_data_error( ex );
			}
		}
	}
	
	
	public void handle_push_data_error( Exception ex )
	{
		ex.printStackTrace( System.err );
	}
	
	
	private void set_data( Object member, Field field, String val ) throws IllegalAccessException
	{
		Type field_type = field.getType();
		
		if( field_type == boolean.class )
		{
			field.setBoolean( member, _boolean( val ) );
		}
		else if( field_type == byte.class )
		{
			field.setByte( member, _byte( val ) );
		}
		else if( field_type == char.class )
		{
			field.setChar( member, _char( val ) );
		}
		else if( field_type == short.class )
		{
			field.setShort( member, _short( val ) );
		}
		else if( field_type == int.class )
		{
			field.setInt( member, _int( val ) );
		}
		else if( field_type == long.class )
		{
			field.setLong( member, _long( val ) );
		}
		else if( field_type == float.class )
		{
			field.setFloat( member, _float( val ) );
		}
		else if( field_type == double.class )
		{
			field.setDouble( member, _double( val ) );
		}
		else if( field_type == String.class )
		{
			field.set( member, val );
		}
		else
		{
			throw new IllegalArgumentException( "ObjectDataParser can't set data for complex types" );
		}
	}
}

package jsi3.lib.text;

import java.util.*;

import static jsi3.lib.console.Statics.*;

public class Template
{
	String txt;

	final HashMap<String, String> map = new HashMap<String, String>();

	final HashMap<String, HashMap<String,ArrayList<String>>> vmap = new HashMap<String, HashMap<String,ArrayList<String>>>();

	private static String default_it = "<$";

	private static String default_ot = ">";

	private static String default_nit = "<#";

	private String it = default_it;

	private String ot = default_ot;

	private String nit = default_nit;

	private String null_replace = "";

	private String empty_replace = "";


	/**
		construct a template from the data string
	*/
	public Template( String s )
	{
		change_format( s );
	}


	/**
		change the underlying template data string
	*/
	public void change_format( String s )
	{
		txt = s;
	}


	/**
		clear map and vmap data
	*/
	public void clear()
	{
		map.clear();

		vmap.clear();
	}

	/**
		change the tag delimiters that are looked for in the underlying template data string
	*/
	public static void change_default_tags( String it, String ot )
	{
		default_it = it;

		default_ot = ot;
	}

	/**
		change the tag delimiters that are looked for in the underlying template data string
	*/
	public static void change_default_tags( String it, String ot, String nit )
	{
		default_it = it;

		default_ot = ot;

		default_nit = nit;
	}

	/**
		change the tag delimiters that are looked for in the underlying template data string
	*/
	public void change_tags( String it, String ot )
	{
		this.it = it;

		this.ot = ot;
	}

	/**
		change the tag delimiters that are looked for in the underlying template data string
	*/
	public void change_tags( String it, String ot, String nit )
	{
		this.it = it;

		this.ot = ot;

		this.nit = nit;
	}

	/**
		change the string that null values map to
	*/
	public void set_null_replace( String null_replace )
	{
		this.null_replace = null_replace;
	}


	/**
		change the string that empty values map to
	*/
	public void set_empty_replace( String empty_replace )
	{
		this.empty_replace = empty_replace;
	}

	/**
		map the given tag to the given value
	*/
	public void map( String tag, Object value )
	{
		if( value == null )
		{
			map.put( tag, null_replace );

			return;
		}

		map.put( tag, value.toString() );
	}


	/**
		map the given tag to the given values
	*/
	public void vmap( String vtag, String tag, Object value )
	{
		HashMap<String,ArrayList<String>> cmap = vmap.get( vtag );

		if( cmap == null )
		{
			cmap = new HashMap<String,ArrayList<String>>();

			vmap.put( vtag, cmap );
		}

		ArrayList<String> values = cmap.get( tag );

		if( values == null )
		{
			values = new ArrayList<String>();

			cmap.put( tag, values );
		}

		if( value == null )
		{
			values.add( null_replace );
		}
		else
		{
			values.add( value.toString() );
		}
	}


	/**
		add the key:value pairs in this map to the current mappings
	*/
	public void map( HashMap<String, String> _map )
	{
		for( String k : _map.keySet() )
		{
			map( k, _map.get( k ) );
		}
	}


	private int cmap_length( HashMap<String,ArrayList<String>> cmap )
	{
		int length = -1;

		for( String k : cmap.keySet() )
		{
			ArrayList<String> values = cmap.get( k );

			if( length == -1 )
			{
				length = values.size();
			}
			else
			{
				if( values.size() != length ) throw new IllegalArgumentException( "Array List Values in vmap not the same length for a given key" );
			}
		}

		return length;
	}

	/**
		apply the map and vmap to the template
	*/
	public String markup()
	{
		String mtxt = txt;


		for( String k : vmap.keySet() )
		{
			String in_tag = nit + k + ot;

			String out_tag = nit + k + ot;

			while( true )
			{
				cverbose.println( "looking for vmap in-tag %s", in_tag );

				int i = mtxt.indexOf( in_tag );

				cverbose.println( "found at %d", i );

				if( i == -1 ) break;

				cverbose.println( "looking for vmap out-tag %s", out_tag );

				int i2 = mtxt.indexOf( out_tag, i + 1 );

				cverbose.println( "found at %d", i2 );

				if( i2 == -1 ) throw new IllegalArgumentException( "a vmap out tag is missing" );

				HashMap<String,ArrayList<String>> cmap = vmap.get( k );

				String s0 = mtxt.substring( 0, i );

				String s1 = mtxt.substring( i + in_tag.length(), i2 );

				String s2 = mtxt.substring( i2 + out_tag.length() );

				Template tt = new Template( s1 );

				for( int i3=0; i3<cmap_length( cmap ); i3++ )
				{
					tt.clear();

					for( String k2 : cmap.keySet() )
					{
						ArrayList<String> values = cmap.get( k2 );

						tt.map( k2, values.get( i3 ) );
					}

					s0 = s0 + tt.markup();
				}

				mtxt = s0 + s2;
			}
		}

// 		for( String k : vmap.keySet() )
// 		{
// 			String sk = nit + k + ot;
//
// 			//cverbose.println( "checking key %s", sk );
//
// 			int i = -1;
//
// 			int i2 = 0;
//
// 			while( true )
// 			{
// 				i = mtxt.indexOf( sk, i + 1 );
//
// 				if( i == -1 ) break;
//
// 				//cverbose.println( "found @ %d", i );
//
// 				i2 = mtxt.indexOf( sk, i + 1 );
//
// 				if( i2 == -1 ) break;
//
// 				//cverbose.println( "found @ %d", i2 );
//
// 				String s0 = mtxt.substring( 0, i );
//
// 				String s1 = mtxt.substring( i + sk.length(), i2 );
//
// 				String s2 = mtxt.substring( i2 + sk.length() );
//
// 				//cverbose.println( s1 );
//
// 				Object[] arr = vmap.get( k );
//
// 				int n = arr.length;
//
// 				//cverbose.println( "mapping %d objects", n );
//
// 				mtxt = s0;
//
// 				Template tt = new Template( s1 );
//
// 				for( int j=0; j<n; j++ )
// 				{
// 					tt.clear();
//
// 					Object o = arr[ j ];
//
// //					map_object( o, tt );
//
// 					mtxt += tt.markup();
// 				}
//
// 				mtxt += s2;
//
// 				i = i2;
// 			}
// 		}

		StringBuilder s = new StringBuilder( mtxt );

		for( String k : map.keySet() )
		{
			String sk = it + k + ot;

			int i = 0;

			while( true )
			{
				i = s.indexOf( sk, i );

				if( i == -1 ) break;

				String v = getValue( k );

				s.replace( i, i + sk.length(), v );
			}
		}

		return s.toString();
	}


	private String getValue( String key )
	{
		String val = map.get( key );

		if( val == null ) return null_replace;

		if( "".equals( val ) ) return empty_replace;

		return val;
	}

	/*
	private String getValue( String key, int n )
	{
		Object[] vals = map.get( key );

		if( vals == null ) return "";

		if( vals.length <= n ) return "";

		if( vals[ n ] == null ) return "";

		return vals[ n ].toString();
	}


	private String markup_part( String txt, int n )
	{
		StringBuilder s = new StringBuilder( txt );

		for( String k : map.keySet() )
		{
			String sk = it + k + ot;

			int i = 0;

			while( true )
			{
				i = s.indexOf( sk, i );

				if( i == -1 ) break;

				String v = getValue( k, n );

				s.replace( i, i + sk.length(), v );
			}
		}

		return s.toString();
	}
	*/
}

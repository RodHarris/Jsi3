package jsi3.lib.text;

import java.io.*;

import java.util.*;

import static jsi3.lib.text.Statics.*;

public class XString extends EString
{
	private int indent;

	private ArrayList<String> tag_stack = new ArrayList<String>();


	public XString()
	{
		super();
	}


	public XString( String s )
	{
		super( s );
	}


	public XString( EString s )
	{
		super( s );
	}


	public void open_tag( String tag, String... attributes )
	{
		super.print( Statics.mult( "\t", indent ) );

		super.print( "<%s", tag );
		
		for( String attribute : attributes )
		{
			super.print( " %s", attribute );
		}
		
		super.println( ">" );

		tag_stack.add( tag );

		indent ++;
	}

	public void close_tag( String tag )
	{
		String ctag = tag_stack.remove( tag_stack.size() - 1 );

		if( ! ctag.equals( tag ) ) throw new IllegalArgumentException( sprintf( "XString error: tried to close tag %s, but should have closed tag %s", tag, ctag ) );

		indent --;

		super.print( Statics.mult( "\t", indent ) );

		super.println( "</%s>", tag );
	}

	public void print_value( Object value )
	{
		if( value == null ) return;

		if( value instanceof byte[] )
		{
			value = new String( (byte[]) value );
		}

		super.print( Statics.mult( "\t", indent ) );

		super.println( escapeStringForXml( value.toString() )  );
	}

	public void print_tag_value( String tag, Object value )
	{
		super.print( Statics.mult( "\t", indent ) );

		if( value == null )
		{
			super.println( "<%s/>", tag );
		}
		else
		{
			if( value instanceof byte[] )
			{
				value = new String( (byte[]) value );
			}

			super.println( "<%s>%s</%s>", tag, escapeStringForXml( value.toString() ), tag );
		}
	}

	public boolean validate()
	{
		return indent == 0;
	}
}

package jsi3.lib.text;

import java.io.*;

import java.util.*;

import static jsi3.lib.text.Statics.*;

public class XmlBuilder
{
	private int indent;

	public final List<String> meta = new ArrayList<String>();

	private XmlNode current_node;
	
	private XmlNode root_node;
	

	public void open_element( String name )
	{
		if( current_node == null && root_node != null ) throw new IllegalStateException( "Stack error: multiple root nodes" );
		
		current_node = new XmlNode( current_node, name );
	}
	
	public void add_attribute( String name, Object value )
	{
		if( current_node == null ) throw new IllegalStateException( "Stack error: No current node to add an attribute to" );
		
		current_node.add_attribute( name, value );
	}
	
	public void set_value( Object value )
	{
		if( current_node == null ) throw new IllegalStateException( "Stack error: No current node to set the value of" );
		
		current_node.set_value( value );
	}
	
	public void close_element( String name )
	{
		if( current_node == null ) throw new IllegalStateException( "Stack error: No element to close" );
		
		if( ! current_node.name.equals( name ) ) throw new IllegalStateException( "Stack error: Closing element with wrong name" );
		
		if( current_node.parent == null )
		{
			root_node = current_node;
		}
		else
		{
			current_node.parent.children.add( current_node );
		}
		
		current_node = current_node.parent;
	}
	
	
	public String toString()
	{
		EString es = new EString();
		
		for( String s : meta )
		{
			es.println( s );
		}
		
		es.print( print_node( root_node ).trim() );
		
		return es.toString();
	}
	
	
	private String print_node( XmlNode node )
	{
		if( node == null ) return "";
		
		EString es = new EString();
		
		es.print( "\n<%s", node.name );
		
		for( Attribute attr : node.attributes )
		{
			es.print( " " + attr );
		}
		
		if( node.value == null && node.children.size() == 0 )
		{
			es.print( "/>" );
			
			return es.toString();
		}

		es.print( ">" );
		
		if( node.value == null )
		{
			//es.println();
		}
		else
		{
			es.print( node.value.toString() );
		}
		
		for( XmlNode child : node.children )
		{
			es.print( print_node( child ) );
		}
		
		if( node.children.size() > 0 )
		{
			es.print( "\n</%s>", node.name );
		}
		else
		{
			es.print( "</%s>", node.name );
		}
		
		return es.toString();
	}
	
/*
	public void open_tag( String tag, String... params )
	{
		
		es.print( Statics.mult( "\t", indent ) );
		
		super.print( "<%s", tag );

		for( String param : params )
		{
			super.print( " " + param );
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
	*/
}


class XmlNode
{
	final String name;
	
	final XmlNode parent;
	
	final List<XmlNode> children = new ArrayList<XmlNode>();
	
	final List<Attribute> attributes = new ArrayList<Attribute>();
	
	Object value;
	
	public XmlNode( XmlNode parent, String name )
	{
		this.parent = parent;
		
		this.name = name;
	}
	
	void add_child( XmlNode child )
	{
		children.add( child );
	}
	
	void add_attribute( String name, Object value )
	{
		attributes.add( new Attribute( name, value ) );
	}
	
	void set_value( Object value )
	{
		this.value = value;
	}
}


class Attribute
{
	final String name;
	
	final Object value;
	
	Attribute( String name, Object value )
	{
		this.name = name;
		
		this.value = value;
	}
	
	public String toString()
	{
		Object _value = value;
		
		if( _value == null ) _value = "";
		
		return fmt( "%s=\"%s\"", name, _value.toString() );
	}
}

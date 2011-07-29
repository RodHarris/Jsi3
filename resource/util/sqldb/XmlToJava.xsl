<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">

<xsl:output method="text" indent="yes" encoding="UTF-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>

<xsl:template match="/class">
package <xsl:value-of select="package"/>;


import static jsi3.lib.system.Statics.call_cstyle_getter;
import static jsi3.lib.system.Statics.call_cstyle_setter;

import jsi3.util.sqldb.*;

import java.sql.*;
import java.util.*;


public class <xsl:value-of select="name"/> extends DBObject
{
	private static boolean _distinct_gets = false;
	
	private boolean _track_sets = true;
	
	
	public <xsl:value-of select="name"/>()
	{
		_set_name( "<xsl:value-of select="tname"/>" );
		
		_set_lname( "<xsl:value-of select="lname"/>" );
	}
	
	
	public int getId()
	{
		return super.getId();
	}


	public void setId( int id )
	{
		super.setId( id );
	}


	public int get_id()
	{
		return super.get_id();
	}


	public void set_id( int id )
	{
		super.set_id( id );
	}


	public void _track_sets( boolean _track_sets )
	{
		this._track_sets = _track_sets;
	}
	
	
	public static void _distinct_gets( boolean distinct_gets )
	{
		_distinct_gets = distinct_gets;
	}
	
<xsl:for-each select="fields/field">
	private <xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="cname"/>;
	
	
	private boolean _update_<xsl:value-of select="cname"/> ;
	
	
<!--
	public <xsl:value-of select="type"/> get<xsl:value-of select="bname"/>()
	{
		return <xsl:value-of select="cname"/>;
	}
-->


<!--
	public void set<xsl:value-of select="bname"/>( <xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="cname"/> )
	{
		this.<xsl:value-of select="cname"/> = <xsl:value-of select="cname"/>;
		
		if( this._track_sets ) this._update_<xsl:value-of select="cname"/> = true;
	}
-->


	public <xsl:value-of select="type"/> get_<xsl:value-of select="cname"/>()
	{
		return <xsl:value-of select="cname"/>;
	}


	public void set_<xsl:value-of select="cname"/>( <xsl:value-of select="type"/><xsl:text> </xsl:text><xsl:value-of select="cname"/> )
	{
		this.<xsl:value-of select="cname"/> = <xsl:value-of select="cname"/>;
		
		if( this._track_sets ) this._update_<xsl:value-of select="cname"/> = true;
	}

</xsl:for-each>

	public static <xsl:value-of select="name"/> get_row( int id ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return (<xsl:value-of select="name"/>) SqlDatabase.db.get_object( <xsl:value-of select="name"/>.class, id );
	}


	public static <xsl:value-of select="name"/> get_row( String where_fmt, Object... where_args ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return (<xsl:value-of select="name"/>) SqlDatabase.db.get_object( <xsl:value-of select="name"/>.class, where_fmt, where_args );
	}


	public static <xsl:value-of select="name"/>[] get_rows() throws InstantiationException, SQLException, IllegalAccessException
	{
		ArrayList&lt;DBObject&gt; dbo_list = SqlDatabase.db.get_objects( <xsl:value-of select="name"/>.class, _distinct_gets );
		
		return (<xsl:value-of select="name"/>[]) dbo_list.toArray( new <xsl:value-of select="name"/>[ dbo_list.size() ] );
	}


	public static <xsl:value-of select="name"/>[] get_rows( String where_fmt, Object... where_args ) throws InstantiationException, SQLException, IllegalAccessException
	{
		ArrayList&lt;DBObject&gt; dbo_list = SqlDatabase.db.get_objects( <xsl:value-of select="name"/>.class, _distinct_gets, where_fmt, where_args );
		
		return (<xsl:value-of select="name"/>[]) dbo_list.toArray( new <xsl:value-of select="name"/>[ dbo_list.size() ] );
	}


	public void update_row() throws SQLException
	{
		String update_fields = "";
<xsl:for-each select="fields/field">
		if( _update_<xsl:value-of select="cname"/> ) update_fields += "<xsl:value-of select="cname"/> ";
</xsl:for-each>
		SqlDatabase.db.update_object( this, update_fields );
<xsl:for-each select="fields/field">
		_update_<xsl:value-of select="cname"/> = false;
</xsl:for-each>
	}


	public void insert_row() throws SQLException
	{
		SqlDatabase.db.insert_object( this );
	}


	public static <xsl:value-of select="name"/> get_row( DBObject dbo ) throws Exception
	{
		int id = (Integer) call_cstyle_getter( dbo, "<xsl:value-of select="lname"/>_id" );
		
		return get_row( id );
	}
	
	
	public static <xsl:value-of select="name"/>[] get_rows( DBObject dbo ) throws Exception
	{
		return get_rows( dbo._get_lname() + "_id = ?", dbo.get_id() );
	}
	
	
	public void add( DBObject... dbos ) throws Exception
	{
		if( get_id() == 0 ) throw new Exception( "Can't call add on an object that hasn't yet been stored in the database" );
		
		for( DBObject dbo : dbos )
		{
			call_cstyle_setter( dbo, "<xsl:value-of select="lname"/>_id", get_id() );
		}
	}
	
	
	public void link( DBObject... dbos ) throws Exception
	{
		for( DBObject dbo : dbos )
		{
			dbo.add( this );
		}
	}
	
	
	public static <xsl:value-of select="name"/> retrieve( int id ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return get_row( id );
	}
	
	
	public static <xsl:value-of select="name"/> retrieve( String where_fmt, Object... where_args ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return get_row( where_fmt, where_args );
	}


	public static <xsl:value-of select="name"/> retrieve( DBObject dbo ) throws Exception
	{
		return get_row( dbo );
	}
	
	
	public static <xsl:value-of select="name"/>[] retrieve_all() throws InstantiationException, SQLException, IllegalAccessException
	{
		return get_rows();
	}


	public static <xsl:value-of select="name"/>[] retrieve_all( String where_fmt, Object... where_args ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return get_rows( where_fmt, where_args );
	}
	

	public static <xsl:value-of select="name"/>[] retrieve_all( DBObject dbo ) throws Exception
	{
		return get_rows( dbo );
	}

	
	public void store() throws Exception
	{
		if( get_id() == 0 )
		{
			insert_row();
		}
		else
		{
			update_row();
		}
	}
}



</xsl:template>

</xsl:stylesheet>

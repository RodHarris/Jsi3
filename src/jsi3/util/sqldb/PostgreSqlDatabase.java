package jsi3.util.sqldb;

import java.util.*;
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;

import javax.naming.*;

import static jsi3.lib.system.Statics.inspect;
import static jsi3.lib.system.Statics.ex_to_string;
import static jsi3.lib.text.Statics.sprintf;
import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;

/**
	This class implements database specific connection, query, insert and update methods
*/
public class PostgreSqlDatabase extends SqlDatabase
{
	public static final String DEFAULT_DB_DRIVER = "org.postgresql.Driver";

	public static final String DEFAULT_HOST = "localhost";

	public static final int DEFAULT_PORT = 5432;


	public PostgreSqlDatabase()
	{
		super( DEFAULT_DB_DRIVER );
	}


	public PostgreSqlDatabase( String db_driver )
	{
		super( db_driver );
	}


	public String format_db_url( String db_name )
	{
		return format_db_url( null, null, db_name );
	}


	public String format_db_url( String hostname, String db_name )
	{
		return format_db_url( hostname, null, db_name );
	}


	public String format_db_url( String hostname, Integer port, String db_name )
	{
		if( hostname == null ) hostname = DEFAULT_HOST;

		if( port == null ) port = DEFAULT_PORT;

		return sprintf( "jdbc:postgresql://%s:%d/%s", hostname, port, db_name );
	}


	public int sequence_value( String table_name ) throws SQLException
	{
		String sql = "SELECT currval( ? );";

		PreparedStatement stmt = get_statement( sql );

		stmt.setObject( 1, sprintf( "\"%s_id_seq\"", table_name ) );

		ResultSet rs = stmt.executeQuery();

		rs.next();

		int id = rs.getInt( 1 );

		return id;
	}


	public ArrayList<String> list_table_names() throws SQLException
	{
		return list_table_names( "public" );
	}
	

	public ArrayList<String> list_table_names( String schema ) throws SQLException
	{
		ArrayList<String> table_names = new ArrayList<String>();

		ResultSet rs = execute_query( fmt( "SELECT table_name FROM INFORMATION_SCHEMA.TABLES where table_schema = '%s';", schema ) );

		while( rs.next() )
		{
			String table_name = rs.getString( 1 );

			table_names.add( table_name );
		}

		return table_names;
	}


}

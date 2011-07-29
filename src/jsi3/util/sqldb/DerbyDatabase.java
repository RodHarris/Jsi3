package jsi3.util.sqldb;

import java.util.*;
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;

import javax.naming.*;

import static jsi3.lib.system.Statics.*;
import static jsi3.lib.text.Statics.*;
import static jsi3.lib.console.Statics.*;

/**
	This class implements database specific connection, query, insert and update methods
*/
public class DerbyDatabase extends SqlDatabase
{
	public static final String DEFAULT_DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

	private String db_name;

	public DerbyDatabase()
	{
		super( DEFAULT_DB_DRIVER );
	}

	public DerbyDatabase( String db_driver )
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
		this.db_name = db_name;

		return sprintf( "jdbc:derby:%s;", db_name );
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


	public ArrayList<String> list_table_names( String schema ) throws SQLException
	{
		not_implemented();
		
		return null;
	}
	
	
	public ArrayList<String> list_table_names() throws SQLException
	{
		try
		{
			ArrayList<String> table_names = new ArrayList<String>();

			ByteArrayInputStream sqlIn = new ByteArrayInputStream( "SHOW TABLES IN APP;".getBytes() );

			String inputEncoding = "UTF-8";

			ByteArrayOutputStream sqlOut = new ByteArrayOutputStream();

			String outputEncoding = "UTF-8";

			org.apache.derby.tools.ij.runScript( dbcon, sqlIn, inputEncoding, sqlOut, outputEncoding );

			String[] lines = sqlOut.toString( outputEncoding ).split( "\n" );

			for( int i=3; i<lines.length-1; i++ )
			{
				String[] tokens = lines[ i ].split( "\\s+" );

				cout.println( lines[ i ] );

				table_names.add( tokens[ 1 ].substring( 1 ).trim() );
			}

			return table_names;
		}
		catch( Exception ex )
		{
			throw new SQLException( ex );
		}

		//return null;
	}
}

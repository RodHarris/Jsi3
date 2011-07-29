package jsi3.util.sqldb;

import java.io.*;
import java.util.*;
import java.sql.*;

import jsi3.lib.text.*;
import jsi3.lib.filesystem.*;
import jsi3.lib.mainopt.*;

import static jsi3.lib.filesystem.Statics.*;
import static jsi3.lib.text.Statics.*;
import static jsi3.lib.system.Statics.*;
import static jsi3.lib.console.Statics.*;


/**
 * Class used to generate ActiveRecord classes from a database schema
 */
public class ActiveRecordClassGenerator extends MainOpt
{
	static SqlDatabase sql_db;

	static boolean depluralise = false;

	static String package_name;


	public static void main( String[] args ) throws Exception
	{
		set( args );
		
		start();
	}

	public static void start() throws Exception
	{
		if( has_flag( "--help", "-h", "--usage", "-u", "-?" ) ) usage( 0 );

		if( has_flag( "--debug", "-d" ) ) cdebug.on();

		if( has_flag( "--verbose", "-v" ) )
		{
			cdebug.on();

			cverbose.on();
		}

		if( has_flag( "--gen-classes", "-g" ) ) gen_classes();
	}


	public static void usage( int exit_code )
	{
		System.err.println( "usage: ActiveRecordClassGenerator [options]" );

		System.err.println( "   --help|-h|--usage|-u|-?    :  prints cli usage" );

		System.err.println( "   --gen-classes|-g           :  generate java class descriptions" );

		System.err.println( "   --postgres : connect to a postgres database" );

		System.err.println( "   --derby : connect to a derby database" );

		System.err.println( "   Database connection options" );

		System.err.println( "      --db [database name]" );

		System.err.println( "      --user [database user]" );

		System.err.println( "      --pass [database user passwd]" );

		System.err.println( "      --write : write generated classes to disk" );

		System.err.println( "      --depluralise : attempt to make sensible java class names from table names" );

		System.err.println( "      --package : the package to put the generated classes into" );

		System.err.println( "    Postgres database options" );

		System.err.println( "      --host [host] : defaults to localhost if this paramater is not specified" );

		System.err.println( "      --port | -p [port] : defaults to 5432 if this paramater is not specified" );

		System.exit( exit_code );
	}


	public static void gen_classes() throws Exception
	{
		String host = get_param( "--host" );

		String port_arg = get_param( "--port", "-p" );

		Integer port = null;

		String db_name = get_param( "--db" );

		String db_url = null;

		if( has_flag( "--postgres" ) )
		{
			sql_db = new PostgreSqlDatabase();
		}
		else if( has_flag( "--derby" ) )
		{
			sql_db = new DerbyDatabase();
		}
		else
		{
			throw new IllegalArgumentException( "no database type specified" );
		}

		affirm( db_name != null, "no db name specified" );

		if( port_arg != null )
		{
			port = _int( port_arg );
		}

		db_url = sql_db.format_db_url( host, port, db_name );

		String db_user = get_param( "--user" );

		String db_pass = get_param( "--pass" );

		affirm( db_user != null, "no db user specified" );

		affirm( db_pass != null, "no db pass specified" );


		//cverbose.println( "type: %s", type );
		cverbose.println( "name: %s", db_name );
		cverbose.println( "host: %s", host );
		cverbose.println( "port: %d", port );
		cverbose.println( "user: %s", db_user );
		cverbose.println( "pass: %s", db_pass );



		sql_db.connect( db_url, db_user, db_pass );

		ArrayList<String> table_names = sql_db.list_table_names();

		boolean write = has_flag( "--write", "-w" );

		depluralise = has_flag( "--depluralise" );

		package_name = get_param( "--package" );

		for( String table_name : table_names )
		{
			cdebug.println( "%s => %s.java", table_name, tablename_to_classname( table_name ) );

			if( ! write ) continue;

			make_class( table_name );
		}
	}


	static void make_class( String table_name ) throws SQLException, FileNotFoundException, IOException, javax.xml.transform.TransformerConfigurationException, javax.xml.transform.TransformerException
	{
		String class_name = tablename_to_classname( table_name );
		
		ResultSet rs = sql_db.execute_query( "SELECT * FROM \"%s\" LIMIT 0", table_name );

		ResultSetMetaData rsmd = rs.getMetaData();
		
		XString xml = new XString();
		
		xml.open_tag( "class" );

		if( package_name != null )
		{
			xml.print_tag_value( "package", package_name );
		}
		
		xml.print_tag_value( "name", class_name );
		
		xml.print_tag_value( "tname", table_name );
		
		xml.print_tag_value( "lname", table_name.toLowerCase() );

		xml.open_tag( "fields" );
		
		for( int i=0; i<rsmd.getColumnCount(); i++ )
		{
			String type = rsmd.getColumnClassName( i + 1 );

			String name = rsmd.getColumnName( i + 1 );
			
			if( "id".equals( name ) ) continue;
			
			xml.open_tag( "field" );
			
			xml.print_tag_value( "type", type );
			
			xml.print_tag_value( "cname", name );
			
			xml.print_tag_value( "bname", capitalise( name ) );
			
			xml.close_tag( "field" );
		}
		
		xml.close_tag( "fields" );
		
		xml.close_tag( "class" );
		
		cdebug.println( "\n%s", xml );
		
		String xmltojava_xsl = load_text_resource( ActiveRecordClassGenerator.class, "XmlToJava.xsl" );

		String java = xslt( xml, xmltojava_xsl );
		
		write_file( new File( class_name + ".java" ), java.getBytes() );
	}
	
	
	static void make_class2( String table_name ) throws SQLException, FileNotFoundException, IOException
	{
		String class_name = tablename_to_classname( table_name );

		String singular_name = tablename_to_singular( table_name );

		EString es = new EString();

		if( package_name != null )
		{
			es.println( "package %s;", package_name );
			es.println();
		}
		es.println( "import jsi.util.sqldb.*;" );
		es.println();
		es.println( "import java.sql.*;" );
		es.println();
		es.println( "import java.util.*;" );
		es.println();
		es.println( "public class %s extends DBObject", class_name );
		es.println( "{" );
		es.println( "\tpublic static final String TABLE_NAME = \"%s\";", table_name );
		es.println();
		es.println( "\tpublic static final String SINGULAR_NAME = \"%s\";", singular_name );
		es.println();
		es.println( "\tprivate static boolean _distinct_gets = false;" );
		es.println();

		//ResultSet rs = sql_db.execute_query( "SELECT * FROM \"%s\" LIMIT 1", table_name );

		ResultSet rs = sql_db.execute_query( "SELECT * FROM \"%s\" LIMIT 0", table_name );

		ResultSetMetaData rsmd = rs.getMetaData();

		for( int i=1; i<=rsmd.getColumnCount(); i++ )
		{
			String type = rsmd.getColumnClassName( i );

			String name = rsmd.getColumnName( i );

			if( name.equals( "id" ) ) continue;
/*			{
				type = "int";
			}
			else*/
			{
				type = column_class_to_java_type( type );
			}

			es.println( "\tprivate %s %s;", type, name );

			es.println( "\tprivate boolean _update_%s;", name );

			es.println();
		}

		es.println( "\tpublic String db_table_name()" );
		es.println( "\t{" );
		es.println( "\t\treturn TABLE_NAME;" );
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic String db_singular_name()" );
		es.println( "\t{" );
		es.println( "\t\treturn SINGULAR_NAME;" );
		es.println( "\t}" );
		es.println();

		for( int i=1; i<=rsmd.getColumnCount(); i++ )
		{
			String type = rsmd.getColumnClassName( i );

			String name = rsmd.getColumnName( i );

			if( name.equals( "id" ) ) continue;
// 			{
// 				type = "int";
// 			}
// 			else
			{
				type = column_class_to_java_type( type );
			}

			es.println( "\tpublic %s get%s()", type, capitalise( name ) );
			es.println( "\t{");
			es.println( "\t\treturn %s;", name );
			es.println( "\t}");
			es.println();
			es.println( "\tpublic void set%s( %s %s )", capitalise( name ), type, name );
			es.println( "\t{");
			es.println( "\t\tthis.%s = %s;", name, name );
			es.println( "\t\tif( _track_sets() ) this._update_%s = true;", name );
			es.println( "\t}");
			es.println();
			es.println( "\tpublic %s get_%s()", type, name );
			es.println( "\t{");
			es.println( "\t\treturn %s;", name );
			es.println( "\t}");
			es.println();
			es.println( "\tpublic void set_%s( %s %s )", name, type, name );
			es.println( "\t{");
			es.println( "\t\tthis.%s = %s;", name, name );
			es.println( "\t\tif( _track_sets() ) this._update_%s = true;", name );
			es.println( "\t}");
			es.println();
			es.println();


			if( name.equals( "name" ) )
			{
				es.println( "\tpublic String toString()" );
				es.println( "\t{");
				es.println( "\t\treturn %s;", name );
				es.println( "\t}");
				es.println();
				es.println();
			}


// 			if( name.equals( "id" ) )
// 			{
// 				es.println( "\tpublic <T> int compareTo( T other )" );
// 				es.println( "\t{");
// 				es.println( "\t\treturn %s;", name );
// 				es.println( "\t}");
// 				es.println();
// 				es.println();
// 			}
		}

		es.println( "\tpublic static %s get_row( int id ) throws InstantiationException, SQLException, IllegalAccessException", class_name );
		es.println( "\t{" );
		es.println( "\t\treturn (%s) SqlDatabase.db.get_object( %s.class, id );", class_name, class_name );
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic static %s get_row( String where_fmt, Object... where_args ) throws InstantiationException, SQLException, IllegalAccessException", class_name );
		es.println( "\t{" );
		es.println( "\t\treturn (%s) SqlDatabase.db.get_object( %s.class, where_fmt, where_args );", class_name, class_name );
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic static %s[] get_rows() throws InstantiationException, SQLException, IllegalAccessException", class_name );
		es.println( "\t{" );
		es.println( "\t\tArrayList<DBObject> dbo_list = SqlDatabase.db.get_objects( %s.class, _distinct_gets );", class_name );
		es.println( "\t\treturn (%s[]) dbo_list.toArray( new %s[ dbo_list.size() ] );", class_name, class_name );
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic static %s[] get_rows( String where_fmt, Object... where_args ) throws InstantiationException, SQLException, IllegalAccessException", class_name );
		es.println( "\t{" );
		es.println( "\t\tArrayList<DBObject> dbo_list = SqlDatabase.db.get_objects( %s.class, _distinct_gets, where_fmt, where_args );", class_name );
		es.println( "\t\treturn (%s[]) dbo_list.toArray( new %s[ dbo_list.size() ] );", class_name, class_name );
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic void update_row() throws SQLException", class_name );
		es.println( "\t{" );
		es.println( "\t\tString update_fields = \"\";" );
		for( int i=1; i<=rsmd.getColumnCount(); i++ )
		{
			String name = rsmd.getColumnName( i );
			
			if( name.equals( "id" ) ) continue;

			es.println( "\t\tif( _update_%s ) update_fields += \"%s \";", name, name );
		}
		es.println( "\t\tSqlDatabase.db.update_object( this, update_fields );" );
		for( int i=1; i<=rsmd.getColumnCount(); i++ )
		{
			String name = rsmd.getColumnName( i );
			
			if( name.equals( "id" ) ) continue;

			es.println( "\t\t_update_%s = false;", name );
		}
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic void insert_row() throws SQLException", class_name );
		es.println( "\t{" );
		es.println( "\t\tSqlDatabase.db.insert_object( this );" );
		es.println( "\t}" );
		es.println();
		
		es.println( "\tpublic int get_id()" );
		es.println( "\t{" );
		es.println( "\t\treturn super.get_id();" );
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic void set_id( int id )" );
		es.println( "\t{" );
		es.println( "\t\tsuper.set_id( id );" );
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic int getId()" );
		es.println( "\t{" );
		es.println( "\t\treturn super.getId();" );
		es.println( "\t}" );
		es.println();

		es.println( "\tpublic void setId( int id )" );
		es.println( "\t{" );
		es.println( "\t\tsuper.setId( id );" );
		es.println( "\t}" );
		es.println();

		
		
		es.println( "\tpublic static void _distinct_gets( boolean distinct_gets )" );
		es.println( "\t{" );
		es.println( "\t\t_distinct_gets = distinct_gets;" );
		es.println( "\t}" );
		es.println();
		
		
		es.println( "\tpublic static %s get_row( DBObject dbo ) throws InstantiationException, SQLException, IllegalAccessException", class_name );
		es.println( "\t{" );
		es.println( "\t\treturn get_row( dbo.get_%s_id() );", class_name.toLowerCase() );
		es.println( "\t}" );
		es.println();
		
		es.println( "}" );

		//cout.println( es );

		write_file( new File( class_name + ".java" ), es.toString().getBytes() );
	}


	static String column_class_to_java_type( final String type )
	{
		return type;

// 		if( type.equals( "java.lang.Integer" ) ) return "Integer";
//
// 		if( type.equals( "java.lang.Double" ) ) return "Double";
//
// 		if( type.equals( "java.lang.Boolean" ) ) return "Boolean";
//
// 		if( type.equals( "java.lang.String" ) ) return "String";
//
// 		if( type.equals( "java.sql.Timestamp" ) ) return "java.util.Date";
//
// 		if( type.equals( "java.sql.Date" ) ) return "java.util.Date";
//
// 		fatal( new Exception( "unknown type: " + type ) );
//
// 		return null;
	}


	static String tablename_to_classname( String table_name )
	{
		String[] parts = table_name.split( "_" );

		String class_name = "";

		for( int i=0; i<parts.length; i++ )
		{
			parts[ i ] = depluralise( parts[ i ] );

			parts[ i ] = capitalise( parts[ i ] );

			class_name += parts[ i ];
		}

		return class_name;
	}


	static String tablename_to_singular( String table_name )
	{
		String[] parts = table_name.split( "_" );

		String singular_name = "";

		for( int i=0; i<parts.length; i++ )
		{
			parts[ i ] = depluralise( parts[ i ] );

			singular_name += parts[ i ];

			if( i < parts.length-1 ) singular_name += "_";
		}

		return singular_name;
	}


	static String capitalise( String s )
	{
		char c = s.charAt( 0 );

		char C = Character.toUpperCase( c );

		return C +  s.substring( 1 );
	}


	static String depluralise( String s )
	{
		if( ! depluralise ) return s;

		if( s.endsWith( "ies" ) ) s = s.substring( 0, s.length() - 3 ) + "y";

		if( s.endsWith( "statuses" ) )
		{
			s = s.substring( 0, s.length() - 2 );
		}
		else if( s.endsWith( "s" ) )
		{
			s = s.substring( 0, s.length() - 1 );
		}

		return s;
	}
}


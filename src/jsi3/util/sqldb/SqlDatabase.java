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
//import static jsi3.lib.system.Statics.ex_to_string;
import static jsi3.lib.text.Statics.sprintf;
import static jsi3.lib.text.Statics.fmt;
import static jsi3.lib.console.Statics.*;

import jsi3.util.configloader.*;

/**

	* <p> This class implements database specific connection, query, insert and update methods
	* <p> Major TODO for this class - it caches the last created SqlDatabase object as a static variable in this class
	* <p> Which will break if multiple databases are accessed by the same application
	* <p> Example:
	<pre>
	*   Map&lt;String,String&gt; conf = new HashMap&lt;String,String&gt;();
	* 
	* 	conf.put( "db.dev.type", "postgres" );
	* 	conf.put( "db.dev.name", "db" );
	* 	conf.put( "db.dev.host", "localhost" );
	* 	conf.put( "db.dev.port", "5432" );
	* 	conf.put( "db.dev.user", "usr" );
	* 	conf.put( "db.dev.pass", "passwd" );
	*
	* 	String env = "dev";
	* 
	* 	SqlDatabase sqldb = SqlDatabase.start_active_record( conf, env );
	* 
	</pre>
*/
public abstract class SqlDatabase
{
	Connection dbcon;

	private String db_driver;

	private final HashMap<String, PreparedStatement> statements = new HashMap<String, PreparedStatement>();

	public boolean do_inserts = true;

	public static SqlDatabase db;

	
	/**
	 * Parses the db_conf string into key=value pairs (one pair per line)
	 * and passes this as a map to the start_active_record mehtod that takes the map
	 */
	public static SqlDatabase start_active_record( String db_conf, String env ) throws Exception
	{
		ConfigLoader conf = new ConfigLoader();
		
		conf.parse_data( db_conf );
		
		return start_active_record( conf, env );
	}
	
	
	/**
	 * Looks for the following keys in the map 
	 * <pre>
	 * db.[env].type
	 * db.[env].name
	 * db.[env].host
	 * db.[env].port
	 * db.[env].user
	 * db.[env].pass
	 * </pre>
	 * If found it calls the start_active_record method with these values
	 */
	public static SqlDatabase start_active_record( Map<String,String> db_conf, String env ) throws Exception
	{
		ConfigLoader conf = new ConfigLoader();
		
		conf.parse_map( db_conf );
		
		return start_active_record( conf, env );
	}
	

	private static SqlDatabase start_active_record( ConfigLoader conf, String env ) throws Exception
	{
		SqlDatabase db = null;
	
		String type_key = fmt( "db.%s.type", env );
		String name_key = fmt( "db.%s.name", env );
		String host_key = fmt( "db.%s.host", env );
		String port_key = fmt( "db.%s.port", env );
		String user_key = fmt( "db.%s.user", env );
		String pass_key = fmt( "db.%s.pass", env );
		
		check( conf.has_str( type_key ), "No database type in db.conf for environment: " + env );
		check( conf.has_str( name_key ), "No database name in db.conf for environment: " + env );
		check( conf.has_str( host_key ), "No database host in db.conf for environment: " + env );
		check( conf.has_int( port_key ), "No database port in db.conf for environment: " + env );
		check( conf.has_str( user_key ), "No database user in db.conf for environment: " + env );
		check( conf.has_str( pass_key ), "No database pass in db.conf for environment: " + env );
		
		String type = conf.get_str( type_key );
		String name = conf.get_str( name_key );
		String host = conf.get_str( host_key );
		int    port = conf.get_int( port_key );
		String user = conf.get_str( user_key );
		String pass = conf.get_str( pass_key );
		
		return start_active_record( type, name, host, port, user, pass );
	}
	
	
	public static SqlDatabase start_active_record( String type, String name, String host, int port, String user, String pass ) throws Exception
	{
		if( "postgres".equals( type ) )
		{
			db = new PostgreSqlDatabase();
		}
		else if( "derby".equals( type ) )
		{
			db = new DerbyDatabase();
		}
		else
		{
			throw new Exception( fmt( "database type (%s) unknown", type ) );
		}
		
		cverbose.println( "type: %s", type );
		cverbose.println( "name: %s", name );
		cverbose.println( "host: %s", host );
		cverbose.println( "port: %d", port );
		cverbose.println( "user: %s", user );
		cverbose.println( "pass: %s", pass );
		
		assert db != null;
		
		String url = db.format_db_url( host, port, name );
		
		db.connect( url, user, pass );
		
		return db;
	}



	protected SqlDatabase( String db_driver )
	{
		this.db_driver = db_driver;

		db = this;
	}


	public void use_driver( String db_driver )
	{
		this.db_driver = db_driver;
	}


	public abstract String format_db_url( String db_name );

	public abstract String format_db_url( String hostname, String db_name );

	public abstract String format_db_url( String hostname, Integer port, String db_name );


	public void connect( String db_url, String db_userid, String db_passwd ) throws ClassNotFoundException, SQLException
	{
		Class.forName( db_driver );

		dbcon = DriverManager.getConnection( db_url, db_userid, db_passwd );

		statements.clear();
	}


	/*
		Standard query
	*/
	public void print_execute_query( String query, Object... args ) throws SQLException
	{
		cinfo.println( "Query: %s", query );
		
		ResultSet rs = execute_query( query, args );
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		String head = "\t";
		
		for( int c=0; c<rsmd.getColumnCount(); c++ )
		{
			head += rsmd.getColumnName( c + 1 ) + ", ";
		}
		
		cinfo.println( head );
		
		cinfo.println( "Rows:" );
		
		int result_count = 0;
		
		while( rs.next() )
		{
			String res = "\t";
			
			for( int c=0; c<rsmd.getColumnCount(); c++ )
			{
				res += rs.getString( c + 1 ) + ", ";
			}
			
			cinfo.println( res );
			
			result_count ++;
		}
		
		cinfo.println( "%d Results", result_count );
	}


	public ResultSet execute_query( String query_fmt, Object... args ) throws SQLException
	{
		String query = String.format( query_fmt, args );

		Statement stmt = dbcon.createStatement();

		cverbose.println( "Query: %s", query );

		ResultSet rs = stmt.executeQuery( query );

		//rs.next();

		return rs;
	}

	
	public int execute_update( String update_fmt, Object... args ) throws SQLException
	{
		String update = String.format( update_fmt, args );

		Statement stmt = dbcon.createStatement();

		cverbose.println( "Query: %s", update );

		int i = stmt.executeUpdate( update );

		//rs.next();

		return i;
	}

	/*
		load rows of tables into objects
	*/

	public DBObject get_object( Class table ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return get_object( table, null );
	}


	public DBObject get_object( DBObject dbo ) throws  SQLException, IllegalAccessException
	{
		return get_object( dbo, null );
	}


	public DBObject get_object( Class table, int id ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return get_object( table, "id = ?", id );
	}


	public DBObject get_object( DBObject dbo, int id ) throws SQLException, IllegalAccessException
	{
		return get_object( dbo, "id = ?", id );
	}


	public DBObject get_object( Class table, String conditions_fmt, Object... args ) throws InstantiationException, SQLException, IllegalAccessException
	{
		Object o = table.newInstance();

		if( ! ( o instanceof DBObject ) ) throw new IllegalArgumentException( "The class representing the table type must extend the DBObject class" );

		DBObject dbo = (DBObject) o;

		return get_object( dbo, conditions_fmt, args );
	}


	/**
	 * Verify the database classes are a correct mapping of the database schema
	 * 
	 * @return true (never false - throws SchemaMismatchException instead)
	 */
	public boolean check_schema( Class... tables ) throws InstantiationException, SQLException, IllegalAccessException, SchemaMismatchException
	{
		for( Class table : tables )
		{
			Object o = table.newInstance();
			
			if( ! ( o instanceof DBObject ) ) throw new IllegalArgumentException( "The class representing the table type must extend the DBObject class" );
			
			DBObject dbo = (DBObject) o;

			check_object( dbo );
		}
		
		return true;
	}
	
	/**
	 * Verify the database classes are a correct mapping of the database schema
	 * 
	 * @return true (never false - throws SchemaMismatchException instead)
	 */
	public boolean check_object( DBObject... dbos ) throws InstantiationException, SQLException, IllegalAccessException, SchemaMismatchException
	{
		for( DBObject dbo : dbos )
		{
			ResultSet rs = execute_query( "SELECT * FROM \"%s\" LIMIT 0", dbo._get_name() );

			ResultSetMetaData rsmd = rs.getMetaData();
			
			SQLGhostObjectHandler.check_state( dbo, rs );
		}
		
		return true;
	}
	

	public DBObject get_object( DBObject dbo, String conditions_fmt, Object... args ) throws SQLException, IllegalAccessException
	{
		String get_query = sprintf( "SELECT * FROM \"%s\"", dbo._get_name() );

		if( conditions_fmt != null )
		{
			get_query += " WHERE " + conditions_fmt;
		}

		//cverbose.println( get_query );

		PreparedStatement stmt = null;

		ResultSet rs = null;

		stmt = get_statement( get_query );

		set_statement_params( stmt, args );

		cverbose.println( stmt );

		rs = stmt.executeQuery();

		dbo._track_sets( false );

		boolean found = SQLGhostObjectHandler.set_state( dbo, rs );

		dbo._track_sets( true );

		//cverbose.println( inspect( dbo ) );

		if( found ) return dbo;
		
		return null;
	}


	public ArrayList<DBObject> get_objects( Class table ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return get_objects( table, false, null );
	}
	
	public ArrayList<DBObject> get_objects( Class table, boolean distinct ) throws InstantiationException, SQLException, IllegalAccessException
	{
		return get_objects( table, distinct, null );
	}


	public ArrayList<DBObject> get_objects( Class table, boolean distinct, String conditions_fmt, Object... args ) throws InstantiationException, SQLException, IllegalAccessException
	{
		ArrayList<DBObject> dbo_list = new ArrayList<DBObject>();
		
		DBObject dbo = null;

		String get_query = null;

		PreparedStatement stmt = null;

		int n = 0;

		ResultSet rs = null;

		Object o = table.newInstance();

		if( ! ( o instanceof DBObject ) ) throw new IllegalArgumentException( "The class representing the table type must extend the DBObject class" );

		dbo = (DBObject) o;
		
		String distinct_str = "";
		
		if( distinct ) distinct_str = "DISTINCT";
		
		get_query = sprintf( "SELECT %s * FROM \"%s\"", distinct_str, dbo._get_name() );

		if( conditions_fmt != null )
		{
			get_query += " WHERE " + conditions_fmt;
		}

		stmt = get_statement( get_query );
		
		cverbose.println( stmt );
		
		set_statement_params( stmt, args );

		cverbose.println( stmt );

		rs = stmt.executeQuery();
		
		while( true )
		{
			if( dbo == null ) dbo = (DBObject) table.newInstance();
			
			dbo = (DBObject) table.newInstance();

			dbo._track_sets( false );

			if( ! SQLGhostObjectHandler.set_state( dbo, rs ) ) break;
			
			cverbose.println( inspect( dbo ) );

			dbo._track_sets( true );
			
			dbo_list.add( dbo );
			
			dbo = null;
		}

		return dbo_list;
	}


	/**
		Create a new table row from an object
		@param o the object containing the data to insert
	*/
	public void insert_object( DBObject o ) throws SQLException
	{
		try
		{
			String sql = "INSERT INTO \"%s\" (%s) VALUES (%s);";

			PreparedStatement stmt = null;

			dbcon.setAutoCommit( false );

			HashMap<String,Object> state = SQLGhostObjectHandler.get_state( o );

			String col_names = "";

			String val_qmarks = "";

			ArrayList<Object> state_values = new ArrayList<Object>();

			for( String key : state.keySet() )
			{
				Object value = state.get( key );

				if( key.equals( "id" ) ) continue;
				
				if( value == null ) continue; // let the default values defined in the database override null values

				col_names += "\"" + key + "\",";

				val_qmarks += "?,";

				state_values.add( value );
			}

			if( col_names.endsWith( "," ) ) col_names = col_names.substring( 0, col_names.length() -1 );

			if( val_qmarks.endsWith( "," ) ) val_qmarks = val_qmarks.substring( 0, val_qmarks.length() -1 );

			sql = sprintf( sql, o._get_name(), col_names, val_qmarks );

			stmt = get_statement( sql );

			int i = 0;

			for( Object val : state_values )
			{
				i++;

				stmt.setObject( i, val );
			}

			cverbose.println( stmt );

			if( do_inserts )
			{
				stmt.executeUpdate();

/*
				int id = sequence_value( o._get_name() );

				cverbose.println( "inserted row id=%d into table %s", id, o._get_name() );
*/

				dbcon.commit();

				o._track_sets( false );

//				o.setId( id );

				o._track_sets( true );
			}
		}
		finally
		{
			try
			{
				dbcon.setAutoCommit( true );
			}
			catch( Exception ex )
			{
			}
		}
	}


	public abstract ArrayList<String> list_table_names() throws SQLException;
	
	public abstract ArrayList<String> list_table_names( String schema ) throws SQLException;

	public abstract int sequence_value( String table_name ) throws SQLException;


	/**
	Update a table row from an object
	@param o Object containing the database
	@param fields names of the fields to update in the db table row [space separated]
	*/
	public void update_object( DBObject o, String fields ) throws SQLException
	{
		update_object( o, fields.split( "\\s+" ) );
	}

	/**
	Update a table row from an object
	@param o Object containing the database
	@param fields names of the fields to update in the db table row
	*/
	public void update_object( DBObject o, String... fields ) throws SQLException
	{
		String sql = "UPDATE \"%s\" SET %s WHERE id=?;";

		PreparedStatement stmt = null;

		try
		{
			dbcon.setAutoCommit( false );

			String sets = "";

			for( String field : fields )
			{
				if( field.equals( "id" ) ) continue;
				
				sets += "\"" + field + "\"=?,";
			}

			if( sets.endsWith( "," ) ) sets = sets.substring( 0, sets.length() -1 );

			sql = sprintf( sql, o._get_name(), sets );

			stmt = get_statement( sql );
			
			int i = 0;
			
			for( String field : fields )
			{
				if( field.equals( "id" ) ) continue;
				
				i++;
				
				Object value = SQLGhostObjectHandler.get( o, field );
				
				stmt.setObject( i, value );
			}

			i++;

			stmt.setObject( i, o.getId() );

			cverbose.println( stmt );

			stmt.executeUpdate();

			dbcon.commit();
		}
		finally
		{
			try
			{
				dbcon.setAutoCommit( true );
			}
			catch( Exception ex )
			{
			}
		}
	}


	/*
		Internal SQL Helpers
	*/

	PreparedStatement get_statement( String query ) throws SQLException
	{
		PreparedStatement stmt = statements.get( query );

		if( stmt == null )
		{
			stmt = dbcon.prepareStatement( query );

			statements.put( query, stmt );
		}

		stmt.clearParameters();
		
		return stmt;
	}


	void set_statement_params( PreparedStatement stmt, Object... args ) throws SQLException
	{
		stmt.clearParameters();
		
		for( int i=0; i<args.length; i++ )
		{
			cverbose.println( "statement param %d = %s", i+1, args[ i ] );
			
			stmt.setObject( i + 1, args[ i ] );
		}
	}
}


/*
	Class to help with introspection
*/
class SQLGhostObjectHandler
{
	// cache fields associated with a class for (hopefully) quicker access


	static final HashMap<Class, Field[]> members = new HashMap<Class, Field[]>();

	static final HashMap<Class, Method[]> functions = new HashMap<Class, Method[]>();


	static boolean check_state( DBObject o, ResultSet rs ) throws SQLException, SchemaMismatchException
	{
		String table_name = o._get_name();
		
		String class_name = o.getClass().getName();
		
		ResultSetMetaData rsmd = rs.getMetaData();

		for( int i=0; i<rsmd.getColumnCount(); i++ )
		{
			String field_name = rsmd.getColumnName( i + 1 );

			Method setter = null;
			
			Method getter = null;

			for( Method m : get_methods( o ) )
			{
				//cverbose.println( "checking set_%s against %s", field_name, m.getName() );
				
				if( m.getName().equals( "set_" + field_name ) )
				{
					setter = m;

					if( getter != null ) break;
					
					continue;
				}
				
				//cverbose.println( "checking get_%s against %s", field_name, m.getName() );

				if( m.getName().equals( "get_" + field_name ) )
				{
					getter = m;

					if( setter != null ) break;
					
					continue;
				}
			}

			if( setter == null ) throw new SchemaMismatchException( fmt( "database table %s has field %s but class %s has no method set_%s", table_name, field_name, class_name, field_name ) );
			
			if( getter == null ) throw new SchemaMismatchException( fmt( "database table %s has field %s but class %s has no method get_%s", table_name, field_name, class_name, field_name ) );
			
			if( "id".equals( field_name ) ) continue;
			
			String field_type = null;
			
			for( Field f : get_fields( o ) )
			{
				cverbose.println( "Checking field: %s", f );
				
				if( f.getName().equals( field_name ) )
				{
					field_type = f.getType().getCanonicalName();

					break;
				}
			}
			
			if( field_type == null ) throw new SchemaMismatchException( fmt( "database table %s has field %s but class %s has no field %s", table_name, field_name, class_name, field_name ) );
			
			String db_field_type = rsmd.getColumnClassName( i + 1 );
			
			if( ! field_type.equals( db_field_type ) ) throw new SchemaMismatchException( fmt( "database table %s has field %s type %s but class %s has field %s type %s", table_name, field_name, db_field_type, class_name, field_name, field_type ) );
		}
		
		return true;
	}
	

	static boolean set_state( DBObject o, ResultSet rs ) throws SQLException
	{
		if( ! rs.next() ) return false;
		
		ResultSetMetaData rsmd = rs.getMetaData();

		for( int i=0; i<rsmd.getColumnCount(); i++ )
		{
			String field_name = rsmd.getColumnName( i + 1 );

			try
			{
				Method setter = null;

				for( Method m : get_methods( o ) )
				{
					if( m.getName().equals( "set_" + field_name ) )
					{
						setter = m;

						break;
					}
				}

				if( setter == null ) throw new NoSuchMethodException( "coudln't find set_" + field_name + " method in class " +  o.getClass().getName() );

				Object data = rs.getObject( field_name );
				
				cverbose.println( "calling %s.%s( %s )", o.getClass().getName(), setter.getName(), data );

				setter.invoke( o, data );
			}
			catch( NoSuchMethodException ex )
			{
				cerr.println( "no setter found for %s.%s", o.getClass().getName(), field_name );

				cerr.println( ex_to_string( ex ) );

				continue;
			}
			catch( IllegalAccessException ex )
			{
				cerr.println( "error calling setter for %s.%s", o.getClass().getName(), field_name );

				cerr.println( ex_to_string( ex ) );

				continue;
			}
			catch( InvocationTargetException ex )
			{
				cerr.println( "error calling setter for %s.%s", o.getClass().getName(), field_name );

				cerr.println( ex_to_string( ex ) );

				continue;
			}
		}

		return true;
	}

	
	static Object get( DBObject o, String field_name )
	{
		try
		{
			Method[] methods = get_methods( o );
			
			Method getter = null;

			for( Method m : methods )
			{
				if( m.getName().equals( "get_" + field_name ) )
				{
					getter = m;

					break;
				}
			}

			if( getter == null ) throw new NoSuchMethodException( "coudln't find get_" + field_name + " method in class " +  o.getClass().getName() );

			//cdebug.println( inspect( getter ) );

			Object data = getter.invoke( o );
			
			return data;
		}
		catch( NoSuchMethodException ex )
		{
			cerr.println( "no getter found for %s.%s", o.getClass().getName(), field_name );

			cerr.println( ex_to_string( ex ) );
		}
		catch( IllegalAccessException ex )
		{
			cerr.println( "error calling getter for %s.%s", o.getClass().getName(), field_name );

			cerr.println( ex_to_string( ex ) );
		}
		catch( InvocationTargetException ex )
		{
			cerr.println( "error calling getter for %s.%s", o.getClass().getName(), field_name );

			cerr.println( ex_to_string( ex ) );
		}
		
		return null;
	}
	


	static HashMap<String,Object> get_state( DBObject o )
	{
		Field[] fields = get_fields( o );
		
		Method[] methods = get_methods( o );

		HashMap<String,Object> state = new HashMap<String,Object>();

		for( Field field : fields )
		{
			if( Modifier.isStatic( field.getModifiers() ) ) continue;

			String field_name = field.getName();

			if( field_name.startsWith( "_" ) ) continue;

			cverbose.println( "Field found: %s.%s", o.getClass().getName(), field.getName() );

			try
			{
				Method getter = null;

				for( Method m : methods )
				{
					if( m.getName().equals( "get_" + field_name ) )
					{
						getter = m;

						break;
					}
				}

				if( getter == null ) throw new NoSuchMethodException( "coudln't find get_" + field_name + " method in class " +  o.getClass().getName() );

				//cdebug.println( inspect( getter ) );

				Object data = getter.invoke( o );

				state.put( field_name, data );
			}
			catch( NoSuchMethodException ex )
			{
				cerr.println( "no getter found for %s.%s", o.getClass().getName(), field_name );

				cerr.println( ex_to_string( ex ) );

				continue;
			}
			catch( IllegalAccessException ex )
			{
				cerr.println( "error calling getter for %s.%s", o.getClass().getName(), field_name );

				cerr.println( ex_to_string( ex ) );

				continue;
			}
			catch( InvocationTargetException ex )
			{
				cerr.println( "error calling getter for %s.%s", o.getClass().getName(), field_name );

				cerr.println( ex_to_string( ex ) );

				continue;
			}
		}

		return state;
	}


	private static Method[] get_methods( DBObject o )
	{
		return get_methods( o.getClass() );
	}


	private static Method[] get_methods( Class c )
	{
		Method[] methods = functions.get( c );

		// lazy initialisation of the methods arrays

		if( methods == null )
		{
			methods = add_type_methods( c );
		}

		return methods;
	}


	private static Method[] add_type_methods( Class c )
	{
		//Method[] methods = c.getMethods();
		
		Method[] methods = c.getDeclaredMethods();
		
		for( Method method : methods ) cverbose.println( "Adding method: %s", method.getName() );

		AccessibleObject.setAccessible( methods, true );

		functions.put( c, methods );

		return methods;
	}



	private static Field[] get_fields( DBObject o )
	{
		return get_fields( o.getClass() );
	}


	private static Field[] get_fields( Class c )
	{
		Field[] fields = members.get( c );

		// lazy initialisation of the fields arrays

		if( fields == null )
		{
			fields = add_type_fields( c );
		}

		return fields;
	}


	private static Field[] add_type_fields( Class c )
	{
		Field[] fields = c.getDeclaredFields();
		
		for( Field field : fields ) cverbose.println( "Adding field: %s", field.getName() );

		AccessibleObject.setAccessible( fields, true );

		members.put( c, fields );

		return fields;
	}
}

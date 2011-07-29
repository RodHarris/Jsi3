package jsi3.lib.system;


import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.beans.*;
import java.net.*;
import java.util.*;
import java.math.*;
import java.nio.*;
import java.security.*;

import jsi3.lib.text.*;

import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;


public class Statics
{
	public static final String PROP_NL = "line.separator";

	public static final String PROP_USER_HOME = "user.home";

	public static final String PROP_USER_NAME = "user.name";

	public static final String PROP_OS_NAME = "os.name";

	public static final String PROP_USER_DIR= "user.dir";

	public static final String PROP_TMP_DIR= "java.io.tmpdir";


	public static final String FILE_SEPARATOR = File.separator;

	public static final String PATH_SEPARATOR = File.pathSeparator;


	public static enum OS_Types
	{
		linux, windows, mac
	}

	public static final OS_Types OS = determin_OS_Type();

	//public static final int DEFAULT_TRACE_DEPTH = get_default_trace_depth();


	public static long systime()
	{
		return System.currentTimeMillis();
	}

	/**
	sets to OS variable after querying the system properties
	*/
	private static OS_Types determin_OS_Type()
	{
		if( os_name().startsWith( "Linux" ) ) return OS_Types.linux;

		if( os_name().startsWith( "Windows" ) ) return OS_Types.windows;

		if( os_name().startsWith( "Mac" ) ) return OS_Types.mac;

		return null;
	}


	/**
	is the operating system a Linux variant
	*/
	public static boolean linux()
	{
		return OS == OS_Types.linux;
	}


	/**
	is the operating system a Windows variant
	*/
	public static boolean windows()
	{
		return OS == OS_Types.windows;
	}


	/**
	is the operating system a Mac variant
	*/
	public static boolean mac()
	{
		return OS == OS_Types.mac;
	}


	public static void exit()
	{
		exit( 0 );
	}

	/**
	Stops the JRE
	*/
	public static void exit( int code )
	{
		System.exit( code );
	}

	/**
	get the specified system property
	*/
	public static String get_property( String prop )
	{
		return java.lang.System.getProperty( prop );
	}


	/**
	get the user name as specified in the system properties
	*/
	public static String user_name()
	{
		return get_property( PROP_USER_NAME );
	}


	/**
	get the OS name as specified in the system properties
	*/
	public static String os_name()
	{
		return get_property( PROP_OS_NAME );
	}


	/**
	get the path to the directory that the JRE was started in
	*/
	public static String exec_dir_path()
	{
		return get_property( PROP_USER_DIR );
	}


	/**
	get the directory that the JRE was started in
	*/
	public static File exec_dir()
	{
		return new File( exec_dir_path() );
	}


	/**
	get the path to the users home directory
	*/
	public static String home_dir_path()
	{
		return get_property( PROP_USER_HOME );
	}


	/**
	get the users home directory
	*/
	public static File home_dir()
	{
		return new File( home_dir_path() );
	}


	/**
	get the path to the tmp directory
	*/
	public static String tmp_dir_path()
	{
		return get_property( PROP_TMP_DIR );
	}


	/**
	get the tmp directory
	*/
	public static File tmp_dir()
	{
		return new File( tmp_dir_path() );
	}


	//----------------------------------    Sercurity


	public static String md5( String... s ) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance( "MD5" );

		for( String d : s )
		{
			md.update( d.getBytes(), 0, d.length() );
		}

		return String.format( "%032x", new BigInteger( 1, md.digest() ) );

	}


	//----------------------------------    LANG

	public static String ex_to_string( Throwable ex  )
	{
		return _ex_to_string( ex );
	}

/*

	public static String _ex_to_string( Throwable ex )
	{
		if( ex == null ) return null;

		String s = "";
		
		String cs = _ex_to_string( ex.getCause() );

		if( cs != null )
		{
			s += cs + "\nWhich Caused\n";
		}

		EString es = new EString();

		ex.printStackTrace( new java.io.PrintStream( es ) );

		return s + es.toString();
	}
*/

	public static String _ex_to_string( Throwable ex )
	{
		if( ex == null ) return null;
		
		EString es = new EString();
		
		Throwable cause = ex.getCause();
		
		if( cause == null )
		{
			es.println( "%s: %s", ex.getClass().getName(), ex.getMessage() );
			
			for( StackTraceElement ste : ex.getStackTrace() )
			{
				es.println( "\tat " + ste );
			}
		}
		else
		{
			es.println( _ex_to_string( cause ) );
			
			es.println( "\tWhich Caused: (%s) %s", ex.getClass().getName(), ex.getMessage() );
		}
		
		return es.toString();
	}


	public static boolean is_null( Object o )
	{
		return o == null;
	}


	public static boolean is_eq( Object o1, Object o2 )
	{
		if( o1 == null ) return false;
		
		if( o2 == null ) return false;
		
		if( o1 == o2 ) return true;
		
		if( o1.equals( o2 ) ) return true;
		
		return false;
	}
	
	

// 	public static void throw_exception( Class ex_class, String msg, Object... args )
// 	{
//
// 	}


	//---------------------------------    COLLECTIONS


	/**
	*	sort the given array based on objects returned by the given method for each element of that array
	*/
	public static <T> T[] sort( T[] array, String method )
	{
		Arrays.sort( array, new MethodComparator( method ) );
		
		return array;
	}
	
	
	/**
	*	reverse sort the given array based on objects returned by the given method for each element of that array
	*/
	public static <T> T[] rsort( T[] array, String method )
	{
		Arrays.sort( array, new MethodComparator( method, -1 ) );
		
		return array;
	}

	/**
	*	trturns true if there is any overlay in the 2 sets
	*/
	public static boolean in_list( Object[] args1, Object... args2 )
	{
		for( Object o1 : args1 )
		{
			for( Object o2 : args2 )
			{
				//cverbose.println( "checking %s = %s", o1, o2 );

				if( o1.equals( o2 ) ) return true;
			}
		}

		return false;
	}


	/**
	*	returns a new array that has objs added to the end
	*/
	public static <T> T[] extend( T[] arr, T... objs )
	{
		if( arr == null || arr.length == 0 ) return objs;

		if( objs == null || objs.length == 0 ) return arr;

		//		For Java 1.6

		T[] new_arr = Arrays.copyOf( arr, arr.length + objs.length );

		java.lang.System.arraycopy( objs, 0, new_arr, arr.length, objs.length );

		//		For Java 1.5
		//
		// 		T[] new_arr = ( T[] ) Array.newInstance( arr[ 0 ].getClass(), arr.length + objs.length );
		//
		// 		System.arraycopy( arr, 0, new_arr, 0, arr.length );
		//
		// 		System.arraycopy( objs, 0, new_arr, arr.length, objs.length );

		return new_arr;
	}


	// inspection
	/**
	*	print out the key:value pairs in the map
	*/
	public static <K, V> String inspect( Map<K,V> map )
	{
		EString ret = new EString();

		ret.println( "size: %d", map.size() );

		for( K k : map.keySet() )
		{
			ret.println( "%s = %s", k, map.get( k ) );
		}

		return ret.toString().trim();
	}

	/**
	*	print out the contents of the array
	*/
	public static <T> String inspect_arr( T[] arr )
	{
		EString ret = new EString();

		for( T t : arr )
		{
			ret.print( "%s, ", t );
		}

		return ret.toString().trim();
	}


	/**
	*	returns true if the array is null or its length is zero
	*/
	public static boolean arrnull( Object[] arr )
	{
		return arr == null || arr.length == 0;
	}


	// system

	public static void fatal( Exception ex )
	{
		ex.printStackTrace();

		exit( -1 );
	}


	public static void fatal()
	{
		Thread.currentThread().dumpStack();

		exit( -1 );
	}


	public static void nonfatal( Exception ex )
	{
		ex.printStackTrace();
	}


	public static void nonfatal()
	{
		Thread.currentThread().dumpStack();
	}


	public static void not_implemented()
	{
		fatal();
	}
	

	/**
	*	my version of assert (fatal if false)
	*/
	public static void affirm( boolean b, String s, Object... args )
	{
		if( b ) return;

		cerr.println( "Affirmation failed: %s", String.format( s, args ) );

		Thread.currentThread().dumpStack();

		exit( -1 );
	}


	/**
	*	throws an exception if false but doesn't exit the jvm
	*/
	public static void check( boolean b, String msg, Object... args )
	{
		if( b ) return;

		throw new RuntimeException( String.format( msg, args ) );
	}


	/**
	*	easy way of throwing common exceptions using the String.format way of creating messages
	*/
	public static void throw_io( String msg_fmt, Object... msg_args ) throws IOException
	{
		throw new IOException( String.format( msg_fmt, msg_args ) );
	}
	
	/**
	*	easy way of throwing common exceptions using the String.format way of creating messages
	*/
	public static void throw_illegal_argument( String msg_fmt, Object... msg_args )
	{
		throw new IllegalArgumentException( String.format( msg_fmt, msg_args ) );
	}

	/**
	*	easy way of throwing common exceptions using the String.format way of creating messages
	*/
	public static void throw_illegal_state( String msg_fmt, Object... msg_args )
	{
		throw new IllegalStateException( String.format( msg_fmt, msg_args ) );
	}
	
	/**
	*	easy way of throwing common exceptions using the String.format way of creating messages
	*/
	public static void throw_runtime( String msg_fmt, Object... msg_args )
	{
		throw new RuntimeException( String.format( msg_fmt, msg_args ) );
	}


	/**
	*	easy way of throwing exceptions using the String.format way of creating messages
	*/
	public static void raise( String msg_fmt, Object... msg_args ) throws Exception
	{
		throw new Exception( String.format( msg_fmt, msg_args ) );
	}
	

	// native


	public static ByteBuffer byte_buffer( int capacity )
	{
		ByteBuffer byte_buffer = ByteBuffer.allocateDirect( capacity );

		byte_buffer.order( ByteOrder.nativeOrder() );

		return byte_buffer;
	}


	public static FloatBuffer float_buffer( int capacity )
	{
		return byte_buffer( capacity ).asFloatBuffer();
	}


	public static ProcessResults exec( String cmd, String... args ) throws IOException, InterruptedException
	{
		String[] cmd_args = new String[ args.length + 1 ];

		cmd_args[ 0 ] = cmd;

		int i = 0;

		for( String s : args )
		{
			i++;

			cmd_args[ i ] = s;
		}

		Exec exec = new Exec( cmd_args );

		cverbose.println( "executing command: %s", exec );

		return exec.run();
	}


	public static ProcessResults run( Exec exec ) throws IOException, InterruptedException, NativeException
	{
		cdebug.println( exec );

		ProcessResults pr = exec.run();

		cdebug.println( pr );

		if( pr.exit_code != 0 ) throw new NativeException( pr );

		return pr;
	}


	public static ProcessResults runtime_exec( String cmd_fmt, Object... args ) throws IOException, InterruptedException
	{
		String cmd = String.format( cmd_fmt, args );

		cverbose.println( "Runtime.exec( %s )", cmd );

		Process p = Runtime.getRuntime().exec( cmd );

		return handle_process( p );
	}


	public static ProcessResults handle_process( Process p )// throws IOException, InterruptedException
	{
		ProcessStreamReader process_stream_reader = new ProcessStreamReader( p );

		Thread process_stream_reader_thread = new Thread( process_stream_reader );

		process_stream_reader_thread.start();

		cverbose.println( "Waiting for process to complete" );

		ProcessResults pr = new ProcessResults();

		while( true )
		{
			try
			{
				try
				{
					p.waitFor();
				}
				catch( InterruptedException ex )
				{
					cerr.println( ex );
				}

				pr.exit_code = p.exitValue();

				cverbose.println( "Process completed [%d]", pr.exit_code );

				break;
			}
			catch( IllegalThreadStateException ex )
			{
				cerr.println( ex );
			}
		}

		process_stream_reader.set_finished( true );

		process_stream_reader.read_streams();

		pr.std_out = process_stream_reader.get_stdout();

		pr.std_err = process_stream_reader.get_stderr();

		return pr;
	}


	static class ProcessStreamReader implements Runnable
	{
		private boolean finished;

		private BufferedInputStream pout;

		private BufferedInputStream perr;

		private EString std_out = new EString();

		private EString std_err = new EString();


		ProcessStreamReader( Process p )
		{
			pout = new BufferedInputStream( p.getInputStream() );

			perr = new BufferedInputStream( p.getErrorStream() );
		}

		void set_finished( boolean finished )
		{
			this.finished = finished;
		}

		public void run()
		{
			while( ! finished )
			{
				try
				{
					Thread.currentThread().sleep( 50 );
				}
				catch( InterruptedException ex )
				{
					cerr.println( ex );
				}

				read_streams();
			}
		}

		void read_streams()
		{
			try
			{
				std_out.read_from( pout );

				std_err.read_from( perr );
			}
			catch( IOException ex )
			{
				cerr.println( ex );
			}
		}

		String get_stdout()
		{
			return std_out.toString().trim();
		}

		String get_stderr()
		{
			return std_err.toString().trim();
		}
	}

	// -------------------------------    LANG

	public static boolean _boolean( String a )
	{
		if( "true".equals( a ) ) return true;

		if( "false".equals( a ) ) return false;

		throw new IllegalArgumentException( a + " cannot be parsed as a boolean" );
	}


	public static boolean _boolean( long l )
	{
		if( l == 0 ) return false;

		return true;
	}


	public static boolean _boolean( double d )
	{
		if( d == 0 ) return false;

		return true;
	}


	public static boolean _boolean( Object o )
	{
		if( o == null ) return false;

		return true;
	}


	public static char _char( String a )
	{
		return a.charAt( 0 );
	}


	public static byte _byte( String a )
	{
		return ( byte ) _long( a );
	}


	public static short _short( String a )
	{
		return ( short ) _long( a );
	}


	public static int _int( String a )
	{
		return ( int ) _long( a );
	}


	public static int _int( double d )
	{
		return ( int ) d;
	}


	public static long _long( String a )
	{
		a = a.trim();
		
		if( a.toLowerCase().startsWith( "0x" ) )
		{
			a = a.substring( 2 );

			return new BigInteger( a, 16 ).longValue();
		}
		else if( a.startsWith( "#" ) )
		{
			a = a.substring( 1 );

			return new BigInteger( a, 16 ).longValue();
		}

		return new BigInteger( a ).longValue();
	}


	public static float _float( String a )
	{
		return ( float ) _double( a );
	}


	public static double _double( String a )
	{
		return new BigDecimal( a ).doubleValue();
	}


	public static String _string( Object o )
	{
		return o.toString();
	}


	public static int[] _ints( String s )
	{
		String[] vals = s.split( "," );

		int[] ints = new int[ vals.length ];
		
		for( int i=0; i<vals.length; i++ )
		{
			ints[ i ] = _int( vals[ i ].trim() );
		}

		return ints;
	}

	public static int[] _ints( String[] vals )
	{
		int[] ints = new int[ vals.length ];
		
		for( int i=0; i<vals.length; i++ )
		{
			ints[ i ] = _int( vals[ i ].trim() );
		}

		return ints;
	}

	public static double[] _doubles( String s )
	{
		String[] vals = s.split( "," );

		double[] doubles = new double[ vals.length ];
		
		for( int i=0; i<vals.length; i++ )
		{
			doubles[ i ] = _double( vals[ i ].trim() );
		}

		return doubles;
	}


	public static String[] _strings( String s )
	{
		String[] vals = s.split( "," );

		for( int i=0; i<vals.length; i++ )
		{
			vals[ i ] = vals[ i ].trim();
		}

		return vals;
	}



	public static Integer _Integer( String s )
	{
		try
		{
			return _int( s );
		}
		catch( Exception ex )
		{
			cerr.println( String.format( "couldn't parse '%s' as an Integer", s ) );
			
			cerr.println( ex );
		}
		
		return null;
	}
	
	
	public static Double _Double( String s )
	{
		try
		{
			return _double( s );
		}
		catch( Exception ex )
		{
			cerr.println( String.format( "couldn't parse '%s' as a Double", s ) );
			
			cerr.println( ex );
		}
		
		return null;
	}
	
	/**
	<br>%c - class name
	<br>%m - method name
	<br>%f - file name
	<br>%l - line number
	*/
	public static String trace_call( int level, String fmt )
	{
		//StackTraceElement[] es = new Throwable().getStackTrace();

		StackTraceElement[] es = Thread.currentThread().getStackTrace();

		if( es.length > level )
		{
			StackTraceElement ste = es[ level ];

			fmt = fmt.replace( "%c", ste.getClassName() );

			fmt = fmt.replace( "%m", ste.getMethodName() );

			fmt = fmt.replace( "%f", ste.getFileName() );

			fmt = fmt.replace( "%l", Integer.toString( ste.getLineNumber() ) );

			return fmt;
		}

		return "[no trace]";
	}


	/**
	<br>%c - class name
	<br>%m - method name
	<br>%f - file name
	<br>%l - line number
	*/
	public static String mtrace( Object... args )
	{
		StackTraceElement ste = Thread.currentThread().getStackTrace()[ 2 ];

		String arglist = "";

		for( Object o : args )
		{
			arglist += o + ", ";
		}

		if( args.length > 0 ) arglist = arglist.substring( 0, arglist.length() - 2 );

		return String.format( "%s.%s( %s )", ste.getClassName(), ste.getMethodName(), arglist );
	}


	public static String inspect( Object o )
	{
		if( o == null ) return "null";

		return inspect( o, o.getClass(), false );
	}


	public static String inspect( Class c )
	{
		return inspect( null, c, false );
	}


	public static String inspect( Object o, boolean recurse )
	{
		if( o == null ) return "null";

		return inspect( o, o.getClass(), recurse );
	}


	public static String inspect( Class c, boolean recurse )
	{
		return inspect( null, c, recurse );
	}


	private static String inspect( Object o, Class c, boolean recurse )
	{
		StringBuilder buf = new StringBuilder();

		String s;

		String vv;

		String[] primitives =
		{
			 "boolean", "byte", "char", "short", "int", "long", "float", "double"
		}
		;

		while( true )
		{
			if( c == null ) break;

			s = String.format( "  Class: %s\n", c.getName() );

			buf.append( s );

			Field[] fields = c.getDeclaredFields();

			AccessibleObject.setAccessible( fields, true );

			for( Field field : fields )
			{
				if( isfieldStatic( field ) == ( o == null ) )
				{
					String field_type = field.getType().toString();

					if( field_type.startsWith( "class " ) ) field_type = field_type.substring( "class ".length() );

					String var = field.getName();

					String val = null;

					try
					{
						if( in_list( primitives, field_type ) )
						{
							vv = "    %s %s = %s\n";

							val = String.format( "%s", field.get( o ) );
						}
						else
						{
							if( field.get( o ) == null )
							{
								vv = "    %s %s = %s\n";

								//val = String.format( "[%s] %s", "null", field_type );

								val = "null";
							}
							else
							{
								if( field_type.equals( "java.lang.String" ) )
								{
									vv = "    %s %s = \"%s\"\n";

									val = String.format( "%s", field.get( o ) );

									val = val.replaceAll( "\n", "[\\\\n]" );
								}
								else
								{
									vv = "    %s %s = %s\n";

									//val = field_type;

									val = field.get( o ).toString();

									//val = val.replaceAll( "\n", "<nl>" );
								}
							}
						}

						s = String.format( vv, field_type, var, val );

						buf.append( s );
					}
					catch( IllegalAccessException ex )
					{
						vv = "    %s %s = %s\n";

						s = String.format( vv, field_type, var, "[IllegalAccessException]" );

						buf.append( s );
					}
				}
			}

			//buf.append( "\n--------------------------------------------\n\n" );

			if( ! recurse ) break;

			c = c.getSuperclass();
		}

		return buf.toString().trim();
	}


	private static boolean isfieldStatic( Field field )
	{
		int mods = field.getModifiers();

		return Modifier.isStatic( mods );
	}


	public static Map<String, String> get_state( Object o )
	{
		return get_state( o, o.getClass(), false );
	}


	public static Map<String, String> get_state( Class c )
	{
		return get_state( null, c, false );
	}


	public static Map<String, String> get_state( Object o, boolean recurse )
	{
		return get_state( o, o.getClass(), recurse );
	}


	public static Map<String, String> get_state( Class c, boolean recurse )
	{
		return get_state( null, c, recurse );
	}


	private static Map<String, String> get_state( Object o, Class c, boolean recurse )// throws IllegalAccessException
	{
		Map<String, String> map = new HashMap<String, String>();

		while( true )
		{
			if( c == null ) break;

			Field[] fields = c.getDeclaredFields();

			AccessibleObject.setAccessible( fields, true );

			for( Field field : fields )
			{
				if( isfieldStatic( field ) == ( o == null ) )
				{
					try
					{
						String field_type = field.getType().toString();

						String var = field.getName();

						Object val = field.get( o );

						if( val == null )
						{
							map.put( var, null );
						}
						else
						{
							map.put( var, val.toString() );
						}
					}
					catch( IllegalAccessException ex )
					{
					}
				}
			}

			//buf.append( "\n--------------------------------------------\n\n" );

			Method[] methods = c.getDeclaredMethods();

			for( Method method : methods )
			{
				if( method.isAnnotationPresent( jsi3.lib.system.Mappable.class ) )
				{
					String var = method.getName();

					try
					{
						Object val = method.invoke( o );

						if( val == null )
						{
							map.put( var, null );
						}
						else
						{
							map.put( var, val.toString() );
						}
					}
					catch( IllegalAccessException ex )
					{
						map.put( var, null );
					}
					catch( InvocationTargetException ex )
					{
						map.put( var, null );
					}
				}
			}

			if( ! recurse ) break;

			c = c.getSuperclass();
		}

		return map;
	}


	public static String object_to_xml( Object o )
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEncoder xmlenc = new XMLEncoder( baos );

		xmlenc.writeObject( o );

		xmlenc.close();

		String xml = baos.toString();

		//cdebug.println( xml );

		return xml;
	}


	public static Object xml_to_object( String xml )
	{
		XMLDecoder xmldec = new XMLDecoder( new ByteArrayInputStream( xml.getBytes() ) );

		return xmldec.readObject();

	}
	
	
	public static Object call_method( Object target, String method_name, Object... args ) throws Exception
	{
		Method method = get_method( target, method_name );
		
		return method.invoke( target, args );
	}
	
	
	public static Object call_cstyle_getter( Object target, String field ) throws Exception
	{
		return call_method( target, "get_" + field );
	}
	
	
	public static void call_cstyle_setter( Object target, String field, Object value ) throws Exception
	{
		call_method( target, "set_" + field, value );
	}
	
	
	public static Method get_method( Object target, String method_name ) throws Exception
	{
		return get_method( target.getClass(), method_name );
	}
	
	
	public static Method get_method( Class target, String method_name ) throws Exception
	{
		Method[] methods = target.getDeclaredMethods();
		
		AccessibleObject.setAccessible( methods, true );
		
		cverbose.println( "Searching class %s for method %s", target.getName(), method_name );
		
		for( Method method : methods ) 
		{
			cverbose.println( "Checking method: %s", method.getName() );
			
			if( method_name.equals( method.getName() ) )
			{
				cverbose.println( "found" );
				
				return method;
			}
		}
		
		cverbose.println( "not found" );
		
		throw new NoSuchMethodException( fmt( "coudln't find method: %s.%s", target.getName(), method_name ) );
	}
}

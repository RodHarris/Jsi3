package jsi3.util.netlayer;


import javax.tools.*;

import java.lang.reflect.*;
import java.net.*;
import java.io.*;
import java.util.*;

import org.apache.xmlrpc.*;
import org.apache.xmlrpc.secure.*;

import jsi3.lib.text.*;


import static jsi3.lib.system.Statics.*;
import static jsi3.lib.console.Statics.*;


public class NetLayer
{
	private static WebServer server;
	
	private static final HashMap<Class, Class> compiled_ghosts = new HashMap<Class, Class>();
	
	private static final HashMap<Class, Class> compiled_proxies = new HashMap<Class, Class>();
	
	private static final HashMap<String, RAMResidentClassFileObject> byte_codes = new HashMap<String, RAMResidentClassFileObject>();
	
	private static JavaFileManager file_manager;
	
	public static final String HTTP = "http";
	
	public static final String HTTPS = "https";
	
	
	static
	{
		if( is_compiler_available() )
		{
			use_ram_resident_file_manager();
		}
		else
		{
			use_standard_file_manager();
		}
	}
	
	
	public static void start() throws Exception
	{
		set_output_level( VERBOSE );
		
		create_ghost_class( TestAPI.class );
		
		create_proxy_class( TestAPI.class );
		
		//start_xmlrpc2_securewebserver( 8080, Main.argv[ 0 ], Main.argv[ 1 ], Main.argv[ 2 ] );

		start_xmlrpc2_webserver( 8080 );

		serve_local_object( new TestImpl(), "test", TestAPI.class );

		TestAPI test = (TestAPI) access_remote_object( "http://localhost:8080", "test", TestAPI.class );

		int[] i = test.sort( new int[]{ 5,4,3,2,1 } );

		//test.sort( null );
		
		cout.println( test.get_a() );

		test.set_a( -500 );

		cout.println( test.get_a() );

		cout.println( test.cat( "!@#$%^&*()-_=+\\|[{]};:'\",<.>/?", "<!--\"\"-->&apos;" ) );

		cout.println( test.rnd() );

		cout.println( test.echo( "Hello World!" ) );

		close_xmlrpc2_server();
	}
	
	
	public static boolean is_compiler_available()
	{
		return ToolProvider.getSystemJavaCompiler() != null;
	}
	
	
	/**
	*	Use the standard java filemanager that writes class files to the current directory
	*/
	public static void use_standard_file_manager()
	{
		file_manager = null;
	}
	
	/**
	*	Use a filemanager that keeps source and class files in memory
	*/
	public static void use_ram_resident_file_manager()
	{
		file_manager = new RAMResidentFileManager();
	}


	/**
	*	Starts a http webserver on the specified port
	@throws IllegalStateException if a server is already running
	*/
	public static WebServer start_xmlrpc2_webserver( int port )
	{
		if( server != null ) throw new IllegalStateException( "server already started" );
		
		server = new WebServer( port );
		
		server.start();

		cdebug.println( "http webserver started on port %d", port );
		
		return server;
	}
	
	
	/**
	*	Starts a https webserver on the specified port, using the given key info
	@throws IllegalStateException if a server is already running
	*/
	public static WebServer start_xmlrpc2_securewebserver( int port, String keyStoreFilename, String keyStorePassword, String keyPassword )
	{
		if( server != null ) throw new IllegalStateException( "server already started" );

		server = new NLSecureWebServer( port, keyStoreFilename, keyStorePassword, keyPassword );
		
		server.start();

		cdebug.println( "https webserver started on port %d", port );
		
		return server;
	}
	
	
	/**
	*	shuts down the server
	@throws IllegalStateException if no serrver is running
	*/
	public static void close_xmlrpc2_server()
	{
		if( server == null ) throw new IllegalStateException( "server not started" );
		
		server.shutdown();
		
		server = null;
	}
	
	
	/**
	*	call this method from the client side to access a network object that implements the given interface
	*	this method will return an object that implements the given interface but when its methods are called it automatically calls the same method on the remote object it represents
	*/
	public static Object access_remote_object( String url, String name, Class interface_type ) throws URISyntaxException, ClassNotFoundException, InstantiationException, MalformedURLException, IllegalAccessException
	{
		Class ghost_class = create_ghost_class( interface_type );
		
		XmlRpc2Ghost ghost = (XmlRpc2Ghost) ghost_class.newInstance();
		
		ghost.connect( url, name );
		
		return ghost;
	}
	
	
	/**
	*	this method allows you to precompile the ghost class file ahead of time for use at times when the java compiler is not available at runtime
	*/
	public static Object access_precompiled_remote_object(  String url, String name, Class interface_type, String classname ) throws ClassNotFoundException, InstantiationException, MalformedURLException, IllegalAccessException
	{
		Class ghost_class = compiled_ghosts.get( interface_type );
		
		if( ghost_class == null )
		{
			ghost_class = Class.forName( classname, true, new LocalClassLoader() );

			compiled_ghosts.put( interface_type, ghost_class );
		}
		
		Object object = ghost_class.newInstance();
		
		if( ! implements_interface( object, interface_type ) ) throw new IllegalArgumentException( "object provided does not implement the given interface" );
		
		XmlRpc2Ghost ghost = (XmlRpc2Ghost) object;
		
		ghost.connect( url, name );

		cdebug.println( "connected to %s/%s", url, name );
		
		return ghost;
	}
	

	/**
	*	call this method to expose the given 'object' to xml-rpc calls over the network (only the methods that are defined in the interface 'interface_type' will be exposed)
	*	the reason I've done it this way is that there may be public methods in 'object' that you dont want exposed
	*	so just make sure the object given implements the interface
	*/
	public static void serve_local_object( Object object, String name, Class interface_type ) throws URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		if( ! implements_interface( object, interface_type ) ) throw new IllegalArgumentException( "object provided does not implement the given interface" );
		
		if( server == null ) throw new IllegalStateException( "Can't serve object: server not started" );
		
		Class proxy_class = create_proxy_class( interface_type );
		
		XmlRpc2Proxy proxy = (XmlRpc2Proxy) proxy_class.newInstance();
		
		proxy.set_client( object );
		
		server.addHandler( name, proxy );

		cdebug.println( "exposed %s", name );
	}
	
	
	/**
	*	convenience method allowing ahead of time compilation of ghost classes (useful to put in initialisation code so any errors in compilation will occur there)
	*/
	public static Class create_ghost_class( Class interface_type ) throws URISyntaxException, ClassNotFoundException
	{
		Class ghost_class = compiled_ghosts.get( interface_type );
		
		if( ghost_class == null )
		{
			String src = create_ghost_class_source( interface_type );

			cverbose.println( "Ghost class source" );

			cverbose.println( src );
		
			RAMResidentJavaFileObject rrjfo = new RAMResidentJavaFileObject( interface_type.getSimpleName() + "Ghost", src );
			
			check( compile( rrjfo ), "Couldn't compile generated class:\n%s", src );
			
			ghost_class = Class.forName( interface_type.getSimpleName() + "Ghost", true, new RAMResidentClassLoader() );
			
			compiled_ghosts.put( interface_type, ghost_class );
		}
		
		return ghost_class;
	}
	
	
	/**
	*	convenience method allowing ahead of time compilation of proxy classes (useful to put in initialisation code so any errors in compilation will occur there)
	*/
	public static Class create_proxy_class( Class interface_type ) throws URISyntaxException, ClassNotFoundException
	{
		Class proxy_class = compiled_proxies.get( interface_type );
		
		if( proxy_class == null )
		{
			String src = create_proxy_class_source( interface_type );

			cverbose.println( "Proxy class source" );

			cverbose.println( src );
		
			RAMResidentJavaFileObject rrjfo = new RAMResidentJavaFileObject( interface_type.getSimpleName() + "Proxy", src );
		
			check( compile( rrjfo ), "Couldn't compile generated class:\n%s", src );
			
			proxy_class = Class.forName( interface_type.getSimpleName() + "Proxy", true, new RAMResidentClassLoader() );
			
			compiled_proxies.put( interface_type, proxy_class );
		}
		
		return proxy_class;
	}


	public static ObjectCodec get_default_object_codec()
	{
		return new XmlObjectCodec();

		//return new SerialiserObjectCodec();
	}
	
	
	private static boolean implements_interface( Object o, Class i )
	{
		Class tc = o.getClass();
		
		cdebug.println( "Checking interfaces of class %s (must implement %s)", tc.getName(), i.getName() );
		
		while( true )
		{
			for( Class c : tc.getInterfaces() )
			{
				cdebug.println( c.getName() );
				
				if( c == i ) return true;
			}
			
			tc = tc.getSuperclass();
			
			if( tc == null ) break;
		}
		
		return false;
	}
	
	
	private static String create_ghost_class_source( Class super_interface )
	{
		String super_interface_pkg_name = super_interface.getPackage().getName();
		
		String super_interface_name = super_interface.getName().substring( super_interface_pkg_name.length() + 1 );
		
		EString src = new EString();
		
		//src.println( "package %s;", "netlayer" );
		
		src.println( "import %s;", super_interface.getName() );
		
		//src.println( "import static jsi3.util.netlayer.XmlObjectCodec.encode;" );
		
		//src.println( "import static jsi3.util.netlayer.XmlObjectCodec.decode;" );
		
		src.println( "public class %s extends %s implements %s", super_interface_name + "Ghost", "jsi3.util.netlayer.XmlRpc2Ghost", super_interface_name );
		
		src.println( "{" );

		src.println( "\tprivate final jsi3.util.netlayer.ObjectCodec object_codec = jsi3.util.netlayer.NetLayer.get_default_object_codec();" );
		
		for( Method method : super_interface.getMethods() )
		{
			String rt_name = method.getReturnType().getName();
			
			if( method.getReturnType().isArray() )
			{
				rt_name = method.getReturnType().getComponentType().getName() + "[]";
			}
			else
			{
			}
			
			src.println( "\tpublic %s %s( %s ) %s", rt_name, method.getName(), write_ghost_param_list( method ), write_throws_exceptions( method ) );
			
			src.println( "\t{" );
			
			src.print( "%s", write_rpc_call( method ) );
			
			src.println( "\t}" );
		}
		
		src.println( "}" );
		
		return src.toString();
	}
	
	
	
	private static String create_proxy_class_source( Class super_interface )
	{
		String super_interface_pkg_name = super_interface.getPackage().getName();
		
		String super_interface_name = super_interface.getName().substring( super_interface_pkg_name.length() + 1 );
		
		EString src = new EString();
		
		//src.println( "package %s;", "netlayer" );
		
		src.println( "import %s;", super_interface.getName() );
		
		//src.println( "import static jsi3.util.netlayer.XmlObjectCodec.encode;" );
		
		//src.println( "import static jsi3.util.netlayer.XmlObjectCodec.decode;" );
		
		src.println( "public class %s extends %s", super_interface_name + "Proxy", "jsi3.util.netlayer.XmlRpc2Proxy" );
		
		src.println( "{" );

		src.println( "\tprivate final jsi3.util.netlayer.ObjectCodec object_codec = jsi3.util.netlayer.NetLayer.get_default_object_codec();" );
		
		src.println( "\tprivate %s _client;", super_interface_name );
		
		src.println( "\tprotected Object get_client()" );
		
		src.println( "\t{" );
		
		src.println( "\t\treturn _client;" );
		
		src.println( "\t}" );
		
		src.println( "\tprotected void set_client( Object o )" );
		
		src.println( "\t{" );
		
		src.println( "\t\tthis._client = (%s) o;", super_interface_name );
		
		src.println( "\t}" );
		
		for( Method method : super_interface.getMethods() )
		{
			Class rt = method.getReturnType();

			String rt_name = "String";
			
			if( is_primitive( rt ) ) rt_name = rt.getName();
			
			src.println( "\tpublic %s %s( %s ) %s", rt_name, method.getName(), write_proxy_param_list( method ), write_throws_exceptions( method ) );
			
			src.println( "\t{" );
			
			src.print( "%s", write_lpc_call( method ) );
			
			src.println( "\t}" );
		}
		
		src.println( "}" );
		
		return src.toString();
	}
	
	
	private static String write_ghost_param_list( Method method )
	{
		EString s = new EString();
		
		Class[] param_types = method.getParameterTypes();
		
		if( param_types.length == 0 ) return "";
		
		for( int i=0; i<param_types.length; i++ )
		{
			String pt_name =  param_types[ i ].getName();
			
			if( param_types[ i ].isArray() )
			{
				pt_name =  param_types[ i ].getComponentType().getName() + "[]";
			}
			
			s.print( "%s %s, ", pt_name, "a" + i );
		}
		
		String r = s.toString();
		
		return r.substring( 0, r.length() - 2 );
	}
	
	
	private static String write_proxy_param_list( Method method )
	{
		EString s = new EString();
		
		Class[] param_types = method.getParameterTypes();
		
		if( param_types.length == 0 ) return "";
		
		for( int i=0; i<param_types.length; i++ )
		{
			String type_name = "String";

			if( is_primitive( param_types[ i ] ) )  type_name = param_types[ i ].getName();

			s.print( "%s %s, ", type_name, "a" + i );
		}
		
		String r = s.toString();
		
		return r.substring( 0, r.length() - 2 );
	}
	
	
	private static String write_throws_exceptions( Method method )
	{
		EString s = new EString();
		
		Class[] ex_types = method.getExceptionTypes();
		
		if( ex_types.length == 0 ) return "";
		
		s.print( "throws " );
		
		for( int i=0; i<ex_types.length; i++ )
		{
			s.print( "%s, ", ex_types[ i ].getName() );
		}
		
		String r = s.toString();
		
		return r.substring( 0, r.length() - 2 );
	}
	
	
	private static String write_rpc_call( Method method )
	{
		EString s = new EString();
		
		if( method.getReturnType() == Void.TYPE )
		{
			s.println( "\t\tinvoke( \"%s\"%s );",  method.getName(), write_rpc_params( method ) );
		}
		
		else
		{
			Class c = method.getReturnType();

			if( is_primitive( c ) )
			{
				s.println( "\t\treturn (%s) invoke( \"%s\"%s );", autobox_cast( c ).getName(), method.getName(), write_rpc_params( method ) );
			}
			else if( is_string( c ) )
			{
				s.println( "\t\treturn (%s) invoke( \"%s\"%s );", c.getName(), method.getName(), write_rpc_params( method ) );
			}
			else
			{
				String rt_name = c.getName();
				
				if( c.isArray() )
				{
					rt_name =  c.getComponentType().getName() + "[]";
				}
				
				s.println( "\t\tString xml = (String) invoke( \"%s\"%s );", method.getName(), write_rpc_params( method ) );

				s.println( "\t\treturn (%s) object_codec.decode( xml );", rt_name );
			}
		}
		
		return s.toString();
	}
	

	private static boolean is_primitive( Class c )
	{
			if( c == byte.class ) return true;

			if( c == short.class ) return true;

			if( c == int.class ) return true;

			if( c == long.class ) return true;

			if( c == float.class ) return true;

			if( c == double.class ) return true;

			if( c == char.class ) return true;

			if( c == boolean.class ) return true;

			return false;
	}
	

	private static boolean is_string( Class c )
	{
		return c == String.class;
	}

	
	
	private static Class autobox_cast( Class c )
	{
		if( ! c.isPrimitive() ) return c;

		if( c == byte.class ) return Byte.class;

		if( c == short.class ) return Short.class;

		if( c == int.class ) return Integer.class;

		if( c == long.class ) return Long.class;

		if( c == float.class ) return Float.class;

		if( c == double.class ) return Double.class;

		if( c == char.class ) return Character.class;

		if( c == boolean.class ) return Boolean.class;
				
		affirm( false, "Unknown primitive type: %s", c );
		
		return null;
	}
	
	
	private static String write_lpc_call( Method method )
	{
		EString s = new EString();
		
		if( method.getReturnType() == Void.TYPE )
		{
			s.println( "\t\t_client.%s( %s );", method.getName(), write_lpc_params( method ) );
			
			s.println( "\t\treturn \"void\";" );
		}

		else if( is_primitive( method.getReturnType() ) )
		{
			s.println( "\t\treturn _client.%s( %s );", method.getName(), write_lpc_params( method ) );
		}
		
		else if( is_string( method.getReturnType() ) )
		{
			s.println( "\t\treturn _client.%s( %s );", method.getName(), write_lpc_params( method ) );
		}

		else
		{
			s.println( "\t\tObject o = _client.%s( %s );", method.getName(), write_lpc_params( method ) );

			s.println( "\t\treturn object_codec.encode( o );" );
		}
		
		return s.toString();
	}
	
	
	private static String write_rpc_params( Method method )
	{
		EString s = new EString();
		
		Class[] param_types = method.getParameterTypes();
		
		if( param_types.length == 0 ) return "";

		s.print( ", " );
		
		for( int i=0; i<param_types.length; i++ )
		{
			if( is_primitive( param_types[ i ] ) )
			{
				s.print( "%s, ", "a" + i );
			}
			else if( is_string( param_types[ i ] ) )
			{
				s.print( "%s, ", "a" + i );
			}
			else
			{
				s.print( "object_codec.encode( %s ), ", "a" + i );
			}
		}
		
		String r = s.toString();
		
		return r.substring( 0, r.length() - 2 );
	}
	
	
	private static String write_lpc_params( Method method )
	{
		EString s = new EString();
		
		Class[] param_types = method.getParameterTypes();
		
		if( param_types.length == 0 ) return "";
		
		for( int i=0; i<param_types.length; i++ )
		{
			if( is_primitive( param_types[ i ] ) )
			{
				s.print( "%s, ", "a" + i );
			}
			else if( is_string( param_types[ i ] ) )
			{
				s.print( "%s, ", "a" + i );
			}
			else
			{
				String pt_name = param_types[ i ].getName();
				
				if( param_types[ i ].isArray() )
				{
					pt_name = param_types[ i ].getComponentType().getName() + "[]";
				}
				
				s.print( "(%s) object_codec.decode( %s ), ", pt_name, "a" + i );
			}
		}
		
		String r = s.toString();
		
		return r.substring( 0, r.length() - 2 );
	}
	
	
	private static boolean compile( JavaFileObject... source )
	{
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		final JavaCompiler.CompilationTask task = compiler.getTask( null, file_manager, null, null, null, Arrays.asList( source ) );
		
		return task.call();
	}


	static RAMResidentClassFileObject new_class_file( String name ) throws URISyntaxException
	{
		RAMResidentClassFileObject rrcfo = new RAMResidentClassFileObject( name );			
		
		byte_codes.put( name, rrcfo );
		
		return rrcfo;
	}

	
	static byte[] get_byte_code( String name ) throws ClassNotFoundException
	{
		RAMResidentClassFileObject rrcfo = byte_codes.get( name );
		
		if( rrcfo == null ) throw new ClassNotFoundException( "Couldn't find class: " + name );
		
		return rrcfo.getBytes();
	}
}

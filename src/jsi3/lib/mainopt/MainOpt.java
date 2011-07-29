package jsi3.lib.mainopt;

import java.util.*;

/**
*	MainOpt originally created using JProject build 111 on Tue Feb 23 16:01:04 EST 2010
*	@author Rod Harris, Digital Resource Services, ANU 2010
*/
public class MainOpt
{
	private static String[] argv;

	private static int argc;

	private static Map<String,String[]> argexp;

	private static ArrayList<KnownFlag> knowns = new ArrayList<KnownFlag>();


	protected static void set( String[] args )
	{
		argexp = new LinkedHashMap<String,String[]>();

		ArrayList<String> argn = new ArrayList<String>();

		for( int i=0; i<args.length; i++ )
		{
			String a = args[ i ];

			if( a.startsWith( "--" ) )
			{
				ArrayList<String> argt = new ArrayList<String>();

				for( i=i+1; i<args.length; i++ )
				{
					String a2 = args[ i ];

					if( a2.startsWith( "-" ) )
					{
						i --;

						break;
					}

					argt.add( a2 );
				}

				argexp.put( a, argt.toArray( new String[ argt.size() ] ) );

				continue;
			}

			if( a.startsWith( "-" ) && a.length() == 2 )
			{
				ArrayList<String> argt = new ArrayList<String>();

				for( i=i+1; i<args.length; i++ )
				{
					String a2 = args[ i ];

					if( a2.startsWith( "-" ) )
					{
						i --;

						break;
					}

					argt.add( a2 );
				}

				argexp.put( a, argt.toArray( new String[ argt.size() ] ) );

				continue;
			}

			if( a.startsWith( "-" ) && a.length() > 2 )
			{
				for( char c : a.toCharArray() )
				{
					if( c == '-' ) continue;

					argexp.put( "-" + c , new String[ 0 ] );
				}

				continue;
			}

			argn.add( a );
		}

		if( argn.size() > 0 )
		{
			argexp.put( "[void]", argn.toArray( new String[ argn.size() ] ) );
		}

		argv = args;

		argc = argv.length;
	}


	protected static void arg_usage()
	{
		System.err.println( "\n   where [options] include:" );

		for( KnownFlag k : knowns )
		{
			System.err.println( "\n     " + k );
		}
	}


	public static KnownParam add_known_param( int n, String... names_and_description )
	{
		if( names_and_description.length == 1 )
		{
			names_and_description = new String[]{ "[void]", names_and_description[ 0 ] };
		}

		String[] names = new String[ names_and_description.length - 1 ];

		System.arraycopy( names_and_description, 0, names, 0, names.length );

		KnownParam kp = new KnownParam( n, names, names_and_description[ names_and_description.length - 1 ] );

		knowns.add( kp );

		return kp;
	}


	public static KnownFlag add_known_flag( int n, String... names_and_description )
	{
		String[] names = new String[ names_and_description.length - 1 ];

		System.arraycopy( names_and_description, 0, names, 0, names.length );

		KnownFlag kf = new KnownFlag( n, names, names_and_description[ names_and_description.length - 1 ] );

		knowns.add( kf );

		return kf;
	}


	public static void show_args()
	{
		System.err.println( "argc: " + argc );

		for( String s : argv ) System.err.println( " >> argv: " + s );

		System.err.println( "argexp:" );

		for( String k : argexp.keySet() )
		{
			System.err.print( "  " + k );

			for( String v : argexp.get( k ) )
			{
				System.err.print( " : " + v );
			}

			System.err.println();
		}
	}


	public static boolean has_flag( String... args )
	{
		for( String a1 : argv )
		{
			for( String a2 : args )
			{
				if( a1.equals( a2 ) ) return true;
			}
		}

		return false;
	}


	public static String get_param( String... args )
	{
		for( String a2 : args )
		{
			for( int i=0; i<argv.length; i++ )
			{
				String a1 = argv[ i ];

				if( a1.equals( a2 ) ) return argv[ i + 1 ];
			}
		}

		return null;
	}


	public static boolean has_param( String... args )
	{
		for( String a2 : args )
		{
			for( int i=0; i<argv.length - 1; i++ )
			{
				String a1 = argv[ i ];

				if( a1.equals( a2 ) )
				{
					return ! argv[ i + 1 ].startsWith( "-" );
				}
			}
		}

		return false;
	}


	public static int count_flags( String... args )
	{
		int count = 0;

		for( String a1 : argv )
		{
			for( String a2 : args )
			{
				if( a1.equals( a2 ) ) count ++;
			}
		}

		return count;
	}


	public static void shift()
	{
		shift( 1 );
	}


	public static void shift( int n )
	{
		String[] args = new String[ argc - n ];

		for( int i=0; i<args.length; i++ )
		{
			args[ i ] = argv[ i + n ];
		}

		argc = args.length;

		argv = args;
	}


	public static String get_first_param()
	{
		for( String s : argv )
		{
			if( ! s.startsWith( "-" ) ) return s;
		}

		return null;
	}


	public static String[] get_params( String param_name )
	{
		ArrayList<String> params = new ArrayList<String>();

		for( int i=0; i<argc; i++ )
		{
			String a = argv[ i ];

			if( a.equals( param_name ) )
			{
				for( int i2=i+1; i2<argc; i2++ )
				{
					String a2 = argv[ i2 ];

					if( a2.startsWith( "-" ) ) break;

					params.add( a2 );
				}

				break;
			}
		}

		return params.toArray( new String[ params.size() ] );
	}


	protected static void check_args()
	{
		for( String k : argexp.keySet() )
		{
			boolean found = false;

			for( KnownFlag kf : knowns )
			{
				if( kf.has_name( k ) )
				{
					found = true;

					break;
				}
			}

			if( ! found ) throw new IllegalArgumentException( "Unknown flag: " + k );
		}

		for( KnownFlag kf : knowns )
		{
			if( kf instanceof KnownParam )
			{
				KnownParam kp = (KnownParam) kf;

				String[] args = get_args( kp );

				if( kp.required )
				{
					if( args.length == 0 ) throw new IllegalArgumentException( "Required parameter " + kp.names() + " has no arguments" );

					if( kp.n != 0 && args.length != kp.n ) throw new IllegalArgumentException( "Required parameter " + kp.names() + " has wrong number of arguments" );
				}
				else
				{
					if( args.length != 0 && kp.n != 0 && args.length != kp.n ) throw new IllegalArgumentException( "Optional parameter " + kp.names() + " has wrong number of arguments" );
				}
			}
			else
			{
				if( kf.required || num_flag( kf ) != 0 )
				{
					if( kf.n != 0 && num_flag( kf ) != kf.n ) throw new IllegalArgumentException( "Flag " + kf.names() + " occurs the wrong number of times" );
				}
			}
		}
	}


	public static String[] get_args( KnownParam kp )
	{
		ArrayList<String> args = new ArrayList<String>();

		for( String k : kp.names )
		{
			String[] argp = argexp.get( k );

			if( argp == null ) continue;

			for( String arg : argp ) args.add( arg );
		}

		return args.toArray( new String[ args.size() ] );
	}


	public static String get_arg( KnownParam kp )
	{
		for( String k : kp.names )
		{
			String[] argp = argexp.get( k );

			if( argp == null ) continue;

			if( argp.length == 0 ) continue;

			return argp[ 0 ];
		}

		return null;
	}


	public static boolean has_flag( KnownFlag kf )
	{
		for( String k : kf.names )
		{
			if( argexp.get( k ) != null ) return true;
		}

		return false;
	}


	public static int num_flag( KnownFlag kf )
	{
		int count = 0;

		for( String k : kf.names )
		{
			if( argexp.get( k ) != null ) count ++;
		}

		return count;
	}
	
	
	public static String[] get_argv()
	{
		return argv;
	}
}

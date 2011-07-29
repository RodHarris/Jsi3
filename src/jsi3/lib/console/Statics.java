package jsi3.lib.console;

import java.io.*;


public class Statics
{
	private static int print_level;
	
	
	public static final OStream cout = new OStream( System.out );
	
	public static final OStream cerr = new OStream( System.err );
	
	public static final OStream cwarn = new OStream( System.err );
	
	public static final OStream cinfo = new OStream( System.err );
	
	public static final OStream cdebug = new OStream( System.err );
	
	public static final OStream cverbose = new OStream( System.err );
	
	
	public static final int NONE = -1;
	
	public static final int OUT = 0;
	
	public static final int ERR = 1;
	
	public static final int WARN = 2;
	
	public static final int INFO = 3;
	
	public static final int DEBUG = 4;
	
	public static final int VERBOSE = 5;


	private static boolean colour_output = false;

	private static boolean timestamp_output = false;
	
	
	static
	{
		cerr.add_decorators( new NameDecorator( "ERROR:   " ), new TraceDecorator( "%f:%l: " ) );
		
		cwarn.add_decorators( new NameDecorator( "WARN:    " ), new TraceDecorator( "%f:%l: " ) );
		
		cinfo.add_decorators( new NameDecorator( "INFO:    " ), new TraceDecorator( "%f:%l: " ) );
		
		cdebug.add_decorators( new NameDecorator( "DEBUG:   " ), new TraceDecorator( "%f:%l: " ) );
		
		cverbose.add_decorators( new NameDecorator( "VERBOSE: " ), new TraceDecorator( "%f:%l: " ) );
		
		set_output_level( INFO );
	}


	/**
	 * Inserts new decorators into the console streams:
	 * cerr: Red
	 * cwarn: Yellow
	 * cinfo: Green
	 * cdebug: Blue
	 * cverbose: Purple
	 */
	public static void colour_output()
	{
		if( colour_output ) return;

		colour_output = true;
		
		cerr.insert_decorator( new ColourDecorator( "Red" ) );

		cinfo.insert_decorator( new ColourDecorator( "Green" ) );

		cwarn.insert_decorator( new ColourDecorator( "Yellow" ) );

		cdebug.insert_decorator( new ColourDecorator( "Blue" ) );

		cverbose.insert_decorator( new ColourDecorator( "Purple" ) );
	}


	/**
	 * Inserts new decorators into each console stream (including cout)
	 * dd/MM/YYYY hh:mm:ss >>
	 */
	public static void timestamp_output()
	{
		if( timestamp_output ) return;

		timestamp_output = true;

		cout.insert_decorator( new DateTimeDecorator() );
		
		cerr.insert_decorator( new DateTimeDecorator() );

		cinfo.insert_decorator( new DateTimeDecorator() );

		cwarn.insert_decorator( new DateTimeDecorator() );

		cdebug.insert_decorator( new DateTimeDecorator() );

		cverbose.insert_decorator( new DateTimeDecorator() );
	}


	/**
	 * Macro to set the level of console logging (ie. turn streams on of off)
	 * levels include (in order):
	 * NONE
	 * OUT
	 * ERR
	 * WARN
	 * INFO
	 * DEBUG
	 * VERBOSE
	 * The default level is INFO
	 * ie. DEBUG and VERBOSE streams won't print
	 */
	public static void set_output_level( int level )
	{
		if( level < NONE ) level = NONE;
		
		if( level > VERBOSE ) level = VERBOSE;
		
		print_level = level;
		
		for( int i=OUT; i<=VERBOSE; i++ )
		{
			get_ostream( i ).on( print_level >= i );
		}
	}
	
	
	private static OStream get_ostream( int level )
	{
		if( level == OUT )
		{
			return cout;
		}
		
		if( level == ERR )
		{
			return cerr;
		}
		
		if( level == WARN )
		{
			return cwarn;
		}
		
		if( level == INFO )
		{
			return cinfo;
		}

		if( level == DEBUG )
		{
			return cdebug;
		}

		return cverbose;
	}


	/**
	prints prompt to screen (a question mark is not appended automatically) and reads input from System.in
	*/
	public static String input( String fmt, Object... args ) throws IOException
	{
		System.out.print( String.format( fmt, args ) );
		
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
		
		String input = in.readLine();
		
		return input;
	}


	private Statics(){};
}

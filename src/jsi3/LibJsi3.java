package jsi3;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;

import static org.junit.Assert.*;

import jsi3.lib.text.*;

import static jsi3.lib.filesystem.Statics.*;
import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;
import static jsi3.lib.system.Statics.*;


public class LibJsi3
{
	public static final int BUILD = 283;

	public static final long BUILD_TIME = 1311931611002L;

	public static void main( String[] args ) throws Exception
	{
		if( args.length == 1 && "--version".equals( args[ 0 ] ) )
		{
			version( 0 );
		}
		else if( args.length == 1 && "--copy".equals( args[ 0 ] ) )
		{
			show_copyrights();
		}
		else if( args.length == 1 && "--unit".equals( args[ 0 ] ) )
		{
			run_unit_tests();
		}
		else if( args.length == 1 && "--demo".equals( args[ 0 ] ) )
		{
			run_demo();
		}
		else
		{
			usage( 0 );
		}
	}


	public static void run_demo()
	{
		jsi3.lib.gui.SliderFrame sf = new jsi3.lib.gui.SliderFrame();
		
		
		sf.add_int_slider( "Val a", 0, 10, 2 );
		
		sf.add_int_slider( "Val b", -50, 50, 2 );
		
		sf.add_int_slider( "Val c", 0, 255, 100 );
		
		sf.add_double_slider( "Val d", 0, 1, .5 );
		
		sf.add_double_slider( "Val e", 0, 8, 4 );
		
		sf.setVisible( true );
		
		jsi3.lib.gui.Statics.resize_window( sf, 0.5, 0.5 );
		
		jsi3.lib.gui.Statics.position_window( sf, 0.5, 0.5 );
	}

	
	@Test
	public void test_text_statics_cut()
	{
		assertEquals( cut( "rod", "j", 0 ), "rod" );
		assertEquals( cut( "rod", "j", 1 ), null );
		assertEquals( cut( "rod", "o", 0 ), "r" );
		assertEquals( cut( "rod", "o", 1 ), "d" );
		assertEquals( cut( "rod", "o", 2 ), null );
	}
	
	
	@Test
	public void test_text_statics_grep()
	{
		EString text = new EString();
		
		text.println( "first name: Rod" );
		text.println( "last name: Harris" );
		text.println( "age: 33" );
		text.println( "occupation: programmer" );
		
		assertNotNull( grep( text.toString(), "last" ) );
		
		assertEquals( 1, grep( text.toString(), "last" ).length );
		
		assertEquals( grep( text.toString(), "last" )[ 0 ], "last name: Harris" );
	}


	@Test
	public void test_text_statics_grep_and_cut()
	{
		EString text = new EString();
		
		text.println( "first name: Rod" );
		text.println( "last name: Harris" );
		text.println( "age: 33" );
		text.println( "occupation: programmer" );
		
		assertEquals( cut( grep( text.toString(), "last" )[ 0 ], ":", 1 ).trim(), "Harris" );
		
		assertEquals( _int( cut( grep( text.toString(), "age" )[ 0 ], ":", 1 ).trim() ), 33 );
	}


	public static void version( int exit_code )
	{
		System.err.println( "LibJsi3 build " + BUILD + " on " + new java.util.Date( BUILD_TIME ) );

		if( exit_code > 0 ) return;

		System.exit( exit_code );
	}

	
	private static void run_unit_tests()
	{
		Result result = JUnitCore.runClasses( LibJsi3.class );

		cout.println( "Unit Testing Results:" );
		
		cout.println( "   Passed:          %b", result.wasSuccessful() );
		
		cout.println( "   Time Taken:      %dms", result.getRunTime() );
		
		cout.println( "   Tests Run:       %d", result.getRunCount() );
		
		cout.println( "   Tests Failed:    %d", result.getFailureCount() );
		
		cout.println( "   Tests Ingored:   %d", result.getIgnoreCount() );
		
		for( Failure failure : result.getFailures() )
		{
			cerr.println( failure );
			
			cerr.println( ex_to_string( failure.getException() ) );
		}
		
		if( result.wasSuccessful() )
		{
			System.exit( 0 );
		}
		else
		{
			System.exit( -1 );
		}
	}


	static void show_copyrights() throws Exception
	{
		for( String line : load_text_resource( LibJsi3.class, "copyrights/list.txt" ).split( "\n" ) )
		{
			if( no_data( line ) ) continue;
			
			line = line.trim();
			
			if( line.startsWith( "#" ) ) continue;
			
			String[] tokens = line.split( " : " );
			
			if( tokens.length != 2 ) continue;
			
			System.out.println( "\n" + tokens[ 0 ] );
			
			//System.out.println( "\n" + tokens[ 1 ] );
			
			System.out.println( "\n" + load_text_resource( LibJsi3.class, "copyrights/" + tokens[ 1 ].trim() ) );
		}
		
		System.exit( 0 );
	}
	
	
	static void usage( int exit_code )
	{
		System.err.println( "usage: LibJsi3 [options]" );

		System.err.println( "   --version  :  prints build number and date" );
		System.err.println( "   --copy     :  copyright info for this library and all included dependencies" );
		System.err.println( "   --unit     :  runs units tests" );

		if( exit_code > 0 ) return;

		System.exit( exit_code );
	}
}

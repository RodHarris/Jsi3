package jsi3.lib.time;

import java.sql.*;
import java.util.*;
import java.util.Date;

import static jsi3.lib.console.Statics.*;
import static jsi3.lib.system.Statics.*;

public class Statics
{
	// -------------------------------    TIME

	private static long stopwatch_start;


	public static Timestamp _timestamp( String date_string )
	{
		String[] time_date_tokens = date_string.split( "\\s+" );

		if( time_date_tokens.length != 2 ) throw new IllegalArgumentException( "Date dormat should be [hh:mm:ss dd/mm/yyyy]" );

		String[] time_tokens = time_date_tokens[ 0 ].split( ":" );

		if( time_tokens.length != 3 ) throw new IllegalArgumentException( "Time dormat should be hh:mm:ss" );

		String[] date_tokens = time_date_tokens[ 1 ].split( "/" );

		if( date_tokens.length != 3 ) throw new IllegalArgumentException( "Date dormat should be dd/mm/yyyy" );

		int hh = _int( time_tokens[ 0 ].trim() );

		int mm = _int( time_tokens[ 1 ].trim() );

		int ss = _int( time_tokens[ 2 ].trim() );

		int dd = _int( date_tokens[ 0 ].trim() );

		int MM = _int( date_tokens[ 1 ].trim() );

		int yyyy = _int( date_tokens[ 2 ].trim() );

		if( yyyy < 100 ) yyyy += 2000;

		return _timestamp( date( yyyy, MM, dd, hh, mm, ss ) );
	}

	
	public static Timestamp _timestamp( Date d )
	{
		return new Timestamp( d.getTime() );
	}
	
	
	public static Date _date( String date_string )
	{
		String[] time_date_tokens = date_string.split( "\\s+" );

		if( time_date_tokens.length != 2 ) throw new IllegalArgumentException( "Date dormat should be [hh:mm:ss dd/mm/yyyy]" );

		String[] time_tokens = time_date_tokens[ 0 ].split( ":" );

		if( time_tokens.length != 3 ) throw new IllegalArgumentException( "Time dormat should be hh:mm:ss" );

		String[] date_tokens = time_date_tokens[ 1 ].split( "/" );

		if( date_tokens.length != 3 ) throw new IllegalArgumentException( "Date dormat should be dd/mm/yyyy" );

		int hh = _int( time_tokens[ 0 ].trim() );

		int mm = _int( time_tokens[ 1 ].trim() );

		int ss = _int( time_tokens[ 2 ].trim() );

		int dd = _int( date_tokens[ 0 ].trim() );

		int MM = _int( date_tokens[ 1 ].trim() );

		int yyyy = _int( date_tokens[ 2 ].trim() );

		if( yyyy < 100 ) yyyy += 2000;

		return date( yyyy, MM, dd, hh, mm, ss );
	}

	/**
	get the system time ( milliseconds since epoch )
	*/
	public static long systime()
	{
		return System.currentTimeMillis();
	}

	/**
	get the date now
	*/
	public static Date now()
	{
		return new Date();
	}

	/**
	get todays date for the given time
	*/
	public static Date today()
	{
		Calendar c = new GregorianCalendar();

		c.set( c.HOUR_OF_DAY, 0 );

		c.set( c.MINUTE, 0 );

		c.set( c.SECOND, 0 );

		c.set( c.MILLISECOND, 0 );

		return c.getTime();
	}

	/**
	get todays date for the given time
	*/
	public static Date today( int hour, int minute )
	{
		Calendar c = new GregorianCalendar();

		c.set( c.HOUR_OF_DAY, hour );

		c.set( c.MINUTE, minute );

		c.set( c.SECOND, 0 );

		c.set( c.MILLISECOND, 0 );

		return c.getTime();
	}


	/**
	get todays date for the given time
	*/
	public static Date today( int hour, int minute, int second )
	{
		Calendar c = new GregorianCalendar();

		c.set( c.HOUR_OF_DAY, hour );

		c.set( c.MINUTE, minute );

		c.set( c.SECOND, second );

		c.set( c.MILLISECOND, 0 );

		return c.getTime();
	}


	public static int hour( Date date )
	{
		Calendar c = new GregorianCalendar();

		c.setTime( date );

		return c.get( Calendar.HOUR );
	}


	public static int day_of_month( Date date )
	{
		Calendar c = new GregorianCalendar();

		c.setTime( date );

		return c.get( Calendar.DAY_OF_MONTH );
	}


	public static int month( Date date )
	{
		Calendar c = new GregorianCalendar();

		c.setTime( date );

		return c.get( Calendar.MONTH ) + 1;
	}


	public static int year( Date date )
	{
		Calendar c = new GregorianCalendar();

		c.setTime( date );

		return c.get( Calendar.YEAR );
	}


	public static Date date( int year, int month, int day )
	{
		GregorianCalendar gc = new GregorianCalendar( year, month - 1, day );

		return gc.getTime();
	}


	public static Date date( int year, int month, int day, int hour, int minute, int second )
	{
		GregorianCalendar gc = new GregorianCalendar( year, month - 1, day, hour, minute, second );

		return gc.getTime();
	}


	public static Date add_days( Date d, int n )
	{
		GregorianCalendar cal = new GregorianCalendar();

		cal.setTime( d );
		
		cal.add( cal.DAY_OF_MONTH, n );
		
		return cal.getTime();
	}


	public static void stopwatch_start()
	{
		stopwatch_start = systime();
	}


	public static long stopwatch_stop()
	{
		long dt = systime() - stopwatch_start;

		cdebug.println( "Stopwatch timer: %dms", dt );

		return dt;
	}

	public static String format( Date date, String fmt )
	{
		GregorianCalendar cal = new GregorianCalendar();

		cal.setTime( date );

		return format( cal, fmt );
	}

	public static String format( Calendar cal, String fmt )
	{
		final String[] days = "Saturday Sunday Monday Tuesday Wednesday Thursday Friday".split( "\\s+" );

		final String[] suffixes = "th st nd rd th th th th th th".split( "\\s+" );

		final String[] months = "January February March April May June July August September October November December".split( "\\s+" );

		fmt = fmt.replace( "DAYNAME", days[ cal.get( cal.DAY_OF_WEEK ) % 7 ] );

		fmt = fmt.replace( "DAYOFWEEK", _string( cal.get( cal.DAY_OF_WEEK ) ) );

		fmt = fmt.replace( "DAYOFMONTH", _string( cal.get( cal.DAY_OF_MONTH ) ) );

		fmt = fmt.replace( "SUFFIX", suffixes[ cal.get( cal.DAY_OF_MONTH ) % 10 ] );// wrong

		fmt = fmt.replace( "MONTHNAME", months[ cal.get( cal.MONTH ) ] );

		fmt = fmt.replace( "DAY", _string( cal.get( cal.DAY_OF_MONTH ) ) );

		fmt = fmt.replace( "MONTH", _string( cal.get( cal.MONTH ) ) );

		fmt = fmt.replace( "YEAR", _string( cal.get( cal.YEAR ) ) );

		return fmt;
	}
}

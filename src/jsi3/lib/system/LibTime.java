package jsi3.lib.system;

import java.util.*;

import static jsi3.lib.console.Statics.*;
import static jsi3.lib.system.Statics.*;


public class LibTime
{
	// -------------------------------    TIME


	private static long stopwatch_start;

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
		GregorianCalendar gc = new GregorianCalendar( year, month, day );

		return gc.getTime();
	}


	public static Date date( int year, int month, int day, int hour, int minute, int second )
	{
		GregorianCalendar gc = new GregorianCalendar( year, month, day, hour, minute, second );

		return gc.getTime();
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

		fmt = fmt.replace( "SUFFIX", suffixes[ cal.get( cal.DAY_OF_MONTH ) % 10 ] );

		fmt = fmt.replace( "MONTHNAME", months[ cal.get( cal.MONTH ) ] );

		fmt = fmt.replace( "DAY", _string( cal.get( cal.DAY_OF_MONTH ) ) );

		fmt = fmt.replace( "MONTH", _string( cal.get( cal.MONTH ) ) );

		fmt = fmt.replace( "YEAR", _string( cal.get( cal.YEAR ) ) );

		return fmt;
	}
}

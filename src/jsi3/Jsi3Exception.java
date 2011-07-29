package jsi3;

public class Jsi3Exception extends Exception
{
	public Jsi3Exception( String msg )
	{
		super( msg );
	}

	public Jsi3Exception( String msg, Object... args )
	{
		super( String.format( msg, args ) );
	}

	public Jsi3Exception( Throwable cause )
	{
		super( cause );
	}

	public Jsi3Exception( Throwable cause, String msg )
	{
		super( msg, cause );
	}
	
	public Jsi3Exception( Throwable cause, String msg, Object... args )
	{
		super( String.format( msg, args ), cause );
	}
}	

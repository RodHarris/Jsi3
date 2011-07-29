package jsi3.util.sqldb;

import jsi3.Jsi3Exception;

public class SchemaMismatchException extends Jsi3Exception
{
	public SchemaMismatchException( String msg )
	{
		super( msg );
	}

	public SchemaMismatchException( String msg, Object... args )
	{
		super( msg, args );
	}

	public SchemaMismatchException( Throwable cause )
	{
		super( cause );
	}

	public SchemaMismatchException( Throwable cause, String msg )
	{
		super( cause, msg );
	}
	
	public SchemaMismatchException( Throwable cause, String msg, Object... args )
	{
		super( cause, msg, args );
	}
}

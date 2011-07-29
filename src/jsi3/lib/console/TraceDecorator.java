package jsi3.lib.console;

public class TraceDecorator implements StreamDecorator
{
	final String fmt;
	
	public static final int TRACE_DEPTH = calibrate_stacktrace();

	private final int trace_depth;

	
	private static int calibrate_stacktrace()
	{
		return method1() + 3;
	}
	
	
	private static int method1()
	{
		int i=0;
		
		for( StackTraceElement ste : Thread.currentThread().getStackTrace() )
		{
			if( ste.getMethodName().equals( "calibrate_stacktrace" ) )
			{
				return i;
			}
			
			i++;
		}
		
		return -1;
	}
	
	public TraceDecorator( String fmt )
	{
		this( fmt, 0 );
	}

        public TraceDecorator( String fmt, int d )
        {
		this.fmt = fmt;

                trace_depth = TRACE_DEPTH + d;
        }
	
	public String pre()
	{
		return trace_call( TRACE_DEPTH, fmt );
	}
	
	public String post()
	{
		return "";
	}
	
	/**
	*	%c - class name
	*	%m - method name
	*	%f - file name
	*	%l - line number
	*/
	private static String trace_call( int level, String fmt )
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
}

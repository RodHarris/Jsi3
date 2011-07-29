package jsi3.lib.system;

import jsi3.lib.text.*;

import java.util.concurrent.*;

import static jsi3.lib.console.Statics.*;

/**
*	Use (extensions of) this class when complex state management is needed to be tracked/changed in response to successful/unsuccessful actions
*/
public abstract class Task
{
	/**
	 *	Flag to specify running a task forever until the first time it is successful (ie do_try doesn't throw an exception)
	 */
	public static final int UNTIL_SUCCESSFUL = 0;

	/**
	 *	Flag to specify running a task forever regardles of success or failure
	 */
	public static final int NEVER_STOP = -1;

	/**
	*   Allows you to simulate a faulure in do_try()
	*   normal - no auto thrown exception (the default)
	*   simulate_failure - run do_try() as normal but throw a checked exception after
	*   simulate_failure_only - do not run do_try(), just throw a checked exception
	*/
	enum Mode
	{
		normal,
		simulate_failure,
		simulate_failure_only
	}

	private Mode mode = Mode.normal;

	/**
	*   Allows you to change the mode (to normal) this task will run as for devel purposes
	*/
	public final void no_simulated_failures()
	{
		this.mode = Mode.normal;
	}

	/**
	*   Allows you to change the mode (to simulate failure) this task will run as for devel purposes
	*/
	public final void simulate_failure()
	{
		this.mode = Mode.simulate_failure;
	}

	/**
	*   Allows you to change the mode (to simulate failure only) this task will run as for devel purposes
	*/
	public final void simulate_failure_only()
	{
		this.mode = Mode.simulate_failure_only;
	}


	private Boolean successful;

	/**
	*   @return the success state of this task
	*   null if the task hasn't been run yet (or has been reset)
	*   true if no exceptions were thrown in do_try()
	*   false if do_try() threw an exception
	*/
	public final Boolean was_successful()
	{
		return successful;
	}


	/**
	*   Sets this task's successful member to null
	*/
	public final void reset_success()
	{
		successful = null;
	}



	protected LString log = new LString();
	
	public String get_log()
	{
		return log.toString();
	}
	
	public void clear_log()
	{
		log = new LString();
	}

	/**
	*   Override this method to perform your task
	*   Don't call this method your self - its designed to be called at the appropriate time from run_XXXX()
	*   don't worry about putting try/catch/finally blocks around your code in this method as thats what do_catch() and do_finally() are for
	*/
	protected abstract void do_try() throws Exception;


	/**
	*   Override this method to clean up any state that may be borked due to an exception being thrown in do_try()
	*   Don't call this method your self - its designed to be called at the appropriate time from run_XXXX()
	*   do_catch() is somewhat guaranteed to be safe, in that its signature defines no checked exceptions that can be thrown and run_XXXX() will catch any unchecked exceptions and print them to stderr
	*   this does mean, however, that you will need to have your own try/catch/finally block in this method if its actions may throw checked exceptions, or if you want to handle any unchecked exceptions yourself
	*/
	protected void do_catch( Exception ex )
	{
	}


	/**
	*   Override this method to clean up any state or objects opened in do_try()
	*   Don't call this method your self - its designed to be called at the appropriate time from run_XXXX()
	*   do_finally() is somewhat guaranteed to be safe, in that its signature defines no checked exceptions that can be thrown and run_XXXX() will catch any unchecked exceptions and print them to stderr
	*   this does mean, however, that you will need to have your own try/catch/finally block in this method if its actions may throw checked exceptions, or if you want to handle any unchecked exceptions yourself
	*/
	protected void do_finally()
	{
	}


	/**
	*   I've defined this method on order to stop the coder from accidentally copying and pasting the do_catch signature and then wondering why their do_finally block wasn't being called
	*/
	private final void do_finally( Exception ex )
	{
	}

	public String toString()
	{
		return this.getClass().getName();
	}


	/**
	*   runs this tasks do_try() (and do_catch() if do_try() threw an exception) and do_finally(), but in a new thread
	*/
	public void do_in_new_thread()
	{
		do_in_new_thread( 1 );
	}


	/**
	*   runs this tasks do_try() (and do_catch() if do_try() threw an exception) and do_finally(), but in a new thread
	*/
	public void do_in_new_thread( final int max_loops )
	{
		Runnable task_runner = new Runnable()
		{
			public void run()
			{
				do_safe( max_loops );
			}
		};

		TaskRunner.execute( task_runner );
	}


	/**
	*   runs this tasks do_try() (and do_catch() if do_try() threw an exception) and do_finally() once
	*/
	public void do_safe()
	{
		do_safe( 1 );
	}


	/**
	*   runs this tasks do_try() (and do_catch() if do_try() threw an exception) and do_finally() once
	*/
	public void do_unsafe() throws Exception
	{
		do_unsafe( 1 );
	}


	/**
	*   runs this tasks do_try() (and do_catch() if do_try() threw an exception) and do_finally() [loops] times
	*/
	public void do_safe( int max_loops )
	{
		int loops = 0;

		while( true )
		{
			loops ++;

			try
			{
				reset_success();

				if( mode == Mode.normal || mode == Mode.simulate_failure )
				{
					cverbose.println( "running %s do_try()", this );

					this.do_try();

					cverbose.println( "%s do_try() method has thrown no errors", this );
				}

				if( mode == Mode.simulate_failure || mode == Mode.simulate_failure_only )
				{
					cwarn.println( "%s mode is set to %s", this, mode );

					throw new Exception( String.format( "Simulating failure in %s do_try()", this ) );
				}

				successful = true;
			}
			catch( Exception ex ) // catches checked and unchecked exceptions, but not errors
			{
				cerr.println( "Error: %s do_try() method has thrown the exception:", this );

				ex.printStackTrace( System.err );

				successful = false;

				try
				{
					cverbose.println( "running %s do_catch()", this );

					this.do_catch( ex );

					cverbose.println( "%s do_catch() method has thrown no errors", this );
				}
				catch( RuntimeException ex2 )
				{
					cerr.println( "Error unsafe do_catch method in %s has thrown the runtime-exception:", this );

					ex2.printStackTrace( System.err );
				}
			}
			finally
			{
				try
				{
					cverbose.println( "running %s do_finally()", this );

					this.do_finally();

					cverbose.println( "%s do_finally() method has thrown no errors", this );
				}
				catch( RuntimeException ex2 )
				{
					cerr.println( "Error unsafe do_finally method in %s has thrown the runtime-exception:", this );

					ex2.printStackTrace( System.err );
				}
			}

			if( max_loops >= 0 )
			{
				if( was_successful() ) break;
			}

			if( max_loops > 0 )
			{
				if( loops >= max_loops ) break;
			}
		}
	}


	/**
	*   runs this tasks do_try() (and do_catch() if do_try() threw an exception) and do_finally()  [loops] times and iff do_try() generated an exception, re-throws it (even if [loops]
	*/
	public void do_unsafe( int max_loops ) throws Exception
	{
		if( max_loops < 1 ) throw new IllegalArgumentException( "do_unsafe can't run endlessly" );

		int loops = 0;

		while( true )
		{
			loops ++;

			try
			{
				reset_success();

				if( mode == Mode.normal || mode == Mode.simulate_failure )
				{
					cverbose.println( "running %s do_try()", this );

					this.do_try();

					cverbose.println( "%s do_try() method has thrown no errors", this );
				}

				if( mode == Mode.simulate_failure || mode == Mode.simulate_failure_only )
				{
					cwarn.println( "%s mode is set to %s", this, mode );

					throw new Exception( String.format( "Simulating failure in %s do_try()", this ) );
				}

				successful = true;
			}
			catch( Exception ex ) // catches checked and unchecked exceptions, but not errors
			{
				cerr.println( "Error: %s do_try() method has thrown the exception:", this );

				ex.printStackTrace( System.err );

				successful = false;

				try
				{
					cverbose.println( "running %s do_catch()", this );

					this.do_catch( ex );

					cverbose.println( "%s do_catch() method has thrown no errors", this );
				}
				catch( RuntimeException ex2 )
				{
					cerr.println( "Error unsafe do_catch method in %s has thrown the runtime-exception:", this );

					ex2.printStackTrace( System.err );
				}

				if( loops == max_loops ) throw ex;
			}
			finally
			{
				try
				{
					cverbose.println( "running %s do_finally()", this );

					this.do_finally();

					cverbose.println( "%s do_finally() method has thrown no errors", this );
				}
				catch( RuntimeException ex2 )
				{
					cerr.println( "Error unsafe do_finally method in %s has thrown the runtime-exception:", this );

					ex2.printStackTrace( System.err );
				}
			}

			if( was_successful() ) break;

			if( loops >= max_loops ) break;
		}
	}



	protected void not_implemented() throws Exception
	{
		cverbose.println( "running %s not_implemented()", this );

		throw new Exception( String.format( "Code in %s has reached a not-implemented break point", this ) );
	}
}

package jsi3.lib.system;

import java.util.concurrent.*;

/**
*	Utility class for running tasks
*/
public class TaskRunner
{
	private static ExecutorService thread_pool = Executors.newCachedThreadPool( Executors.defaultThreadFactory() );
	
	/**
	 *	Only call this when you are not going to exec any more tasks
	 */
	public static void shutdown_thread_pool()
	{
		thread_pool.shutdown();
	}


	public static void execute( Runnable runnable )
	{
		thread_pool.execute( runnable );
	}
	

	public static <T extends Task> T run_safe( T task )
	{
		task.do_safe( 1 );

		return task;
	}

	public static <T extends Task> T run_unsafe( T task ) throws Exception
	{
		task.do_unsafe( 1 );

		return task;
	}

	public static <T extends Task> T run_safe( T task, int max_tries )
	{
		task.do_safe( max_tries );

		return task;
	}

	public static <T extends Task> T run_unsafe( T task, int max_tries ) throws Exception
	{
		task.do_unsafe( max_tries );

		return task;
	}
	
	public static <T extends Task> T run_in_new_thread( T task )
	{
		task.do_in_new_thread( 1 );

		return task;
	}

	public static <T extends Task> T run_in_new_thread( T task, int max_tries )
	{
		task.do_in_new_thread( max_tries );

		return task;
	}

	public static <T extends Task> T run_as_daemon( T task )
	{
		task.do_in_new_thread( Task.NEVER_STOP );

		return task;
	}
}

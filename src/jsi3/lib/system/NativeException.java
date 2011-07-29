package jsi3.lib.system;

public class NativeException extends Exception
{
	public final ProcessResults process_results;

	public NativeException( ProcessResults process_results )
	{
		super( process_results.toString() );

		this.process_results = process_results;
	}
}

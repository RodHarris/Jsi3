package jsi3.lib.system;

import jsi3.lib.text.*;


public class ProcessResults
{
	public int exit_code;

	public String std_out;

	public String std_err;
	
	public String toString()
	{
		EString es = new EString();
		
		es.println( "exit_code = %d", exit_code );
		
		es.println( "std_out = %s", std_out );
		
		es.println( "std_err = %s", std_err );
		
		return es.toString().trim();
	}
}

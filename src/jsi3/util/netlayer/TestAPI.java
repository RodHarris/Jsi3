package jsi3.util.netlayer;


public interface TestAPI
{
	int get_a();
	
	void set_a( int a ) throws IllegalArgumentException, IllegalStateException;
	
	double rnd();
	
	String cat( String s0, String s1 );
	
	String echo( String s );

	int[] sort( int[] nums );
}

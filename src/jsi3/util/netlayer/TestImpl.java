package jsi3.util.netlayer;


class TestImpl implements TestAPI
{
	private int a;
	
	
	public int get_a()
	{
		return a;
	}
	
	
	public void set_a( int a ) throws IllegalArgumentException, IllegalStateException
	{
		this.a = a;
	}
	
	
	public double rnd()
	{
		return Math.random();
	}
	
	
	public String cat( String s0, String s1 )
	{
		return s0 + s1;
	}
	
	
	public String echo( String s )
	{
		return s;
	}


	public int[] sort( int[] nums )
	{
		java.util.Arrays.sort( nums );

		return nums;
	}
}

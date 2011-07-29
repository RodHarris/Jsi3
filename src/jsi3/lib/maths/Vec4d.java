package jsi3.lib.maths;


public class Vec4d
{
	public double x, y, z, w;
	
	public String print_format = "%.2f";
	
	
	public Vec4d()
	{
	}
	
	public Vec4d( Vec4d other )
	{
		set( other );
	}
	
	public Vec4d( double x, double y, double z, double w )
	{
		set( x, y, z, w );
	}
	
	public void set( Vec4d other )
	{
		set( other.x, other.y, other.z, other.w );
	}
	
	public void set( double x, double y, double z, double w )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public String toString()
	{
		return String.format( String.format( "( %1$s, %1$s, %1$s, %1$s )", print_format ), x, y, z, w );
	}
}

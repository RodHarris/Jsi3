package jsi3.lib.maths;


public class Vec2d
{
	public double x, y;
	
	public String print_format = "%.2f";
	
	public Vec2d()
	{
	}
	
	public Vec2d( Vec2d other )
	{
		set( other );
	}
	
	public Vec2d( double x, double y )
	{
		set( x, y );
	}
	
	public void set( Vec2d other )
	{
		set( other.x, other.y );
	}
	
	public void set( double x, double y )
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return String.format( String.format( "( %1$s, %1$s )", print_format ), x, y );
	}
	
	public double getX()
	{
		return x;
	}
	
	public void setX( double x )
	{
		this.x = x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setY( double y )
	{
		this.y = y;
	}
	
	public String getPrint_format()
	{
		return print_format;
	}
	
	public void setPrint_format( String print_format )
	{
		this.print_format = print_format;
	}
	
	
}

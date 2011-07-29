package jsi3.lib.maths;


public final class Vec2i
{
	public int x, y;
	
	public Vec2i()
	{
	}
	
	public Vec2i( Vec2i other )
	{
		set( other );
	}
	
	public Vec2i( int x, int y )
	{
		set( x, y );
	}
	
	public void set( Vec2i other )
	{
		set( other.x, other.y );
	}
	
	public void set( int x, int y )
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return String.format( "( %d, %d )", x, y );
	}
}

package jsi3.lib.maths;

//import org.simpleframework.xml.*;

public class Vec3d// extends Vec2d
{
	//@Element
	public double x, y, z;
	
	public String print_format = "%.2f";
	
	
	public Vec3d()
	{
	}
	
	
	public Vec3d( Vec3d other )
	{
		set( other );
	}
	
	
	public Vec3d( double x, double y, double z )
	{
		set( x, y, z );
	}
	
	
	public void set( Vec3d other )
	{
		set( other.x, other.y, other.z );
	}
	
	
	public void set( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public void zero()
	{
		x = y = z = 0;
	}
	
	
	public String toString()
	{
		return String.format( String.format( "( %1$s, %1$s, %1$s )", print_format ), x, y, z );
	}
	
	
	public double length_squared()
	{
		return x * x + y * y + z * z;
	}
	
	
	public double length()
	{
		return Math.sqrt( length_squared() );
	}
	
	
	public void normalise()
	{
		scale( 1.0 / length() );
	}
	
	
	/**
	adds the arguments to this vector ( this vector is not zeroed first )
	*/
	public void add( Vec3d... vs )
	{
		for( Vec3d v : vs )
		{
			x += v.x;
			y += v.y;
			z += v.z;
		}
	}
	
	
	/**
	set this vector to be equals to the sum of the arguments ( this vector is zeroed first )
	*/
	public void sum( Vec3d... vs )
	{
		zero();
		
		for( Vec3d v : vs )
		{
			x += v.x;
			y += v.y;
			z += v.z;
		}
	}
	
	
	/**
	this += s * v;
	*/
	public void add( double s, Vec3d v )
	{
		x += s * v.x;
		y += s * v.y;
		z += s * v.z;
	}
	
	
	/**
	this -= v;
	*/
	public void sub( Vec3d v )
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	
	/**
	this = v0 - v1;
	*/
	public void sub( Vec3d v0, Vec3d v1 )
	{
		x = v0.x - v1.x;
		y = v0.y - v1.y;
		z = v0.z - v1.z;
	}
	
	
	/**
	this *= s;
	*/
	public void scale( double s )
	{
		mult( s );
	}
	
	/**
	this *= s;
	*/
	public void mult( double s )
	{
		x *= s;
		y *= s;
		z *= s;
	}
	
	/**
	this = s * v
	*/
	public void set( double s, Vec3d v )
	{
		x = v.x * s;
		y = v.y * s;
		z = v.z * s;
	}
	
	
	/**
	this = v1 x v2
	*/
	public void cross( Vec3d v1, Vec3d v2 )
	{
		x = v1.y * v2.z - v1.z * v2.y;
		y = v1.z * v2.x - v1.x * v2.z;
		z = v1.x * v2.y - v1.y * v2.x;
	}
	
	
	/**
	returns ( this . v )
	*/
	public double dot( Vec3d v )
	{
		return x * v.x + y * v.y + z * v.z;
	}
	
	
	/**
	sets this vector to be the average of the arguments ( this vectors initial values are not included )
	*/
	public void avg( Vec3d... vs )
	{
		zero();
		
		for( Vec3d v : vs )
		{
			x += v.x;
			y += v.y;
			z += v.z;
		}
		
		x /= vs.length;
		y /= vs.length;
		z /= vs.length;
	}
}

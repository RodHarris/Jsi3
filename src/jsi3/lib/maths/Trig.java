package jsi3.lib.maths;


public class Trig
{
	public static final double PI = Math.PI;
	
	public static final double RAD_TO_DEG = 180.0 / PI;
	
	public static final double DEG_TO_RAD = PI / 180.0;
	
	private double angle_in_mult = 1.0;
	
	private double angle_out_mult = 1.0;

	private boolean locked;
	
	
	/**
	 * Creates an object for doing trigonometry calculations
	 * <p>
	 * The units can be changed at runtime by calling the degrees() or radians() methods
	 * The Statics class has immutable public static instances of both a degrees Trig and a radians Trig 
	 * by default and they're thread safe so there's 
	 * probably not much reason to create new ones of these
	 * @see jsi3.lib.maths.Statics#deg
	 * @see jsi3.lib.maths.Statics#rad
	 * @see #radians
	 * @see #degrees
	 */
	public Trig()
	{
	}
	
	
	/**
	 * Creates an object for doing trigonometry calculations assuming input and output units are degrees
	 * The Statics class has an immutable public static instance of one of these by default and they're thread safe so there's 
	 * probably not much reason to create new ones of these
	 * @see jsi3.lib.maths.Statics#deg
	 */
	public static Trig create_immutable_degrees_trig()
	{
		Trig trig = new Trig();

		trig.degrees();

		trig.locked = true;

		return trig;
	}
	

	/**
	 * Creates an object for doing trigonometry calculations assuming input and output units are radians
	 * The Statics class has an immutable public static instance of one of these by default and they're thread safe so there's 
	 * probably not much reason to create new ones of these
	 * @see jsi3.lib.maths.Statics#rad
	 */
	public static Trig create_immutable_radians_trig()
	{
		Trig trig = new Trig();

		trig.radians();

		trig.locked = true;

		return trig;
	}


	/**
	 * use radians - all calls to this Trig object's functions and returned values are now assumed to be in radians
	 * @throws IllegalStateException if this Trig object is immutable
	*/
	public void radians()
	{
		if( locked ) throw new IllegalStateException( "This Trig instance is immutable" );
		
		angle_in_mult = 1;
		
		angle_out_mult = 1;
	}
	
	/**
	 * use degrees - all calls to this Trig object's functions and returned values are now assumed to be in degrees
	 * @throws IllegalStateException if this Trig object is immutable
	*/
	public void degrees()
	{
		if( locked ) throw new IllegalStateException( "This Trig instance is immutable" );
		
		angle_in_mult = DEG_TO_RAD;
		
		angle_out_mult = RAD_TO_DEG;
	}
	
	
	private double angle_in( double angle )
	{
		return angle_in_mult * angle;
	}
	
	
	private double angle_out( double angle )
	{
		return angle_out_mult * angle;
	}

	
	public static double radians( double degrees )
	{
		return degrees * DEG_TO_RAD;
	}
	
	
	public static double degrees( double radians )
	{
		return radians * RAD_TO_DEG;
	}
	
	
	public double cos( double angle )
	{
		return Math.cos( angle_in( angle ) );
	}
	
	
	public double sin( double angle )
	{
		return Math.sin( angle_in( angle ) );
	}
	
	
	public double tan( double angle )
	{
		return Math.tan( angle_in( angle ) );
	}
	
	
	public double atan( double v )
	{
		return angle_out( Math.atan( v ) );
	}

	public double acos( double v )
	{
		return angle_out( Math.acos( v ) );
	}
	
	public double asin( double v )
	{
		return angle_out( Math.asin( v ) );
	}
	
	
	public Vec3d cylindrical_to_cartesian( double r, double theta, double h )
	{
		Vec3d result = new Vec3d();
		
		cylindrical_to_cartesian( r, theta, h, result );
		
		return result;
	}
	
	
	public void cylindrical_to_cartesian( double r, double theta, double h, Vec3d result )
	{
		double cos_theta = cos( theta );
		
		double sin_theta = sin( theta );
		
		result.x = r * cos_theta;
		result.y = h;
		result.z = r * sin_theta;
	}
	
	
	public Vec3d polar_to_cartesian( double r, double theta, double phi )
	{
		Vec3d result = new Vec3d();
		
		polar_to_cartesian( r, theta, phi, result );
		
		return result;
	}
	
	
	public void polar_to_cartesian( double r, double theta, double phi, Vec3d result )
	{
		double cos_theta = cos( theta );
		double cos_phi = cos( phi );
		
		double sin_theta = sin( theta );
		double sin_phi = sin( phi );
		
		result.x = r * cos_theta * cos_phi;
		result.y = r * sin_phi;
		result.z = r * sin_theta * cos_phi;
	}

	/**
	 * Rotate a point p by angle theta around an arbitrary axis r
	 * <p>
	 * Positive angles are anticlockwise looking down the axis towards the origin.
	 * Assume right hand coordinate system.
	 * @param p the point to be rotated
	 * @param theta the angle
	 * @param r the axis of rotation
	 * @return a new rotated point
	*/
	public Vec3d rotate( Vec3d p, double theta, Vec3d r )
	{
		Vec3d q = new Vec3d();
		
		r.normalise();
		
		double costheta = cos( theta );
		double sintheta = sin( theta );
		
		q.x += ( costheta + ( 1 - costheta ) * r.x * r.x ) * p.x;
		q.x += ( ( 1 - costheta ) * r.x * r.y - r.z * sintheta ) * p.y;
		q.x += ( ( 1 - costheta ) * r.x * r.z + r.y * sintheta ) * p.z;
		
		q.y += ( ( 1 - costheta ) * r.x * r.y + r.z * sintheta ) * p.x;
		q.y += ( costheta + ( 1 - costheta ) * r.y * r.y ) * p.y;
		q.y += ( ( 1 - costheta ) * r.y * r.z - r.x * sintheta ) * p.z;
		
		q.z += ( ( 1 - costheta ) * r.x * r.z - r.y * sintheta ) * p.x;
		q.z += ( ( 1 - costheta ) * r.y * r.z + r.x * sintheta ) * p.y;
		q.z += ( costheta + ( 1 - costheta ) * r.z * r.z ) * p.z;
		
		return q;
	}


	/**
	 * The angle between 2 points given in right-ascencion, declination corrdinates
	 * The angles (both in and out) are assumed to be in either degrees or radians depending on whether the corresponding methods have been called
	 */
	public double angle( double ra1, double dec1, double ra2, double dec2 )
	{
		Vec3d v1 = polar_to_cartesian( 1, ra1, dec1 );
		
		Vec3d v2 = polar_to_cartesian( 1, ra2, dec2 );
		
		return angle( v1, v2 );
	}


	/**
	 * Returns the angle between the 2 given vectors
	 * coppies of the 2 input vectors are normalised in this process
	 */
	public double angle( Vec3d v1, Vec3d v2 )
	{
		return angle_out( ( double ) Math.acos( Statics.dot( Statics.normalise( v1 ), Statics.normalise( v2 ) ) ) );
	}
}

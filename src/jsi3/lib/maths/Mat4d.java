package jsi3.lib.maths;

public class Mat4d
{
	public double m00, m01, m02, m03;
	public double m10, m11, m12, m13;
	public double m20, m21, m22, m23;
	public double m30, m31, m32, m33;

	/**
		sets the values in column major order
	*/
	public void setGL( double[] gl_matrix )
	{
		m00 = gl_matrix[ 0 ];
		m10 = gl_matrix[ 1 ];
		m20 = gl_matrix[ 2 ];
		m30 = gl_matrix[ 3 ];

		m01 = gl_matrix[ 4 ];
		m11 = gl_matrix[ 5 ];
		m21 = gl_matrix[ 6 ];
		m31 = gl_matrix[ 7 ];

		m02 = gl_matrix[ 8 ];
		m12 = gl_matrix[ 9 ];
		m22 = gl_matrix[ 10 ];
		m32 = gl_matrix[ 11 ];

		m03 = gl_matrix[ 12 ];
		m13 = gl_matrix[ 13 ];
		m23 = gl_matrix[ 14 ];
		m33 = gl_matrix[ 15 ];
	}

	public String toString()
	{
		return String.format
		(
			"%.2f, %.2f, %.2f, %.2f,\n%.2f, %.2f, %.2f, %.2f,\n%.2f, %.2f, %.2f, %.2f,\n%.2f, %.2f, %.2f, %.2f,\n",
			m00, m01, m02, m03,
			m10, m11, m12, m13,
			m20, m21, m22, m23,
			m30, m31, m32, m33
		);
	}
}

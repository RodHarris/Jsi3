package jsi3.lib.gui;

import java.awt.image.*;

import static jsi3.lib.gui.Statics.*;


public class ColorScaleFilter extends JSIImageFilter
{
	private int r, g, b;


	public ColorScaleFilter( int col )
	{
		r = argbR( col );
		g = argbG( col );
		b = argbB( col );
	}


	public int shade( int x, int y, int argb )
	{
		float or = argbR( argb );
		float og = argbG( argb );
		float ob = argbB( argb );

		float v = 1.0f - ( ( or + og + ob ) / 255.0f / 3.0f );

		//if( v < 0.25f ) v = 0.25f;

		int nr = (int) ( v * r );
		int ng = (int) ( v * g );
		int nb = (int) ( v * b );

		return argb( 255, nr, ng, nb );
	}
}

package jsi3.lib.gui;

import java.awt.image.*;

import static jsi3.lib.gui.Statics.*;


public abstract class JSIImageFilter
{
	private ImageTypes type;

	public static ImageTypes default_type = ImageTypes.argb;


	public JSIImageFilter()
	{
		type = default_type;
	}


	public JSIImageFilter( ImageTypes type )
	{
		this.type = type;
	}


	public void set_type_rgb()
	{
		type = ImageTypes.rgb;
	}


	public void set_type_argb()
	{
		type = ImageTypes.argb;
	}


	public BufferedImage apply( BufferedImage image )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();

		BufferedImage new_image = create_image( iw, ih, type );

		for( int x=0; x<iw; x++ )
		{
			for( int y=0; y<ih; y++ )
			{
				int argb = image.getRGB( x, y ); // argb

				int new_argb = shade( x, y, argb );

				new_image.setRGB( x, y, new_argb );
			}
		}

		return new_image;
	}


	public abstract int shade( int x, int y, int argb );
}

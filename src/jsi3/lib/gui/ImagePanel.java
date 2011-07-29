package jsi3.lib.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;

public class ImagePanel extends JPanel
{
	private BufferedImage image;

	private int image_width;

	private int image_height;

	private boolean grid;


	public ImagePanel()
	{
	}

	public ImagePanel( BufferedImage image )
	{
		update( image );
	}

	public void update( BufferedImage image )
	{
		this.image = image;

		if( image != null )
		{
			image_width = image.getWidth();

			image_height = image.getHeight();
		}

		repaint();
	}

	public void no_grid()
	{
		grid = false;
	}

	public void grid()
	{
		grid = true;
	}


	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		if( image == null ) return;

		int panel_width = getWidth();

		int panel_height = getHeight();

		if( grid )
		{
			for( int xi=0; xi<panel_width; xi+=10 )
			{
				g.drawLine( xi, 0, xi, panel_height );
			}
			for( int yi=0; yi<panel_height; yi+=10 )
			{
				g.drawLine( 0, yi, panel_width, yi );
			}
		}

		float pw = panel_width;

		float ph = panel_height;

		float iw = image_width;

		float ih = image_height;

		float sih;

		float siw;

		if( pw / ph >= iw / ih )
		{
			sih = panel_height;

			siw = panel_height * iw / ih;
		}
		else
		{
			siw = panel_width;

			sih = panel_width * ih / iw;
		}

		int scaled_image_height = (int) sih;

		int scaled_image_width = (int) siw;

		int x = ( panel_width - scaled_image_width ) / 2;

		int y = ( panel_height - scaled_image_height ) / 2;

		//g.drawImage( image, x, y, null );

		g.drawImage( image, x, y, scaled_image_width + x, scaled_image_height + y, 0, 0, image_width, image_height, null );
	}
}

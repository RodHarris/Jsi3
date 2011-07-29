package jsi3.lib.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;


public class ImageViewer
{
	public final JFrame frame;

	private ImagePanel image_panel;


	public ImageViewer( BufferedImage image )
	{
		frame = new JFrame( "Image Viewer" );

		frame.setSize( 800, 600 );

		image_panel = new ImagePanel( image );

		frame.getContentPane().add( image_panel );

		frame.setVisible( true );
	}


	public void update( BufferedImage image )
	{
		image_panel.update( image );
	}
}

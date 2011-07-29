package jsi3.lib.gui;

import java.awt.*;

import javax.swing.*;

public abstract class DrawPanel extends JPanel
{
	protected Graphics2D g2d;

	protected boolean clear = true;
	
	public void paintComponent( Graphics g )
	{
		if( clear ) super.paintComponent( g );
		
		g2d = (Graphics2D) g;
		
		draw();
	}
	
	
	protected void set_colour( int c )
	{
		g2d.setColor( new Color( c ) );
	}
	
	
	
	public abstract void draw();
}

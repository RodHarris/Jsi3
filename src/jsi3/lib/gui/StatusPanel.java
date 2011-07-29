package jsi3.lib.gui;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class StatusPanel extends JPanel
{
	JLabel label = new JLabel();
	
	
	public StatusPanel()
	{
		super( new BorderLayout() );
		
		add( label, BorderLayout.CENTER );
	}
	
	
	public void set_text( String msg, Object... args )
	{
		label.setText( String.format( msg, args ) );
	}
	
	
	public void attach( JFrame frame )
	{
		frame.add( this, BorderLayout.SOUTH );
	}
}

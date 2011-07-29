package jsi3.lib.gui;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.image.*;

import static jsi3.lib.system.Statics.*;
import static jsi3.lib.gui.Statics.*;
import static jsi3.lib.console.Statics.*;
import static jsi3.lib.text.Statics.*;

public class ImageSelectionPanel extends JPanel
{
	private final String[] image_filenames;
	
	private int selection_index;
	
	private ImagePanel image_panel;
	
	private JLabel img_title_label;
	
	private boolean img_error;
	
	
	public ImageSelectionPanel( String... image_filenames ) throws Exception
	{
		check( image_filenames != null, "Can't pass no image filenames to ImageSelectionPanel constructor" );
		
		check( image_filenames.length > 0, "Can't pass no image filenames to ImageSelectionPanel constructor" );
		
		this.image_filenames = image_filenames;
		
		image_panel = new ImagePanel();
		
		JPanel center_panel = new JPanel( new BorderLayout() );
		
		center_panel.setBorder( new TitledBorder( "Image" ) );
		
		center_panel.add( image_panel );

		setLayout( new BorderLayout() );
		
		JPanel west_panel = new JPanel();
		
		west_panel.setLayout( new VerticalLayout( 5, VerticalLayout.CENTER, VerticalLayout.CENTER ) );
		
		JPanel east_panel = new JPanel();
		
		east_panel.setLayout( new VerticalLayout( 5, VerticalLayout.CENTER, VerticalLayout.CENTER ) );
		
		center_panel.add( west_panel, BorderLayout.WEST );
		
		west_panel.add( new JButton( create_action( "Prev", this, "previous_image" ) ) );
		
		center_panel.add( east_panel, BorderLayout.EAST );
		
		east_panel.add( new JButton( create_action( "Next", this, "next_image" ) ) );
		
		add( center_panel, BorderLayout.CENTER );
		
		JPanel img_title_panel = new JPanel( new BorderLayout() );
		
		img_title_label = new JLabel();
		
		img_title_panel.add( img_title_label );
		
		add( img_title_panel, BorderLayout.NORTH );
		
		img_title_panel.setBorder( new TitledBorder( "File" ) );
		
		this_image();
	}


	public void next_image()
	{
		selection_index ++;
		
		if( selection_index == image_filenames.length ) selection_index = 0;
		
		this_image();
	}


	public void previous_image()
	{
		selection_index --;
		
		if( selection_index == -1 ) selection_index += image_filenames.length;
		
		this_image();
	}
	
	
	private void this_image()
	{
		try
		{
			BufferedImage image = load_image( image_filenames[ selection_index ] );
			
			image_panel.update( image );
			
			img_title_label.setText( fmt( "%s (%dx%d)", image_filenames[ selection_index ], image.getWidth(), image.getHeight() ) );
			
			img_error = false;
		}
		catch( Exception ex )
		{
			cerr.println( ex_to_string( ex ) );
			
			image_panel.update( (BufferedImage) null );
			
			img_title_label.setText( fmt( "%s (Error)", image_filenames[ selection_index ] ) );
			
			img_error = true;
		}
	}
	
	
	public String get_selected_image_filename()
	{
		if( img_error ) return null;
		
		return image_filenames[ selection_index ];
	}

}

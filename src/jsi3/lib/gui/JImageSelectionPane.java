package jsi3.lib.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;

import static jsi3.lib.system.Statics.*;
import static jsi3.lib.gui.Statics.*;
import static jsi3.lib.console.Statics.*;

public class JImageSelectionPane extends JDialog
{
	private final ImageSelectionPanel isp;
	
	private boolean cancelled;
	
	
	public static String show_input_dialog( Window owner, String... image_filenames ) throws Exception
	{
		JImageSelectionPane dialog = new JImageSelectionPane( owner, image_filenames );
		
		resize_window( dialog, 0.35, 0.35 );
		
		position_window( dialog, 0.5, 0.5 );
		
		dialog.cancelled = true;
		
		dialog.setVisible( true );
		
		if( dialog.cancelled ) return null;
		
		return dialog.isp.get_selected_image_filename();
	}	
	
	
	public JImageSelectionPane( Window owner, String... image_filenames ) throws Exception
	{
		super( owner, "Select an Image", Dialog.ModalityType.APPLICATION_MODAL );
		
		setLayout( new BorderLayout() );
		
		isp = new ImageSelectionPanel( image_filenames );
		
		add( isp, BorderLayout.CENTER );
		
		JPanel button_panel = new JPanel();
		
		add( button_panel, BorderLayout.SOUTH );
		
		button_panel.add( new JButton( create_action( "Cancel", this, "cancel" ) ) );
		
		button_panel.add( new JButton( create_action( "Select", this, "select" ) ) );
	}
	
	public void cancel()
	{
		cancelled = true;
		
		setVisible( false );
	}
	
	public void select()
	{
		cancelled = false;
		
		setVisible( false );
	}
}

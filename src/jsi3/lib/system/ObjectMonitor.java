
package jsi3.lib.system;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


public class ObjectMonitor extends JFrame
{
	// class data

	public static final ObjectMonitor default_dialog;
	
	private static String updateButtonName;


	// member data

	private boolean guiCreated;
	
	public boolean autoSwitch;
	
	private Vector<Monitor> monitors;

	private Vector<String> colNames;
	
	private JTextArea textArea;
	
	private JPanel buttonPanel;
	
	private JScrollPane tablePane;
	
	private int index;
	
	private JTable table;
	
	private JRadioButton radio_toString;

	private JRadioButton radio_reflection;

	private JCheckBox check_update;
	
	private JCheckBox check_auto_switch;
	
	
	static
	{
		updateButtonName = "Update";
		
		default_dialog = new ObjectMonitor();
	}

	
	public static ObjectMonitor get()
	{
		return default_dialog;
	}


	public static synchronized void monitor( String name, Object o, int time )
	{
		default_dialog.monitorObject( name, o, time );
	}


	public static void exitWhenClosed( int... monitors )
	{
		default_dialog.setDefaultCloseOperation( EXIT_ON_CLOSE );
	}
		
	
	private ObjectMonitor()
	{
		super( "ObjectMonitor " );

		createGui();
		
		setVisible( true );
	}
	
	
	private void createGui()
	{
		//setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		
		autoSwitch = true;
		
		monitors = new Vector<Monitor>();

		colNames = new Vector<String>();
		
		index = -1;
		
		colNames.add( "Object" );
		
		table = new JTable( 0, 1 );
		
		JPanel mainPanel = new JPanel( new BorderLayout() );
		
		add( mainPanel );
		
		tablePane = new JScrollPane( table );
		
		textArea = new JTextArea();
		
		JScrollPane textPane = new JScrollPane( textArea );

		JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, tablePane, textPane );
		//JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, textPane, tablePane );
		
		mainPanel.add( splitPane, BorderLayout.CENTER );
		
		mainPanel.add( buttonPanel = new JPanel(), BorderLayout.SOUTH );
		
		buttonPanel.setBorder( new CompoundBorder( new EmptyBorder(3,3,3,3), new EtchedBorder() ) );
		
		ButtonGroup radioGroup = new ButtonGroup();
		
		radio_toString = new JRadioButton( "To String" );
		radio_reflection = new JRadioButton( "Reflection" );

		radioGroup.add( radio_toString );
		radioGroup.add( radio_reflection );
		
		check_update = new JCheckBox( "Auto Update" );
		check_auto_switch = new JCheckBox( "Auto Switch" );
		
		buttonPanel.add( check_update );
		addButton( updateButtonName );
		buttonPanel.add( radio_toString );
		buttonPanel.add( radio_reflection );
		buttonPanel.add( check_auto_switch );
		
		radio_toString.setSelected( true );
		
		check_auto_switch.setSelected( true );
		
		radio_toString.addChangeListener( new RadioChangeListener() );
		radio_reflection.addChangeListener( new RadioChangeListener() );
		
		check_update.addActionListener( new CheckActionListener() );
	
		textArea.setEditable( false );
		
		textArea.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		
		guiCreated = true;
		
		//IfSys.test.println3( "Dialog " + instance_num + " created" );
		
		setVisible( true );
		
		splitPane.setResizeWeight( 0.25 );
		splitPane.setDividerLocation( 0.25 );
	}


	private void monitorObject( String name, Object o, int time )
	{
		int oldindex = index;

		Monitor m = new Monitor( name, o, time, monitors.size() );

		//IfSys.out.debugln( "DebugObjectDialog: adding Monitor: " + m );

		monitors.add( m );

		int monitors_size = monitors.size();

		index = monitors_size -1;

		table = new JTable( index + 1, 1 );

		for( int i=0; i<monitors_size; i++ )
		{
			m = monitors.get( i );

			table.setValueAt( m, i, 0 );
		}

		table.clearSelection();

		table.addMouseListener( new TableClickHandler() );

		tablePane.setViewportView( table );

		if( check_auto_switch.isSelected() )
		{
			updateGui();
		}
		else
		{
			index = oldindex;
		}
	}
	
	
	private void moniterTimerHit( int id )
	{
		if( index == id )
		{
			updateGui();
		}
	}
	
	
	private void appendTitle( String s )
	{
		setTitle( "ObjectMonitor - " + s );
	}
	
	
	private void addButton( String buttonName )
	{
		JButton button = new JButton( new ButtonAction( buttonName ) );
		
		buttonPanel.add( button );
	}
	
	
	private void buttonPressed( String buttonName )
	{
		//IfSys.test.println( "buttonPressed" );
		
		if( buttonName.equals( updateButtonName ) )
		{
			updateGui();
		}
	}
	
	
	private void updateGui()
	{
		//IfSys.test.println3( "updateGui" );
		
		if( index >= 0 && index < monitors.size() )
		{
			Monitor m = monitors.get( index );

			if( m.time > 0 )
			{
				check_update.setEnabled( true );

				check_update.setSelected( m.auto_update );
			}
			else
			{
				check_update.setSelected( false );
				
				check_update.setEnabled( false );
			}

			appendTitle( m.toString() );

			if( m.style == m.TO_STRING )
			{
				if( m.o instanceof Object[] )
				{
					textArea.setText( Arrays.toString( (Object[]) m.o ) );
				}
				else
				{
					textArea.setText( m.o.toString() );
				}
				
				radio_toString.setSelected( true );
			}
			else if( m.style == m.REFLECTION )
			{
				textArea.setText( Statics.inspect( m.o ) );
				
				radio_reflection.setSelected( true );
			}
		}
	}
	
	
	private void radioButtonChanged()
	{
		if( index >= 0 && index < monitors.size() )
		{
			Monitor m = monitors.get( index );
			
			if( radio_toString.isSelected() )
			{
				m.style = m.TO_STRING;
				//IfSys.debug.println2( m + " style = toString" );
			}
			else if( radio_reflection.isSelected() )
			{
				m.style = m.REFLECTION;
				//IfSys.debug.println2( m + " style = reflection" );
			}
		}
	}
	
	
	private void checkButtonChanged()
	{
		if( index >= 0 && index < monitors.size() )
		{
			Monitor m = (Monitor) monitors.get( index );

			m.auto_update = check_update.isSelected();
		}
	}
		
		
	private class ButtonAction extends AbstractAction
	{
		private String buttonName;
		
		public ButtonAction( String buttonName )
		{
			super( buttonName );
			
			this.buttonName = buttonName;
		}
		
		public void actionPerformed( ActionEvent e )
		{
			buttonPressed( buttonName );
		}
	}
	
	
	private class TableClickHandler extends MouseInputAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			int row = table.getSelectedRow();
			
			if( row != -1 )
			{
				index = row;
				
				updateGui();
			}
		}
	}
	
	
	private class Monitor
	{
		private String name;
		
		protected Object o;
		
		protected int style;
		
		protected boolean auto_update;
		
		protected int time;
		
		private int id;
		
		public static final int TO_STRING = 0;
		
		public static final int REFLECTION = 1;
		
		
		public Monitor( String name, Object o, int time, int id )
		{
			this.name = name;
			this.o = o;
			this.time = time;
			this.id = id;
			
			style = TO_STRING;
	
			if( time > 0 )
			{
				auto_update = true;
				
				ActionListener taskPerformer = new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						timerHit();
					}
				};
				
				new javax.swing.Timer( time, taskPerformer ).start();
			}
		}
		
		public String toString()
		{
			return name;
		}
		
		private void timerHit()
		{
			if( auto_update )
			{
				moniterTimerHit( id );
			}
		}
	}
	
	private class RadioChangeListener implements ChangeListener
	{
		public void stateChanged( ChangeEvent e )
		{
			radioButtonChanged();
		}
	}
	
	private class CheckActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//IfSys.debug.println3( "checkButtonChanged" );
			
			checkButtonChanged();
		}
	}
}

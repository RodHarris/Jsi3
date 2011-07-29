package jsi3.lib.gui;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.util.*;

import static jsi3.lib.maths.Statics.*;
import static jsi3.lib.text.Statics.*;

public class SliderFrame extends JFrame
{
	HashMap<String, IntSliderPanel> int_sliders = new HashMap<String, IntSliderPanel>();
	
	HashMap<String, DoubleSliderPanel> double_sliders = new HashMap<String, DoubleSliderPanel>();
	
	
	public SliderFrame()
	{
		setLayout( new VerticalLayout() );
	}
	
	
	public void add_int_slider( String name, int min, int max, int initial )
	{
		IntSliderPanel isp = new IntSliderPanel( name, min, max, initial );

		if( int_sliders.get( name ) != null ) throw new RuntimeException( "This slider frame already has an int slider with the name: " + name );
		
		int_sliders.put( name, isp );
		
		add( isp );
	}
	

	public void add_double_slider( String name, double min, double max, double initial )
	{
		DoubleSliderPanel dsp = new DoubleSliderPanel( name, min, max, initial );

		if( double_sliders.get( name ) != null ) throw new RuntimeException( "This slider frame already has an double slider with the name: " + name );
		
		double_sliders.put( name, dsp );
		
		add( dsp );
	}
	
	
	public int get_int_value( String name )
	{
		return int_sliders.get( name ).get_value();
	}
	
	
	public double get_double_value( String name )
	{
		return double_sliders.get( name ).get_value();
	}
	
	
	abstract class SliderPanel extends JPanel
	{
		public abstract void change_label_value();
	}
	
	
	class IntSliderPanel extends SliderPanel
	{
		JSlider slider;
		
		JLabel value_label = new JLabel();
		
		IntSliderPanel( String name, int min, int max, int initial )
		{
			setLayout( new BorderLayout() );
			
			slider = new JSlider( min, max, initial );
			
			add( new JLabel( name ), BorderLayout.WEST );
			
			add( slider, BorderLayout.CENTER );
			
			change_label_value();
			
			add( value_label, BorderLayout.EAST );
			
			slider.addChangeListener( new ValueClangeListener( this ) );
		}

		public void change_label_value()
		{
			value_label.setText( "" + get_value() );
		}
		
		public int get_value()
		{
			return slider.getValue();
		}
	}
	
	class DoubleSliderPanel extends SliderPanel
	{
		JSlider slider;
		
		JLabel value_label = new JLabel();
		
		double min, max;
		
		DoubleSliderPanel( String name, double min, double max, double initial )
		{
			this.min = min;
			
			this.max = max;
			
			setLayout( new BorderLayout() );
			
			int imin = (int) jsi3.lib.maths.Statics.pack( min, min, max, 0, 100 );
			
			int imax = (int) jsi3.lib.maths.Statics.pack( max, min, max, 0, 100 );
			
			int iinitial = (int) jsi3.lib.maths.Statics.pack( initial, min, max, 0, 100 );
			
			slider = new JSlider( imin, imax, iinitial );
			
			add( new JLabel( name ), BorderLayout.WEST );
			
			add( slider, BorderLayout.CENTER );
			
			change_label_value();
			
			add( value_label, BorderLayout.EAST );
			
			slider.addChangeListener( new ValueClangeListener( this ) );
		}

		public void change_label_value()
		{
			value_label.setText( sprintf( "%.2f", get_value() ) );
		}
		
		public double get_value()
		{
			return jsi3.lib.maths.Statics.pack( slider.getValue(), 0, 100, min, max );
		}
	}
	
	class ValueClangeListener implements ChangeListener
	{
		SliderPanel slider_panel;
		
		ValueClangeListener( SliderPanel slider_panel )
		{
			this.slider_panel = slider_panel;
		}
		
		public void stateChanged( ChangeEvent e )
		{
			slider_panel.change_label_value();
		}
	}
}

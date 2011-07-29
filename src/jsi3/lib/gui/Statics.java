package jsi3.lib.gui;

import java.util.*;
import java.io.*;
import java.nio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.math.*;
import java.lang.reflect.*;

import javax.imageio.*;
import javax.swing.*;

import javax.imageio.stream.*;

import jsi3.lib.system.*;

import static jsi3.lib.maths.Statics.*;
import static jsi3.lib.system.Statics.*;


public class Statics
{
	static final String BASE_DIR = System.getProperty( "gui.dir" );

	private static int desktop_height;

	private static int desktop_width;

	private static GraphicsDevice[] screens;

	public static int left_mouse = 1;

	public static int right_mouse = 3;

	public static int middle_mouse = 2;

	public static int any_mouse = -1;

	public static int no_mouse = 0;

	public static final HashMap<String, Integer> colours = new HashMap<String, Integer>();

	public static final int AliceBlue = parse_colour( "#F0F8FF" );
	public static final int AntiqueWhite = parse_colour( "#FAEBD7" );
	public static final int Aqua = parse_colour( "#00FFFF" );
	public static final int Aquamarine = parse_colour( "#7FFFD4" );
	public static final int Azure = parse_colour( "#F0FFFF" );
	public static final int Beige = parse_colour( "#F5F5DC" );
	public static final int Bisque = parse_colour( "#FFE4C4" );
	public static final int Black = parse_colour( "#000000" );
	public static final int BlanchedAlmond = parse_colour( "#FFEBCD" );
	public static final int Blue = parse_colour( "#0000FF" );
	public static final int BlueViolet = parse_colour( "#8A2BE2" );
	public static final int Brown = parse_colour( "#A52A2A" );
	public static final int BurlyWood = parse_colour( "#DEB887" );
	public static final int CadetBlue = parse_colour( "#5F9EA0" );
	public static final int Chartreuse = parse_colour( "#7FFF00" );
	public static final int Chocolate = parse_colour( "#D2691E" );
	public static final int Coral = parse_colour( "#FF7F50" );
	public static final int CornflowerBlue = parse_colour( "#6495ED" );
	public static final int Cornsilk = parse_colour( "#FFF8DC" );
	public static final int Crimson = parse_colour( "#DC143C" );
	public static final int Cyan = parse_colour( "#00FFFF" );
	public static final int DarkBlue = parse_colour( "#00008B" );
	public static final int DarkCyan = parse_colour( "#008B8B" );
	public static final int DarkGoldenRod = parse_colour( "#B8860B" );
	public static final int DarkGray = parse_colour( "#A9A9A9" );
	public static final int DarkGrey = parse_colour( "#A9A9A9" );
	public static final int DarkGreen = parse_colour( "#006400" );
	public static final int DarkKhaki = parse_colour( "#BDB76B" );
	public static final int DarkMagenta = parse_colour( "#8B008B" );
	public static final int DarkOliveGreen = parse_colour( "#556B2F" );
	public static final int Darkorange = parse_colour( "#FF8C00" );
	public static final int DarkOrchid = parse_colour( "#9932CC" );
	public static final int DarkRed = parse_colour( "#8B0000" );
	public static final int DarkSalmon = parse_colour( "#E9967A" );
	public static final int DarkSeaGreen = parse_colour( "#8FBC8F" );
	public static final int DarkSlateBlue = parse_colour( "#483D8B" );
	public static final int DarkSlateGray = parse_colour( "#2F4F4F" );
	public static final int DarkSlateGrey = parse_colour( "#2F4F4F" );
	public static final int DarkTurquoise = parse_colour( "#00CED1" );
	public static final int DarkViolet = parse_colour( "#9400D3" );
	public static final int DeepPink = parse_colour( "#FF1493" );
	public static final int DeepSkyBlue = parse_colour( "#00BFFF" );
	public static final int DimGray = parse_colour( "#696969" );
	public static final int DimGrey = parse_colour( "#696969" );
	public static final int DodgerBlue = parse_colour( "#1E90FF" );
	public static final int FireBrick = parse_colour( "#B22222" );
	public static final int FloralWhite = parse_colour( "#FFFAF0" );
	public static final int ForestGreen = parse_colour( "#228B22" );
	public static final int Fuchsia = parse_colour( "#FF00FF" );
	public static final int Gainsboro = parse_colour( "#DCDCDC" );
	public static final int GhostWhite = parse_colour( "#F8F8FF" );
	public static final int Gold = parse_colour( "#FFD700" );
	public static final int GoldenRod = parse_colour( "#DAA520" );
	public static final int Gray = parse_colour( "#808080" );
	public static final int Grey = parse_colour( "#808080" );
	public static final int Green = parse_colour( "#008000" );
	public static final int GreenYellow = parse_colour( "#ADFF2F" );
	public static final int HoneyDew = parse_colour( "#F0FFF0" );
	public static final int HotPink = parse_colour( "#FF69B4" );
	public static final int IndianRed = parse_colour( "#CD5C5C" );
	public static final int Indigo = parse_colour( "#4B0082" );
	public static final int Ivory = parse_colour( "#FFFFF0" );
	public static final int Khaki = parse_colour( "#F0E68C" );
	public static final int Lavender = parse_colour( "#E6E6FA" );
	public static final int LavenderBlush = parse_colour( "#FFF0F5" );
	public static final int LawnGreen = parse_colour( "#7CFC00" );
	public static final int LemonChiffon = parse_colour( "#FFFACD" );
	public static final int LightBlue = parse_colour( "#ADD8E6" );
	public static final int LightCoral = parse_colour( "#F08080" );
	public static final int LightCyan = parse_colour( "#E0FFFF" );
	public static final int LightGoldenRodYellow = parse_colour( "#FAFAD2" );
	public static final int LightGray = parse_colour( "#D3D3D3" );
	public static final int LightGrey = parse_colour( "#D3D3D3" );
	public static final int LightGreen = parse_colour( "#90EE90" );
	public static final int LightPink = parse_colour( "#FFB6C1" );
	public static final int LightSalmon = parse_colour( "#FFA07A" );
	public static final int LightSeaGreen = parse_colour( "#20B2AA" );
	public static final int LightSkyBlue = parse_colour( "#87CEFA" );
	public static final int LightSlateGray = parse_colour( "#778899" );
	public static final int LightSlateGrey = parse_colour( "#778899" );
	public static final int LightSteelBlue = parse_colour( "#B0C4DE" );
	public static final int LightYellow = parse_colour( "#FFFFE0" );
	public static final int Lime = parse_colour( "#00FF00" );
	public static final int LimeGreen = parse_colour( "#32CD32" );
	public static final int Linen = parse_colour( "#FAF0E6" );
	public static final int Magenta = parse_colour( "#FF00FF" );
	public static final int Maroon = parse_colour( "#800000" );
	public static final int MediumAquaMarine = parse_colour( "#66CDAA" );
	public static final int MediumBlue = parse_colour( "#0000CD" );
	public static final int MediumOrchid = parse_colour( "#BA55D3" );
	public static final int MediumPurple = parse_colour( "#9370D8" );
	public static final int MediumSeaGreen = parse_colour( "#3CB371" );
	public static final int MediumSlateBlue = parse_colour( "#7B68EE" );
	public static final int MediumSpringGreen = parse_colour( "#00FA9A" );
	public static final int MediumTurquoise = parse_colour( "#48D1CC" );
	public static final int MediumVioletRed = parse_colour( "#C71585" );
	public static final int MidnightBlue = parse_colour( "#191970" );
	public static final int MintCream = parse_colour( "#F5FFFA" );
	public static final int MistyRose = parse_colour( "#FFE4E1" );
	public static final int Moccasin = parse_colour( "#FFE4B5" );
	public static final int NavajoWhite = parse_colour( "#FFDEAD" );
	public static final int Navy = parse_colour( "#000080" );
	public static final int OldLace = parse_colour( "#FDF5E6" );
	public static final int Olive = parse_colour( "#808000" );
	public static final int OliveDrab = parse_colour( "#6B8E23" );
	public static final int Orange = parse_colour( "#FFA500" );
	public static final int OrangeRed = parse_colour( "#FF4500" );
	public static final int Orchid = parse_colour( "#DA70D6" );
	public static final int PaleGoldenRod = parse_colour( "#EEE8AA" );
	public static final int PaleGreen = parse_colour( "#98FB98" );
	public static final int PaleTurquoise = parse_colour( "#AFEEEE" );
	public static final int PaleVioletRed = parse_colour( "#D87093" );
	public static final int PapayaWhip = parse_colour( "#FFEFD5" );
	public static final int PeachPuff = parse_colour( "#FFDAB9" );
	public static final int Peru = parse_colour( "#CD853F" );
	public static final int Pink = parse_colour( "#FFC0CB" );
	public static final int Plum = parse_colour( "#DDA0DD" );
	public static final int PowderBlue = parse_colour( "#B0E0E6" );
	public static final int Purple = parse_colour( "#800080" );
	public static final int Red = parse_colour( "#FF0000" );
	public static final int RosyBrown = parse_colour( "#BC8F8F" );
	public static final int RoyalBlue = parse_colour( "#4169E1" );
	public static final int SaddleBrown = parse_colour( "#8B4513" );
	public static final int Salmon = parse_colour( "#FA8072" );
	public static final int SandyBrown = parse_colour( "#F4A460" );
	public static final int SeaGreen = parse_colour( "#2E8B57" );
	public static final int SeaShell = parse_colour( "#FFF5EE" );
	public static final int Sienna = parse_colour( "#A0522D" );
	public static final int Silver = parse_colour( "#C0C0C0" );
	public static final int SkyBlue = parse_colour( "#87CEEB" );
	public static final int SlateBlue = parse_colour( "#6A5ACD" );
	public static final int SlateGray = parse_colour( "#708090" );
	public static final int SlateGrey = parse_colour( "#708090" );
	public static final int Snow = parse_colour( "#FFFAFA" );
	public static final int SpringGreen = parse_colour( "#00FF7F" );
	public static final int SteelBlue = parse_colour( "#4682B4" );
	public static final int Tan = parse_colour( "#D2B48C" );
	public static final int Teal = parse_colour( "#008080" );
	public static final int Thistle = parse_colour( "#D8BFD8" );
	public static final int Tomato = parse_colour( "#FF6347" );
	public static final int Turquoise = parse_colour( "#40E0D0" );
	public static final int Violet = parse_colour( "#EE82EE" );
	public static final int Wheat = parse_colour( "#F5DEB3" );
	public static final int White = parse_colour( "#FFFFFF" );
	public static final int WhiteSmoke = parse_colour( "#F5F5F5" );
	public static final int Yellow = parse_colour( "#FFFF00" );
	public static final int YellowGreen = parse_colour( "#9ACD32" );


	static
	{
		colours.put( "AliceBlue", AliceBlue );
		colours.put( "AntiqueWhite", AntiqueWhite );
		colours.put( "Aqua", Aqua );
		colours.put( "Aquamarine", Aquamarine );
		colours.put( "Azure", Azure );
		colours.put( "Beige", Beige );
		colours.put( "Bisque", Bisque );
		colours.put( "Black", Black );
		colours.put( "BlanchedAlmond", BlanchedAlmond );
		colours.put( "Blue", Blue );
		colours.put( "BlueViolet", BlueViolet );
		colours.put( "Brown", Brown );
		colours.put( "BurlyWood", BurlyWood );
		colours.put( "CadetBlue", CadetBlue );
		colours.put( "Chartreuse", Chartreuse );
		colours.put( "Chocolate", Chocolate );
		colours.put( "Coral", Coral );
		colours.put( "CornflowerBlue", CornflowerBlue );
		colours.put( "Cornsilk", Cornsilk );
		colours.put( "Crimson", Crimson );
		colours.put( "Cyan", Cyan );
		colours.put( "DarkBlue", DarkBlue );
		colours.put( "DarkCyan", DarkCyan );
		colours.put( "DarkGoldenRod", DarkGoldenRod );
		colours.put( "DarkGray", DarkGray );
		colours.put( "DarkGrey", DarkGrey );
		colours.put( "DarkGreen", DarkGreen );
		colours.put( "DarkKhaki", DarkKhaki );
		colours.put( "DarkMagenta", DarkMagenta );
		colours.put( "DarkOliveGreen", DarkOliveGreen );
		colours.put( "Darkorange", Darkorange );
		colours.put( "DarkOrchid", DarkOrchid );
		colours.put( "DarkRed", DarkRed );
		colours.put( "DarkSalmon", DarkSalmon );
		colours.put( "DarkSeaGreen", DarkSeaGreen );
		colours.put( "DarkSlateBlue", DarkSlateBlue );
		colours.put( "DarkSlateGray", DarkSlateGray );
		colours.put( "DarkSlateGrey", DarkSlateGrey );
		colours.put( "DarkTurquoise", DarkTurquoise );
		colours.put( "DarkViolet", DarkViolet );
		colours.put( "DeepPink", DeepPink );
		colours.put( "DeepSkyBlue", DeepSkyBlue );
		colours.put( "DimGray", DimGray );
		colours.put( "DimGrey", DimGrey );
		colours.put( "DodgerBlue", DodgerBlue );
		colours.put( "FireBrick", FireBrick );
		colours.put( "FloralWhite", FloralWhite );
		colours.put( "ForestGreen", ForestGreen );
		colours.put( "Fuchsia", Fuchsia );
		colours.put( "Gainsboro", Gainsboro );
		colours.put( "GhostWhite", GhostWhite );
		colours.put( "Gold", Gold );
		colours.put( "GoldenRod", GoldenRod );
		colours.put( "Gray", Gray );
		colours.put( "Grey", Grey );
		colours.put( "Green", Green );
		colours.put( "GreenYellow", GreenYellow );
		colours.put( "HoneyDew", HoneyDew );
		colours.put( "HotPink", HotPink );
		colours.put( "IndianRed", IndianRed );
		colours.put( "Indigo", Indigo );
		colours.put( "Ivory", Ivory );
		colours.put( "Khaki", Khaki );
		colours.put( "Lavender", Lavender );
		colours.put( "LavenderBlush", LavenderBlush );
		colours.put( "LawnGreen", LawnGreen );
		colours.put( "LemonChiffon", LemonChiffon );
		colours.put( "LightBlue", LightBlue );
		colours.put( "LightCoral", LightCoral );
		colours.put( "LightCyan", LightCyan );
		colours.put( "LightGoldenRodYellow", LightGoldenRodYellow );
		colours.put( "LightGray", LightGray );
		colours.put( "LightGrey", LightGrey );
		colours.put( "LightGreen", LightGreen );
		colours.put( "LightPink", LightPink );
		colours.put( "LightSalmon", LightSalmon );
		colours.put( "LightSeaGreen", LightSeaGreen );
		colours.put( "LightSkyBlue", LightSkyBlue );
		colours.put( "LightSlateGray", LightSlateGray );
		colours.put( "LightSlateGrey", LightSlateGrey );
		colours.put( "LightSteelBlue", LightSteelBlue );
		colours.put( "LightYellow", LightYellow );
		colours.put( "Lime", Lime );
		colours.put( "LimeGreen", LimeGreen );
		colours.put( "Linen", Linen );
		colours.put( "Magenta", Magenta );
		colours.put( "Maroon", Maroon );
		colours.put( "MediumAquaMarine", MediumAquaMarine );
		colours.put( "MediumBlue", MediumBlue );
		colours.put( "MediumOrchid", MediumOrchid );
		colours.put( "MediumPurple", MediumPurple );
		colours.put( "MediumSeaGreen", MediumSeaGreen );
		colours.put( "MediumSlateBlue", MediumSlateBlue );
		colours.put( "MediumSpringGreen", MediumSpringGreen );
		colours.put( "MediumTurquoise", MediumTurquoise );
		colours.put( "MediumVioletRed", MediumVioletRed );
		colours.put( "MidnightBlue", MidnightBlue );
		colours.put( "MintCream", MintCream );
		colours.put( "MistyRose", MistyRose );
		colours.put( "Moccasin", Moccasin );
		colours.put( "NavajoWhite", NavajoWhite );
		colours.put( "Navy", Navy );
		colours.put( "OldLace", OldLace );
		colours.put( "Olive", Olive );
		colours.put( "OliveDrab", OliveDrab );
		colours.put( "Orange", Orange );
		colours.put( "OrangeRed", OrangeRed );
		colours.put( "Orchid", Orchid );
		colours.put( "PaleGoldenRod", PaleGoldenRod );
		colours.put( "PaleGreen", PaleGreen );
		colours.put( "PaleTurquoise", PaleTurquoise );
		colours.put( "PaleVioletRed", PaleVioletRed );
		colours.put( "PapayaWhip", PapayaWhip );
		colours.put( "PeachPuff", PeachPuff );
		colours.put( "Peru", Peru );
		colours.put( "Pink", Pink );
		colours.put( "Plum", Plum );
		colours.put( "PowderBlue", PowderBlue );
		colours.put( "Purple", Purple );
		colours.put( "Red", Red );
		colours.put( "RosyBrown", RosyBrown );
		colours.put( "RoyalBlue", RoyalBlue );
		colours.put( "SaddleBrown", SaddleBrown );
		colours.put( "Salmon", Salmon );
		colours.put( "SandyBrown", SandyBrown );
		colours.put( "SeaGreen", SeaGreen );
		colours.put( "SeaShell", SeaShell );
		colours.put( "Sienna", Sienna );
		colours.put( "Silver", Silver );
		colours.put( "SkyBlue", SkyBlue );
		colours.put( "SlateBlue", SlateBlue );
		colours.put( "SlateGray", SlateGray );
		colours.put( "SlateGrey", SlateGrey );
		colours.put( "Snow", Snow );
		colours.put( "SpringGreen", SpringGreen );
		colours.put( "SteelBlue", SteelBlue );
		colours.put( "Tan", Tan );
		colours.put( "Teal", Teal );
		colours.put( "Thistle", Thistle );
		colours.put( "Tomato", Tomato );
		colours.put( "Turquoise", Turquoise );
		colours.put( "Violet", Violet );
		colours.put( "Wheat", Wheat );
		colours.put( "White", White );
		colours.put( "WhiteSmoke", WhiteSmoke );
		colours.put( "Yellow", Yellow );
		colours.put( "YellowGreen", YellowGreen );


		get_desktop_size();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		screens = ge.getScreenDevices();

// 		for( int i=0; i<screens.length; i++ )
// 		{
// 			debug( "screen %d", i );
//
// 			debug( "   screen width = %d", screen_width( i ) );
//
// 			debug( "   screen height = %d", screen_height( i ) );
//
// /*			GraphicsConfiguration[] gc = gd.getConfigurations();
//
// 			debug( "# of GraphicsConfigurations = %d", gc.length );
//
// 			for( GraphicsConfiguration gci : gc )
// 			{
// 				LibSys.debug( gci );
// 			}*/
// 		}

	}



	// *************************************************************	Window Functions


	/**
	*	sets the look and feel to Windows, Aqua or GTK depending on the runtime platform
	*/
	public static void set_platform_look_and_feel()
	{
		try
		{
			if( windows() ) UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );

			//if( linux() ) UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );

			if( linux() ) UIManager.setLookAndFeel( "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" );

			if( mac() ) UIManager.setLookAndFeel( "apple.laf.AquaLookAndFeel" );
		}

		catch( Exception ex )
		{
			ex.printStackTrace( System.err );
		}
	}


	/**
		loads the dimensions of the screen to the static screen_height and screen_width variables and also returns them as a Dimension object
	*/
	public static Dimension get_desktop_size()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension desktop_size = toolkit.getScreenSize();

		desktop_width = (int) desktop_size.getWidth();
		desktop_height = (int) desktop_size.getHeight();

		return desktop_size;
	}


	public static int desktop_width()
	{
		return desktop_width;
	}


	public static int desktop_height()
	{
		return desktop_height;
	}


	public static int screen_width()
	{
		return screen_width( 0 );
	}


	public static int screen_height()
	{
		return screen_height( 0 );
	}


	public static int screen_width( int screen_index )
	{
		return screens[ screen_index ].getDisplayMode().getWidth();
	}


	public static int screen_height( int screen_index )
	{
		return screens[ screen_index ].getDisplayMode().getHeight();
	}


	/**
		sets the size of the window to be a fraction of the desktops width and height
	*/
	public static Window resize_desktop_window( Window window, double fw, double fh )
	{
		int window_width = (int) ( desktop_width * fw );
		int window_height = (int) ( desktop_height * fh );

		window.setSize( window_width, window_height );

		return window;
	}


	/**
		positions the window a certain fraction of the way across the desktop
		<pre>
		eg:
		0   - left/top aligned
		0.5 - centered
		1.0 - right/bottom aligned
		</pre>
	*/
	public static Window position_desktop_window( Window window, double fx, double fy )
	{
		int x = (int) ( ( desktop_width - window.getWidth() ) * fx );
		int y = (int) ( ( desktop_height - window.getHeight() ) * fy );

		window.setLocation( x, y );

		return window;
	}


	public static Window resize_window( Window window, double fw, double fh )
	{
		return resize_window( window, fw, fh, 0 );
	}


	/**
		sets the size of the window to be a fraction of the specified screens width and height
	*/
	public static Window resize_window( Window window, double fw, double fh, int screen_index )
	{
		int window_width = (int) ( screen_width( screen_index ) * fw );
       		 int window_height = (int) ( screen_height( screen_index ) * fh );

		window.setSize( window_width, window_height );

		return window;
	}


	public static Window position_window( Window window, double fw, double fh )
	{
		return position_window( window, fw, fh, 0 );
	}


	/**
		positions the window a certain fraction of the way across the specified screen
		<pre>
		eg:
		0   - left/top aligned
		0.5 - centered
		1.0 - right/bottom aligned
		</pre>
	*/
	public static Window position_window( Window window, double fx, double fy, int screen_index )
	{
		int x = (int) ( ( screen_width( screen_index ) - window.getWidth() ) * fx );
		int y = (int) ( ( screen_height( screen_index ) - window.getHeight() ) * fy );

		for( int i=0; i<screen_index; i++ )
		{
			x += screen_width( i );
		}

		window.setLocation( x, y );

		return window;
	}



	public static boolean left_mouse( MouseEvent e )
	{
		return left_mouse( e.getButton() );
	}

	public static boolean left_mouse( int button )
	{
		return button == left_mouse;
	}


	public static boolean right_mouse( MouseEvent e )
	{
		return right_mouse( e.getButton() );
	}

	public static boolean right_mouse( int button )
	{
		return button == right_mouse;
	}


	public static boolean middle_mouse( MouseEvent e )
	{
		return middle_mouse( e.getButton() );
	}

	public static boolean middle_mouse( int button )
	{
		return button == middle_mouse;
	}


	public static boolean mouse_button( int button, MouseEvent e )
	{
		if( button == no_mouse ) return false;

		if( button == any_mouse ) return true;

		return e.getButton() == button;
	}




	public static void layout_vertically( Container container, Component... components )
	{
		container.setLayout( new GridBagLayout() );

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.insets = new Insets( 5, 5, 5, 5 );

		gbc.weightx = 1;

		for( Component component : components )
		{
			gbc.gridy ++;

			container.add( component, gbc );
		}

		gbc.gridy ++;

		gbc.weighty = 1;

		gbc.fill = GridBagConstraints.BOTH;

		container.add( new JPanel(), gbc );
	}






	//	***************************************************************	Colour Functions








	/**
	*	create an opaque 32 bit ARGB color. range 0-255
	*/
	public static int rgb( int r, int g, int b )
	{
		return argb( 255, r, g, b );
	}


	/**
	*	create an 32 bit ARGB color. range 0-255
	*/
	public static int argb( int a, int r, int g, int b )
	{
		int argb = 0;

		argb |= ( a & 0xff ) << 24;
		argb |= ( r & 0xff ) << 16;
		argb |= ( g & 0xff ) << 8;
		argb |= ( b & 0xff ) << 0;

		return argb;
	}


	/**
	*	create an opaque 32 bit ARGB color. range 0-1
	*/
	public static int rgb( double r, double g, double b )
	{
		return argb( 1.0, r, g, b );
	}


	/**
	*	create an 32 bit ARGB color. range 0-1
	*/
	public static int argb( double a, double r, double g, double b )
	{
		return argb( rint( a * 255 ), rint( r * 255 ), rint( g * 255 ), rint( b * 255 ) );
	}


	/**
	*	get the Alpha component out of an 32 bit ARGB color. range 0-255
	*/
	public static int argbA( int argb )
	{
		return ( argb >> 24 ) & 0xff;
	}


	/**
	*	get the Red component out of an 32 bit ARGB color. range 0-255
	*/
	public static int argbR( int argb )
	{
		return ( argb >> 16 ) & 0xff;
	}


	/**
	*	get the Green component out of an 32 bit ARGB color using. range 0-255
	*/
	public static int argbG( int argb )
	{
		return ( argb >> 8 ) & 0xff;
	}


	/**
	*	get the Blue component out of an 32 bit ARGB color. range 0-255
	*/
	public static int argbB( int argb )
	{
		return ( argb >> 0 ) & 0xff;
	}


	public static int darken( int argb )
	{
		return lighten( argb, -1 );
	}


	public static int darken( int argb, int amount )
	{
		return lighten( argb, -amount );
	}


	public static int lighten( int argb )
	{
		return lighten( argb, 1 );
	}


	public static int lighten( int argb, int amount )
	{
		int a = argbA( argb );
		int r = argbR( argb );
		int g = argbG( argb );
		int b = argbB( argb );

		r = clamp( r + amount, 0, 255 );
		g = clamp( g + amount, 0, 255 );
		b = clamp( b + amount, 0, 255 );

		return argb( a, r, g, b );
	}


	public static double argbAd( int argb )
	{
		return ( ( argb >> 24 ) & 0xff ) / 255.0;
	}


	public static double argbRd( int argb )
	{
		return ( ( argb >> 16 ) & 0xff ) / 255.0;
	}


	public static double argbGd( int argb )
	{
		return ( ( argb >> 8 ) & 0xff ) / 255.0;
	}


	public static double argbBd( int argb )
	{
		return ( ( argb >> 0 ) & 0xff ) / 255.0;
	}


	/**
	 * mix argb1 * ( 1-v ) with argb2 * v
	 * @param v in the range 0..1
	 * */
	public static int mix( int argb1, int argb2, double v )
	{
		double a1 = argbAd( argb1 );
		double r1 = argbRd( argb1 );
		double g1 = argbGd( argb1 );
		double b1 = argbBd( argb1 );

		double a2 = argbAd( argb2 );
		double r2 = argbRd( argb2 );
		double g2 = argbGd( argb2 );
		double b2 = argbBd( argb2 );

		double a = interpolate( a1, a2, v );
		double r = interpolate( r1, r2, v );
		double g = interpolate( g1, g2, v );
		double b = interpolate( b1, b2, v );

		return argb( a, r, g, b );
	}


	/**
	 * Parse the given string as a colour
	 * must start with the # character
	 * must be in the format
	 * aarrggbb
	 * or
	 * rrggbb
	 * where each colour component is in the range 0-255
	 * if the format is rrggbb the aa component is set to 255 (fully opaque)
	 * */
	public static int parse_colour( String hex )
	{
		if( ! hex.startsWith( "#" ) ) throw new IllegalArgumentException( "colour must be in the format #rrggbb or #aarrggbb" );

		hex = hex.substring( 1 );

		if( hex.length() == 6 )
		{
			int colour = new BigInteger( hex, 16 ).intValue();

			colour |= 0xff << 24;

			return colour;
		}
		else if( hex.length() == 8 )
		{
			return new BigInteger( hex, 16 ).intValue();
		}
		else
		{
			throw new IllegalArgumentException( "colour must be in the format #rrggbb or #aarrggbb" );
		}
	}



	//************************************************************  Image Functions



	public static RenderingHints image_hints;

	public static final RenderingHints quality_image_hints;

	public static final RenderingHints default_image_hints;

	public static enum ImageTypes
	{
		argb, rgb
	}

	static
	{
		default_image_hints = new RenderingHints( null );

		quality_image_hints = new RenderingHints( null );

		quality_image_hints.put( RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
		quality_image_hints.put( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		quality_image_hints.put( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY );
		quality_image_hints.put( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE );
		quality_image_hints.put( RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON );
		quality_image_hints.put( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC );
		quality_image_hints.put( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE );
		quality_image_hints.put( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		quality_image_hints.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );

		image_hints = quality_image_hints;
	}


	public static BufferedImage copy_image( BufferedImage img )
	{
		return img.getSubimage( 0, 0, img.getWidth(), img.getHeight() );
	}
	

	public static BufferedImage load_image( String filename ) throws IOException
	{
		return load_image( new File( filename ) );

		//return toBufferedImage( Toolkit.getDefaultToolkit().createImage( filename ) );
	}

	public static BufferedImage load_image( File file ) throws IOException
	{
		return ImageIO.read( file );

		//return load_image( file.getPath() );
	}

	// This method returns a buffered image with the contents of an image
	public static BufferedImage toBufferedImage(Image image)
	{
		if (image instanceof BufferedImage) {
			return (BufferedImage)image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see Determining If an Image Has Transparent Pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(
				image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	// This method returns true if the specified image has transparent pixels
	public static boolean hasAlpha(Image image) {
	// If buffered image, the color model is readily available
	if (image instanceof BufferedImage) {
		BufferedImage bimage = (BufferedImage)image;
		return bimage.getColorModel().hasAlpha();
	}

	// Use a pixel grabber to retrieve the image's color model;
	// grabbing a single pixel is usually sufficient
	PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
	try {
		pg.grabPixels();
	} catch (InterruptedException e) {
	}

	// Get the image's color model
	ColorModel cm = pg.getColorModel();
	return cm.hasAlpha();
	}




	public static BufferedImage create_image( int width, int height, ImageTypes type )
	{
		if( type == ImageTypes.rgb )
		{
			return create_rgb_image( width, height );
		}

		if( type == ImageTypes.argb )
		{
			return create_argb_image( width, height );
		}

		throw new IllegalArgumentException( "Can't create image of type: " + type );
	}


	public static BufferedImage create_argb_image( int width, int height )
	{
		return new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
	}


	public static BufferedImage create_rgb_image( int width, int height )
	{
		return new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
	}


	public static void save_image( BufferedImage image, String filename ) throws IOException
	{
		save_image( image, new File( filename ) );
	}


	public static void save_image( BufferedImage image, File file ) throws IOException
	{
		String format = file.getName().substring( file.getName().lastIndexOf( "." ) + 1 );

		if( "jpg".equalsIgnoreCase( format ) )
		{
			BufferedImage image2 = create_rgb_image( image.getWidth(), image.getHeight() );

			image2.getGraphics().drawImage( image, 0, 0, null );

			image = image2;
		}

		ImageIO.write( image, format, file );
	}

	public static void save_jpg_image( BufferedImage image, File file )  throws IOException
	{
		Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");

		ImageWriter writer = (ImageWriter)iter.next();
		// instantiate an ImageWriteParam object with default compression options
		ImageWriteParam iwp = writer.getDefaultWriteParam();

		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(1);   // an integer between 0 and 1
		// 1 specifies minimum compression and maximum quality

		FileImageOutputStream output = new FileImageOutputStream(file);
		writer.setOutput(output);
		IIOImage iio_image = new IIOImage(image, null, null);
		writer.write(null, iio_image, iwp);
		writer.dispose();
	}

// 	public static ImageTypes image_type( BufferedImage image )
// 	{
// 		Object o = image.getProperty( "imageType" );
//
// 		info( o );
//
// 		return null;
// 	}


	public static BufferedImage resize_image( BufferedImage image, int new_width, int new_height )
	{
		int old_width = image.getWidth();

		int old_height = image.getHeight();

		if( old_width == new_width && old_height == new_height ) return image;

		double scalex = 1.0 * new_width / old_width;

		double scaley = 1.0 * new_height / old_height;

		AffineTransform transform = AffineTransform.getScaleInstance( scalex, scaley );

		AffineTransformOp op = new AffineTransformOp( transform, image_hints );

		return op.filter( image, null );
	}


	public static BufferedImage filter_image( BufferedImage image, JSIImageFilter filter )
	{
		return filter.apply( image );
	}


	public static BufferedImage mask_image( BufferedImage image, BufferedImage mask )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();

		int mw = mask.getWidth();
		int mh = mask.getHeight();

		if( iw != mw || ih != mh )
		{
//			println( "mask_image error: images are not the same size" );

			return null;
		}

		BufferedImage new_image = create_argb_image( iw, ih );

		for( int x=0; x<iw; x++ )
		{
			for( int y=0; y<ih; y++ )
			{
				int pix_col = image.getRGB( x, y ); // argb

				int pix_mask = mask.getRGB( x, y ); // argb

				int mr = ( pix_mask >> 16 ) & 0xff;
				int mg = ( pix_mask >>  8 ) & 0xff;
				int mb = ( pix_mask >>  0 ) & 0xff;

				int ma = ( mr + mg + mb ) / 3;

				int ir = ( pix_col >> 16 ) & 0xff;
				int ig = ( pix_col >>  8 ) & 0xff;
				int ib = ( pix_col >>  0 ) & 0xff;

				int argb = argb( ma, ir, ig, ib );

				new_image.setRGB( x, y, argb );
			}
		}

		return new_image;
	}


	public static BufferedImage mask_image( BufferedImage image, int mask )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();

		BufferedImage new_image = create_argb_image( iw, ih );

		for( int x=0; x<iw; x++ )
		{
			for( int y=0; y<ih; y++ )
			{
				int pix_col = image.getRGB( x, y ); // argb

				int argb = pix_col & mask;

				new_image.setRGB( x, y, argb );
			}
		}

		return new_image;
	}


	public static BufferedImage set_image_transparency( BufferedImage image, int a )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();

		BufferedImage new_image = create_argb_image( iw, ih );

		for( int x=0; x<iw; x++ )
		{
			for( int y=0; y<ih; y++ )
			{
				int pix_col = image.getRGB( x, y ); // argb

				int ir = ( pix_col >> 16 ) & 0xff;
				int ig = ( pix_col >>  8 ) & 0xff;
				int ib = ( pix_col >>  0 ) & 0xff;

				int argb = argb( a, ir, ig, ib );

				new_image.setRGB( x, y, argb );
			}
		}

		return new_image;
	}


	public static int get_pixel( BufferedImage image, int x, int y )
	{
		return image.getRGB( x, y ); // argb
	}


	public static void set_pixel( BufferedImage image, int x, int y, int argb )
	{
		image.setRGB( x, y, argb ); // argb
	}


	public static double rgb_dist( int rgb1, int rgb2 )
	{
		int r1 = ( rgb1 >> 16 ) & 0xff;
		int g1 = ( rgb1 >>  8 ) & 0xff;
		int b1 = ( rgb1 >>  0 ) & 0xff;

		int r2 = ( rgb2 >> 16 ) & 0xff;
		int g2 = ( rgb2 >>  8 ) & 0xff;
		int b2 = ( rgb2 >>  0 ) & 0xff;

		return dist( r1, g1, b1, r2, g2, b2 );
	}


	public static BufferedImage get_image_row( BufferedImage image, int row )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();

		BufferedImage new_image = create_argb_image( iw, 1 );

		int y = row;

		for( int x=0; x<iw; x++ )
		{
			int argb = image.getRGB( x, y ); // argb

			new_image.setRGB( x, 0, argb );
		}

		return new_image;
	}


	public static BufferedImage get_image_block( BufferedImage image, int x0, int y0, int x1, int y1 )
	{
		BufferedImage new_image = create_argb_image( x1 - x0 + 1, y1 - y0 + 1 );

		for( int x=x0; x<=x1; x++ )
		{
			for( int y=y0; y<=y1; y++ )
			{
				int argb = image.getRGB( x, y ); // argb

				new_image.setRGB( x - x0, y - y0, argb );
			}
		}

		return new_image;
	}


	public static void set_image_row( BufferedImage image, BufferedImage row_image, int row )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();

		int riw = row_image.getWidth();
		int rih = row_image.getHeight();

		if( rih != 1 )
		{
			throw new IllegalArgumentException( "row_image height != 1" );
		}

		if( riw != iw )
		{
			throw new IllegalArgumentException( "row_image width != image width" );
		}

		int y = row;

		for( int x=0; x<iw; x++ )
		{
			int argb = row_image.getRGB( x, 0 ); // argb

			image.setRGB( x, y, argb );
		}
	}


	public static BufferedImage rotate_right( BufferedImage image )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();
		
		int niw = ih;
		int nih = iw;
		
		ImageTypes image_type = ImageTypes.rgb;
		
		if( hasAlpha( image ) ) image_type = ImageTypes.argb;
		
		BufferedImage new_image = create_image( niw, nih, image_type );
		
		for( int ix=0; ix<iw; ix++ )
		{
			for( int iy=0; iy<ih; iy++ )
			{
				int nix = niw - iy - 1;
				int niy = ix;
				
				set_pixel( new_image, nix, niy, get_pixel( image, ix, iy ) );
			}
		}
		
		return new_image;
	}
	

	public static BufferedImage rotate_left( BufferedImage image )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();
		
		int niw = ih;
		int nih = iw;
		
		ImageTypes image_type = ImageTypes.rgb;
		
		if( hasAlpha( image ) ) image_type = ImageTypes.argb;
		
		BufferedImage new_image = create_image( niw, nih, image_type );
		
		for( int ix=0; ix<iw; ix++ )
		{
			for( int iy=0; iy<ih; iy++ )
			{
				int nix = iy;
				int niy = nih - ix - 1;
				
				set_pixel( new_image, nix, niy, get_pixel( image, ix, iy ) );
			}
		}
		
		return new_image;
	}
	
	public static ImageViewer view_image( BufferedImage image )
	{
		return new ImageViewer( image );
	}


	public static void close_viewer( ImageViewer image_viewer )
	{
		image_viewer.frame.setVisible( false );
	}


	public static BufferedImage darken( BufferedImage image )
	{
		return lighten( image, -1 );
	}


	public static BufferedImage darken( BufferedImage image, int amount )
	{
		return lighten( image, -amount );
	}


	public static BufferedImage lighten( BufferedImage image )
	{
		return lighten( image, 1 );
	}


	public static BufferedImage lighten( BufferedImage image, int amount )
	{
		int iw = image.getWidth();
		int ih = image.getHeight();

		BufferedImage new_image = create_argb_image( iw, ih );

		for( int x=0; x<iw; x++ )
		{
			for( int y=0; y<ih; y++ )
			{
				int pix_col = image.getRGB( x, y ); // argb

				int argb = lighten( pix_col, amount );

				new_image.setRGB( x, y, argb );
			}
		}

		return new_image;
	}
	
	
	public static Action create_action( String name, final Object target, String method_name ) throws NoSuchMethodException
	{
		final Method method = target.getClass().getMethod( method_name );
		
		return new AbstractAction( name )
		{
			public void actionPerformed( ActionEvent e )
			{
				try
				{
					method.invoke( target );
				}
				catch( Exception ex )
				{
					throw new RuntimeException( ex );
				}
			}
		};
	}

}

package jsi3.lib.console;

import java.util.*;

public class ColourDecorator implements StreamDecorator
{
	final String colour_code;

	final String normal_colour_code = "\033[0m";

	public static final Map<String,String> colours = new HashMap<String,String>();

	

	static
	{
		colours.put( "Black", "\033[30m" );
		colours.put( "Blue", "\033[34m" );
		colours.put( "Green", "\033[32m" );
		colours.put( "Cyan", "\033[36m" );
		colours.put( "Red", "\033[31m" );
		colours.put( "Purple", "\033[35m" );
		colours.put( "Yellow", "\033[33m" );
		colours.put( "Grey", "\033[37m" );
		
		colours.put( "Bold Black", "\033[1;30m" );
		colours.put( "Bold Blue", "\033[1;34m" );
		colours.put( "Bold Green", "\033[1;32m" );
		colours.put( "Bold Cyan", "\033[1;36m" );
		colours.put( "Bold Red", "\033[1;31m" );
		colours.put( "Bold Purple", "\033[1;35m" );
		colours.put( "Bold Yellow", "\033[1;33m" );
		colours.put( "Bold Grey", "\033[1;31m" );
	}
	
	public ColourDecorator( String colour )
	{
		String colour_code = colours.get( colour );

		if( colour_code == null ) throw new IllegalArgumentException( String.format( "colour %s unknown", colour ) );

		this.colour_code = colour_code;
	}
	
	public String pre()
	{
		return colour_code;
	}
	
	public String post()
	{
		return normal_colour_code;
	}
}

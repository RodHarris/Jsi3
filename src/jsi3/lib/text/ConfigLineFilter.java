package jsi3.lib.text;

public class ConfigLineFilter implements LineFilter
{
	public boolean accept( String line )
	{
		String tline = line.trim();
		
		if( tline.length() == 0 ) return false;
		
		if( tline.startsWith( "#" ) ) return false;
		
		return true;
	}
}

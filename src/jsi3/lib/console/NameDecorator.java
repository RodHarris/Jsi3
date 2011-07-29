package jsi3.lib.console;

public class NameDecorator implements StreamDecorator
{
	final String name;
	
	public NameDecorator( String name )
	{
		this.name = name;
	}
	
	public String pre()
	{
		return name;
	}
	
	public String post()
	{
		return "";
	}
}

package jsi3.lib.mainopt;

public class KnownFlag
{
	static final String SPACES = "                         ";

	String[] names;

	String description;

	boolean required;

	int n;

	public KnownFlag( int n, String[] names, String description )
	{
		this.names = names;

		this.description = description;

		this.n = n;
	}
	
	public void required()
	{
		required = true;
	}

	public void optional()
	{
		required = false;
	}

	public String toString()
	{
		String s = "";

		for( String name : names )
		{
			s += name + " | ";
		}

		s = s.substring( 0, s.length() - 3 );

		if( s.length() < 25 )
		{
			s += SPACES.substring( 0, 20 - s.length() );
		}
		else
		{
			s += "\n" + SPACES;
		}

		s += " :";

/*
		if (required )
		{
			s += " (required)";
		}
		else
		{
			s += " (optional)";
		}
*/

		if( n > 0 )
		{
			s += " (" + n + " of)";
		}

		s += " " + description;

		return s;
	}

	public boolean has_name( String n )
	{
		for( String name : names )
		{
			if( name.equals( n ) ) return true;
		}

		return false;
	}


	public String names()
	{
		String s = "(";

		for( String name : names )
		{
			s += name + " | ";
		}

		s = s.substring( 0, s.length() - 3 );

		s += ")";

		return s;
	}
}

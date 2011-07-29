package jsi3.lib.mainopt;

public class KnownParam extends KnownFlag
{
	public KnownParam( int n, String[] names, String description )
	{
		super( n, names, description );
	}

	public String toString()
	{
		String s = "";

		for( String name : names )
		{
			s += name + " | ";
		}

		s = s.substring( 0, s.length() - 3 );

		if( n > 0 )
		{
			for( int i=0; i<n; i++ )
			{
				s += " <...>";
			}
		}
		else
		{
			s += " <...>...";
		}

		if( s.length() < 25 )
		{
			s += SPACES.substring( 0, 20 - s.length() );
		}
		else
		{
			s += "\n" + SPACES;
		}

/*
		if (required )
		{
			s += " : (required) ";
		}
		else
		{
			s += " : (optional) ";
		}
*/

		s += description;

		return s;
	}
}

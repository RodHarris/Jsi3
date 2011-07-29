package jsi3.util.netlayer;

import java.beans.*;
import java.io.*;


public class SerialiserObjectCodec implements ObjectCodec
{
	public String encode( Object o )
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream( baos );

			oos.writeObject( o );

			oos.close();

			String str = baos.toString();

			return str;
		}
		catch( IOException ex )
		{
			ex.printStackTrace( System.err );
		}

		return null;
	}


	public Object decode( String s )
	{
		try
		{
			ObjectInputStream ios = new ObjectInputStream( new ByteArrayInputStream( s.getBytes() ) );
		
			return ios.readObject();
		}
		catch( IOException ex )
		{
			ex.printStackTrace( System.err );
		}
		catch( ClassNotFoundException ex )
		{
			ex.printStackTrace( System.err );
		}

		return null;
	}
}

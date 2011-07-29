package jsi3.util.netlayer;

import java.beans.*;
import java.io.*;


public class XmlObjectCodec implements ObjectCodec
{
	public String encode( Object o )
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEncoder xmlenc = new XMLEncoder( baos );

		xmlenc.writeObject( o );

		xmlenc.close();

		String xml = baos.toString();

		return xml;
	}
	
	
	public Object decode( String xml )
	{
		XMLDecoder xmldec = new XMLDecoder( new ByteArrayInputStream( xml.getBytes() ) );

		return xmldec.readObject();
	}
}

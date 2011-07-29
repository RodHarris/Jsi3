package jsi3.util.netlayer;


import java.beans.*;
import java.io.*;


public interface ObjectCodec
{
	public String encode( Object o );

	public Object decode( String s );
}

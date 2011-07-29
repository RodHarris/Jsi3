package jsi3.util.netlayer;


class RAMResidentClassLoader extends ClassLoader
{
	public Class findClass( String name ) throws ClassNotFoundException
	{
		byte[] b = NetLayer.get_byte_code( name );
		
		return defineClass( name, b, 0, b.length );
	}
}

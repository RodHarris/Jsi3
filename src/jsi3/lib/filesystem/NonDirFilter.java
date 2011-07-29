package jsi3.lib.filesystem;

import java.io.*;

public class NonDirFilter implements FileFilter
{
	 public boolean accept( File pathname )
	 {
	 	return ! pathname.isDirectory();
	 }
}

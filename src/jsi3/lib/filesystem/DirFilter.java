package jsi3.lib.filesystem;

import java.io.*;

public class DirFilter implements FileFilter
{
	 public boolean accept( File pathname )
	 {
	 	return pathname.isDirectory();
	 }
}

package com.suiheikoubou.common;

import java.io.*;

public class XFileFilter
{
	private XFileFilter()
	{
	}
	public static class Folder implements FileFilter
	{
		public Folder()
		{
		}
		public boolean accept( File pathname )
		{
			return	pathname.isDirectory();
		}
	}
	public static class Text implements FileFilter
	{
		public Text()
		{
		}
		public boolean accept( File pathname )
		{
			return	( pathname.isFile() ) && ( pathname.getName().toUpperCase().endsWith( ".TXT" ) ) ;
		}
	}
	public static class Json implements FileFilter
	{
		public Json()
		{
		}
		public boolean accept( File pathname )
		{
			return	( pathname.isFile() ) && ( pathname.getName().toUpperCase().endsWith( ".JSON" ) ) ;
		}
	}
}

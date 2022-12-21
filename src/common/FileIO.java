package com.suiheikoubou.common;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public final class FileIO
{
	protected FileIO()
	{
	}
	//----------------------------------------------------------------------------------------------
	public static List<String> loadLines( File file ) throws IOException
	{
		return	loadLines( file , Charset.defaultCharset() );
	}
	public static List<String> loadLines( File file , Charset cs ) throws IOException
	{
		List<String>					lines				= new ArrayList<String>();
		BufferedReader					reader				= new BufferedReader( new InputStreamReader( new FileInputStream( file ) , cs ) );
		String							line				= "";
		while( ( line = reader.readLine() ) != null )
		{
			lines.add( line );
		}
		reader.close();
		return	lines;
	}
	//----------------------------------------------------------------------------------------------
	public static void storeLines( File file , Collection<String> lines ) throws IOException
	{
		storeLines( file , false , lines , Charset.defaultCharset() );
	}
	public static void storeLines( File file ,  Collection<String> lines , Charset cs ) throws IOException
	{
		storeLines( file , false , lines , cs );
	}
	public static void storeLines( File file , boolean append , Collection<String> lines ) throws IOException
	{
		storeLines( file , append , lines , Charset.defaultCharset() );
	}
	public static void storeLines( File file , boolean append , Collection<String> lines , Charset cs ) throws IOException
	{
		PrintWriter						writer				= new PrintWriter( new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file , append ) , cs ) ) );
		for( String line : lines )
		{
			writer.println( line );
		}
		writer.close();
	}
	//----------------------------------------------------------------------------------------------
	public static byte[] loadBytes( InputStream inStream ) throws IOException
	{
		int								maxLen				= 65536;
		int								len					= 0;
		byte[]							buffer				= new byte[ maxLen ];
		ByteArrayOutputStream			outStream			= new ByteArrayOutputStream();
		while( ( len = inStream.read( buffer , 0 , maxLen ) ) >= 0 )
		{
			outStream.write( buffer , 0 , len );
		}
		byte[]							res					= outStream.toByteArray();
		outStream.close();
		return	res;
	}
	public static byte[] loadBytes( File file ) throws IOException
	{
		BufferedInputStream				inStream			= new BufferedInputStream( new FileInputStream( file ) );
		byte[]							res					= loadBytes( inStream );
		inStream.close();
		return	res;
	}
	//----------------------------------------------------------------------------------------------
	public static void storeBytes( File file , byte[] bytes ) throws IOException
	{
		int								maxLen				= 65536;
		int								offset				= 0;
		BufferedOutputStream			outStream			= new BufferedOutputStream( new FileOutputStream( file ) );
		while( offset < bytes.length )
		{
			int							len					= maxLen;
			if( len > bytes.length - offset )
			{
				len											= bytes.length - offset;
			}
			outStream.write( bytes , offset , len );
			offset											+=len;
		}
		outStream.close();
	}
	//----------------------------------------------------------------------------------------------
	public static String getCleanString( String inStr )
	{
		return	inStr.replace( "\t" , " " ).replace( "\n" , " " ).replace( "\r" , " " );
	}
}

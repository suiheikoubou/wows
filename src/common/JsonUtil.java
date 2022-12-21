package com.suiheikoubou.common;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.math.*;
import net.arnx.jsonic.*;

public final class JsonUtil
{
	protected JsonUtil()
	{
	}
	public static byte[] loadUrl( URL url ) throws IOException
	{
		URLConnection					connection			= url.openConnection();
		connection.setConnectTimeout( 10000 );
		connection.setReadTimeout( 10000 );
		connection.setDoOutput( false );
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
		connection.connect();
		BufferedInputStream				inStream			= new BufferedInputStream( connection.getInputStream() );
		byte[]							bytes				= FileIO.loadBytes( inStream );
		return	bytes;
	}
	public static Object toJsonData( byte[] bytes ) throws IOException
	{
		String							jsonStr				= new String( bytes , "UTF8" );
		Object							jsonData			= JSON.decode( jsonStr );
		return	jsonData;
	}
	protected static DateFormat			_format;
	static
	{
		_format												= new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
	}
	public static Map getMapDataMap( Map mapData , String key )
	{
		Map								value				= null;
		try
		{
			Object						obj					= mapData.get( key );
			value											= (Map)obj;
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
	public static List getMapDataList( Map mapData , String key )
	{
		List							value				= null;
		try
		{
			Object						obj					= mapData.get( key );
			value											= (List)obj;
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
	public static BigDecimal getMapDataDecimal( Map mapData , String key )
	{
		return	getMapDataDecimal( mapData , key , BigDecimal.ZERO );
	}
	public static BigDecimal getMapDataDecimal( Map mapData , String key , BigDecimal defaultValue )
	{
		BigDecimal						value				= defaultValue;
		try
		{
			Object						obj					= mapData.get( key );
			BigDecimal					dec					= (BigDecimal)obj;
			value											= dec.add( BigDecimal.ZERO );
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
	public static int getMapDataInt( Map mapData , String key )
	{
		return	getMapDataInt( mapData , key , -1 );
	}
	public static int getMapDataInt( Map mapData , String key , int defaultValue )
	{
		int							value				= defaultValue;
		try
		{
			Object						obj					= mapData.get( key );
			BigDecimal					dec					= (BigDecimal)obj;
			value											= dec.intValue();
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
	public static long getMapDataLong( Map mapData , String key )
	{
		return	getMapDataLong( mapData , key , -1 );
	}
	public static long getMapDataLong( Map mapData , String key , long defaultValue )
	{
		long							value				= defaultValue;
		try
		{
			Object						obj					= mapData.get( key );
			BigDecimal					dec					= (BigDecimal)obj;
			value											= dec.longValue();
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
	public static long getMapDataLongS( Map mapData , String key )
	{
		return	getMapDataLongS( mapData , key , -1 );
	}
	public static long getMapDataLongS( Map mapData , String key , long defaultValue )
	{
		long							value				= defaultValue;
		try
		{
			Object						obj					= mapData.get( key );
			String						dec					= (String)obj;
			value											= Long.parseLong( dec );
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
	public static String getMapDataString( Map mapData , String key )
	{
		String							value				= "";
		try
		{
			Object						obj					= mapData.get( key );
			if( obj != null )
			{
				value										= (String)obj;
			}
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
	public static boolean getMapDataBoolean( Map mapData , String key )
	{
		boolean							value				= false;
		try
		{
			Object						obj					= mapData.get( key );
			Boolean						bool				= (Boolean)obj;
			value											= bool.booleanValue();
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
	public static String getMapDataTimestamp( Map mapData , String key )
	{
		return	getMapDataTimestamp( mapData , key , 0 );
	}
	public static String getMapDataTimestamp( Map mapData , String key , long hourShift )
	{
		String							value				= "";
		try
		{
			Object						obj					= mapData.get( key );
			BigDecimal					dec					= (BigDecimal)obj;
			Date						date				= new Date( dec.longValue() *1000L + hourShift *60L*60L*1000L );
			value											= _format.format( date );
		}
		catch( Exception ex )
		{
		}
		return	value;
	}
}

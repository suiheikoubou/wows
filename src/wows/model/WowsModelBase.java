package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import java.math.*;
import java.nio.charset.*;
import com.suiheikoubou.common.*;

public final class WowsModelBase
{
	public static final int				KT_NONE				=    0;
	public static final int				KT_AID100			=    2;
	public static final int				KT_AID1000			=    3;
	public static final int				KT_SHIPID			= 1001;
	public static final int				CMDS_LEN			=   40;
	public static final String			DELIMITER			= "\t";
	public static final String			LAST_LETTER			= "0" ;

	protected WowsModelBase()
	{
	}

	//----------------------------------------------------------------------------------------------
	public static MathContext			mcDown;
	public static MathContext			mcHU;
	public static Charset				cs;
	static
	{
		mcDown												= new MathContext( 32 , RoundingMode.DOWN );
		mcHU												= new MathContext( 32 , RoundingMode.HALF_UP );
		cs													= Charset.forName( "UTF8" );
	}
	//----------------------------------------------------------------------------------------------
	public static void appendString( StringBuffer buffer , String value , boolean addDelimiter )
	{
		if( addDelimiter )
		{
			buffer.append( DELIMITER );
		}
		buffer.append( value );
	}
	public static void appendString( StringBuffer buffer , int value , boolean addDelimiter )
	{
		appendString( buffer , String.valueOf(value) , addDelimiter );
	}
	public static void appendString( StringBuffer buffer , long value , boolean addDelimiter )
	{
		appendString( buffer , String.valueOf(value) , addDelimiter );
	}
	public static void appendString( StringBuffer buffer , boolean value , boolean addDelimiter )
	{
		appendString( buffer , String.valueOf(value) , addDelimiter );
	}
	public static void appendString( StringBuffer buffer , BigDecimal value , boolean addDelimiter )
	{
		appendString( buffer , value.toPlainString() , addDelimiter );
	}
	//----------------------------------------------------------------------------------------------
	public static List<Long> loadLongs( File file ) throws IOException
	{
		List<Long>						longs				= new ArrayList<Long>();
		List<String>					lines				= FileIO.loadLines( file , cs );
		for( String line: lines )
		{
			longs.add( Long.valueOf( line ) );
		}
		lines.clear();
		return	longs;
	}
	//----------------------------------------------------------------------------------------------
	public static void storeLongs( File file , Collection<Long> longs ) throws IOException
	{
		List<String>					lines				= new ArrayList<String>();
		for( Long val : longs )
		{
			lines.add( val.toString() );
		}
		FileIO.storeLines( file , false , lines , cs );
		lines.clear();
	}
}

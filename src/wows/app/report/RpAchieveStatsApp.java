package com.suiheikoubou.wows.app.report;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RpAchieveStatsApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 6 )
			{
				File					inBaseFolder		= new File( args[0] );
				File					inSvrFolder			= new File( inBaseFolder  , args[2] );
				File					thisFolder			= new File( inSvrFolder   , args[3] );
				File					inThisFolder		= new File( thisFolder    , args[4] );
				File					outBaseFolder		= new File( args[1] );
				File					outSvrFolder		= new File( outBaseFolder , args[2] );
				File					outThisFolder		= new File( outSvrFolder  , args[3] );
				File					outFile				= new File( outThisFolder , args[5] );
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpAchieveStatsApp		instance			= new RpAchieveStatsApp( logFile );
				instance.execute( inThisFolder , outFile , outFile );
			}
			else
			{
				System.out.println( "usage : java RpAchieveStatsApp [in base folder] [out base folder] [server] [date this] [in folder] [out file]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RpAchieveStatsApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inThisFolder , File outFile , File outFile ) throws Exception
	{
		outFile.getParentFile().mkdirs();
		Map<String,Long>				frequencyMap		= new TreeMap<String,Long>();
		File[]							inThisFiles			= inThisFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inThisFile : inThisFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inThisFiles.length ) + ":" + inThisFile.getName() );
			processFrequency( inThisFile , frequencyMap );
			ix++;
		}
		outFrequency( outFile , frequencyMap );
	}
	protected void processFrequency( File inThisFile , Map<String,Long> frequencyMap ) throws IOException
	{
		List<AccountAchieveInfo>		thisModels			= Models.loadModels( inThisFile , new AccountAchieveInfo() , WowsModelBase.cs );

		for( AccountAchieveInfo thisModel : thisModels )
		{
			String						frequencyKey		= getFrequencyKey( thisModel );
			Long						achieveCnt			= frequencyMap.get( frequencyKey );
			if( achieveCnt == null )
			{
				achieveCnt									= Long.valueOf( 1 );
			}
			else
			{
				achieveCnt									= Long.valueOf( achieveCnt.longValue() + 1 );
			}
			frequencyMap.put( frequencyKey , achieveCnt );
		}
		thisModels.clear();
	}
	protected String getFrequencyKey( AccountAchieveInfo info )
	{
		return	info.key.achieveId + "\t" + String.format( "%06d" , info.achieveValue );
	}
	protected String getFrequencyLine( String key , Long value )
	{
		return	key + "\t" + String.format( "%d" , value.longValue() );
	}
	protected void outFrequency( File outFile , Map<String,Long> frequencyMap ) throws IOException
	{
		List<String>					lines				= new ArrayList<String>();
		for( Map.Entry<String,Long> entry : frequencyMap.entrySet() )
		{
			String						line				= getFrequencyLine( entry.getKey() , entry.getValue() );
			lines.add( line );
		}
		FileIO.storeLines( outFile , lines );
		lines.clear();
	}
}

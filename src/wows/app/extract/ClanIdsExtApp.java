package com.suiheikoubou.wows.app.extract;

import java.io.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public final class ClanIdsExtApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 6 )
			{
				File					inBaseFolder		= new File( args[0] );
				File					inSvrFolder			= new File( inBaseFolder , args[2] );
				File					inThisFolder		= new File( inSvrFolder  , args[3] );
				File					inFolder			= new File( inThisFolder , args[4] );
				File					outBaseFolder		= new File( args[1] );
				File					outSvrFolder		= new File( outBaseFolder , args[2] );
				File					outThisFolder		= new File( outSvrFolder  , args[3] );
				File					outFile				= new File( outThisFolder , args[5] );
				File					logFile				= new File( outThisFolder  , "error.log" );
				ClanIdsExtApp			instance			= new ClanIdsExtApp( logFile );
				instance.execute( inFolder , outFile );
			}
			else
			{
				System.out.println( "usage : java ClanIdsExtApp [in base folder] [out base folder] [server] [date this] [in folder] [out file]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public ClanIdsExtApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFile ) throws Exception
	{
		List<String>					lines				= new ArrayList<String>();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Json() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( inFile.getPath() );
			List<String>				ids					= loadIds( inFile );
			lines.addAll( ids );
			ids.clear();
		}
		FileIO.storeLines( outFile , lines , WowsModelBase.cs );
	}
	protected List<String> loadIds( File inFile ) throws Exception
	{
		List<String>					ids					= new ArrayList<String>();

		Map								jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( inFile ) ));
		if( JsonUtil.getMapDataString( jsonData , "status" ).equals( "ok" ) )
		{
			List						dataList			= (List)(jsonData.get( "data" ));
			for( Object dataObj : dataList )
			{
				Map						dataMap				= (Map)(dataObj);
				if( dataMap != null )
				{
					long				clanId				= JsonUtil.getMapDataLong	( dataMap , "clan_id" );
					ids.add( String.valueOf( clanId ) );
				}
			}
		}
		else
		{
			outLog( "status not ok : " + inFile.getName() );
		}
		jsonData.clear();
		return	ids;
	}
}

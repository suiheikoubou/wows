package com.suiheikoubou.wows.app.process;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class SummaryShipBattleApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 8 )
			{
				File					mstFolder			= new File( args[0] );
				File					mstFile				= new File( mstFolder     , args[5] );
				File					inBaseFolder		= new File( args[1] );
				File					inSvrFolder			= new File( inBaseFolder  , args[3] );
				File					inThisFolder		= new File( inSvrFolder   , args[4] );
				File					inFolder			= new File( inThisFolder  , args[6] );
				File					outBaseFolder		= new File( args[2] );
				File					outSvrFolder		= new File( outBaseFolder , args[3] );
				File					outThisFolder		= new File( outSvrFolder  , args[4] );
				File					outFolder			= new File( outThisFolder , args[7] );
				File					logFile				= new File( outFolder     , "error.log" );
				SummaryShipBattleApp	instance			= new SummaryShipBattleApp( logFile );
				instance.execute( mstFile , inFolder , outFolder );
			}
			else
			{
				System.out.println( "usage : java SummaryShipBattleApp [mst folder] [in base folder] [out base folder] [server] [date this] [mst file] [in folder] [out folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public SummaryShipBattleApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File mstFile , File inFolder , File outFolder ) throws Exception
	{
		List<ShipInfo>					ships				= Models.loadModels( mstFile , new ShipInfo() , WowsModelBase.cs );
		Map<Long,ShipInfo>				shipMap				= Models.toMap( ships );

		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			File						outFile				= new File( outFolder , inFile.getName() );
			processSummary( shipMap , inFile , outFile );
			ix++;
		}
		shipMap.clear();
	}
	protected void processSummary( Map<Long,ShipInfo> shipMap , File inFile , File outFile ) throws IOException
	{
		Map<Long,AccountSummaryInfo>	summaryMap			= new TreeMap<Long,AccountSummaryInfo>();

		List<ShipBattleInfo>			shipBattles			= Models.loadModels( inFile , new ShipBattleInfo() , WowsModelBase.cs );
		for( ShipBattleInfo shipBattle : shipBattles )
		{
			AccountSummaryInfo			summary				= summaryMap.get( Long.valueOf( shipBattle.key.accountId ) );
			if( summary == null )
			{
				summary										= new AccountSummaryInfo();
				summary.accountId							= shipBattle.key.accountId;
			}
			ShipInfo					ship				= shipMap.get( Long.valueOf( shipBattle.key.shipId ) );
			summary.add( shipBattle , ship );
			summaryMap.put( Long.valueOf( summary.accountId ) , summary );
		}
		shipBattles.clear();

		Models.storeModels( outFile , false , summaryMap.values() , AccountSummaryInfo.ST_NONE , WowsModelBase.cs );
		summaryMap.clear();
	}
}

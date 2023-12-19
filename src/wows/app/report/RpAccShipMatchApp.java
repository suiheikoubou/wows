package com.suiheikoubou.wows.app.report;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RpAccShipMatchApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 7 )
			{
				File					inBaseFolder		= new File( args[0] );
				File					inSvrFolder			= new File( inBaseFolder  , args[2] );
				File					thisFolder			= new File( inSvrFolder   , args[3] );
				File					acntFolder			= new File( thisFolder    , args[4] );
				File					shipFolder			= new File( thisFolder    , args[5] );
				File					outBaseFolder		= new File( args[1] );
				File					outSvrFolder		= new File( outBaseFolder , args[2] );
				File					outThisFolder		= new File( outSvrFolder  , args[3] );
				File					outFile				= new File( outThisFolder , args[6] );
				long					threshold			= 10;
				long					limit				= 70;
				if( args.length >= 8 )
				{
					threshold								= Long.valueOf( args[7] );
				}
				if( args.length >= 9 )
				{
					limit									= Long.valueOf( args[8] );
				}
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpAccShipMatchApp		instance			= new RpAccShipMatchApp( logFile );
				instance.execute( acntFolder , shipFolder , outFile , threshold , limit );
			}
			else
			{
				System.out.println( "usage : java RpAccShipMatchApp [in base folder] [out base folder] [server] [date this] [acnt folder] [ship folder] [out file] (threshold) (limit)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RpAccShipMatchApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File acntFolder , File shipFolder , File outFile , long threshold , long limit ) throws Exception
	{
		List<String>					lines				= new ArrayList<String>();
		outFile.getParentFile().mkdirs();
		File[]							acntFiles			= acntFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File acntFile : acntFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , acntFiles.length ) + ":" + acntFile.getName() );
			File						shipFile			= new File( shipFolder , acntFile.getName() );
			processBattleMatch( acntFile , shipFile , lines , threshold , limit );
			ix++;
		}
		FileIO.storeLines( outFile , lines , WowsModelBase.cs );
		lines.clear();
	}
	protected void processBattleMatch( File acntFile , File shipFile , List<String> lines , long threshold , long limit ) throws IOException
	{
		List<AccountBattleInfo>			accountBattles		= Models.loadModels( acntFile , new AccountBattleInfo() , WowsModelBase.cs );
		List<ShipBattleInfo>			shipBattles			= Models.loadModels( shipFile , new ShipBattleInfo() , WowsModelBase.cs );
		Map<Long,Long>					totalBattleMap		= getShipTotalBattleMap( shipBattles );
		shipBattles.clear();

		for( AccountBattleInfo accountBattle : accountBattles )
		{
			long						pvpBattle			= accountBattle.pvpBattles.longValue();
			if( pvpBattle >= threshold )
			{
				Long					accountId			= Long.valueOf( accountBattle.accountId );
				Long					totalBattle			= totalBattleMap.get( accountId );
				if( totalBattle == null )
				{
					totalBattle								= Long.valueOf( 0 );
				}
				if( ( totalBattle.longValue() * 100 ) / pvpBattle < limit )
				{
//					String				line				= accountId.toString();
					String				line				= accountId.toString() + "\t" + String.valueOf( pvpBattle ) + "\t" + totalBattle.toString();
					lines.add( line );
				}
			}
		}

		accountBattles.clear();
		totalBattleMap.clear();
	}
	protected Map<Long,Long> getShipTotalBattleMap( List<ShipBattleInfo> shipBattles )
	{
		Map<Long,Long>					totalBattleMap		= new TreeMap<Long,Long>();
		for( ShipBattleInfo shipBattle : shipBattles )
		{
			Long						accountId			= Long.valueOf( shipBattle.key.accountId );
			Long						totalBattle			= totalBattleMap.get( accountId );
			if( totalBattle != null )
			{
				totalBattle									= Long.valueOf( totalBattle.longValue() + shipBattle.valueBattles.longValue() );
			}
			else
			{
				totalBattle									= Long.valueOf( shipBattle.valueBattles.longValue() );
			}
			totalBattleMap.put( accountId , totalBattle );
		}
		return	totalBattleMap;
	}
}

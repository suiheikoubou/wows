package com.suiheikoubou.wows.app.extract;

import java.io.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public final class ShipBattleExtApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 7 )
			{
				File					inBaseFolder		= new File( args[0] );
				File					inSvrFolder			= new File( inBaseFolder , args[2] );
				File					inThisFolder		= new File( inSvrFolder  , args[3] );
				File					inFolder			= new File( inThisFolder , args[4] );
				File					outBaseFolder		= new File( args[1] );
				File					outSvrFolder		= new File( outBaseFolder , args[2] );
				File					outThisFolder		= new File( outSvrFolder  , args[3] );
				File					outFolder			= new File( outThisFolder , args[5] );
				File					hiddenFile			= new File( outThisFolder , args[6] );
				String					category			= "pvp";
				if( args.length >= 8 )
				{
					category								= args[7];
				}
				File					logFile				= new File( outThisFolder  , "error.log" );
				ShipBattleExtApp		instance			= new ShipBattleExtApp( logFile );
				instance.execute( inFolder , outFolder , hiddenFile , category );
			}
			else
			{
				System.out.println( "usage : java ShipBattleExtApp [in base folder] [out base folder] [server] [date this] [in folder] [out folder] [hidden file] (category)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public ShipBattleExtApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder , File hiddenFile , String category ) throws Exception
	{
		List<String>					hiddens				= new ArrayList<String>();
		ModelStorage<ShipBattleInfo>	modelStorage		= new ModelStorage<ShipBattleInfo>();
		outLog( outFolder.getPath() );
		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Json() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			try
			{
				if( ix % 1000 == 0 )
				{
					outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
				}
				List<ShipBattleInfo>	models				= loadModels( inFile , category , hiddens );
				for( ShipBattleInfo model : models )
				{
					modelStorage.add( model , WowsModelBase.KT_AID1000 );
				}
				models.clear();
				ix++;
				if( ix % 100000 == 0 )
				{
					outLog( "flush:" + String.format( "%d" , modelStorage.size() ) );
					modelStorage.store( outFolder , ".txt" , true , ShipBattleInfo.ST_NONE , WowsModelBase.cs );
					modelStorage.clear();
				}
			}
			catch( Exception ex )
			{
				outLog( "file:" + inFile.getPath() );
				throw	ex;
			}
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
		outLog( "flush:" + String.format( "%d" , modelStorage.size() ) );
		modelStorage.store( outFolder , ".txt" , true , ShipBattleInfo.ST_NONE , WowsModelBase.cs );
		modelStorage.clear();
		FileIO.storeLines( hiddenFile , hiddens , WowsModelBase.cs );
		hiddens.clear();
	}
	protected List<ShipBattleInfo> loadModels( File inFile , String category , List<String> hiddens ) throws Exception
	{
		List<ShipBattleInfo>			models				= new ArrayList<ShipBattleInfo>();

		Map								jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( inFile ) ));
		if( JsonUtil.getMapDataString( jsonData , "status" ).equals( "ok" ) )
		{
			Map							metaMap				= (Map)(jsonData.get( "meta" ));
			List						hiddenData			= (List)(metaMap.get( "hidden" ));
			if( hiddenData != null )
			{
				for( Object hiddenObj : hiddenData )
				{
					hiddens.add( hiddenObj.toString() );
				}
			}
			Map							dataMap				= (Map)(jsonData.get( "data" ));
			for( Object accountDataObj : dataMap.values() )
			{
				List		shipList		= (List)accountDataObj;
				if( shipList != null )
				{
					for( Object shipDataObj : shipList )
					{
						Map					shipData		= (Map)shipDataObj;
						Map					pvpData			= (Map)(shipData.get( category ));
						long				battles			= JsonUtil.getMapDataLong( pvpData  , "battles" );
						if( battles > 0 )
						{
							ShipBattleInfo	model			= new ShipBattleInfo();
							Map				mbData			= (Map)(pvpData.get( "main_battery" ));
							model.key.accountId				= JsonUtil.getMapDataLong   ( shipData , "account_id" );
							model.key.shipId				= JsonUtil.getMapDataLong   ( shipData , "ship_id" );
							model.valueBattles				= JsonUtil.getMapDataDecimal( pvpData  , "battles" );
							model.valueWins					= JsonUtil.getMapDataDecimal( pvpData  , "wins" );
							model.valueDraws				= JsonUtil.getMapDataDecimal( pvpData  , "draws" );
							model.valueLosses				= JsonUtil.getMapDataDecimal( pvpData  , "losses" );
							model.valueSurvivedBattles		= JsonUtil.getMapDataDecimal( pvpData  , "survived_battles" );
							model.valueDamageDealt			= JsonUtil.getMapDataDecimal( pvpData  , "damage_dealt" );
							model.valueFrags				= JsonUtil.getMapDataDecimal( pvpData  , "frags" );
							model.valuePlanesKilled			= JsonUtil.getMapDataDecimal( pvpData  , "planes_killed" );
							model.valueCapturePoints		= JsonUtil.getMapDataDecimal( pvpData  , "capture_points" );
							model.valueDroppedCapturePoints	= JsonUtil.getMapDataDecimal( pvpData  , "dropped_capture_points" );
							model.valueXp					= JsonUtil.getMapDataDecimal( pvpData  , "xp" );
							model.valueArtAgro				= JsonUtil.getMapDataDecimal( pvpData  , "art_agro" );
							model.valueTorpedoAgro			= JsonUtil.getMapDataDecimal( pvpData  , "torpedo_agro" );
							model.valueDamageScouting		= JsonUtil.getMapDataDecimal( pvpData  , "damage_scouting" );
							model.valueShipsSpotted			= JsonUtil.getMapDataDecimal( pvpData  , "ships_spotted" );
							model.valueTeamCapturePoints	= JsonUtil.getMapDataDecimal( pvpData  , "team_capture_points" );
							model.valueTeamDroppedPoints	= JsonUtil.getMapDataDecimal( pvpData  , "team_dropped_capture_points" );
							model.valueBattlesSince512		= JsonUtil.getMapDataDecimal( pvpData  , "battles_since_512" );
							model.valueMainShots			= JsonUtil.getMapDataDecimal( mbData   , "shots" );
							model.valueMainHits				= JsonUtil.getMapDataDecimal( mbData   , "hits" );
							model.valueDistance				= JsonUtil.getMapDataDecimal( shipData , "distance" );
							models.add( model );
						}
					}
				}
			}
		}
		else
		{
			outLog( "status not ok : " + inFile.getName() );
		}
		jsonData.clear();
		return	models;
	}
}

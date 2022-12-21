package com.suiheikoubou.wows.app.extract;

import java.io.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public final class AccountBattleExtApp extends AbstractApp
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
				File					outFolder			= new File( outThisFolder , args[5] );
				File					logFile				= new File( outThisFolder  , "error.log" );
				AccountBattleExtApp		instance			= new AccountBattleExtApp( logFile );
				instance.execute( inFolder , outFolder );
			}
			else
			{
				System.out.println( "usage : java AccountBattleExtApp [in base folder] [out base folder] [server] [date this] [in folder] [out folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public AccountBattleExtApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder ) throws Exception
	{
		ModelStorage<AccountBattleInfo>	modelStorage		= new ModelStorage<AccountBattleInfo>();
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
				List<AccountBattleInfo>	models				= loadModels( inFile );
				for( AccountBattleInfo model : models )
				{
					modelStorage.add( model , WowsModelBase.KT_AID1000 );
				}
				models.clear();
				ix++;
				if( ix % 100000 == 0 )
				{
					outLog( "flush:" + String.format( "%d" , modelStorage.size() ) );
					modelStorage.store( outFolder , ".txt" , true , AccountBattleInfo.ST_NONE , WowsModelBase.cs );
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
		modelStorage.store( outFolder , ".txt" , true , AccountBattleInfo.ST_NONE , WowsModelBase.cs );
		modelStorage.clear();
	}
	protected List<AccountBattleInfo> loadModels( File inFile ) throws Exception
	{
		List<AccountBattleInfo>			models				= new ArrayList<AccountBattleInfo>();

		Map								jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( inFile ) ));
		if( JsonUtil.getMapDataString( jsonData , "status" ).equals( "ok" ) )
		{
			Map							dataMap				= (Map)(jsonData.get( "data" ));
			for( Object accountDataObj : dataMap.values() )
			{
				if( accountDataObj != null )
				{
					Map					accountMap			= (Map)accountDataObj;
					AccountBattleInfo	model				= new AccountBattleInfo();
					model.accountId							= JsonUtil.getMapDataLong( accountMap , "account_id" );
					model.accountName						= JsonUtil.getMapDataString( accountMap , "nickname" );
					model.levelingTier						= JsonUtil.getMapDataLong( accountMap , "leveling_tier" , 0 );
					model.createdAt							= JsonUtil.getMapDataTimestamp( accountMap , "created_at" , 9 );			// JST
					model.lastBattleTime					= JsonUtil.getMapDataTimestamp( accountMap , "last_battle_time" , 9 );		// JST
					model.hiddenProfile						= JsonUtil.getMapDataBoolean( accountMap , "hidden_profile" );
					Map					statisticsMap		= (Map)(accountMap.get( "statistics" ));
					if( statisticsMap != null )
					{
						Map				pvpMap				= (Map)(statisticsMap.get( "pvp" ));
						Map				pveMap				= (Map)(statisticsMap.get( "pve" ));
						Map				rankMap				= (Map)(statisticsMap.get( "rank_solo" ));
						Map				clubMap				= (Map)(statisticsMap.get( "club" ));
						Map				pvp1Map				= (Map)(statisticsMap.get( "pvp_solo" ));
						Map				pvp2Map				= (Map)(statisticsMap.get( "pvp_div2" ));
						Map				pvp3Map				= (Map)(statisticsMap.get( "pvp_div3" ));
						Map				opsoloMap			= (Map)(statisticsMap.get( "oper_solo" ));
						Map				opdivMap			= (Map)(statisticsMap.get( "oper_div" ));
						Map				ophardMap			= (Map)(statisticsMap.get( "oper_div_hard" ));

						model.pvpBattles					= JsonUtil.getMapDataDecimal( pvpMap  , "battles" );
						model.pvpWins						= JsonUtil.getMapDataDecimal( pvpMap  , "wins" );
						model.pvpDraws						= JsonUtil.getMapDataDecimal( pvpMap  , "draws" );
						model.pvpLosses						= JsonUtil.getMapDataDecimal( pvpMap  , "losses" );
						model.pveBattles					= JsonUtil.getMapDataDecimal( pveMap  , "battles" );
						model.pveWins						= JsonUtil.getMapDataDecimal( pveMap  , "wins" );
						model.rankBattles					= JsonUtil.getMapDataDecimal( rankMap , "battles" );
						model.rankWins						= JsonUtil.getMapDataDecimal( rankMap , "wins" );
						model.clubBattles					= JsonUtil.getMapDataDecimal( clubMap , "battles" );
						model.clubWins						= JsonUtil.getMapDataDecimal( clubMap , "wins" );
						model.pvp1Battles					= JsonUtil.getMapDataDecimal( pvp1Map , "battles" );
						model.pvp1Wins						= JsonUtil.getMapDataDecimal( pvp1Map , "wins" );
						model.pvp2Battles					= JsonUtil.getMapDataDecimal( pvp2Map , "battles" );
						model.pvp2Wins						= JsonUtil.getMapDataDecimal( pvp2Map , "wins" );
						model.pvp3Battles					= JsonUtil.getMapDataDecimal( pvp3Map , "battles" );
						model.pvp3Wins						= JsonUtil.getMapDataDecimal( pvp3Map , "wins" );
						model.oper1Battles					= JsonUtil.getMapDataDecimal( opsoloMap , "battles" );
						model.oper1Wins						= JsonUtil.getMapDataDecimal( opsoloMap , "wins" );
						model.operdBattles					= JsonUtil.getMapDataDecimal( opdivMap  , "battles" );
						model.operdWins						= JsonUtil.getMapDataDecimal( opdivMap  , "wins" );
						model.operhBattles					= JsonUtil.getMapDataDecimal( ophardMap , "battles" );
						model.operhWins						= JsonUtil.getMapDataDecimal( ophardMap , "wins" );
					}

					models.add( model );
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

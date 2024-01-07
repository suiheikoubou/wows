package com.suiheikoubou.wows.app.report;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RpShipStatsApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 10 )
			{
				File					mstFolder			= new File( args[0] );
				File					mstFile				= new File( mstFolder       , args[6] );
				File					acntBaseFolder		= new File( args[1] );
				File					acntSvrFolder		= new File( acntBaseFolder  , args[4] );
				File					acntThisFolder		= new File( acntSvrFolder   , args[5] );
				File					acntFolder			= new File( acntThisFolder  , args[7] );
				File					shipBaseFolder		= new File( args[2] );
				File					shipSvrFolder		= new File( shipBaseFolder  , args[4] );
				File					shipThisFolder		= new File( shipSvrFolder   , args[5] );
				File					shipFolder			= new File( shipThisFolder  , args[8] );
				File					outBaseFolder		= new File( args[3] );
				File					outSvrFolder		= new File( outBaseFolder   , args[4] );
				File					outThisFolder		= new File( outSvrFolder    , args[5] );
				File					outFile				= new File( outThisFolder   , args[9] );
				boolean					isWrRank			= false;
				if( args.length >= 11 )
				{
					isWrRank								= Boolean.parseBoolean( args[10] );
				}
				boolean					isOutInactive		= false;
				if( args.length >= 12 )
				{
					isOutInactive							= Boolean.parseBoolean( args[11] );
				}
				String					server				= args[4];
				String					processDate			= args[5];
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpShipStatsApp			instance			= new RpShipStatsApp( logFile );
				instance.execute( mstFile , acntFolder , shipFolder , outFile , isWrRank , isOutInactive , server , processDate );
			}
			else
			{
				System.out.println( "usage : java RpShipStatsApp [mst folder] [acnt base folder] [ship base folder] [out base folder] [server] [date this] [mst file] [acnt folder] [ship folder] [out file] (is WR rank) (is out inactive)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RpShipStatsApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File mstFile , File acntFolder , File shipFolder , File outFile , boolean isWrRank , boolean isOutInactive , String server , String processDate ) throws Exception
	{
		List<ShipInfo>					ships				= Models.loadModels( mstFile , new ShipInfo() , WowsModelBase.cs );
		Map<Long,ShipInfo>				shipMap				= Models.toMap( ships );
		Map<ShipBattleInfoKey,ShipBattleInfo>	modelMap	= new TreeMap<ShipBattleInfoKey,ShipBattleInfo>();
		outFile.getParentFile().mkdirs();

		File[]							acntFiles			= acntFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File acntFile : acntFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , acntFiles.length ) + ":" + acntFile.getName() );
			File						shipFile			= new File( shipFolder , acntFile.getName() );
			processShipStats( acntFile , shipFile , modelMap , shipMap , isWrRank , isOutInactive , server , processDate );
			ix++;
		}
		shipMap.clear();

		outLog( outFile.getPath() );
		Models.storeModels( outFile , false , modelMap.values() , ShipBattleInfo.ST_STATS , WowsModelBase.cs );
		modelMap.clear();
	}
	protected void processShipStats( File acntFile , File shipFile , Map<ShipBattleInfoKey,ShipBattleInfo> modelMap , Map<Long,ShipInfo> shipMap , boolean isWrRank , boolean isOutInactive , String server , String processDate ) throws IOException
	{
		List<ShipBattleInfo>			shipBattles			= Models.loadModels( shipFile , new ShipBattleInfo() , WowsModelBase.cs );
		List<AccountBattleInfo>			accountBattles		= Models.loadModels( acntFile , new AccountBattleInfo() , WowsModelBase.cs );
		Map<Long,AccountBattleInfo>		accountMap			= Models.toMap( accountBattles );

		for( ShipBattleInfo shipBattle : shipBattles )
		{
			long						accountRank			= 0;
			if( isWrRank )
			{
				AccountBattleInfo		account				= accountMap.get( Long.valueOf( shipBattle.key.accountId ) );
				if( account == null )
				{
					accountRank								= 50;
				}
				else
				{
					accountRank								= account.getAccountRank();
				}
			}
			ShipBattleInfoKey			key					= new ShipBattleInfoKey( accountRank , shipBattle.key.shipId );
			ShipBattleInfo				model				= modelMap.get( key );
			if( model == null )
			{
				model										= new ShipBattleInfo( key );
				model.setReportKey( server , processDate );
				ShipInfo				ship				= shipMap.get( Long.valueOf( shipBattle.key.shipId ) );
				if( ship != null )
				{
					model.setShipInfo( ship );
				}
			}
			shipBattle.players								= BigDecimal.ONE;
			model.add( shipBattle );
			if( ( model.active ) || ( isOutInactive ) )
			{
				modelMap.put( model.key , model );
			}
		}
		accountMap.clear();
		shipBattles.clear();
	}
}

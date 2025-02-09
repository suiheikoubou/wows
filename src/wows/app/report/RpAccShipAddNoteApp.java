package com.suiheikoubou.wows.app.report;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RpAccShipAddNoteApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 12 )
			{
				File					mstFolder			= new File( args[0] );
				File					mstFile				= new File( mstFolder       , args[7] );
				File					acntBaseFolder		= new File( args[1] );
				File					acntSvrFolder		= new File( acntBaseFolder  , args[5] );
				File					acntThisFolder		= new File( acntSvrFolder   , args[6] );
				File					acntFolder			= new File( acntThisFolder  , args[8] );
				File					shipBaseFolder		= new File( args[2] );
				File					shipSvrFolder		= new File( shipBaseFolder  , args[5] );
				File					shipThisFolder		= new File( shipSvrFolder   , args[6] );
				File					shipFolder			= new File( shipThisFolder  , args[9] );
				File					clanBaseFolder		= new File( args[3] );
				File					clanSvrFolder		= new File( clanBaseFolder  , args[5] );
				File					clanThisFolder		= new File( clanSvrFolder   , args[6] );
				File					clanFolder			= new File( clanThisFolder  , args[10] );
				File					outBaseFolder		= new File( args[4] );
				File					outSvrFolder		= new File( outBaseFolder   , args[5] );
				File					outThisFolder		= new File( outSvrFolder    , args[6] );
				File					outFolder			= new File( outThisFolder   , args[11] );
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpAccShipAddNoteApp		instance			= new RpAccShipAddNoteApp( logFile );
				instance.execute( mstFile , acntFolder , shipFolder , clanFolder , outFolder );
			}
			else
			{
				System.out.println( "usage : java RpAccShipAddNoteApp [mst folder] [acnt base folder] [ship base folder] [clan base folder] [out base folder] [server] [date this] [mst file] [acnt folder] [ship folder] [clan folder] [out folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RpAccShipAddNoteApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File mstFile , File acntFolder , File shipFolder , File clanFolder , File outFolder ) throws Exception
	{
		List<ShipInfo>					ships				= Models.loadModels( mstFile , new ShipInfo() , WowsModelBase.cs );
		Map<Long,ShipInfo>				shipMap				= Models.toMap( ships );

		outFolder.mkdirs();
		File[]							shipFiles			= shipFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File shipFile : shipFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , shipFiles.length ) + ":" + shipFile.getName() );
			File						acntFile			= new File( acntFolder , shipFile.getName() );
			File						clanFile			= new File( clanFolder , shipFile.getName() );
			File						outFile				= new File( outFolder  , shipFile.getName() );
			processAddNote( shipMap , acntFile , shipFile , clanFile , outFile );
			ix++;
		}
		shipMap.clear();
	}
	protected void processAddNote( Map<Long,ShipInfo> shipMap , File acntFile , File shipFile , File clanFile , File outFile ) throws IOException
	{
		List<ShipBattleInfo>			shipBattles			= Models.loadModels( shipFile , new ShipBattleInfo() , WowsModelBase.cs );
		List<AccountBattleInfo>			accountInfos		= Models.loadModels( acntFile , new AccountBattleInfo() , WowsModelBase.cs );
		List<AccountClanInfo>			clanInfos			= Models.loadModels( clanFile , new AccountClanInfo()   , WowsModelBase.cs );
		Map<Long,AccountBattleInfo>		accountMap			= Models.toMap( accountInfos );
		Map<Long,AccountClanInfo>		clanMap				= Models.toMap( clanInfos );

		for( ShipBattleInfo shipBattle : shipBattles )
		{
			ShipInfo					ship				= shipMap.get( Long.valueOf( shipBattle.key.shipId ) );
			AccountBattleInfo			accountInfo			= accountMap.get( Long.valueOf( shipBattle.key.accountId ) );
			AccountClanInfo				clanInfo			= clanMap.get( Long.valueOf( shipBattle.key.accountId ) );
			String						clanTag				= "";
			if( ship != null )
			{
				shipBattle.setShipInfo( ship );
			}
			if( clanInfo != null )
			{
				clanTag										= clanInfo.clanTag;
			}
			if( accountInfo != null )
			{
				shipBattle.setAccountInfo( clanTag , accountInfo.accountName );
			}
		}
		accountMap.clear();
		clanMap.clear();

		Models.storeModels( outFile , false , shipBattles , ShipBattleInfo.ST_ADDNOTE , WowsModelBase.cs );
		shipBattles.clear();
	}
}

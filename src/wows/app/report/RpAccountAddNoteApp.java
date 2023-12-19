package com.suiheikoubou.wows.app.report;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RpAccountAddNoteApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 10 )
			{
				File					acntBaseFolder		= new File( args[0] );
				File					acntSvrFolder		= new File( acntBaseFolder  , args[4] );
				File					acntThisFolder		= new File( acntSvrFolder   , args[5] );
				File					acntFolder			= new File( acntThisFolder  , args[6] );
				File					shipBaseFolder		= new File( args[1] );
				File					shipSvrFolder		= new File( shipBaseFolder  , args[4] );
				File					shipThisFolder		= new File( shipSvrFolder   , args[5] );
				File					shipFolder			= new File( shipThisFolder  , args[7] );
				File					clanBaseFolder		= new File( args[2] );
				File					clanSvrFolder		= new File( clanBaseFolder  , args[4] );
				File					clanThisFolder		= new File( clanSvrFolder   , args[5] );
				File					clanFolder			= new File( clanThisFolder  , args[8] );
				File					outBaseFolder		= new File( args[3] );
				File					outSvrFolder		= new File( outBaseFolder   , args[4] );
				File					outThisFolder		= new File( outSvrFolder    , args[5] );
				File					outFolder			= new File( outThisFolder   , args[9] );
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpAccountAddNoteApp		instance			= new RpAccountAddNoteApp( logFile );
				instance.execute( acntFolder , shipFolder , clanFolder , outFolder );
			}
			else
			{
				System.out.println( "usage : java RpAccountAddNoteApp [acnt base folder] [ship base folder] [clan base folder] [out base folder] [server] [date this] [acnt folder] [ship folder] [clan folder] [out folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RpAccountAddNoteApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File acntFolder , File shipFolder , File clanFolder , File outFolder ) throws Exception
	{
		outFolder.mkdirs();

		File[]							acntFiles			= acntFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File acntFile : acntFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , acntFiles.length ) + ":" + acntFile.getName() );
			File						shipFile			= new File( shipFolder , acntFile.getName() );
			File						clanFile			= new File( clanFolder , acntFile.getName() );
			File						outFile				= new File( outFolder  , acntFile.getName() );
			processAddNote( acntFile , shipFile , clanFile , outFile );
			ix++;
		}
	}
	protected void processAddNote( File acntFile , File shipFile , File clanFile , File outFile ) throws IOException
	{
		List<AccountBattleInfo>			accountInfos		= Models.loadModels( acntFile , new AccountBattleInfo() , WowsModelBase.cs );
		List<AccountSummaryInfo>		summaryInfos		= Models.loadModels( shipFile , new AccountSummaryInfo(), WowsModelBase.cs );
		List<AccountClanInfo>			clanInfos			= Models.loadModels( clanFile , new AccountClanInfo()   , WowsModelBase.cs );
		Map<Long,AccountSummaryInfo>	summaryMap			= Models.toMap( summaryInfos );
		Map<Long,AccountClanInfo>		clanMap				= Models.toMap( clanInfos );

		for( AccountBattleInfo accountInfo : accountInfos )
		{
			AccountSummaryInfo			summaryInfo			= summaryMap.get( Long.valueOf( accountInfo.accountId ) );
			AccountClanInfo				clanInfo			= clanMap.get( Long.valueOf( accountInfo.accountId ) );
			accountInfo.addNote( clanInfo , summaryInfo );
		}
		summaryMap.clear();
		clanMap.clear();

		Models.storeModels( outFile , false , accountInfos , AccountBattleInfo.ST_ADDNOTE , WowsModelBase.cs );
		accountInfos.clear();
	}
}

package com.suiheikoubou.wows.app.migrate;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class MigAppAccount extends AbstractApp
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
				MigAppAccount			instance			= new MigAppAccount( logFile );
				instance.execute( inFolder , outFolder );
			}
			else
			{
				System.out.println( "usage : java MigAppAccount [in base folder] [out base folder] [server] [date this] [in folder] [out folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public MigAppAccount( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder ) throws Exception
	{
		ModelStorage<AccountBattleInfo>	modelStorage		= new ModelStorage<AccountBattleInfo>();
		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			Map<Long,XAccountBattleInfo>	infos			= XAccountBattleInfo.loadXAccountBattleInfos( inFile );
			for( XAccountBattleInfo info : infos.values() )
			{
				AccountBattleInfo		model				= new AccountBattleInfo();
				copyInfo( model , info );
				modelStorage.add( model , WowsModelBase.KT_AID1000 );
			}
			modelStorage.store( outFolder , ".txt" , true , AccountBattleInfo.ST_NONE , WowsModelBase.cs );
			modelStorage.clear();
			ix++;
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
	}
	protected void copyInfo( AccountBattleInfo newInfo , XAccountBattleInfo oldInfo )
	{
		newInfo.accountId									= oldInfo.accountId;
		newInfo.accountName									= oldInfo.accountName;
		newInfo.levelingTier								= oldInfo.levelingTier;
		newInfo.createdAt									= oldInfo.createdAt;
		newInfo.lastBattleTime								= oldInfo.lastBattleTime;
		newInfo.hiddenProfile								= oldInfo.hiddenProfile;

		newInfo.pvpBattles									= oldInfo.pvpBattles;
		newInfo.pvpWins										= oldInfo.pvpWins;
		newInfo.pvpDraws									= oldInfo.pvpDraws;
		newInfo.pvpLosses									= oldInfo.pvpLosses;
		newInfo.pveBattles									= oldInfo.pveBattles;
		newInfo.pveWins										= oldInfo.pveWins;
		newInfo.rankBattles									= oldInfo.rankBattles;
		newInfo.rankWins									= oldInfo.rankWins;
		newInfo.clubBattles									= oldInfo.clubBattles;
		newInfo.clubWins									= oldInfo.clubWins;
		newInfo.pvp1Battles									= oldInfo.pvp1Battles;
		newInfo.pvp1Wins									= oldInfo.pvp1Wins;
		newInfo.pvp2Battles									= oldInfo.pvp2Battles;
		newInfo.pvp2Wins									= oldInfo.pvp2Wins;
		newInfo.pvp3Battles									= oldInfo.pvp3Battles;
		newInfo.pvp3Wins									= oldInfo.pvp3Wins;
		newInfo.oper1Battles								= oldInfo.oper1Battles;
		newInfo.oper1Wins									= oldInfo.oper1Wins;
		newInfo.operdBattles								= oldInfo.operdBattles;
		newInfo.operdWins									= oldInfo.operdWins;
		newInfo.operhBattles								= oldInfo.operhBattles;
		newInfo.operhWins									= oldInfo.operhWins;
	}
}

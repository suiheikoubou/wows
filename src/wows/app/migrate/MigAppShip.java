package com.suiheikoubou.wows.app.migrate;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class MigAppShip extends AbstractApp
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
				MigAppShip				instance			= new MigAppShip( logFile );
				instance.execute( inFolder , outFolder );
			}
			else
			{
				System.out.println( "usage : java MigAppShip [in base folder] [out base folder] [server] [date this] [in folder] [out folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public MigAppShip( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder ) throws Exception
	{
		ModelStorage<ShipBattleInfo>	modelStorage		= new ModelStorage<ShipBattleInfo>();
		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			Map<ShipResultKey,ShipResultValue>	infos		= ShipResultValue.loadShipResults( inFile );
			for( ShipResultValue info : infos.values() )
			{
				ShipBattleInfo		model				= new ShipBattleInfo();
				copyInfo( model , info );
				modelStorage.add( model , WowsModelBase.KT_AID1000 );
			}
			modelStorage.store( outFolder , ".txt" , true , ShipBattleInfo.ST_NONE , WowsModelBase.cs );
			modelStorage.clear();
			ix++;
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
	}
	protected void copyInfo( ShipBattleInfo newInfo , ShipResultValue oldInfo )
	{
		newInfo.key.accountId								= 	oldInfo.key.accountId	;
		newInfo.key.shipId									= 	oldInfo.key.shipId	;
		newInfo.valueBattles								= 	oldInfo.valueBattles	;
		newInfo.valueWins									= 	oldInfo.valueWins	;
		newInfo.valueDraws									= 	oldInfo.valueDraws	;
		newInfo.valueLosses									= 	oldInfo.valueLosses	;
		newInfo.valueSurvivedBattles						= 	oldInfo.valueSurvivedBattles	;
		newInfo.valueDamageDealt							= 	oldInfo.valueDamageDealt	;
		newInfo.valueFrags									= 	oldInfo.valueFrags	;
		newInfo.valuePlanesKilled							= 	oldInfo.valuePlanesKilled	;
		newInfo.valueCapturePoints							= 	oldInfo.valueCapturePoints	;
		newInfo.valueDroppedCapturePoints					= 	oldInfo.valueDroppedCapturePoints	;
		newInfo.valueXp										= 	oldInfo.valueXp	;
		newInfo.valueArtAgro								= 	oldInfo.valueArtAgro	;
		newInfo.valueTorpedoAgro							= 	oldInfo.valueTorpedoAgro	;
		newInfo.valueDamageScouting							= 	oldInfo.valueDamageScouting	;
		newInfo.valueShipsSpotted							= 	oldInfo.valueShipsSpotted	;
		newInfo.valueTeamCapturePoints						= 	oldInfo.valueTeamCapturePoints	;
		newInfo.valueTeamDroppedPoints						= 	oldInfo.valueTeamDroppedPoints	;
		newInfo.valueBattlesSince512						= 	oldInfo.valueBattlesSince512	;
		newInfo.valueMainShots								= 	oldInfo.valueMainShots	;
		newInfo.valueMainHits								= 	oldInfo.valueMainHits	;
		newInfo.valueDistance								= 	oldInfo.valueDistance	;
	}
}

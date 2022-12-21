package com.suiheikoubou.wows.app.process;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class SubtractShipBattleApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 8 )
			{
				File					inBaseFolder		= new File( args[0] );
				File					inSvrFolder			= new File( inBaseFolder  , args[2] );
				File					thisFolder			= new File( inSvrFolder   , args[3] );
				File					prevFolder			= new File( inSvrFolder   , args[4] );
				File					inThisFolder		= new File( thisFolder    , args[5] );
				File					inPrevFolder		= new File( prevFolder    , args[5] );
				File					outBaseFolder		= new File( args[1] );
				File					outSvrFolder		= new File( outBaseFolder , args[2] );
				File					outThisFolder		= new File( outSvrFolder  , args[3] );
				File					outFolder			= new File( outThisFolder , args[6] );
				File					hiddenFile			= new File( prevFolder    , args[7] );
				File					logFile				= new File( outFolder     , "error.log" );
				SubtractShipBattleApp	instance			= new SubtractShipBattleApp( logFile );
				instance.execute( inThisFolder , inPrevFolder , outFolder , hiddenFile );
			}
			else
			{
				System.out.println( "usage : java SubtractShipBattleApp [in base folder] [out base folder] [server] [date this] [date prev] [in folder] [out folder] [hidden file]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public SubtractShipBattleApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inThisFolder , File inPrevFolder , File outFolder , File hiddenFile ) throws Exception
	{
		List<Long>						hiddens				= WowsModelBase.loadLongs( hiddenFile );
		outFolder.mkdirs();
		File[]							inThisFiles			= inThisFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inThisFile : inThisFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inThisFiles.length ) + ":" + inThisFile.getName() );
			File						inPrevFile			= new File( inPrevFolder , inThisFile.getName() );
			File						outFile				= new File( outFolder    , inThisFile.getName() );
			processSubtract( inThisFile , inPrevFile , outFile , hiddens );
			ix++;
		}
		hiddens.clear();
	}
	protected void processSubtract( File inThisFile , File inPrevFile , File outFile , List<Long> hiddens ) throws IOException
	{
		List<ShipBattleInfo>			outModels			= new ArrayList<ShipBattleInfo>();
		List<ShipBattleInfo>			thisModels			= Models.loadModels( inThisFile , new ShipBattleInfo() , WowsModelBase.cs );
		List<ShipBattleInfo>			prevModels			= Models.loadModels( inPrevFile , new ShipBattleInfo() , WowsModelBase.cs );
		Map<ShipBattleInfoKey,ShipBattleInfo>	prevMap		= Models.toMap( prevModels );

		for( ShipBattleInfo thisModel : thisModels )
		{
			if( !( hiddens.contains( Long.valueOf(thisModel.key.accountId) ) ) )
			{
				ShipBattleInfo			prevModel			= prevMap.get( thisModel.key );
				if( prevModel != null )
				{
					thisModel.subtract( prevModel );
				}
				if( thisModel.valueBattles.signum() > 0 )
				{
					outModels.add( thisModel );
				}
			}
		}

		thisModels.clear();
		prevMap.clear();
		Models.storeModels( outFile , false , outModels , ShipBattleInfo.ST_NONE , WowsModelBase.cs );
		outModels.clear();
	}
}

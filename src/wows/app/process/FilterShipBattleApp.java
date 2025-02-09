package com.suiheikoubou.wows.app.process;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class FilterShipBattleApp extends AbstractApp
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
				FilterShipBattleApp		instance			= new FilterShipBattleApp( logFile );
				instance.execute( mstFile , inFolder , outFolder );
			}
			else
			{
				System.out.println( "usage : java FilterShipBattleApp [mst folder] [in base folder] [out base folder] [server] [date this] [mst file] [in folder] [out folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public FilterShipBattleApp( File logFile )
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
			process_filter( shipMap , inFile , outFile );
			ix++;
		}
		shipMap.clear();

	}
	protected void process_filter( Map<Long,ShipInfo> shipMap , File inFile , File outFile ) throws IOException
	{
		List<ShipBattleInfo>			outModels			= new ArrayList<ShipBattleInfo>();
		List<ShipBattleInfo>			shipBattles			= Models.loadModels( inFile , new ShipBattleInfo() , WowsModelBase.cs );
		for( ShipBattleInfo shipBattle : shipBattles )
		{
			Long						shipId				= Long.valueOf( shipBattle.key.shipId );
			ShipInfo					ship				= shipMap.get( shipId );
			if( ship == null )
			{
//				System.out.println( "unknown ship > " + shipId.toString() );
			}
			else
			{
//				if( ( ship.shipType.equals( ShipInfo.TYPE_CV ) ) && ( ship.tier == 10 ) )
				if( shipBattle.key.shipId == 3655218480L )
				{
					outModels.add( shipBattle );
				}
			}
		}
		shipBattles.clear();
		Models.storeModels( outFile , false , outModels , ShipBattleInfo.ST_NONE , WowsModelBase.cs );
		outModels.clear();
	}
}

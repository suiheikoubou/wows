package com.suiheikoubou.wows.app.extract;

import java.io.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public final class MstShipExtApp extends AbstractApp
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
				File					outFile				= new File( outBaseFolder , args[5] );
				File					oldFile				= null;
				if( args.length >= 7 )
				{
					oldFile									= new File( outBaseFolder , args[6] );
				}
				File					logFile				= new File( outBaseFolder  , "error.log" );
				MstShipExtApp			instance			= new MstShipExtApp( logFile );
				instance.execute( inFolder , outFile , oldFile );
			}
			else
			{
				System.out.println( "usage : java MstShipExtApp [in base folder] [out base folder] [server] [date this] [in folder] [out file] (old file)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public MstShipExtApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFile , File oldFile ) throws Exception
	{
		List<ShipInfo>					models				= new ArrayList<ShipInfo>();
		if( oldFile != null )
		{
			List<ShipInfo>				oldmodels			= Models.loadModels( oldFile , new ShipInfo() , WowsModelBase.cs );
			for( ShipInfo model : oldmodels )
			{
				models.add( model );
			}
			oldmodels.clear();
		}
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Json() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			try
			{
				outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
				List<ShipInfo>			pmodels				= loadModels( inFile );
				for( ShipInfo model : pmodels )
				{
					models.add( model );
				}
				ix++;
			}
			catch( Exception ex )
			{
				outLog( "file:" + inFile.getPath() );
				throw	ex;
			}
		}
		Collections.sort( models );
		Models.storeModels( outFile , models , ShipInfo.ST_NONE , WowsModelBase.cs );
		models.clear();
	}
	protected List<ShipInfo> loadModels( File inFile ) throws Exception
	{
		List<ShipInfo>					models				= new ArrayList<ShipInfo>();

		Map								jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( inFile ) ));
		if( JsonUtil.getMapDataString( jsonData , "status" ).equals( "ok" ) )
		{
			Map							dataMap				= (Map)(jsonData.get( "data" ));
			for( Object shipDataObj : dataMap.values() )
			{
				Map						shipData			= (Map)shipDataObj;
				ShipInfo				model				= new ShipInfo();
				model.shipId								= JsonUtil.getMapDataLong	( shipData , "ship_id" );
				model.shipName								= JsonUtil.getMapDataString	( shipData , "name" );
				model.shipType								= JsonUtil.getMapDataString	( shipData , "type" );
				model.nation								= JsonUtil.getMapDataString	( shipData , "nation" );
				model.tier									= JsonUtil.getMapDataInt	( shipData , "tier" );
				model.premium								= JsonUtil.getMapDataBoolean( shipData , "is_premium" );
				model.active								= true;
				if( model.shipId == 3552524272L )	// Alabama ST
				{
					model.active							= false;
				}
				model.priceCredit							= JsonUtil.getMapDataInt	( shipData , "price_credit" );
				model.priceGold								= JsonUtil.getMapDataInt	( shipData , "price_gold" );
				model.shipIdStr								= JsonUtil.getMapDataString	( shipData , "ship_id_str" );
				models.add( model );
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

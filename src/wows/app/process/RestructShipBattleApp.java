package com.suiheikoubou.wows.app.process;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RestructShipBattleApp extends AbstractApp
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
				String					type				= args[6];
				File					logFile				= new File( outFolder     , "error.log" );
				RestructShipBattleApp	instance			= new RestructShipBattleApp( logFile );
				instance.execute( inFolder , outFolder , type );
			}
			else
			{
				System.out.println( "usage : java RestructShipBattleApp [in base folder] [out base folder] [server] [date this] [in folder] [out folder] [type]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RestructShipBattleApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder , String type ) throws Exception
	{
		int								keyType				= getKeyType( type );
		ModelStorage<ShipBattleInfo>	modelStorage		= new ModelStorage<ShipBattleInfo>();
		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			List<ShipBattleInfo>		models				= Models.loadModels( inFile , new ShipBattleInfo() , WowsModelBase.cs );
			for( ShipBattleInfo model : models )
			{
				modelStorage.add( model , keyType );
			}
			models.clear();
			ix++;
			if( ix % 100 == 0 )
			{
				outLog( "flush:" + String.format( "%d" , modelStorage.size() ) );
				modelStorage.store( outFolder , ".txt" , true , ShipBattleInfo.ST_NONE , WowsModelBase.cs );
				modelStorage.clear();
			}
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
		outLog( "flush:" + String.format( "%d" , modelStorage.size() ) );
		modelStorage.store( outFolder , ".txt" , true , ShipBattleInfo.ST_NONE , WowsModelBase.cs );
		modelStorage.clear();
	}
	protected int getKeyType( String type )
	{
		int								keyType				= WowsModelBase.KT_NONE;
		if( type.toUpperCase().equals( "ACCOUNT" ) )
		{
			keyType											= WowsModelBase.KT_AID1000;
		}
		else if( type.toUpperCase().equals( "SHIP" ) )
		{
			keyType											= WowsModelBase.KT_SHIPID;
		}
		else
		{
			throw	new IllegalArgumentException( "unknown keyType" );
		}
		return	keyType;
	}
}

package com.suiheikoubou.wows.app.process;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class MergeShipBattleApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 7 )
			{
				File					baseFolder			= new File( args[0] );
				File					baseSvrFolder		= new File( baseFolder    , args[2] );
				File					thisFolder			= new File( baseSvrFolder , args[3] );
				File					prevFolder			= new File( baseSvrFolder , args[4] );
				File					thisOutFolder		= new File( thisFolder    , args[5] );
				File					prevOutFolder		= new File( prevFolder    , args[5] );
				File					tmpBaseFolder		= new File( args[1] );
				File					tmpSvrFolder		= new File( tmpBaseFolder , args[2] );
				File					tmpThisFolder		= new File( tmpSvrFolder  , args[3] );
				File					tmpInFolder			= new File( tmpThisFolder , args[6] );
				File					logFile				= new File( thisFolder    , "error.log" );
				MergeShipBattleApp		instance			= new MergeShipBattleApp( logFile );
				instance.execute( thisOutFolder , prevOutFolder , tmpInFolder );
			}
			else
			{
				System.out.println( "usage : java MergeShipBattleApp [base folder] [tmp base folder] [server] [date this] [date prev] [out folder] [tmp in folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public MergeShipBattleApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File thisOutFolder , File prevOutFolder , File tmpInFolder ) throws Exception
	{
		thisOutFolder.mkdirs();
		File[]							prevFiles			= prevOutFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File prevFile : prevFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , prevFiles.length ) + ":" + prevFile.getName() );
			File						thisFile			= new File( thisOutFolder , prevFile.getName() );
			File						tmpFile				= new File( tmpInFolder   , prevFile.getName() );
			Map<ShipBattleInfoKey,ShipBattleInfo>	modelMap= new TreeMap<ShipBattleInfoKey,ShipBattleInfo>();
			processMerge( modelMap , prevFile );
			processMerge( modelMap , tmpFile  );
			Models.storeModels( thisFile , false , modelMap.values() , ShipBattleInfo.ST_NONE , WowsModelBase.cs );
			modelMap.clear();
			ix++;
		}
	}
	protected void 	processMerge( Map<ShipBattleInfoKey,ShipBattleInfo> modelMap, File inFile ) throws IOException
	{
		if( inFile.exists() )
		{
			List<ShipBattleInfo>		inModels			= Models.loadModels( inFile , new ShipBattleInfo() , WowsModelBase.cs );
			for( ShipBattleInfo model : inModels )
			{
				modelMap.put( model.key , model );
			}
			inModels.clear();
		}
	}
}

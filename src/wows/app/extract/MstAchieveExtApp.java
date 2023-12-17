package com.suiheikoubou.wows.app.extract;

import java.io.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public final class MstAchieveExtApp extends AbstractApp
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
				MstAchieveExtApp		instance			= new MstAchieveExtApp( logFile );
				instance.execute( inFolder , outFile , oldFile );
			}
			else
			{
				System.out.println( "usage : java MstAchieveExtApp [in base folder] [out base folder] [server] [date this] [in folder] [out file] (old file)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public MstAchieveExtApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFile , File oldFile ) throws Exception
	{
		List<AchieveInfo>				models				= new ArrayList<AchieveInfo>();
		if( oldFile != null )
		{
			List<AchieveInfo>			oldmodels			= Models.loadModels( oldFile , new AchieveInfo() , WowsModelBase.cs );
			for( AchieveInfo model : oldmodels )
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
				List<AchieveInfo>		pmodels				= loadModels( inFile );
				for( AchieveInfo model : pmodels )
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
		Models.storeModels( outFile , models , AchieveInfo.ST_NONE , WowsModelBase.cs );
		models.clear();
	}
	protected List<AchieveInfo> loadModels( File inFile ) throws Exception
	{
		List<AchieveInfo>				models				= new ArrayList<AchieveInfo>();

		Map								jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( inFile ) ));
		if( JsonUtil.getMapDataString( jsonData , "status" ).equals( "ok" ) )
		{
			Map							dataMap				= (Map)(jsonData.get( "data" ));
			Map							battleMap			= (Map)(dataMap.get( "battle" ));
			for( Object battleDataObj : battleMap.values() )
			{
				Map						battleData			= (Map)battleDataObj;
				AchieveInfo				model				= new AchieveInfo();
				model.achieveId								= JsonUtil.getMapDataString	( battleData , "achievement_id" );
				model.achieveName							= JsonUtil.getMapDataString	( battleData , "name" );
				model.achieveType							= JsonUtil.getMapDataString	( battleData , "type" );
				model.achieveSubType						= JsonUtil.getMapDataString	( battleData , "sub_type" );
				model.description							= JsonUtil.getMapDataString	( battleData , "description" );
				model.multiple								= JsonUtil.getMapDataInt	( battleData , "multiple" );
				model.threshold								= -1;
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

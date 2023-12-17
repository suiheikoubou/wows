package com.suiheikoubou.wows.app.extract;

import java.io.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public final class AchieveExtApp extends AbstractApp
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
				AchieveExtApp		instance			= new AchieveExtApp( logFile );
				instance.execute( inFolder , outFolder );
			}
			else
			{
				System.out.println( "usage : java AchieveExtApp [in base folder] [out base folder] [server] [date this] [in folder] [out folder]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public AchieveExtApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder ) throws Exception
	{
		ModelStorage<AccountAchieveInfo>	modelStorage	= new ModelStorage<AccountAchieveInfo>();
		outLog( outFolder.getPath() );
		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Json() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			try
			{
				if( ix % 1000 == 0 )
				{
					outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
				}
				List<AccountAchieveInfo>	models			= loadModels( inFile );
				for( AccountAchieveInfo model : models )
				{
					modelStorage.add( model , WowsModelBase.KT_AID1000 );
				}
				models.clear();
				ix++;
				if( ix % 100000 == 0 )
				{
					outLog( "flush:" + String.format( "%d" , modelStorage.size() ) );
					modelStorage.store( outFolder , ".txt" , true , AccountAchieveInfo.ST_NONE , WowsModelBase.cs );
					modelStorage.clear();
				}
			}
			catch( Exception ex )
			{
				outLog( "file:" + inFile.getPath() );
				throw	ex;
			}
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
		outLog( "flush:" + String.format( "%d" , modelStorage.size() ) );
		modelStorage.store( outFolder , ".txt" , true , AccountAchieveInfo.ST_NONE , WowsModelBase.cs );
		modelStorage.clear();
	}
	protected List<AccountAchieveInfo> loadModels( File inFile ) throws Exception
	{
		List<AccountAchieveInfo>		models				= new ArrayList<AccountAchieveInfo>();

		Map								jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( inFile ) ));
		if( JsonUtil.getMapDataString( jsonData , "status" ).equals( "ok" ) )
		{
			Map							dataMap				= (Map)(jsonData.get( "data" ));
			for( Object accountIdObj : dataMap.keySet() )
			{
				String					accountId			= (String)accountIdObj;
				Map						accountData			= (Map)(dataMap.get( accountIdObj ));
				if( accountData != null )
				{
					Object				battleDataObj		= accountData.get( "battle" );
					Map					battleMap			= (Map)(battleDataObj);
					for( Object achieveIdObj : battleMap.keySet() )
					{
						String			achieveId			= (String)(achieveIdObj);
						BigDecimal		achieveValue		= (BigDecimal)(battleMap.get( achieveIdObj ));
						AccountAchieveInfo	model			= new AccountAchieveInfo();
						model.key.accountId					= Long.valueOf( accountId );
						model.key.achieveId					= achieveId;
						model.achieveValue					= achieveValue.longValue();
						models.add( model );
					}
				}
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

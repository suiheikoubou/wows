package com.suiheikoubou.wows.app.process;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class SubtractAccountBattleApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 7 )
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
				String					since				= "0000/00/00 00:00:00";
				if( args.length >= 8 )
				{
					since									= args[7];
				}
				File					logFile				= new File( outFolder     , "error.log" );
				SubtractAccountBattleApp	instance		= new SubtractAccountBattleApp( logFile );
				instance.execute( inThisFolder , inPrevFolder , outFolder , since );
			}
			else
			{
				System.out.println( "usage : java SubtractAccountBattleApp [in base folder] [out base folder] [server] [date this] [date prev] [in folder] [out folder] (since)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public SubtractAccountBattleApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inThisFolder , File inPrevFolder , File outFolder , String since ) throws Exception
	{
		outFolder.mkdirs();
		File[]							inThisFiles			= inThisFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inThisFile : inThisFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inThisFiles.length ) + ":" + inThisFile.getName() );
			File						inPrevFile			= new File( inPrevFolder , inThisFile.getName() );
			File						outFile				= new File( outFolder    , inThisFile.getName() );
			processSubtract( inThisFile , inPrevFile , outFile , since );
			ix++;
		}
	}
	protected void processSubtract( File inThisFile , File inPrevFile , File outFile , String since ) throws IOException
	{
		List<AccountBattleInfo>			outModels			= new ArrayList<AccountBattleInfo>();
		List<AccountBattleInfo>			thisModels			= Models.loadModels( inThisFile , new AccountBattleInfo() , WowsModelBase.cs );
		List<AccountBattleInfo>			prevModels			= Models.loadModels( inPrevFile , new AccountBattleInfo() , WowsModelBase.cs );
		Map<Long,AccountBattleInfo>		prevMap				= Models.toMap( prevModels );

		for( AccountBattleInfo thisModel : thisModels )
		{
			if( !( thisModel.hiddenProfile ) )
			{
				AccountBattleInfo			prevModel			= prevMap.get( Long.valueOf( thisModel.accountId )) ;
				if( prevModel != null )
				{
					if( prevModel.hiddenProfile )
					{
						thisModel.clean();
					}
					else
					{
						thisModel.subtract( prevModel );
					}
				}
				else
				{
					if( thisModel.createdAt.compareTo( since ) < 0 )
					{
						thisModel.clean();
					}
				}
				if( thisModel.getTotalBattles().signum() > 0 )
				{
					outModels.add( thisModel );
				}
			}
		}

		thisModels.clear();
		prevMap.clear();
		Models.storeModels( outFile , false , outModels , AccountBattleInfo.ST_NONE , WowsModelBase.cs );
		outModels.clear();
	}
}

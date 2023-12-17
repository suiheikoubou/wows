package com.suiheikoubou.wows.app.process;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class FilterAccountBattleApp extends AbstractApp
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
				int						threshold			= 0;
				if( args.length >= 8 )
				{
					threshold								= Integer.parseInt( args[7] );
				}
				File					logFile				= new File( outThisFolder  , "error.log" );
				FilterAccountBattleApp	instance			= new FilterAccountBattleApp( logFile );
				instance.execute( inFolder , outFolder , type , threshold );
			}
			else
			{
				System.out.println( "usage : java FilterAccountBattleApp [in base folder] [out base folder] [server] [date this] [in folder] [out folder] [type] (threshold)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public FilterAccountBattleApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder , String type , int threshold ) throws Exception
	{
		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			File						outFile				= new File( outFolder , inFile.getName() );
			List<AccountBattleInfo>		outModels			= new ArrayList<AccountBattleInfo>();
			List<AccountBattleInfo>		inModels			= Models.loadModels( inFile , new AccountBattleInfo() , WowsModelBase.cs );
			for( AccountBattleInfo accountInfo : inModels )
			{
				boolean					isOut				= false;
				if( type.equals( "pvpbattles" ) )
				{
					if( accountInfo.pvpBattles.intValue() >= threshold )
					{
						isOut								= true;
					}
				}
				if( isOut )
				{
					outModels.add( accountInfo );
				}
			}
			inModels.clear();
			Models.storeModels( outFile , false , outModels , AccountBattleInfo.ST_NONE , WowsModelBase.cs );
			outModels.clear();
			ix++;
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
	}
}

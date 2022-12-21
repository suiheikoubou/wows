package com.suiheikoubou.wows.app.process;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class FilterAccountIdApp extends AbstractApp
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
				File					outFile				= new File( outThisFolder , args[5] );
				String					type				= args[6];
				String					since				= "0000/00/00";
				if( args.length >= 8 )
				{
					since									= args[7];
				}
				File					logFile				= new File( outThisFolder  , "error.log" );
				FilterAccountIdApp		instance			= new FilterAccountIdApp( logFile );
				instance.execute( inFolder , outFile , type , since );
			}
			else
			{
				System.out.println( "usage : java FilterAccountIdApp [in base folder] [out base folder] [server] [date this] [in folder] [out file] [type] (since)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public FilterAccountIdApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFile , String type , String since ) throws Exception
	{
		Set<String>						ids					= new TreeSet<String>();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			List<AccountBattleInfo>		models				= Models.loadModels( inFile , new AccountBattleInfo() , WowsModelBase.cs );
			for( AccountBattleInfo accountInfo : models )
			{
				boolean					isOut				= false;
				if( type.equals( "all" ) )
				{
					isOut									= true;
				}
				if( type.equals( "active" ) )
				{
					if( !( accountInfo.hiddenProfile ) )
					if( accountInfo.lastBattleTime.compareTo( since ) > 0 )
					{
						isOut								= true;
					}
				}
				if( type.equals( "hidden" ) )
				{
					isOut									= accountInfo.hiddenProfile;
				}
				if( isOut )
				{
					ids.add( String.valueOf( accountInfo.accountId ) );
				}
			}
			models.clear();
			ix++;
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
		FileIO.storeLines( outFile , ids , WowsModelBase.cs );
		ids.clear();
	}
}

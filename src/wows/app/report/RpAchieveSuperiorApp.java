package com.suiheikoubou.wows.app.report;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RpAchieveSuperiorApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 9 )
			{
				File					inBaseFolder		= new File( args[0] );
				File					inSvrFolder			= new File( inBaseFolder  , args[3] );
				File					thisFolder			= new File( inSvrFolder   , args[4] );
				File					inFolder			= new File( thisFolder    , args[6] );
				File					acntFolder			= new File( thisFolder    , args[7] );
				File					outBaseFolder		= new File( args[1] );
				File					outSvrFolder		= new File( outBaseFolder , args[3] );
				File					outThisFolder		= new File( outSvrFolder  , args[4] );
				File					outFile				= new File( outThisFolder , args[8] );
				File					mstBaseFolder		= new File( args[2] );
				File					mstFile				= new File( mstBaseFolder , args[5] );
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpAchieveSuperiorApp	instance			= new RpAchieveSuperiorApp( logFile );
				instance.execute( mstFile , inFolder , acntFolder , outFile );
			}
			else
			{
				System.out.println( "usage : java RpAchieveSuperiorApp [in base folder] [out base folder] [mst base folder] [server] [date this] [mst file] [in folder] [acnt folder] [out file]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RpAchieveSuperiorApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File mstFile , File inFolder , File acntFolder , File outFile ) throws Exception
	{
		List<String>					outLines			= new ArrayList<String>();
		List<AchieveInfo>				achieves			= Models.loadModels( mstFile , new AchieveInfo() , WowsModelBase.cs );
		Map<String,AchieveInfo>			achieveMap			= Models.toMap( achieves );
		outFile.getParentFile().mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			File						acntFile			= new File( acntFolder , inFile.getName() );
			processAchieve( achieveMap , inFile , acntFile , outLines );
			ix++;
		}
		achieveMap.clear();
		FileIO.storeLines( outFile , outLines , WowsModelBase.cs );
		outLines.clear();
	}
	protected void processAchieve( Map<String,AchieveInfo> achieveMap , File inFile , File acntFile , List<String> outLines ) throws IOException
	{
		List<AccountAchieveInfo>		infos				= Models.loadModels( inFile   , new AccountAchieveInfo() , WowsModelBase.cs );
		List<AccountBattleInfo>			accounts			= Models.loadModels( acntFile , new AccountBattleInfo()  , WowsModelBase.cs );
		Map<Long,AccountBattleInfo>		accountMap			= Models.toMap( accounts );

		for( AccountAchieveInfo info : infos )
		{
			AchieveInfo					achieve				= achieveMap.get( info.key.achieveId );
			if( ( achieve.threshold > 0  ) && ( info.achieveValue.longValue()  >= achieve.threshold ) )
			{
				AccountBattleInfo		account				= accountMap.get( Long.valueOf( info.key.accountId ) );
				String					outLine				= getOutLine( achieve , info , account );
				outLines.add( outLine );
			}
		}
		infos.clear();
		accountMap.clear();
	}
	protected String getOutLine( AchieveInfo achieve , AccountAchieveInfo info , AccountBattleInfo account )
	{
		StringBuffer					buffer				= new StringBuffer();
		WowsModelBase.appendString( buffer , info.key.accountId				, false );
		WowsModelBase.appendString( buffer , achieve.achieveId				, true );
		WowsModelBase.appendString( buffer , account.accountName			, true );
		WowsModelBase.appendString( buffer , achieve.achieveName			, true );
		WowsModelBase.appendString( buffer , info.achieveValue.longValue()	, true );
		return	buffer.toString();
	}
}

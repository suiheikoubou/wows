package com.suiheikoubou.wows.app.report;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RpAccountStatsApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 8 )
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
//				File					outFile				= new File( outThisFolder , args[7] );
				File					outFile				= new File( outBaseFolder , args[2] + args[3] + args[7] );
				String					repLabel			= args[2] + "\t" + args[3];
				String					since				= args[6];
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpAccountStatsApp		instance			= new RpAccountStatsApp( logFile );
				instance.execute( inThisFolder , inPrevFolder , outFile , repLabel , since );
			}
			else
			{
				System.out.println( "usage : java RpAccountStatsApp [in base folder] [out base folder] [server] [date this] [date prev] [in folder] [since] [out file]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RpAccountStatsApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inThisFolder , File inPrevFolder , File outFile , String repLabel , String since ) throws Exception
	{
		outFile.getParentFile().mkdirs();
		Map<Long,Long>					reportMap			= new TreeMap<Long,Long>();
		File[]							inThisFiles			= inThisFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inThisFile : inThisFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inThisFiles.length ) + ":" + inThisFile.getName() );
			File						inPrevFile			= new File( inPrevFolder , inThisFile.getName() );
			processSubtract( inThisFile , inPrevFile , reportMap , since );
			ix++;
		}
		outReport( outFile , reportMap , repLabel );
	}
	protected void processSubtract( File inThisFile , File inPrevFile , Map<Long,Long> reportMap , String since ) throws IOException
	{
		List<AccountBattleInfo>			thisModels			= Models.loadModels( inThisFile , new AccountBattleInfo() , WowsModelBase.cs );
		List<AccountBattleInfo>			prevModels			= Models.loadModels( inPrevFile , new AccountBattleInfo() , WowsModelBase.cs );
		Map<Long,AccountBattleInfo>		prevMap				= Models.toMap( prevModels );

		for( AccountBattleInfo thisModel : thisModels )
		{
			AccountBattleInfo			prevModel			= prevMap.get( Long.valueOf( thisModel.accountId )) ;
			Long						accountCategory		= getAccountCategory( thisModel , prevModel , since );
			Long						catCnt				= reportMap.get( accountCategory );
			if( catCnt == null )
			{
				catCnt										= Long.valueOf( 1 );
			}
			else
			{
				catCnt										= Long.valueOf( catCnt.longValue() + 1 );
			}
			reportMap.put( accountCategory , catCnt );
		}
		thisModels.clear();
		prevMap.clear();
	}
	protected Long getAccountCategory( AccountBattleInfo thisInfo , AccountBattleInfo prevInfo , String since )
	{
		long							res					= 0;
		if( thisInfo.hiddenProfile )
		{
			res												= -99;
		}
		else
		{
			if( prevInfo != null )
			{
				if( prevInfo.hiddenProfile )
				{
					res										= -99;
				}
				else
				{
					thisInfo.subtract( prevInfo );
					res										= getAccountCategorySub( thisInfo );
				}
			}
			else
			{
				if( thisInfo.createdAt.compareTo( since ) < 0 )
				{
					res										= -10;
				}
				else
				{
					res										= getAccountCategorySub( thisInfo );
				}
			}
		}
		return	Long.valueOf( res );
	}
	protected long getAccountCategorySub( AccountBattleInfo info )
	{
		long							res					= 0;
		long							pvpBattle			= info.pvpBattles.longValue();
		long							totalBattle			= info.getTotalBattles().longValue();
		if( pvpBattle == 0 )
		{
			if( totalBattle == 0 )
			{
				res											= 0;
			}
			else
			{
				res											= -1;
			}
		}
		else
		{
			res												= 1;
			if( pvpBattle >= 10 )
			{
				res											= 10;
			}
			if( pvpBattle >= 100 )
			{
				res											= 100;
			}
			if( pvpBattle >= 200 )
			{
				res											= 200;
			}
			if( pvpBattle >= 500 )
			{
				res											= 500;
			}
			if( pvpBattle >= 1000 )
			{
				res											= 1000;
			}
			if( pvpBattle >= 2000 )
			{
				res											= 2000;
			}
			if( pvpBattle >= 3000 )
			{
				res											= 3000;
			}
			if( pvpBattle >= 5000 )
			{
				res											= 5000;
			}
			if( pvpBattle >= 10000 )
			{
				res											= 10000;
			}
		}
		return	res;
	}
	protected String getReportLine( Long key , Long value )
	{
		return	String.format( "%d\t%d" , key.longValue() , value.longValue() );
	}
	protected void outReport( File outFile , Map<Long,Long> reportMap , String repLabel ) throws IOException
	{
		List<String>					lines				= new ArrayList<String>();
		for( Map.Entry<Long,Long> entry : reportMap.entrySet() )
		{
			String						line				= repLabel + "\t" + getReportLine( entry.getKey() , entry.getValue() );
			lines.add( line );
		}
		FileIO.storeLines( outFile , lines );
		lines.clear();
	}
}

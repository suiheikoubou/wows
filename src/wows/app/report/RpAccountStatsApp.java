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
			if( args.length >= 9 )
			{
				File					inBaseFolder		= new File( args[0] );
				File					inSvrFolder			= new File( inBaseFolder  , args[2] );
				File					thisFolder			= new File( inSvrFolder   , args[3] );
				File					prevFolder			= new File( inSvrFolder   , args[4] );
				File					inThisFolder		= new File( thisFolder    , args[6] );
				File					inPrevFolder		= new File( prevFolder    , args[6] );
				File					outBaseFolder		= new File( args[1] );
				File					outSvrFolder		= new File( outBaseFolder , args[2] );
				File					outThisFolder		= new File( outSvrFolder  , args[3] );
				File					outFile				= new File( outThisFolder , args[8] );
//				File					outFile				= new File( outBaseFolder , args[2] + args[3] + args[8] );
				File					rankFile			= new File( outBaseFolder , args[5] );
				String					repLabel			= args[2] + "\t" + args[3];
				String					since				= args[7];
				boolean					isOutWR				= false;
				if( args.length >= 10 )
				{
					isOutWR									= Boolean.parseBoolean( args[9] );
				}
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpAccountStatsApp		instance			= new RpAccountStatsApp( logFile );
				instance.execute( inThisFolder , inPrevFolder , rankFile , outFile , repLabel , since , isOutWR );
			}
			else
			{
				System.out.println( "usage : java RpAccountStatsApp [in base folder] [out base folder] [server] [date this] [date prev] [rank file] [in folder] [since] [out file] (is out WR)" );
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
	public void execute( File inThisFolder , File inPrevFolder , File rankFile , File outFile , String repLabel , String since , boolean isOutWR ) throws Exception
	{
		Set<Long>						battleRanks			= loadBattleRanks( rankFile );
		outFile.getParentFile().mkdirs();
		Map<String,Report>				reportMap			= new TreeMap<String,Report>();
		File[]							inThisFiles			= inThisFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inThisFile : inThisFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inThisFiles.length ) + ":" + inThisFile.getName() );
			File						inPrevFile			= new File( inPrevFolder , inThisFile.getName() );
			processSubtract( inThisFile , inPrevFile , battleRanks , reportMap , since , isOutWR );
			ix++;
		}
		battleRanks.clear();
		outReport( outFile , reportMap , repLabel );
		reportMap.clear();
	}
	protected void processSubtract( File inThisFile , File inPrevFile , Set<Long> battleRanks , Map<String,Report> reportMap , String since , boolean isOutWR ) throws IOException
	{
		List<AccountBattleInfo>			thisModels			= Models.loadModels( inThisFile , new AccountBattleInfo() , WowsModelBase.cs );
		List<AccountBattleInfo>			prevModels			= Models.loadModels( inPrevFile , new AccountBattleInfo() , WowsModelBase.cs );
		Map<Long,AccountBattleInfo>		prevMap				= Models.toMap( prevModels );

		for( AccountBattleInfo thisModel : thisModels )
		{
			AccountBattleInfo			prevModel			= prevMap.get( Long.valueOf( thisModel.accountId )) ;
			long						battleCategory		= getBattleCategory( thisModel , prevModel , battleRanks , since );
			if( battleCategory <= 0 )
			{
				thisModel.clean();
			}
			String						accountCategory		= getAccountCategory( thisModel , battleCategory , isOutWR );
			Report						report				= reportMap.get( accountCategory );
			if( report == null )
			{
				report										= new Report();
			}
			report.add( thisModel );
			reportMap.put( accountCategory , report );
		}
		thisModels.clear();
		prevMap.clear();
	}
	protected String getAccountCategory( AccountBattleInfo info , long battleCategory , boolean isOutWR )
	{
		int								wrValue				= -1;
		if( isOutWR )
		{
			if( battleCategory > 0 )
			{
				BigDecimal				wins100				= info.pvpWins.multiply( BigDecimal.TEN , WowsModelBase.mcDown ).multiply( BigDecimal.TEN , WowsModelBase.mcDown );
				wrValue										= wins100.divide( info.pvpBattles , 0 , RoundingMode.DOWN ).intValue();
			}
		}
		return	String.format( "%06d\t%03d" , battleCategory , wrValue );
	}
	protected long getBattleCategory( AccountBattleInfo thisInfo , AccountBattleInfo prevInfo , Set<Long> battleRanks , String since )
	{
		long							battleCategory		= 0;
		if( thisInfo.hiddenProfile )
		{
			battleCategory									= -99;
		}
		else
		{
			if( prevInfo != null )
			{
				if( prevInfo.hiddenProfile )
				{
					battleCategory							= -99;
				}
				else
				{
					thisInfo.subtract( prevInfo );
					battleCategory							= getBattleCategorySub( thisInfo , battleRanks );
				}
			}
			else
			{
				if( thisInfo.createdAt.compareTo( since ) < 0 )
				{
					battleCategory							= -10;
				}
				else
				{
					battleCategory							= getBattleCategorySub( thisInfo , battleRanks );
				}
			}
		}
		return	battleCategory;
	}
	protected long getBattleCategorySub( AccountBattleInfo info , Set<Long> battleRanks )
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
			for( Long battleRank : battleRanks )
			{
				if( pvpBattle >= battleRank.longValue() )
				{
					res										= battleRank.longValue();
				}
			}
		}
		return	res;
	}
	protected void outReport( File outFile , Map<String,Report> reportMap , String repLabel ) throws IOException
	{
		List<String>					lines				= new ArrayList<String>();
		for( Map.Entry<String,Report> entry : reportMap.entrySet() )
		{
			Report						report				= entry.getValue();
			StringBuffer				buffer				= new StringBuffer();
			buffer.append( repLabel );
			buffer.append( WowsModelBase.DELIMITER );
			buffer.append( entry.getKey() );
			WowsModelBase.appendString( buffer , report.players				, true );
			WowsModelBase.appendString( buffer , report.battles				, true );
			WowsModelBase.appendString( buffer , report.wins				, true );
			WowsModelBase.appendString( buffer , report.draws				, true );
			WowsModelBase.appendString( buffer , report.losses				, true );
			WowsModelBase.appendString( buffer , report.wrs					, true );
			lines.add( buffer.toString() );
		}
		FileIO.storeLines( outFile , lines );
		lines.clear();
	}
	protected Set<Long> loadBattleRanks( File file ) throws IOException
	{
		Set<Long>						battleRanks			= new TreeSet<Long>();
		List<String>					lines				= FileIO.loadLines( file , WowsModelBase.cs );
		for( String line : lines )
		{
			battleRanks.add( Long.valueOf( line ) );
		}
		lines.clear();
		return	battleRanks;
	}
	static class Report
	{
		public BigDecimal				players;
		public BigDecimal				battles;
		public BigDecimal				wins;
		public BigDecimal				draws;
		public BigDecimal				losses;
		public BigDecimal				wrs;
		public Report()
		{
			clean();
		}
		public void clean()
		{
			players											= BigDecimal.ZERO;
			battles											= BigDecimal.ZERO;
			wins											= BigDecimal.ZERO;
			draws											= BigDecimal.ZERO;
			losses											= BigDecimal.ZERO;
			wrs												= BigDecimal.ZERO;
		}
		public void add( AccountBattleInfo info )
		{
			players											= players	.add( BigDecimal.ONE );
			if( info.pvpBattles.longValue() > 0 )
			{
				battles										= battles	.add( info.pvpBattles , WowsModelBase.mcDown );
				wins										= wins		.add( info.pvpWins , WowsModelBase.mcDown );
				draws										= draws		.add( info.pvpDraws , WowsModelBase.mcDown );
				losses										= losses	.add( info.pvpLosses , WowsModelBase.mcDown );
				wrs											= wrs		.add( info.pvpWins.divide( info.pvpBattles , 20 , RoundingMode.DOWN ) , WowsModelBase.mcDown );
			}
		}
	}
}

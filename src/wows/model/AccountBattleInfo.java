package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.model.*;

public class AccountBattleInfo implements Comparable<AccountBattleInfo>,Mappable<Long>,Model<AccountBattleInfo>
{
	public static final int				ST_NONE				= 0;
	public static final int				ST_ADDNOTE			= 10;

	public long							accountId;
	public String						accountName;
	public long							levelingTier;
	public String						createdAt;
	public String						lastBattleTime;
	public boolean						hiddenProfile;

	public BigDecimal					pvpBattles;
	public BigDecimal					pvpWins;
	public BigDecimal					pvpDraws;
	public BigDecimal					pvpLosses;
	public BigDecimal					pveBattles;
	public BigDecimal					pveWins;
	public BigDecimal					rankBattles;
	public BigDecimal					rankWins;
	public BigDecimal					clubBattles;
	public BigDecimal					clubWins;
	public BigDecimal					pvp1Battles;
	public BigDecimal					pvp1Wins;
	public BigDecimal					pvp2Battles;
	public BigDecimal					pvp2Wins;
	public BigDecimal					pvp3Battles;
	public BigDecimal					pvp3Wins;
	public BigDecimal					oper1Battles;
	public BigDecimal					oper1Wins;
	public BigDecimal					operdBattles;
	public BigDecimal					operdWins;
	public BigDecimal					operhBattles;
	public BigDecimal					operhWins;

	public String						clanTag;
	public String						mostPlayed;
	public BigDecimal					tierAvg;

	public AccountBattleInfo()
	{
		clean();
	}
	public void clean()
	{
		accountId											= 0L;
		accountName											= "";
		levelingTier										= 0L;
		createdAt											= "";
		lastBattleTime										= "";
		hiddenProfile										= false;

		pvpBattles											= BigDecimal.ZERO;
		pvpWins												= BigDecimal.ZERO;
		pvpDraws											= BigDecimal.ZERO;
		pvpLosses											= BigDecimal.ZERO;
		pveBattles											= BigDecimal.ZERO;
		pveWins												= BigDecimal.ZERO;
		rankBattles											= BigDecimal.ZERO;
		rankWins											= BigDecimal.ZERO;
		clubBattles											= BigDecimal.ZERO;
		clubWins											= BigDecimal.ZERO;
		pvp1Battles											= BigDecimal.ZERO;
		pvp1Wins											= BigDecimal.ZERO;
		pvp2Battles											= BigDecimal.ZERO;
		pvp2Wins											= BigDecimal.ZERO;
		pvp3Battles											= BigDecimal.ZERO;
		pvp3Wins											= BigDecimal.ZERO;
		oper1Battles										= BigDecimal.ZERO;
		oper1Wins											= BigDecimal.ZERO;
		operdBattles										= BigDecimal.ZERO;
		operdWins											= BigDecimal.ZERO;
		operhBattles										= BigDecimal.ZERO;
		operhWins											= BigDecimal.ZERO;

		clanTag												= "";
		mostPlayed											= "";
		tierAvg												= BigDecimal.ZERO;
	}
	public int compareTo( AccountBattleInfo perm )
	{
		int		res		= 0;
		if( res == 0 )
		{
			if( this.accountId		< perm.accountId )
			{
				res		=-1;
			}
			if( this.accountId		> perm.accountId )
			{
				res		= 1;
			}
		}
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean	res		= false;
		if( obj instanceof AccountBattleInfo )
		{
			AccountBattleInfo			perm				= (AccountBattleInfo)obj;
			if( this.compareTo( perm ) == 0 )
			{
				res		= true;
			}
		}
		return	res;
	}
	//----------------------------------------------------------------------------------------------
	public Long getKey()
	{
		return	Long.valueOf( accountId );
	}
	public AccountBattleInfo newInstance()
	{
		return	new AccountBattleInfo();
	}
	public void load( String[] cmds )
	{
		if( cmds.length == WowsModelBase.CMDS_LEN )
		{
			clean();
			accountId										= Long.parseLong( cmds[0] );
			accountName										= cmds[1];
			levelingTier									= Long.parseLong( cmds[2] );
			createdAt										= cmds[3];
			lastBattleTime									= cmds[4];
			hiddenProfile									= cmds[5].equals( "hidden" );

			pvpBattles										= new BigDecimal( cmds[ 6] );
			pvpWins											= new BigDecimal( cmds[ 7] );
			pvpDraws										= new BigDecimal( cmds[ 8] );
			pvpLosses										= new BigDecimal( cmds[ 9] );
			pveBattles										= new BigDecimal( cmds[10] );
			pveWins											= new BigDecimal( cmds[11] );
			rankBattles										= new BigDecimal( cmds[12] );
			rankWins										= new BigDecimal( cmds[13] );
			clubBattles										= new BigDecimal( cmds[14] );
			clubWins										= new BigDecimal( cmds[15] );
			pvp1Battles										= new BigDecimal( cmds[16] );
			pvp1Wins										= new BigDecimal( cmds[17] );
			pvp2Battles										= new BigDecimal( cmds[18] );
			pvp2Wins										= new BigDecimal( cmds[19] );
			pvp3Battles										= new BigDecimal( cmds[20] );
			pvp3Wins										= new BigDecimal( cmds[21] );
			oper1Battles									= new BigDecimal( cmds[22] );
			oper1Wins										= new BigDecimal( cmds[23] );
			operdBattles									= new BigDecimal( cmds[24] );
			operdWins										= new BigDecimal( cmds[25] );
			operhBattles									= new BigDecimal( cmds[26] );
			operhWins										= new BigDecimal( cmds[27] );
		}
		else
		{
			throw	new IllegalArgumentException( "undefined version:" + cmds[0] );
		}
	}
	public String getStoreText( int version )
	{
		int								cmds_cnt			= 0;
		StringBuffer					buffer				= new StringBuffer();
		switch( version )
		{
		case	ST_NONE	:
			WowsModelBase.appendString( buffer , accountId				, false );
			WowsModelBase.appendString( buffer , accountName			, true );
			WowsModelBase.appendString( buffer , levelingTier			, true );
			WowsModelBase.appendString( buffer , createdAt				, true );
			WowsModelBase.appendString( buffer , lastBattleTime			, true );
			if( hiddenProfile )
			{
				WowsModelBase.appendString( buffer , "hidden"			, true );
			}
			else
			{
				WowsModelBase.appendString( buffer , "normal"			, true );
			}
			WowsModelBase.appendString( buffer , pvpBattles				, true );
			WowsModelBase.appendString( buffer , pvpWins				, true );
			WowsModelBase.appendString( buffer , pvpDraws				, true );
			WowsModelBase.appendString( buffer , pvpLosses				, true );
			WowsModelBase.appendString( buffer , pveBattles				, true );
			WowsModelBase.appendString( buffer , pveWins				, true );
			WowsModelBase.appendString( buffer , rankBattles			, true );
			WowsModelBase.appendString( buffer , rankWins				, true );
			WowsModelBase.appendString( buffer , clubBattles			, true );
			WowsModelBase.appendString( buffer , clubWins				, true );
			WowsModelBase.appendString( buffer , pvp1Battles			, true );
			WowsModelBase.appendString( buffer , pvp1Wins				, true );
			WowsModelBase.appendString( buffer , pvp2Battles			, true );
			WowsModelBase.appendString( buffer , pvp2Wins				, true );
			WowsModelBase.appendString( buffer , pvp3Battles			, true );
			WowsModelBase.appendString( buffer , pvp3Wins				, true );
			WowsModelBase.appendString( buffer , oper1Battles			, true );
			WowsModelBase.appendString( buffer , oper1Wins				, true );
			WowsModelBase.appendString( buffer , operdBattles			, true );
			WowsModelBase.appendString( buffer , operdWins				, true );
			WowsModelBase.appendString( buffer , operhBattles			, true );
			WowsModelBase.appendString( buffer , operhWins				, true );
			cmds_cnt										= 28;
			break;
		case	ST_ADDNOTE	:
			WowsModelBase.appendString( buffer , accountId				, false );
			WowsModelBase.appendString( buffer , accountName			, true );
			WowsModelBase.appendString( buffer , levelingTier			, true );
			WowsModelBase.appendString( buffer , createdAt				, true );
			WowsModelBase.appendString( buffer , lastBattleTime			, true );
			if( hiddenProfile )
			{
				WowsModelBase.appendString( buffer , "hidden"			, true );
			}
			else
			{
				WowsModelBase.appendString( buffer , "normal"			, true );
			}
			WowsModelBase.appendString( buffer , pvpBattles				, true );
			WowsModelBase.appendString( buffer , pvpWins				, true );
			WowsModelBase.appendString( buffer , pvpDraws				, true );
			WowsModelBase.appendString( buffer , pvpLosses				, true );
			WowsModelBase.appendString( buffer , pveBattles				, true );
			WowsModelBase.appendString( buffer , pveWins				, true );
			WowsModelBase.appendString( buffer , rankBattles			, true );
			WowsModelBase.appendString( buffer , rankWins				, true );
			WowsModelBase.appendString( buffer , clubBattles			, true );
			WowsModelBase.appendString( buffer , clubWins				, true );
			WowsModelBase.appendString( buffer , pvp1Battles			, true );
			WowsModelBase.appendString( buffer , pvp1Wins				, true );
			WowsModelBase.appendString( buffer , pvp2Battles			, true );
			WowsModelBase.appendString( buffer , pvp2Wins				, true );
			WowsModelBase.appendString( buffer , pvp3Battles			, true );
			WowsModelBase.appendString( buffer , pvp3Wins				, true );
			WowsModelBase.appendString( buffer , oper1Battles			, true );
			WowsModelBase.appendString( buffer , oper1Wins				, true );
			WowsModelBase.appendString( buffer , operdBattles			, true );
			WowsModelBase.appendString( buffer , operdWins				, true );
			WowsModelBase.appendString( buffer , operhBattles			, true );
			WowsModelBase.appendString( buffer , operhWins				, true );

			WowsModelBase.appendString( buffer , clanTag				, true );
			WowsModelBase.appendString( buffer , mostPlayed				, true );
			WowsModelBase.appendString( buffer , tierAvg				, true );
			cmds_cnt										= 31;
			break;
		}

		for( int ix = cmds_cnt ; ix < WowsModelBase.CMDS_LEN ; ix++ )
		{
			buffer.append( WowsModelBase.DELIMITER );
		}
		buffer.append( WowsModelBase.LAST_LETTER );
		return	buffer.toString();
	}
	public String toString()
	{
		return	getStoreText( ST_NONE );
	}
	public String getStorageKey( int keyType )
	{
		String							storageKey			= String.valueOf( accountId );
		switch( keyType )
		{
		case	WowsModelBase.KT_AID100:
			storageKey										= String.valueOf( ( accountId %  100 ) +  100 ).substring( 1 );
			break;
		case	WowsModelBase.KT_AID1000:
			storageKey										= String.valueOf( ( accountId % 1000 ) + 1000 ).substring( 1 );
			break;
		}
		return	storageKey;
	}
	//----------------------------------------------------------------------------------------------
	public void add( AccountBattleInfo perm )
	{
		pvpBattles											=	pvpBattles			.add(		perm.	pvpBattles			, WowsModelBase.mcDown );
		pvpWins												=	pvpWins				.add(		perm.	pvpWins				, WowsModelBase.mcDown );
		pvpDraws											=	pvpDraws			.add(		perm.	pvpDraws			, WowsModelBase.mcDown );
		pvpLosses											=	pvpLosses			.add(		perm.	pvpLosses			, WowsModelBase.mcDown );
		pveBattles											=	pveBattles			.add(		perm.	pveBattles			, WowsModelBase.mcDown );
		pveWins												=	pveWins				.add(		perm.	pveWins				, WowsModelBase.mcDown );
		rankBattles											=	rankBattles			.add(		perm.	rankBattles			, WowsModelBase.mcDown );
		rankWins											=	rankWins			.add(		perm.	rankWins			, WowsModelBase.mcDown );
		clubBattles											=	clubBattles			.add(		perm.	clubBattles			, WowsModelBase.mcDown );
		clubWins											=	clubWins			.add(		perm.	clubWins			, WowsModelBase.mcDown );
		pvp1Battles											=	pvp1Battles			.add(		perm.	pvp1Battles			, WowsModelBase.mcDown );
		pvp1Wins											=	pvp1Wins			.add(		perm.	pvp1Wins			, WowsModelBase.mcDown );
		pvp2Battles											=	pvp2Battles			.add(		perm.	pvp2Battles			, WowsModelBase.mcDown );
		pvp2Wins											=	pvp2Wins			.add(		perm.	pvp2Wins			, WowsModelBase.mcDown );
		pvp3Battles											=	pvp3Battles			.add(		perm.	pvp3Battles			, WowsModelBase.mcDown );
		pvp3Wins											=	pvp3Wins			.add(		perm.	pvp3Wins			, WowsModelBase.mcDown );
		oper1Battles										=	oper1Battles		.add(		perm.	oper1Battles		, WowsModelBase.mcDown );
		oper1Wins											=	oper1Wins			.add(		perm.	oper1Wins			, WowsModelBase.mcDown );
		operdBattles										=	operdBattles		.add(		perm.	operdBattles		, WowsModelBase.mcDown );
		operdWins											=	operdWins			.add(		perm.	operdWins			, WowsModelBase.mcDown );
		operhBattles										=	operhBattles		.add(		perm.	operhBattles		, WowsModelBase.mcDown );
		operhWins											=	operhWins			.add(		perm.	operhWins			, WowsModelBase.mcDown );
	}
	public void subtract( AccountBattleInfo perm )
	{
		pvpBattles											=	pvpBattles			.subtract(	perm.	pvpBattles			, WowsModelBase.mcDown );
		pvpWins												=	pvpWins				.subtract(	perm.	pvpWins				, WowsModelBase.mcDown );
		pvpDraws											=	pvpDraws			.subtract(	perm.	pvpDraws			, WowsModelBase.mcDown );
		pvpLosses											=	pvpLosses			.subtract(	perm.	pvpLosses			, WowsModelBase.mcDown );
		pveBattles											=	pveBattles			.subtract(	perm.	pveBattles			, WowsModelBase.mcDown );
		pveWins												=	pveWins				.subtract(	perm.	pveWins				, WowsModelBase.mcDown );
		rankBattles											=	rankBattles			.subtract(	perm.	rankBattles			, WowsModelBase.mcDown );
		rankWins											=	rankWins			.subtract(	perm.	rankWins			, WowsModelBase.mcDown );
		clubBattles											=	clubBattles			.subtract(	perm.	clubBattles			, WowsModelBase.mcDown );
		clubWins											=	clubWins			.subtract(	perm.	clubWins			, WowsModelBase.mcDown );
		pvp1Battles											=	pvp1Battles			.subtract(	perm.	pvp1Battles			, WowsModelBase.mcDown );
		pvp1Wins											=	pvp1Wins			.subtract(	perm.	pvp1Wins			, WowsModelBase.mcDown );
		pvp2Battles											=	pvp2Battles			.subtract(	perm.	pvp2Battles			, WowsModelBase.mcDown );
		pvp2Wins											=	pvp2Wins			.subtract(	perm.	pvp2Wins			, WowsModelBase.mcDown );
		pvp3Battles											=	pvp3Battles			.subtract(	perm.	pvp3Battles			, WowsModelBase.mcDown );
		pvp3Wins											=	pvp3Wins			.subtract(	perm.	pvp3Wins			, WowsModelBase.mcDown );
		oper1Battles										=	oper1Battles		.subtract(	perm.	oper1Battles		, WowsModelBase.mcDown );
		oper1Wins											=	oper1Wins			.subtract(	perm.	oper1Wins			, WowsModelBase.mcDown );
		operdBattles										=	operdBattles		.subtract(	perm.	operdBattles		, WowsModelBase.mcDown );
		operdWins											=	operdWins			.subtract(	perm.	operdWins			, WowsModelBase.mcDown );
		operhBattles										=	operhBattles		.subtract(	perm.	operhBattles		, WowsModelBase.mcDown );
		operhWins											=	operhWins			.subtract(	perm.	operhWins			, WowsModelBase.mcDown );
	}
	public BigDecimal getTotalBattles()
	{
		return	pvpBattles.add( pveBattles ).add( rankBattles ).add( clubBattles ).add( oper1Battles ).add( operdBattles ).add( operhBattles ) ;
	}
	public void addNote( AccountClanInfo clanInfo , AccountSummaryInfo summaryInfo )
	{
		if( clanInfo != null )
		{
			clanTag											= clanInfo.clanTag;
		}
		if( summaryInfo != null )
		{
			mostPlayed										= summaryInfo.getMostPlayed();
			tierAvg											= summaryInfo.getTierAvg();
		}
	}
	public long getAccountRank()
	{
		long							accountRank			= 50;
		if( pvpBattles.longValue() >= 10 )
		{
			BigDecimal					wins100				= pvpWins.multiply( BigDecimal.TEN , WowsModelBase.mcDown ).multiply( BigDecimal.TEN , WowsModelBase.mcDown );
			long						wrValue				= wins100.divide( pvpBattles , 0 , RoundingMode.DOWN ).longValue();
			accountRank										= 30;
			if( wrValue >= 35 )
			{
				accountRank									= 40;
			}
			if( wrValue >= 45 )
			{
				accountRank									= 50;
			}
			if( wrValue >= 55 )
			{
				accountRank									= 60;
			}
			if( wrValue >= 65 )
			{
				accountRank									= 70;
			}
		}
		return	accountRank;
	}
}

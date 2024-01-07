package com.suiheikoubou.wows.app.migrate;

import java.io.*;
import java.util.*;
import java.math.*;

public class XAccountBattleInfo implements Comparable<XAccountBattleInfo>
{
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

	public XAccountBattleInfo()
	{
		accountId											= 0L;
		accountName											= "";
		levelingTier										= 0L;
		createdAt											= "";
		lastBattleTime										= "";
		hiddenProfile										= false;

		pvpBattles											= new BigDecimal( 0 );
		pvpWins												= new BigDecimal( 0 );
		pvpDraws											= new BigDecimal( 0 );
		pvpLosses											= new BigDecimal( 0 );
		pveBattles											= new BigDecimal( 0 );
		pveWins												= new BigDecimal( 0 );
		rankBattles											= new BigDecimal( 0 );
		rankWins											= new BigDecimal( 0 );
		clubBattles											= new BigDecimal( 0 );
		clubWins											= new BigDecimal( 0 );
		pvp1Battles											= new BigDecimal( 0 );
		pvp1Wins											= new BigDecimal( 0 );
		pvp2Battles											= new BigDecimal( 0 );
		pvp2Wins											= new BigDecimal( 0 );
		pvp3Battles											= new BigDecimal( 0 );
		pvp3Wins											= new BigDecimal( 0 );
		oper1Battles										= new BigDecimal( 0 );
		oper1Wins											= new BigDecimal( 0 );
		operdBattles										= new BigDecimal( 0 );
		operdWins											= new BigDecimal( 0 );
		operhBattles										= new BigDecimal( 0 );
		operhWins											= new BigDecimal( 0 );
	}

	public int compareTo( XAccountBattleInfo perm )
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
		if( obj instanceof XAccountBattleInfo )
		{
			XAccountBattleInfo			perm				= (XAccountBattleInfo)obj;
			if( this.compareTo( perm ) == 0 )
			{
				res		= true;
			}
		}
		return	res;
	}
	public String toString()
	{
		StringBuffer					buffer				= new StringBuffer();
		buffer.append( String.valueOf( accountId ) );
		buffer.append( "\t" );
		buffer.append( accountName );
		buffer.append( "\t" );
		buffer.append( String.valueOf( levelingTier ) );
		buffer.append( "\t" );
		buffer.append( createdAt );
		buffer.append( "\t" );
		buffer.append( lastBattleTime );
		buffer.append( "\t" );
		if( hiddenProfile )
		{
			buffer.append( String.valueOf( "hidden" ) );
		}
		else
		{
			buffer.append( String.valueOf( "normal" ) );
		}
		buffer.append( "\t" );
		buffer.append( pvpBattles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvpWins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvpDraws.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvpLosses.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pveBattles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pveWins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( rankBattles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( rankWins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( clubBattles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( clubWins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvp1Battles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvp1Wins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvp2Battles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvp2Wins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvp3Battles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( pvp3Wins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( oper1Battles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( oper1Wins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( operdBattles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( operdWins.toPlainString() );
		buffer.append( "\t" );
		buffer.append( operhBattles.toPlainString() );
		buffer.append( "\t" );
		buffer.append( operhWins.toPlainString() );
		return	buffer.toString();
	}

	public static XAccountBattleInfo parseInfo( String line ) 
	{
		XAccountBattleInfo				info				= null;
		String[]						cmds				= line.split("\t");
		if( cmds.length == 28 )
		{
			info											= new XAccountBattleInfo();
			info.accountId									= Long.parseLong( cmds[0] );
			info.accountName								= cmds[1];
			info.levelingTier								= Long.parseLong( cmds[2] );
			info.createdAt									= cmds[3];
			info.lastBattleTime								= cmds[4];
			info.hiddenProfile								= cmds[5].equals( "hidden" );

			info.pvpBattles									= new BigDecimal( cmds[ 6] );
			info.pvpWins									= new BigDecimal( cmds[ 7] );
			info.pvpDraws									= new BigDecimal( cmds[ 8] );
			info.pvpLosses									= new BigDecimal( cmds[ 9] );
			info.pveBattles									= new BigDecimal( cmds[10] );
			info.pveWins									= new BigDecimal( cmds[11] );
			info.rankBattles								= new BigDecimal( cmds[12] );
			info.rankWins									= new BigDecimal( cmds[13] );
			info.clubBattles								= new BigDecimal( cmds[14] );
			info.clubWins									= new BigDecimal( cmds[15] );
			info.pvp1Battles								= new BigDecimal( cmds[16] );
			info.pvp1Wins									= new BigDecimal( cmds[17] );
			info.pvp2Battles								= new BigDecimal( cmds[18] );
			info.pvp2Wins									= new BigDecimal( cmds[19] );
			info.pvp3Battles								= new BigDecimal( cmds[20] );
			info.pvp3Wins									= new BigDecimal( cmds[21] );
			info.oper1Battles								= new BigDecimal( cmds[22] );
			info.oper1Wins									= new BigDecimal( cmds[23] );
			info.operdBattles								= new BigDecimal( cmds[24] );
			info.operdWins									= new BigDecimal( cmds[25] );
			info.operhBattles								= new BigDecimal( cmds[26] );
			info.operhWins									= new BigDecimal( cmds[27] );
		}
		else
		{
			throw	new IllegalArgumentException( "データ不正 : " + line );
		}
		return	info;
	}
	public static Map<Long,XAccountBattleInfo> loadXAccountBattleInfos( File file ) throws IOException
	{
		Map<Long,XAccountBattleInfo>	infos				= new TreeMap<Long,XAccountBattleInfo>();
		BufferedReader					reader				= new BufferedReader( new FileReader( file ) );
		String							line				= "";
		while( ( line = reader.readLine() ) != null )
		{
			XAccountBattleInfo						info		= parseInfo( line );
			infos.put( new Long( info.accountId ) , info );
		}
		reader.close();
		return	infos;
	}
	public static void storeXAccountBattleInfos( File file , Map<Long,XAccountBattleInfo> infos ) throws IOException
	{
		PrintWriter						writer				= new PrintWriter( new BufferedWriter( new FileWriter( file ) ) );
		for( XAccountBattleInfo info : infos.values() )
		{
			writer.println( info.toString() );
		}
		writer.close();
	}
}

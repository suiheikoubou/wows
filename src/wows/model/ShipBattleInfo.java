package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.model.*;

public class ShipBattleInfo implements Comparable<ShipBattleInfo>,Mappable<ShipBattleInfoKey>,Model<ShipBattleInfo>
{
	public static final int				ST_NONE				= 0;
	public static final int				ST_STATS			= 10;
	public static final int				ST_ADDNOTE			= 20;

	public ShipBattleInfoKey			key;
	public BigDecimal					valueBattles;
	public BigDecimal					valueWins;
	public BigDecimal					valueDraws;
	public BigDecimal					valueLosses;
	public BigDecimal					valueSurvivedBattles;
	public BigDecimal					valueDamageDealt;
	public BigDecimal					valueFrags;
	public BigDecimal					valuePlanesKilled;
	public BigDecimal					valueCapturePoints;
	public BigDecimal					valueDroppedCapturePoints;
	public BigDecimal					valueXp;
	public BigDecimal					valueArtAgro;
	public BigDecimal					valueTorpedoAgro;
	public BigDecimal					valueDamageScouting;
	public BigDecimal					valueShipsSpotted;
	public BigDecimal					valueTeamCapturePoints;
	public BigDecimal					valueTeamDroppedPoints;
	public BigDecimal					valueBattlesSince512;
	public BigDecimal					valueMainShots;
	public BigDecimal					valueMainHits;
	public BigDecimal					valueDistance;

	public String						server;
	public String						processDate;

	public String						shipName;
	public String						shipType;
	public String						nation;
	public int							tier;
	public boolean						active;
	public BigDecimal					players;

	public String						clanTag;
	public String						accountName;

	public ShipBattleInfo()
	{
		this( new ShipBattleInfoKey() );
	}
	public ShipBattleInfo( ShipBattleInfoKey p_key )
	{
		key													= p_key;
		clean();
	}
	public void clean()
	{
//		key.accountId										= 0L;
//		key.shipId											= 0L;
		valueBattles										= BigDecimal.ZERO;
		valueWins											= BigDecimal.ZERO;
		valueDraws											= BigDecimal.ZERO;
		valueLosses											= BigDecimal.ZERO;
		valueSurvivedBattles								= BigDecimal.ZERO;
		valueDamageDealt									= BigDecimal.ZERO;
		valueFrags											= BigDecimal.ZERO;
		valuePlanesKilled									= BigDecimal.ZERO;
		valueCapturePoints									= BigDecimal.ZERO;
		valueDroppedCapturePoints							= BigDecimal.ZERO;
		valueXp												= BigDecimal.ZERO;
		valueArtAgro										= BigDecimal.ZERO;
		valueTorpedoAgro									= BigDecimal.ZERO;
		valueDamageScouting									= BigDecimal.ZERO;
		valueShipsSpotted									= BigDecimal.ZERO;
		valueTeamCapturePoints								= BigDecimal.ZERO;
		valueTeamDroppedPoints								= BigDecimal.ZERO;
		valueBattlesSince512								= BigDecimal.ZERO;
		valueMainShots										= BigDecimal.ZERO;
		valueMainHits										= BigDecimal.ZERO;
		valueDistance										= BigDecimal.ZERO;

		server												= "";
		processDate											= "";

		shipName											= "";
		shipType											= "";
		nation												= "";
		tier												= 0;
		active												= false;
		players												= BigDecimal.ZERO;

		clanTag												= "";
		accountName											= "";
	}

	public int compareTo( ShipBattleInfo perm )
	{
		int								res					= 0;
		if( res == 0 )
		{
			res												= this.key.compareTo( perm.key );
		}
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean							res					= false;
		if( obj instanceof ShipBattleInfo )
		{
			ShipBattleInfo				perm				= (ShipBattleInfo)obj;
			res												= this.key.equals( perm.key );
		}
		return	res;
	}
	//----------------------------------------------------------------------------------------------
	public ShipBattleInfoKey getKey()
	{
		return	key;
	}
	public ShipBattleInfo newInstance()
	{
		return	new ShipBattleInfo();
	}
	public void load( String[] cmds )
	{
		if( cmds.length == WowsModelBase.CMDS_LEN )
		{
			clean();
			key.accountId									= Long.parseLong( cmds[ 0] );
			key.shipId										= Long.parseLong( cmds[ 1] );

			valueBattles									= new BigDecimal( cmds[ 2] );
			valueWins										= new BigDecimal( cmds[ 3] );
			valueDraws										= new BigDecimal( cmds[ 4] );
			valueLosses										= new BigDecimal( cmds[ 5] );
			valueSurvivedBattles							= new BigDecimal( cmds[ 6] );
			valueDamageDealt								= new BigDecimal( cmds[ 7] );
			valueFrags										= new BigDecimal( cmds[ 8] );
			valuePlanesKilled								= new BigDecimal( cmds[ 9] );
			valueCapturePoints								= new BigDecimal( cmds[10] );
			valueDroppedCapturePoints						= new BigDecimal( cmds[11] );
			valueXp											= new BigDecimal( cmds[12] );
			valueArtAgro									= new BigDecimal( cmds[13] );
			valueTorpedoAgro								= new BigDecimal( cmds[14] );
			valueDamageScouting								= new BigDecimal( cmds[15] );
			valueShipsSpotted								= new BigDecimal( cmds[16] );
			valueTeamCapturePoints							= new BigDecimal( cmds[17] );
			valueTeamDroppedPoints							= new BigDecimal( cmds[18] );
			valueBattlesSince512							= new BigDecimal( cmds[19] );
			valueMainShots									= new BigDecimal( cmds[20] );
			valueMainHits									= new BigDecimal( cmds[21] );
			valueDistance									= new BigDecimal( cmds[22] );
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
			WowsModelBase.appendString( buffer , key.accountId				, false );
			WowsModelBase.appendString( buffer , key.shipId					, true );
			WowsModelBase.appendString( buffer , valueBattles				, true );
			WowsModelBase.appendString( buffer , valueWins					, true );
			WowsModelBase.appendString( buffer , valueDraws					, true );
			WowsModelBase.appendString( buffer , valueLosses				, true );
			WowsModelBase.appendString( buffer , valueSurvivedBattles		, true );
			WowsModelBase.appendString( buffer , valueDamageDealt			, true );
			WowsModelBase.appendString( buffer , valueFrags					, true );
			WowsModelBase.appendString( buffer , valuePlanesKilled			, true );
			WowsModelBase.appendString( buffer , valueCapturePoints			, true );
			WowsModelBase.appendString( buffer , valueDroppedCapturePoints	, true );
			WowsModelBase.appendString( buffer , valueXp					, true );
			WowsModelBase.appendString( buffer , valueArtAgro				, true );
			WowsModelBase.appendString( buffer , valueTorpedoAgro			, true );
			WowsModelBase.appendString( buffer , valueDamageScouting		, true );
			WowsModelBase.appendString( buffer , valueShipsSpotted			, true );
			WowsModelBase.appendString( buffer , valueTeamCapturePoints		, true );
			WowsModelBase.appendString( buffer , valueTeamDroppedPoints		, true );
			WowsModelBase.appendString( buffer , valueBattlesSince512		, true );
			WowsModelBase.appendString( buffer , valueMainShots				, true );
			WowsModelBase.appendString( buffer , valueMainHits				, true );
			WowsModelBase.appendString( buffer , valueDistance				, true );
			cmds_cnt										= 23;
			break;
		case	ST_STATS	:
			WowsModelBase.appendString( buffer , key.accountId				, false );
			WowsModelBase.appendString( buffer , key.shipId					, true );
			WowsModelBase.appendString( buffer , valueBattles				, true );
			WowsModelBase.appendString( buffer , valueWins					, true );
			WowsModelBase.appendString( buffer , valueDraws					, true );
			WowsModelBase.appendString( buffer , valueLosses				, true );
			WowsModelBase.appendString( buffer , valueSurvivedBattles		, true );
			WowsModelBase.appendString( buffer , valueDamageDealt			, true );
			WowsModelBase.appendString( buffer , valueFrags					, true );
			WowsModelBase.appendString( buffer , valuePlanesKilled			, true );
			WowsModelBase.appendString( buffer , valueCapturePoints			, true );
			WowsModelBase.appendString( buffer , valueDroppedCapturePoints	, true );
			WowsModelBase.appendString( buffer , valueXp					, true );
			WowsModelBase.appendString( buffer , valueArtAgro				, true );
			WowsModelBase.appendString( buffer , valueTorpedoAgro			, true );
			WowsModelBase.appendString( buffer , valueDamageScouting		, true );
			WowsModelBase.appendString( buffer , valueShipsSpotted			, true );
			WowsModelBase.appendString( buffer , valueTeamCapturePoints		, true );
			WowsModelBase.appendString( buffer , valueTeamDroppedPoints		, true );
			WowsModelBase.appendString( buffer , valueBattlesSince512		, true );
			WowsModelBase.appendString( buffer , valueMainShots				, true );
			WowsModelBase.appendString( buffer , valueMainHits				, true );
			WowsModelBase.appendString( buffer , valueDistance				, true );
			WowsModelBase.appendString( buffer , server						, true );
			WowsModelBase.appendString( buffer , processDate				, true );
			WowsModelBase.appendString( buffer , shipName					, true );
			WowsModelBase.appendString( buffer , shipType					, true );
			WowsModelBase.appendString( buffer , nation						, true );
			WowsModelBase.appendString( buffer , tier						, true );
			WowsModelBase.appendString( buffer , active						, true );
			WowsModelBase.appendString( buffer , players					, true );
			cmds_cnt										= 31;
			break;
		case	ST_ADDNOTE	:
			WowsModelBase.appendString( buffer , key.accountId				, false );
			WowsModelBase.appendString( buffer , key.shipId					, true );
			WowsModelBase.appendString( buffer , valueBattles				, true );
			WowsModelBase.appendString( buffer , valueWins					, true );
			WowsModelBase.appendString( buffer , valueDraws					, true );
			WowsModelBase.appendString( buffer , valueLosses				, true );
			WowsModelBase.appendString( buffer , valueSurvivedBattles		, true );
			WowsModelBase.appendString( buffer , valueDamageDealt			, true );
			WowsModelBase.appendString( buffer , valueFrags					, true );
			WowsModelBase.appendString( buffer , valuePlanesKilled			, true );
			WowsModelBase.appendString( buffer , valueCapturePoints			, true );
			WowsModelBase.appendString( buffer , valueDroppedCapturePoints	, true );
			WowsModelBase.appendString( buffer , valueXp					, true );
			WowsModelBase.appendString( buffer , valueArtAgro				, true );
			WowsModelBase.appendString( buffer , valueTorpedoAgro			, true );
			WowsModelBase.appendString( buffer , valueDamageScouting		, true );
			WowsModelBase.appendString( buffer , valueShipsSpotted			, true );
			WowsModelBase.appendString( buffer , valueTeamCapturePoints		, true );
			WowsModelBase.appendString( buffer , valueTeamDroppedPoints		, true );
			WowsModelBase.appendString( buffer , valueBattlesSince512		, true );
			WowsModelBase.appendString( buffer , valueMainShots				, true );
			WowsModelBase.appendString( buffer , valueMainHits				, true );
			WowsModelBase.appendString( buffer , valueDistance				, true );
			WowsModelBase.appendString( buffer , server						, true );
			WowsModelBase.appendString( buffer , processDate				, true );
			WowsModelBase.appendString( buffer , shipName					, true );
			WowsModelBase.appendString( buffer , shipType					, true );
			WowsModelBase.appendString( buffer , nation						, true );
			WowsModelBase.appendString( buffer , tier						, true );
			WowsModelBase.appendString( buffer , active						, true );
			WowsModelBase.appendString( buffer , players					, true );
			WowsModelBase.appendString( buffer , clanTag					, true );
			WowsModelBase.appendString( buffer , accountName				, true );
			cmds_cnt										= 33;
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
		String							storageKey			= key.toString();
		switch( keyType )
		{
		case	WowsModelBase.KT_AID100:
			storageKey										= String.valueOf( ( key.accountId %  100 ) +  100 ).substring( 1 );
			break;
		case	WowsModelBase.KT_AID1000:
			storageKey										= String.valueOf( ( key.accountId % 1000 ) + 1000 ).substring( 1 );
			break;
		case	WowsModelBase.KT_SHIPID:
			storageKey										= String.valueOf( key.shipId );
			break;
		}
		return	storageKey;
	}
	//----------------------------------------------------------------------------------------------
	public BigDecimal getKDR()
	{
		BigDecimal						kdr					= new BigDecimal( "999.99" );
		BigDecimal						killR				= valueFrags;
		BigDecimal						deathR				= BigDecimal.ONE.subtract( valueSurvivedBattles );
		if( deathR.signum() > 0 )
		{
			kdr												= killR.divide( deathR , 2 , RoundingMode.DOWN );
		}
		return	kdr;
	}
	public BigDecimal getHitRatio()
	{
		BigDecimal						hitRatio			= new BigDecimal( "0.00" );
		if( valueMainShots.signum() > 0 )
		{
			hitRatio										= valueMainHits.divide( valueMainShots , 4 , RoundingMode.DOWN );
		}
		return	hitRatio;
	}
	public BigDecimal getWinrate()
	{
		BigDecimal						winrate				= new BigDecimal( "0.00" );
		if( valueBattles.signum() > 0 )
		{
			winrate											= valueWins.divide( valueBattles , 6 , RoundingMode.DOWN );
		}
		return	winrate;
	}
	public void add( ShipBattleInfo perm )
	{
		valueBattles										=	valueBattles				.add(		perm.	valueBattles				, WowsModelBase.mcDown );
		valueWins											=	valueWins					.add(		perm.	valueWins					, WowsModelBase.mcDown );
		valueDraws											=	valueDraws					.add(		perm.	valueDraws					, WowsModelBase.mcDown );
		valueLosses											=	valueLosses					.add(		perm.	valueLosses					, WowsModelBase.mcDown );
		valueSurvivedBattles								=	valueSurvivedBattles		.add(		perm.	valueSurvivedBattles		, WowsModelBase.mcDown );
		valueDamageDealt									=	valueDamageDealt			.add(		perm.	valueDamageDealt			, WowsModelBase.mcDown );
		valueFrags											=	valueFrags					.add(		perm.	valueFrags					, WowsModelBase.mcDown );
		valuePlanesKilled									=	valuePlanesKilled			.add(		perm.	valuePlanesKilled			, WowsModelBase.mcDown );
		valueCapturePoints									=	valueCapturePoints			.add(		perm.	valueCapturePoints			, WowsModelBase.mcDown );
		valueDroppedCapturePoints							=	valueDroppedCapturePoints	.add(		perm.	valueDroppedCapturePoints	, WowsModelBase.mcDown );
		valueXp												=	valueXp						.add(		perm.	valueXp						, WowsModelBase.mcDown );
		valueArtAgro										=	valueArtAgro				.add(		perm.	valueArtAgro				, WowsModelBase.mcDown );
		valueTorpedoAgro									=	valueTorpedoAgro			.add(		perm.	valueTorpedoAgro			, WowsModelBase.mcDown );
		valueDamageScouting									=	valueDamageScouting			.add(		perm.	valueDamageScouting			, WowsModelBase.mcDown );
		valueShipsSpotted									=	valueShipsSpotted			.add(		perm.	valueShipsSpotted			, WowsModelBase.mcDown );
		valueTeamCapturePoints								=	valueTeamCapturePoints		.add(		perm.	valueTeamCapturePoints		, WowsModelBase.mcDown );
		valueTeamDroppedPoints								=	valueTeamDroppedPoints		.add(		perm.	valueTeamDroppedPoints		, WowsModelBase.mcDown );
		valueBattlesSince512								=	valueBattlesSince512		.add(		perm.	valueBattlesSince512		, WowsModelBase.mcDown );
		valueMainShots										=	valueMainShots				.add(		perm.	valueMainShots				, WowsModelBase.mcDown );
		valueMainHits										=	valueMainHits				.add(		perm.	valueMainHits				, WowsModelBase.mcDown );
		valueDistance										=	valueDistance				.add(		perm.	valueDistance				, WowsModelBase.mcDown );
		players												=	players						.add(		perm.	players						, WowsModelBase.mcDown );
	}
	public void subtract( ShipBattleInfo perm )
	{
		valueBattles										=	valueBattles				.subtract(	perm.	valueBattles				, WowsModelBase.mcDown );
		valueWins											=	valueWins					.subtract(	perm.	valueWins					, WowsModelBase.mcDown );
		valueDraws											=	valueDraws					.subtract(	perm.	valueDraws					, WowsModelBase.mcDown );
		valueLosses											=	valueLosses					.subtract(	perm.	valueLosses					, WowsModelBase.mcDown );
		valueSurvivedBattles								=	valueSurvivedBattles		.subtract(	perm.	valueSurvivedBattles		, WowsModelBase.mcDown );
		valueDamageDealt									=	valueDamageDealt			.subtract(	perm.	valueDamageDealt			, WowsModelBase.mcDown );
		valueFrags											=	valueFrags					.subtract(	perm.	valueFrags					, WowsModelBase.mcDown );
		valuePlanesKilled									=	valuePlanesKilled			.subtract(	perm.	valuePlanesKilled			, WowsModelBase.mcDown );
		valueCapturePoints									=	valueCapturePoints			.subtract(	perm.	valueCapturePoints			, WowsModelBase.mcDown );
		valueDroppedCapturePoints							=	valueDroppedCapturePoints	.subtract(	perm.	valueDroppedCapturePoints	, WowsModelBase.mcDown );
		valueXp												=	valueXp						.subtract(	perm.	valueXp						, WowsModelBase.mcDown );
		valueArtAgro										=	valueArtAgro				.subtract(	perm.	valueArtAgro				, WowsModelBase.mcDown );
		valueTorpedoAgro									=	valueTorpedoAgro			.subtract(	perm.	valueTorpedoAgro			, WowsModelBase.mcDown );
		valueDamageScouting									=	valueDamageScouting			.subtract(	perm.	valueDamageScouting			, WowsModelBase.mcDown );
		valueShipsSpotted									=	valueShipsSpotted			.subtract(	perm.	valueShipsSpotted			, WowsModelBase.mcDown );
		valueTeamCapturePoints								=	valueTeamCapturePoints		.subtract(	perm.	valueTeamCapturePoints		, WowsModelBase.mcDown );
		valueTeamDroppedPoints								=	valueTeamDroppedPoints		.subtract(	perm.	valueTeamDroppedPoints		, WowsModelBase.mcDown );
		valueBattlesSince512								=	valueBattlesSince512		.subtract(	perm.	valueBattlesSince512		, WowsModelBase.mcDown );
		valueMainShots										=	valueMainShots				.subtract(	perm.	valueMainShots				, WowsModelBase.mcDown );
		valueMainHits										=	valueMainHits				.subtract(	perm.	valueMainHits				, WowsModelBase.mcDown );
		valueDistance										=	valueDistance				.subtract(	perm.	valueDistance				, WowsModelBase.mcDown );
		players												=	players						.subtract(	perm.	players						, WowsModelBase.mcDown );
	}
	public void setReportKey( String p_server , String p_processDate )
	{
		server												= p_server;
		processDate											= p_processDate;
	}
	public void setShipInfo( ShipInfo info )
	{
		shipName											= info.shipName;
		shipType											= info.shipType;
		nation												= info.nation;
		tier												= info.tier;
		active												= info.active;
	}
	public void setAccountInfo( String p_clanTag , String p_accountName )
	{
		clanTag												= p_clanTag;
		accountName											= p_accountName;
	}
}

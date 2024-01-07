package com.suiheikoubou.wows.app.migrate;

import java.io.*;
import java.util.*;
import java.math.*;
import java.text.*;

public class ShipResultValue implements Comparable<ShipResultValue>
{
	public ShipResultKey		key;
	public BigDecimal			valueBattles;
	public BigDecimal			valueWins;
	public BigDecimal			valueSurvivedBattles;
	public BigDecimal			valueDamageDealt;
	public BigDecimal			valueFrags;
	public BigDecimal			valuePlanesKilled;
	public BigDecimal			valueCapturePoints;
	public BigDecimal			valueDroppedCapturePoints;
	public BigDecimal			valueXp;
	public BigDecimal			valueDraws;
	public BigDecimal			valueLosses;
	public String				accountName;
	public String				shipName;
	public String				shipNation;
	public String				shipClass;
	public int					shipTier;
	public boolean				shipPremium;
	public BigDecimal			players;
	public BigDecimal			point;
	public long					pointRank;

	public BigDecimal			valueArtAgro;
	public BigDecimal			valueTorpedoAgro;
	public BigDecimal			valueDamageScouting;
	public BigDecimal			valueShipsSpotted;
	public BigDecimal			valueTeamCapturePoints;
	public BigDecimal			valueTeamDroppedPoints;
	public BigDecimal			valueBattlesSince512;

	public BigDecimal			valueMainShots;
	public BigDecimal			valueMainHits;

	public BigDecimal			valueDays;
	public BigDecimal			valueDistance;
	public Date					lastBattleDate;

	public ShipResultValue()
	{
		this( new ShipResultKey() );
	}

	public ShipResultValue( ShipResultKey p_key )
	{
		key							= p_key;
		valueBattles				= new BigDecimal( 0 );
		valueWins					= new BigDecimal( 0 );
		valueSurvivedBattles		= new BigDecimal( 0 );
		valueDamageDealt			= new BigDecimal( 0 );
		valueFrags					= new BigDecimal( 0 );
		valuePlanesKilled			= new BigDecimal( 0 );
		valueCapturePoints			= new BigDecimal( 0 );
		valueDroppedCapturePoints	= new BigDecimal( 0 );
		valueXp						= new BigDecimal( 0 );
		valueDraws					= new BigDecimal( 0 );
		valueLosses					= new BigDecimal( 0 );
		accountName					= "";
		shipName					= "";
		shipNation					= "";
		shipClass					= "";
		shipTier					= 0;
		shipPremium					= false;
		players						= new BigDecimal( 0 );
		point						= new BigDecimal( 0 );
		pointRank					= -1;
		valueArtAgro				= new BigDecimal( 0 );
		valueTorpedoAgro			= new BigDecimal( 0 );
		valueDamageScouting			= new BigDecimal( 0 );
		valueShipsSpotted			= new BigDecimal( 0 );
		valueTeamCapturePoints		= new BigDecimal( 0 );
		valueTeamDroppedPoints		= new BigDecimal( 0 );
		valueBattlesSince512		= new BigDecimal( 0 );
		valueMainShots				= new BigDecimal( 0 );
		valueMainHits				= new BigDecimal( 0 );
		valueDays					= new BigDecimal( 0 );
		valueDistance				= new BigDecimal( 0 );
		lastBattleDate				= null;
	}

	public int compareTo( ShipResultValue perm )
	{
		int		res		= 0;
		if( res == 0 )
		{
			res			= this.key.compareTo( perm.key );
		}
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean	res		= false;
		if( obj instanceof ShipResultValue )
		{
			ShipResultValue		perm		= (ShipResultValue)obj;
			res								= this.key.equals( perm.key );
		}
		return	res;
	}
	public String toString()
	{
		return	toString( DEFAULT_VERSION , false );
	}
	public String toString( Version ver , boolean isMask )
	{
		StringBuffer			buffer		= new StringBuffer();
		switch( ver )
		{
		case	V01N	:
		case	V01		:
		case	V02N	:
		case	V02		:
			if( ! isMask )
			{
				buffer.append( String.valueOf( key.accountId ) );
			}
			buffer.append( "\t" );
			buffer.append( String.valueOf( key.shipId ) );
			buffer.append( "\t" );
			buffer.append( valueBattles.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueWins.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueDraws.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueLosses.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueSurvivedBattles.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueDamageDealt.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueFrags.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valuePlanesKilled.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueCapturePoints.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueDroppedCapturePoints.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueXp.toPlainString() );
			break;
		}

		switch( ver )
		{
		case	V02N	:
		case	V02		:
			buffer.append( "\t" );
			buffer.append( valueArtAgro.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueTorpedoAgro.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueDamageScouting.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueShipsSpotted.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueTeamCapturePoints.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueTeamDroppedPoints.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueBattlesSince512.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueMainShots.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueMainHits.toPlainString() );
			buffer.append( "\t" );
			buffer.append( "0" );
			buffer.append( "\t" );
			buffer.append( "0" );
			buffer.append( "\t" );
			buffer.append( "0" );
			buffer.append( "\t" );
			buffer.append( valueDays.toPlainString() );
			buffer.append( "\t" );
			buffer.append( valueDistance.toPlainString() );
			buffer.append( "\t" );
			if( lastBattleDate != null )
			{
				String					lastBattleStr		= _format.format( lastBattleDate );
				buffer.append( lastBattleStr );
			}
			break;
		}

		switch( ver )
		{
		case	V01		:
		case	V02		:
			buffer.append( "\t" );
			if( ! isMask )
			{
				buffer.append( accountName );
			}
			buffer.append( "\t" );
			buffer.append( shipName );
			buffer.append( "\t" );
			buffer.append( shipNation );
			buffer.append( "\t" );
			buffer.append( shipClass );
			buffer.append( "\t" );
			buffer.append( String.valueOf( shipTier ) );
			buffer.append( "\t" );
			buffer.append( String.valueOf( shipPremium ) );
			buffer.append( "\t" );
			buffer.append( players.toPlainString() );
			buffer.append( "\t" );
			buffer.append( point.toPlainString() );
			buffer.append( "\t" );
			buffer.append( String.valueOf( pointRank ) );
			break;
		}

		return	buffer.toString();
	}
	public BigDecimal getKDR()
	{
		BigDecimal				kdr			= new BigDecimal( "999.99" );
		BigDecimal				killR		= valueFrags;
		BigDecimal				deathR		= BigDecimal.ONE.subtract( valueSurvivedBattles );
		if( deathR.signum() > 0 )
		{
			kdr								= killR.divide( deathR , 2 , BigDecimal.ROUND_HALF_UP );
		}
		return					kdr;
	}
	public BigDecimal getHitRatio()
	{
		BigDecimal				hitRatio	= new BigDecimal( "0.00" );
		if( valueMainShots.signum() > 0 )
		{
			hitRatio						= valueMainHits.divide( valueMainShots , 4 , BigDecimal.ROUND_HALF_UP );
		}
		return					hitRatio;
	}
	public void add( ShipResultValue perm )
	{
		this.valueBattles					= this.valueBattles.add( perm.valueBattles );
		this.valueWins						= this.valueWins.add( perm.valueWins );
		this.valueSurvivedBattles			= this.valueSurvivedBattles.add( perm.valueSurvivedBattles );
		this.valueDamageDealt				= this.valueDamageDealt.add( perm.valueDamageDealt );
		this.valueFrags						= this.valueFrags.add( perm.valueFrags );
		this.valuePlanesKilled				= this.valuePlanesKilled.add( perm.valuePlanesKilled );
		this.valueCapturePoints				= this.valueCapturePoints.add( perm.valueCapturePoints );
		this.valueDroppedCapturePoints		= this.valueDroppedCapturePoints.add( perm.valueDroppedCapturePoints );
		this.valueXp						= this.valueXp.add( perm.valueXp );
		this.valueDraws						= this.valueDraws.add( perm.valueDraws );
		this.valueLosses					= this.valueLosses.add( perm.valueLosses );

		this.valueArtAgro					= this.valueArtAgro.add( perm.valueArtAgro );
		this.valueTorpedoAgro				= this.valueTorpedoAgro.add( perm.valueTorpedoAgro );
		this.valueDamageScouting			= this.valueDamageScouting.add( perm.valueDamageScouting );
		this.valueShipsSpotted				= this.valueShipsSpotted.add( perm.valueShipsSpotted );
		this.valueTeamCapturePoints			= this.valueTeamCapturePoints.add( perm.valueTeamCapturePoints );
		this.valueTeamDroppedPoints			= this.valueTeamDroppedPoints.add( perm.valueTeamDroppedPoints );
		this.valueBattlesSince512			= this.valueBattlesSince512.add( perm.valueBattlesSince512 );

		this.valueMainShots					= this.valueMainShots.add( perm.valueMainShots );
		this.valueMainHits					= this.valueMainHits.add( perm.valueMainHits );

		this.valueDistance					= this.valueDistance.add( perm.valueDistance );

		if( this.lastBattleDate == null )
		{
			this.lastBattleDate				= perm.lastBattleDate;
		}
		else
		{
			if( perm.lastBattleDate != null )
			{
				if( this.lastBattleDate.compareTo( perm.lastBattleDate ) < 0 )
				{
					this.lastBattleDate		= perm.lastBattleDate;
				}
			}
		}
	}
	public void subtract( ShipResultValue perm )
	{
		this.valueBattles					= this.valueBattles.subtract( perm.valueBattles );
		this.valueWins						= this.valueWins.subtract( perm.valueWins );
		this.valueSurvivedBattles			= this.valueSurvivedBattles.subtract( perm.valueSurvivedBattles );
		this.valueDamageDealt				= this.valueDamageDealt.subtract( perm.valueDamageDealt );
		this.valueFrags						= this.valueFrags.subtract( perm.valueFrags );
		this.valuePlanesKilled				= this.valuePlanesKilled.subtract( perm.valuePlanesKilled );
		this.valueCapturePoints				= this.valueCapturePoints.subtract( perm.valueCapturePoints );
		this.valueDroppedCapturePoints		= this.valueDroppedCapturePoints.subtract( perm.valueDroppedCapturePoints );
		this.valueXp						= this.valueXp.subtract( perm.valueXp );
		this.valueDraws						= this.valueDraws.subtract( perm.valueDraws );
		this.valueLosses					= this.valueLosses.subtract( perm.valueLosses );

		this.valueArtAgro					= this.valueArtAgro.subtract( perm.valueArtAgro );
		this.valueTorpedoAgro				= this.valueTorpedoAgro.subtract( perm.valueTorpedoAgro );
		this.valueDamageScouting			= this.valueDamageScouting.subtract( perm.valueDamageScouting );
		this.valueShipsSpotted				= this.valueShipsSpotted.subtract( perm.valueShipsSpotted );
		this.valueTeamCapturePoints			= this.valueTeamCapturePoints.subtract( perm.valueTeamCapturePoints );
		this.valueTeamDroppedPoints			= this.valueTeamDroppedPoints.subtract( perm.valueTeamDroppedPoints );
		this.valueBattlesSince512			= this.valueBattlesSince512.subtract( perm.valueBattlesSince512 );

		this.valueMainShots					= this.valueMainShots.subtract( perm.valueMainShots );
		this.valueMainHits					= this.valueMainHits.subtract( perm.valueMainHits );

		this.valueDistance					= this.valueDistance.subtract( perm.valueDistance );
	}
	public ShipResultValue getAvg()
	{
		ShipResultValue			avg			= new ShipResultValue();
		avg.key								= this.key;
		avg.valueBattles					= this.valueBattles;
		avg.accountName						= this.accountName;
		avg.shipName						= this.shipName;
		avg.shipNation						= this.shipNation;
		avg.shipClass						= this.shipClass;
		avg.shipTier						= this.shipTier;
		avg.shipPremium						= this.shipPremium;
		avg.players							= this.players;
		avg.point							= this.point;
		avg.pointRank						= this.pointRank;
		avg.valueBattlesSince512			= this.valueBattlesSince512;
		avg.valueDays						= this.valueDays;
		avg.lastBattleDate					= this.lastBattleDate;

		if( valueBattles.signum() > 0 )
		{
			avg.valueWins					= this.valueWins					.divide( this.valueBattles , 4 , BigDecimal.ROUND_HALF_UP );
			avg.valueSurvivedBattles		= this.valueSurvivedBattles			.divide( this.valueBattles , 4 , BigDecimal.ROUND_HALF_UP );
			avg.valueDamageDealt			= this.valueDamageDealt				.divide( this.valueBattles , 0 , BigDecimal.ROUND_HALF_UP );
			avg.valueFrags					= this.valueFrags					.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );
			avg.valuePlanesKilled			= this.valuePlanesKilled			.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );
			avg.valueCapturePoints			= this.valueCapturePoints			.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );
			avg.valueDroppedCapturePoints	= this.valueDroppedCapturePoints	.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );
			avg.valueXp						= this.valueXp						.divide( this.valueBattles , 0 , BigDecimal.ROUND_HALF_UP );
			avg.valueDraws					= this.valueDraws					.divide( this.valueBattles , 4 , BigDecimal.ROUND_HALF_UP );
			avg.valueLosses					= this.valueLosses					.divide( this.valueBattles , 4 , BigDecimal.ROUND_HALF_UP );

			avg.valueArtAgro				= this.valueArtAgro					.divide( this.valueBattles , 0 , BigDecimal.ROUND_HALF_UP );
			avg.valueTorpedoAgro			= this.valueTorpedoAgro				.divide( this.valueBattles , 0 , BigDecimal.ROUND_HALF_UP );
			avg.valueDamageScouting			= this.valueDamageScouting			.divide( this.valueBattles , 0 , BigDecimal.ROUND_HALF_UP );
			avg.valueShipsSpotted			= this.valueShipsSpotted			.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );
			avg.valueTeamCapturePoints		= this.valueTeamCapturePoints		.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );
			avg.valueTeamDroppedPoints		= this.valueTeamDroppedPoints		.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );

			avg.valueMainShots				= this.valueMainShots				.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );
			avg.valueMainHits				= this.valueMainHits				.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );

			avg.valueDistance				= this.valueDistance				.divide( this.valueBattles , 2 , BigDecimal.ROUND_HALF_UP );
		}
		return	avg;
	}

	public static ShipResultValue parseValue( String line ) 
	{
		ShipResultValue			value		= null;
		String[]				cmds		= line.split("\t");
		if( ( cmds.length == 13 ) || ( cmds.length == 22 ) )
		{
			long				accountId	= Long.parseLong( cmds[ 0] );
			long				shipId		= Long.parseLong( cmds[ 1] );
			ShipResultKey		key			= new ShipResultKey( accountId , shipId );
			value							= new ShipResultValue( key );
			value.valueBattles				= new BigDecimal( cmds[ 2] );
			value.valueWins					= new BigDecimal( cmds[ 3] );
			value.valueSurvivedBattles		= new BigDecimal( cmds[ 4] );
			value.valueDamageDealt			= new BigDecimal( cmds[ 5] );
			value.valueFrags				= new BigDecimal( cmds[ 6] );
			value.valuePlanesKilled			= new BigDecimal( cmds[ 7] );
			value.valueCapturePoints		= new BigDecimal( cmds[ 8] );
			value.valueDroppedCapturePoints	= new BigDecimal( cmds[ 9] );
			value.valueXp					= new BigDecimal( cmds[10] );
			value.valueDraws				= new BigDecimal( cmds[11] );
			value.valueLosses				= new BigDecimal( cmds[12] );
			if( cmds.length == 22 )
			{
				value.accountName			= cmds[13];
				value.shipName				= cmds[14];
				value.shipNation			= cmds[15];
				value.shipClass				= cmds[16];
				value.shipTier				= Integer.parseInt( cmds[17] );
				value.shipPremium			= Boolean.parseBoolean( cmds[18] );
				value.players				= new BigDecimal( cmds[19] );
				value.point					= new BigDecimal( cmds[20] );
				value.pointRank				= Long.parseLong( cmds[21] );
			}
		}
		else if( ( cmds.length == 28 ) || ( cmds.length == 37 ) )
		{
			long				accountId	= Long.parseLong( cmds[ 0] );
			long				shipId		= Long.parseLong( cmds[ 1] );
			ShipResultKey		key			= new ShipResultKey( accountId , shipId );
			value							= new ShipResultValue( key );
			value.valueBattles				= new BigDecimal( cmds[ 2] );
			value.valueWins					= new BigDecimal( cmds[ 3] );
			value.valueDraws				= new BigDecimal( cmds[ 4] );
			value.valueLosses				= new BigDecimal( cmds[ 5] );
			value.valueSurvivedBattles		= new BigDecimal( cmds[ 6] );
			value.valueDamageDealt			= new BigDecimal( cmds[ 7] );
			value.valueFrags				= new BigDecimal( cmds[ 8] );
			value.valuePlanesKilled			= new BigDecimal( cmds[ 9] );
			value.valueCapturePoints		= new BigDecimal( cmds[10] );
			value.valueDroppedCapturePoints	= new BigDecimal( cmds[11] );
			value.valueXp					= new BigDecimal( cmds[12] );
			
			value.valueArtAgro				= new BigDecimal( cmds[13] );
			value.valueTorpedoAgro			= new BigDecimal( cmds[14] );
			value.valueDamageScouting		= new BigDecimal( cmds[15] );
			value.valueShipsSpotted			= new BigDecimal( cmds[16] );
			value.valueTeamCapturePoints	= new BigDecimal( cmds[17] );
			value.valueTeamDroppedPoints	= new BigDecimal( cmds[18] );
			value.valueBattlesSince512		= new BigDecimal( cmds[19] );
			
			value.valueMainShots			= new BigDecimal( cmds[20] );
			value.valueMainHits				= new BigDecimal( cmds[21] );

			value.valueDays					= new BigDecimal( cmds[25] );
			value.valueDistance				= new BigDecimal( cmds[26] );
			if( cmds[27].length() == 10 )
			{
				try
				{
					value.lastBattleDate	= _format.parse( cmds[27] );
				}
				catch( ParseException ex )
				{
					throw	new IllegalArgumentException( "データ不正 : " + line );
				}
			}

			if( cmds.length == 37 )
			{
				value.accountName			= cmds[28];
				value.shipName				= cmds[29];
				value.shipNation			= cmds[30];
				value.shipClass				= cmds[31];
				value.shipTier				= Integer.parseInt( cmds[32] );
				value.shipPremium			= Boolean.parseBoolean( cmds[33] );
				value.players				= new BigDecimal( cmds[34] );
				value.point					= new BigDecimal( cmds[35] );
				value.pointRank				= Long.parseLong( cmds[36] );
			}
		}
		else
		{
			throw	new IllegalArgumentException( "データ不正 : " + line );
		}
		return	value;
	}
	public static Map<ShipResultKey,ShipResultValue> loadShipResults( File file ) throws IOException
	{
		Map<ShipResultKey,ShipResultValue>	results		= new TreeMap<ShipResultKey,ShipResultValue>();
		BufferedReader						reader		= new BufferedReader( new FileReader( file ) );
		String								line		= "";
		while( ( line = reader.readLine() ) != null )
		{
			ShipResultValue					value		= parseValue( line );
			results.put( value.key , value );
		}
		reader.close();
		return	results;
	}
	public static void storeShipResults( File file , Map<ShipResultKey,ShipResultValue> results ) throws IOException
	{
		storeShipResults( file , results , DEFAULT_VERSION , false );
	}
	public static void storeShipResults( File file , Map<ShipResultKey,ShipResultValue> results , Version ver , boolean isMask ) throws IOException
	{
		PrintWriter							writer		= new PrintWriter( new BufferedWriter( new FileWriter( file ) ) );
		for( ShipResultValue value : results.values() )
		{
			writer.println( value.toString( ver , isMask ) );
		}
		writer.close();
	}
	protected static DateFormat		_format;
	static
	{
		_format						= new SimpleDateFormat( "yyyy/MM/dd" );
	}
	public static enum Version
	{
		V01N
	,	V01
	,	V02N
	,	V02
	}
	public static final Version			DEFAULT_VERSION		= Version.V02;
}

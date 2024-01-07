package com.suiheikoubou.wows.app.report;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class RpShipFreqsApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 10 )
			{
				File					mstFolder			= new File( args[0] );
				File					mstFile				= new File( mstFolder       , args[6] );
				File					acntBaseFolder		= new File( args[1] );
				File					acntSvrFolder		= new File( acntBaseFolder  , args[4] );
				File					acntThisFolder		= new File( acntSvrFolder   , args[5] );
				File					acntFolder			= new File( acntThisFolder  , args[7] );
				File					shipBaseFolder		= new File( args[2] );
				File					shipSvrFolder		= new File( shipBaseFolder  , args[4] );
				File					shipThisFolder		= new File( shipSvrFolder   , args[5] );
				File					shipFolder			= new File( shipThisFolder  , args[8] );
				File					outBaseFolder		= new File( args[3] );
				File					outSvrFolder		= new File( outBaseFolder   , args[4] );
				File					outThisFolder		= new File( outSvrFolder    , args[5] );
				File					outFile				= new File( outThisFolder   , args[9] );
				String					server				= args[4];
				String					processDate			= args[5];
				int						tierThreshold		= 0;
				if( args.length >= 11 )
				{
					tierThreshold							= Integer.parseInt( args[10] );
				}
				File					logFile				= new File( outBaseFolder , "error.log" );
				RpShipFreqsApp			instance			= new RpShipFreqsApp( logFile );
				instance.execute( mstFile , acntFolder , shipFolder , outFile , server , processDate , tierThreshold );
			}
			else
			{
				System.out.println( "usage : java RpShipFreqsApp [mst folder] [acnt base folder] [ship base folder] [out base folder] [server] [date this] [mst file] [acnt folder] [ship folder] [out file] (tier threshold)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public RpShipFreqsApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File mstFile , File acntFolder , File shipFolder , File outFile , String server , String processDate , int tierThreshold ) throws Exception
	{
		List<ShipInfo>					ships				= Models.loadModels( mstFile , new ShipInfo() , WowsModelBase.cs );
		Map<Long,ShipInfo>				shipMap				= Models.toMap( ships );
		Map<FreqKey,FreqInfo>			modelMap			= new TreeMap<FreqKey,FreqInfo>();
		outFile.getParentFile().mkdirs();

		File[]							acntFiles			= acntFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File acntFile : acntFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , acntFiles.length ) + ":" + acntFile.getName() );
			File						shipFile			= new File( shipFolder , acntFile.getName() );
			processShipStats( acntFile , shipFile , modelMap , shipMap , server , processDate , tierThreshold );
			ix++;
//			if(ix > 10)break;
		}
		shipMap.clear();

		outLog( outFile.getPath() );
		Models.storeModels( outFile , false , modelMap.values() , FreqInfo.ST_NONE , WowsModelBase.cs );
		modelMap.clear();
	}
	protected void processShipStats( File acntFile , File shipFile , Map<FreqKey,FreqInfo> modelMap , Map<Long,ShipInfo> shipMap , String server , String processDate , int tierThreshold ) throws IOException
	{
		List<ShipBattleInfo>			shipBattles			= Models.loadModels( shipFile , new ShipBattleInfo() , WowsModelBase.cs );
		List<AccountBattleInfo>			accountBattles		= Models.loadModels( acntFile , new AccountBattleInfo() , WowsModelBase.cs );
		Map<Long,AccountBattleInfo>		accountMap			= Models.toMap( accountBattles );

		Collection<ShipBattleInfo>		mergedBattles		= getMergedBattleInfo( shipBattles , shipMap , tierThreshold );
		shipBattles.clear();

		for( ShipBattleInfo mergedBattle : mergedBattles )
		{
			long						accountRank			= 50;
			AccountBattleInfo			account				= accountMap.get( Long.valueOf( mergedBattle.key.accountId ) );
			if( account != null )
			{
				accountRank									= account.getAccountRank();
			}
			for( ValueCoeff coeff : valueCoeffs )
			{
				FreqKey					freqKey				= getFreqKey( mergedBattle.key.shipId , accountRank , coeff , mergedBattle );
				FreqInfo				freqInfo			= modelMap.get( freqKey );
				if( freqInfo == null )
				{
					freqInfo								= new FreqInfo( freqKey );
					freqInfo.value							= freqKey.valueNorm.multiply( coeff.valueNorm , WowsModelBase.mcDown );
					freqInfo.setReportKey( server , processDate );
				}
				freqInfo.players							+=1;
				freqInfo.battles							+=mergedBattle.valueBattles.longValue();
				modelMap.put( freqKey , freqInfo );
			}
		}
		mergedBattles.clear();

		accountMap.clear();
	}
	protected Collection<ShipBattleInfo> getMergedBattleInfo( List<ShipBattleInfo> shipBattles , Map<Long,ShipInfo> shipMap , int tierThreshold )
	{
		Map<ShipBattleInfoKey,ShipBattleInfo>	mergedMap	= new TreeMap<ShipBattleInfoKey,ShipBattleInfo>();
		for( ShipBattleInfo shipBattle : shipBattles )
		{
			ShipInfo					ship				= shipMap.get( Long.valueOf( shipBattle.key.shipId ) );
			if( ship.tier >= tierThreshold )
			{
				ShipBattleInfoKey		mergedKey			= new ShipBattleInfoKey( shipBattle.key.accountId , ship.getShipTypeNum() );
				ShipBattleInfo			mergedInfo			= mergedMap.get( mergedKey );
				if( mergedInfo == null )
				{
					mergedInfo								= new ShipBattleInfo( mergedKey );
				}
				mergedInfo.add( shipBattle );
				mergedMap.put( mergedKey , mergedInfo );
			}
		}
		return	mergedMap.values();
	}
	protected static FreqKey getFreqKey( long shipTypeNum , long accountRank , ValueCoeff coeff , ShipBattleInfo battleInfo )
	{
		FreqKey							freqKey				= new FreqKey();
		BigDecimal						battleValue			= getBattleValue( coeff.valueName , battleInfo );
		BigDecimal						nvalue				= getNormValue( battleValue , coeff.valueNorm );
		freqKey.shipType									= ShipInfo.getShipType( shipTypeNum );
		freqKey.tier										= ShipInfo.getTier( shipTypeNum );
		freqKey.accountRank									= accountRank;
		freqKey.valueName									= coeff.valueName;
		freqKey.valueNorm									= nvalue;
		return	freqKey;
	}
	protected static BigDecimal getNormValue( BigDecimal value , BigDecimal norm )
	{
		BigDecimal						nvalue				= BigDecimal.ZERO;
		if( norm.signum() > 0 )
		{
			nvalue											= value.divide( norm , 0 , RoundingMode.DOWN );
		}
		return	nvalue;
	}

	static class ValueCoeff
	{
		public String					valueName;
		public BigDecimal				valueNorm;
		public ValueCoeff()
		{
			this( "" , BigDecimal.ONE );
		}
		public ValueCoeff( String p_valueName , BigDecimal p_valueNorm )
		{
			valueName										= p_valueName;
			valueNorm										= p_valueNorm;
		}
	}
	protected static List<ValueCoeff>	valueCoeffs;
	static
	{
		valueCoeffs											= new ArrayList<ValueCoeff>();
		valueCoeffs.add( new ValueCoeff( "battle"     , BigDecimal.valueOf( 10 ) ) );
		valueCoeffs.add( new ValueCoeff( "winrate"    , BigDecimal.valueOf( 0.02 ) ) );
		valueCoeffs.add( new ValueCoeff( "damage"     , BigDecimal.valueOf( 2000 ) ) );
		valueCoeffs.add( new ValueCoeff( "survive"    , BigDecimal.valueOf( 0.02 ) ) );
		valueCoeffs.add( new ValueCoeff( "flag"       , BigDecimal.valueOf( 0.05 ) ) );
		valueCoeffs.add( new ValueCoeff( "capture"    , BigDecimal.valueOf( 1 ) ) );
		valueCoeffs.add( new ValueCoeff( "drcapture"  , BigDecimal.valueOf( 1 ) ) );
		valueCoeffs.add( new ValueCoeff( "artagro"    , BigDecimal.valueOf( 20000 ) ) );
		valueCoeffs.add( new ValueCoeff( "scoutdmg"   , BigDecimal.valueOf( 2000 ) ) );
		valueCoeffs.add( new ValueCoeff( "dist"       , BigDecimal.valueOf( 5 ) ) );
		valueCoeffs.add( new ValueCoeff( "shot"       , BigDecimal.valueOf( 5 ) ) );
		valueCoeffs.add( new ValueCoeff( "hitratio"   , BigDecimal.valueOf( 0.02 ) ) );
	}
	protected static BigDecimal getBattleValue( String valueName , ShipBattleInfo battleInfo )
	{
		BigDecimal						value				= BigDecimal.ZERO;
		if( battleInfo.valueBattles.signum() > 0 )
		{
			if( valueName.equals( "battle" ) )
			{
				value										= battleInfo.valueBattles;
			}
			if( valueName.equals( "winrate" ) )
			{
				value										= battleInfo.getWinrate();
			}
			if( valueName.equals( "damage" ) )
			{
				value										= battleInfo.valueDamageDealt.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "survive" ) )
			{
				value										= battleInfo.valueSurvivedBattles.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "flag" ) )
			{
				value										= battleInfo.valueFrags.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "capture" ) )
			{
				value										= battleInfo.valueCapturePoints.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "drcapture" ) )
			{
				value										= battleInfo.valueDroppedCapturePoints.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "artagro" ) )
			{
				value										= battleInfo.valueArtAgro.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "scoutdmg" ) )
			{
				value										= battleInfo.valueDamageScouting.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "dist" ) )
			{
				value										= battleInfo.valueDistance.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "shot" ) )
			{
				value										= battleInfo.valueMainShots.divide( battleInfo.valueBattles , 10 , RoundingMode.DOWN );
			}
			if( valueName.equals( "hitratio" ) )
			{
				value										= battleInfo.getHitRatio();
			}
			if( value.signum() < 0 )
			{
				value										= BigDecimal.ZERO;
			}
		}
		return	value;
	}
	static class FreqKey implements Comparable<FreqKey>
	{
		public String					shipType;
		public int						tier;
		public long						accountRank;
		public String					valueName;
		public BigDecimal				valueNorm;
		public FreqKey()
		{
			shipType										= "";
			tier											= 0;
			accountRank										= 0;
			valueName										= "";
			valueNorm										= BigDecimal.ZERO;
		}
		public int compareTo( FreqKey perm )
		{
			int							res					= 0;
			if( res == 0 )
			{
				res											= this.shipType.compareTo( perm.shipType );
			}
			if( res == 0 )
			{
				if( this.tier			< perm.tier )
				{
					res										=-1;
				}
				if( this.tier			> perm.tier )
				{
					res										= 1;
				}
			}
			if( res == 0 )
			{
				if( this.accountRank	< perm.accountRank )
				{
					res										=-1;
				}
				if( this.accountRank	> perm.accountRank )
				{
					res										= 1;
				}
			}
			if( res == 0 )
			{
				res											= this.valueName.compareTo( perm.valueName );
			}
			if( res == 0 )
			{
				res											= this.valueNorm.compareTo( perm.valueNorm );
			}
			return	res;
		}
		public boolean equals(Object obj)
		{
			boolean						res					= false;
			if( obj instanceof FreqKey )
			{
				FreqKey					perm				= (FreqKey)obj;
				if( this.compareTo( perm ) == 0 )
				{
					res										= true;
				}
			}
			return	res;
		}
		public String toString()
		{
			StringBuffer				buffer				= new StringBuffer();
			WowsModelBase.appendString( buffer , shipType				, false );
			WowsModelBase.appendString( buffer , tier					, true );
			WowsModelBase.appendString( buffer , accountRank			, true );
			WowsModelBase.appendString( buffer , valueName				, true );
			WowsModelBase.appendString( buffer , valueNorm				, true );
			return	buffer.toString();
		}
	}
	static class FreqInfo implements Comparable<FreqInfo>,Mappable<FreqKey>,Model<FreqInfo>
	{
		public static final int			ST_NONE				= 0;

		public FreqKey					key;
		public BigDecimal				value;
		public long						players;
		public long						battles;
		public String					server;
		public String					processDate;

		public FreqInfo()
		{
			this( new FreqKey() );
		}
		public FreqInfo( FreqKey p_key )
		{
			key												= p_key;
			clean();
		}
		public void clean()
		{
			value											= BigDecimal.ZERO;
			players											= 0;
			battles											= 0;
			server											= "";
			processDate										= "";
		}
		public int compareTo( FreqInfo perm )
		{
			int							res					= 0;
			if( res == 0 )
			{
				res											= this.key.compareTo( perm.key );
			}
			return	res;
		}
		public boolean equals(Object obj)
		{
			boolean						res					= false;
			if( obj instanceof FreqInfo )
			{
				FreqInfo				perm				= (FreqInfo)obj;
				res											= this.key.equals( perm.key );
			}
			return	res;
		}
		public FreqKey getKey()
		{
			return	key;
		}
		public FreqInfo newInstance()
		{
			return	new FreqInfo();
		}
		public void load( String[] cmds )
		{
			throw	new IllegalArgumentException( "undefined version" );
		}
		public String getStoreText( int version )
		{
			int							cmds_cnt			= 0;
			StringBuffer				buffer				= new StringBuffer();
			switch( version )
			{
			case	ST_NONE	:
				WowsModelBase.appendString( buffer , key.shipType				, false );
				WowsModelBase.appendString( buffer , key.tier					, true );
				WowsModelBase.appendString( buffer , key.accountRank			, true );
				WowsModelBase.appendString( buffer , key.valueName				, true );
				WowsModelBase.appendString( buffer , key.valueNorm				, true );
				WowsModelBase.appendString( buffer , value						, true );
				WowsModelBase.appendString( buffer , players					, true );
				WowsModelBase.appendString( buffer , battles					, true );
				WowsModelBase.appendString( buffer , server						, true );
				WowsModelBase.appendString( buffer , processDate				, true );
				cmds_cnt										= 10;
				break;
			}
/*
			for( int ix = cmds_cnt ; ix < WowsModelBase.CMDS_LEN ; ix++ )
			{
				buffer.append( WowsModelBase.DELIMITER );
			}
			buffer.append( WowsModelBase.LAST_LETTER );
*/
			return	buffer.toString();
		}
		public String toString()
		{
			return	getStoreText( ST_NONE );
		}
		public String getStorageKey( int keyType )
		{
			String						storageKey			= key.toString();
			return	storageKey;
		}
		public void setReportKey( String p_server , String p_processDate )
		{
			server											= p_server;
			processDate										= p_processDate;
		}
	}
	
}

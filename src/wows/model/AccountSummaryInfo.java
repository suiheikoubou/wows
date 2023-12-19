package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.model.*;

public class AccountSummaryInfo implements Comparable<AccountSummaryInfo>,Mappable<Long>,Model<AccountSummaryInfo>
{
	public static final int				ST_NONE				= 0;
	public static final int				CMDS_LEN			= 8;

	public long							accountId;
	public BigDecimal					valueTiers;
	public BigDecimal					valueBattles;
	public BigDecimal					valueBattlesBB;
	public BigDecimal					valueBattlesCA;
	public BigDecimal					valueBattlesDD;
	public BigDecimal					valueBattlesCV;
	public BigDecimal					valueBattlesSS;

	public AccountSummaryInfo()
	{
		clean();
	}
	public void clean()
	{
		accountId											= 0L;
		valueTiers											= BigDecimal.ZERO;
		valueBattles										= BigDecimal.ZERO;
		valueBattlesBB										= BigDecimal.ZERO;
		valueBattlesCA										= BigDecimal.ZERO;
		valueBattlesDD										= BigDecimal.ZERO;
		valueBattlesCV										= BigDecimal.ZERO;
		valueBattlesSS										= BigDecimal.ZERO;
	}

	public int compareTo( AccountSummaryInfo perm )
	{
		int								res					= 0;
		if( res == 0 )
		{
			if( this.accountId			< perm.accountId )
			{
				res											=-1;
			}
			if( this.accountId			> perm.accountId )
			{
				res											= 1;
			}
		}
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean							res					= false;
		if( obj instanceof AccountSummaryInfo )
		{
			AccountSummaryInfo			perm				= (AccountSummaryInfo)obj;
			if( this.compareTo( perm ) == 0 )
			{
				res											= true;
			}
		}
		return	res;
	}
	//----------------------------------------------------------------------------------------------
	public Long getKey()
	{
		return	Long.valueOf( accountId );
	}
	public AccountSummaryInfo newInstance()
	{
		return	new AccountSummaryInfo();
	}
	public void load( String[] cmds )
	{
		if( ( cmds.length == CMDS_LEN ) || ( cmds.length == WowsModelBase.CMDS_LEN ) )
		{
			clean();
			accountId										= Long.parseLong( cmds[ 0] );
			valueTiers										= new BigDecimal( cmds[ 1] );
			valueBattles									= new BigDecimal( cmds[ 2] );
			valueBattlesBB									= new BigDecimal( cmds[ 3] );
			valueBattlesCA									= new BigDecimal( cmds[ 4] );
			valueBattlesDD									= new BigDecimal( cmds[ 5] );
			valueBattlesCV									= new BigDecimal( cmds[ 6] );
			valueBattlesSS									= new BigDecimal( cmds[ 7] );
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
			WowsModelBase.appendString( buffer , accountId			, false );
			WowsModelBase.appendString( buffer , valueTiers			, true );
			WowsModelBase.appendString( buffer , valueBattles		, true );
			WowsModelBase.appendString( buffer , valueBattlesBB		, true );
			WowsModelBase.appendString( buffer , valueBattlesCA		, true );
			WowsModelBase.appendString( buffer , valueBattlesDD		, true );
			WowsModelBase.appendString( buffer , valueBattlesCV		, true );
			WowsModelBase.appendString( buffer , valueBattlesSS		, true );
			cmds_cnt										= 8;
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
	public void add( ShipBattleInfo battle , ShipInfo ship )
	{
		BigDecimal						tierBattles			= battle.valueBattles.multiply( BigDecimal.valueOf( ship.tier ) , WowsModelBase.mcDown );
		valueTiers											= valueTiers.add( tierBattles , WowsModelBase.mcDown );
		valueBattles										= valueBattles.add( battle.valueBattles , WowsModelBase.mcDown );
		if( ship.isTypeBB() )
		{
			valueBattlesBB									= valueBattlesBB.add( battle.valueBattles , WowsModelBase.mcDown );
		}
		if( ship.isTypeCA() )
		{
			valueBattlesCA									= valueBattlesCA.add( battle.valueBattles , WowsModelBase.mcDown );
		}
		if( ship.isTypeDD() )
		{
			valueBattlesDD									= valueBattlesDD.add( battle.valueBattles , WowsModelBase.mcDown );
		}
		if( ship.isTypeCV() )
		{
			valueBattlesCV									= valueBattlesCV.add( battle.valueBattles , WowsModelBase.mcDown );
		}
		if( ship.isTypeSS() )
		{
			valueBattlesSS									= valueBattlesSS.add( battle.valueBattles , WowsModelBase.mcDown );
		}
	}
	public String getMostPlayed()
	{
		String							mostPlayed			= "  ";
		long							mostValue			= 0;
		if( valueBattlesBB.longValue() > mostValue )
		{
			mostPlayed										= "BB";
			mostValue										= valueBattlesBB.longValue();
		}
		if( valueBattlesCA.longValue() > mostValue )
		{
			mostPlayed										= "CA";
			mostValue										= valueBattlesCA.longValue();
		}
		if( valueBattlesDD.longValue() > mostValue )
		{
			mostPlayed										= "DD";
			mostValue										= valueBattlesDD.longValue();
		}
		if( valueBattlesCV.longValue() > mostValue )
		{
			mostPlayed										= "CV";
			mostValue										= valueBattlesCV.longValue();
		}
		if( valueBattlesSS.longValue() > mostValue )
		{
			mostPlayed										= "SS";
			mostValue										= valueBattlesSS.longValue();
		}
		return	mostPlayed;
	}
	public BigDecimal getTierAvg()
	{
		BigDecimal						tierAvg				= BigDecimal.ZERO;
		if( valueBattles.signum() > 0 )
		{
			tierAvg											= valueTiers.divide( valueBattles , 5 , RoundingMode.DOWN );
		}
		return	tierAvg;
	}
}

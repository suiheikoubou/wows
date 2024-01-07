package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import com.suiheikoubou.common.model.*;

public class ShipInfo implements Comparable<ShipInfo>,Mappable<Long>,Model<ShipInfo>
{
	public static final int				ST_NONE				= 0;
	public static final int				CMDS_LEN			= 10;

	public static final String			TYPE_BB				= "Battleship";
	public static final String			TYPE_CA				= "Cruiser";
	public static final String			TYPE_DD				= "Destroyer";
	public static final String			TYPE_CV				= "AirCarrier";
	public static final String			TYPE_SS				= "Submarine";

	public long							shipId;
	public String						shipName;
	public String						shipType;
	public String						nation;
	public int							tier;
	public boolean						premium;
	public boolean						active;
	public int							priceCredit;
	public int							priceGold;
	public String						shipIdStr;

	public ShipInfo()
	{
		clean();
	}
	public void clean()
	{
		shipId												= 0L;
		shipName											= "";
		shipType											= "";
		nation												= "";
		tier												= 0;
		premium												= false;
		active												= false;
		priceCredit											= 0;
		priceGold											= 0;
		shipIdStr											= "";
	}

	public int compareTo( ShipInfo perm )
	{
		int								res					= 0;
		if( res == 0 )
		{
			if( this.shipId				< perm.shipId )
			{
				res											=-1;
			}
			if( this.shipId				> perm.shipId )
			{
				res											= 1;
			}
		}
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean							res					= false;
		if( obj instanceof ShipInfo )
		{
			ShipInfo					perm				= (ShipInfo)obj;
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
		return	Long.valueOf( shipId );
	}
	public ShipInfo newInstance()
	{
		return	new ShipInfo();
	}
	public void load( String[] cmds )
	{
		if( ( cmds.length == CMDS_LEN ) || ( cmds.length == WowsModelBase.CMDS_LEN ) )
		{
			clean();
			shipId											= Long.parseLong( cmds[ 0] );
			shipName										= cmds[ 1];
			shipType										= cmds[ 2];
			nation											= cmds[ 3];
			tier											= Integer.parseInt( cmds[ 4] );
			premium											= Boolean.parseBoolean( cmds[5] );
			active											= Boolean.parseBoolean( cmds[6] );
			priceCredit										= Integer.parseInt( cmds[ 7] );
			priceGold										= Integer.parseInt( cmds[ 8] );
			shipIdStr										= cmds[ 9];
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
			WowsModelBase.appendString( buffer , shipId				, false );
			WowsModelBase.appendString( buffer , shipName			, true );
			WowsModelBase.appendString( buffer , shipType			, true );
			WowsModelBase.appendString( buffer , nation				, true );
			WowsModelBase.appendString( buffer , tier				, true );
			WowsModelBase.appendString( buffer , premium			, true );
			WowsModelBase.appendString( buffer , active				, true );
			WowsModelBase.appendString( buffer , priceCredit		, true );
			WowsModelBase.appendString( buffer , priceGold			, true );
			WowsModelBase.appendString( buffer , shipIdStr			, true );
			cmds_cnt										= 10;
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
		String							storageKey			= String.valueOf( shipId );
		return	storageKey;
	}
	//----------------------------------------------------------------------------------------------
	public boolean isPremium()
	{
		boolean							res					= premium;
		if( priceGold > 0 )
		{
			res												= true;
		}
		return	res;
	}
	public boolean isTypeBB()
	{
		return	shipType.equals( TYPE_BB );
	}
	public boolean isTypeCA()
	{
		return	shipType.equals( TYPE_CA );
	}
	public boolean isTypeDD()
	{
		return	shipType.equals( TYPE_DD );
	}
	public boolean isTypeCV()
	{
		return	shipType.equals( TYPE_CV );
	}
	public boolean isTypeSS()
	{
		return	shipType.equals( TYPE_SS );
	}
	public long getShipTypeNum()
	{
		long							shipTypeNum			= 0;
		if( isTypeBB() )
		{
			shipTypeNum										= 100000 + tier*100;
		}
		if( isTypeCA() )
		{
			shipTypeNum										= 200000 + tier*100;
		}
		if( isTypeDD() )
		{
			shipTypeNum										= 300000 + tier*100;
		}
		if( isTypeCV() )
		{
			shipTypeNum										= 400000 + tier*100;
		}
		if( isTypeSS() )
		{
			shipTypeNum										= 500000 + tier*100;
		}
		return	shipTypeNum;
	}
	public static String getShipType( long shipTypeNum )
	{
		String							shipType			= "";
		switch( (int)(( shipTypeNum / 10000 ) % 100) )
		{
		case	10:
			shipType										= TYPE_BB;
			break;
		case	20:
			shipType										= TYPE_CA;
			break;
		case	30:
			shipType										= TYPE_DD;
			break;
		case	40:
			shipType										= TYPE_CV;
			break;
		case	50:
			shipType										= TYPE_SS;
			break;
		}
		return	shipType;
	}
	public static int getTier( long shipTypeNum )
	{
		return	(int)(( shipTypeNum / 100 ) % 100);
	}
}

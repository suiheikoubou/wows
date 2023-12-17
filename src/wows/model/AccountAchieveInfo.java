package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.model.*;

public class AccountAchieveInfo implements Comparable<AccountAchieveInfo>,Mappable<AccountAchieveInfoKey>,Model<AccountAchieveInfo>
{
	public static final int				ST_NONE				= 0;

	public AccountAchieveInfoKey		key;
	public Long							achieveValue;

	public AccountAchieveInfo()
	{
		this( new AccountAchieveInfoKey() );
	}
	public AccountAchieveInfo( AccountAchieveInfoKey p_key )
	{
		key													= p_key;
		clean();
	}
	public void clean()
	{
		key.accountId										= 0L;
		key.achieveId										= "";
		achieveValue										= 0L;
	}

	public int compareTo( AccountAchieveInfo perm )
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
		if( obj instanceof AccountAchieveInfo )
		{
			AccountAchieveInfo				perm				= (AccountAchieveInfo)obj;
			res												= this.key.equals( perm.key );
		}
		return	res;
	}
	//----------------------------------------------------------------------------------------------
	public AccountAchieveInfoKey getKey()
	{
		return	key;
	}
	public AccountAchieveInfo newInstance()
	{
		return	new AccountAchieveInfo();
	}
	public void load( String[] cmds )
	{
		if( ( cmds.length == 3 ) || ( cmds.length == WowsModelBase.CMDS_LEN ) )
		{
			clean();
			key.accountId									= Long.parseLong( cmds[ 0] );
			key.achieveId									= cmds[ 1];
			achieveValue									= Long.parseLong( cmds[ 2] );
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
			WowsModelBase.appendString( buffer , key.achieveId				, true );
			WowsModelBase.appendString( buffer , achieveValue				, true );
			cmds_cnt										= 3;
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
		}
		return	storageKey;
	}
	//----------------------------------------------------------------------------------------------
}

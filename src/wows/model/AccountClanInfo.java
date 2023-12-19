package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import com.suiheikoubou.common.model.*;

public class AccountClanInfo implements Comparable<AccountClanInfo>,Mappable<Long>,Model<AccountClanInfo>
{
	public static final int				ST_NONE				= 0;
	public static final int				CMDS_LEN			= 3;

	public long							accountId;
	public long							clanId;
	public String						clanTag;

	public AccountClanInfo()
	{
		clean();
	}
	public void clean()
	{
		accountId											= 0L;
		clanId												= 0L;
		clanTag												= "";
	}

	public int compareTo( AccountClanInfo perm )
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
		if( obj instanceof AccountClanInfo )
		{
			AccountClanInfo				perm				= (AccountClanInfo)obj;
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
	public AccountClanInfo newInstance()
	{
		return	new AccountClanInfo();
	}
	public void load( String[] cmds )
	{
		if( ( cmds.length == CMDS_LEN ) || ( cmds.length == WowsModelBase.CMDS_LEN ) )
		{
			clean();
			accountId										= Long.parseLong( cmds[ 0] );
			clanId											= Long.parseLong( cmds[ 1] );
			clanTag											= cmds[ 2];
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
			WowsModelBase.appendString( buffer , clanId				, true );
			WowsModelBase.appendString( buffer , clanTag			, true );
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
}

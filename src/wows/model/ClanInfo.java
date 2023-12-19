package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import com.suiheikoubou.common.model.*;

public class ClanInfo implements Comparable<ClanInfo>,Mappable<Long>,Model<ClanInfo>
{
	public static final int				ST_NONE				= 0;
	public static final int				CMDS_LEN			= 3;

	public long							clanId;
	public String						clanName;
	public String						clanTag;

	public ClanInfo()
	{
		clean();
	}
	public void clean()
	{
		clanId												= 0L;
		clanName											= "";
		clanTag												= "";
	}

	public int compareTo( ClanInfo perm )
	{
		int								res					= 0;
		if( res == 0 )
		{
			if( this.clanId				< perm.clanId )
			{
				res											=-1;
			}
			if( this.clanId				> perm.clanId )
			{
				res											= 1;
			}
		}
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean							res					= false;
		if( obj instanceof ClanInfo )
		{
			ClanInfo					perm				= (ClanInfo)obj;
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
		return	Long.valueOf( clanId );
	}
	public ClanInfo newInstance()
	{
		return	new ClanInfo();
	}
	public void load( String[] cmds )
	{
		if( ( cmds.length == CMDS_LEN ) || ( cmds.length == WowsModelBase.CMDS_LEN ) )
		{
			clean();
			clanId											= Long.parseLong( cmds[ 0] );
			clanName										= cmds[ 1];
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
			WowsModelBase.appendString( buffer , clanId				, false );
			WowsModelBase.appendString( buffer , clanName			, true );
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
		String							storageKey			= String.valueOf( clanId );
		return	storageKey;
	}
}

package com.suiheikoubou.wows.model;

import java.io.*;
import java.util.*;
import com.suiheikoubou.common.model.*;

public class AchieveInfo implements Comparable<AchieveInfo>,Mappable<String>,Model<AchieveInfo>
{
	public static final int				ST_NONE				= 0;
	public static final int				CMDS_LEN			= 7;

	public String						achieveId;
	public String						achieveName;
	public String						achieveType;
	public String						achieveSubType;
	public String						description;
	public int							multiple;
	public int							threshold;

	public AchieveInfo()
	{
		clean();
	}
	public void clean()
	{
		achieveId											= "";
		achieveName											= "";
		achieveType											= "";
		achieveSubType										= "";
		description											= "";
		multiple											= 0;
		threshold											= 0;
	}

	public int compareTo( AchieveInfo perm )
	{
		return	this.achieveId.compareTo( perm.achieveId );
	}
	public boolean equals(Object obj)
	{
		boolean							res					= false;
		if( obj instanceof AchieveInfo )
		{
			AchieveInfo					perm				= (AchieveInfo)obj;
			if( this.compareTo( perm ) == 0 )
			{
				res											= true;
			}
		}
		return	res;
	}
	//----------------------------------------------------------------------------------------------
	public String getKey()
	{
		return	achieveId;
	}
	public AchieveInfo newInstance()
	{
		return	new AchieveInfo();
	}
	public void load( String[] cmds )
	{
		if( ( cmds.length == CMDS_LEN ) || ( cmds.length == WowsModelBase.CMDS_LEN ) )
		{
			clean();
			achieveId										= cmds[ 0];
			achieveName										= cmds[ 1];
			achieveType										= cmds[ 2];
			achieveSubType									= cmds[ 3];
			description										= cmds[ 4];
			multiple										= Integer.parseInt( cmds[ 5] );
			threshold										= Integer.parseInt( cmds[ 6] );
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
			WowsModelBase.appendString( buffer , achieveId			, false );
			WowsModelBase.appendString( buffer , achieveName		, true );
			WowsModelBase.appendString( buffer , achieveType		, true );
			WowsModelBase.appendString( buffer , achieveSubType		, true );
			WowsModelBase.appendString( buffer , description		, true );
			WowsModelBase.appendString( buffer , multiple			, true );
			WowsModelBase.appendString( buffer , threshold			, true );
			cmds_cnt										= CMDS_LEN;
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
		String							storageKey			= achieveId;
		return	storageKey;
	}
	//----------------------------------------------------------------------------------------------
}

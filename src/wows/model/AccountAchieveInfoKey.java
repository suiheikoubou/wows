package com.suiheikoubou.wows.model;

import com.suiheikoubou.common.model.*;

public final class AccountAchieveInfoKey implements Comparable<AccountAchieveInfoKey>
{
	public long							accountId;
	public String						achieveId;

	public AccountAchieveInfoKey()
	{
		this( 0L , "" );
	}

	public AccountAchieveInfoKey( long p_accountId , String p_achieveId )
	{
		accountId											= p_accountId;
		achieveId											= p_achieveId;
	}

	public int compareTo( AccountAchieveInfoKey perm )
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
		if( res == 0 )
		{
			res												= this.achieveId.compareTo( perm.achieveId );
		}
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean							res					= false;
		if( obj instanceof AccountAchieveInfoKey )
		{
			AccountAchieveInfoKey			perm				= (AccountAchieveInfoKey)obj;
			if( this.compareTo( perm ) == 0 )
			{
				res											= true;
			}
		}
		return	res;
	}
	public String toString()
	{
		StringBuffer					buffer				= new StringBuffer();
		buffer.append( String.valueOf( accountId ) );
		buffer.append( WowsModelBase.DELIMITER );
		buffer.append( achieveId );
		return	buffer.toString();
	}
}

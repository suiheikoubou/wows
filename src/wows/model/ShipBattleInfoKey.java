package com.suiheikoubou.wows.model;

import com.suiheikoubou.common.model.*;

public final class ShipBattleInfoKey implements Comparable<ShipBattleInfoKey>
{
	public long							accountId;
	public long							shipId;

	public ShipBattleInfoKey()
	{
		this( 0L , 0L );
	}

	public ShipBattleInfoKey( long p_accountId , long p_shipId )
	{
		accountId											= p_accountId;
		shipId												= p_shipId;
	}

	public int compareTo( ShipBattleInfoKey perm )
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
		if( obj instanceof ShipBattleInfoKey )
		{
			ShipBattleInfoKey			perm				= (ShipBattleInfoKey)obj;
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
		buffer.append( String.valueOf( shipId ) );
		return	buffer.toString();
	}
}

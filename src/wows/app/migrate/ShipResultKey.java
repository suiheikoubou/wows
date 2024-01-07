package com.suiheikoubou.wows.app.migrate;


public class ShipResultKey implements Comparable<ShipResultKey>
{
	public long				accountId;
	public long				shipId;

	public ShipResultKey()
	{
		this( 0L , 0L );
	}

	public ShipResultKey( long p_accountId , long p_shipId )
	{
		accountId			= p_accountId;
		shipId				= p_shipId;
	}

	public int compareTo( ShipResultKey perm )
	{
		int		res		= 0;
		if( res == 0 )
		{
			if( this.accountId		< perm.accountId )
			{
				res		=-1;
			}
			if( this.accountId		> perm.accountId )
			{
				res		= 1;
			}
		}
		if( res == 0 )
		{
			if( this.shipId			< perm.shipId )
			{
				res		=-1;
			}
			if( this.shipId			> perm.shipId )
			{
				res		= 1;
			}
		}
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean	res		= false;
		if( obj instanceof ShipResultKey )
		{
			ShipResultKey		perm		= (ShipResultKey)obj;
			if( this.compareTo( perm ) == 0 )
			{
				res		= true;
			}
		}
		return	res;
	}
	public String toString()
	{
		StringBuffer			buffer		= new StringBuffer();
		buffer.append( String.valueOf( accountId ) );
		buffer.append( "\t" );
		buffer.append( String.valueOf( shipId ) );
		return	buffer.toString();
	}
}

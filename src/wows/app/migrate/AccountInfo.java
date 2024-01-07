package com.suiheikoubou.wows.app.migrate;

import java.io.*;
import java.util.*;

public class AccountInfo implements Comparable<AccountInfo>
{
	public long				accountId;
	public String			accountName;
	public String			accountMask;

	public AccountInfo()
	{
		accountId					= 0L;
		accountName					= "";
		accountMask					= "";
	}

	public int compareTo( AccountInfo perm )
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
		return	res;
	}
	public boolean equals(Object obj)
	{
		boolean	res		= false;
		if( obj instanceof AccountInfo )
		{
			AccountInfo		perm		= (AccountInfo)obj;
			if( this.compareTo( perm ) == 0 )
			{
				res		= true;
			}
		}
		return	res;
	}
	public String toString()
	{
		return	toString( false );
	}
	public String toString( boolean maskEnabled )
	{
		StringBuffer			buffer		= new StringBuffer();
		buffer.append( String.valueOf( accountId ) );
		buffer.append( "\t" );
		buffer.append( accountName );
		if( maskEnabled )
		{
			buffer.append( "\t" );
			buffer.append( accountMask );
		}
		return	buffer.toString();
	}

	public static AccountInfo parseInfo( String line ) 
	{
		AccountInfo				info		= null;
		String[]				cmds		= line.split("\t");
		if( ( cmds.length == 2 ) || ( cmds.length == 3 ) )
		{
			info							= new AccountInfo();
			info.accountId					= Long.parseLong( cmds[0] );
			info.accountName				= cmds[1];
			if( cmds.length == 3 )
			{
				info.accountMask			= cmds[2];
			}
		}
		else
		{
			throw	new IllegalArgumentException( "データ不正 : " + line );
		}
		return	info;
	}
	public static Map<Long,AccountInfo> loadAccountInfos( File file ) throws IOException
	{
		Map<Long,AccountInfo>				infos		= new TreeMap<Long,AccountInfo>();
		BufferedReader						reader		= new BufferedReader( new FileReader( file ) );
		String								line		= "";
		while( ( line = reader.readLine() ) != null )
		{
			AccountInfo						info		= parseInfo( line );
			infos.put( new Long( info.accountId ) , info );
		}
		reader.close();
		return	infos;
	}
	public static void storeAccountInfos( File file , Map<Long,AccountInfo> infos ) throws IOException
	{
		storeAccountInfos( file , infos , false );
	}
	public static void storeAccountInfos( File file , Map<Long,AccountInfo> infos , boolean maskEnabled ) throws IOException
	{
		PrintWriter							writer		= new PrintWriter( new BufferedWriter( new FileWriter( file ) ) );
		for( AccountInfo info : infos.values() )
		{
			writer.println( info.toString( maskEnabled ) );
		}
		writer.close();
	}
}

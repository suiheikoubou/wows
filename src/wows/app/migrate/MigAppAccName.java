package com.suiheikoubou.wows.app.migrate;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public class MigAppAccName extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 7 )
			{
				File					inBaseFolder		= new File( args[0] );
				File					inSvrFolder			= new File( inBaseFolder , args[2] );
				File					inThisFolder		= new File( inSvrFolder  , args[3] );
				File					inFolder			= new File( inThisFolder , args[4] );
				File					outBaseFolder		= new File( args[1] );
				File					outSvrFolder		= new File( outBaseFolder , args[2] );
				File					outThisFolder		= new File( outSvrFolder  , args[3] );
				File					outFolder			= new File( outThisFolder , args[5] );
				File					hiddenFile			= new File( inThisFolder , args[6] );
				File					logFile				= new File( outThisFolder  , "error.log" );
				MigAppAccName			instance			= new MigAppAccName( logFile );
				instance.execute( inFolder , outFolder , hiddenFile );
			}
			else
			{
				System.out.println( "usage : java MigAppAccName [in base folder] [out base folder] [server] [date this] [in folder] [out folder] [hidden file]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public MigAppAccName( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder , File hiddenFile ) throws Exception
	{
		List<String>					hiddens				= FileIO.loadLines( hiddenFile );
		ModelStorage<AccountBattleInfo>	modelStorage		= new ModelStorage<AccountBattleInfo>();
		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Text() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			Map<Long,AccountInfo>		infos				= AccountInfo.loadAccountInfos( inFile );
			for( AccountInfo info : infos.values() )
			{
				AccountBattleInfo		model				= new AccountBattleInfo();
				copyInfo( model , info );
				setHidden( model , hiddens );
				modelStorage.add( model , WowsModelBase.KT_AID1000 );
			}
			modelStorage.store( outFolder , ".txt" , true , AccountBattleInfo.ST_NONE , WowsModelBase.cs );
			modelStorage.clear();
			ix++;
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
		hiddens.clear();
	}
	protected void copyInfo( AccountBattleInfo newInfo , AccountInfo oldInfo )
	{
		newInfo.accountId									= oldInfo.accountId;
		newInfo.accountName									= oldInfo.accountName;
	}
	protected void setHidden( AccountBattleInfo newInfo , List<String> hiddens )
	{
		String							key					= String.valueOf( newInfo.accountId );
		if( hiddens.contains( key ) )
		{
			newInfo.hiddenProfile							= true;
		}
	}
}

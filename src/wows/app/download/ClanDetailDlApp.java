package com.suiheikoubou.wows.app.download;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.dlworker.*;

public final class ClanDetailDlApp extends AbstractWoWsDlApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 8 )
			{
				File					baseFolder			= new File( args[0] );
				File					svrFolder			= new File( baseFolder , args[1] );
				File					thisFolder			= new File( svrFolder  , args[2] );
				File					inFile				= new File( thisFolder , args[3] );
				File					outFolder			= new File( thisFolder , args[4] );
				File					logFile				= new File( thisFolder  , "error.log" );
				URL						hostUrl				= new URL( "http" , args[5] , "/wows/clans/info/" );
				String					applicationId		= args[6];
				long					interval			= Long.parseLong( args[7] );
				ClanDetailDlApp			instance			= new ClanDetailDlApp( logFile );
				instance.execute( hostUrl , applicationId , interval , inFile , outFolder );
			}
			else
			{
				System.out.println( "usage : java ClanDetailDlApp [base folder] [server] [date this] [in file] [out folder] [host url] [app id] [interval]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public ClanDetailDlApp( File logFile )
	{
		super( logFile );
	}
	public void execute( URL hostUrl , String applicationId , long interval , File inFile , File outFolder ) throws Exception
	{
		setUrl( hostUrl );
		setApplicationId( applicationId );
		setOutFolder( outFolder );
		setSeqNo( 0 );
		outFolder.mkdirs();
		appendLines( inFile );
		
		DlWorker						worker				= DlWorker.getInstance( "DL_Clan" , this , this , 100 , interval );
		worker.startAll( 100L );
		while( worker.activeCount() > 0 )
		{
			outLog( "line count : " + String.valueOf( getLineCount() ) );
			Thread.sleep( 10000L );
		}
	}
	public synchronized DlQueue getQueue()
	{
		DlQueue							queue				= null;
		String							clan_id				= getQueueString( 100 );
		if( clan_id != null )
		{
			File						outFile				= new File( getOutFolder() , String.format( "%06d" , getSeqNo() ) + ".json" );
			String						keyString			= outFile.getPath();
			StringBuffer				buffer				= new StringBuffer();
			buffer.append( "application_id=" );
			buffer.append( getApplicationId() );
			buffer.append( "&" );
			buffer.append( "language=en" );

			buffer.append( "&" );
			buffer.append( "clan_id=" );
			buffer.append( clan_id );
//			System.out.println( buffer.toString() );
			queue											= DlQueue.getInstance( getSeqNo() , getUrl() , keyString , buffer.toString() , outFile );
			incrementSeqNo();
		}
		return	queue;
	}
	public boolean checkQueue( DlQueue queue )
	{
		boolean							res					= false;
		try
		{
			res												= getStatus( queue.getOutFile() );
		}
		catch( Exception ex )
		{
			outLog( Thread.currentThread().getName() + "\t" + queue.getOutFile().getPath() + "\t" + ex.toString() , true );
		}
		return	res;
	}
}

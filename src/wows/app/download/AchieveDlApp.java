package com.suiheikoubou.wows.app.download;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.dlworker.*;

public final class AchieveDlApp extends AbstractWoWsDlApp
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
				URL						hostUrl				= new URL( "http" , args[5] , "/wows/account/achievements/" );
				String					applicationId		= args[6];
				long					interval			= Long.parseLong( args[7] );
				AchieveDlApp			instance			= new AchieveDlApp( logFile );
				instance.execute( hostUrl , applicationId , interval , inFile , outFolder );
			}
			else
			{
				System.out.println( "usage : java AchieveDlApp [base folder] [server] [date this] [in file] [out folder] [host url] [app id] [interval]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public AchieveDlApp( File logFile )
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
		
		DlWorker						worker				= DlWorker.getInstance( "DL_Achieve" , this , this , 100 , interval );
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
		String							accountId			= getQueueString( 100 );
		if( accountId != null )
		{
			File						outFile				= new File( getOutFolder() , String.format( "%06d" , getSeqNo() ) + ".json" );
			String						keyString			= outFile.getPath();
			StringBuffer				buffer				= new StringBuffer();
			buffer.append( "application_id=" );
			buffer.append( getApplicationId() );
			buffer.append( "&" );
			buffer.append( "language=en" );

			buffer.append( "&" );
			buffer.append( "account_id=" );
			buffer.append( accountId );
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

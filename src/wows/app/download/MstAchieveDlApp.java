package com.suiheikoubou.wows.app.download;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.dlworker.*;

public final class MstAchieveDlApp extends AbstractWoWsDlApp
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
				String					language			= args[3];
				File					outFolder			= new File( thisFolder , args[4] );
				File					logFile				= new File( thisFolder  , "error.log" );
				URL						hostUrl				= new URL( "http" , args[5] , "/wows/encyclopedia/achievements/" );
				String					applicationId		= args[6];
				long					interval			= Long.parseLong( args[7] );
				MstAchieveDlApp			instance			= new MstAchieveDlApp( logFile );
				instance.execute( hostUrl , applicationId , interval , language , outFolder );
			}
			else
			{
				System.out.println( "usage : java MstAchieveDlApp [base folder] [server] [date this] [language] [out folder] [host url] [app id] [interval]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public MstAchieveDlApp( File logFile )
	{
		super( logFile );
	}
	public void execute( URL hostUrl , String applicationId , long interval , String language , File outFolder ) throws Exception
	{
		setUrl( hostUrl );
		setApplicationId( applicationId );
		setOutFolder( outFolder );
		setSeqNo( 0 );
		outFolder.mkdirs();
		appendLines( genLines( language ) );
		
		DlWorker						worker				= DlWorker.getInstance( "DL_MstAchieve" , this , this , 10 , interval );
		worker.startAll( 100L );
		while( worker.activeCount() > 0 )
		{
			outLog( "line count : " + String.valueOf( getLineCount() ) );
			Thread.sleep( 1000L );
		}
	}
	protected List<String> genLines( String language )
	{
		List<String>					lines				= new ArrayList<String>();
		lines.add( language );
		return	lines;
	}
	public synchronized DlQueue getQueue()
	{
		DlQueue							queue				= null;
		String							language			= getQueueString( 1 );
		if( language != null )
		{
			File						outFile				= new File( getOutFolder() , language + ".json" );
			String						keyString			= outFile.getPath();
			StringBuffer				buffer				= new StringBuffer();
			buffer.append( "application_id=" );
			buffer.append( getApplicationId() );
			buffer.append( "&" );
			buffer.append( "language=" + language );
//			buffer.append( "&" );
//			buffer.append( "fields=" );
//			buffer.append( "battle.achievement_id" );
//			buffer.append(",battle.name" );
//			buffer.append(",battle.type" );
//			buffer.append(",battle.sub_type" );
//			buffer.append(",battle.description" );
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

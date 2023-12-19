package com.suiheikoubou.wows.app.download;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.dlworker.*;

public final class ClanIdsDlApp extends AbstractWoWsDlApp
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
				int						dlCnt				= Integer.parseInt( args[3] );
				File					outFolder			= new File( thisFolder , args[4] );
				File					logFile				= new File( thisFolder  , "error.log" );
				URL						hostUrl				= new URL( "http" , args[5] , "/wows/clans/list/" );
				String					applicationId		= args[6];
				long					interval			= Long.parseLong( args[7] );
				ClanIdsDlApp			instance			= new ClanIdsDlApp( logFile );
				instance.execute( hostUrl , applicationId , interval , dlCnt , outFolder );
			}
			else
			{
				System.out.println( "usage : java ClanIdsDlApp [base folder] [server] [date this] [cnt] [out folder] [host url] [app id] [interval]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public ClanIdsDlApp( File logFile )
	{
		super( logFile );
	}
	public void execute( URL hostUrl , String applicationId , long interval , int dlCnt , File outFolder ) throws Exception
	{
		setUrl( hostUrl );
		setApplicationId( applicationId );
		setOutFolder( outFolder );
		setSeqNo( 0 );
		outFolder.mkdirs();
		appendLines( genLines( dlCnt ) );
		
		DlWorker						worker				= DlWorker.getInstance( "DL_ClanIds" , this , this , 100 , interval );
		worker.startAll( 100L );
		while( worker.activeCount() > 0 )
		{
			outLog( "line count : " + String.valueOf( getLineCount() ) );
			Thread.sleep( 1000L );
		}
	}
	protected List<String> genLines( long limitCnt )
	{
		List<String>					lines				= new ArrayList<String>();
		for( long ix = 1 ; ix <= limitCnt ; ix++ )
		{
			String						line				= String.format( "%d" , ix );
			lines.add( line );
		}
		return	lines;
	}
	public synchronized DlQueue getQueue()
	{
		DlQueue							queue				= null;
		String							page_no				= getQueueString( 1 );
		if( page_no != null )
		{
			File						outFile				= new File( getOutFolder() , page_no + ".json" );
			String						keyString			= outFile.getPath();
			StringBuffer				buffer				= new StringBuffer();
			buffer.append( "application_id=" );
			buffer.append( getApplicationId() );
			buffer.append( "&" );
			buffer.append( "language=en" );
			buffer.append( "&" );
			buffer.append( "page_no=" );
			buffer.append( page_no );
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

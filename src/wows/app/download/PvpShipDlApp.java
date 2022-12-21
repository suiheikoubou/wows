package com.suiheikoubou.wows.app.download;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.dlworker.*;

public final class PvpShipDlApp extends AbstractWoWsDlApp
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
				URL						hostUrl				= new URL( "http" , args[5] , "/wows/ships/stats/" );
				String					applicationId		= args[6];
				long					interval			= Long.parseLong( args[7] );
				boolean					isGetExtra			= false;
				if( args.length >= 9 )
				{
					isGetExtra								= Boolean.parseBoolean( args[8] );
				}
				PvpShipDlApp			instance			= new PvpShipDlApp( logFile );
				instance.execute( hostUrl , applicationId , interval , inFile , outFolder , isGetExtra );
			}
			else
			{
				System.out.println( "usage : java PvpShipDlApp [base folder] [server] [date this] [in file] [out folder] [host url] [app id] [interval] (isGetExtra)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	private boolean						_isGetExtra;
	protected void setIsGetExtra( boolean isGetExtra )
	{
		_isGetExtra											= isGetExtra;
	}
	protected boolean getIsGetExtra()
	{
		return	_isGetExtra;
	}
	public PvpShipDlApp( File logFile )
	{
		super( logFile );
	}
	public void execute( URL hostUrl , String applicationId , long interval , File inFile , File outFolder , boolean isGetExtra ) throws Exception
	{
		setUrl( hostUrl );
		setApplicationId( applicationId );
		setOutFolder( outFolder );
		setSeqNo( 0 );
		setIsGetExtra( isGetExtra );
		outFolder.mkdir();
		appendLines( inFile );
		
		DlWorker						worker				= DlWorker.getInstance( "DL_PvpShip" , this , this , 100 , interval );
		worker.startAll( 100L );
		while( worker.activeCount() > 0 )
		{
			outLog( "line count : " + String.valueOf( getLineCount() ) );
			Thread.sleep( 10000L );
		}
	}
	public synchronized DlQueue getQueue()
	{
		boolean							isGetExtra			= getIsGetExtra();
		DlQueue							queue				= null;
		String							accountId			= getQueueString( 1 );
		if( accountId != null )
		{
			File						outFile				= new File( getOutFolder() , accountId + ".json" );
			String						keyString			= outFile.getPath();
			StringBuffer				buffer				= new StringBuffer();
			buffer.append( "application_id=" );
			buffer.append( getApplicationId() );
			buffer.append( "&" );
			buffer.append( "language=en" );
			if( isGetExtra )
			{
				buffer.append( "&" );
				buffer.append( "extra=pvp_solo,pvp_div2,pvp_div3" );
			}
			buffer.append( "&" );
			buffer.append( "fields=" );
			buffer.append( "account_id" );
			buffer.append(",ship_id" );
			buffer.append(",last_battle_time" );
			buffer.append(",battles" );
			buffer.append(",distance" );

			appendPvpFields( buffer , "pvp" , false );
			if( isGetExtra )
			{
				appendPvpFields( buffer , "pvp_solo" , true );
				appendPvpFields( buffer , "pvp_div2" , true );
				appendPvpFields( buffer , "pvp_div3" , true );
			}

			buffer.append( "&" );
			buffer.append( "account_id=" );
			buffer.append( accountId );
//			System.out.println( buffer.toString() );
			queue											= DlQueue.getInstance( getSeqNo() , getUrl() , keyString , buffer.toString() , outFile );
			incrementSeqNo();
		}
		return	queue;
	}
	protected static void appendPvpFields( StringBuffer buffer , String category , boolean isHideSubs )
	{
		buffer.append("," + category + ".battles");
		buffer.append("," + category + ".wins");
		buffer.append("," + category + ".draws");
		buffer.append("," + category + ".losses");
		buffer.append("," + category + ".survived_battles");
		buffer.append("," + category + ".xp");
		buffer.append("," + category + ".damage_dealt");
		buffer.append("," + category + ".frags");
		buffer.append("," + category + ".planes_killed");
		buffer.append("," + category + ".capture_points");
		buffer.append("," + category + ".dropped_capture_points");
		buffer.append("," + category + ".art_agro");
		buffer.append("," + category + ".torpedo_agro");
		buffer.append("," + category + ".damage_scouting");
		buffer.append("," + category + ".ships_spotted");
		if( ! isHideSubs )
		{
			buffer.append("," + category + ".team_capture_points");
			buffer.append("," + category + ".team_dropped_capture_points");
			buffer.append("," + category + ".battles_since_512");
			buffer.append("," + category + ".main_battery.shots");
			buffer.append("," + category + ".main_battery.hits");
		}
		buffer.append("," + category + ".max_xp");
		buffer.append("," + category + ".max_damage_dealt");
		buffer.append("," + category + ".max_frags_battle");
		buffer.append("," + category + ".max_planes_killed");
		buffer.append("," + category + ".max_total_agro");
		buffer.append("," + category + ".max_damage_scouting");
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

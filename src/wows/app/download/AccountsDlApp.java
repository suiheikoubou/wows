package com.suiheikoubou.wows.app.download;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.dlworker.*;

public final class AccountsDlApp extends AbstractWoWsDlApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 11 )
			{
				File					baseFolder			= new File( args[0] );
				File					svrFolder			= new File( baseFolder , args[1] );
				File					thisFolder			= new File( svrFolder  , args[2] );
				long					offset				= Long.parseLong( args[3] );
				long					limitCnt			= Long.parseLong( args[4] );
				long					bind				= Long.parseLong( args[5] );
				long					breakCnt			= Long.parseLong( args[6] );
				File					outFolder			= new File( thisFolder , args[7] );
				File					logFile				= new File( thisFolder  , "error.log" );
				URL						hostUrl				= new URL( "http" , args[8] , "/wows/account/info/" );
				String					applicationId		= args[9];
				long					interval			= Long.parseLong( args[10] );
				AccountsDlApp			instance			= new AccountsDlApp( logFile );
				instance.execute( hostUrl , applicationId , interval , offset , limitCnt , bind , breakCnt , outFolder );
			}
			else
			{
				System.out.println( "usage : java AccountsDlApp [base folder] [server] [date this] [offset] [limitCnt] [bind] [breakCnt] [out folder] [host url] [app id] [interval]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	private long						_bind;
	protected void setBind( long bind )
	{
		_bind												= bind;
	}
	protected long getBind()
	{
		return	_bind;
	}
	private long						_breakCnt;
	protected void setBreakCnt( long breakCnt )
	{
		_breakCnt											= breakCnt;
	}
	protected long getBreakCnt()
	{
		return	_breakCnt;
	}
	private long						_noneCnt;
	protected void setNoneCnt( long noneCnt )
	{
		_noneCnt											= noneCnt;
	}
	protected long getNoneCnt()
	{
		return	_noneCnt;
	}
	protected void incrementNoneCnt()
	{
		_noneCnt++;
	}

	public AccountsDlApp( File logFile )
	{
		super( logFile );
		setBind( 100 );
		setBreakCnt( 100 );
		setNoneCnt( 0 );
	}
	protected List<String> genLines( long offset , long limitCnt )
	{
		List<String>					lines				= new ArrayList<String>();
		long							poffset				= offset / getBind();
		for( long ix = 0 ; ix < limitCnt ; ix++ )
		{
			String						line				= String.format( "%d" , poffset + ix );
			lines.add( line );
		}
		return	lines;
	}
	protected static String getIdList( String keyId , long bind )
	{
		StringBuffer					buffer				= new StringBuffer();
		for( long ix = 0 ; ix < bind ; ix++ )
		{
			if( ix > 0 )
			{
				buffer.append( "," );
			}
			buffer.append( keyId );
			buffer.append( String.format( "%02d" , ix ) );
		}
		return	buffer.toString();
	}
	public void execute( URL hostUrl , String applicationId , long interval , long offset , long limitCnt , long bind , long breakCnt , File outFolder ) throws Exception
	{
		setBind( bind );
		setBreakCnt( breakCnt );
		setNoneCnt( 0 );
		setUrl( hostUrl );
		setApplicationId( applicationId );
		setOutFolder( outFolder );
		setSeqNo( 0 );
		outFolder.mkdir();
		appendLines( genLines( offset , limitCnt ) );
		Thread.sleep( 1000L );

		DlWorker						worker				= DlWorker.getInstance( "DL_Ids" , this , this , 100 , interval );
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
		String							keyId				= getQueueString( 1 );
		if( keyId != null )
		{
			String						fileName			= String.format( "%08d" , Long.parseLong( keyId ) ) + ".json";
			File						outFile				= new File( getOutFolder() , fileName );
			String						keyString			= outFile.getPath();
			String						searchWord			= getIdList( keyId , getBind() );
			StringBuffer				buffer				= new StringBuffer();
			buffer.append( "application_id=" );
			buffer.append( getApplicationId() );
			buffer.append( "&" );
			buffer.append( "language=en" );
			buffer.append( "&" );
			buffer.append( "extra=statistics.club,statistics.pve,statistics.pvp_solo,statistics.pvp_div2,statistics.pvp_div3,statistics.rank_solo,statistics.oper_solo,statistics.oper_div,statistics.oper_div_hard" );
			buffer.append( "&" );
			buffer.append( "fields=" );
			buffer.append( "account_id" );
			buffer.append(",created_at" );
			buffer.append(",karma" );
			buffer.append(",leveling_tier" );
			buffer.append(",last_battle_time" );
			buffer.append(",nickname" );
			buffer.append(",hidden_profile" );
			buffer.append(",statistics.club.battles");
			buffer.append(",statistics.club.wins");
			buffer.append(",statistics.pvp.battles");
			buffer.append(",statistics.pvp.wins");
			buffer.append(",statistics.pvp.draws");
			buffer.append(",statistics.pvp.losses");
			buffer.append(",statistics.pve.battles");
			buffer.append(",statistics.pve.wins");
			buffer.append(",statistics.pvp_solo.battles");
			buffer.append(",statistics.pvp_solo.wins");
			buffer.append(",statistics.pvp_div2.battles");
			buffer.append(",statistics.pvp_div2.wins");
			buffer.append(",statistics.pvp_div3.battles");
			buffer.append(",statistics.pvp_div3.wins");
			buffer.append(",statistics.rank_solo.battles");
			buffer.append(",statistics.rank_solo.wins");
			buffer.append(",statistics.oper_solo.battles");
			buffer.append(",statistics.oper_solo.wins");
			buffer.append(",statistics.oper_div.battles");
			buffer.append(",statistics.oper_div.wins");
			buffer.append(",statistics.oper_div_hard.battles");
			buffer.append(",statistics.oper_div_hard.wins");
			buffer.append(",statistics.pvp.max_frags_battle");
			buffer.append(",statistics.pvp.max_frags_ship_id");
			buffer.append(",statistics.pvp.main_battery.max_frags_battle");
			buffer.append(",statistics.pvp.main_battery.max_frags_ship_id");
			buffer.append(",statistics.pvp.second_battery.max_frags_battle");
			buffer.append(",statistics.pvp.second_battery.max_frags_ship_id");
			buffer.append(",statistics.pvp.torpedoes.max_frags_battle");
			buffer.append(",statistics.pvp.torpedoes.max_frags_ship_id");
			buffer.append(",statistics.pvp.ramming.max_frags_battle");
			buffer.append(",statistics.pvp.ramming.max_frags_ship_id");
			buffer.append( "&" );
			buffer.append( "account_id=" );
			buffer.append( searchWord );
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
			Map							jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( queue.getOutFile() ) ));

			String						status				= (String)(jsonData.get( "status" ));
			if( status.equals( "ok" ) )
			{
				res											= true;
				int						dataCnt				= 0;
				Map						dataMap				= (Map)(jsonData.get( "data" ));
				for( Object accountDataObj : dataMap.values() )
				{
					if( accountDataObj != null )
					{
						dataCnt++;
					}
				}
				if( dataCnt > 0 )
				{
					setNoneCnt( 0 );
				}
				else
				{
					incrementNoneCnt();
					outLog( "data zero : times " + String.valueOf( getNoneCnt() ) );
					if( getNoneCnt() >= getBreakCnt() )
					{
						outLog( "over limit data zero" );
						clearLines();
					}
				}
			}
			jsonData.clear();
		}
		catch( Exception ex )
		{
			outLog( Thread.currentThread().getName() + "\t" + queue.getOutFile().getPath() + "\t" + ex.toString() , true );
		}
		return	res;
	}
}

package com.suiheikoubou.wows.app.extract;

import java.io.*;
import java.util.*;
import java.math.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.model.*;
import com.suiheikoubou.wows.model.*;

public final class ClanDetailExtApp extends AbstractApp
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
				File					clanFile			= new File( outThisFolder , args[6] );
				File					logFile				= new File( outThisFolder  , "error.log" );
				ClanDetailExtApp		instance			= new ClanDetailExtApp( logFile );
				instance.execute( inFolder , outFolder , clanFile );
			}
			else
			{
				System.out.println( "usage : java ClanDetailExtApp [in base folder] [out base folder] [server] [date this] [in folder] [out folder] [clan file]" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public ClanDetailExtApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFolder , File outFolder , File clanFile ) throws Exception
	{
		List<ClanInfo>					clans				= new ArrayList<ClanInfo>();
		ModelStorage<AccountClanInfo>	modelStorage		= new ModelStorage<AccountClanInfo>();
		outLog( outFolder.getPath() );
		outFolder.mkdirs();
		File[]							inFiles				= inFolder.listFiles( new XFileFilter.Json() );
		int								ix					= 0;
		for( File inFile : inFiles )
		{
			outLog( String.format( "%6d/%6d" , ix , inFiles.length ) + ":" + inFile.getName() );
			List<AccountClanInfo>		models				= loadModels( inFile , clans );
			for( AccountClanInfo model : models )
			{
				modelStorage.add( model , WowsModelBase.KT_AID1000 );
			}
			models.clear();
			ix++;
		}
		outLog( String.format( "%6d/%6d" , ix , inFiles.length ) );
		outLog( "flush:" + String.format( "%d" , modelStorage.size() ) );
		modelStorage.store( outFolder , ".txt" , true , AccountClanInfo.ST_NONE , WowsModelBase.cs );
		modelStorage.clear();
		Models.storeModels( clanFile , clans , ClanInfo.ST_NONE , WowsModelBase.cs );
		clans.clear();
	}
	protected List<AccountClanInfo> loadModels( File inFile , List<ClanInfo> clans ) throws Exception
	{
		List<AccountClanInfo>			models				= new ArrayList<AccountClanInfo>();

		Map								jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( inFile ) ));
		if( JsonUtil.getMapDataString( jsonData , "status" ).equals( "ok" ) )
		{
			Map							dataMap				= (Map)(jsonData.get( "data" ));
			for( Object clanIdObj : dataMap.keySet() )
			{
				String					tclanId				= (String)clanIdObj;
				Map						clanDataMap			= (Map)(dataMap.get( tclanId ));
				if( clanDataMap != null )
				{
					ClanInfo			clan				= new ClanInfo();
					clan.clanId								= JsonUtil.getMapDataLong	( clanDataMap , "clan_id" );
					clan.clanName							= JsonUtil.getMapDataString	( clanDataMap , "name" );
					clan.clanTag							= JsonUtil.getMapDataString	( clanDataMap , "tag" );
					clans.add( clan );
					
					List				members				= (List)(clanDataMap.get( "members_ids" ));
					for( Object memberIdObj : members )
					{
						BigDecimal		memberIdValue		= (BigDecimal)(memberIdObj);
						AccountClanInfo	model				= new AccountClanInfo();
						model.accountId						= Long.valueOf( memberIdValue.longValue() );
						model.clanId						= clan.clanId;
						model.clanTag						= clan.clanTag;
						models.add( model );
					}
				}
			}
		}
		else
		{
			outLog( "status not ok : " + inFile.getName() );
		}
		jsonData.clear();
		return	models;
	}
}

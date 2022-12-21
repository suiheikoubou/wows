package com.suiheikoubou.wows.app.download;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;
import com.suiheikoubou.common.dlworker.*;

public abstract class AbstractWoWsDlApp extends AbstractDlApp
{
	private String						_applicationId;
	protected AbstractWoWsDlApp( File logFile )
	{
		super( logFile );
		_applicationId										= "";
	}
	protected final void setApplicationId( String applicationId )
	{
		_applicationId										= applicationId;
	}
	protected final String getApplicationId()
	{
		return	_applicationId;
	}
	public abstract DlQueue getQueue();
	public abstract boolean checkQueue( DlQueue queue );
	public DlQueue checkQueue( DlQueue queue , int retryCnt )
	{
		DlQueue							nextQueue			= queue;
		if( checkQueue( queue ) )
		{
			nextQueue										= null;
		}
		return	nextQueue;
	}
	protected boolean getStatus( File file ) throws IOException
	{
		boolean							status				= false;
		Map								jsonData			= (Map)(JsonUtil.toJsonData( FileIO.loadBytes( file ) ));
		String							statusStr			= (String)(jsonData.get( "status" ));
		if( statusStr.equals( "ok" ) )
		{
			status											= true;
		}
		jsonData.clear();
		return	status;
	}
}

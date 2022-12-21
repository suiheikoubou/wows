package com.suiheikoubou.common;

import java.io.*;
import java.util.*;

public abstract class AbstractApp implements Logger
{
	private File						_logFile;
	
	protected AbstractApp( File logFile )
	{
		setLogFile( logFile );
	}
	protected final void setLogFile( File logFile )
	{
		_logFile											= logFile;
	}

	public void outLog( String message )
	{
		outLog( message , false );
	}
	public synchronized void outLog( String message , boolean inFileOutput )
	{
		System.out.println( message );
		if( ( _logFile != null ) && ( inFileOutput ) )
		{
			try
			{
				PrintWriter				writer				= new PrintWriter( new BufferedWriter( new FileWriter( _logFile , true ) ) );
				writer.println( message );
				writer.close();
			}
			catch( IOException ex )
			{
				throw	new RuntimeException( "outLog:" + ex.getMessage() , ex.getCause() );
			}
		}
	}
}

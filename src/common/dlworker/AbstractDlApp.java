package com.suiheikoubou.common.dlworker;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;

public abstract class AbstractDlApp extends AbstractApp implements DlCallback
{
	private Queue<String>				_lines;
	private URL							_url;
	private File						_outFolder;
	private File						_lockFile;
	private int							_seqNo;
	
	protected AbstractDlApp( File logFile )
	{
		super( logFile );
		_lines												= new LinkedList<String>();
		_url												= null;
		_outFolder											= null;
		_seqNo												= 0;
	}
	protected final void setUrl( URL url )
	{
		_url												= url;
	}
	protected final void setOutFolder( File outFolder )
	{
		_outFolder											= outFolder;
		_lockFile											= new File( outFolder , "lock.txt" );
	}
	protected final void setSeqNo( int seqNo )
	{
		_seqNo												= seqNo;
	}
	protected final URL getUrl()
	{
		return	_url;
	}
	protected final File getOutFolder()
	{
		return	_outFolder;
	}
	protected final File getLockFile()
	{
		return	_lockFile;
	}
	public final boolean isLock()
	{
		return	getLockFile().exists();
	}
	protected final int getSeqNo()
	{
		return	_seqNo;
	}
	protected final void incrementSeqNo()
	{
		_seqNo++;
	}
	protected final void appendLines( File inFile ) throws IOException
	{
		BufferedReader					reader				= new BufferedReader( new FileReader( inFile ) );
		String							line				= "";
		while( ( line = reader.readLine() ) != null )
		{
			synchronized( _lines )
			{
				_lines.add( line );
			}
		}
		reader.close();
	}
	protected final void appendLines( List<String> plines )
	{
		for( String line : plines )
		{
			synchronized( _lines )
			{
				_lines.add( line );
			}
		}
	}
	protected final void clearLines()
	{
		synchronized( _lines )
		{
			_lines.clear();
		}
	}
	protected final String pollLine()
	{
		synchronized( _lines )
		{
			return	_lines.poll();
		}
	}
	public final int getLineCount()
	{
		return	_lines.size();
	}
	protected String getQueueString( int keyCnt )
	{
		String							queueString			= null;
		StringBuffer					buffer				= new StringBuffer();
		for( int ix = 0 ; ix < keyCnt ; ix++ )
		{
			String						line				= pollLine();
			if( line != null )
			if( line.length() >= 1 )
			{
				String[]				cmds				= line.split( "\t" );
				if( cmds[0].length() >= 1 )
				{
					if( buffer.length() > 0 )
					{
						buffer.append( "," );
					}
					buffer.append( cmds[0] );
				}
			}
		}
		if( buffer.length() > 0 )
		{
			queueString										= buffer.toString();
		}
		return	queueString;
	}
	public abstract DlQueue getQueue();
//	public abstract boolean checkQueue( DlQueue queue );
	public abstract DlQueue checkQueue( DlQueue queue , int retryCnt );
}

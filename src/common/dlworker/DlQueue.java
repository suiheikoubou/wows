package com.suiheikoubou.common.dlworker;

import java.io.*;
import java.net.*;
import java.util.*;

public class DlQueue
{
	private static final int			_MAX_BUFFER_SIZE_	= 65536;
	private int							_seqNo;
	private URL							_url;
	private String						_keyword;
	private String						_postString;
	private File						_outFile;
	private boolean						_isSkip;
	
	protected DlQueue()
	{
		_seqNo												= 0;
		_url												= null;
		_keyword											= "";
		_postString											= "";
		_outFile											= null;
		_isSkip												= false;
	}
	public final int					getSeqNo()
	{
		return	_seqNo;
	}
	public final URL					getUrl()
	{
		return	_url;
	}
	public final String					getKeyword()
	{
		return	_keyword;
	}
	public final String					getPostString()
	{
		return	_postString;
	}
	public final File					getOutFile()
	{
		return	_outFile;
	}
	public final boolean				isSkip()
	{
		return	_isSkip;
	}
	public final void					setSkip()
	{
		setSkip( true );
	}
	public final void					setSkip( boolean val )
	{
		_isSkip												= val;
	}
	public String						toString()
	{
		return	getKeyword();
	}
	public static DlQueue getInstance( int seqNo , URL url , String keyword , String postString , File outFile )
	{
		DlQueue							queue				= new DlQueue();
		queue._seqNo										= seqNo;
		queue._url											= url;
		queue._keyword										= keyword;
		queue._postString									= postString;
		queue._outFile										= outFile;
		return	queue;
	}
	public void download() throws IOException
	{
		download( null );
	}
	public void download( Hashtable<String,String> reqProperties ) throws IOException
	{
		URLConnection					connection			= getUrl().openConnection();
		connection.setConnectTimeout( 10000 );
		connection.setReadTimeout( 10000 );
		if( reqProperties != null )
		{
			for( Map.Entry<String,String> reqProperty : reqProperties.entrySet() )
			{
				connection.setRequestProperty( reqProperty.getKey() , reqProperty.getValue() );
			}
		}
		String							postString			= getPostString();
		if( postString == null )
		{
			postString										= "";
		}
		if( postString.length() > 0 )
		{
			connection.setDoOutput( true );
			BufferedOutputStream		outStream			= new BufferedOutputStream( connection.getOutputStream() );
			byte[]						outBytes			= postString.getBytes();
			outStream.write( outBytes , 0 , outBytes.length );
			outStream.close();
		}
		else
		{
			connection.setDoOutput( false );
		}
		
		connection.connect();
		
		BufferedInputStream				inStream			= new BufferedInputStream( connection.getInputStream() );
		BufferedOutputStream			fileStream			= new BufferedOutputStream( new FileOutputStream( getOutFile() ) );
		byte[]							inBytes				= new byte[ _MAX_BUFFER_SIZE_ ];
		int								inLen				= 0;
		while( ( inLen = inStream.read( inBytes , 0 , _MAX_BUFFER_SIZE_ ) ) > 0 )
		{
			fileStream.write( inBytes , 0 , inLen );
		}
		inStream.close();
		fileStream.close();
	}
}

package com.suiheikoubou.common.dlworker;

import java.io.*;
import java.net.*;
import java.util.*;
import com.suiheikoubou.common.*;

public class DlThread extends Thread
{
	private Logger						_logger;
	private DlCallback					_callback;
	private boolean						_enabled;
	private long						_interval;
	private int							_maxRetryCnt;
	private int							_exceptionCnt;
	private Hashtable<String,String>	_reqProperties;
	
	private DlThread()
	{
		super();
	}
	private DlThread( ThreadGroup group , String name )
	{
		super( group , name );
		_reqProperties										= new Hashtable<String,String>();
	}
	private final void setLogger( Logger logger )
	{
		if( logger != null )
		{
			_logger											= logger;
		}
		else
		{
			throw	new IllegalArgumentException( "setLogger: logger=null" );
		}
	}
	private final void setCallback( DlCallback callback )
	{
		if( callback != null )
		{
			_callback										= callback;
		}
		else
		{
			throw	new IllegalArgumentException( "setCallback: callback=null" );
		}
	}
	private final void setInterval( long interval )
	{
		if( ( interval > 0L ) && ( interval < 1000000L ) )
		{
			_interval										= interval;
		}
		else
		{
			throw	new IllegalArgumentException( "setInterval: interval=" + String.valueOf( interval ) );
		}
	}
	private final void setMaxRetryCnt( int maxRetryCnt )
	{
		if( ( maxRetryCnt > 0 ) && ( maxRetryCnt < 100000 ) )
		{
			_maxRetryCnt									= maxRetryCnt;
		}
		else
		{
			throw	new IllegalArgumentException( "setMaxRetryCnt: maxRetryCnt=" + String.valueOf( maxRetryCnt ) );
		}
	}
	private final void putReqProperties( Map<String,String> reqProperties )
	{
		_reqProperties.putAll( reqProperties );
	}
	protected final long waitNext( long nextMillis )
	{
		long							millis				= nextMillis - System.currentTimeMillis();
		waitP( millis );
		return	nextMillis + _interval;
	}
	protected final void waitP( long millis )
	{
		if( millis > 0L )
		{
			try
			{
				sleep( millis );
			}
			catch( InterruptedException ex )
			{
				outLog( "interrupted" , true );
				setDisabled();
			}
		}
	}
	private final void setEnabled()
	{
		_enabled											= true;
	}
	public final void setDisabled()
	{
		_enabled											= false;
	}
	protected final void outLog( String message )
	{
		outLog( message , false );
	}
	protected final void outLog( String message , boolean isFileOutput )
	{
		_logger.outLog( getName() + " " + message , isFileOutput );
	}
	public final void start()
	{
		if( _enabled )
		{
			super.start();
		}
		else
		{
			throw	new RuntimeException( getName() + " thread not initialized" );
		}
	}
	public void run()
	{
		long							nextMillis			= System.currentTimeMillis() + _interval;
		DlQueue							queue				= null;
		int								retryCnt			= 0;
		_exceptionCnt										= 0;

		outLog( "started" );
		while( _enabled )
		{
			if( queue != null )
			{
				if( _callback.isLock() )
				{
				}
				else
				{
					retryCnt++;
					if( retryCnt >= _maxRetryCnt )
					{
						outLog( "retry over limit " + queue.toString() , true );
						queue								= null;
					}
				}
			}
			if( queue == null )
			{
				queue										= _callback.getQueue();
				retryCnt									= 0;
				_exceptionCnt								= 0;
			}
			if( queue != null )
			{
				if( _callback.isLock() )
				{
					outLog( "locked " + queue.toString() );
				}
				else
				{
					outLog( String.format( "%05d" , retryCnt ) + " " + queue.toString() );
					if( !( queue.isSkip() ) )
					{
						if( executeQueue( queue ) )
						{
							_exceptionCnt					= 0;
							DlQueue		nextQueue			= _callback.checkQueue( queue , retryCnt );
							if( nextQueue == null )
							{
								queue						= null;
							}
							else
							{
								if( nextQueue.equals( queue ) )
								{
									outLog( "retry " + queue.toString() );
									queue.getOutFile().delete();
								}
								else
								{
									queue					= nextQueue;
								}
							}
						}
					}
					else
					{
						outLog( "skip " + queue.toString() );
						queue								= null;
					}
				}
			}
			else
			{
				setDisabled();
			}
			if( _exceptionCnt > 100 )
			{
				outLog( "exception over " + queue.toString() );
				queue										= null;
			}
			nextMillis										= waitNext( nextMillis );
		}
		outLog( "finished" );
	}
	protected boolean executeQueue( DlQueue queue )
	{
		boolean							res					= false;
		try
		{
			queue.download( _reqProperties );
			res												= queue.getOutFile().exists();
		}
		catch( Exception ex )
		{
			outLog( "executeQueue\t" + ex.toString() );
			_exceptionCnt++;
		}
		return	res;
	}
	public static DlThread getInstance( ThreadGroup group , String name , Logger logger , DlCallback callback , long interval )
	{
		return	getInstance( group , name , logger , callback , interval , 90 , null );
	}
	public static DlThread getInstance( ThreadGroup group , String name , Logger logger , DlCallback callback , long interval , int maxRetryCnt , Hashtable<String,String> reqProperties )
	{
		DlThread						thread				= new DlThread( group , name );
		thread.setLogger( logger );
		thread.setCallback( callback );
		thread.setInterval( interval );
		thread.setMaxRetryCnt( maxRetryCnt );
		if( reqProperties != null )
		{
			thread.putReqProperties( reqProperties );
		}
		thread.setEnabled();
		return	thread;
	}
}

package com.suiheikoubou.common.dlworker;

import java.util.*;
import com.suiheikoubou.common.*;

public class DlWorker
{
	private ThreadGroup					_group;
	private List<DlThread>				_threads;
	
	protected DlWorker( String name )
	{
		_group												= new ThreadGroup( name );
		_threads											= new ArrayList<DlThread>();
	}
	protected final ThreadGroup			getThreadGroup()
	{
		return	_group;
	}
	protected final void				appendThread( DlThread thread )
	{
		_threads.add( thread );
	}
	public void startAll( long shiftTime ) throws InterruptedException
	{
		for( DlThread thread : _threads )
		{
			thread.start();
			Thread.sleep( shiftTime );
		}
	}
	public final void disabledAll()
	{
		for( DlThread thread : _threads )
		{
			thread.setDisabled();
		}
	}
	public final int activeCount()
	{
		return	_group.activeCount();
	}
	public static DlWorker getInstance( String name , Logger logger , DlCallback callback , int threadCnt , long interval )
	{
		return	getInstance( name , logger , callback , threadCnt , interval , 90 , null );
	}
	public static DlWorker getInstance( String name , Logger logger , DlCallback callback , int threadCnt , long interval , int maxRetryCnt , Hashtable<String,String> reqProperties )
	{
		DlWorker						worker				= new DlWorker( name );
		for( int ix = 0 ; ix < threadCnt ; ix++ )
		{
			String						threadName			= name + "_" + String.format( "%03d" , ix );
			DlThread					thread				= DlThread.getInstance( worker.getThreadGroup() , threadName , logger , callback , interval , maxRetryCnt , reqProperties );
			worker.appendThread( thread );
		}
		return	worker;
	}
}

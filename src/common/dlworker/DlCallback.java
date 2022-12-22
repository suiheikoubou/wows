package com.suiheikoubou.common.dlworker;

public interface DlCallback
{
	public DlQueue getQueue();
//	public boolean checkQueue( DlQueue queue );
	public DlQueue checkQueue( DlQueue queue , int retryCnt );
	public boolean isLock();
}

package com.suiheikoubou.common.model;

public interface Model<T>
{
	public T newInstance();
	public void load( String[] cmds );
	public String getStoreText( int version );
	public String getStorageKey( int keyType );
}

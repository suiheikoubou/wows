package com.suiheikoubou.common.model;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public final class ModelStorage<T extends Model<T>>
{
	protected Map<String,List<T>>		storageMap;
	
	public ModelStorage()
	{
		storageMap											= new TreeMap<String,List<T>>();
	}
	public void clear()
	{
		for( List<T> models : storageMap.values() )
		{
			models.clear();
		}
		storageMap.clear();
	}
	public void add( T model , int keyType )
	{
		String							storageKey			= model.getStorageKey( keyType );
		List<T>							models				= storageMap.get( storageKey );
		if( models == null )
		{
			models											= new ArrayList<T>();
		}
		models.add( model );
		storageMap.put( storageKey , models );
	}
	public int size()
	{
		int								sz					= 0;
		for( List<T> models : storageMap.values() )
		{
			sz												+=models.size();
		}
		return	sz;
	}
	public void store( File folder , String postfix , int version , Charset cs ) throws IOException
	{
		store( folder , postfix , false , version , cs );
	}
	public void store( File folder , String postfix , boolean append , int version , Charset cs ) throws IOException
	{
		for( Map.Entry<String,List<T>> entry : storageMap.entrySet() )
		{
			String						key					= entry.getKey();
			List<T>						models				= entry.getValue();
			File						outFile				= new File( folder , key + postfix );
			Models.storeModels( outFile , append , models , version , cs );
		}
	}
}

package com.suiheikoubou.common.model;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public final class Models
{
	public static final String			DEFAULT_DELIMITER	= "\t";
	
	protected Models()
	{
	}
	//----------------------------------------------------------------------------------------------
	public static <T extends Model<T>> List<T> loadModels( File file , T hina ) throws IOException
	{
		return	loadModels( file , hina , Charset.defaultCharset() , DEFAULT_DELIMITER );
	}
	public static <T extends Model<T>> List<T> loadModels( File file , T hina , Charset cs ) throws IOException
	{
		return	loadModels( file , hina , cs , DEFAULT_DELIMITER );
	}
	public static <T extends Model<T>> List<T> loadModels( File file , T hina , Charset cs , String delimiter ) throws IOException
	{
		List<T>							models				= new ArrayList<T>();
		BufferedReader					reader				= new BufferedReader( new InputStreamReader( new FileInputStream( file ) , cs ) );
		String							line				= "";
		while( ( line = reader.readLine() ) != null )
		{
			T							model				= hina.newInstance();
			model.load( line.split( delimiter ) );
			models.add( model );
		}
		reader.close();
		return	models;
	}
	//----------------------------------------------------------------------------------------------
	public static <T extends Model<T>> void storeModels( File file , Collection<T> models , int version ) throws IOException
	{
		storeModels( file , false , models , version , Charset.defaultCharset() );
	}
	public static <T extends Model<T>> void storeModels( File file , Collection<T> models , int version , Charset cs ) throws IOException
	{
		storeModels( file , false , models , version , cs );
	}
	public static <T extends Model<T>> void storeModels( File file , boolean append , Collection<T> models , int version ) throws IOException
	{
		storeModels( file , append , models , version , Charset.defaultCharset() );
	}
	public static <T extends Model<T>> void storeModels( File file , boolean append , Collection<T> models , int version , Charset cs ) throws IOException
	{
		PrintWriter						writer				= new PrintWriter( new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file , append ) , cs ) ) );
		for( T model : models )
		{
			writer.println( model.getStoreText( version ) );
		}
		writer.close();
	}
	//----------------------------------------------------------------------------------------------
	public static <K,V extends Mappable<K>> Map<K,V> toMap( Collection<V> models )
	{
		Map<K,V>						map					= new TreeMap<K,V>();
		for( V model : models )
		{
			map.put( model.getKey() , model );
		}
		return	map;
	}
	//----------------------------------------------------------------------------------------------


	//----------------------------------------------------------------------------------------------
}

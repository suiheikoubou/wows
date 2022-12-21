package com.suiheikoubou.app;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import com.suiheikoubou.common.*;

public class StringSetMergeApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 4 )
			{
				File					baseFolder			= new File( args[0] );
				File					in1File				= new File( baseFolder , args[1] );
				File					in2File				= new File( baseFolder , args[2] );
				File					outFile				= new File( baseFolder , args[3] );
				Charset					cs					= Charset.defaultCharset();
				if( args.length >= 5 )
				{
					cs										= Charset.forName( args[4] );
				}
				File					logFile				= new File( baseFolder , "error.log" );
				StringSetMergeApp		instance			= new StringSetMergeApp( logFile );
				instance.execute( in1File , in2File , outFile , cs );
			}
			else
			{
				System.out.println( "usage : java StringSetMergeApp [base folder] [in1 file] [in2 file] [out file] (charset)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public StringSetMergeApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File in1File , File in2File , File outFile , Charset cs ) throws Exception
	{
		List<String>					in1Lines			= FileIO.loadLines( in1File , cs );
		List<String>					in2Lines			= FileIO.loadLines( in2File , cs );
		Set<String>						outLineSet			= new TreeSet<String>();
		outLineSet.addAll( in1Lines );
		outLineSet.addAll( in2Lines );
		FileIO.storeLines( outFile , outLineSet , cs );
		in1Lines.clear();
		in2Lines.clear();
		outLineSet.clear();
	}
}

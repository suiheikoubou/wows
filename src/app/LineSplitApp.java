package com.suiheikoubou.app;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import com.suiheikoubou.common.*;

public class LineSplitApp extends AbstractApp
{
	public static void main( String[] args )
	{
		try
		{
			if( args.length >= 5 )
			{
				File					baseFolder			= new File( args[0] );
				File					inFile				= new File( baseFolder , args[1] );
				File					outFile				= new File( baseFolder , args[2] );
				Charset					charset				= Charset.forName( args[3] );
				int						column				= Integer.parseInt( args[4] );
				String					delimiter			= "\t";
				if( args.length >= 6 )
				{
					delimiter								= args[5];
				}
				File					logFile				= new File( baseFolder , "error.log" );
				LineSplitApp			instance			= new LineSplitApp( logFile );
				instance.execute( inFile , outFile , charset , column , delimiter );
			}
			else
			{
				System.out.println( "usage : java LineSplitApp [base folder] [in file] [out file] [charset] [column] (delimiter)" );
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	public LineSplitApp( File logFile )
	{
		super( logFile );
	}
	public void execute( File inFile , File outFile , Charset charset , int column , String delimiter ) throws Exception
	{
		List<String>					inLines				= FileIO.loadLines( inFile , charset );
		outLog( "in  " + inFile.getPath() + " " + String.format( "%d" , inLines.size() ) );
		List<String>					outLines			= new ArrayList<String>();
		for( String inLine : inLines )
		{
			String[]					cmds				= inLine.split( delimiter );
			outLines.add( cmds[ column ] );
		}
		FileIO.storeLines( outFile , outLines , charset );
		outLog( "out " + outFile.getPath() + " " + String.format( "%d" , outLines.size() ) );
		inLines.clear();
		outLines.clear();
	}
}

package com.ween.shooter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.util.Log;

/**
 * Parses files containing time stamps into a list of 'Long' values
 * Simple support for comments
 */

public class Parser {
	
	private final static String PARSER_TAG = "Parser";
	
	private Context context;
	private BufferedReader bufferedReader;

	// Special parsing characters
	private final static String SEPARATOR = ",";
	private final static String COMMENT = "//";
	private final static String COLON = ":";
	
	Parser(Context context) {
		// Context is required to access the assets directory
		this.context = context;
	}
	
	public Queue<Long> parse(String filename) {	
		// Used to enqueue the time stamps
		Queue<Long> timings = new LinkedList<Long>();
		
		// Reads in the first time stamp
		readFile(filename);
		String line = readLine();
		
		// Steps through line by line and adds the time stamps to a list
		while (line != null) {
			String[] values = line.split(SEPARATOR);
			for (String value : values) {
				if (!value.startsWith(COMMENT) && !value.isEmpty())
					timings.add(Long.valueOf(value.trim().replace(COLON, "")));
			}
			
			// Next iteration
			line = readLine();
		} 
		
		return timings;
	}
	
	private void readFile(String filename) {
		InputStream inputStream = null;
		try {
			// Our file must be located within the assets directory
			inputStream = context.getResources().getAssets().open(filename);
		} catch (IOException e) {
			Log.e(PARSER_TAG, "Couldn't find '" + filename + "' in assets directory");
			e.printStackTrace();
		}
		InputStreamReader streamReader = new InputStreamReader(inputStream);
		bufferedReader = new BufferedReader(streamReader);
	}
	
	private String readLine() {
		String line = null;
		try {
			line = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
	
}

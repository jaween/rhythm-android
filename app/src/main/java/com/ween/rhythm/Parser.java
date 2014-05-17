package com.ween.rhythm;

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
	
	private Context context;
	private BufferedReader bufferedReader;

	// Special parsing characters
	private final static String SEPARATOR = ",";
	private final static String COMMENT_REGEX = "//.*";
	private final static String WHITESPACE_REGEX = "[\t ]+";
	private final static String COLON = ":";

    private final static String TAG = "Parser";
	
	Parser(Context context) {
		// Context is required to access the assets directory
		this.context = context;
	}
	
	// Extracts time stamps from an input file 
	public Queue<Long> parse(String filename) {	
		// Used to enqueue the time stamps
		Queue<Long> timings = new LinkedList<Long>();
		
		// Reads in the first time stamp
		readFile(filename);
		String line = readLine();
		
		// Steps through line by line and adds the time stamps to a list
        int lineNumber = 1;
		while (line != null) {
			String clearedLine = line.replaceAll(COMMENT_REGEX, "");			// Comments ('//' followed by any character)
			String[] values = clearedLine.split(SEPARATOR);
			for (String value : values) {
				// Remove unnecessary characters
				value = value.replaceAll(WHITESPACE_REGEX, "");	// Tabs and spaces
				value = value.replace(COLON, "");				// Time stamp colons
				
				// String should now be empty or contain a time stamp
				if (!value.isEmpty())
                    try {
                        timings.add(Long.valueOf(value));
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Line " + lineNumber + " is invalid (" + line + ")");
                        continue;
                    }
			}
			
			// Next iteration
			line = readLine();
            lineNumber++;
		} 
		
		return timings;
	}
	
	private void readFile(String filename) {
		InputStream inputStream = null;
		try {
			// Our file must be located within the assets directory
			inputStream = context.getResources().getAssets().open(filename);
		} catch (IOException e) {
			Log.e(TAG, "Couldn't find '" + filename + "' in assets directory");
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

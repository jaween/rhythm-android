package com.ween.shooter;

import java.util.Queue;

import android.util.Log;

/**
 * Keeps track of the rhythm (temporary class name)
 */

public class Beats {
	
	// Keep track of rhythm
	private Queue<Long> timings;	// All beats
	private Long currentBeat;		// Head of queue
	
	// Player attempt result types
	public final static int RESULT_GOOD = 0;
	public final static int RESULT_BAD = 1;
	public final static int RESULT_MISS = 2;
	public final static int RESULT_WAY_OFF = 3;
	public final static int RESULT_NO_REMAINING_BEATS = 4;
	
	// Time in milliseconds
	private final static long LEEWAY = 200; 
	private final static long MISSED = 300;
	
	private final static String BEATS_TAG = "Beats";
	
	Beats(Queue<Long> timings) {
		this.timings = timings;
		currentBeat = timings.remove();	
	}
	
	// Determines if the player was on beat, moves to the next time stamp regardless of success
	public int pollSuccess(long currentTime) {
		if (currentBeat == null) {
			return RESULT_NO_REMAINING_BEATS;
		} else if (currentTime > currentBeat - LEEWAY && currentTime < currentBeat + LEEWAY) {
			// Attempted and on beat (move to next beat)
			currentBeat = timings.poll();
			return RESULT_GOOD;
		} else if (currentTime > currentBeat - LEEWAY - MISSED && currentTime < currentBeat + LEEWAY + MISSED) {
			// Attempted but off beat (move to next beat)
			currentBeat = timings.poll();
			return RESULT_BAD;
		} else {
			// Not a legitimate attempt
			return RESULT_WAY_OFF;
		}
	}
	
	// Determines the current state rhythm state, moves to next time stamp ONLY if the user missed the beat
	public int peekSuccess(long currentTime) {
		if (currentBeat == null) {
			return RESULT_NO_REMAINING_BEATS;
		} else if (currentTime > currentBeat - LEEWAY && currentTime < currentBeat + LEEWAY) {
			return RESULT_GOOD;
		} else if (currentTime > currentBeat - LEEWAY - MISSED && currentTime < currentBeat + LEEWAY + MISSED) {
			return RESULT_BAD;
		} else if (currentTime > currentBeat + LEEWAY + MISSED) {
			currentBeat = timings.poll();
			return RESULT_MISS;
		} else {
			return RESULT_WAY_OFF;
		}
	}
}
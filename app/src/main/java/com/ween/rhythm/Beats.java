package com.ween.rhythm;

import android.util.Log;

import java.util.Queue;

/**
 * Keeps track of the rhythm (temporary class name)
 */

public class Beats {
	
	// Keep track of rhythm
	private Queue<Long> timings;	// All beats
	private Long currentBeat;		// Head of queue (the beat that is just about to come up)
	
	// Player attempt result types
	public final static int RESULT_GOOD = 0;                // On beat
	public final static int RESULT_BAD = 1;                 // Just off beat
	public final static int RESULT_MISS = 2;                // Completely off beat
	public final static int RESULT_WAY_OFF = 3;             // Not a real attempt (not counted)
	public final static int RESULT_NO_REMAINING_BEATS = 4;  // End of song
	
	// Time in milliseconds
	public final static long LEEWAY = 40;
	public final static long MISSED = 80;
	
	private final static String TAG = "Beats";
	
	private RhythmEvent rhythmEvent;
	
	interface RhythmEvent {
		void nextEvent();
	}
	
	Beats(Queue<Long> timings) {
		this.timings = timings;
		currentBeat = timings.remove();
	}
	
	public void setRhythmEvent(RhythmEvent rhythmEvent) {
		this.rhythmEvent = rhythmEvent;
	}
	
	public Long getCurrentBeat() {
		return currentBeat;
	}
	
	public int getSize() {
		return timings.size();
	}
	
	// Determines if the player was on beat, moves to the next time stamp (if applicable)
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
			// Not a legitimate attempt (do nothing)
			return RESULT_WAY_OFF;
		}
	}
	
	// Determines the rhythm state, moves to next time stamp ONLY if the user missed the beat
	public int peekSuccess(long currentTime) {
		//
		if (rhythmEvent != null && currentBeat != null && currentTime >= currentBeat) {
			rhythmEvent.nextEvent();
			currentBeat = timings.poll();
			return RESULT_GOOD;
		}
		
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
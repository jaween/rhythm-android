package com.ween.shooter;

import android.graphics.Canvas;
import android.provider.MediaStore.Audio;
import android.view.MotionEvent;
import android.view.View;

/**
 * Manages music, tracking score, instances, input, player actions, various events, etc.
 * Levels must inherit from this class 
 */

public abstract class Choreographer {
	
	protected int score = 0;
	
	protected Beats eventBeats;
	protected Beats playerBeats;
	
	protected Audio music;
	
	Choreographer(Beats eventBeats, Beats playerBeats) {
		this.eventBeats = eventBeats;
		this.playerBeats = playerBeats;
	}
	
	public abstract void update(long time);
	
	public abstract void draw(Canvas canvas);
	
	public abstract boolean onTouch(View v, MotionEvent event);
	
	// Called from parent's onPause
	public void onPause() {
		//music.stop();
	}
	
	// Called from parent's onResume
	public void onResume() {
		//music.start();
	}
}

package com.ween.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Manages music, tracking score, instances, input, player actions, various events, etc.
 * Levels must inherit from this class 
 */

public abstract class Choreographer {
	
	// Game variables
	protected int score = 0;
	
	// Timings
	protected Beats eventBeats;				// E.g. food being flicked (automatic event)
	protected Beats playerBeats;			// E.g. fork stabbing food (player's action)

	// Screen information
	protected float dp;
	protected int screenWidth;
	protected int screenHeight;
	
	// Audio variables
	protected SoundPool soundPool;			// Sound effects
	protected MediaPlayer mediaPlayer;		// Background music
	protected AudioManager audioManager;

	// 
	protected Context context;
	
	Choreographer(Context context, DisplayMetrics metrics, Beats eventBeats, Beats playerBeats) {
		this.context = context;
		this.eventBeats = eventBeats;
		this.playerBeats = playerBeats;
		
		dp = metrics.density;
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public abstract void update(long time);
	
	public abstract void draw(Canvas canvas);
	
	public abstract boolean onTouch(View v, MotionEvent event);
	
	protected void playSound(int id) {
		float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		soundPool.play(id, streamVolume, streamVolume, 1, 0, 1f);
	}
	
	protected void loadBackgroundMusic(int resourceID) {
		mediaPlayer = MediaPlayer.create(context, resourceID);
	}
	
	// Called from parent's onPause
	public void onPause() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			/*mediaPlayer.release();
			mediaPlayer = null;*/
		}
	}
	
	// Called from parent's onResume
	public void onResume() {
		float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		if (mediaPlayer != null) {
			mediaPlayer.setVolume(streamVolume*2f, streamVolume*2f);
			mediaPlayer.start();
		}
	}
}

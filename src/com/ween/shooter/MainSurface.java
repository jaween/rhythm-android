package com.ween.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainSurface extends SurfaceView implements Runnable {

	// 
	private Thread thread;
	private boolean run;
	private SurfaceHolder holder;
	private final static String MAIN_SURFACE_TAG = "MainSurface";
	
	// Game variables
	private Beats playerBeats;
	private Beats eventBeats;
	private ShooterLevel level;
	
	// Screen refresh variables
	private final static int MAX_FPS = 60;
	private final static int SKIP_TICKS = 1000 / MAX_FPS;
	private long beginTime;
	
	public MainSurface(Context context) {
		super(context);
		setFocusable(true);
		
		holder = getHolder();
		
		// Loads in the timing data
		final String player_beats = "shooter_player.time";
		final String event_beats = "shooter_event.time";
		Parser parser = new Parser(context);
		playerBeats = new Beats(parser.parse(player_beats));
		eventBeats = new Beats(parser.parse(event_beats));
		
		// Creates a level (loads in graphics and audio)
		level = new ShooterLevel(context, eventBeats, playerBeats);
	}
	
	@Override
	public void run() {
		beginTime = System.currentTimeMillis();
		long nextGameTick = beginTime;
		long sleepTime = 0;
		
		while (run) {
			if (!holder.getSurface().isValid())
				continue;
			
			level.update(System.currentTimeMillis());	// Update
			Canvas c = holder.lockCanvas();
			level.draw(c);								// Draw
			holder.unlockCanvasAndPost(c);
			
			// Sleeps until next frame
			nextGameTick += SKIP_TICKS;
			sleepTime = nextGameTick - System.currentTimeMillis();
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// We're behind
			}
			
			
		}
		
	}
	
	// Called from the parent Activity's onPause
	public void onPause() {
		run = false;
		
		while(true) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		
		// End music
		level.onPause();
		
		thread = null;
	}
	
	// Called from the parent Activity's onResume
	public void onResume() {
		run = true;
		thread = new Thread(this);
		thread.start();
		
		// Begin music (begins level)
		level.onResume();
	}

	// Called from parent Activity's onTouch
	public boolean touched(View v, MotionEvent event) {
		return level.onTouch(v, event);
	}

}

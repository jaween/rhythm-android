package com.ween.rhythm;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ween.rhythm.shooter.ShooterLevel;

public class MainSurface extends SurfaceView implements Runnable {

	// Loop variables
	private Thread thread;
	private boolean run;
	private SurfaceHolder holder;
	
	// Game variables
	private Beats playerBeats;
	private Beats eventBeats;
	private ShooterLevel level;
	
	// Screen refresh variables
	private final static int MAX_FPS = 60;
	private final static int SKIP_TICKS = 1000 / MAX_FPS;
	private long beginTime;

    private final static String TAG = "MainSurface";
	
	public MainSurface(Context context, DisplayMetrics metrics) {
		super(context);
		setFocusable(true);
		
		holder = getHolder();
		
		// Creates level
		String playerBeatsFilename = "shooter_player.time";
		String eventBeatsFilename = "shooter_event.time";
		level = new ShooterLevel(context, metrics, playerBeatsFilename, eventBeatsFilename);
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
			// TODO Crashed once when starting the game
			// IMGSRV	Cannot ungregister a locked buffer
			// Surface	Surface::unlockAndPost failed, no locked buffer
			// At line holder.unlockAndPost(c) 
			// Why? Maybe use a try/catch block

            // TODO Crashed once when resuming the game
            // IMGSRV   :0: gralloc_unregister_buffer: Cannot unregister a locked buffer (ID=33633)
            // Surface  Surface::unlockAndPost failed, no locked buffer
            // Could be same problem as above
			
			// Sleeps until next frame
			nextGameTick += SKIP_TICKS;
			sleepTime = nextGameTick - System.currentTimeMillis();
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				// Skipped frames
			}
		}
	}
	
	// Called from parent's onPause()
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
		
		// Ends level and music
		level.onPause();
		
		thread = null;
	}

    // Called from parent's onResume()
	public void onResume() {
		run = true;
		thread = new Thread(this);
		thread.start();
		
		// Begins level and music
		level.onResume();
	}

    // Called from parent's onTouch()
	public boolean touched(View v, MotionEvent event) {
		return level.onTouch(v, event);
	}

}

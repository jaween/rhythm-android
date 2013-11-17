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
	private long beginTime;
	private Beats playerBeats;
	private ShooterLevel level;
	
	public MainSurface(Context context) {
		super(context);
		setFocusable(true);
		
		holder = getHolder();
		
		final String filename = "shooter.time"; 
		Parser parser = new Parser(context);
		playerBeats = new Beats(parser.parse(filename));
		
		level = new ShooterLevel(playerBeats, playerBeats);
	}

	@Override
	public void run() {
		beginTime = System.currentTimeMillis();
		
		while (run) {
			if (!holder.getSurface().isValid())
				continue;
			
			Canvas c = holder.lockCanvas();
			level.draw(c);
			holder.unlockCanvasAndPost(c);
			
			level.update(System.currentTimeMillis());
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
		
		thread = null;
	}
	
	// Called from the parent Activity's onResume
	public void onResume() {
		run = true;
		thread = new Thread(this);
		thread.start();
	}

	// Called from parent Activity's onTouch
	public boolean touched(View v, MotionEvent event) {
		return level.onTouch(v, event);
	}

}

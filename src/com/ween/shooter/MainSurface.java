package com.ween.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainSurface extends SurfaceView implements Runnable {

	private Thread thread;
	private boolean run;
	private SurfaceHolder holder;
	
	public MainSurface(Context context) {
		super(context);
		setFocusable(true);
		
		holder = getHolder();
	}

	@Override
	public void run() {
		while (run) {
			if (!holder.getSurface().isValid())
				continue;
			
			Canvas c = holder.lockCanvas();
			onDrawThings(c);
			holder.unlockCanvasAndPost(c);
		}
		
	}
	
	private void onDrawThings(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
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

}

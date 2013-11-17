package com.ween.shooter;

import java.text.NumberFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
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
	private Beats userBeats;
	
	// Temp variables
	private int colour = Color.BLUE;
	private Paint tempPaint = new Paint();
	private Paint tempTextPaint = new Paint();
	
	public MainSurface(Context context) {
		super(context);
		setFocusable(true);
		
		holder = getHolder();
		
		final String filename = "shooter.time";
		Parser parser = new Parser(context);
		userBeats = new Beats(parser.parse(filename));
		
		// Temp
		tempPaint.setAntiAlias(true);
		tempTextPaint.setAntiAlias(true);
		tempTextPaint.setTextSize(40);
		tempTextPaint.setTextAlign(Align.LEFT);
	}

	@Override
	public void run() {
		beginTime = System.currentTimeMillis();
		
		while (run) {
			if (!holder.getSurface().isValid())
				continue;
			
			Canvas c = holder.lockCanvas();
			testResults();
			onDrawThings(c);
			holder.unlockCanvasAndPost(c);
		}
		
	}
	
	private void onDrawThings(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		tempPaint.setColor(colour);
		canvas.drawCircle(350, 400, 100, tempPaint);
		
		String time = Float.toString(((float) (System.currentTimeMillis() - beginTime))/1000f);
		
		canvas.drawText(time, 300, 550, tempTextPaint);
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
	
	private void testResults() {
		int result = userBeats.wasSuccessNotVolatile(System.currentTimeMillis() - beginTime);
		long time = System.currentTimeMillis() - beginTime;
		switch (result) {
		case Beats.RESULT_GOOD:
			colour = 0xFFFF9900;
			break;
		case Beats.RESULT_BAD:
			colour = 0x44FF9900;
			break;
		case Beats.RESULT_WAY_OFF:
			colour = 0x11FF9900;
			break;
		case Beats.RESULT_NO_REMAINING_BEATS:
			colour = 0x11993366;
			break;
		}
	}

	// Called from parent Activity's onTouch
	public boolean touched(View v, MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//
				break;
			case MotionEvent.ACTION_UP:
				//
				break;
			case MotionEvent.ACTION_CANCEL:
				//
				break;
		}
		return false;
	}

}

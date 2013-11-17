package com.ween.shooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ShooterLevel extends Choreographer {

	// Temp variables
	private int colour = Color.BLUE;
	private Paint tempPaint = new Paint();
	private Paint tempTextPaint = new Paint();
	private long beginTime;
	
	private String playerResult = "";
	
	private final static String SHOOTER_LEVEL_TAG = "ShooterLevel";
	
	ShooterLevel(Beats eventBeats, Beats playerBeats) {
		super(eventBeats, playerBeats);
		
		// Temp
		tempPaint.setAntiAlias(true);
		tempTextPaint.setAntiAlias(true);
		tempTextPaint.setTextSize(40);
		tempTextPaint.setTextAlign(Align.LEFT);
		beginTime = System.currentTimeMillis();
	}
	
	@Override
	public void update(long time) {
		peekState();
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		tempPaint.setColor(colour);
		canvas.drawCircle(350, 400, 100, tempPaint);
		
		String time = Float.toString(((float) (System.currentTimeMillis() - beginTime))/1000f);
		
		canvas.drawText(time, 300, 550, tempTextPaint);
		canvas.drawText(playerResult, 300, 600, tempTextPaint);
	}
	
	private void peekState() {
		int result = playerBeats.peekSuccess(System.currentTimeMillis() - beginTime);
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
		case Beats.RESULT_MISS:
			Log.d(SHOOTER_LEVEL_TAG, "Miss");
			playerResult = "Miss";
			break;
		case Beats.RESULT_NO_REMAINING_BEATS:
			colour = 0x11993366;
			playerResult = "終わり";
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int result = playerBeats.pollSuccess(System.currentTimeMillis() - beginTime);
			switch (result) {
			case Beats.RESULT_GOOD:
				Log.d(SHOOTER_LEVEL_TAG, "Good");
				playerResult = "Good";
				colour = 0xFFFF9900;
				break;
			case Beats.RESULT_BAD:
				Log.d(SHOOTER_LEVEL_TAG, "Bad");
				playerResult = "Bad";
				colour = 0x44FF9900;
				break;
			case Beats.RESULT_WAY_OFF:
				Log.d(SHOOTER_LEVEL_TAG, "Way off");
				playerResult = "Way off";
				colour = 0x11FF9900;
				break;
			case Beats.RESULT_NO_REMAINING_BEATS:
				Log.d(SHOOTER_LEVEL_TAG, "None remaining");
				playerResult = "終わり";
				colour = 0x11993366;
				break;
			}
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

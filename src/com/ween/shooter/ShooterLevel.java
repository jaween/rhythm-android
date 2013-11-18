package com.ween.shooter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ShooterLevel extends Choreographer implements Beats.RhythmEvent {

	// Temp variables
	private int colour = Color.BLUE;
	private Paint tempPaint = new Paint();
	private Paint tempTextPaint = new Paint();
	private long beginTime;
	
	private int eventIndex = -1;
	
	// Keeps track of instances
	private ArrayList<ShooterEnemy> enemies;
	//private ArrayList<Lasers> lasers;
	private Laser laser;
	
	private String playerResult = "";
	
	private final static String SHOOTER_LEVEL_TAG = "ShooterLevel";
	private Context context;
	
	long tick = 0;
	long lastDrawTime;
	
	ShooterLevel(Context context, Beats eventBeats, Beats playerBeats) {
		super(eventBeats, playerBeats);
		this.context = context;
		
		initialiseInstances();
		initialisePaints();
		
		eventBeats.setRhythmEvent(this);
		beginTime = System.currentTimeMillis();
		lastDrawTime = beginTime;
	}
	
	private void initialisePaints() {
		// Temp
		tempPaint.setAntiAlias(true);
		tempTextPaint.setAntiAlias(true);
		tempTextPaint.setTextSize(40);
		tempTextPaint.setTextAlign(Align.LEFT);
	}
	
	@Override
	public void update(long time) {
		peekState();
		animate();
	}
	
	private void initialiseInstances() {
		//Gunner gunner = new Gunner();
		
		// Create the reusable enemy objects
		final int maxEnemies = 8;
		enemies = new ArrayList<ShooterEnemy>(maxEnemies);
		for (int i = 0; i < maxEnemies; i++) {
			enemies.add(new ShooterEnemy(context));
		}
		
		// Create the resuable laser objects
//		final int maxLasers = 8;
//		lasers = new AraryList<Lasers>(maxLasers);
//		for (int i = 0; i < maxLasers; i++) {
//			lasers.add(new Laser(context));
//		}
		//Temp rapper/laser
		laser = new Laser(context);	
		laser.setCoordinates(50, 70);
		
	}
	
	// Events that are triggered at certain times
	private void animate() {
		
		switch (eventIndex) {
		case 0:
			enemies.get(0).setVisible(true);
			break;
		case 1:
			enemies.get(1).setVisible(true);
			break;
		case 2:
			enemies.get(2).setVisible(true);
			break;
		case 3:
			enemies.get(3).setVisible(true);
			break;
		case 4:
			enemies.get(0).setVisible(false);
			break;
		case 5:
			enemies.get(1).setVisible(false);
			break;
		case 6:
			enemies.get(2).setVisible(false);
			break;
		case 7:
			enemies.get(3).setVisible(false);
			break;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		// Background
		canvas.drawColor(Color.WHITE);
		
		// Center orange circle
		tempPaint.setColor(colour);
		canvas.drawCircle(350, 400, 100, tempPaint);
		
		// Timer
		String time = Float.toString(((float) (System.currentTimeMillis() - beginTime))/1000f);
		canvas.drawText(time, 300, 550, tempTextPaint);
		canvas.drawText(playerResult, 300, 600, tempTextPaint);
		
		// FPS Indicator
		canvas.drawText((1000/(System.currentTimeMillis() - lastDrawTime)) + " FPS", 30, 60, tempTextPaint);
		lastDrawTime = System.currentTimeMillis();
		
		// Pink enemy circles
		for (ShooterEnemy enemy : enemies) {
			if (enemy.isVisible())
				enemy.draw(canvas);
		}
		
		// Lasers circles
		/*for (Laser laser : lasers) {
			if (laser.isVisible())
				laser.draw(canvas);
		}*/
		
		// Temp Rapper/laser
		laser.draw(canvas);
		laser.nextFrame();
	}
	
	private void peekState() {
		eventBeats.peekSuccess(System.currentTimeMillis() - beginTime);
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

	@Override
	public void nextEvent() {
		eventIndex++;	
		Log.d(SHOOTER_LEVEL_TAG, "eventIndex is now " + eventIndex);
	}

}

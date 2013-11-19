package com.ween.shooter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ShooterLevel extends Choreographer implements Beats.RhythmEvent {

	// Debug variables
	private int colour = Color.BLUE;
	private Paint debugPaint = new Paint();
	private Paint debugTextPaint = new Paint();
	private final static String SHOOTER_LEVEL_TAG = "ShooterLevel";
	
	// Timing variables
	private long beginTime;
	private long lastDrawTime;
	private int eventIndex = -1;
	
	// Keeps track of instances
	private ArrayList<ShooterEnemy> enemies;
	private ArrayList<Star> stars;
	private ShooterEnemy currentEnemy;
	private Laser laser;
	private Earth earth;
	private Explosion explosion;
	
	// Sound effect IDs
	private int laserAudioID;
	
	// Game variables
	private String playerResult = "";
	private int backgroundColour;
	
	// System variables
	private Context context;
	
	ShooterLevel(Context context, DisplayMetrics metrics, Beats eventBeats, Beats playerBeats) {
		super(context, metrics, eventBeats, playerBeats);
		this.context = context;
		
		// Load in resources
		initialiseResources();
		initialisePaints();
		initialiseAudio();
		
		// Callback to be notified of beats (automatic event triggers)
		eventBeats.setRhythmEvent(this);
		
		// All timings are relative to the this
		beginTime = System.currentTimeMillis();
		lastDrawTime = beginTime;
	}
	
	private void initialisePaints() {
		// Debug Paints
		debugPaint.setAntiAlias(true);
		debugTextPaint.setColor(Color.WHITE);
		debugTextPaint.setAntiAlias(true);
		debugTextPaint.setTextSize(40);
		debugTextPaint.setTextAlign(Align.LEFT);
	}
	
	private void initialiseResources() {	
		// Get colours
		backgroundColour = context.getResources().getColor(R.color.background_colour);
		
		// Create the reusable enemy objects
		final int maxEnemies = 8;
		enemies = new ArrayList<ShooterEnemy>(maxEnemies);
		for (int i = 0; i < maxEnemies; i++) {
			ShooterEnemy enemy = new ShooterEnemy(context);
			enemies.add(enemy);
		}
		
		// Create the resuable laser object
		laser = new Laser(context);	
		laser.setCoordinates(screenWidth/2 - laser.getWidth()/2, screenHeight - 600);
		
		// Gun 'stand' and face
		//Gunner gunner = new Gunner();
		// Gun barrel
		
		earth = new Earth(context);
		earth.setCoordinates(screenWidth/5 - earth.getWidth()/2, screenHeight/5 - earth.getHeight()/2);
		earth.setVisible(true);
		
		explosion = new Explosion(context);
		explosion.setCoordinates(screenWidth/2 - explosion.getWidth()/2, screenHeight/2 - explosion.getHeight()/2);
		
		// Background stars
		int numberOfStars = 30;
		stars = new ArrayList<Star>(numberOfStars);
		for (int i = 0; i < numberOfStars; i++) {
			Star star = new Star(context);
			randomiseStarValues(star);
			star.setVisible(true);
			stars.add(star);
		}
	}
	
	private void initialiseAudio() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		laserAudioID = soundPool.load(context, R.raw.laser_sound, 1);
		//loadBackgroundMusic(R.raw.munch_monk_music);
	}
	
	@Override
	public void update(long time) {
		peekState();
		animate();
		
		for (Star star : stars)
			if (star.isVisible())
				star.update();
			else {
				randomiseStarValues(star);
				star.setVisible(true);
			}
	}
	
	// Events that are triggered at certain times
	private void animate() {
		
		switch (eventIndex) {
		case 0:
			enemies.get(0).setCoordinates(screenWidth/6 - enemies.get(0).getWidth()/2, screenHeight/6 - enemies.get(0).getHeight()/2);
			enemies.get(0).setVisible(true);
			break;
		case 1:
			enemies.get(1).setCoordinates(screenWidth - screenWidth/6 - enemies.get(1).getWidth()/2, screenHeight/6 - enemies.get(1).getHeight()/2);
			enemies.get(1).setVisible(true);
			break;
		case 2:
			enemies.get(2).setCoordinates(screenWidth/6 - enemies.get(2).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(2).getHeight()/2);
			enemies.get(2).setVisible(true);
			break;
		case 3:
			enemies.get(3).setCoordinates(screenWidth - screenWidth/6 - enemies.get(3).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(3).getHeight()/2);
			enemies.get(3).setVisible(true);
			currentEnemy = enemies.get(0);
			break;
		case 4:
			enemies.get(0).setVisible(false);
			currentEnemy = enemies.get(1);
			break;
		case 5:
			enemies.get(1).setVisible(false);
			currentEnemy = enemies.get(2);
			break;
		case 6:
			enemies.get(2).setVisible(false);
			currentEnemy = enemies.get(3);
			break;
		case 7:
			enemies.get(3).setVisible(false);
			break;
		}
	}
	
	private void animateGunner() {
		laser.startAnimation();
		laser.setVisible(true);
		soundPool.play(laserAudioID, 1, 1, 1, 0, 1);
	}
	
	private void randomiseStarValues(Star star) {
		int  marginHorz = screenWidth/5;	// We don't want stars created at the edge of screen, margins fix this
		int  marginVert = screenHeight/5;
		star.setCoordinates((float) (Math.random()*(screenWidth-marginHorz)+marginHorz/2), (float) (Math.random()*(screenHeight-marginVert)+marginVert/2));
		star.setDirection((int) (star.getX() - screenWidth/2), (int) (star.getY() - screenHeight/2)); // Slope from center of screen to star
	}
	
	@Override
	public void draw(Canvas canvas) {
		// Background
		canvas.drawColor(backgroundColour);
		
		if (earth.isVisible()) {
			earth.draw(canvas);
		}
		
		// Background stars
		for (Star star : stars)
			star.draw(canvas);
		
		// Center orange circle
		debugPaint.setColor(colour);
		canvas.drawCircle(screenWidth/2, 400, 100, debugPaint);
		
		// Timer
		String time = Float.toString(((float) (System.currentTimeMillis() - beginTime))/1000f);
		canvas.drawText(time, screenWidth/2 - 55, 550, debugTextPaint);
		canvas.drawText(playerResult, screenWidth/2 - 55, 600, debugTextPaint);
		
		// FPS Indicator
		canvas.drawText((1000/(System.currentTimeMillis() - lastDrawTime)) + " FPS", 30, 60, debugTextPaint);
		lastDrawTime = System.currentTimeMillis();
		
		// Pink enemy circles
		for (ShooterEnemy enemy : enemies)
			if (enemy.isVisible())
				enemy.draw(canvas);
		
		// Laser
		if (laser.isVisible())
			laser.draw(canvas);
		
		// Explosion
		if (explosion.isVisible())
			explosion.draw(canvas);
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
			animateGunner();
			
			int result = playerBeats.pollSuccess(System.currentTimeMillis() - beginTime);
			switch (result) {
			case Beats.RESULT_GOOD:
				currentEnemy.setShotType(ShooterEnemy.SHOT_MAJOR_HIT);
				explosion.setVisible(true);
				playerResult = "Good";
				colour = 0xFFFF9900;
				break;
			case Beats.RESULT_BAD:
				currentEnemy.setShotType(ShooterEnemy.SHOT_MINOR_HIT);
				explosion.setVisible(true);
				playerResult = "Bad";
				colour = 0x44FF9900;
				break;
			case Beats.RESULT_WAY_OFF:
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

	@Override
	public void onPause() {
		// Free-up memory
		soundPool.release();
		soundPool = null;
		super.onPause();
	}

	@Override
	public void onResume() {
		// Reload audio clips
		initialiseAudio();
		super.onResume();
	}
}

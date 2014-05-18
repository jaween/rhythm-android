package com.ween.rhythm.shooter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.ween.rhythm.Beats;
import com.ween.rhythm.Choreographer;
import com.ween.rhythm.R;

public class ShooterLevel extends Choreographer {

	// Debug variables
	private final static String TAG = "ShooterLevel";
	
	// Keeps track of instances
	private ArrayList<Enemy> enemies;
	private ArrayList<Star> stars;
	private Enemy currentEnemy;
	private Laser laser;
    private Gunner gunner;
	private Earth earth;
	private Explosion explosion;
	
	// Sound effect IDs
	private int laserAudioID;
	
	// Game variables
	private int backgroundColour;
	
	// System variables
	private Context context;
	
	public ShooterLevel(Context context, DisplayMetrics metrics, String playerBeatsFilename, String eventBeatsFilename, int windowHeight) {
		super(context, metrics, playerBeatsFilename, eventBeatsFilename, windowHeight);
		this.context = context;
		
		// Load in resources
		initialiseResources();
		initialiseAudio();
	}
	
	private void initialiseResources() {	
		// Get colours
		backgroundColour = context.getResources().getColor(R.color.background_colour);

		// Create the reusable enemy objects
		final int maxEnemies = 8;
		enemies = new ArrayList<Enemy>(maxEnemies);
		for (int i = 0; i < maxEnemies; i++) {
			Enemy enemy = new Enemy(context);
			enemies.add(enemy);
		}
		
		// Gun with canon and face
		gunner = new Gunner(context);
        gunner.setCoordinates(screenWidth / 2 - gunner.getWidth() / 2, screenHeight - gunner.getHeight());
        gunner.setVisible(true);

        // Create the resuable laser object
        laser = new Laser(context);
        laser.setCoordinates(screenWidth/2 - laser.getWidth()/2, screenHeight - laser.getHeight());
		
		earth = new Earth(context);
		earth.setCoordinates(screenWidth/4 - earth.getWidth()/2, screenHeight/5 - earth.getHeight()/2);
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
		loadBackgroundMusic(R.raw.shooter_music);
	}
	
	@Override
	public void update(long time) {
		super.update(time);
		
		// Background stars (if they're off the edge of the screen restart them)
		for (Star star : stars) {
			if (star.getX() + star.getWidth() < 0)
				randomiseStarValues(star);
			else if (star.getX() > screenWidth)
				randomiseStarValues(star);
			else if (star.getY() + star.getHeight() < 0)
				randomiseStarValues(star);
			else if (star.getY() > screenHeight)
				randomiseStarValues(star);
			else 
				star.update();
		}
	}
	
	// Events that are triggered at certain times
	@Override
	protected void animate() {
        switch (eventIndex) {
            // FIRST WAVE
            case 0:
                currentEnemy = enemies.get(0);
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
                break;
            case 4:
                enemies.get(0).setVisible(false);
                enemies.get(0).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(1);
                break;
            case 5:
                enemies.get(1).setVisible(false);
                enemies.get(1).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(2);
                break;
            case 6:
                enemies.get(2).setVisible(false);
                enemies.get(2).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(3);
                break;
            case 7:
                enemies.get(3).setVisible(false);
                enemies.get(3).setShotType(Enemy.SHOT_NOT_HIT);
                break;
            case 8:
                enemies.get(0).setCoordinates(screenWidth/6 - enemies.get(0).getWidth()/2, screenHeight/6 - enemies.get(0).getHeight()/2);
                enemies.get(0).setVisible(true);
                break;
            case 9:
                enemies.get(1).setCoordinates(screenWidth - screenWidth/6 - enemies.get(1).getWidth()/2, screenHeight/6 - enemies.get(1).getHeight()/2);
                enemies.get(1).setVisible(true);
                break;
            case 10:
                enemies.get(2).setCoordinates(screenWidth/6 - enemies.get(2).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(2).getHeight()/2);
                enemies.get(2).setVisible(true);
                break;
            case 11:
                enemies.get(3).setCoordinates(screenWidth - screenWidth/6 - enemies.get(3).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(3).getHeight()/2);
                enemies.get(3).setVisible(true);
                currentEnemy = enemies.get(0);
                break;
            case 12:
                enemies.get(0).setVisible(false);
                enemies.get(0).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(1);
                break;
            case 13:
                enemies.get(1).setVisible(false);
                enemies.get(1).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(2);
                break;
            case 14:
                enemies.get(2).setVisible(false);
                enemies.get(2).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(3);
                break;
            case 15:
                enemies.get(3).setVisible(false);
                enemies.get(3).setShotType(Enemy.SHOT_NOT_HIT);
                break;
            case 16:
                enemies.get(0).setCoordinates(screenWidth/6 - enemies.get(0).getWidth()/2, screenHeight/6 - enemies.get(0).getHeight()/2);
                enemies.get(0).setVisible(true);
                break;
            case 17:
                enemies.get(1).setCoordinates(screenWidth - screenWidth/6 - enemies.get(1).getWidth()/2, screenHeight/6 - enemies.get(1).getHeight()/2);
                enemies.get(1).setVisible(true);
                break;
            case 18:
                enemies.get(2).setCoordinates(screenWidth/6 - enemies.get(2).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(2).getHeight()/2);
                enemies.get(2).setVisible(true);
                break;
            case 19:
                enemies.get(3).setCoordinates(screenWidth - screenWidth/6 - enemies.get(3).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(3).getHeight()/2);
                enemies.get(3).setVisible(true);
                currentEnemy = enemies.get(0);
                break;
            case 20:
                enemies.get(0).setVisible(false);
                enemies.get(0).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(1);
                break;
            case 21:
                enemies.get(1).setVisible(false);
                enemies.get(1).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(2);
                break;
            case 22:
                enemies.get(2).setVisible(false);
                enemies.get(2).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(3);
                break;
            case 23:
                enemies.get(3).setVisible(false);
                enemies.get(3).setShotType(Enemy.SHOT_NOT_HIT);
                break;
            case 24:
                enemies.get(0).setCoordinates(screenWidth/3 - enemies.get(0).getWidth()/2, screenHeight/4 - enemies.get(0).getHeight()/2);
                enemies.get(0).setVisible(true);
                break;
            case 25:
                enemies.get(1).setCoordinates(screenWidth - screenWidth/3 - enemies.get(1).getWidth()/2, screenHeight/4 - enemies.get(1).getHeight()/2);
                enemies.get(1).setVisible(true);
                currentEnemy = enemies.get(0);
                break;
            case 26:
                enemies.get(0).setVisible(false);
                enemies.get(0).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(1);
                break;
            case 27:
                enemies.get(1).setVisible(false);
                enemies.get(1).setShotType(Enemy.SHOT_NOT_HIT);
                break;
            // SECOND WAVE
            case 28:
                enemies.get(0).setCoordinates(screenWidth/6 - enemies.get(0).getWidth()/2, screenHeight/6 - enemies.get(0).getHeight()/2);
                enemies.get(0).setVisible(true);
                break;
            case 29:
                enemies.get(1).setCoordinates(screenWidth - screenWidth/6 - enemies.get(1).getWidth()/2, screenHeight/6 - enemies.get(1).getHeight()/2);
                enemies.get(1).setVisible(true);
                break;
            case 30:
                enemies.get(2).setCoordinates(screenWidth/6 - enemies.get(2).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(2).getHeight()/2);
                enemies.get(2).setVisible(true);
                break;
            case 31:
                enemies.get(3).setCoordinates(screenWidth - screenWidth/6 - enemies.get(3).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(3).getHeight()/2);
                enemies.get(3).setVisible(true);
                currentEnemy = enemies.get(0);
                break;
            case 32:
                enemies.get(0).setVisible(false);
                enemies.get(0).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(1);
                break;
            case 33:
                enemies.get(1).setVisible(false);
                enemies.get(1).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(2);
                break;
            case 34:
                enemies.get(2).setVisible(false);
                enemies.get(2).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(3);
                break;
            case 35:
                enemies.get(3).setVisible(false);
                enemies.get(3).setShotType(Enemy.SHOT_NOT_HIT);
                break;
            case 36:
                enemies.get(0).setCoordinates(screenWidth/6 - enemies.get(0).getWidth()/2, screenHeight/6 - enemies.get(0).getHeight()/2);
                enemies.get(0).setVisible(true);
                break;
            case 37:
                enemies.get(1).setCoordinates(screenWidth - screenWidth/6 - enemies.get(1).getWidth()/2, screenHeight/6 - enemies.get(1).getHeight()/2);
                enemies.get(1).setVisible(true);
                break;
            case 38:
                enemies.get(2).setCoordinates(screenWidth/6 - enemies.get(2).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(2).getHeight()/2);
                enemies.get(2).setVisible(true);
                break;
            case 39:
                enemies.get(3).setCoordinates(screenWidth - screenWidth/6 - enemies.get(3).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(3).getHeight()/2);
                enemies.get(3).setVisible(true);
                currentEnemy = enemies.get(0);
                break;
            case 40:
                enemies.get(0).setVisible(false);
                enemies.get(0).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(1);
                break;
            case 41:
                enemies.get(1).setVisible(false);
                enemies.get(1).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(2);
                break;
            case 42:
                enemies.get(2).setVisible(false);
                enemies.get(2).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(3);
                break;
            case 43:
                enemies.get(3).setVisible(false);
                enemies.get(3).setShotType(Enemy.SHOT_NOT_HIT);
                break;
            case 44:
                enemies.get(0).setCoordinates(screenWidth/6 - enemies.get(0).getWidth()/2, screenHeight/6 - enemies.get(0).getHeight()/2);
                enemies.get(0).setVisible(true);
                break;
            case 45:
                enemies.get(1).setCoordinates(screenWidth - screenWidth/6 - enemies.get(1).getWidth()/2, screenHeight/6 - enemies.get(1).getHeight()/2);
                enemies.get(1).setVisible(true);
                break;
            case 46:
                enemies.get(2).setCoordinates(screenWidth/6 - enemies.get(2).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(2).getHeight()/2);
                enemies.get(2).setVisible(true);
                break;
            case 47:
                enemies.get(3).setCoordinates(screenWidth - screenWidth/6 - enemies.get(3).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(3).getHeight()/2);
                enemies.get(3).setVisible(true);
                currentEnemy = enemies.get(0);
                break;
            case 48:
                enemies.get(0).setVisible(false);
                enemies.get(0).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(1);
                break;
            case 49:
                enemies.get(1).setVisible(false);
                enemies.get(1).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(2);
                break;
            case 50:
                enemies.get(2).setVisible(false);
                enemies.get(2).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(3);
                break;
            case 51:
                enemies.get(3).setVisible(false);
                enemies.get(3).setShotType(Enemy.SHOT_NOT_HIT);
                break;
            case 52:
                enemies.get(0).setCoordinates(screenWidth/6 - enemies.get(0).getWidth()/2, screenHeight/6 - enemies.get(0).getHeight()/2);
                enemies.get(0).setVisible(true);
                break;
            case 53:
                enemies.get(1).setCoordinates(screenWidth - screenWidth/6 - enemies.get(1).getWidth()/2, screenHeight/6 - enemies.get(1).getHeight()/2);
                enemies.get(1).setVisible(true);
                break;
            case 54:
                enemies.get(2).setCoordinates(screenWidth/6 - enemies.get(2).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(2).getHeight()/2);
                enemies.get(2).setVisible(true);
                break;
            case 55:
                enemies.get(3).setCoordinates(screenWidth - screenWidth/6 - enemies.get(3).getWidth()/2, screenHeight/2 - screenHeight/6 - enemies.get(3).getHeight()/2);
                enemies.get(3).setVisible(true);
                currentEnemy = enemies.get(0);
                break;
            case 56:
                enemies.get(0).setVisible(false);
                enemies.get(0).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(1);
                break;
            case 57:
                enemies.get(1).setVisible(false);
                enemies.get(1).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(2);
                break;
            case 58:
                enemies.get(2).setVisible(false);
                enemies.get(2).setShotType(Enemy.SHOT_NOT_HIT);
                currentEnemy = enemies.get(3);
                break;
            case 59:
                enemies.get(3).setVisible(false);
                enemies.get(3).setShotType(Enemy.SHOT_NOT_HIT);
                break;
		}
	}

    @Override
    protected void miss() {
        // Gunner is sad and wobbles for a second
        gunner.startAnimation();
    }

    private void shoot() {
        gunner.shoot();

		laser.startAnimation();
		laser.setVisible(true);
		soundPool.play(laserAudioID, 1, 1, 1, 0, 1);
	}
	
	private void randomiseStarValues(Star star) {
        // Margins stop stars from being placed at the edge of the screen
		final int  marginHorz = screenWidth/5;
		final int  marginVert = screenHeight/5;

        // Positions star and directs it along a sloped path
		star.setCoordinates((float) (Math.random()*(screenWidth-marginHorz)+marginHorz/2), (float) (Math.random()*(screenHeight-marginVert)+marginVert/2));
		star.setDirection((int) (star.getX() - screenWidth/2), (int) (star.getY() - screenHeight/2));
		star.setRandomFrame();
	}
	
	@Override
	public void draw(Canvas canvas) {
		// Background
		canvas.drawColor(backgroundColour);
		
		if (earth.isVisible())
			earth.draw(canvas);
		
		// Debug info
		super.draw(canvas);
		
		// Background stars
		for (Star star : stars)
			star.draw(canvas);
		
		// Pink enemy circles
		for (Enemy enemy : enemies) {
            if (enemy.isVisible()) {
                enemy.draw(canvas);
            }
        }
		
		// Laser
		if (laser.isVisible())
			laser.draw(canvas);

        // Gunner canon
        if (gunner.isVisible())
            gunner.draw(canvas);
		
		// Explosion
		if (explosion.isVisible())
			explosion.draw(canvas);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			shoot();
		
			super.onTouch(v, event);
		
			switch (timingResult) { 
			case Beats.RESULT_GOOD:
				currentEnemy.setShotType(Enemy.SHOT_MAJOR_HIT);
				explosion.setVisible(true);
				debugTimingResult = "Good";
				break;
			case Beats.RESULT_BAD:
				currentEnemy.setShotType(Enemy.SHOT_MINOR_HIT);
				explosion.setVisible(true);
				debugTimingResult = "Bad";
				break;
			case Beats.RESULT_WAY_OFF:
				debugTimingResult = "Way off";
				break;
			case Beats.RESULT_NO_REMAINING_BEATS:
				debugTimingResult = "終わり";
				break;
			}
		}
		return false;
	}
	
	@Override
	public void onPause() {
		// Frees up memory
		soundPool.release();
		soundPool = null;
		super.onPause();
	}

	@Override
	public void onResume() {
		// Reloads audio clips
		initialiseAudio();
		super.onResume();
	}
}

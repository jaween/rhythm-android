package com.ween.shooter;

import android.content.Context;

public class ShooterEnemy extends Entity {
	
	public static final int SHOT_NOT_HIT = 0;	// No injury
	public static final int SHOT_MAJOR_HIT = 1;	// Destroyed on the spot
	public static final int SHOT_MINOR_HIT = 2;	// Destroyed after falling
	private int shotType = SHOT_NOT_HIT;	
	
	ShooterEnemy(Context context) {
		super(context);
		
		// First static sprite (enemy in good condition, not shot)
		int drawableID = R.drawable.enemy;
		int width = 30;
		int height = 32;
		int frames = 1;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		currentSprite.setAnimated(false);
		
		// Second static sprite (enemy in bad condition)
		/*int drawableID = R.drawable.enemy;
		int width = 30;
		int height = 32;
		int frames = 1;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		currentSprite.setAnimated(false);*/
		
		// Trail in from top left
		drawableID = R.drawable.trail_from_top_left_strip;
		width = 53;
		height = 69;
		frames = 2;
		looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		
		currentSprite = sprites.get(0);
		
		
	}
	
	public void setShotType(int shotType) {
		this.shotType = shotType;
		switch (shotType) {
			case SHOT_MAJOR_HIT: 
			currentSprite = sprites.get(1);
			break;
			case SHOT_MINOR_HIT:
			visible = false;
			break;
		}
	}
	
	public int getShotType() {
		return shotType;
	}
}

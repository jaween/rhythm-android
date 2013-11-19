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
	}
	
	public void setShotType(int shotType) {
		this.shotType = shotType;
	}
	
	public int getShotType() {
		return shotType;
	}
}

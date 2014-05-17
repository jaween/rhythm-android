package com.ween.rhythm.shooter;

import android.content.Context;

import com.ween.rhythm.Entity;
import com.ween.shooter.R;

/**
 * Laser which the player shoots at the enemies
 */

public class Laser extends Entity {
	
	Laser(Context context) {
		super(context);
		
		int drawableID = R.drawable.laser_strip;
		int width = 15;
		int height = 173;
		int frames = 8;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
	}
}

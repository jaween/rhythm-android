package com.ween.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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

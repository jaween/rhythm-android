package com.ween.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Laser which the player shoots at the enemies
 */

public class Laser extends Sprite {
	
	Laser(Context context) {
		super(context);
		
		numberOfFrames = 8;
		width = 15;
		height = 173;
		loadSpriteSheet(R.drawable.laser_strip, width, height);
	}
}

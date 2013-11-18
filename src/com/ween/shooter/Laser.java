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
		
		width = 44;
		height = 89;
		numberOfFrames = 8;
		loadSpriteSheet(R.drawable.rapper, width, height);
	}
}

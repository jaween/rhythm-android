package com.ween.rhythm.shooter;

import android.content.Context;

import com.ween.rhythm.Entity;
import com.ween.rhythm.R;

public class Explosion extends Entity {

	Explosion(Context context) {
		super(context);
		
		int drawableID = R.drawable.explosion;
		int width = 103;
		int height = 85;
		int frames = 1;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		currentSprite.setSpriteSpeed(0.1f);
	}

}

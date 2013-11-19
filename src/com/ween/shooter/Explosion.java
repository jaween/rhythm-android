package com.ween.shooter;

import android.content.Context;

public class Explosion extends Entity {

	Explosion(Context context) {
		super(context);
		
		int drawableID = R.drawable.explosion_strip;
		int width = 89;
		int height = 88;
		int frames = 5;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
	}

}

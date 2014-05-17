package com.ween.shooter;

import android.content.Context;

public class Earth extends Entity {

	Earth(Context context) {
		super(context);
		
		int drawableID = R.drawable.earth;
		int width = 86;
		int height = 85;
		int frames = 1;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		
		currentSprite.setAnimated(false);
	}

}

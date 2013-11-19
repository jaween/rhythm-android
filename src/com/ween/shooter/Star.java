package com.ween.shooter;

import android.content.Context;

public class Star extends Entity {

	private float[] direction = { 0, 0 };
	private int speed;
	
	Star(Context context) {
		super(context);
		
		int drawableID = R.drawable.stars_strip;
		int width = 6;
		int height = 6;
		int frames = 3;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		
		float minSpeed = 0.02f;
		currentSprite.setSpriteSpeed((float) (Math.random() * minSpeed + minSpeed));
	}

	public void setDirection(int xstep, int ystep) {
		// Direction is the unit vector from the center of the screen to the star
		float norm = (float) (Math.sqrt(xstep*xstep + ystep*ystep));
		direction[0] = xstep/norm;
		direction[1] = ystep/norm;
	}

	@Override
	public void update() {
		float speed = 1; //currentSprite.getSpriteSpeed();
		setCoordinates(x + direction[0]*speed, y + direction[1]*speed);
		super.update();
	}
}

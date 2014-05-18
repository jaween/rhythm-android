package com.ween.rhythm.shooter;

import android.content.Context;

import com.ween.rhythm.Entity;
import com.ween.rhythm.R;

public class Star extends Entity {

	private float[] direction = { 0, 0 };
	private float speed;
	
	Star(Context context) {
		super(context);
		
		int drawableID = R.drawable.stars_strip;
		int width = 6;
		int height = 6;
		int frames = 3;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		
		currentSprite.setAnimated(false);
		
		setRandomFrame();
		setRandomSpeed();
	}

	public void setDirection(int xstep, int ystep) {
		// Direction is the unit vector from the center of the screen to the star
		float norm = (float) (Math.sqrt(xstep*xstep + ystep*ystep));
		direction[0] = xstep/norm;
		direction[1] = ystep/norm;
	}
	
	public void setRandomFrame() {
		int frame = (int) (Math.random() * currentSprite.getFrames());
		currentSprite.setFrame(frame);
	}
	
	public void setRandomSpeed() {
		int averageSpeed = 1; // This isn't really average speed, but can't I think of a better name
		float minSpeed = 0.5f;
		speed = (float) (Math.random() * averageSpeed) + minSpeed;
	}
	
	@Override
	public void update() {
		setCoordinates(x + direction[0]*speed, y + direction[1]*speed);
		super.update();
	}
}

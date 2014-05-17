package com.ween.shooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Simple graphics that can be animated
 * 
 * In order to display, sprites must do the following:
 *    Inherit from Sprite (this class)
 *    Load a sprite sheet (typically in constructor of subclass)
 *    Have their coordinates set (setCoordinates() function)
 *    Have their visibility set to true (setVisibility() function)
 */

public class Sprite {
	
	// Dimension variables
	protected int width;
	protected int height;
	
	// Display variables
	protected Bitmap spriteSheet;
	protected Rect sourceRect;
	protected Paint paint;
	protected int scale = 3;
	
	// Animation variables
	protected boolean animated = true;
	protected float spriteSpeed = 0.3f;
	protected float frameIndexFloat = 0;
	protected int frameIndex = 0;
	protected int numberOfFrames = 1;
	protected boolean looping = false;
	
	Sprite(Bitmap spriteSheet, Rect sourceRect, int frames, boolean looping) {
		this.spriteSheet = spriteSheet;
		this.sourceRect = sourceRect;
		this.numberOfFrames = frames;
		this.looping = looping;
		
		width = sourceRect.width();
		height = sourceRect.height();
		
		paint = new Paint();
	}
	
	// Returns the scaled width (useful for layout)
	public int getWidth() {
		return width*scale;
	}
	
	// Returns the scaled height (useful for layout)
	public int getHeight() {
		return height*scale;
	}
	
	protected void setScale(int scale) {
		this.scale = scale;
	}
	
	public int getScale() {
		return scale;
	}
	
	public void setSpriteSpeed(float speed) {
		this.spriteSpeed = speed;
	}
	
	public float getSpriteSpeed() {
		return spriteSpeed;
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}
	
	public boolean getAnimated() {
		return animated;
	}
	// Returns true if there are more frames to display (if looping, always returns true)
	protected boolean nextFrame() {
		// Static images should be visible unless otherwise stated
		if (!animated)
			return true;
			
		// Slower animations are achieved using a frameIndexFloat that is incremented slowly
		if (frameIndex <= numberOfFrames - 1) {
			frameIndexFloat += spriteSpeed;
			frameIndex = (int) frameIndexFloat;
			sourceRect.set(frameIndex*width, 0, (frameIndex+1)*width, height);
		} else {
			restartAnimation();
			sourceRect.set(frameIndex*width, 0, (frameIndex+1)*width, height);
			
			if (!looping) 
				return false;
		}
		return true;
	}
	
	public void restartAnimation() {
		frameIndexFloat = 0;
		frameIndex = 0;
	}
	
	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	public void draw(Canvas canvas, Rect destRect) {
		canvas.drawBitmap(spriteSheet, sourceRect, destRect, paint);
	}
}

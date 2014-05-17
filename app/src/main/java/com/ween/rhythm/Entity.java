package com.ween.rhythm;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ween.rhythm.Sprite;

/**
 * Game objects, visible or not (enemies, lasers, etc.)
 */

public class Entity {
	
	// Position variables
	protected float x;
	protected float y;
	protected Rect destRect;
	
	// Display variables
	protected boolean visible = false;
	protected ArrayList<Sprite> sprites;
	protected Sprite currentSprite;
	
	// Used to access drawables
	protected Context context;
	
	public Entity(Context context) {
		this.context = context;
		destRect = new Rect();
		sprites = new ArrayList<Sprite>(1);
	}
	
	public void setCoordinates(float x, float y) {
		this.x = x;
		this.y = y;
		
		// Updates the rectangle within which the sprite will be drawn (also upscales sprite)
		destRect.set((int) x, (int) y, (int) (x + currentSprite.getWidth()), (int) (y + currentSprite.getHeight()));
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	// Returns the width of the current sprite
	public int getWidth() {
		return currentSprite.getWidth();
	}
	
	// Returns the height of the current sprite
	public int getHeight() {
		return currentSprite.getHeight();
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	protected void setScale(int scale) {
		currentSprite.setScale(scale);
	}
	
	public int getScale() {
		return currentSprite.getScale();
	}
	
	public void startAnimation() {
		currentSprite.setFrame(0);
	}
	
	// Loads a drawable (animated or not) and assigns it to a Sprite
	protected int loadSpriteSheet(int drawableID, int width, int height, int frames, boolean looping) {
		// Creates the sprite instance
		Drawable drawable = context.getResources().getDrawable(drawableID);
		Bitmap spriteSheet = ((BitmapDrawable) drawable).getBitmap();
		Rect sourceRect = new Rect(0, 0, width, height);
		Sprite sprite = new Sprite(spriteSheet, sourceRect, frames, looping);
		currentSprite = sprite;
		sprites.add(currentSprite);
		
		// Returns the index of the sprite for reference (rather than using the ArrayList)
		return sprites.size() - 1;
	}
	
	public void update() {

	}
	
	public void draw(Canvas canvas) {
		if (visible)
			currentSprite.draw(canvas, destRect);
		
		boolean moreFrames = currentSprite.nextFrame();
		
		if (!moreFrames) 
			visible = false;
	}
}

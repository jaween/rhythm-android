package com.ween.shooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Sprite {
	
	// Position variables
	protected float x;
	protected float y;
	protected int width;
	protected int height;
	protected Rect destRect;
	
	// Sprite variables
	protected static Bitmap spriteSheet;
	protected boolean visible = false;
	protected Rect sourceRect;
	protected Paint paint;
	protected int scale = 3;
	
	// Animation variables
	protected float spriteSpeed = 0.2f;
	protected float frameIndexFloat;
	protected int frameIndex;
	protected int numberOfFrames;

	// Used to access drawables
	protected Context context;
	
	Sprite(Context context) {
		this.context = context;
		paint = new Paint();
		destRect = new Rect();
	}
	
	public void setCoordinates(float x, float y) {
		this.x = x;
		this.y = y;
		
		// Updates the rectangle within which the sprite will be drawn (also upscales sprite)
		destRect.set((int) x, (int) y, (int) (x + width*scale), (int) (y + height*scale));
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	protected void setScale(int scale) {
		this.scale = scale;
	}
	
	protected void loadSpriteSheet(int drawableID, int width, int height) {
		Drawable drawable = context.getResources().getDrawable(drawableID);
		spriteSheet = ((BitmapDrawable) drawable).getBitmap();
		sourceRect = new Rect(0, 0, width, height);
	}
	
	protected void nextFrame() {
		// Slower animations are achieved using a frameIndexFloat that is incremented slowly
		if (frameIndex < numberOfFrames - 1) {
			frameIndexFloat += spriteSpeed;
		} else {
			//visible = false;
			frameIndexFloat = 0;
			frameIndex = 0;
			return;
		}
		frameIndex = (int) frameIndexFloat;
		sourceRect.set(frameIndex*width, 0, (frameIndex+1)*width, height);
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(spriteSheet, sourceRect, destRect, paint);
	}
}
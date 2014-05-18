package com.ween.rhythm.shooter;

import android.content.Context;
import android.graphics.Canvas;

import com.ween.rhythm.Entity;
import com.ween.rhythm.R;

/**
 * Created by ween on 5/18/14.
 */
public class Gunner extends Entity {

    private GunnerFace gunnerFace;
    private GunnerCanon gunnerCanon;

    private static final int FRAMES_PER_ANIMATION = 10;
    private int remainingAnimationFrames = 0;
    private float xBeforeAnimation;

    public Gunner(Context context) {
        super(context);

        int drawableId = R.drawable.gunner_body;
        int width = 192;
        int height = 32;
        int frames = 1;
        boolean looping = false;
        loadSpriteSheet(drawableId, width, height, frames, looping);

        gunnerCanon = new GunnerCanon(context);
        gunnerFace = new GunnerFace(context);

        currentSprite.setAnimated(false);
    }

    @Override
    public void draw(Canvas canvas) {
        gunnerCanon.draw(canvas);
        super.draw(canvas);
        gunnerFace.draw(canvas);

        if (remainingAnimationFrames > 0) {
            // Amount to move by
            float remainingFramesRatio = (float) remainingAnimationFrames / (float) FRAMES_PER_ANIMATION;
            double angle = remainingFramesRatio * (2 * Math.PI); //
            
            setCoordinates(xBeforeAnimation + ((float) Math.sin(angle)) * currentSprite.getScale() * 3, y);
            remainingAnimationFrames--;

            // Return to starting postition
            if (remainingAnimationFrames == 0)
                setCoordinates(xBeforeAnimation, y);
        }
    }

    @Override
    public void setCoordinates(float x, float y) {
        super.setCoordinates(x, y);
        gunnerCanon.setCoordinates(x + currentSprite.getWidth()/2 - gunnerCanon.getWidth()/2, y + getHeight() - gunnerCanon.getHeight());
        gunnerFace.setCoordinates(x + currentSprite.getWidth()/2 - gunnerFace.getWidth()/2, y + getHeight()/2 - gunnerFace.getWidth()/2);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        gunnerCanon.setVisible(visible);
        gunnerFace.setVisible(visible);
    }

    public void shoot() {
        gunnerCanon.startAnimation();
    }

    @Override
    public void startAnimation() {
        super.startAnimation();
        gunnerFace.startAnimation();

        // Keep the a single starting point even on successive misses
        if (remainingAnimationFrames == 0)
            xBeforeAnimation = x;

        remainingAnimationFrames = FRAMES_PER_ANIMATION;
    }
}

package com.ween.rhythm.shooter;

import android.content.Context;
import android.graphics.Canvas;

import com.ween.rhythm.Entity;
import com.ween.rhythm.R;

/**
 * Created by ween on 5/19/14.
 */
public class GunnerFace extends Entity {

    private boolean animating = false;

    public GunnerFace(Context context) {
        super(context);

        int drawableID = R.drawable.gunner_face;
        int width = 24;  // TODO Fix
        int height = 24; // TODO Fix
        int frames = 2;
        boolean looping = false;
        loadSpriteSheet(drawableID, width, height, frames, looping);

        currentSprite.setAnimated(true);
        currentSprite.setSpriteSpeed(0.05f);
    }

    @Override
    public void draw(Canvas canvas) {
        currentSprite.draw(canvas, destRect);

        if (animating) {
            boolean moreFrames = currentSprite.nextFrame();
            if (!moreFrames)
                animating = false;
        }
    }

    @Override
    public void startAnimation() {
        super.startAnimation();

        // Stays on the sad face frame if we're hit WHILE the sad face frame is already being shown
        currentSprite.setFrame(1);
        animating = true;
    }
}

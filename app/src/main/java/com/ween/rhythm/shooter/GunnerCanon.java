package com.ween.rhythm.shooter;

import android.content.Context;
import android.graphics.Canvas;

import com.ween.rhythm.Entity;
import com.ween.rhythm.R;

/**
 * Created by ween on 5/18/14.
 */
public class GunnerCanon extends Entity {

    private boolean animating = false;

    public GunnerCanon(Context context) {
        super(context);

        int drawableId = R.drawable.gunner_canon;
        int width = 72;
        int height = 54;
        int frames = 5;
        boolean looping = false;
        loadSpriteSheet(drawableId, width, height, frames, looping);

        currentSprite.setAnimated(true);
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
        animating = true;
    }
}

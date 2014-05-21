package com.ween.rhythm.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.ween.rhythm.Entity;
import com.ween.rhythm.R;

/**
 * Created by ween on 5/19/14.
 */
public class EnemyMovementTrail extends Entity{

    private static final int VISIBILITY_TIME = 20;
    private int visibilityTimer = 0;

    public enum TrailType {
        APPEARING, DESTROYING, ATTACKING;
    }

    public EnemyMovementTrail(Context context) {
        super(context);

        // TODO Fix these properties
        int drawableID = R.drawable.trails;
        int width = 53;
        int height = 69;
        int frames = 1;
        boolean looping = false;
        loadSpriteSheet(drawableID, width, height, frames, looping);
    }

    public void setQuadrant(Enemy.Quadrant quadrant, int parentWidth, int parentHeight) {
        if (visible)
            Log.e("Trail", "Is already visible, but now setting to " + quadrant);

        // Makes the trail appear to be along the path that the enemy came from
        switch (quadrant) {
            case TOP_LEFT:
                currentSprite.setFrame(0);
                setCoordinates(x - getWidth(), y - getHeight());
                break;
            case TOP_LEFT_INNER:
                currentSprite.setFrame(3);
                setCoordinates(x - getWidth(), y - getHeight());
                break;
            case TOP_RIGHT:
                currentSprite.setFrame(6);
                setCoordinates(x + parentWidth, y - getHeight());
                break;
            case TOP_RIGHT_INNER:
                currentSprite.setFrame(9);
                setCoordinates(x + parentWidth, y - getHeight());
                break;
            case BOTTOM_LEFT:
                currentSprite.setFrame(12);
                setCoordinates(x - getWidth(), y + parentHeight);
                break;
            case BOTTOM_LEFT_INNER:
                currentSprite.setFrame(15);
                setCoordinates(x - getWidth(), y + parentHeight);
                break;
            case BOTTOM_RIGHT:
                currentSprite.setFrame(18);
                setCoordinates(x + parentWidth, y + parentHeight);
                break;
            case BOTTOM_RIGHT_INNER:
                currentSprite.setFrame(21);
                setCoordinates(x + parentWidth, y + parentHeight);
                break;
            case CENTER:
                currentSprite.setFrame(24);
                setCoordinates(x + parentWidth/2 - getWidth()/2, y - getHeight());
                break;
        }
    }

    public void setTrailType(TrailType type) {
        switch (type) {
            case APPEARING:
                break;
            case DESTROYING:
                currentSprite.setFrame(currentSprite.getFrame() + 1);
                break;
            case ATTACKING:
                currentSprite.setFrame(currentSprite.getFrame() + 2);
                break;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visible && visibilityTimer > 0)
            Log.e("Trail", "Someone reset trail timer, from " + visibilityTimer);
        visibilityTimer = VISIBILITY_TIME;
        super.setVisible(visible);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Trails appear only for a couple frames
        visibilityTimer--;
        Log.d("Trail", "Frames remaining is " + visibilityTimer);
        if (visibilityTimer <= 0) {
            visible = false;
            Log.e("Trail", "Timer has finished");
        }
    }
}

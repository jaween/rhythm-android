package com.ween.rhythm.shooter;

import android.content.Context;

import com.ween.rhythm.Entity;
import com.ween.rhythm.R;

/**
 * Created by ween on 5/19/14.
 */
public class EnemyMovementTrail extends Entity{

    public EnemyMovementTrail(Context context) {
        super(context);

        // TODO Fix these properties
        int drawableID = 0;//R.drawable.enemy_movement_trail;
        int width = 30;
        int height = 32;
        int frames = 2;
        boolean looping = false;
        loadSpriteSheet(drawableID, width, height, frames, looping);
        currentSprite.setAnimated(true);
    }

    public void setPosition(Enemy.Position position) {
        switch (position) {
            case TOP_LEFT:
                currentSprite.setFrame(0);
                break;
            case TOP_LEFT_INNER:
                currentSprite.setFrame(2);
                break;
            case TOP_RIGHT:
                currentSprite.setFrame(4);
                break;
            case TOP_RIGHT_INNER:
                currentSprite.setFrame(6);
                break;
            case BOTTOM_LEFT:
                currentSprite.setFrame(8);
                break;
            case BOTTOM_LEFT_INNER:
                currentSprite.setFrame(10);
                break;
            case BOTTOM_RIGHT:
                currentSprite.setFrame(12);
                break;
            case BOTTOM_RIGHT_INNER:
                currentSprite.setFrame(14);
                break;
            case CENTER:
                currentSprite.setFrame(16);
                break;
        }
    }
}

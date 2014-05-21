package com.ween.rhythm.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.ween.rhythm.Entity;
import com.ween.rhythm.R;

public class Enemy extends Entity {
	
	public static final int SHOT_NOT_HIT = 0;	// No injury
	public static final int SHOT_MAJOR_HIT = 1;	// Destroyed on the spot
	public static final int SHOT_MINOR_HIT = 2;	// Destroyed after falling
	private int shotType = SHOT_NOT_HIT;

    // Determines the sprite for the trail
    public enum Quadrant {
        TOP_LEFT, TOP_LEFT_INNER, TOP_RIGHT, TOP_RIGHT_INNER,
        BOTTOM_LEFT, BOTTOM_LEFT_INNER, BOTTOM_RIGHT, BOTTOM_RIGHT_INNER,
        CENTER
    }
    private Quadrant quadrant;

    private EnemyMovementTrail movementTrail;

    private static final int FRAMES_PER_MOVEMENT = 16;
    private int framesUntilNextMovement = FRAMES_PER_MOVEMENT;
	
	Enemy(Context context) {
		super(context);
		
		// First static sprite (enemy in good condition, not shot)
		int drawableID = R.drawable.enemy;
		int width = 30;
		int height = 32;
		int frames = 1;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		currentSprite.setAnimated(false);

        movementTrail = new EnemyMovementTrail(context);
		
		// Second static sprite (enemy in bad condition)
		/*int drawableID = R.drawable.enemy;
		int width = 30;
		int height = 32;
		int frames = 1;
		boolean looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);
		currentSprite.setAnimated(false);*/
		
		/*// Trail in from top left
		drawableID = R.drawable.trail_from_top_left_strip;
		width = 53;
		height = 69;
		frames = 2;
		looping = false;
		loadSpriteSheet(drawableID, width, height, frames, looping);*/
		
		currentSprite = sprites.get(0);
	}
	
	public void setShotType(int shotType) {
		this.shotType = shotType;
		switch (shotType) {
			case SHOT_MAJOR_HIT: 
			    //currentSprite = sprites
                movementTrail.setTrailType(EnemyMovementTrail.TrailType.DESTROYING);
                movementTrail.setVisible(true);
			    visible = false;
			    break;
			case SHOT_MINOR_HIT:
			    visible = false;
                movementTrail.setTrailType(EnemyMovementTrail.TrailType.DESTROYING);
                movementTrail.setVisible(true);

                /*if (quadrant == any left)
                    // Do stuff with rightward momentum
                else if (quadrant == any right)
                    // Do stuff with leftward momentum
                else
                    // Do stuff with downward momentum
                }*/
			    break;
			case SHOT_NOT_HIT: 
			    //currentSprite = sprites.get(0);
                movementTrail.setTrailType(EnemyMovementTrail.TrailType.APPEARING);
			    break;
		}
	}

    public void attack() {
        movementTrail.setTrailType(EnemyMovementTrail.TrailType.ATTACKING);
        movementTrail.setVisible(true);
    }
	
	public int getShotType() {
		return shotType;
	}

    public void setQuadrant(Quadrant quadrant) {
        movementTrail.setQuadrant(quadrant, getWidth(), getHeight());
        this.quadrant = quadrant;
    }

    private void moveDownward() {
        super.setCoordinates(x, y + currentSprite.getScale() * 1.5f);
    }

    @Override
    public void setCoordinates(float x, float y) {
        super.setCoordinates(x, y);
        movementTrail.setCoordinates(x, y);
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visible && visible)
            Log.e("Enemy", "Enemy is already visible!");
        super.setVisible(visible);

        // The enemy shouldn't hide the trail, but if the enemy becomes visible, the trail should too
        if (visible)
            movementTrail.setVisible(visible);
    }

    @Override
    public void draw(Canvas canvas) {
        // Trail can still be drawn even if the enemy is invisible
        if (movementTrail.isVisible()) {
            movementTrail.draw(canvas);
            if (!visible)
                Log.d("Enemy", "Trail drawing when enemy invisible");
        }

        if (visible)
            super.draw(canvas);

        // After a few frames the enemy edges downward
        framesUntilNextMovement--;
        if (framesUntilNextMovement <= 0) {
            moveDownward();
            framesUntilNextMovement = FRAMES_PER_MOVEMENT;
        }
    }
}

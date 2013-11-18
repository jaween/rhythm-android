package com.ween.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class ShooterEnemy extends Sprite {
	
	ShooterEnemy(Context context) {
		super(context);
		
		width = 30;
		height = 32;
		loadSpriteSheet(R.drawable.enemy, width, height);
	}
}

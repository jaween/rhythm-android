package com.ween.shooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class ShooterEnemy extends Sprite {
	
	ShooterEnemy(Context context) {
		super(context);
		
		paint.setAntiAlias(true);
		paint.setColor(Color.MAGENTA);
		paint.setAlpha(70);
		
		x = ((float) Math.random()*600);
		y = ((float) Math.random()*800);
	}
	
	@Override
	public void draw(Canvas canvas) {
		float radius = 60f;
		canvas.drawCircle(x, y, radius, paint);
	}
}

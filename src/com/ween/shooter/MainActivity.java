package com.ween.shooter;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity implements OnTouchListener {

	private MainSurface mainSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mainSurface = new MainSurface(getApplicationContext());
		mainSurface.setOnTouchListener(this);
		
		setContentView(mainSurface);
	}

	@Override
	protected void onPause() {
		mainSurface.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mainSurface.onResume();
		super.onResume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mainSurface.touched(v, event);
		return false;
	}
}

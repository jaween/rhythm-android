package com.ween.rhythm;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public class MainActivity extends Activity implements OnTouchListener {

	private MainSurface mainSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Display info used when drawing (dimensions, dp value, etc.)
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		// Hides ActionBar (TODO Disable in Manifest for all supported SDK versions instead)
		requestWindowFeature(Window.FEATURE_NO_TITLE);


		mainSurface = new MainSurface(getApplicationContext(), metrics);
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
		return mainSurface.touched(v, event);
	}
}

package com.ween.shooter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	private MainSurface mainSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mainSurface = new MainSurface(getApplicationContext());
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
	
	

}

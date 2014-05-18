package com.ween.rhythm;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public class MainActivity extends ActionBarActivity implements OnTouchListener {

	private MainSurface mainSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Hides (Support) ActionBar on versions older than Honeycomb
		if (Build.VERSION.SDK_INT < 11)
            getSupportActionBar().hide();
	}

	@Override
	protected void onPause() {
		mainSurface.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mainSurface.touched(v, event);
	}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Display info used when drawing (dimensions, dp value, etc.)
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // To get the height of the screen minus the navigation bar and status bar
        // we must wait until after a layout, onWindowFocusChanged() is called
        // "when your Activity is just about visible to the user", according to this
        // http://stackoverflow.com/questions/5886615/display-metrics-minus-status-bar
        View content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mainSurface = new MainSurface(getApplicationContext(), metrics, content.getHeight());
        mainSurface.setOnTouchListener(this);

        setContentView(mainSurface);
        mainSurface.onResume();
    }
}

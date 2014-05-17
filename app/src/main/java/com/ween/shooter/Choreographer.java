package com.ween.shooter;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Manages music, tracking score, instances, input, player actions, various events, etc.
 * Levels must inherit from this class 
 */

public abstract class Choreographer implements Beats.RhythmEvent {
	
	// Debug variables
	private int timingCircleColour = Color.BLUE;
	protected String debugTimingResult = ""; // Text on screen ('Miss', etc.)
	
	// Game variables
	protected int score = 0;
	
	// Gameplay timing variables
	protected Beats eventBeats;				// E.g. pea being flicked at player (automatic event)
	protected Beats playerBeats;			// E.g. fork stabbing pea by player (player's action)
	
	// Implementation timing variables
	protected int eventIndex = -1;			// Events are triggered by looking at this index
	private Queue<Long> timers;				// The remaining time between the musical 'beat' and the accepted leeway
	private long beginTime;	
	private long lastDrawTime;				// Determines when to draw the next frame
	protected int timingResult;

	// Screen information
	protected float dp;
	protected int screenWidth;
	protected int screenHeight;
	
	// Audio variables
	protected SoundPool soundPool;			// Sound effects
	protected MediaPlayer mediaPlayer;		// Background music
	protected AudioManager audioManager;	// System audio info (volumes, etc.)
	
	// Grants access to system services
	protected Context context;
	
	// Paints
	private Paint debugPaint = new Paint();
	private Paint debugTextPaint = new Paint();
	
	
	Choreographer(Context context, DisplayMetrics metrics, String playerBeatsFilename, String eventBeatsFilename) {
		this.context = context;
		this.eventBeats = eventBeats;
		this.playerBeats = playerBeats;
		
		dp = metrics.density;
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		loadBeats(playerBeatsFilename, eventBeatsFilename);
		
		// Used to get system volume, etc.
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		// Used when determining if the player was on beat
		timers = new LinkedList<Long>();
		
		
		// Callback to be notified of beats (event triggers, e.g. enemies appearing on beat)
		eventBeats.setRhythmEvent(this);
		
		initialisePaints();
	}
	
	private void initialisePaints() {
		// Debug Paints
		debugPaint.setAntiAlias(true);
		debugTextPaint.setColor(Color.WHITE);
		debugTextPaint.setAntiAlias(true);
		debugTextPaint.setTextSize(40);
		debugTextPaint.setTextAlign(Align.LEFT);
	}
	
	private void loadBeats(String playerBeatsFilename, String eventBeatsFilename) {
		// Loads in the timing data
		Parser parser = new Parser(context);
		playerBeats = new Beats(parser.parse(playerBeatsFilename));
		eventBeats = new Beats(parser.parse(eventBeatsFilename));
	}
	
	@Override
	public void nextEvent() {
		// When the player must react to an event the event will be delayed 
		// until the end of the range of accepted on beat times (i.e. the actual beat plus the leeway)
		// Thus if the player misses the beat, the event is triggered afterward (e.g. enemy attacking the player)
		// but if the player reacts within the accepted range, the event is triggered BY THE PLAYER
		
		if (playerBeats.getCurrentBeat() == eventBeats.getCurrentBeat()) {
			// We achieve the delay using a queue of timers
			timers.add(eventBeats.getCurrentBeat() + eventBeats.LEEWAY + eventBeats.MISSED);
		} else {
			eventIndex++;
		}
	}
	
	protected abstract void animate();
	
	public void update(long time) {
		if (!timers.isEmpty()) {
			if (System.currentTimeMillis() - beginTime >= timers.peek())
			{
				// Event has been delayed, remove timer
				timers.remove();
				
				// We can now trigger the delayed event (e.g. enemy attacking player)
				nextEvent();
			}
		}
		
		peekState();
		animate();
	}
	
	public void draw(Canvas canvas) {
		// Center orange circle
		debugPaint.setColor(timingCircleColour);
		canvas.drawCircle(screenWidth/2, 400, 100, debugPaint);
		
		// Timer
		String time = Float.toString(((float) (System.currentTimeMillis() - beginTime))/1000f);
		canvas.drawText(time, screenWidth/2 - 55, 550, debugTextPaint);
		canvas.drawText(debugTimingResult, screenWidth/2 - 55, 600, debugTextPaint);
		
		// FPS Indicator
		canvas.drawText((1000/(System.currentTimeMillis() - lastDrawTime)) + " FPS", 30, 60, debugTextPaint);
		lastDrawTime = System.currentTimeMillis();
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Rework this code, too untidy
		int result;
		long timeOfTouch = System.currentTimeMillis() - beginTime;
		Log.d("Choreo", "Curret event beat is " + eventBeats.getCurrentBeat() + "(" + timeOfTouch + "), there are " + timers.size() + " timers");
		if (!timers.isEmpty()) {
			long timerValue = timers.remove();
			if (timeOfTouch < timerValue - Beats.MISSED)
				result = Beats.RESULT_GOOD;
			else if (timeOfTouch < timerValue)
				result = Beats.RESULT_BAD;
			else
				result = Beats.RESULT_MISS;
			Log.d("Choreo", "Result is " + result);
			eventIndex++;
			eventBeats.pollSuccess(timeOfTouch);
			playerBeats.pollSuccess(timeOfTouch);
		} else {
			Log.d("Choreo", "There are no timers");
			long prePollPlayerBeat = playerBeats.getCurrentBeat();
			result = playerBeats.pollSuccess(timeOfTouch);
			if (result == Beats.RESULT_GOOD || result == Beats.RESULT_BAD) {
				eventIndex++;
				if (prePollPlayerBeat == eventBeats.getCurrentBeat())
					eventBeats.pollSuccess(timeOfTouch);
			}
		}
		timingResult = result;
		return false;	
	}
	
	private void peekState() {	
		eventBeats.peekSuccess(System.currentTimeMillis() - beginTime);
		int result = playerBeats.peekSuccess(System.currentTimeMillis() - beginTime);
		switch (result) {
		case Beats.RESULT_GOOD:
			timingCircleColour = 0xFFFF9900;
			break;
		case Beats.RESULT_BAD:
			timingCircleColour = 0x44FF9900;
			break;
		case Beats.RESULT_WAY_OFF:
			timingCircleColour = 0x11FF9900;
			break;
		case Beats.RESULT_MISS:
			debugTimingResult = "Miss";
			break;
		case Beats.RESULT_NO_REMAINING_BEATS:
			timingCircleColour = 0x11993366;
			debugTimingResult = "終わり";
			break;
		}
	}
	
	protected void playSound(int id) {
		float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		soundPool.play(id, streamVolume, streamVolume, 1, 0, 1f);
	}
	
	protected void loadBackgroundMusic(int resourceID) {
		mediaPlayer = MediaPlayer.create(context, resourceID);
	}
	
	// Called from parent's onPause
	public void onPause() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			/*mediaPlayer.release();
			mediaPlayer = null;*/
		}
	}
	
	// Called from parent's onResume
	public void onResume() {
		float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		if (mediaPlayer != null) {
			mediaPlayer.setVolume(streamVolume*2f, streamVolume*2f);
			mediaPlayer.start();
		}
		
		// All timings are relative to the this
		beginTime = System.currentTimeMillis();
		lastDrawTime = beginTime;
	}
}

package com.ween.rhythm;

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
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Manages music, tracking score, instances, input, player actions, various events, etc.
 * Levels must inherit from this class
 *
 * ISSUES
 *      If a timer is running future events will be delayed until the timer has finished
 *      or is destroyed, even if the subsequent event MUST occur on beat (e.g. an enemy appearing).
 *      This could be a problem between the end of a player's round of inputs and the beginning
 *      of the next round that starts with automatic events.
 */

public abstract class Choreographer implements Beats.RhythmEvent {
	
	// Game variables
	protected int score = 0;
	
	// Gameplay timing variables
	protected Beats eventBeats;				// e.g. pea being flicked at player (automatic event)
	protected Beats playerBeats;			// e.g. fork stabbing pea by player (player's action)
	
	// Implementation timing variables
	protected int eventIndex = -1;			// Events are triggered by looking at this index
	private Queue<Long> timers;				// The remaining time between the musical 'beat' and the leeway (a few milliseconds)
	protected long beginTime;
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

	protected Context context;
	
	// Paints
	private Paint debugPaint = new Paint();
	private Paint debugTextPaint = new Paint();

    // Debug variables
    private static final String TAG = "Choreographer";
    private int timingCircleColour = Color.BLUE;
    protected String debugTimingResult = ""; // On screen text ('Miss', etc.)
	
	public Choreographer(Context context, DisplayMetrics metrics, String playerBeatsFilename, String eventBeatsFilename) {
		this.context = context;
		
		dp = metrics.density;
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		loadBeats(playerBeatsFilename, eventBeatsFilename);

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
        Log.d(TAG, "Current player beat is " + playerBeats.getCurrentBeat() + ", current event beat is " + eventBeats.getCurrentBeat());
        Long playerBeat = playerBeats.getCurrentBeat();
        Long eventBeat = eventBeats.getCurrentBeat();
		if (playerBeat != null && eventBeat != null && playerBeat.longValue() <= eventBeat.longValue()) {
			// We achieve the delay using a queue of timers
            Log.d(TAG, "Timer added");
			timers.add(eventBeats.getCurrentBeat() + Beats.LEEWAY + Beats.MISSED);
		} else {
			eventIndex++;
		}
	}
	
	protected abstract void animate();
	
	public void update(long time) {
		if (!timers.isEmpty()) {
			if (SystemClock.elapsedRealtime() - beginTime >= timers.peek())
			{
                // TODO Crashed at condition with NullPointerException, why?
                // Just beore first enemy appeared


				// Event delay has finished, remove timer
				timers.remove();
                Log.d(TAG, "Timer finished");

                // Removes failed player event
                // If we passed in the current time, it would be considered 'way off' and wouldn't poll
                playerBeats.pollSuccess(playerBeats.getCurrentBeat());
				
				// We can now trigger the delayed event (e.g. enemy attacking player)
				//nextEvent();
                eventIndex++;
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
		String time = Float.toString(((float) (SystemClock.elapsedRealtime() - beginTime))/1000f);
		canvas.drawText(time, screenWidth/2 - 55, 550, debugTextPaint);
		canvas.drawText(debugTimingResult, screenWidth/2 - 55, 600, debugTextPaint);
		
		// FPS Indicator
		canvas.drawText((1000/(SystemClock.elapsedRealtime() - lastDrawTime)) + " FPS", 30, 60, debugTextPaint);
		lastDrawTime = SystemClock.elapsedRealtime();
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Rework this code, too untidy
		final int result;
		long timeOfTouch = SystemClock.elapsedRealtime() - beginTime;
		Log.d(TAG, "Touched! Current event beat is " + eventBeats.getCurrentBeat() + "(" + timeOfTouch + "), there are " + timers.size() + " timers");
		if (!timers.isEmpty()) {
			long timerValue = timers.remove();

			if (timeOfTouch < timerValue - Beats.MISSED)
				result = Beats.RESULT_GOOD;
			else if (timeOfTouch < timerValue)
				result = Beats.RESULT_BAD;
			else
				result = Beats.RESULT_MISS;
			Log.d(TAG, "Timer destroyed! Result is " + result);
			eventIndex++;

			playerBeats.pollSuccess(timeOfTouch);
		} else {
			Long prePollPlayerBeat = playerBeats.getCurrentBeat();
			result = playerBeats.pollSuccess(timeOfTouch);
			if (result == Beats.RESULT_GOOD || result == Beats.RESULT_BAD) {
				//eventIndex++;
				if (prePollPlayerBeat == eventBeats.getCurrentBeat()) {
                    eventBeats.pollSuccess(timeOfTouch);
                }
			}
		}
		timingResult = result;
		return false;	
	}

	private void peekState() {	
		eventBeats.peekSuccess(SystemClock.elapsedRealtime() - beginTime);

        // Purely used for debugging
		/*int result = playerBeats.peekSuccess(SystemClock.elapsedRealtime() - beginTime);
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
		}*/
	}
	
	protected void playSound(int id) {
		final float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		final float streamVolumeNormalised = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		soundPool.play(id, streamVolumeNormalised, streamVolumeNormalised, 1, 0, 1f);
	}

	protected void loadBackgroundMusic(int resourceID) {
		mediaPlayer = MediaPlayer.create(context, resourceID);
	}
	
	// Called from parent's onPause
	public void onPause() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
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
		beginTime = SystemClock.elapsedRealtime();
		lastDrawTime = beginTime;
	}
}

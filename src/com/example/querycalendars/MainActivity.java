package com.example.querycalendars;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	TextToSpeech mTts;
	private static int TTS_DATA_CHECK = 1;
	
	protected static final int RESULT_SPEECH = 1; 
    private ImageButton btnSpeak;
    
	public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        
        mTts=new TextToSpeech(getApplicationContext(), 
      	      new TextToSpeech.OnInitListener() {
      	      @Override
      	      public void onInit(int status) {
      	         if(status != TextToSpeech.ERROR){
      	             mTts.setLanguage(Locale.US);
      	            }
      	         else {
      	        	 Log.e("TTS","Fail");
      	         }
      	         }
      });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_SPEECH) {
        	if (resultCode == RESULT_OK && null != data) {
        		 
                ArrayList<String> text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(text.get(0).contains("get events")){
                	readAllEvents();
                }
                else if(text.get(0).contains("add event")){
                	addAnEvent();
                }
                else if(text.get(0).contains("tell events")){
                	tellAllEvents();
                }
        	}
        }else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
        	showToastMessage("Audio Error");
	    }else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
	    	showToastMessage("Client Error");
	    }else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
		   showToastMessage("Network Error");
	    }else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
	    	showToastMessage("No Match");
	    }else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
	    	showToastMessage("Server Error");
	    }
        super.onActivityResult(requestCode, resultCode, data);

    }
	
	void showToastMessage(String message){
		  Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		 }
	
	public void speakNow(View view){
		Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		           RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak any of the given options: \n1)Get events\t2)Add Event\t3)Tell Events"); 
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1); 
        

        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

	public void readEvents(View view) {
		readAllEvents();
	    }

	public void readAllEvents() {
		TextView eventResults;
		
		Context context = getApplicationContext();
		
		Cursor cursor = context.getContentResolver()
				.query(
						Uri.parse("content://com.android.calendar/events"),
	                    new String[] { "calendar_id", "title", "description",
							"dtstart", "dtend", "eventLocation" }, null,
	                        null, null);
	        	cursor.moveToFirst();
	        	// fetching calendars name
	        	String CNames[] = new String[cursor.getCount()];

	        	// fetching calendars id
	        	nameOfEvent.clear();
	        	startDates.clear();
	        	endDates.clear();
	        	descriptions.clear();
	        	
	        	for (int i = 0; i < CNames.length; i++) {

	            nameOfEvent.add(cursor.getString(1));
	            startDates.add(getDate(Long.parseLong(cursor.getString(3))));
	            endDates.add(getDate(Long.parseLong(cursor.getString(4))));
	            descriptions.add(cursor.getString(2));
	            CNames[i] = cursor.getString(1);
	            cursor.moveToNext();

	        	}
	        	eventResults = (TextView)findViewById(R.id.textView2);
	        	eventResults.setMovementMethod(new ScrollingMovementMethod());
	        	eventResults.setText("");
	        	for(int j=0; j<nameOfEvent.toArray().length; j++) {
	        		eventResults.setText(eventResults.getText() + (nameOfEvent.toArray()[j] + " " + "from" + " " + startDates.toArray()[j] + " " + "to" + " " + endDates.toArray()[j] + "\n\n"));
	        	}
	        	
	    }
	    public static String getDate(long milliSeconds) {
	        SimpleDateFormat formatter = new SimpleDateFormat(
	                "dd/MM/yyyy hh:mm a");
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(milliSeconds);
	        return formatter.format(calendar.getTime());
	    }
	    
	    public void listenEvents(View view){
	    	tellAllEvents();
	    }
	    
	    public void tellAllEvents(){
	    	readAllEvents();
	    	for(int k=0; k<nameOfEvent.toArray().length; k++) {
	    		mTts.speak((nameOfEvent.toArray()[k]+ " " + "from" + " " + startDates.toArray()[k] + " " + "to" + " " + endDates.toArray()[k] + "\n\n"), TextToSpeech.QUEUE_ADD, null);
        	}
	    }
	    
	    public void addEvents(View view) {
	    	addAnEvent();
	    }
	    
	    public void addAnEvent() {
	    	//Send Intent
        	Intent i = new Intent(this, EventActivity.class);
        	startActivity(i);
	    }
}

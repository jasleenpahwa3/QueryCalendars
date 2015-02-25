package com.example.querycalendars;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class EventActivity extends Activity{
	
	static boolean check=true;
	static TextView startDate;
	static TextView endDate;
	static TextView startTime;
	static TextView endTime;
	static int hour;
	static int minute;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
    }
    
    public void createEvent(View v){
    	EditText event = (EditText)findViewById(R.id.event);
    	EditText location = (EditText)findViewById(R.id.event_location);
    	EditText description = (EditText)findViewById(R.id.event_description);
    	            
    	Intent calIntent = new Intent(Intent.ACTION_EDIT);
    	calIntent.setType("vnd.android.cursor.item/event");     
    	calIntent.putExtra("title", event.getText().toString()); 
    	calIntent.putExtra("description", description.getText().toString());
    	calIntent.putExtra("location", location.getText().toString()); 
    	calIntent.putExtra("beginDate", startDate.getText().toString());
    	calIntent.putExtra("beginTime", startTime.getText().toString());
    	calIntent.putExtra("beginDate", endDate.getText().toString());
    	calIntent.putExtra("endTime", endTime.getText().toString());
    	 
    	startActivity(calIntent);
    }
    
    public void onClickDateTime(View v) {
    	    	
    	    	
    	startDate = (TextView)findViewById(R.id.event_start);
    	endDate = (TextView)findViewById(R.id.event_end);
    	startTime = (TextView)findViewById(R.id.event_start_time);
    	endTime = (TextView)findViewById(R.id.event_end_time);

    	switch (v.getId()) {

    	case R.id.event_start:
    		showDatePickerDialog();
    		check = true;
    		break;

    	case R.id.event_end:
    		showDatePickerDialog();
    		check = false;
    		break;

    	case R.id.event_start_time:
    		showTimePickerDialog();
    		check = true;
    		break;

    	case R.id.event_end_time:
    		showTimePickerDialog();
    		check = false;
    		break;
    	}
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    
    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
        	
        	if(check==true)
            {
                startDate.setText(new StringBuilder().append(month+1).append("/")
            		    .append(day).append("/").append(year));
            }
            else if(check ==false)
            {
                endDate.setText(new StringBuilder().append(month+1).append("/")
            		    .append(day).append("/").append(year));
            } 
        }
    }
    
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default time in the picker
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
        	if(check==true)
            {
        		startTime.setText(new StringBuilder().append(hour).append(":")
        		    .append(minute));
            }
        	else if(check ==false)
            {
        		endTime.setText(new StringBuilder().append(hour).append(":")
            		    .append(minute));
            }
        }
    }
}


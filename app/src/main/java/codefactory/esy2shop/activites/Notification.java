package codefactory.esy2shop.activites;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.location.Address;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import codefactory.projectshop.R;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.Geofence;

import java.util.Calendar;

public class Notification extends Activity {



    Spinner dateSpinner;
    Spinner timeSpinner;
    private static Calendar calendar;

    //Used to set the date in the spinners
    private Calendar tempCalendar;
    private int tempInt;


    //Geofencing variables

    Address locationNotification;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notification);


        /*
            Banner Add
         */
        AdView mAdView = (AdView) findViewById(R.id.notification_add_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //_______________________


        //Calender Being init
        calendar = Calendar.getInstance();
        /*
            Gets intent Extras
         */
        //If store has been selected on the maps activity
        if(getIntent().getExtras().containsKey("storeAddress"))
            locationNotification = getIntent().getExtras().getParcelable("storeAddress");





        /*
            Spinners
         */
        //Initialise Calender

        //Populate Date Spinner
        dateSpinner = (Spinner) findViewById(R.id.date_spinner);
        ArrayAdapter<CharSequence> dateOptionAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_options, android.R.layout.simple_spinner_item);
        dateOptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateOptionAdapter);

        //Set Listener on date Spinner
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    //Today
                    case 0:

                        //Sets the calenders current date to today
                        tempCalendar = Calendar.getInstance();
                        calendar.set(tempCalendar.get(Calendar.YEAR),tempCalendar.get(Calendar.MONTH),tempCalendar.get(Calendar.DAY_OF_MONTH));
                        break;

                    //Tommorow
                    case 1:

                        tempCalendar = Calendar.getInstance();
                        tempInt = tempCalendar.get(Calendar.DAY_OF_MONTH) + 1;
                        calendar.set(Calendar.DAY_OF_MONTH, tempInt);
                        break;

                    //Next Week
                    case 2:

                        tempCalendar = Calendar.getInstance();
                        tempInt = tempCalendar.get(Calendar.DAY_OF_MONTH) + 1;
                        calendar.set(Calendar.DAY_OF_MONTH,tempInt);
                        break;

                    //Select a date
                    case 3:

                        DialogFragment newFragment = new DatePickerFragment();
                        newFragment.show(getFragmentManager(), "DatePicker");
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //__________________________________________________________________


        //Initisiling Time Spinner
        timeSpinner = (Spinner) findViewById(R.id.time_spinner);
        ArrayAdapter<CharSequence> timeOptionAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_options, android.R.layout.simple_spinner_item);
        timeOptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeOptionAdapter);


        /*
            Listener
         */
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    //Morning is selected
                    case 0:


                        /*
                            Sets time on calender to 9:00 am
                         */
                        calendar.set(Calendar.HOUR_OF_DAY, 9);
                        break;

                    //Noon is selected
                    case 1:

                        // Sets to 12:00 noon
                        calendar.set(Calendar.HOUR_OF_DAY, 12);
                        break;

                    //Evening is Selected
                    case 2:

                        //sets to 5:00 pm
                        calendar.set(Calendar.HOUR_OF_DAY, 17);
                        break;

                    //Night is selectedd
                    case 3:

                        //sets to 9:00 pm
                        calendar.set(Calendar.HOUR_OF_DAY, 21);
                        break;

                    //Select a time is selected
                    case 4:
                        DialogFragment newFragment = new TimePickerFragment();
                        newFragment.show(getFragmentManager(),"Choose a Time");
                        break;


                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }






    /*
        Date Picker
        Used on select date
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        /*
            When use sets date on the fragemet
         */
        public void onDateSet(DatePicker view, int year, int month, int day) {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

        }
    }





    /*
        Time Picker dialog
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


        /*
            User selects a time
         */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

        }
    }





    /*
        Geofening Class
        ---------------------------------------------------------------------------
     */
    private Geofence createGeofence(Address address){

        return new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(entry.getKey())

                        // Set the circular region of this geofence.
                .setCircularRegion(
                        entry.getValue().latitude,
                        entry.getValue().longitude,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )

                        // Set the expiration duration of the geofence. This geofence gets automatically
                        // removed after this period of time.
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                        // Set the transition types of interest. Alerts are only generated for these
                        // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)

                        // Create the geofence.
                .build();

    }









}



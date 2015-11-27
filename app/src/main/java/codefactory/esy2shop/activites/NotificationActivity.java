package codefactory.esy2shop.activites;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import codefactory.esy2shop.services.GeofenceTransitionsIntentService;
import codefactory.esy2shop.adapters.NotificationAdapter;
import codefactory.esy2shop.helpers.GeofenceErrorMessages;
import codefactory.esy2shop.helpers.GeofencingConstants;
import codefactory.esy2shop.helpers.TimeNotificationPublisher;
import codefactory.esy2shop.models.ListNotifcation;
import codefactory.projectshop.R;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

public class NotificationActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status> {


    //Tag for Log Cat
    protected static final String TAG = "NotificationActivity";

    //Adapter Vars
    ArrayList<ListNotifcation> notifcationArrayList;
    NotificationAdapter notificationAdapter;
    ListView notificationListView;

    Intent receivedIntent;

    Spinner dateSpinner;
    Spinner timeSpinner;
    protected static Calendar calendar;

    //Used to set the date in the spinners
    private Calendar tempCalendar;
    private int tempInt;

    // list id
    private int listId;

    //Geofencing variables
    Address locationNotification;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * The list of geofences used in this sample.
     */
    protected ArrayList<Geofence> mGeofenceList;

    /**
     * Used to keep track of whether geofences were added.
     */
    private boolean mGeofencesAdded;
    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;






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


        //Calender Being initinitled
        calendar = Calendar.getInstance();


        /*
            Spinners
         */
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

                        //   Sets time on calender to 9:00 am
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
                        newFragment.show(getFragmentManager(), "Choose a Time");
                        break;


                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        /*
            Geofencing

            Creates a notfication when user enters a area around a shop
            _________________________________________________________________________________
         */

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null.
        mGeofencePendingIntent = null;

        // Retrieve an instance of the SharedPreferences object.
        mSharedPreferences = getSharedPreferences(GeofencingConstants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences.getBoolean(GeofencingConstants.GEOFENCES_ADDED_KEY, false);

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();
        
        /*
            Initise notification array list
            and add to adatapter
         */
        notifcationArrayList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this,notifcationArrayList);
        notificationListView = (ListView) findViewById(R.id.notification_listView);
        notificationListView.setAdapter(notificationAdapter);



        /**
         *  Gets extras from intents
         */
        // Get intent
        receivedIntent = getIntent();

                /*
            List id from extras
            Should have if coming from either Maps or edit list
         */
        if(receivedIntent.hasExtra("ListId")){

            listId = receivedIntent.getIntExtra("ListId", -1);

        } else {

            Log.d(TAG,"Recvied intent has no list id");
        }



    } /// End of Oncreate





    /**
     * Builds a Notification to be used by the
     * Geo Notification and the time notify
     *
     * @param content
     * @return
     */
    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Pantry");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        return builder.build();
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




    /**
     *  Creates the notification and pending intent for the time notifications
     *  Time is set to current instance of calender as set by user.
     *  Broadcast reciever - @see codefactory.esy2shop.helpers.TimeNotificationPublisher
     *
     *
     * @param notification
     */
    public void scheduleNotification(Notification notification) {

        Intent notificationIntent = new Intent(this, TimeNotificationPublisher.class);
        notificationIntent.putExtra(TimeNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(TimeNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    /*
        GeoFencing
        ---------------------------------------------------------------------
     */


    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    public void populateGeofence(Address location, String geoFenceID) {

        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(geoFenceID)

                        // Set the circular region of this geofence.
                .setCircularRegion(
                        location.getLatitude(),
                        location.getLongitude(),
                        GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
                )

                        // Set the expiration duration of the geofence. This geofence gets automatically
                        // removed after this period of time.
                .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                        // Set the transition types of interest. Alerts are only generated for these
                        // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)

                        // Create the geofence.
                .build());

    }



    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }





    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        Log.i(TAG, "Connected to GoogleApiClient");

        // Gets the address from the intent from google maps activity
        if(receivedIntent.hasExtra("storeAddress")) {

            /*
                Gets the sent address from the google maps
             */
            locationNotification = getIntent().getExtras().getParcelable("storeAddress");

            Log.d(TAG, locationNotification.getFeatureName());
            Toast.makeText(NotificationActivity.this, "Great", Toast.LENGTH_SHORT).show();


            /*
                Check google api conncection
             */

            if (!mGoogleApiClient.isConnected()) {
                Toast.makeText(this, getString(R.string.google_api_not_connected), Toast.LENGTH_SHORT).show();
                return;
            }



            try {


                Toast.makeText(NotificationActivity.this, "Really Great", Toast.LENGTH_SHORT).show();
                // Create new listNotification
                ListNotifcation listNotifcation = new ListNotifcation(listId,false);
                listNotifcation.setTitle("List Name here"); // Needs to get the current lIst name
                // Corresponds to listId
                listNotifcation.setDetails(locationNotification.getFeatureName());
                listNotifcation.setNotifyAddress(locationNotification);

                //Add to Array list
                notifcationArrayList.add(listNotifcation);
                notificationAdapter.notifyDataSetChanged();

                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        // The GeofenceRequest object.
                        getGeofencingRequest(),
                        // A pending intent that that is reused when calling removeGeofences(). This
                        // pending intent is used to generate an intent when a matched geofence
                        // transition is observed.
                        getGeofencePendingIntent()
                ).setResultCallback(this); // Result processed in onResult().


            } catch (SecurityException securityException) {
                // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
                logSecurityException(securityException);
                Toast.makeText(NotificationActivity.this, "Security Exception", Toast.LENGTH_SHORT).show();
            }

        }else{
            Log.d(TAG,"Has no Store Address");
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.i(TAG, "Connection suspended");

        // onConnected() will be called again automatically when the service reconnects
    }




    /**
     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
     * Either method can complete successfully or with an error.
     *
     * Since this activity implements the {@link ResultCallback} interface, we are required to
     * define this method.
     *
     * @param status The Status returned through a PendingIntent when addGeofences() or
     *               removeGeofences() get called.
     */
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(GeofencingConstants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            //setButtonsEnabledState();

            Toast.makeText(
                    this,
                    "Toast and Vegemite",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }




    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence. -- Changed to Dwell
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }


    /**
     *
     * @param securityException
     */
    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }


    /*
        Onclick Methods
     */



    /**
     * Onclick method for the date button
     * Adds a date notification for a certain list
     * @param view
     */
    public void addDateNotification(View view){

        //TEST
        Toast.makeText(NotificationActivity.this, "addDateNottification", Toast.LENGTH_SHORT).show();

        //Create Notification
        Notification timeNotification = getNotification("Test");  // Should be list.getName()......

        //Schedule the notification
        scheduleNotification(timeNotification);

        // Create new listNotification
        ListNotifcation listNotifcation = new ListNotifcation(listId,true);
        listNotifcation.setNotifyTime(calendar);
        listNotifcation.setListID(listId);
        listNotifcation.setTitle("List Name");
        listNotifcation.setDetails(convertCalenderToString(calendar));

        //Add notification to the adapter
        notifcationArrayList.add(listNotifcation);
        notificationAdapter.notifyDataSetChanged();


    }


    /**
     * Onclick method for geo notification
     * Opens google maps activitie
     * Sends list id as extra
     * @param view
     */
    public void addGeoNotification(View view){

        Intent listIntent;
        listIntent = new Intent(getApplicationContext(), GoogleMapActivity.class);
        listIntent.putExtra("ListID", -1);
        startActivity(listIntent);
    }




    /**
     * Returns a formatted string rep of a calenders
     *
     * @param calender
     * @return
     */
    public String convertCalenderToString(Calendar calender){

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a  EEE MMM d yyyy G");
        dateFormat.setCalendar(calender);
        return dateFormat.format(calender.getTime());

    }






}








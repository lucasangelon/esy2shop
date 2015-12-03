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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import codefactory.esy2shop.adapters.ItemAdapter;
import codefactory.esy2shop.adapters.NotificationAdapter;
import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.helpers.GeofenceErrorMessages;
import codefactory.esy2shop.helpers.GeofencingConstants;
import codefactory.esy2shop.helpers.TimeNotificationPublisher;
import codefactory.esy2shop.models.Item;
import codefactory.esy2shop.models.List;
import codefactory.esy2shop.models.ListNotifcation;
import codefactory.esy2shop.models.Store;
import codefactory.esy2shop.services.GeofenceTransitionsIntentService;
import codefactory.projectshop.R;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

public class EditList extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {




    /*
        Variables
     */
    List list;
    DatabaseManager db;
    EditText listNameField;
    Spinner listCategorySpinner;
    ArrayAdapter<String> listCategoryAdapter;
    ArrayList<Item> mRemovedItems;
    int[] listCategoryIDs;
    ItemAdapter itemAdapter;
    Button listAddItemBtn;
    EditText itemAddText;


    //Spinner Layout
    LinearLayout spinnerLayout;


    // Aadpter Variables
    ArrayList<ListNotifcation> notifcationArrayList;
    NotificationAdapter notificationAdapter;
    ListView notificationListView;


    //Date and time Spinner
    Spinner dateSpinner;
    Spinner timeSpinner;
    protected static Calendar calendar;

    //Used to set the date in the spinners
    private Calendar tempCalendar;
    private int tempInt;

    //Notification layour
    LinearLayout notificationLayout;


    /**
        Geofenceing variables
     */
    Address locationNotification;
    boolean hasLocation;

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


    /**
     *  Tag used for log cat
     */
    final static String TAG = "EditList";



    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth((90));
            // set a icon
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        //Init removed items
        mRemovedItems = new ArrayList<>();

        //Get ID from intent
        db = new DatabaseManager(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("ListID", -1);


        //Checks for address in intent
        if(intent.hasExtra("storeAddress")){

                /*
                    Gets the sent address from the google maps
                */
            locationNotification = intent.getExtras().getParcelable("storeAddress");
            hasLocation = true;

        } else
            hasLocation = false;


        // If id == -1 new list else get from DB
        list = (id == -1) ? new List(): new List(db, id);

        //Set name
        getSupportActionBar().setTitle(list.getName());

        // Update Name Field
        listNameField = (EditText) findViewById(R.id.listNameField);
        listNameField.setText(list.getName());

        // Update Category Spinner
        listCategorySpinner = (Spinner) findViewById(R.id.listCategorySpinner);
        int listCategorySize = DatabaseManager.CATEGORIES.size();

        // Get Category Names and prepare IDs
        String[] listCategoryNames = new String[listCategorySize + 1];
        listCategoryNames[0] = "";
        listCategoryIDs = new int[listCategorySize + 1];
        listCategoryIDs[0] = -1;


        // Get the List Category ID for testing
        int listCategoryID = list.getCategory();
        int CategoryStartPos = 0;

        // Date spinner layout
        spinnerLayout = (LinearLayout) findViewById(R.id.time_spinner_layout);

        //Notification Layout
        notificationLayout = (LinearLayout) findViewById(R.id.notification_list_layout);


        //Linear Layout
        notificationListView = (ListView) findViewById(R.id.edit_list_notification_listView);

        // Get DB Categories IDs
        String[] tempListCategoryNames = DatabaseManager.CATEGORIES.values().toArray(new String[listCategorySize]);
        Integer[] tempListCategoryIDs = DatabaseManager.CATEGORIES.keySet().toArray(new Integer[listCategorySize]);


        //

        // Add DB data to adapter array
        for(int i = 0; i < listCategorySize; i++)
        {
            listCategoryNames[i + 1] = tempListCategoryNames[i];
            listCategoryIDs[i + 1] = tempListCategoryIDs[i];
            if(listCategoryID == tempListCategoryIDs[i])
            {
                CategoryStartPos = i;
            }
        }


        // Finalise adapter and spinner
        listCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryNames);
        listCategorySpinner.setAdapter(listCategoryAdapter);
        listCategorySpinner.setSelection(CategoryStartPos);
        listCategorySpinner.setFocusable(true);
        listCategorySpinner.setFocusableInTouchMode(true);

        // Setup Add Item Button
        listAddItemBtn = (Button) findViewById(R.id.add_item_button);
        itemAddText = (EditText) findViewById(R.id.add_item_editText);
        itemAddText.setFocusableInTouchMode(true);
        itemAddText.setFocusable(true);




        /*
            Ste up Item adapter
         */
        itemAdapter = new ItemAdapter(this, list);
        com.baoyz.swipemenulistview.SwipeMenuListView listItemsView = (com.baoyz.swipemenulistview.SwipeMenuListView) findViewById(R.id.list_item_view);
        listItemsView.setAdapter(itemAdapter);
        listItemsView.setMenuCreator(creator);
        listItemsView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        itemAdapter.remove(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });






        /*
            IME handle for next on list name field
         */
        listNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {


                    /*
                        Set list name
                     */
                    list.setName(listNameField.getText().toString());
                    getSupportActionBar().setTitle(list.getName());

                    /*
                        Hide Keyboard
                     */
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(listNameField.getWindowToken(), 0);
                    /*
                        Sets focus after name is entered to the spinner
                     */
                    listCategorySpinner.requestFocus();
                    listCategorySpinner.performClick();


                    handled = true;

                }
                return handled;
            }
        });





        /*
            Ime handler for add items
         */
        itemAddText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    handled = true;
                    addItem();
                }
                return handled;
            }
        });




        //Banner Ad Create
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        //Notification Adapter
        notifcationArrayList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this,notifcationArrayList);
        notificationListView = (ListView) findViewById(R.id.edit_list_notification_listView);
        notificationListView.setAdapter(notificationAdapter);



        /*
            Time and Date Spinner
         */
        //Initialises calender to current time
        calendar = Calendar.getInstance();


        /**
         *  Spinners
         *  User sets a time for push notification
         */
        //Populate Date Spinner
        dateSpinner = (Spinner) findViewById(R.id.edit_list_date_spinner);
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



        //Initisiling Time Spinner
        timeSpinner = (Spinner) findViewById(R.id.edit_list_time_spinner);
        ArrayAdapter<CharSequence> timeOptionAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_options, android.R.layout.simple_spinner_item);
        timeOptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeOptionAdapter);


        /*
            Time Spinner onItemSelecter lister
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



        /**
         *  Gefenceing code
         *
         *  Sets a push notification for location
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


    }



    @Override
    public void onPause()
    {
        listSave();
        super.onPause();
    }





    @Override
    public void onDestroy()
    {
        listSave();
        super.onDestroy();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_list, menu);
        return true;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            list.setStore(data.getIntExtra("StoreID", -1));

        }
        else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            // Deal with Notifications
        }
    }






    @Override
    public void onBackPressed()
    {
        listSave();

        // Toast
        Toast.makeText(this,"Changes Saved",Toast.LENGTH_LONG).show();

        // Close
        finish();
    }





    public void listSave(){

        // Make useable DbManager
        DatabaseManager db = new DatabaseManager(this);

        //get from adapter (updates checkboxes)
        list = itemAdapter.getList();

        // Update List Object
        list.setName(listNameField.getText().toString());

        //remove items from database
        for(Item i : mRemovedItems)
        {
            db.DeleteItem(i.getId());
        }

        //set the catagory
        list.setCategory(listCategoryIDs[listCategorySpinner.getSelectedItemPosition()]);

        // Apply Changes
        list.SaveChanges(db);
    }




    /**
     *  Adds item to the list
     *
     *  Used when add_item_button is pressed or
     *  the 'next' is pressed on teh keyboard
     */
    private void addItem(){

        /*
            If txt is empty
         */
        if(itemAddText.getText().toString().trim().equals("")){


            itemAddText.setText("");
            Toast.makeText(EditList.this, "Please enter Tex", Toast.LENGTH_SHORT).show();

        }else{

            /*
                If text field has text
             */

            //Add item to adapter
            Item item = new Item();
            item.setName(itemAddText.getText().toString().trim());
            Toast.makeText(EditList.this, itemAddText.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            item.setComplete(false);
            list.add(item);
            itemAdapter.notifyDataSetChanged();



            //Clear text
            //itemAddText.clearComposingText();

            //Focus
            itemAddText.requestFocus();
            itemAddText.performClick();
            itemAddText.setText("");


        }

    }


    /**
     *  Onclick method for add_item_button
     *
     * @param view
     */
    public void addItemClick(View view){
        addItem();
    }


    /**
     *  Removes and item from the currnent list
     * @param position
     */
    public void removeItem(int position){

        //Add to removed items
        mRemovedItems.add(list.getItemList().get(position));

        //remove item form list
        list.getItemList().remove(position);

        //Update adapter
        itemAdapter.notifyDataSetChanged();

    }




    /**
     * Onclick Method for the GPS icon
     *
     * Directs user to the Google maps activity
     * @param view
     */
    public void gpsIconClick(View view){

        Intent listIntent;
        listIntent = new Intent(getApplicationContext(), GoogleMapActivity.class);
        listIntent.putExtra("ListId", list.getId());
        startActivity(listIntent);

    }






    /**
     * OnClick method for date notification
     *
     * @param view
     */
    public void bellButtonClick(View view){


        if(spinnerLayout.getVisibility() == View.GONE){

            //Spinner is not visible
            spinnerLayout.setVisibility(View.VISIBLE);

        } else {

            spinnerLayout.setVisibility(View.GONE);

        }


    }


    /**
     *  DatePickerFragment gets a custom date from user
     *  Displayed when 'select date' is selected from the date spinner
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


    /**
     * TimePickerFragment get a custom time from user
     * Displayed when 'select time' is pressed in time spinner
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



    /**
     *
     * @param view
     */
    public void addTimeNotificationClick(View view){


        //TEST
        Toast.makeText(this, "addDateNottification", Toast.LENGTH_SHORT).show();

        //Create Notification
        Notification timeNotification = getNotification("Test");  // Should be list.getName()......

        //Schedule the notification
        scheduleNotification(timeNotification);

        // Create new listNotification
        ListNotifcation listNotifcation = new ListNotifcation(list.getId(),true);
        listNotifcation.setNotifyTime(calendar);
        listNotifcation.setListID(list.getId());
        listNotifcation.setTitle("List Name");
        listNotifcation.setDetails(convertCalenderToString(calendar));

        //Add notification to the adapter
        notifcationArrayList.add(listNotifcation);
        notificationAdapter.notifyDataSetChanged();

        // Make Notification Layout invisible
        if(notificationLayout.getVisibility() == View.GONE)
            notificationLayout.setVisibility(View.VISIBLE);


        //Make Adapter Layout visible
        if(spinnerLayout.getVisibility() == View.VISIBLE)
            spinnerLayout.setVisibility(View.GONE);


    }



    /**
     * Returns a formatted string rep of a calenders
     *
     * @param calender
     * @return String represtation of Calender
     */
    public String convertCalenderToString(Calendar calender){

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a  EEE MMM d yyyy G");
        dateFormat.setCalendar(calender);
        return dateFormat.format(calender.getTime());

    }


    /**
     *  Creates the notification and pending intent for the time notifications
     *  Time is set to current instance of calender as set by user.
     *  Broadcast reciever - @see codefactory.esy2shop.helpers.TimeNotificationPublisher
     *
     *
     * @param notification -
     */
    public void scheduleNotification(Notification notification) {

        Intent notificationIntent = new Intent(this, TimeNotificationPublisher.class);
        notificationIntent.putExtra(TimeNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(TimeNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    /**
     *  Geofenceing code
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
        listSave();
        mGoogleApiClient.disconnect();
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        Log.i(TAG, "Connected to GoogleApiClient");
        Toast.makeText(EditList.this, "Connected to GoogleApiClient", Toast.LENGTH_SHORT).show();


        if(hasLocation) {



            Log.d(TAG, locationNotification.getFeatureName());
            Toast.makeText(this, "Great", Toast.LENGTH_SHORT).show();


            /*
                Check google api conncection
             */

            if (!mGoogleApiClient.isConnected()) {
                Toast.makeText(this, getString(R.string.google_api_not_connected), Toast.LENGTH_SHORT).show();
                return;
            }



            try {


                Toast.makeText(this, "Really Great", Toast.LENGTH_SHORT).show();
                // Create new listNotification
                ListNotifcation listNotifcation = new ListNotifcation(list.getId(),false);
                listNotifcation.setTitle("List Name here"); // Needs to get the current lIst name
                // Corresponds to listId
                listNotifcation.setDetails(locationNotification.getFeatureName());
                listNotifcation.setNotifyAddress(locationNotification);

                //Add to Array list
                notifcationArrayList.add(listNotifcation);
                notificationAdapter.notifyDataSetChanged();

                //Remove Spinner layout if visible
                if(spinnerLayout.getVisibility() == View.VISIBLE)
                    spinnerLayout.setVisibility(View.GONE);

                //Make Adapter layout visible
                if(notificationLayout.getVisibility() == View.GONE)
                    notificationLayout.setVisibility(View.VISIBLE);

                //Add geofecnce to mGeofenceList
                populateGeofence(locationNotification,String.valueOf(list.getId()));

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
                Toast.makeText(this, "Security Exception", Toast.LENGTH_SHORT).show();
            }

        }else{
            Log.d(TAG, "Has no Store Address");
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
     *  Logs Security Exception
     *
     * @param securityException
     */
    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }
}

package codefactory.esy2shop.activites;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import codefactory.esy2shop.adapters.AddressAdapter;
import codefactory.esy2shop.helpers.PermissionUtils;
import codefactory.projectshop.R;







public class GoogleMapActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    /*
        Map
     */
    private GoogleMap mMap;

    /*
        Views
     */
    //Serach bar
    EditText searchInputText;
    ImageButton searchButtonInput;

    //Results list
    ListView resultsList;
    AddressAdapter addressAdapter;


    Intent receivedIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        receivedIntent = getIntent();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*
            Initialising Views
         */
        searchInputText = (EditText) findViewById(R.id.map_search_input_text);
        searchButtonInput = (ImageButton) findViewById(R.id.map_search_image_button);
        resultsList = (ListView) findViewById(R.id.results_map_list_view);


        /*
            Listener for search button
         */
        searchButtonInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GoogleMapActivity.this, "Search", Toast.LENGTH_SHORT).show();
                searchForLocation(); // See below for method
            }
        });


        /*
            IME actions for edittext
         */
        searchInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchForLocation();
                    handled = true;
                }
                return handled;
            }
        });


    }




    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }





    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }








    /*
        Centres Map On users current location
     */
    @Override
    public boolean onMyLocationButtonClick() {


        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }





    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }





    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }




    /**
     * Search for locations from the user input
     */
    public void searchForLocation() {

        EditText userDestination;
        String destination;
        ArrayList<Address> addressList;



        userDestination = (EditText) findViewById(R.id.map_search_input_text);
        destination = userDestination.getText().toString();

        //Check if destination is null or empty
        if (destination != null || !destination.equals("")) {

            Geocoder geocoder = new Geocoder(this);

            try {
                //Using Geocoder's object to transform string in geolocation
                addressList = (ArrayList) geocoder.getFromLocationName(destination, 20);

                //set adapter
                addressAdapter = new AddressAdapter(GoogleMapActivity.this, addressList);
                resultsList.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

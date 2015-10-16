package codefactory.esy2shop.activites;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import codefactory.esy2shop.adapters.StoreAdapter;
import codefactory.esy2shop.database.DatabaseManager;
import codefactory.projectshop.R;

public class EditStore extends ActionBarActivity implements OnMapReadyCallback {

    EditText newStoreField;
    StoreAdapter storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);

        newStoreField = (EditText) findViewById(R.id.newStoreField);
        ListView storesListView = (ListView) findViewById(R.id.storesListView);
        storeAdapter = new StoreAdapter(this, (getCallingActivity() != null));
        storesListView.setAdapter(storeAdapter);

        // Test connection to set up GoogleMaps
        ConnectivityManager conn = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conn.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting())
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.storeMap);
            mapFragment.getMapAsync(this);
        }
    }

    public void newStoreBtnOnClick(View view)
    {
        storeAdapter.Update(newStoreField.getText().toString());
    }

    @Override
    public void onBackPressed()
    {
        storeAdapter.onBack();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        storeAdapter.SetGoogleMap(googleMap);
        int intentStoreID = getIntent().getIntExtra("StoreID", -1);
        if(intentStoreID != -1)
        {
            storeAdapter.UpdateMapMarker(intentStoreID);
        }
        else
        {
            storeAdapter.Update();
        }
    }
}

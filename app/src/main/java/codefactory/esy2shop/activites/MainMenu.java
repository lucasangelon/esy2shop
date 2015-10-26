package codefactory.esy2shop.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.models.Store;
import codefactory.projectshop.R;
import codefactory.esy2shop.models.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainMenu extends ActionBarActivity {


    Intent listIntent;
    codefactory.esy2shop.adapters.ListAdapter listAdapter;

    ListView mainListView;

    //Navigation Drawer
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

    //@Bind(R.id.refreshImageView) ImageView mRefreshImageView;

    //Method called in onCreate
    private void addDrawerItems(){
        String[] osArray = { "All Lists", "Work", "Personal", "Daily Reminders", "Saved Stores", "About Page"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        listIntent = new Intent(this, EditList.class);




        Button newButton = (Button) findViewById(R.id.newList);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listIntent.putExtra("ListID", -1);
                startActivity(listIntent);
            }});



        // Load the Database for Static
        DatabaseManager dbLoad = new DatabaseManager(this, true);

        //Set adapter to list view
        listAdapter = new codefactory.esy2shop.adapters.ListAdapter(dbLoad.GetLists(null), this);
        ListView mainListView = (ListView)findViewById(R.id.mainListView);
        mainListView.setAdapter(listAdapter);

        placeholderComponents();

        //create Banner Ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        // Load the Database
        DatabaseManager dbLoad = new DatabaseManager(this);
        listAdapter.Update(dbLoad.GetLists(null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void placeholderComponents()
    {
        final Spinner categorySpinner = (Spinner) findViewById(R.id.TEMPCategorySpinner);
        int categorySize = DatabaseManager.CATEGORIES.size();
        String[] categoryNames = new String[categorySize];
        categoryNames = DatabaseManager.CATEGORIES.values().toArray(categoryNames);
        final Integer[] categoryIDs = DatabaseManager.CATEGORIES.keySet().toArray(new Integer[categorySize]);
        // Finalise adapter and spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryNames);
        categorySpinner.setAdapter(categoryAdapter);
        Button categoryRefine = (Button) findViewById(R.id.refineCategoryBtn);
        categoryRefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.FilterCategory(categoryIDs[categorySpinner.getSelectedItemPosition()]);
            }
        });

        final Spinner storeSpinner = (Spinner) findViewById(R.id.TEMPStoreSpinner);
        int storeSize = DatabaseManager.STORES.size();
        String[] storeNames = new String[storeSize];
        storeNames = DatabaseManager.STORES.values().toArray(storeNames);
        final Integer[] storeIDs = DatabaseManager.STORES.keySet().toArray(new Integer[storeSize]);
        // Finalise adapter and spinner
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, storeNames);
        storeSpinner.setAdapter(storeAdapter);
        Button storeRefine = (Button) findViewById(R.id.refineStoreBtn);
        final CheckBox active = (CheckBox) findViewById(R.id.storeActiveBtn);
        storeRefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.FilterStore(storeIDs[storeSpinner.getSelectedItemPosition()], active.isChecked());
            }
        });
    }
}


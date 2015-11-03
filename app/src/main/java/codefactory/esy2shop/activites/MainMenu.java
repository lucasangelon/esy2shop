package codefactory.esy2shop.activites;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import codefactory.esy2shop.database.DatabaseManager;
import codefactory.projectshop.R;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import codefactory.esy2shop.database.DatabaseManager;
import codefactory.projectshop.R;
import codefactory.esy2shop.adapters.ShoppingListAdapter;


public class MainMenu extends ActionBarActivity {

    ShoppingListAdapter listAdapter;

    ListView mainListView;

    //Nav_drawer_redo
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;

    //@Bind(R.id.refreshImageView) ImageView mRefreshImageView;

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
            deleteItem.setIcon(R.mipmap.ic_launcher);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        /*
           New List Button
         */
        Button newButton = (Button) findViewById(R.id.newList);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent listIntent;
                listIntent = new Intent(getApplicationContext(), EditList.class);
                listIntent.putExtra("ListID", -1);
                startActivity(listIntent);

            }});



        // Load the Database for Static
        DatabaseManager dbLoad = new DatabaseManager(this, true);

        //Set adapter to list view
        listAdapter = new ShoppingListAdapter(dbLoad.GetLists(null), this);
        com.baoyz.swipemenulistview.SwipeMenuListView  mainListView = (com.baoyz.swipemenulistview.SwipeMenuListView )findViewById(R.id.mainListView);
        mainListView.setAdapter(listAdapter);
        mainListView.setMenuCreator(creator);

        mainListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        listAdapter.remove(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });




        AdView mAdView = (AdView) findViewById(R.id.add_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Create Nav Drawer JLO
        mDrawerList = (ListView)findViewById(R.id.navList);
        drawer =(DrawerLayout) findViewById(R.id.drawer_layout);

        //Call the method
        addDrawerItems();
        mDrawerList.bringToFront();
        drawer.requestLayout();
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.FilterCategory(position+1);
                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(mDrawerList);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_action_settings:
                drawer.openDrawer(mDrawerList);

            default:
                return super.onOptionsItemSelected(item);
        }
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_menu, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
    }


    /*
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
<<<<<<< HEAD

    }
    */

    //Create Drawer items (called in onCreate) JLO
    private void addDrawerItems(){
        String[] osArray = { "All Lists", "Work", "Personal", "Daily Reminders", "Saved Stores", "About Page"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

    }
}


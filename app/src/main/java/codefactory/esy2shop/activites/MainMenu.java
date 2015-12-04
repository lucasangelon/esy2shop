package codefactory.esy2shop.activites;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;


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


public class MainMenu extends AppCompatActivity {


    ShoppingListAdapter listAdapter;

    //Nav_drawer_redo
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout drawer;
    private String[] drawer_items;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;



    /*
        Swipe Menu
     */
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
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
        ImageButton newButton = (ImageButton) findViewById(R.id.newList);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent listIntent;
                listIntent = new Intent(getApplicationContext(), EditList.class);
                listIntent.putExtra("ListID", -1);
                startActivity(listIntent);

            }
        });





        /*
            Database initialisation
         */
        DatabaseManager dbLoad = new DatabaseManager(this, true);




        /*
            Main List View
         */
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

        //______________________________________________________






        /*
            Banner Adds
         */
        AdView mAdView = (AdView) findViewById(R.id.add_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //_______________________






        /*
            Navigation Drawer
         */
        mDrawerList = (ListView)findViewById(R.id.navList);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_items = getResources().getStringArray(R.array.menu_items);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawer_items);

        mDrawerList.setAdapter(mAdapter);
        mDrawerList.bringToFront();
        //Set listener for list (see Beloe)
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawer.requestLayout();


        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mTitle = mDrawerTitle = getTitle();


        /*
            Lister for drawer (Opening and Closing the draw)
         */
        mDrawerToggle = new ActionBarDrawerToggle(
                this,           /* host Activity */
                drawer,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        )

        {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        //________________________________________


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

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
        return super.onCreateOptionsMenu(menu);
    }







    /*
        Disables backstack to splash

        Needs some sort of press again to exit thing going on =)
     */
    @Override
    public void onBackPressed() {
    }



    /*
        For Nav Drawer
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }



    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }




    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawer.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.menu_action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /*

    mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listAdapter.FilterCategory(position + 1);
            drawer.closeDrawer(mDrawerList);
        }
    });

     */



    /*
        Onclick listener for navigation Drawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position); // See below
        }
    }

    /*
        Called in Drawer Listener
     */
    private void selectItem(int position) {

        switch (position){

            /*
                For Filtering list
             */
            case 0:case 1:case 2:case 3:

                //Update adpapter. Set Title
                listAdapter.FilterCategory(position + 1);
                setTitle(drawer_items[position]);
                drawer.closeDrawer(mDrawerList);
                break;


            /*
                Saved Stores
            */
            case 4:

                //
                drawer.closeDrawer(mDrawerList);
                Intent mapIntent;
                mapIntent = new Intent(getApplicationContext(), GoogleMapActivity.class);
                startActivity(mapIntent);
                break;


            /*
                About page
             */
            case 5:

                // Starts about activity
                Intent aboutIntent;
                aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(aboutIntent);
                break;


            default:

                //
                Toast.makeText(MainMenu.this, "", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(mDrawerList);


        }

    }


}


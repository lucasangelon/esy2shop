package codefactory.projectshop.activites;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.app.ActionBar;
import android.widget.ListView;


import junit.framework.Test;

import java.util.ArrayList;

import butterknife.Bind;
import codefactory.projectshop.R;
import codefactory.projectshop.models.Item;
import codefactory.projectshop.models.List;

public class MainMenu extends AppCompatActivity {

    ListView mainListView;

    //Navigation Drawer
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    //@Bind(R.id.refreshImageView) ImageView mRefreshImageView;

    //Method called in onCreate
    private void addDrawerItems(){
        String[] osArray = { "All Lists", "Work", "Personal", "Daily Reminders"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        //Navigation Drawer
        mDrawerList = (ListView)findViewById(R.id.navList);

        //Array for adapter
        ArrayList<List> listOfLists = new ArrayList<>();


        //setting up default data
        List testList1 = new List();
        testList1.setName("Groceries");
        listOfLists.add(testList1);
        List testList2 = new List();
        testList2.setName("Pokemon");
        listOfLists.add(testList2);
        List testList3 = new List();
        testList3.setName("Digimon");
        listOfLists.add(testList3);
        List testList4 = new List();
        testList4.setName("Tamagotchis");
        listOfLists.add(testList4);


        //Set adapter to list view
        ListAdapter adapter = new codefactory.projectshop.adapters.ListAdapter(listOfLists, MainMenu.this);
        mainListView = (ListView)findViewById(R.id.mainListView);
        mainListView.setAdapter(adapter);

        //Navigation Drawer
        addDrawerItems();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

}


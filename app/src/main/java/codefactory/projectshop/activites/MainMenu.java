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
import android.widget.ListAdapter;
import android.app.ActionBar;
import android.widget.ListView;


import junit.framework.Test;

import java.util.ArrayList;

import codefactory.projectshop.R;
import codefactory.projectshop.models.Item;
import codefactory.projectshop.models.List;

public class MainMenu extends AppCompatActivity {

    ListView mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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




      /*  Test for now, will change to get list of lists later
        String[] listOfListsTest = {"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, );
        setListAdapter(adapter); */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}


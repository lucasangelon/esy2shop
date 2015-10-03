package codefactory.esy2shop.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.models.Store;
import codefactory.projectshop.R;
import codefactory.esy2shop.models.List;

public class MainMenu extends Activity {

    ListView mainListView;
    Intent listIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Load the Database for Static
        DatabaseManager dbLoad = new DatabaseManager(getApplicationContext(), true);
        // Get Database Lists
        ArrayList<List> listOfLists = dbLoad.GetLists(null);

        /*
        TEST  TEST  TEST
        Make sure there are placeholder objects
         */
        dbLoad.UpdateCategory(1, "General");
        dbLoad.UpdateCategory(2, "Specific");
        Map<Integer, String> empty = new TreeMap<Integer, String>();
        dbLoad.UpdateStore(new Store(1, "Store1", 1, 1, empty));
        dbLoad.UpdateStore(new Store(2, "Store2", 2, 2, empty));
        dbLoad.UpdateStore(new Store(3, "Store3", 3, 3, empty));

        listIntent = new Intent(this, EditList.class);

        Button newButton = (Button) findViewById(R.id.newList);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listIntent.putExtra("ListID", -1);
                startActivity(listIntent);
            }});

        //Set adapter to list view
        ListAdapter adapter = new codefactory.esy2shop.adapters.ListAdapter(listOfLists, MainMenu.this);
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


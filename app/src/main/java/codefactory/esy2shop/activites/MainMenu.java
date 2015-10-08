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

    Intent listIntent;
    codefactory.esy2shop.adapters.ListAdapter listAdapter;

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
}


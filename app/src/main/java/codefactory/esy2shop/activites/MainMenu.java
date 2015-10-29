package codefactory.esy2shop.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import codefactory.esy2shop.database.DatabaseManager;
import codefactory.projectshop.R;
import codefactory.esy2shop.adapters.ShoppingListAdapter;

public class MainMenu extends Activity {

    ShoppingListAdapter listAdapter;

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
        ListView mainListView = (ListView)findViewById(R.id.mainListView);
        mainListView.setAdapter(listAdapter);

        //placeholderComponents();


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
    } */
}


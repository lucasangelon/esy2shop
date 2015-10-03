package codefactory.esy2shop.activites;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.models.Item;
import codefactory.esy2shop.models.List;
import codefactory.esy2shop.models.Store;
import codefactory.projectshop.R;

public class EditList extends ActionBarActivity {

    /*
        Values
     */
    private List list;

    /*
        Views
     */

    private EditText listName;
    private Integer[] categoryIDs;
    private Spinner categorySpinner;
    private Integer[] storeIDs;
    private Spinner storeSpinner;
    private CheckBox isComplete;
    private Button saveButton;

    /*
        Test Test Test
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        /*
        Deal with List from Intent and/or Database
         */
        //Get ID from intent
        Intent intent = getIntent();
        int id = intent.getIntExtra("ListID", -1);
        // If id == -1 new list else get from DB
        list = (id == -1) ? new List(): new List(getApplicationContext(), id);

        /*
            Views
         */
        listName = (EditText) findViewById(R.id.listNameText);
        listName.setText(list.getName());

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        String[] categoryNames = new String[DatabaseManager.CATEGORIES.size()];
        categoryNames = DatabaseManager.CATEGORIES.values().toArray(categoryNames);
        if(categoryNames.length == 0)
        {
            categoryNames = new String[]{"General"};
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryNames);
        categorySpinner.setAdapter(categoryAdapter);

        storeSpinner = (Spinner) findViewById(R.id.storeSpinner);
        String[] storeNames = new String[DatabaseManager.STORES.size()];
        storeNames = DatabaseManager.STORES.values().toArray(storeNames);
        storeIDs = new Integer[storeNames.length];
        storeIDs = DatabaseManager.STORES.keySet().toArray(storeIDs);
        if(storeNames.length == 0)
        {
            storeNames = new String[]{"Empty"};
        }
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, storeNames);
        storeSpinner.setAdapter(storeAdapter);

        isComplete = (CheckBox) findViewById(R.id.isCompleteBox);
        isComplete.setChecked(list.isComplete());

        saveButton = (Button) findViewById(R.id.saveButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        OnClick Listener

     */
    public void onSaveClick(View view){
        /*
            Save changes
         */
        //NEEDS SOME REWORK
        int storeID = storeIDs[storeSpinner.getSelectedItemPosition()];
        Store selectedStore = new DatabaseManager(getApplicationContext()).GetStore(storeID);
        saveChanges(listName.getText().toString(), selectedStore, list.getCategory(), isComplete.isChecked());

        /*
            Toast
         */
        Toast.makeText(this,"Changes Saved",Toast.LENGTH_LONG).show();

    }


    /*
        Saves Changes to a list

        Saving in Variables

        Database will be added later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public void saveChanges(String name, Store store, int category, boolean complete){
        list.setName(name);
        // ITEM HANDLER NEEDS TO GO HERE
        list.setCategory(category);
        list.setStore(store);
        list.setComplete(complete);

        list.SaveChanges(getApplicationContext());
        finish();
    }
}

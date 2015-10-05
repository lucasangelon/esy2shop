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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import codefactory.esy2shop.adapters.ListItemAdapter;
import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.models.Item;
import codefactory.esy2shop.models.List;
import codefactory.esy2shop.models.Store;
import codefactory.projectshop.R;

public class EditList extends ActionBarActivity {

    List list;
    DatabaseManager db;
    EditText listNameField;
    CheckBox listCompleteField;
    Button newStoreBtn;
    Spinner listStoreSpinner;
    ArrayAdapter<String> listStoreAdapter;
    int[] listStoreIDs;
    Spinner listCategorySpinner;
    ArrayAdapter<String> listCategoryAdapter;
    int[] listCategoryIDs;
    Button listAddItemBtn;
    ListItemAdapter listItemsAdapter;
    Button listSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        //Get ID from intent
        db = new DatabaseManager(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("ListID", -1);
        // If id == -1 new list else get from DB
        list = (id == -1) ? new List(): new List(db, id);

        // Update Name Field
        listNameField = (EditText) findViewById(R.id.listNameField);
        listNameField.setText(list.getName());

        // Update Completed Checkbox
        listCompleteField = (CheckBox) findViewById(R.id.listCompleteField);
        listCompleteField.setChecked(list.isComplete());

        // Set New Store button
        newStoreBtn = (Button) findViewById(R.id.listNewStoreBtn);
        // REQ ATTN

        // Update Store Spinner
        listStoreSpinner = (Spinner) findViewById(R.id.listStoreSpinner);
        int listStoreSize = DatabaseManager.STORES.size();
        // Prepare Store Names and IDs
        String[] listStoreNames = new String[listStoreSize + 1];
        listStoreNames[0] = "None";
        listStoreIDs = new int[listStoreSize + 1];
        listStoreIDs[0] = -1;
        // Get the List Store ID for testing
        int listStoreID = list.getStore();
        int storeStartPos = 0;
        // Get DB Store names and IDs
        String[] tempListStoreNames = new String[listStoreSize];
        tempListStoreNames = DatabaseManager.STORES.values().toArray(tempListStoreNames);
        Integer[] tempListStoreIDs = new Integer[listStoreSize];
        tempListStoreIDs = DatabaseManager.STORES.keySet().toArray(tempListStoreIDs);
        // Add DB data to adapter array
        for(int i = 0; i < listStoreSize; i++)
        {
            listStoreNames[i + 1] = tempListStoreNames[i];
            listStoreIDs[i + 1] = tempListStoreIDs[i];
            if(listStoreID == tempListStoreIDs[i])
            {
                storeStartPos = i + 1;
            }
        }
        // Finalise adapter and spinner
        listStoreAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStoreNames);
        listStoreSpinner.setAdapter(listStoreAdapter);
        listStoreSpinner.setSelection(storeStartPos);

        // Update Category Spinner
        listCategorySpinner = (Spinner) findViewById(R.id.listCategorySpinner);
        int listCategorySize = DatabaseManager.CATEGORIES.size();
        // Get Category Names and prepare IDs
        String[] listCategoryNames = new String[listCategorySize];
        listCategoryNames = DatabaseManager.CATEGORIES.values().toArray(listCategoryNames);
        listCategoryIDs = new int[listCategorySize];
        // Get the List Category ID for testing
        int listCategoryID = list.getCategory();
        int CategoryStartPos = 0;
        // Get DB Categories IDs
        Integer[] tempListCategoryIDs = new Integer[listCategorySize];
        tempListCategoryIDs = DatabaseManager.CATEGORIES.keySet().toArray(tempListCategoryIDs);
        // Add DB data to adapter array
        for(int i = 0; i < listCategorySize; i++)
        {
            listCategoryIDs[i] = tempListCategoryIDs[i];
            if(listCategoryID == tempListCategoryIDs[i])
            {
                CategoryStartPos = i;
            }
        }
        // Finalise adapter and spinner
        listCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCategoryNames);
        listCategorySpinner.setAdapter(listCategoryAdapter);
        listCategorySpinner.setSelection(CategoryStartPos);

        // Setup Add Item Button
        listAddItemBtn = (Button) findViewById(R.id.listNewItem);

        // Setup Items List View
        listItemsAdapter = new ListItemAdapter(list.getItemList(), this);
        ListView listItemsView = (ListView) findViewById(R.id.listItemVeiw);
        listItemsView.setAdapter(listItemsAdapter);


        // Setup Save Button
        listSaveBtn = (Button) findViewById(R.id.listSaveBtn);
    }

    public void onResume()
    {
        super.onResume();
        int listStoreSize = DatabaseManager.STORES.size();
        // Prepare Store Names and IDs
        listStoreAdapter.clear();
        listStoreAdapter.insert("None", 0);
        listStoreIDs = new int[listStoreSize + 1];
        listStoreIDs[0] = -1;
        // Get the List Store ID for testing
        int listStoreID = list.getStore();
        int storeStartPos = 0;
        // Get DB Store names and IDs
        String[] tempListStoreNames = new String[listStoreSize];
        tempListStoreNames = DatabaseManager.STORES.values().toArray(tempListStoreNames);
        Integer[] tempListStoreIDs = new Integer[listStoreSize];
        tempListStoreIDs = DatabaseManager.STORES.keySet().toArray(tempListStoreIDs);
        // Add DB data to adapter array
        for(int i = 0; i < listStoreSize; i++)
        {
            listStoreAdapter.insert(tempListStoreNames[i], 1);
            listStoreIDs[i + 1] = tempListStoreIDs[i];
            if(listStoreID == tempListStoreIDs[i])
            {
                storeStartPos = i + 1;
            }
        }
        listStoreAdapter.notifyDataSetChanged();
        listCategorySpinner.setSelection(storeStartPos);
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
    public void listAddItemBtnOnClick(View view){
        listItemsAdapter.AddItem();
    }

    public void listSaveBtnOnClick(View view){
        // Make useable DbManager
        DatabaseManager db = new DatabaseManager(this);

        // Update List Object
        list.setName(listNameField.getText().toString());
        list.setItems(listItemsAdapter.GetListItems());
        for(Item i : listItemsAdapter.mRemovedItems)
        {
            db.DeleteItem(i.getId());
        }
        list.setStore(listStoreIDs[listStoreSpinner.getSelectedItemPosition()]);
        list.setCategory(listCategoryIDs[listCategorySpinner.getSelectedItemPosition()]);
        list.setComplete(listCompleteField.isChecked());

        // Apply Changes
        list.SaveChanges(db);

        // Toast
        Toast.makeText(this,"Changes Saved",Toast.LENGTH_LONG).show();

        // Close
        finish();
    }
}

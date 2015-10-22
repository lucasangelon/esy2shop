package codefactory.esy2shop.activites;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
    Button newStoreBtn;
    Spinner listCategorySpinner;
    ArrayAdapter<String> listCategoryAdapter;
    int[] listCategoryIDs;
    Button listAddItemBtn;
    ListItemAdapter listItemsAdapter;

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

        // Set New Store button
        newStoreBtn = (Button) findViewById(R.id.listStoreBtn);

        // Update Category Spinner
        listCategorySpinner = (Spinner) findViewById(R.id.listCategorySpinner);
        int listCategorySize = DatabaseManager.CATEGORIES.size();

        // Get Category Names and prepare IDs
        String[] listCategoryNames = new String[listCategorySize + 1];
        listCategoryNames[0] = "";
        listCategoryIDs = new int[listCategorySize + 1];
        listCategoryIDs[0] = -1;


        // Get the List Category ID for testing
        int listCategoryID = list.getCategory();
        int CategoryStartPos = 0;


        // Get DB Categories IDs
        String[] tempListCategoryNames = DatabaseManager.CATEGORIES.values().toArray(new String[listCategorySize]);
        Integer[] tempListCategoryIDs = DatabaseManager.CATEGORIES.keySet().toArray(new Integer[listCategorySize]);
        // Add DB data to adapter array
        for(int i = 0; i < listCategorySize; i++)
        {
            listCategoryNames[i + 1] = tempListCategoryNames[i];
            listCategoryIDs[i + 1] = tempListCategoryIDs[i];
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
    }



    @Override
    public void onPause()
    {
        listSave();
        super.onPause();
    }



    @Override
     public void onStop()
    {
        listSave();
        super.onStop();
    }


    @Override
    public void onDestroy()
    {
        listSave();
        super.onDestroy();
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

    public void listStoreBtnOnClick(View view){
        // Get the store ID from intent
        Intent storeIntent = new Intent(this, EditStore.class);
        storeIntent.putExtra("StoreID", list.getStore());
        startActivityForResult(storeIntent, 1);
    }

    public void listNotificationBtnOnClick(View view){
        Intent storeIntent = new Intent(this, EditStore.class);
        startActivity(storeIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            list.setStore(data.getIntExtra("StoreID", -1));
        }
        else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            // Deal with Notifications
        }
    }




    @Override
    public void onBackPressed()
    {
        listSave();

        // Toast
        Toast.makeText(this,"Changes Saved",Toast.LENGTH_LONG).show();

        // Close
        finish();
    }



    public void listSave(){
        // Make useable DbManager
        DatabaseManager db = new DatabaseManager(this);

        // Update List Object
        list.setName(listNameField.getText().toString());
        list.setItems(listItemsAdapter.GetListItems());
        for(Item i : listItemsAdapter.mRemovedItems)
        {
            db.DeleteItem(i.getId());
        }
        list.setCategory(listCategoryIDs[listCategorySpinner.getSelectedItemPosition()]);

        // Apply Changes
        list.SaveChanges(db);
    }
}

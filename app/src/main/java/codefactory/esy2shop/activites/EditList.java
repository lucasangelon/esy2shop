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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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
        com.baoyz.swipemenulistview.SwipeMenuListView listItemsView = (com.baoyz.swipemenulistview.SwipeMenuListView) findViewById(R.id.listItemVeiw);
        listItemsView.setAdapter(listItemsAdapter);
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
        listItemsView.setMenuCreator(creator);
        listItemsView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listItemsView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                       listItemsAdapter.remove(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
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
        startActivityForResult(storeIntent, 1);
    }

    public void listNotificationBtnOnClick(View view){
        Intent notificationIntent = new Intent(this, EditNotification.class);
        startActivityForResult(notificationIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            list.setStore(data.getIntExtra("result", -1));
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

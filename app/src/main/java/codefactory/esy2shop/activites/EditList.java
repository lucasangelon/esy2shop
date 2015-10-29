package codefactory.esy2shop.activites;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import codefactory.esy2shop.adapters.ItemAdapter;
import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.models.Item;
import codefactory.esy2shop.models.List;
import codefactory.esy2shop.models.Store;
import codefactory.projectshop.R;

public class EditList extends ActionBarActivity {




    List list;
    DatabaseManager db;
    EditText listNameField;
    Spinner listCategorySpinner;
    ArrayAdapter<String> listCategoryAdapter;
    ArrayList<Item> mRemovedItems;
    int[] listCategoryIDs;
    ItemAdapter itemAdapter;
    Button listAddItemBtn;
    EditText itemAddText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);


        //Init removed items
        mRemovedItems = new ArrayList<>();

        //Get ID from intent
        db = new DatabaseManager(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("ListID", -1);


        // If id == -1 new list else get from DB
        list = (id == -1) ? new List(): new List(db, id);

        // Update Name Field
        listNameField = (EditText) findViewById(R.id.listNameField);
        listNameField.setText(list.getName());

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
        listCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategoryNames);
        listCategorySpinner.setAdapter(listCategoryAdapter);
        listCategorySpinner.setSelection(CategoryStartPos);
        listCategorySpinner.setFocusable(true);
        listCategorySpinner.setFocusableInTouchMode(true);

        // Setup Add Item Button
        listAddItemBtn = (Button) findViewById(R.id.add_item_button);
        itemAddText = (EditText) findViewById(R.id.add_item_editText);
        itemAddText.setFocusableInTouchMode(true);
        itemAddText.setFocusable(true);


        /*
            Ste up Item adapter
         */
        itemAdapter = new ItemAdapter(this, list);
        ListView listItemsView = (ListView) findViewById(R.id.list_item_view);
        listItemsView.setAdapter(itemAdapter);




        /*
            IME handle for next on list name field
         */
        listNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    /*
                        Hide Keyboard
                     */
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(listNameField.getWindowToken(), 0);
                    /*
                        Sets focus after name is entered to the spinner
                     */
                    listCategorySpinner.requestFocus();
                    listCategorySpinner.performClick();

                    handled = true;

                }
                return handled;
            }
        });



        /*
            Ime handler for add items
         */
        itemAddText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    addItem();

                }
                return handled;
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

        //get from adapter (updates checkboxes)
        list = itemAdapter.getList();

        // Update List Object
        list.setName(listNameField.getText().toString());

        //remove items from database
        for(Item i : mRemovedItems)
        {
            db.DeleteItem(i.getId());
        }

        //set the catagory
        list.setCategory(listCategoryIDs[listCategorySpinner.getSelectedItemPosition()]);

        // Apply Changes
        list.SaveChanges(db);
    }

    /*
        When an item is added to the list
     */
    private void addItem(){

        /*
            If txt is empty
         */
        if(itemAddText.getText().toString().trim().equals("")){


            itemAddText.setText("");
            Toast.makeText(EditList.this, "Please enter Tex", Toast.LENGTH_SHORT).show();

        }else{

            /*
                If text field has text
             */

            //Add item to adapter
            Item item = new Item();
            item.setName(itemAddText.getText().toString().trim());
            Toast.makeText(EditList.this, itemAddText.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            item.setComplete(false);
            list.add(item);
            itemAdapter.notifyDataSetChanged();



            //Clear text
            //itemAddText.clearComposingText();

            //Focus
            itemAddText.requestFocus();
            itemAddText.performClick();
            itemAddText.setText("");


        }

    }


    /*
        "add" button is clicked
     */
    public void addItemClick(View view){
        addItem();
    }


    /*
        Item is removed from list
     */
    public void removeItem(int position){

        //Add to removed items
        mRemovedItems.add(list.getItemList().get(position));

        //remove item form list
        list.getItemList().remove(position);

        //Update adapter
        itemAdapter.notifyDataSetChanged();

    }


}

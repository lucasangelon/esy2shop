package codefactory.projectshop.activites;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import codefactory.projectshop.R;
import codefactory.projectshop.models.List;
import codefactory.projectshop.models.Store;

public class EditList extends ActionBarActivity {


    /*
        Views
     */
    private List list;
    private EditText listName;
    private EditText listCatagory;
    private Spinner storeSpinner;
    private CheckBox isComplete;
    private Button saveButton;


    /*
        Test Test Test
    */
    private ArrayList<Store> storeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);


        /*
            Views
         */
        list = new List();
        listName = (EditText) findViewById(R.id.listNameText);
        listCatagory = (EditText) findViewById(R.id.catagoryText);
        storeSpinner = (Spinner) findViewById(R.id.storeSpinner);
        isComplete = (CheckBox) findViewById(R.id.isCompleteBox);
        saveButton = (Button) findViewById(R.id.saveButton);



        /*
            TEST TEST TEST
         */
        storeList = new ArrayList<Store>();

        //Add shops to store list
        storeList.add(new Store("Coles",0,0));
        storeList.add(new Store("Woolies",1,1));
        storeList.add(new Store("IGA",2,2));

        /*
            TEST TEST TEST
         */


        /*
                Populate Spinner
        */
        ArrayList<String> storeNames = new ArrayList<>();

        for(Store addStore : storeList)
            storeNames.add(addStore.getName());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, storeNames);
        storeSpinner.setAdapter(spinnerAdapter);

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
        Store selectedStore = storeList.get(storeSpinner.getSelectedItemPosition());
        saveChanges(listName.getText().toString(),listCatagory.getText().toString(),selectedStore,isComplete.isChecked());


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
    public void saveChanges(String name,String catagory, Store store, boolean complete){

        /*

        Useless anyway

        list.setName(name);
        list.setCatagory(catagory);
        list.setStoreId(store);
        list.setComplete(complete);

        */

    }
}

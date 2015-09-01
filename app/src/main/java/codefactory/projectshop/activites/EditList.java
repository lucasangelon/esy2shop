package codefactory.projectshop.activites;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import codefactory.projectshop.R;
import codefactory.projectshop.models.List;
import codefactory.projectshop.models.Store;

public class EditList extends ActionBarActivity {



    /*
        Test Test Test
     */
    private List list;
    private EditText listName;
    private EditText listCatagory;
    private Spinner storeSpinner;
    private CheckBox isComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        list = new List();

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
    public void onSaveClick(){



    }


    /*
        Saves Changes to a list

        Saving in Variables

        Database will be added later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public void saveChanges(String name,String catagory, Store store, boolean complete){


        list.setName(name);
        list.setCatagory(catagory);
        list.setStoreId(store);
        list.setComplete(complete);

    }
}

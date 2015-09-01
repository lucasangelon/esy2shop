package codefactory.projectshop.activites;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import codefactory.projectshop.R;
import codefactory.projectshop.adapters.ItemAdapter;
import codefactory.projectshop.models.Item;
import codefactory.projectshop.models.List;

public class AddItems extends ActionBarActivity implements View.OnClickListener {



    private Button shopName;
    private Button add;
    private Button clear;
    private ListView shoppingList;
    private List items = new List();
    private ItemAdapter itemsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        /*
            Assign Buttons
         */
        shopName =(Button) findViewById(R.id.shop);
        add =(Button) findViewById(R.id.item);
        clear =(Button) findViewById(R.id.clear);

        /*
            Add Listeners
         */
        shopName.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);


        /*
            Create a list to be used by activity

            This may be sent by intent from previous activity,
            retrieved from global variables or
            loaded from a database in later version

            items is for test only
         */
        items = new List();


        /*
            TEST TEST TEST TEST TEST TEST
         */
        items.add(new Item(1, "Mother"));
        items.add(new Item(2, "Chips"));
        items.add(new Item(3, "Steak"));
        /*
            TEST TEST TEST TEST TEST TEST
         */

        itemsAdapter = new ItemAdapter(items.getItemList(),getApplicationContext());

        //Set adapter to list view
        shoppingList = (ListView) findViewById(R.id.shoppingList);
        shoppingList.setAdapter(itemsAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_items, menu);
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


    public void returnString(int id,String str) {

                items.add(new Item(items.listSize(), str));
                itemsAdapter = new ItemAdapter(items.getItemList(),getApplicationContext());
                shoppingList.setAdapter(itemsAdapter);
    }


    @Override
    public void onClick(View v) {

    }
}

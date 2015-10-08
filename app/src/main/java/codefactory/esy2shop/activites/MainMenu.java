package codefactory.esy2shop.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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
        com.baoyz.swipemenulistview.SwipeMenuListView mainListView = (com.baoyz.swipemenulistview.SwipeMenuListView)findViewById(R.id.mainListView);
        mainListView.setAdapter(listAdapter);

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

        mainListView.setMenuCreator(creator);
        mainListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mainListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        listAdapter.remove(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


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


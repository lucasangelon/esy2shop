package codefactory.esy2shop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import codefactory.esy2shop.models.Item;
import codefactory.projectshop.R;

/**
 *  Item Adapter,
 *  Displays the items in a list
 *
 *  Jack Kitchener - James McNeil
 */
public class LocationsAdapter extends BaseAdapter {


    public ArrayList<Item> items;

    //Added Context
    Context mContext;


    public LocationsAdapter(ArrayList<Item> items, Context mContext){
        this.items = items;
        this.mContext = mContext;

    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position).getName();
    }


    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }



    /*
        Changes 14/9/2015

        Uses a custom layout
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View V = convertView;
        final int finalPos = position;

        if(V == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            V = vi.inflate(R.layout.list_item, null);
        }

        final Item it = items.get(finalPos);


        /*
            Text View
         */
        final TextView txt = (TextView)V.findViewById(R.id.itemText);
        txt.setText(it.getName());


        /*
            Complete button
         */
        Button completeBtn = (Button) V.findViewById(R.id.completeButton);

        //Completes item!

        completeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /// Toggles complete status of item
                it.setComplete(!it.isComplete());

                //Changes text
                if(it.isComplete())
                    txt.setText(it.getName() + " -C");
                else
                    txt.setText(it.getName());

                /*
                    DATABASE STUFF GOOSE HERE
                 */


            }

        });

        /*
            Delete Button
         */
        Button deleteButton = (Button) V.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Delete item from array list
                items.remove(finalPos);

                //Update adapter
                notifyDataSetChanged();

                 /*
                    DATABASE STUFF GOOSE HERE
                 */

            }
        });

        /*
            Edit Button
         */
        Button editButton = (Button) V.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ////// Do stuff here

            }
        });




        return V;
    }
}
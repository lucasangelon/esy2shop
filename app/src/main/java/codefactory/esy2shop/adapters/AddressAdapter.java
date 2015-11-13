package codefactory.esy2shop.adapters;

import android.location.Address;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;

import codefactory.projectshop.R;

/**
 * Created by James
 *
 * Used to display multiple loctions in the google maps activity
 *
 */
public class AddressAdapter extends BaseAdapter {


    ArrayList<Address> addressArrayList;

    @Override
    public int getCount() {
        return addressArrayList.size();
    }


    @Override
    public Address getItem(int position) {
        return addressArrayList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }



    /*
        Creates the View
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;



        if(convertView == null) {

            view = mInflater.inflate(R.layout.item_list_item, parent, false);

        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

    }
}

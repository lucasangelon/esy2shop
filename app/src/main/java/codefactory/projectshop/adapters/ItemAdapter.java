package codefactory.projectshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import codefactory.projectshop.models.Item;

/**
 *  Item Adapter,
 *  Displays the items in a list
 *
 *  Jack Kitchener
 */
public class ItemAdapter extends BaseAdapter {


    public ArrayList<Item> items;

    //Added Context
    Context mContext;


    public ItemAdapter(ArrayList<Item> items, Context mContext){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View V = convertView;

        if(V == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            V = vi.inflate(android.R.layout.simple_list_item_1, null);
        }

        Item it = items.get(position);
        TextView txt = (TextView)V.findViewById(android.R.id.text1);
        txt.setText(it.getName());
        return V;
    }
}
package codefactory.esy2shop.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.PriorityQueue;

import codefactory.esy2shop.activites.AboutActivity;
import codefactory.esy2shop.activites.EditList;
import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.models.List;
import codefactory.projectshop.R;

/**
 * Should help display list of list on main page
 *
 *  Josh, with help from ItemAdapter
 *  Update Dillon (041502996) 5/10/15
 *
 */
public class ShoppingListAdapter extends BaseAdapter {



    LayoutInflater inflater;
    private Context mContext;
    private ArrayList<List> mList;
    ArrayList<List> fullList;





    public ShoppingListAdapter(ArrayList<List> fullList, Context mContext) {

        this.mContext = mContext;
        this.fullList = fullList;
        this.mList = fullList;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }





    public void Update(ArrayList<List> list)
    {
        fullList = list;
        mList = list;
        notifyDataSetChanged();
    }





    public void FilterCategory(int CategoryID)
    {
        mList = new ArrayList<>();
        for(List l : fullList)
        {
            if(CategoryID==1)
                mList.add(l);
            else
            if(CategoryID==6)
            {
                Intent i = new Intent(mContext, AboutActivity.class);
                mContext.startActivity(i);
            }
            if(l.getCategory() == CategoryID)
            {
                mList.add(l);
            }
        }
        notifyDataSetChanged();
    }




    public void FilterStore(int StoreID, boolean OnlyActive)
    {
        mList = new ArrayList<>();
        for(List l : fullList)
        {
            if(l.getStore() == StoreID && !(OnlyActive && l.getProximityAlert()))
            {
                mList.add(l);
            }
        }
        notifyDataSetChanged();
    }




    public void FilterTodays()
    {
        Date today = new Date();
        mList = new ArrayList<List>();
        for(List l : fullList)
        {
            if(l.getDateAlert() == today)
            {
                mList.add(l);
            }
        }
        notifyDataSetChanged();
    }



    public void FilterClear()
    {
        mList = fullList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    //Not used atm
    @Override
    public long getItemId(int position) {
        return mList.get(position).getId();
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final List list = mList.get(position);

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_list, null);
        }

        /*
            Edit Button
         */
        Button editButton = (Button) convertView.findViewById(R.id.editListBtn);
        editButton.setText(list.getName());


        /*
            Generates gradient for the Lists.
         */
        int colorPos = position%6;
        int red = 253;
        int green = (int)(231 -(22/6)*colorPos);
        int blue = (int)(130-(106/6)*colorPos);
        editButton.setBackgroundColor(Color.rgb(red,green,blue));



        /*
            OnClick Listener
            Re-driects to edit list activity.
            Passes the list id to edit list
         */
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toListIntent = new Intent(mContext, EditList.class);
                int listID = list.getId();
                toListIntent.putExtra("ListID", listID);
                mContext.startActivity(toListIntent);
            }});

        return convertView;


    }
}

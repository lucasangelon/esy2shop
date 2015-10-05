package codefactory.esy2shop.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.PriorityQueue;

import codefactory.esy2shop.activites.EditList;
import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.models.List;
import codefactory.projectshop.R;

/**
 * Should help display list of list on main page
 *
 *  Josh, with help from ItemAdapter
 *  Update Dillon (041502996) 5/10/15
 */
public class ListAdapter extends BaseAdapter {

    LayoutInflater inflater;

    private Context mContext;
    private ArrayList<List> mList;

    public ListAdapter (ArrayList<List> list, Context context) {
        mContext = context;
        mList = list;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    public void Update(ArrayList<List> list)
    {
        mList = list;
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
        if(list.isComplete())
        {
            editButton.setPaintFlags(editButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent toListIntent = new Intent(mContext, EditList.class);
                int listID = list.getId();
                toListIntent.putExtra("ListID", listID);
                mContext.startActivity(toListIntent);
            }});

        /*
            Delete Button
        */
        Button deleteButton = (Button) convertView.findViewById(R.id.deleteListBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseManager db = new DatabaseManager(mContext);
                list.Delete(db);
                mList.remove(position);

                //Update adapter
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}

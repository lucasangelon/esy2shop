package codefactory.projectshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import codefactory.projectshop.R;
import codefactory.projectshop.models.Item;
import codefactory.projectshop.models.List;

/**
 * Should help display list of list on main page
 *
 *  Josh, with help from ItemAdapter
 */
public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<List> mList;




    public ListAdapter (ArrayList<List> list, Context context) {
        this.mList = list;
        this.mContext = context;


    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position).getName();
    }

    //Not used atm
    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View V = convertView;
        final int finalPos = position;

        if (V == null) {

            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            V = vi.inflate(R.layout.main_list, null);

        }

        final List list = mList.get(finalPos);

        /*
            Inital Views
         */
        TextView listTitleLabel = (TextView) V.findViewById(R.id.listTitleLabel);
        Button deleteButton = (Button) V.findViewById(R.id.deleteButton);
        Button editButton = (Button) V.findViewById(R.id.editButton);

        /*
            Set label name and buttons
         */
        listTitleLabel.setText(list.getName());
        deleteButton.setText("D");
        editButton.setText("E");

        /*
            Delete button listener
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*
                    Whats happens when you delete a list
                 */

                mList.remove(finalPos);

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
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Navigate to edit List fragment/screen

            }
        });

        return V;
    }

    private static class ViewHolder {
        TextView listTitleLabel;
        Button editButton;
        Button deleteButton;
    }


}

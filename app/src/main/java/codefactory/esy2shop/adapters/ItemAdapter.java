package codefactory.esy2shop.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import codefactory.esy2shop.models.Item;
import codefactory.esy2shop.models.List;
import codefactory.projectshop.R;

/**
 * Created by James on 10/27/2015.
 */
public class ItemAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private List itemList;
    Context mContext;


    public ItemAdapter(Context mContext, List itemList){

        this.itemList = itemList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);

    }


    @Override
    public int getCount() {
        return itemList.getItemList().size();
    }


    @Override
    public Item getItem(int position) {
        return itemList.getItemList().get(position);
    }


    @Override
    public long getItemId(int position) {
        return itemList.getItemList().get(position).getId();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder = new ViewHolder();



        if(convertView == null) {

            view = mInflater.inflate(R.layout.item_list_item, parent, false);

        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }


        // Init views
        holder.completeCheckBox = (CheckBox) view.findViewById(R.id.item_complete_checkbox);
        holder.itemEditText = (EditText) view.findViewById(R.id.edit_List_Text);


            /*
                Set Text and Checkbox
             */
        holder.completeCheckBox.setChecked(itemList.getItemList().get(position).isComplete());
        holder.itemEditText.setText(itemList.getItemList().get(position).getName());



            /*
                Set listener for checkbox
             */
        holder.completeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                   /*
                                                                      Changes top the current state of the checkbox
                                                                   */
                                                                   itemList.getItemList().get(position).setComplete(isChecked);
                                                               }
                                                           }
        );


        view.setTag(holder);
        return view;

    }




    public class ViewHolder{

        public CheckBox completeCheckBox;
        public EditText itemEditText;

    }



    public List getList(){
        return itemList;
    }




}

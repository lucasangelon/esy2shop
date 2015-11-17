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
import android.widget.TextView;
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


    public void remove(int index)
    {
        itemList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged()
    {

        super.notifyDataSetChanged();
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
        holder.itemEditText = (TextView) view.findViewById(R.id.edit_List_Text);


            /*
                Set Text and Checkbox
             */

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
        if(holder.completeCheckBox.isChecked()&& !itemList.getItemList().get(position).isComplete())
            holder.completeCheckBox.performClick();

        if(!holder.completeCheckBox.isChecked()&& itemList.getItemList().get(position).isComplete())
            holder.completeCheckBox.performClick();

        //dont use setChecked as it will bug out
        view.setTag(holder);
        return view;

    }




    public class ViewHolder{

        public CheckBox completeCheckBox;
        public TextView itemEditText;

    }



    public List getList(){
        return itemList;
    }




}

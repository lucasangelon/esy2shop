package codefactory.esy2shop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import codefactory.esy2shop.models.Item;
import codefactory.esy2shop.models.List;
import codefactory.esy2shop.models.Store;
import codefactory.projectshop.R;

/**
 * Created by Dillon on 5/10/2015.
 *
 *
 * Use in edit list activity.
 * Inflates item_list_item
 */
public class ListItemAdapter extends BaseAdapter {


    LayoutInflater inflater;
    private Context mContext;
    private ArrayList<ItemHandler> itemHandlers = new ArrayList<ItemHandler>();
    public ArrayList<Item> mRemovedItems = new ArrayList<Item>();


    public ListItemAdapter (ArrayList<Item> items, Context context) {
        mContext = context;
        for(Item i : items)
        {
            itemHandlers.add(new ItemHandler(i));
        }
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    public void Update(ArrayList<Item> items)
    {
        itemHandlers = new ArrayList<ItemHandler>();
        for(Item i : items)
        {
            itemHandlers.add(new ItemHandler(i));
        }
        notifyDataSetChanged();
    }

    public void addItem()
    {
        GetListItems();
        itemHandlers.add(new ItemHandler(new Item()));
        notifyDataSetChanged();
    }

    /*
        27.10.15
     */
    public void addItem(String itemName){

        GetListItems();
        itemHandlers.add(new ItemHandler());

    }

    @Override
    public int getCount() {
        return itemHandlers.size();
    }

    @Override
    public Object getItem(int position) {
        return itemHandlers.get(position).item;
    }

    //Not used atm
    @Override
    public long getItemId(int position) {
        return itemHandlers.get(position).item.getId();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_list_item, null);
        }

        itemHandlers.get(position).SetCompletedField((CheckBox) convertView.findViewById(R.id.item_complete_checkbox));
        itemHandlers.get(position).SetItemNameField((EditText) convertView.findViewById(R.id.edit_List_Text));


        /*
                Need to be uncommented when swipe is in -------------
         */
/*        Button deleteBtn = (Button) convertView.findViewById(R.id.listItemDelete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemovedItems.add(itemHandlers.get(position).item);
                itemHandlers.remove(position);
                notifyDataSetChanged();
            }
        });*/



        return convertView;
    }




    public ArrayList<Item> GetListItems()
    {
        ArrayList<Item> result = new ArrayList<Item>();

        for(ItemHandler i : itemHandlers)
        {
            Item tempItem = i.returnUpdate();
            if (!tempItem.getName().equals(""))
            {
                result.add(tempItem);
            }
        }

        return result;
    }






    private class ItemHandler
    {
        public Item item;
        private CheckBox completedField;
        private EditText itemNameField;

        ItemHandler(final Item item)
        {
            this.item = item;
        }

        public void SetCompletedField(CheckBox completedField)
        {
            this.completedField = completedField;
            completedField.setChecked(item.isComplete());
        }

        public void SetItemNameField(EditText itemNameField)
        {
            this.itemNameField = itemNameField;
            itemNameField.setText(item.getName());
        }

        public Item returnUpdate(String name)
        {
            item.setComplete(completedField.isChecked());
            //item.setName(itemNameField.getText().toString());
            item.setName(name);
            return item;
        }

    }
}

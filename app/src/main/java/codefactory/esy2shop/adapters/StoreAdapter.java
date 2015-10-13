package codefactory.esy2shop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

import codefactory.esy2shop.database.DatabaseManager;
import codefactory.esy2shop.models.Store;
import codefactory.projectshop.R;

/**
 * Created by Dillon on 9/10/2015.
 */
public class StoreAdapter extends BaseAdapter {

    LayoutInflater inflater;
    @LayoutRes int searched = R.layout.item_store_search;
    @LayoutRes int existing = R.layout.item_store_existing;
    @LayoutRes int using;

    Context mContext;
    boolean expectResult;
    DatabaseManager db;
    ArrayList<Store> storeList;


    // Include the GoogleMaps portion to store it in here
    public StoreAdapter(Context context, boolean ExpectResult)
    {
        mContext = context;
        expectResult = ExpectResult;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseManager(context);

        using = existing;
        storeList = db.GetStores(null);
        UpdateMapMarker(storeList);
    }

    public void Update(String search)
    {
        using = searched;
        storeList = GetMapMarkers(search);
        UpdateMapMarker(storeList);
        notifyDataSetChanged();
    }

    public void Update()
    {
        using = existing;
        storeList = db.GetStores(null);
        UpdateMapMarker(storeList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return storeList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView == null)
        {
            convertView = inflater.inflate(using, null);
        }

        final Store store = storeList.get(position);

        Button selectBtn = (Button) convertView.findViewById(R.id.chooseStoreBtn);
        selectBtn.setText(storeList.get(position).getName());
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.UpdateStore(store);

                if(expectResult)
                {
                    Intent intent = new Intent();
                    intent.putExtra("StoreID", store.getId());
                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                    ((Activity) mContext).finish();
                }
                else
                {
                    UpdateMapMarker(store);
                    Update();
                }
            }
        });

        if(using == existing)
        {
            Button deleteBtn = (Button) convertView.findViewById(R.id.deleteStoreBtn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Just a double check that we are at the right stage of using the adapter and not outside of this
                    if (storeList.contains(store)) {
                        store.Delete(db);
                        Update();
                    }
                }
            });
        }

        return convertView;
    }

    public void onBack()
    {
        if(using == existing)
        {
            if(expectResult)
            {
                ((Activity) mContext).setResult(Activity.RESULT_CANCELED);
            }
            ((Activity) mContext).finish();
        }
        else
        {
            Update();
        }
    }

    public void UpdateMapMarker(int StoreID)
    {
        for(Store s : storeList)
        {
            if(s.getId() == StoreID)
            {
                UpdateMapMarker(s);
                break;
            }
        }
    }

    public void UpdateMapMarker(Store store)
    {
        if(storeList.contains(store))
        {
            storeList.remove(store);
            storeList.add(0, store);
            notifyDataSetChanged();

            ArrayList<Store> result = new ArrayList<Store>();
            result.add(store);
            UpdateMapMarker(result);
        }
    }

    // REQ ATTN
    // These should influence the Google Maps component
    public void UpdateMapMarker(ArrayList<Store> Stores)
    {

    }

    public ArrayList<Store> GetMapMarkers(String search)
    {
        ArrayList<Store> result = new ArrayList<Store>();
        result.add(new Store("TEST SEARCHED STORE", 0, 0));
        return result;
    }
}

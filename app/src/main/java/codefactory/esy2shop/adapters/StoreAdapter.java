package codefactory.esy2shop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    GoogleMap mMap;
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

    public void SetGoogleMap(GoogleMap googleMap)
    {
        mMap = googleMap;
    }

    public void Update(String search)
    {

        ArrayList<Store> searchResult = GetMapMarkers(search);
        if(searchResult.size() > 0)
        {
            using = searched;
            storeList = searchResult;
            UpdateMapMarker(storeList);
            notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(mContext, "Search Returned No Results", Toast.LENGTH_SHORT).show();
        }
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

                int dbID = db.UpdateStore(store);

                if(expectResult)
                {
                    Intent intent = new Intent();
                    intent.putExtra("StoreID", dbID);
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
        if(mMap != null)
        {
            mMap.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(Store s : Stores)
            {
                MarkerOptions storeMarker = new MarkerOptions();

                storeMarker.title(s.getName());
                LatLng storePos = new LatLng(s.getLatitude(), s.getLongitude());
                storeMarker.position(storePos);

                mMap.addMarker(storeMarker);
                builder.include(storeMarker.getPosition());
            }
            CameraUpdate mapMove = CameraUpdateFactory.newLatLngBounds(builder.build(), 5);
            mMap.moveCamera(mapMove);
        }
    }

    public ArrayList<Store> GetMapMarkers(String search)
    {
        ArrayList<Store> result = new ArrayList<Store>();
        try {
            Geocoder gcode = new Geocoder(mContext, Locale.getDefault());
            List<Address> searchList = gcode.getFromLocationName(search, 10);
            if(searchList.size() > 0)
            {
                for(Address a : searchList)
                {
                    result.add(new Store(a.getFeatureName(), a.getLongitude(), a.getLatitude()));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
}

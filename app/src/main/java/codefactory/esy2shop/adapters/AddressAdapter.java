package codefactory.esy2shop.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import codefactory.esy2shop.activites.Notification;
import codefactory.projectshop.R;

/**
 * Created by James
 *
 * Used to display multiple loctions in the google maps activity
 *
 */
public class AddressAdapter extends BaseAdapter {


    private ArrayList<Address> addressArrayList;
    private LayoutInflater mInflater;
    private Context mContext;


    /*
        Construct
     */
    public AddressAdapter(Context mContext,ArrayList<Address> addressArrayList){

        this.mContext = mContext;
        this.addressArrayList = addressArrayList;
        mInflater = LayoutInflater.from(mContext);

    }

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

     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        AddressViewHolder mViewHolder;
        final Address address;

        /*
            Initiase Views
         */
        if(convertView == null) {


            convertView = mInflater.inflate(R.layout.item_address_result_layout, parent, false);
            mViewHolder = new AddressViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (AddressViewHolder) convertView.getTag();
        }

        //Gets the Address
        address = addressArrayList.get(position);

        /*
            Listeners and View Content
         */

        //Image Button
        mViewHolder.markerLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Marker Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        // Name
        mViewHolder.addressNameTextView.setText(address.getFeatureName());

        //Details
        mViewHolder.addressDetailTextView.setText(address.getLocality() + ", " + address.getAddressLine(1));



        //Layout ---- This adds the location to the list as a notification
        mViewHolder.addressNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Layout Pressed\n" + address.getFeatureName(), Toast.LENGTH_SHORT).show();

                /*
                    Redirects to notification page -
                    Parses the selected address in the intent
                 */
                Intent notficationIntent = new Intent(mContext, Notification.class);
                notficationIntent.putExtra("storeAddress", address); // Address is parcelable, uses getParcelable to retrieve in notifications
                mContext.startActivity(notficationIntent);

            }
        });

        return convertView;

    }




    /*
        View Holder
     */
    private class AddressViewHolder{

        /*
            Views in item_address_result_layout
         */
        TextView addressNameTextView;
        TextView addressDetailTextView;
        RelativeLayout addressNameLayout;
        ImageButton markerLocationButton;

        public AddressViewHolder(View item) {

            addressNameTextView = (TextView) item.findViewById(R.id.address_name_text_view);
            addressDetailTextView = (TextView) item.findViewById(R.id.address_detail_text_view);
            addressNameLayout = (RelativeLayout) item.findViewById(R.id.address_name_detail_layout);
            markerLocationButton = (ImageButton) item.findViewById(R.id.address_icon_button);


        }

    }
}

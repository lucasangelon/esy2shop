package codefactory.esy2shop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import codefactory.esy2shop.models.ListNotifcation;
import codefactory.projectshop.R;

/**
 * Handles notification on the notification page
 *
 * inflates item_notifications
 *
 */
public class NotificationAdapter extends BaseAdapter{



    private LayoutInflater mInflater;
    private ArrayList<ListNotifcation> notifcationArrayList;
    Context mContext;

    public NotificationAdapter(Context mContext, ArrayList<ListNotifcation> notifcationArrayList) {

        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.notifcationArrayList = notifcationArrayList;

    }



    @Override
    public int getCount() {
        return notifcationArrayList.size();
    }



    @Override
    public Object getItem(int position) {
        return notifcationArrayList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        ViewHolder viewHolder;


        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.item_notification, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }



        // Set text for each notification
        viewHolder.titleTextView.setText(notifcationArrayList.get(position).getTitle());
        viewHolder.detailsTextView.setText(notifcationArrayList.get(position).getDetails());


        //Sets image if date or geo notification
        if(notifcationArrayList.get(position).isTime()){

            //If Notification is a time notification
            viewHolder.iconImageView.setImageResource(R.mipmap.bell_icon);

        } else {

            //if Notification is a geo notification
            viewHolder.iconImageView.setImageResource(R.mipmap.gps_icon);
        }



        //When user removes notification
        viewHolder.removeNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Removes notifcication
                notifcationArrayList.remove(position);

                //Updates adapter
                notifyDataSetChanged();

            }
        });



        //Return the view
        return convertView;
    }


    /**
     *  Contains the view used in item_notification
     */
    public class ViewHolder{

        TextView titleTextView;
        TextView detailsTextView;
        ImageButton removeNotificationButton;
        ImageView iconImageView;


        public ViewHolder(View view){

            //Initialise Views
            this.titleTextView = (TextView) view.findViewById(R.id.notification_item_title);
            this.detailsTextView = (TextView) view.findViewById(R.id.notification_item_deatails);
            this.iconImageView = (ImageView) view.findViewById(R.id.notification_item_image);
            this.removeNotificationButton = (ImageButton) view.findViewById(R.id.notification_item_delete_button);

        }

    }





}
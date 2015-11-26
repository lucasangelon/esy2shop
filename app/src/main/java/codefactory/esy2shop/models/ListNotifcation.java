package codefactory.esy2shop.models;

import android.location.Address;
import android.telecom.Call;

import java.util.Calendar;

/**
 * List Notification
 *
 * Holds details of notification created by user
 * for a certain list
 */
public class ListNotifcation {


    /**
        isTime is true if the notification is a time notifcation
        False if a geo Notification
     */
    private boolean isTime;


    /**
     *  Notification strings to display.
     */
    private String title;
    private String details;

    /**
        The id from the database to which this notification is attached
     */
    private int listID;

    /**
     * Calender for the date if a date notification
     */
    private Calendar notifyTime;

    /**
     * Address of the notification if a geo notification
     * (Radius of geo fence is always 1 km)
     */
    private Address notifyAddress;


    /**
     * Default constructor
     *
     * @param listID
     * @param isTime
     */
    public ListNotifcation(int listID,boolean isTime){

        this.listID = listID;
        this.isTime = isTime;

    }


    public boolean isTime() {
        return isTime;
    }

    public void setIsTime(boolean isTime) {
        this.isTime = isTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public Calendar getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Calendar notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Address getNotifyAddress() {
        return notifyAddress;
    }

    public void setNotifyAddress(Address notifyAddress) {
        this.notifyAddress = notifyAddress;
    }
}

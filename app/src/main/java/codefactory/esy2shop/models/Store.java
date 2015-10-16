package codefactory.esy2shop.models;

import android.content.Context;

import java.util.Map;
import java.util.TreeMap;

import codefactory.esy2shop.database.DatabaseManager;

/**
 * Store
 *
 * Saves the store associated with a list
 *
 * Created by 041401076 on 20/08/2015
 * Updated by 041502996 on 17/09/2015
 */
public class Store {

    /*
        Vars
     */
    private int id;
    private String name;
    private double latitude;
    private double longitude;

    /*
        Constructors
     */
    public Store(String name, double longitude, double latitude){
        this.id = -1;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Store(Context context, int ID){
        DatabaseManager db = new DatabaseManager(context);
        Store dbStore = db.GetStore(ID);
        if(dbStore != null) {
            this.id = dbStore.getId();
            this.name = dbStore.getName();
            this.longitude = dbStore.getLongitude();
            this.latitude = dbStore.getLatitude();
        }
    }

    public Store(int id, String name, double longitude, double latitude){
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public boolean Delete(DatabaseManager db)
    {
        return db.DeleteStore(id);
    }

    /*
        Get and Set
     */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

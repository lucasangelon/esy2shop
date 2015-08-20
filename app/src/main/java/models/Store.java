package models;

/**
 * Store
 *
 * Saves the store assicitated with a list
 *
 * Created by 041401076 on 20/08/2015.
 */
public class Store {

    /*
        Vars
     */

    private String name;
    private int id;
    private double latitude;
    private double longitude;


    /*
        Constructors
     */

    public Store(){}

    public Store(int id, String name, double longitude,double latitude){

        this.id=id;
        this.name=name;
        this.longitude=longitude;
        this.latitude=latitude;

    }

    /*
        Get and Set
     */

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }


    

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

}

package codefactory.projectshop.models;

import java.util.ArrayList;

/**
 * List
 *
 * Stores a list of items
 *
 * Created by 041402465 on 20/08/2015.
 */
public class List{

    /*
        Vars
     */
    ArrayList<Item> items;
    String name;
    String catagory;
    boolean complete;
    Store store;


    /*
        Constructors
     */
    public List(){

        items = new ArrayList<Item>();
        complete = false;

    }


    public List(String name, String catagory){
        items = new ArrayList<Item>();
        this.name = name;
        this.catagory = catagory;
        complete = false;
    }


    /*
        Methods
     */

    /**
     * Adds an Item to the arraylist
     * @param item
     */
    public void add(Item item){
        items.add(item);
    }

    /**
     * Removes the Item at the specified index
     * @param index
     */
    public void remove(int index){
        items.remove(index);
    }

    /**
     * Returns the size of the list
     * @return int
     */
    public int listSize(){
        return items.size();
    }

    /**
     * Returns a single item
     *
     * @param index
     * @return Item
     */
    public Item getItem(int index){
        return items.get(index);
    }

    /*
        Getters and Setters
     */
    public String getCatagory() {
        return catagory;
    }

    public Store getStoreId() {
        return store;
    }

    public ArrayList<Item> getItemList() {
        return items;
    }

    public String getName() {
        return name;
    }

    public boolean isComplete() {
        return complete;
    }

    //--------------------------

    public void setStoreId(Store store) {
        this.store = store;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public void setName(String name) {
        this.name = name;
    }
}

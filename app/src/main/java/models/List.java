package models;

import java.util.ArrayList;
import java.util.HashMap;

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
    int catagory;
    boolean complete;

    List(String p_name, int p_catagory){
        items = new ArrayList<Item>();
        name = p_name;
        catagory = p_catagory;
    }

    public void add(Item item){
        items.add(item);
    }

    public void remove(int index){
        items.remove(index);
    }

}

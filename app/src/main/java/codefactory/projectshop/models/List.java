package codefactory.projectshop.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import codefactory.projectshop.database.DatabaseManager;

/**
 * List
 *
 * Stores a list of items
 *
 * Created by 041402465 on 20/08/2015.
 * Updated by 041502996 on 16/09/2015
 */
public class List{

    /*
        Vars
     */
    int id;
    String name;
    ArrayList<Item> items;
    Store store;
    int category;
    boolean complete;

    /*
        Constructors
     */
    public List(){
        this.id = -1;
        this.name = "";
        this.items = new ArrayList<Item>();
        this.store = null;
        this.category = 0;
        this.complete = false;
    }

    public List(Context context, int ID){
        DatabaseManager db = new DatabaseManager(context);
        List dbList = db.GetList(ID);
        if(dbList != null) {
            this.id = dbList.id;
            this.name = dbList.name;
            this.items = dbList.items;
            this.store = dbList.store;
            this.category = dbList.category;
            this.complete = dbList.complete;
        }
    }

    public List(int id, String name, ArrayList<Item> items, Store store, int category, boolean complete){
        this.id = id;
        this.name = name;
        this.items = items;
        this.store = store;
        this.category = category;
        this.complete = complete;
    }

    public void SaveChanges(Context context)
    {
        DatabaseManager db = new DatabaseManager(context);
        // As per hierarchy
        // Update Store ?and Category? first
        store.setId(db.UpdateStore(store));
        // DEBUG PLACEHOLDER CATEGORY
        category = db.UpdateCategory(-1, "General");

        // Then update List
        id = db.UpdateList(this);

        // Finally update Items ?and notifications?
        for(Item i : items)
        {
            i.setId(db.UpdateItem(i, id));
        }
    }

    public boolean Delete(Context context)
    {
        DatabaseManager db = new DatabaseManager(context);
        boolean result = true;
        for(Item i : items)
        {
            if(db.DeleteItem(i.getId()))
            {
                result = false;
            }
        }
        if(db.DeleteList(id))
        {
            result = false;
        }
        return result;
    }

    /*
        Get and Set
     */
    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<Item> getItemList() {
        return items;
    }


    /**
     * Returns the size of the list
     * @return int
     */
    public int listSize(){
        return items.size();
    }
    public void add(Item item){
        items.add(item);
    }
    public void remove(Item item){
        items.remove(item);
    }
    public Item getItem(int id, String name){
        for(int i = 0; i < items.size(); i++)
        {
            if(items.get(i).getId() == id && items.get(i).getName().equals(name))
            {
                return items.get(i);
            }
        }
        return null;
    }

    public Store getStore() {
        return store;
    }
    public void setStore(Store store) {
        this.store = store;
    }

    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isComplete() {
        return complete;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}

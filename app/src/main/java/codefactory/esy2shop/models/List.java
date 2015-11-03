package codefactory.esy2shop.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import codefactory.esy2shop.database.DatabaseManager;

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
    int store;
    boolean proximityAlert;
    int category;
    Date dateAlert;

    /*
        Constructors
     */
    public List(){
        this.id = -1;
        this.name = "";
        this.items = new ArrayList<Item>();
        this.store = -1;
        this.proximityAlert = true;
        this.category = 0;
        this.dateAlert = null;
    }

    public List(DatabaseManager db, int ID){
        List dbList = db.GetList(ID);
        if(dbList != null) {
            this.id = dbList.id;
            this.name = dbList.name;
            this.items = dbList.items;
            this.store = dbList.store;
            this.proximityAlert = dbList.proximityAlert;
            this.category = dbList.category;
            this.dateAlert = dbList.dateAlert;
        }
        else
        {
            this.id = -1;
            this.name = "";
            this.items = new ArrayList<Item>();
            this.store = -1;
            this.proximityAlert = true;
            this.category = 0;
            this.dateAlert = null;
        }
    }

    public void remove(int index)
    {
        items.remove(index);
    }

    public List(int id, String name, ArrayList<Item> items, int store, boolean proximityAlert, int category, Date dateAlert){
        this.id = id;
        this.name = name;
        this.items = items;
        this.store = store;
        this.proximityAlert = proximityAlert;
        this.category = category;
        this.dateAlert = dateAlert;
    }



    public void SaveChanges(DatabaseManager db)
    {
        // As per hierarchy
        // Then update List
        id = db.UpdateList(this);

        // Finally update Items
        for(Item i : items)
        {
            i.setId(db.UpdateItem(i, id));
        }
    }




    public boolean Delete(DatabaseManager db)
    {
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
    public void setItems(ArrayList<Item> items)
    {
        this.items = items;
    }
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

    public int getStore() {
        return store;
    }
    public void setStore(int store) {
        this.store = store;
    }
    public boolean getProximityAlert(){
        return proximityAlert;
    }
    public void setProximityAlert(boolean proximityAlert){
        this.proximityAlert = proximityAlert;
    }

    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }

    public Date getDateAlert(){
        return dateAlert;
    }
    public void setDateAlert(Date dateAlert){
        this.dateAlert = dateAlert;
    }
}

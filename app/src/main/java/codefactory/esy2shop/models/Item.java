package codefactory.esy2shop.models;

import android.content.Context;

import codefactory.esy2shop.database.DatabaseManager;

/**
 * Item
 *
 * One item stored on A list
 *
 * Created by 041402465 on 20/08/2015
 * Updated by 041502996 on 16/09/2015
 */
public class Item {

    /*
        Vars
     */
    private int id;
    private String name;
    private boolean complete;

    /*
        Constructors
     */
    public Item(){
        this.id = -1;
        this.name = "";
        this.complete = false;
    }

    public Item(int id, String name){

        this.id = id;
        this.name = name;
        complete = false;

    }

    public Item(int id, String name, boolean complete){
        this.id=id;
        this.name=name;
        this.complete = complete;
    }

    public boolean Delete(DatabaseManager db)
    {
        return db.DeleteItem(id);
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

    public boolean isComplete() {
        return complete;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}

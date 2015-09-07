package codefactory.projectshop.models;

/**
 * Item
 *
 * One item stored on A list
 *
 * Created by 041402465 on 20/08/2015.
 */
public class Item {

    /*
        Vars
     */
    private String name;
    private boolean complete;
    private int id;

    /*
        Constructors
     */
    public Item(){}

    public Item(int id,String name){

        this.name=name;
        this.id=id;
        /*
            complate always defaults to false
         */
        complete = false;

    }


    /*
        Get and Set
     */

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

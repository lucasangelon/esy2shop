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
    private int id;
    private String name;
    private boolean complete;

    /*
        Constructors
     */
    public Item(){}

    public Item(int id,String name){

        this.id=id;
        this.name=name;

        /*
            complate always defaults to false
         */
        complete = false;

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

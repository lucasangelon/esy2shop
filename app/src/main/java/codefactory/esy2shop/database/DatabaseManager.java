package codefactory.esy2shop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import codefactory.esy2shop.models.Item;
import codefactory.esy2shop.models.List;
import codefactory.esy2shop.models.Store;

/**
 * Created by Dillon on 15/09/2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;

    // Global static Maps, used to access Stores, Categories and Lists ID/Name combo from anywhere
    public static Map<Integer, String> CATEGORIES;
    public static Map<Integer, String> STORES;
    public static Map<Integer, String> LISTS;

    /*
    ALL DATABASE VARIABLES
     */
    public static final String DATABASE_NAME = "ezy2shop.db";
    public static int DATABASE_VERSION = 1;

    public static final String TABLE_CATEGORY = "category";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    final String SQL_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_CATEGORY_NAME + " TEXT NOT NULL"
            + ");";

    public static final String TABLE_STORE = "store";
    public static final String COLUMN_STORE_ID = "store_id";
    public static final String COLUMN_STORE_NAME = "store_name";
    public static final String COLUMN_STORE_LAT = "store_lat";
    public static final String COLUMN_STORE_LONG = "store_long";
    final String SQL_STORE = "CREATE TABLE " + TABLE_STORE + "("
            + COLUMN_STORE_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_STORE_NAME + " TEXT NOT NULL, "
            + COLUMN_STORE_LAT + " DOUBLE NOT NULL, "
            + COLUMN_STORE_LONG + " DOUBLE NOT NULL"
            + ");";

    public static final String TABLE_LIST = "list";
    public static final String COLUMN_LIST_ID = "list_id";
    // FK to COLUMN_STORE_ID
    // FK to COLUMN_CATEGORY_ID
    public static final String COLUMN_LIST_NAME = "list_name";
    public static final String COLUMN_LIST_ALERT_DATE = "list_alert_date";
    public static final String COLUMN_LIST_ALERT_PROXIMITY = "list_alert_proximity";
    final String SQL_LIST = "CREATE TABLE " + TABLE_LIST + "("
            + COLUMN_LIST_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_STORE_ID + " INTEGER, "
            + COLUMN_CATEGORY_ID + " INTEGER, "
            + COLUMN_LIST_NAME + " TEXT NOT NULL, "
            + COLUMN_LIST_ALERT_DATE + " TEXT, "
            + COLUMN_LIST_ALERT_PROXIMITY + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_STORE_ID + ") REFERENCES " + TABLE_STORE + "(" + COLUMN_STORE_ID + "), "
            + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + ")"
            + ");";

    public static final String TABLE_ITEM = "item";
    public static final String COLUMN_ITEM_ID = "item_id";
    // FK to COLUMN_LIST_ID
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_COMPLETE = "item_complete";
    final String SQL_ITEM = "CREATE TABLE " + TABLE_ITEM + "("
            + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_LIST_ID + " INTEGER NOT NULL, "
            + COLUMN_ITEM_NAME + " TEXT NOT NULL, "
            + COLUMN_ITEM_COMPLETE + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_LIST_ID + ") REFERENCES " + TABLE_LIST + "(" + COLUMN_LIST_ID + ")"
            + ");";

    /*
    Database Constructors
     */
    public DatabaseManager(Context Context) {
        super(Context, DATABASE_NAME, null, DATABASE_VERSION);
        context = Context;
        db = getReadableDatabase();
    }

    public DatabaseManager(Context Context, boolean updateGlobal) {
        super(Context, DATABASE_NAME, null, DATABASE_VERSION);
        context = Context;
        db = getReadableDatabase();
        if(updateGlobal)
        {
            UpdateCategory(2, "Work");
            UpdateCategory(3, "Personal");
            Map<Integer, String> empty = new TreeMap<Integer, String>();
            UpdateStore(new Store(1, "Store1", 1, 1, empty));
            UpdateStore(new Store(2, "Store2", 2, 2, empty));
            UpdateStore(new Store(3, "Store3", 3, 3, empty));
            Log.d("", "Placeholder Values Updated from 'new DatabaseManager(Context, boolean == true)'");

            CATEGORIES = GetCategoryMap(null);
            STORES = GetStoreMap(null);
            LISTS = GetListMap(null);
        }
    }

    /*
    Super Overrides
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CATEGORY);
        db.execSQL(SQL_STORE);
        db.execSQL(SQL_LIST);
        db.execSQL(SQL_ITEM);
        Log.d("", "All tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQL_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + SQL_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + SQL_STORE);
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CATEGORY);
        Log.d("", "All tables dropped");
        DATABASE_VERSION = newVersion;
        onCreate(db);
    }

    /*
    CATEGORIES
    As categories don't have an object, only Get method is GetCategoryMap
     */
    // GetCategoryMap(String Where), Returns Map<Category_ID, Category_Name> Where
    // use Where == 'null' for all categories
    private Map<Integer, String> GetCategoryMap(String Where)
    {
        String[] columns = new String[]{COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME};
        Cursor dbResult = db.query(TABLE_CATEGORY, columns, Where, null, null, null, null);

        Map<Integer, String> result = new TreeMap<Integer, String>();
        while(dbResult.moveToNext()) {
            int tempCategoryID = dbResult.getInt(0);
            String tempCategoryName = dbResult.getString(1);
            result.put(tempCategoryID, tempCategoryName);
        }
        return result;
    }

    // UpdateCategory(int ID, String name)
    // Inserts/Update the category passed to it
    // Updates the DatabaseManager's static CATEGORIES at end
    // Returns the Category_ID, used to reference category_id on return
    public int UpdateCategory(int ID, String name) {

        ContentValues dbValues = new ContentValues();
        dbValues.put(COLUMN_CATEGORY_NAME, name);

        int result = -1;
        Map<Integer, String> test = GetCategoryMap(null);
        if (test.containsKey(ID))
        {
            db.update(TABLE_CATEGORY, dbValues, COLUMN_CATEGORY_ID + " = " + ID, null);
            result = ID;
        }
        else
        {
            dbValues.put(COLUMN_CATEGORY_ID, ID);
            long rowID = db.insert(TABLE_CATEGORY, null, dbValues);
            Cursor dbResult = db.query(TABLE_CATEGORY, new String[]{COLUMN_CATEGORY_ID}, "ROWID = " + rowID, null, null, null, null);
            dbResult.moveToFirst();
            result = dbResult.getInt(0);
        }

        CATEGORIES = GetCategoryMap(null);
        return result;
    }

    // DeleteCategory(int ID)
    // Attempts to delete the Category_ID row
    // Returns true if deleted, and updates static CATEGORIES
    public boolean DeleteCategory(int ID)
    {
        boolean result = db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = " + ID, null) > 0;
        if(result)
        {
            CATEGORIES = GetStoreMap(null);
        }
        return result;
    }

    /*
    STORES
     */
    // GetStoreMap(String Where), Returns Map<Store_ID, Store_Name> Where
    // use Where == 'null' for all categories
    // Used to return basic info (ID and Name)
    private Map<Integer, String> GetStoreMap(String Where)
    {
        String[] columns = new String[]{COLUMN_STORE_ID, COLUMN_STORE_NAME};
        Cursor dbResult = db.query(TABLE_STORE, columns, Where, null, null, null, null);

        Map<Integer, String> result = new TreeMap<Integer, String>();
        while(dbResult.moveToNext()) {
            int tempStoreID = dbResult.getInt(0);
            String tempStoreName = dbResult.getString(1);
            result.put(tempStoreID, tempStoreName);
        }
        return result;
    }

    // GetStore(int ID), Returns Store, null if not found
    // Used to access GetStores with ID as Where-clause to return a single Store
    public Store GetStore(int storeID)
    {
        ArrayList<Store> dbTemp = GetStores(COLUMN_STORE_ID + " = " + storeID);
        return (dbTemp.size() == 1) ? dbTemp.get(0) : null;
    }

    // GetStores(String Where), returns an ArrayList<Store>
    // Used to get all Stores Where, use Where == 'null' for all stores
    public ArrayList<Store> GetStores(String Where)
    {
        String[] columns = new String[]{COLUMN_STORE_ID, COLUMN_STORE_NAME, COLUMN_STORE_LAT, COLUMN_STORE_LONG};
        Cursor dbResult = db.query(TABLE_STORE, columns, Where, null, null, null, null);

        ArrayList<Store> result = new ArrayList<Store>();
        while(dbResult.moveToNext())
        {
            int tempStoreID = dbResult.getInt(0);
            String tempStoreName = dbResult.getString(1);
            double tempStoreLat = dbResult.getDouble(2);
            double tempStoreLong = dbResult.getDouble(3);
            Map<Integer, String> tempStoreList = GetListMap(COLUMN_STORE_ID + " = " + tempStoreID);
            result.add(new Store(tempStoreID, tempStoreName, tempStoreLat, tempStoreLong, tempStoreList));
        }
        return result;
    }

    // UpdateStore(Store inputStore)
    // Inserts/Update the Store passed to it
    // Updates the DatabaseManager's static STORES at end
    // Returns the Store_ID, used to update Store objects with null(-1) id
    public int UpdateStore(Store inputStore)
    {
        Store dbTest = null;
        if(inputStore.getId() != -1) {
            dbTest = GetStore(inputStore.getId());
        }

        ContentValues dbValues = new ContentValues();
        dbValues.put(COLUMN_STORE_NAME, inputStore.getName());
        dbValues.put(COLUMN_STORE_LAT, inputStore.getLatitude());
        dbValues.put(COLUMN_STORE_LONG, inputStore.getLongitude());

        int result = -1;
        if(dbTest == null)
        {
            long rowID = db.insert(TABLE_STORE, null, dbValues);
            Cursor dbResult = db.query(TABLE_STORE, new String[]{COLUMN_STORE_ID}, "ROWID = " + rowID, null, null, null, null);
            dbResult.moveToFirst();
            result = dbResult.getInt(0);
        }
        else
        {
            db.update(TABLE_STORE, dbValues, COLUMN_STORE_ID + " = " + inputStore.getId(), null);
            result = inputStore.getId();
        }

        STORES = GetStoreMap(null);
        return result;
    }

    // DeleteStore(int ID)
    // Attempts to delete the Store_ID row
    // Returns true if deleted, and updates static STORES
    public boolean DeleteStore(int ID)
    {
        boolean result = db.delete(TABLE_STORE, COLUMN_STORE_ID + " = " + ID, null) > 0;
        if(result)
        {
            STORES = GetStoreMap(null);
        }
        return result;
    }

    /*
    LISTS
     */
    // GetListMap(), Returns Map<List_ID, List_Name> Where
    // use Where == 'null' for all categories
    // Used to return basic info (ID and Name)
    private Map<Integer, String> GetListMap(String Where)
    {
        String[] columns = new String[]{COLUMN_LIST_ID, COLUMN_LIST_NAME};
        Cursor dbResult = db.query(TABLE_LIST, columns, Where, null, null, null, null);

        Map<Integer, String> result = new TreeMap<Integer, String>();
        while(dbResult.moveToNext()) {
            int tempListID = dbResult.getInt(0);
            String tempListName = dbResult.getString(1);
            result.put(tempListID, tempListName);
        }
        return result;
    }

    // GetList(int ID), Returns List, null if not found
    // Used to access GetStores with ID as Where-clause to return a single Store
    public List GetList(int listID)
    {
        ArrayList<List> dbTemp = GetLists(COLUMN_LIST_ID + " = " + listID);
        return (dbTemp.size() == 1) ? dbTemp.get(0) : null;
    }

    // GetLists(String Where), returns an ArrayList<List>
    // Used to get all List Where, use Where == 'null' for all Lists
    public ArrayList<List> GetLists(String Where)
    {
        String[] columns = new String[]{COLUMN_LIST_ID, COLUMN_STORE_ID, COLUMN_CATEGORY_ID, COLUMN_LIST_NAME, COLUMN_LIST_ALERT_DATE, COLUMN_LIST_ALERT_PROXIMITY};
        Cursor dbResult = db.query(TABLE_LIST, columns, Where, null, null, null, null);

        ArrayList<List> result = new ArrayList<List>();
        while(dbResult.moveToNext())
        {
            int tempListID = dbResult.getInt(0);
            int tempStore = dbResult.getInt(1);
            int tempCategoryID = !(dbResult.isNull(2)) ? dbResult.getInt(2) : -1;
            String tempListName = dbResult.getString(3);
            Date tempDateAlert = (dbResult.getString(4) != null) ? new Date(dbResult.getString(4)) : null;
            boolean tempProximityAlert = dbResult.getInt(5) == 1;
            ArrayList<Item> tempItems = GetItems(COLUMN_LIST_ID + " = " + tempListID);
            result.add(new List(tempListID, tempListName, tempItems, tempStore, tempProximityAlert, tempCategoryID, tempDateAlert));
        }
        return result;
    }

    // UpdateList(List inputList)
    // Inserts/Update the List passed to it
    // Updates the DatabaseManager's static LIST at end
    // Returns the List_ID, used to update List objects with null(-1) id
    public int UpdateList(List inputList)
    {
        List dbTest = null;
        if(inputList.getId() != -1) {
            dbTest = GetList(inputList.getId());
        }

        ContentValues dbValues = new ContentValues();
        dbValues.put(COLUMN_STORE_ID, inputList.getStore());
        dbValues.put(COLUMN_CATEGORY_ID, (inputList.getCategory() != -1) ? inputList.getCategory() : null);
        dbValues.put(COLUMN_LIST_NAME, inputList.getName());
        dbValues.put(COLUMN_LIST_ALERT_PROXIMITY, (inputList.getProximityAlert()) ? 1 : 0);
        dbValues.put(COLUMN_LIST_ALERT_DATE, (inputList.getDateAlert() != null) ? inputList.getDateAlert().toString() : null);

        int result = -1;
        if(dbTest == null)
        {
            long rowID = db.insert(TABLE_LIST, null, dbValues);
            Cursor dbResult = db.query(TABLE_LIST, new String[]{COLUMN_LIST_ID}, "ROWID = " + rowID, null, null, null, null);
            dbResult.moveToFirst();
            result = dbResult.getInt(0);
        }
        else
        {
            db.update(TABLE_LIST, dbValues, COLUMN_LIST_ID + " = " + inputList.getId(), null);
            result = inputList.getId();
        }

        LISTS = GetListMap(null);
        return result;
    }

    // DeleteList(int ID)
    // Attempts to delete the List_ID row
    // Returns true if deleted, and updates static LISTS
    public boolean DeleteList(int ID)
    {
        boolean result = db.delete(TABLE_LIST, COLUMN_LIST_ID + " = " + ID, null) > 0;
        if(result)
        {
            LISTS = GetStoreMap(null);
        }
        return result;
    }

    /*
    ITEMS
    As Items should never be accessed individually, nor outside List, only GetItems() required
     */
    // GetItem(int ID), Returns Item, null if not found
    public Item GetItem(int itemID)
    {
        ArrayList<Item> dbTemp = GetItems(COLUMN_ITEM_ID + " = " + itemID);
        return (dbTemp.size() == 1) ? dbTemp.get(0) : null;
    }

    // GetItems(String Where), returns an ArrayList<Item>
    // Used to get all Items Where, use Where == 'null' for all Items
    public ArrayList<Item> GetItems(String Where)
    {
        String[] columns = new String[]{COLUMN_ITEM_ID, COLUMN_LIST_ID, COLUMN_ITEM_NAME, COLUMN_ITEM_COMPLETE};
        Cursor dbResult = db.query(TABLE_ITEM, columns, Where, null, null, null, null);

        ArrayList<Item> result = new ArrayList<Item>();
        while(dbResult.moveToNext())
        {
            int tempItemID = dbResult.getInt(0);
            String tempItemName = dbResult.getString(2);
            boolean tempItemComplete = (dbResult.getInt(3) == 1);
            result.add(new Item(tempItemID, tempItemName, tempItemComplete));
        }
        return result;
    }

    // UpdateItem(Item inputItem)
    // Inserts/Update the Item passed to it
    // Returns the Item_ID, used to update Item objects with null(-1) id
    public int UpdateItem(Item inputItem, int listID)
    {
        Item dbTest = null;
        if(inputItem.getId() != -1) {
            dbTest = GetItem(inputItem.getId());
        }

        ContentValues dbValues = new ContentValues();
        dbValues.put(COLUMN_LIST_ID, listID);
        dbValues.put(COLUMN_ITEM_NAME, inputItem.getName());
        dbValues.put(COLUMN_ITEM_COMPLETE, (inputItem.isComplete()) ? 1 : 0);

        int result = -1;
        if(dbTest == null)
        {
            long rowID = db.insert(TABLE_ITEM, null, dbValues);
            Cursor dbResult = db.query(TABLE_ITEM, new String[]{COLUMN_ITEM_ID}, "ROWID = " + rowID, null, null, null, null);
            dbResult.moveToFirst();
            result = dbResult.getInt(0);
        }
        else
        {
            db.update(TABLE_ITEM, dbValues, COLUMN_ITEM_ID + " = " + inputItem.getId(), null);
            result = inputItem.getId();
        }

        return result;
    }

    // DeleteItem(int ID)
    // Attempts to delete the Item_ID row
    // Returns true if deleted
    public boolean DeleteItem(int ID)
    {
        return db.delete(TABLE_ITEM, COLUMN_ITEM_ID + " = " + ID, null) > 0;
    }
}


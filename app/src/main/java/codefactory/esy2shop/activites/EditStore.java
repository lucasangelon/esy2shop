package codefactory.esy2shop.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import codefactory.esy2shop.database.DatabaseManager;
import codefactory.projectshop.R;

public class EditStore extends ActionBarActivity {

    Spinner existingStoreSpinner;
    ArrayAdapter<String> existingStoreAdapter;
    int[] existingStoreIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);

        // Update Existing Store Spinner
        existingStoreSpinner = (Spinner) findViewById(R.id.existingStoreSpinner);
        int existingStoreSize = DatabaseManager.STORES.size();
        // Prepare Store Names IDs
        String[] existingStoreNames = new String[existingStoreSize + 1];
        existingStoreNames[0] = "";
        existingStoreIDs = new int[existingStoreSize + 1];
        existingStoreIDs[0] = -1;
        // Get DB Store Name and IDs
        Integer[] tempExistingStoreIDs = new Integer[existingStoreSize];
        tempExistingStoreIDs = DatabaseManager.STORES.keySet().toArray(tempExistingStoreIDs);
        String[] tempExistingStoreNames = new String[existingStoreSize];
        tempExistingStoreNames = DatabaseManager.STORES.values().toArray(tempExistingStoreNames);
        // Add DB data to arrays
        for(int i = 0; i < existingStoreSize; i++)
        {
            existingStoreIDs[i + 1] = tempExistingStoreIDs[i];
            existingStoreNames[i + 1] = tempExistingStoreNames[i];
        }
        // Finalise adapter and spinner
        existingStoreAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, existingStoreNames);
        existingStoreSpinner.setAdapter(existingStoreAdapter);
    }

    public void storeResultSubmitPLACEHOLDEROnClick(View view)
    {
        Intent returnIntent = new Intent();
        int result = existingStoreIDs[existingStoreSpinner.getSelectedItemPosition()];
        returnIntent.putExtra("result", result);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}

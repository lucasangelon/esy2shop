package codefactory.esy2shop.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import codefactory.esy2shop.adapters.StoreAdapter;
import codefactory.esy2shop.database.DatabaseManager;
import codefactory.projectshop.R;

public class EditStore extends ActionBarActivity {

    EditText newStoreField;
    StoreAdapter storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);

        newStoreField = (EditText) findViewById(R.id.newStoreField);
        ListView storesListView = (ListView) findViewById(R.id.storesListView);
        storeAdapter = new StoreAdapter(this, (getCallingActivity() != null));
        storesListView.setAdapter(storeAdapter);

        Intent intent = getIntent();
        int extra = intent.getIntExtra("StoreID", -1);
        storeAdapter.UpdateMapMarker(getIntent().getIntExtra("StoreID", -1));
    }

    public void newStoreBtnOnClick(View view)
    {
        storeAdapter.Update(newStoreField.getText().toString());
    }

    @Override
    public void onBackPressed()
    {
        storeAdapter.onBack();
    }
}

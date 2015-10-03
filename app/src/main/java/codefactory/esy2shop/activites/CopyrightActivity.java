package codefactory.esy2shop.activites;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import codefactory.projectshop.R;

import android.support.v7.widget.Toolbar;

/**
 * Created by Connie on 19/08/15.
 */
public class CopyrightActivity extends Activity {

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object

        // REQUIRES ATTENTION SINCE SDK DROP
        //setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

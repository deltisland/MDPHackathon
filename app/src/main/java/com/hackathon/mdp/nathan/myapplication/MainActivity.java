package com.hackathon.mdp.nathan.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private String auth_token = null;
    private String key = PrivateKeys.key;
    private String secret = PrivateKeys.secret;
    private String version = "preview1";
    private String practiceid = "195900";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.auth_token = getAuthToken();
        //sampleGetRequest();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //This function will return the authentication token from the Athena server.
    private void getAuthToken() {


    }


    public void sampleGetRequest(View v) {
        TextView textView = (TextView) findViewById(R.id.textViewRegion);

        try{
            AthenaAPI api = new AthenaAPI(version, key, secret, practiceid);

            // If you want to set the practice ID after construction, this is how.
            api.setPracticeID("195900");

            /*
            ////////////////////////////////////////////////////////////////////////////////////////////
            // GET without parameters
            ////////////////////////////////////////////////////////////////////////////////////////////
            JSONArray customfields = (JSONArray) api.GET("/customfields");
            System.out.println("Custom fields:");
            for (int i = 0; i < customfields.length(); i++) {
                System.out.println("\t" + customfields.getJSONObject(i).get("name"));
            }
            System.out.println("End GET without parameters");

            */
            ////////////////////////////////////////////////////////////////////////////////////////////
            // GET with parameters
            ////////////////////////////////////////////////////////////////////////////////////////////
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Calendar today = Calendar.getInstance();
            Calendar nextyear = Calendar.getInstance();
            nextyear.roll(Calendar.YEAR, 1);

            Map<String, String> search = new HashMap<String, String>();
            search.put("departmentid", "1");
            search.put("startdate", format.format(today.getTime()));
            search.put("enddate", format.format(nextyear.getTime()));
            search.put("appointmenttypeid", "82");
            search.put("providerid", "1");
            JSONObject open_appts = (JSONObject) api.GET("/appointments/open", search);
            System.out.println(open_appts.toString());
            //JSONObject appt = open_appts.getJSONArray("appointments").getJSONObject(0);
            JSONObject appt = open_appts.getJSONArray("appointments").getJSONObject(0);
            System.out.println("Open appointment:");
            textView.setText(appt.toString());
        } catch(Exception e) {
            Log.d("LOL", "ATHENA");
        }

    }
}
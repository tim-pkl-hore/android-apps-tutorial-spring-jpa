package com.example.rizqy.spring_boot_pkl_peristence;import android.app.ListActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class People extends AppCompatActivity {
    @Bind(R.id.list) ListView list;
    private JSONArray jsonArray;
    private String[] list_array;
    private int[] list_arrayid;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(People.this, Detail.class);
                i.putExtra("id", list_arrayid[position]);
                startActivity(i);
            }
        });
        new loadData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_people, menu);
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
    class loadData extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(People.this);
            pDialog.setMessage("Please Waiting..");
            pDialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            JSonParser jp = new JSonParser();
            List<NameValuePair> param = new ArrayList<>();
            String json = jp.makeHttpRequest("http://192.168.1.100:8082/people/list", "GET", param);
            Log.d("test", json);

            try {
                jsonArray = new JSONArray(json);
                list_array = new String[jsonArray.length()];
                list_arrayid = new int[jsonArray.length()];
                JSONObject id = null;
                for (int i=0; i<jsonArray.length();i++){
                    id = jsonArray.getJSONObject(i);
                    list_arrayid[i] = id.getInt("id");
                    list_array[i] = id.getString("name");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(People.this, android.R.layout.simple_list_item_1, list_array);
            list.setAdapter(adapter);
            super.onPostExecute(s);
            pDialog.dismiss();
        }
    }


}


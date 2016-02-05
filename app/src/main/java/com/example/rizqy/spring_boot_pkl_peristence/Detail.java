package com.example.rizqy.spring_boot_pkl_peristence;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Detail extends AppCompatActivity {
    @Bind(R.id.id) TextView id;
    @Bind(R.id.name) TextView name;
    @Bind(R.id.email) TextView email;
    @Bind(R.id.gender)TextView gender;
    private ProgressDialog pDialog;
    private int idP;
    private int idLoad;
    private String nameLoad;
    private String emailLoad;
    private String genderLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        idP = bundle.getInt("id");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new loadData().execute();
    }
    class loadData extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Detail.this);
            pDialog.setMessage("Please Waiting..");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSonParser jp = new JSonParser();
            List<NameValuePair> param = new ArrayList<>();
            String json = jp.makeHttpRequest("http://192.168.1.100:8082/people/detail/" +idP , "GET", param);
            Log.d("test", json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                idLoad = jsonObject.getInt("id");
                nameLoad = jsonObject.getString("name");
                emailLoad = jsonObject.getString("email");
                genderLoad = jsonObject.getString("gender");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            id.setText(String.valueOf(idLoad));
            name.setText(nameLoad);
            email.setText(emailLoad);
            gender.setText(genderLoad);

            pDialog.dismiss();
        }
    }
}

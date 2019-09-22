package com.example.test;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private static final String URL = "http://www.intelligentscripts.com/demo_api/app/car/car_list";
    private RecyclerView userList;
    private RecyclerView.Adapter adapter;

    private List<Data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = findViewById(R.id.userList);
        userList.setHasFixedSize(true);
        userList.setLayoutManager(new LinearLayoutManager(this));

        dataList = new ArrayList<>();


        loadRecyclerviewData();
    }

    private void loadRecyclerviewData()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {
                progressDialog.dismiss();
                Log.d("CODE",s);
                try
                {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("data");
                    //JSONObject array = jsonObject.getJSONObject("data");

                    for(int i = 0; i<array.length(); i++ )
                    {
                         JSONObject o = array.getJSONObject(i);
                         //JSONObject o = array.getJSONObject(""+i);
                         Data data = new Data(
                                 o.getString("full_name"),
                                 o.getString("profile_picture")
                         );


                         dataList.add(data);
                    }

                    adapter = new DataAdapter(dataList,getApplicationContext());

                    userList.setAdapter(adapter);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });




        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

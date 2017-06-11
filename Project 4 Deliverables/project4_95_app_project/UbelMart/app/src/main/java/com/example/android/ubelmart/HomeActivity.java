package com.example.android.ubelmart;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private int pageNum = 1;
    private static final int DEFAULT_NUMBER_OF_RESULTS = 10;
    private List<AndroidItem> allItems = new ArrayList<AndroidItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // search with empty query for initial results
        doSearch("");
        displayItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.app_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
            displayItems();
        }
    }

    private void doSearch(String query) {
        // reset page number to 1
        pageNum = 1;
        // do search with the query
        sendRequest(query);

        // test
//        String[] values = new String[] { "hello" };
//        // put results in arraylist
////        final ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < values.length; ++i) {
//            items.add(values[i]);
//        }
    }

    private void sendRequest(String query) {
        Log.d("mainActivity", "query is: " + query);

        // http parameters
        final Map<String, String> params = new HashMap<String, String>();
        params.put("searchtype", "itemSearch");
        params.put("query", query);
        params.put("isAndroid", "true");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://13.113.6.36:8080/project4_95_phone/autosuggest";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mainActivity", "response is: " + response);
                        // add to array of items
                        AndroidItem[] items = new Gson().fromJson(response, AndroidItem[].class);
                        allItems = Arrays.asList(items);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    Log.d("mainActivity", "error is null");
                }
                else {
                    String body;
                    //get status code here
                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                    //get response body and parse with appropriate encoding
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // exception
                    }
                    Log.d("mainActivity", "got error");
                    Log.d("mainActivity", statusCode);
                    Log.d("mainActivity", "error");
                }
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getPrevPage(View view) {
        pageNum--;
        Log.d("HomeActivity", Integer.toString(pageNum));
    }

    public void getNextPage(View view) {
        pageNum++;
        Log.d("HomeActivity", Integer.toString(pageNum));
    }

    private void displayItems() {
        List<String> itemsToDisplay = new ArrayList<String>();
        int i = 0;
        while(i < allItems.size() && i < DEFAULT_NUMBER_OF_RESULTS) {
            itemsToDisplay.add(allItems.get(i+pageNum*DEFAULT_NUMBER_OF_RESULTS).getName());
            i++;
        }
        final ListView listview = (ListView) findViewById(R.id.item_list);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, itemsToDisplay);
        listview.setAdapter(adapter);
    }
}

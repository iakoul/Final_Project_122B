package com.example.android.ubelmart;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // enable type-to-search functionality
//        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
//            doSearch(query);
            Log.d("SearchableActivity", query);

            setContentView(R.layout.activity_home);
            final ListView listview = (ListView) findViewById(R.id.item_list);
            String[] values = new String[] { "hello" };

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < values.length; ++i) {
                list.add(values[i]);
            }
            final ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
        }
    }

    private void doSearch(String query) {
//        setContentView(R.layout.activity_home);
//        final ListView listview = (ListView) findViewById(R.id.item_list);
//        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
//                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
//                "Android", "iPhone", "WindowsMobile" };
//
//        final ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < values.length; ++i) {
//            list.add(values[i]);
//        }
//        final ArrayAdapter adapter = new ArrayAdapter(this,
//                android.R.layout.simple_list_item_1, list);
//        listview.setAdapter(adapter);
    }
}

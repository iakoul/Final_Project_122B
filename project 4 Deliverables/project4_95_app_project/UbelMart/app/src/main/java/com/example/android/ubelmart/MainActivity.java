package com.example.android.ubelmart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "myPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            EditText username = (EditText) findViewById(R.id.username);
            EditText password = (EditText) findViewById(R.id.password);

            username.setText(savedInstanceState.getString("username"), TextView.BufferType.EDITABLE);
            password.setText(savedInstanceState.getString("password"), TextView.BufferType.EDITABLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        // Save the user's current game state
        savedInstanceState.putString("username", username.getText().toString());
        savedInstanceState.putString("password", password.getText().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        username.setText(savedInstanceState.getString("username"), TextView.BufferType.EDITABLE);
        password.setText(savedInstanceState.getString("password"), TextView.BufferType.EDITABLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        editor.putString("username", username.getText().toString());
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String user = sharedpreferences.getString("username", "");

        EditText username = (EditText) findViewById(R.id.username);
        username.setText(user, TextView.BufferType.EDITABLE);
    }

    // called when login button is pressed
    public void login(View view) {
//        CoordinatorLayout cl = (CoordinatorLayout) findViewById(R.id.myCoordinatorLayout);
//        LinearLayout ll = (LinearLayout) cl.findViewById(R.id.activity_main);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        if(username.getText().toString().equals("") && password.getText().toString().equals("")) {
            Toast.makeText(this, "Fields cannot be left blank.", Toast.LENGTH_LONG).show();
        }
        else if(username.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a username.", Toast.LENGTH_LONG).show();
        }
        else if(password.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a password.", Toast.LENGTH_LONG).show();
        }
        else {
            // http parameters
            final Map<String, String> params = new HashMap<String, String>();
            params.put("username", username.getText().toString());
            params.put("password", password.getText().toString());
            params.put("isAndroid", "true");

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://54.190.26.95:8080/Project2/login";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(Boolean.parseBoolean(response.trim())) {
                                Log.d("mainActivity", response.trim());
                                Log.d("mainActivity", "success!");
                                // direct to home view
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Log.d("mainActivity", response.trim());
                                Log.d("mainActivity", "invalid login");
                                Toast.makeText(getApplicationContext(), "Invalid username password combination. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("mainActivity", "error");
                }
            }) {
                @Override
                public Map<String, String> getParams() {
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);


            // for testing
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
        }
    }
}

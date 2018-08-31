package com.example.dell.student.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.student.R;
import com.example.dell.student.constants.JApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ACCOUNT = "account";
    private static final String type = "login_details";
    private static String key = "myKey";
    EditText usrname, passwd;
    String server_url = JApplication.getLogin_url();
    String userName, password;
    Intent intent;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usrname = findViewById(R.id.usrname);
        passwd = findViewById(R.id.passwd);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Welcome to Document Sharing");
    }

    public void onLoginClick(View view) {

        userName = usrname.getText().toString().trim();
        password = passwd.getText().toString().trim();
        final RequestQueue requestQueue = Volley.newRequestQueue(Login_Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagg", "Login_Activity " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("response");
                            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(type, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(key + 1, userName);
                            editor.putString(key + 2, password);
                            if (result.equals("IT")) {
                                editor.putString(key + 3, result);
                                editor.commit();
                                intent = new Intent(Login_Activity.this, Student_Activity.class);
                                intent.putExtra("course", "Information Technology");
                                startActivity(intent);
                                finish();
                            } else if (result.equals("CS")) {
                                editor.putString(key + 3, result);
                                editor.commit();
                                intent = new Intent(Login_Activity.this, Student_Activity.class);
                                intent.putExtra("course", "Computer Science");
                                startActivity(intent);
                                finish();
                            }
                            requestQueue.stop();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i("tagg", "Login_Activity " + error.toString());
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                        requestQueue.stop();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prams = new HashMap<>();
                prams.put(KEY_USERNAME, userName);
                prams.put(KEY_PASSWORD, password);
                prams.put(KEY_ACCOUNT, "Student");
                return prams;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void onSignupClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}

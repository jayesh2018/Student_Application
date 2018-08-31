package com.example.dell.student.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class OTPActivity extends AppCompatActivity {

    String URL = JApplication.getAddUser_url();
    String email, otp, username, toastmsg, alertbtn;

    EditText eOTP;
    TextView msg;
    Button bVerify;

    Intent intent;
    RequestQueue requestQueue;
    StringRequest moveRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        msg = findViewById(R.id.msg);
        eOTP = findViewById(R.id.OTP);
        receiveExtra();
        msg.setText("Please enter verification code sent to\n" + email);
        bVerify = findViewById(R.id.verify);
        bVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyClicked();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
    }

    private void verifyClicked() {
        if (otp.equals(eOTP.getText().toString())) {
            instantiateStringRequest();
            moveRecords.setShouldCache(false);
            requestQueue.add(moveRecords);
        } else {
            Toast.makeText(getBaseContext(), "Incorrect Code", Toast.LENGTH_LONG).show();
        }
    }

    private void instantiateStringRequest() {
        moveRecords = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagg", "response " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            toastmsg = jsonObject.getString("response");
                            showDialog(toastmsg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                        requestQueue.stop();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prams = new HashMap<>();
                prams.put("username", username);
                //Log.i("tagg", prams.toString());
                return prams;
            }
        };
    }

    private void showDialog(String msg) {
        String alertMsg = msg;
        if (alertMsg.equals("Request Timeout")) {
            alertbtn = "Register Again";
            intent = new Intent(this, RegisterActivity.class);
        } else if (alertMsg.contains("Successfully Registered")) {
            alertbtn = "Login";
            intent = new Intent(this, Login_Activity.class);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(alertMsg);
        alertDialogBuilder.setPositiveButton(alertbtn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void receiveExtra() {
        intent = getIntent();
        otp = intent.getStringExtra("code");
        email = intent.getStringExtra("email");
        username = intent.getStringExtra("username");
        eOTP.setText(otp);
    }
}

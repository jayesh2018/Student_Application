package com.example.dell.student.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dell.student.R;
import com.example.dell.student.constants.JApplication;
import com.example.dell.student.constants.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String URL = JApplication.getRegister_url();
    ArrayAdapter<CharSequence> adapter;
    Button submit;
    EditText eFn, eLn, eMob, eEmail, eUserName, ePass1, ePass2;
    String toastmsg = "null";
    String fn, ln, course, sem, mob, email, username, pass1, pass2;
    StringRequest sendDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eFn = findViewById(R.id.fn);
        eLn = findViewById(R.id.ln);
        eMob = findViewById(R.id.mob);
        eEmail = findViewById(R.id.email);
        eUserName = findViewById(R.id.username);
        ePass1 = findViewById(R.id.password);
        ePass2 = findViewById(R.id.repassword);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClicked(view);
            }
        });

        Spinner spinnercourse = findViewById(R.id.course);
        Spinner spinnersem = findViewById(R.id.sem);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.course, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercourse.setAdapter(adapter);
        spinnercourse.setOnItemSelectedListener(this);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.sem, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersem.setAdapter(adapter);
        spinnersem.setOnItemSelectedListener(this);

    }

    private void instantiateStringRequest() {
        sendDetails = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagg", "response " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            toastmsg = jsonObject.getString("response");
                            if (toastmsg.equals("User Already Exists") || toastmsg.equals("Try again later")) {
                                Toast.makeText(getBaseContext(), toastmsg, Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("code", toastmsg);
                                intent.putExtra("username", username);
                                Log.i("tagg", "success");
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prams = new HashMap<>();
                prams.put("fn", fn);
                prams.put("ln", ln);
                prams.put("mob", mob);
                prams.put("email", email);
                prams.put("password", pass1);
                prams.put("course", course);
                prams.put("sem", sem);
                prams.put("username", username);
                //Log.i("tagg", prams.toString());
                return prams;
            }
        };
    }

    public void submitClicked(View view) {

        fn = eFn.getText().toString();
        ln = eLn.getText().toString();
        mob = eMob.getText().toString();
        email = eEmail.getText().toString();
        username = eUserName.getText().toString();
        pass1 = ePass1.getText().toString();
        pass2 = ePass2.getText().toString();
        if (validated()) {
            instantiateStringRequest();
            sendDetails.setShouldCache(false);
            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
            VolleySingleton.getInstance(getApplicationContext()).addToRequestque(sendDetails);
        }

    }

    private boolean validated() {
        if (fn.isEmpty() || ln.isEmpty() || mob.isEmpty() || email.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            Toast.makeText(this, "ALL FIELDS ARE MANDATORY", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mob.length() != 10) {
            Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (username.length() < 8 || username.length() > 15) {
            Toast.makeText(this, "Username should be between 8 to 15 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!pass2.equals(pass1)) {
            Toast.makeText(this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (((Spinner) adapterView).getId()) {
            case R.id.sem:
                sem = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.course:
                course = adapterView.getItemAtPosition(i).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}



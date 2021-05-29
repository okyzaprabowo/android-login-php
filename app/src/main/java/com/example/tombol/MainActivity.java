package com.example.tombol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // loginamik
    // z&HXtxY6DQX!dpjY#tm1

    public EditText username;
    public String username_text;
    public EditText password;
    public String password_text;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.login_state),"");

        if (loginStatus.equals("loggedin")){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        // act click
        Button tombol = findViewById(R.id.Button01);
        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                Log.i("username", username_text);
                Log.i("password", password_text);
                if (TextUtils.isEmpty(username_text) || TextUtils.isEmpty(password_text)){
                    Toast.makeText(MainActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }
                else{
                    login(username_text,password_text);
                }
            }
        });
    }

    private void login(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Mencoba login ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
//        String uRl = "https://amikmobile.000webhostapp.com/login.php";
        // localhost version
        String uRl = "http://10.0.2.2/amikmobile/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Login Success")){
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    progressDialog.dismiss();
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("password",password);
                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(request);
    }
}
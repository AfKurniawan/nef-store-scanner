package com.bsn.nurelfal;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //Defining views
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUserId;
    private Context context;
    private Button buttonLogin;
    private ProgressDialog pDialog;

    SharedPreferences sp;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;

        pDialog = new ProgressDialog(context);
        editTextEmail = (EditText) findViewById(R.id.editTextUser);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUserId = (EditText) findViewById(R.id.editTextUserId);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        CookieManager cookieManage = new CookieManager();
        CookieHandler.setDefault(cookieManage);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();


        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isUserLogin()) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();

        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (editTextEmail.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Masukkan Username...", Toast.LENGTH_SHORT).show();
                } else if (editTextPassword.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Masukkan Password...", Toast.LENGTH_SHORT).show();
                }  else {
                    login();
                }


            }
        });

    }

    private void login() {

        sessionManager.setSavedPassword(editTextPassword.getText().toString());
        sessionManager.setSavedUserName(editTextEmail.getText().toString());
        sessionManager.setSavedIdUser(editTextUserId.getText().toString());
        sessionManager.setUserLoggedIn(true);

        final String user = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String id_user = editTextUserId.getText().toString().trim();

        if (TextUtils.isEmpty(user)) {
            editTextEmail.setError("Username Kosong");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password Kosong");
            editTextPassword.requestFocus();
            return;
        }

        pDialog.setMessage("Tunggu Sebentar...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains(Variable.LOGIN_SUCCESS)) {
                            hideDialog();
                            gotoScanActivity();

                        } else {
                            hideDialog();
                            Toast.makeText(context, "Username atau Password salah...", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        hideDialog();
                        Toast.makeText(context, "The server tidak merespon", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(Variable.KEY_USER, user);
                params.put(Variable.KEY_PASSWORD, password);
                params.put(Variable.KEY_ID_USER, id_user);


                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void gotoScanActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}

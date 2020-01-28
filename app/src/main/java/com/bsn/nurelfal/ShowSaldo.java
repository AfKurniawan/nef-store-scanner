package com.bsn.nurelfal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bsn.nurelfal.Models.Nasabah;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ShowSaldo extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getSimpleName();

    private TextView txtNama, txtNorek, txtNoatm, txtSaldo;

    EditText etIdNasabah;

    private ImageView imgPoster;

    private Button btnLogout, btnScan;

    private SessionManager sessionManager;


    private NasabahView nasabahView;

    final Context context = this;

    RequestQueue requestQueue;

    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_layout);

        txtNama = findViewById(R.id.tv_nama);
        txtNoatm = findViewById(R.id.tv_noatm);
        txtNorek = findViewById(R.id.tv_norek);
        txtSaldo = findViewById(R.id.tv_saldo);
        imgPoster = findViewById(R.id.iv_foto);
        btnLogout = findViewById(R.id.btn_logout);
        btnScan = findViewById(R.id.btn_scan);

        etIdNasabah = findViewById(R.id.et_id_nasabah);
        etIdNasabah.setVisibility(View.GONE);


        sessionManager = new SessionManager(getApplicationContext());


        requestQueue = Volley.newRequestQueue(ShowSaldo.this);

        pd = new ProgressDialog(ShowSaldo.this);

        //ReadBarcode();



        getSaldo();

        initToolbar();
        initComponent();

        SharedPreferences pref;

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logout();


            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowSaldo.this, ScanActivity.class);
                startActivity(intent);

            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saldo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        final CircularImageView image = (CircularImageView) findViewById(R.id.iv_foto);
        final CollapsingToolbarLayout collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                image.setScaleX(scale >= 0 ? scale : 0);
                image.setScaleY(scale >= 0 ? scale : 0);
            }
        });
    }

    private void getSaldo() {

        pd.setMessage("Sedang mengambil data...");
        pd.show();

        etIdNasabah.setText(sessionManager.getSavedIdNasabah());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.SALDO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        pd.dismiss();


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(ServerResponse);

                            JSONArray heroArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject hObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                com.bsn.nurelfal.Models.Nasabah nsb = new com.bsn.nurelfal.Models.Nasabah(

                                        hObject.getString("id_nasabah"),
                                        hObject.getString("norek"),
                                        hObject.getString("noatm"),
                                        hObject.getString("nama"),
                                        hObject.getString("saldo"),
                                        hObject.getString("foto")
                                );


                                //etNoatm.setText(nsb.getNoatm());
                                txtNama.setText(nsb.getNama());
                                txtNorek.setText(nsb.getNorek());
                                txtNoatm.setText(nsb.getNoatm());
                                txtSaldo.setText(nsb.getSaldo());
                                Glide.with(getApplicationContext()).load(nsb.getFoto()).into(imgPoster);




                            }



                        } catch (JSONException e) {
                            e.printStackTrace();




                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("id_nasabah", etIdNasabah.getText().toString());



                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(ShowSaldo.this);

        requestQueue.add(stringRequest);


    }

    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.alert_logout);
        alertDialogBuilder.setPositiveButton(R.string.ya,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        /*//Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(sessionManager.KEY_PASSWORD,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(sessionManager.KEY_IS_LOGIN, false);

                        //Putting blank value to email
                        editor.putString(Variable.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(ShowSaldo.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //sessionManager.clearSession();
                        editor.clear();
                        editor.commit();
                        startActivity(intent);

                        finish();*/


                        sessionManager.clearSession();
                        Intent intent = new Intent(ShowSaldo.this, LoginActivity.class);
                        intent.putExtra("finish", true); // if you are checking for this in your other Activities
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.tidak,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void ReadBarcode() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Variable.SALDO_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Nasabah response: " + response.toString());

                        getNasabah(response);

                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());

                //showNoData();
                Toast.makeText(getApplicationContext(), "Tidak tersambung" , Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getNasabah(JSONObject response) {
        try {

            Nasabah nasabah = new Gson().fromJson(response.toString(), Nasabah.class);


                txtNama.setText(nasabah.getNama());
                txtNoatm.setText(nasabah.getNoatm());
                txtNorek.setText(nasabah.getNorek());
                txtSaldo.setText(nasabah.getSaldo());
                Glide.with(this).load(nasabah.getFoto()).into(imgPoster);

                nasabahView.setVisibility(View.VISIBLE);



        } catch (JsonSyntaxException e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error occurred.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error occurred.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private class Nasabah {
        String id_nasabah;
        String id_user;
        String nama;
        String norek;
        String noatm;
        String saldo;
        String foto;
        String value;
        String keterangan;

        public String getIdNasa(){
            return id_nasabah;
        }

        public void setIdNasa(){
            this.id_nasabah = id_nasabah;
        }

        public String getNama() {

            return nama;
        }

        public void setNama( String nama){

            this.nama = nama;
        }


        public String getNorek() {

            return norek;
        }

        public void setNorek(){
            this.norek = norek;
        }

        public String getNoatm() {

            return noatm;
        }

        public void setNoatm(String noatm) {
            this.noatm = noatm;
        }

        public String getSaldo() {

            return saldo;
        }

        public void setSaldo(String saldo) {
            this.saldo = saldo;
        }

        public String getFoto() {
            return Variable.URL_PHOTO + foto;
        }

        public String getValue() {

            return value;
        }

        public String getIdUser() {
            return id_user;
        }

        public void setIdUser (String id_user){
            this.id_user = id_user;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKeterangan() {
            return keterangan;
        }

        public void setKeterangan(String keterangan){

            this.keterangan = keterangan;
        }

    }

}



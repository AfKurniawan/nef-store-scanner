package com.bsn.nurelfal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bsn.nurelfal.Models.IdUser;
import com.bsn.nurelfal.Models.LimitTrx;
import com.bsn.nurelfal.Models.Nasabah;
import com.bsn.nurelfal.Models.Pembatasan;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsn.nurelfal.Variable.KEY_USER;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getSimpleName();

    private TextView txtNama, txtNorek, txtNoatm, txtSaldo;

    TextView jenisTransaksi, tvTotalPenarikan, tvLimit, tvTanggal, lblTotalPenarikan ;

    EditText Jumlah, dateView, Keterangan, tanggal, datetime, txtIdNasa, idUser, etNoatm;

    EditText etUserId, etUser, etValParam;

    EditText etTotalPenarikan, etPembatasan;

    MaterialSpinner spinner;

    private ImageView imgPoster;

    private Button btnBuy;

    private NasabahView nasabahView;

    final Context context = this;

    RequestQueue requestQueue;

    String ValueHolder, TanggalHolder, KetHolder, idUserHolder, DateTimeHolder, JenisTransHolder, IdNasabahHolder, SaldoHolder, pilihJumlah;

    ProgressDialog pd;

    Boolean CheckEditText;

    private String jsonResponse;

    LinearLayout parentLL, llFormPenarikan;

    NestedScrollView scrollView;


    SessionManager sessionManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_nasabah);



        sessionManager = new SessionManager(getApplicationContext());

        txtNama = findViewById(R.id.tv_nama);
        txtNoatm = findViewById(R.id.tv_noatm);
        txtNorek = findViewById(R.id.tv_norek);
        txtSaldo = findViewById(R.id.tv_saldo);
        imgPoster = findViewById(R.id.iv_foto);
        btnBuy = findViewById(R.id.btn_buy);


        etNoatm = findViewById(R.id.et_noatm);
        etNoatm.setVisibility(GONE);

        etUserId = findViewById(R.id.et_user_id);
        etUserId.setVisibility(GONE);

        etUser = findViewById(R.id.et_user);
        final String username = sessionManager.getSavedUserName();
        etUser.setText(username);
        etUser.setVisibility(GONE);

        nasabahView = findViewById(R.id.layout_ticket);






        datetime = findViewById(R.id.inp_datetime);
        SimpleDateFormat jam = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String waktu = jam.format(Calendar.getInstance().getTime());
        datetime.setText(waktu);
        datetime.setFocusable(false);
        datetime.setVisibility(GONE);


        tvTanggal = findViewById(R.id.tv_tanggal);
        SimpleDateFormat tgl = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        String dino = tgl.format(Calendar.getInstance().getTime());
        tvTanggal.setText(dino);




        dateView =   findViewById(R.id.inp_tgl);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String time = sdf.format(new Date());
        dateView.setText(time);
        dateView.setFocusable(false);
        dateView.setVisibility(GONE);

        txtIdNasa = findViewById(R.id.inp_idNasabah);
        txtIdNasa.setFocusable(false);
        //txtIdNasa.setVisibility(GONE);

        jenisTransaksi = (EditText) findViewById(R.id.jenis_trans);
        jenisTransaksi.setText("Tarik");
        jenisTransaksi.setFocusable(false);
        jenisTransaksi.setVisibility(GONE);

        datetime = findViewById( R.id.inp_tgl );
        datetime.setFocusable(false);

        Jumlah =  findViewById(R.id.inp_value);

        ValueHolder = Jumlah.getText().toString().trim();

        etValParam = findViewById(R.id.et_value);
        etValParam.setText("5000"); //DEFAULT
        etValParam.setVisibility(GONE);

        Jumlah.setText("5000");//default

        Keterangan =  findViewById(R.id.inp_ket);
        Keterangan.setText("Jajan");

        lblTotalPenarikan = findViewById(R.id.label_total_penarikan);
        lblTotalPenarikan.setText("Total penarikan hari ini sudah mencapai");


        etTotalPenarikan = findViewById(R.id.et_total_penarikan);
        etPembatasan = findViewById(R.id.et_pembatasan);
        etTotalPenarikan.setVisibility(GONE);
        etPembatasan.setText("0");
        etPembatasan.setVisibility(GONE);

        tvLimit = findViewById(R.id.tv_limit);

        tvTotalPenarikan = findViewById(R.id.tv_total_penarikan);
        tvTotalPenarikan.setText("0");

        requestQueue = Volley.newRequestQueue(ResultActivity.this);

        pd = new ProgressDialog(ResultActivity.this);

        initToolbar();

        initComponent();

        initSpinner();

        getIdUser();



        parentLL = findViewById(R.id.parent_ll);

        scrollView = findViewById(R.id.nested_content);





        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();


                if (CheckEditText) {

                    ngitungPembatasanEtJumlah();


                } else {

                    Toast.makeText(ResultActivity.this, "Silahkan masukkan jumlah penarikan.", Toast.LENGTH_LONG).show();

                }
            }
        });


        /*Jumlah.setInputType(InputType.TYPE_CLASS_NUMBER);
        Jumlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                pembatasanTransaksi();
            }
        });

        Jumlah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // showMyDialog();
                    //ngitungPembatasanEtJumlah();
                    pembatasanTransaksi();
                }
            }
        });*/



    } //tutup oncreate

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Penarikan");
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

    private void ReadBarcode() {

        //pd.setMessage("Sedang mengambil data...");
        //pd.show();

        String qrcode = getIntent().getStringExtra("noatm");
        etNoatm.setText(qrcode);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.URL_BARCODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                       // pd.dismiss();

                        //getIdUser();
                        pembatasanTransaksi();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(ServerResponse);

                            JSONArray heroArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject hObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                Nasabah nsb = new Nasabah(

                                        hObject.getString("id_nasabah"),
                                        hObject.getString("norek"),
                                        hObject.getString("noatm"),
                                        hObject.getString("nama"),
                                        hObject.getString("saldo"),
                                        hObject.getString("foto")
                                );


                                txtIdNasa.setText(nsb.getId_nasabah());
                                txtNama.setText(nsb.getNama());
                                txtNorek.setText(nsb.getNorek());
                                txtNoatm.setText(nsb.getNoatm());
                                txtSaldo.setText(nsb.getSaldo());
                                Glide.with(getApplicationContext()).load(nsb.getFoto()).into(imgPoster);


                                sessionManager.setSavedIdNasabah(txtIdNasa.getText().toString());







                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                            showNoData();


                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showNoData();
                    }



                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("noatm", etNoatm.getText().toString());

                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);

        requestQueue.add(stringRequest);

    }

    private void initSpinner(){



        spinner = findViewById(R.id.spinner);
        spinner.setItems("5000", "10000", "15000", "20000");
        //spinner.setItems(5000, 10000, 15000, 20000);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {



                switch (position) {

                    case 0:
                        //pembatasanTransaksi();
                        Jumlah.setText("5000");
                        etValParam.setText("5000");
                        Jumlah.setEnabled(true);
                        break;

                    case 1:

                        //pembatasanTransaksi();
                        Jumlah.setText("10000");
                        etValParam.setText("10000");
                        Keterangan.setText("Jajan");
                        break;

                    case 2:
                        //pembatasanTransaksi();
                        Jumlah.setText("15000");
                        etValParam.setText("15000");
                        Keterangan.setText("Jajan");
                        break;

                    case 3:
                        //pembatasanTransaksi();
                        Jumlah.setText("20000");
                        etValParam.setText("20000");
                        Keterangan.setText("Jajan");
                        break;

                    /*case 4:
                        pembatasanTransaksi();
                        Jumlah.setText(item);
                        Keterangan.setText("Jajan");
                        break;*/


                    /*case 5:
                        pembatasanTransaksi();
                        Jumlah.setText(item);
                        Keterangan.setText("Jajan");
                        break;*/


                }

            }
        });

    }

    public void pembatasanTransaksi(){

        pd.setMessage("Sedang megecek data...");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.PEMBATASAN_TRANSAKSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        pd.dismiss();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(ServerResponse);

                            pd.dismiss();

                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject hObject = heroArray.getJSONObject(i);

                                Pembatasan pembatasan = new Pembatasan(

                                        hObject.getString("pembatasan"),
                                        hObject.getString("total")
                                );

                                //int penarikan = hObject.getInt("pembatasan");
                                etTotalPenarikan.setText(pembatasan.getPembatasan());


                                tvLimit.setText(pembatasan.getPembatasan());
                                tvTotalPenarikan.setText(pembatasan.getTotal());

                                String tot = tvTotalPenarikan.getText().toString();

                                if (tot.equals("null")){

                                    tvTotalPenarikan.setText("0");
                                    tvTotalPenarikan.setVisibility(GONE);
                                    lblTotalPenarikan.setText("Belum ada penarikan hari ini.");
                                }

                                if (tot.equals("0")){

                                    tvTotalPenarikan.setVisibility(GONE);
                                    lblTotalPenarikan.setText("Belum ada penarikan hari ini.");

                                }


                                int limit = hObject.getInt("total");
                                etPembatasan.setText(pembatasan.getTotal());


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

                params.put("id_nasabah", txtIdNasa.getText().toString());
                params.put ("tanggal", dateView.getText().toString());
                params.put("jenis_trans", jenisTransaksi.getText().toString());


                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);

        requestQueue.add(stringRequest);



    }

    private void ngitungSaldoMinus(){

        int saldo = Integer.parseInt(txtSaldo.getText().toString());

        if (saldo < 0){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Saldo Nasabah sudah minus sebesar " + saldo+" Anda tidak dapat melakukan penarikan lagi.")
                    .setCancelable(false)
                    .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.setTitle("Status");
            alert.show();


        } else {

            InsertTarik();
        }


    }

    private void ngitungPembatasanEtJumlah(){


        int pembatasan = Integer.parseInt(etPembatasan.getText().toString());
        int iTotalPenarikanHariIni = Integer.parseInt(etTotalPenarikan.getText().toString());
        int iJumlahYgakanDitarik = Integer.parseInt(Jumlah.getText().toString());
        int hasil = iTotalPenarikanHariIni - pembatasan;





        if( hasil < iJumlahYgakanDitarik){

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
            String sekarang = sdf.format(new Date());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);



            if (hasil < 0) {
                builder.setMessage("Jumlah penarikan hari ini, " + sekarang + ", sudah minus sebesar " + hasil + ", Anda tidak dapat melakukan penarikan lagi.")
                        .setCancelable(false)
                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });



            } else if (hasil == 0) {

                builder.setMessage("Jumlah penarikan hari ini, " + sekarang + ", tinggal tersisa " + hasil + ", Anda tidak dapat melakukan penarikan lagi.")
                        .setCancelable(false)
                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });


            } else {

                builder.setMessage("Jumlah penarikan hari ini, " + sekarang + ", tinggal tersisa, " + hasil + " Anda hanya dapat melakukan penarikan sebesar " + hasil)
                        .setCancelable(false)
                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
            }

            AlertDialog alert = builder.create();
            alert.setTitle("Status");
            alert.show();


        } else {

            //ngitungSaldoMinus();
            InsertTarik();
        }



    }






    public void getIdUser(){

        pd.setMessage("Sedang megecek data...");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.GET_USER_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        pd.dismiss();
                        ReadBarcode();

                        try {

                            JSONObject obj = new JSONObject(ServerResponse);

                            pd.dismiss();

                            JSONArray heroArray = obj.getJSONArray("data");

                            for (int i = 0; i < heroArray.length(); i++) {

                                JSONObject hObject = heroArray.getJSONObject(i);


                                IdUser id = new IdUser(

                                        hObject.getString("id_user")
                                );


                                etUserId.setText(id.getId_user());


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

                params.put("user", etUser.getText().toString());


                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);

        requestQueue.add(stringRequest);



    }

    public void InsertTarik () {


        pd.setMessage("Sedang mengirim data...");
        pd.show();


        CheckEditTextIsEmptyOrNot();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variable.URL_PENARIKAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {


                        pd.dismiss();

                        try {


                            JSONObject res = new JSONObject(ServerResponse);

                            Toast.makeText(ResultActivity.this, "pesan : " + res.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        DialogInsertBerhasil();

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        DialogInsertGagal();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();


                params.put("id_nasabah", IdNasabahHolder);
                params.put("jenis_trans", JenisTransHolder);
                params.put("datetime", DateTimeHolder);
                params.put("tanggal", TanggalHolder);
                params.put("id_user", etUserId.getText().toString());
                params.put("value", ValueHolder);
                params.put("keterangan", KetHolder);

                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);

        requestQueue.add(stringRequest);

    }


    public void CheckEditTextIsEmptyOrNot() {
        SaldoHolder = txtSaldo.getText().toString().trim();
        IdNasabahHolder = txtIdNasa.getText().toString().trim();
        JenisTransHolder = jenisTransaksi.getText().toString().trim();
        DateTimeHolder = datetime.getText().toString().trim();
        //idUserHolder = idUser.getText().toString().trim();
        TanggalHolder = dateView.getText().toString().trim();
        ValueHolder = Jumlah.getText().toString().trim();
        KetHolder = Keterangan.getText().toString().trim();


        if (TextUtils.isEmpty(ValueHolder)) {

            CheckEditText = false;

        } else {


            CheckEditText = true;
        }
    }


    public void DialogInsertBerhasil() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setMessage("Penarikan saldo berhasil")

                .setCancelable(false)
                .setPositiveButton("Oke", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        //txtIdNasa = findViewById(R.id.inp_idNasabah);

                        //dialog.dismiss();

                        Intent intent = new Intent(ResultActivity.this, ShowSaldo.class);
                        startActivity(intent);
                        finish();


                    }
                });


        AlertDialog alert = builder.create();

        alert.setTitle("Status");
        alert.show();
        setContentView(R.layout.activity_saldo_layout);

    }


    private void DialogInsertGagal() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Penarikan saldo Gagal")
                .setCancelable(false)
                .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Status");
        alert.show();

    }


    private void DialogLimit() {



        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        String sekarang = sdf.format(new Date());




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Jumlah penarikan hari ini, "+sekarang+", sudah mencapai pembatasan, Anda tidak dapat melakukan penarikan lagi.")
                .setCancelable(false)
                .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        //startActivity(getIntent());


                        //btnBuy.setVisibility(GONE);
                        btnBuy.setEnabled(false);
                        btnBuy.setBackgroundResource(R.drawable.disabled_button);
                        btnBuy.setTextColor(getApplication().getResources().getColor(R.color.grey_40));
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Status");
        alert.show();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }









    public void showNoData() {


        btnBuy.setVisibility(View.GONE);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("QR Code tidak ada di database")
                .setCancelable(false)
                .setPositiveButton("Scan Lagi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startActivity( new Intent(ResultActivity.this,ScanActivity.class));
                    }
                });


        AlertDialog alert = builder.create();

        alert.setTitle("Data Barcode Tidak Ada");
        alert.show();

    }




   /* private class Nasabah {
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
*/



}



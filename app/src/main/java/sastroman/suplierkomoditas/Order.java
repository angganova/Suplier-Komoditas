package sastroman.suplierkomoditas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.AdapterOrder;
import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import model.DisplayOrder;

public class Order extends AppCompatActivity {


    private static final String url = (AppConfig.URL_SETTING);
    private static final String TAG = Order.class.getSimpleName();
    public ListView listView;
    private AdapterOrder adapter;
    private ArrayList<DisplayOrder> orderList = new ArrayList<DisplayOrder>();
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private Toolbar toolbar;
    public String mini = null;
    public String total = null;
    public String kir = null;
    public String minkir = null;
    public String poin = null;
    public DecimalFormat df = new DecimalFormat("###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        final String username = user.get("username");

        getSetting();
        getTotal(username);
        db = new SQLiteHandler(this);
        Intent i = getIntent();
        orderList = i.getParcelableArrayListExtra("array_list");

        listView = (ListView) findViewById(R.id.lvitem);
        adapter = new AdapterOrder(this, orderList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_plus:
                onBackPressed();
                return true;

            case R.id.action_shipping:
                sendData();
                    return true;

            default:
                break;
        }
        return false;
    }


    public void getSetting() {

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                mini = obj.getString("pmp");
                                kir = obj.getString("mbk");
                                minkir = obj.getString("pmk");
                                Log.e(TAG, "Values : " + mini + " | " + kir);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void sendData() {

        final JSONArray ol = new JSONArray();
        for (int i = 0; i < orderList.size(); i++) {

            DisplayOrder m = orderList.get(i);
            HashMap<String, String> user = db.getUserDetails();
            m.setUser(user.get("user_id"));

            String product_id = m.getId();
            String cust_id = m.getUser();
            String quantity = m.getQty();

            if (quantity != null  && !quantity.isEmpty() && !quantity.equals("null") && !quantity.equals("0") && !quantity.equals("")) {
                double qty = Double.parseDouble(quantity.replaceAll(",", "."));
                double min = Double.parseDouble(m.getKet());
                double p1 = Double.parseDouble(m.getPrice1().replaceAll("\\.", ""));
                double p2 = Double.parseDouble(m.getPrice2().replaceAll("\\.", ""));
                if (qty >= min) {
                    m.setPrice(String.valueOf((int) p2));
                    m.setTotal(String.valueOf((int) (p2 * qty)));
                } else {
                    m.setPrice(String.valueOf((int) p1));
                    m.setTotal(String.valueOf((int) (p1 * qty)));
                }

                String price = m.getPrice();
                String total = m.getTotal();

                JSONObject orderPackage = new JSONObject();
                try {
                    orderPackage.put("product_id", product_id);
                    orderPackage.put("cust_id", cust_id);
                    orderPackage.put("price", price);
                    orderPackage.put("quantity", quantity);
                    orderPackage.put("total", total);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ol.put(orderPackage);
            }
        }

        final String dataToSend = ol.toString();
        Log.d(TAG, dataToSend);

        String Total2 = "";
        String shipping = "";
        double Totaldb = 0;
        if (total != null && !total.equals("") && !total.equals("null")) {
            Totaldb = Double.parseDouble(total);
        }
        for (int i = 0; i < orderList.size(); i++) {
            DisplayOrder m = orderList.get(i);
            String mTotal = m.getQty();
            if (mTotal != null && !mTotal.isEmpty() && !mTotal.equals("null") && !mTotal.equals("0") && !mTotal.equals("")
                    && mini != null && kir != null & minkir != null) {
                Totaldb = Totaldb + Double.parseDouble(m.getTotal());
                Total2 = (String.valueOf(df.format(Totaldb).replaceAll(",", ".")));
                double minkir2 = Double.parseDouble(minkir);
                double min2 = Double.parseDouble(mini);
                double kir2 = Double.parseDouble(kir);
                poin = String.valueOf((int) (Totaldb / min2));
                if (Totaldb >= minkir2) {
                    shipping = "Gratis";
                } else {
                    double shipping2 = kir2 - (Totaldb * 0.2);
                    shipping = String.valueOf("Rp. " + df.format(shipping2).replaceAll(",", "."));
                }
            } else{
                Totaldb = 0;
            }
        }

        int size1 = orderList.size();
        int size2 = ol.length();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        Log.d(TAG, size1 + " : " + size2);
        if (size1 == size2) {
            alertDialogBuilder.setMessage("Total pembelian hari ini Rp. " + Total2 + "." + '\n' +
                    "Biaya Kirim " + shipping + "." + '\n' +
                    "Anda mendapatkan " + poin + " Poin." + '\n' +
                    "Anda yakin ingin memesan ini semua?");
            alertDialogBuilder.setNegativeButton("Tidak",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            alertDialogBuilder.setPositiveButton("Ya",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            sendOrder(dataToSend);
                        }
                    });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Tolong isi semua data yang diperlukan", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendOrder( final String dataToSend) {

        pDialog.setMessage("Mengirim Pesan ...");
        showDialog();

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDERIN, new Response.Listener<String>() {

            @Override
            public void onResponse(java.lang.String response) {
                Log.d(TAG, "Order Response: " + response.toString());

                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Pesanan Sudah Diterima", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(Order.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        hideDialog();
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Order Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("json", dataToSend);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getTotal(final String username) {

        String tag_string_req = "req_total";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LIST_TOTO, new Response.Listener<String>() {

            @Override
            public void onResponse(java.lang.String response) {
                Log.d(TAG, "Total Response: " + response);
                DecimalFormat df = new DecimalFormat("###,###");

                if (!response.isEmpty()) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        total = jObj.getString("total");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Total Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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

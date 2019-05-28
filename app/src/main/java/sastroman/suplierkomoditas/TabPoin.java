package sastroman.suplierkomoditas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.AdapterPoin;
import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import model.DisplayPoin;

/**
 * Created by A10 on 03/02/2017.
 */
public class TabPoin extends Fragment {

    // Log tag
    private static final String TAG = TabPoin.class.getSimpleName();

    //  json url
    private static final String url = (AppConfig.URL_POIN);
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private ListView listview;
    private AdapterPoin adapter;
    private ArrayList<DisplayPoin> itemList = new ArrayList<DisplayPoin>();
    public TextView tvPoin;
    private Integer poin = 0;
    private ProgressBar pb;
    private String username;
    private String cust_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_poin, container, false);
        setHasOptionsMenu(true);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        tvPoin = (TextView)rootView.findViewById(R.id.tvPoin);
        listview = (ListView)rootView.findViewById(R.id.lvpoin);
        pb = (ProgressBar) rootView.findViewById(R.id.pb);
        db = new SQLiteHandler(getActivity());
        // Fetching user details from sqlite
        final HashMap<String, String> user = db.getUserDetails();

        cust_id = user.get("user_id");
        username = user.get("username");

        getData();
        getPoin(username);

        adapter = new AdapterPoin(getActivity(), itemList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                DisplayPoin dv = (DisplayPoin) parent.getItemAtPosition(position);
                String name = dv.getName();
                final String ket = dv.getKet();
                String unit = dv.getUnit();
                final String poin_id = dv.getId();
                String poin_for = dv.getPoin();
                int poin2 = Integer.parseInt(poin_for);

                if (poin >= poin2){
                    final String fpoin = String.valueOf(poin2);
                    alertDialogBuilder.setMessage("Nama item " + name + " " + ket + " " + unit + "." + '\n' +
                            "Harga penukaran " + poin_for + " Poin." + '\n' +
                            "Anda yakin ingin menukarkan ini?");
                            alertDialogBuilder.setNegativeButton("Tidak",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            changePoin(poin_id, cust_id, fpoin, ket);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                     }else{
                        Toast.makeText(getContext(), "Poin anda tidak cukup!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_profile2, menu);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void getPoin(final String username) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHECKPOIN, new Response.Listener<String>() {

            @Override
            public void onResponse(java.lang.String response) {
                Log.d(getTag(), "Gp Response: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        poin = jObj.getInt("poin");
                        tvPoin.setText(String.valueOf(poin));
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(getTag(), "Gp Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
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

    public void getData() {

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pb.setVisibility(View.GONE);
                        listview.setVisibility(View.VISIBLE);

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                DisplayPoin items = new DisplayPoin();
                                items.setId(obj.getString("id"));
                                items.setName(obj.getString("name"));
                                items.setThumbnailUrl(AppConfig.URL_IMAGE + obj.getString("image") + ".jpg");
                                items.setKet(obj.getString("details"));
                                items.setUnit(obj.getString("unit"));
                                items.setPoin(obj.getString("wholesaler_price"));

                                // adding movie to movies array
                                itemList.add(items);

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


    private void changePoin(final String poin_id, final String cust_id, final String fpoin, final String ket) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Mengirim ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHPOIN, new Response.Listener<String>() {

            @Override
            public void onResponse(java.lang.String response) {
                Log.d(TAG, "Cp Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getPoin(username);
                        Toast.makeText(getContext(), "Poin berhasil di tukarkan", Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Cp Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", poin_id);
                params.put("cust_id", cust_id);
                params.put("poin", fpoin);
                params.put("qty", ket);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
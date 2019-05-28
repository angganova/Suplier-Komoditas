package sastroman.suplierkomoditas;

/**
 * Created by A10 on 01/02/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import adapter.AdapterOrto;
import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import model.DisplayOrto;


public class OrtoTab extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, SearchView.OnQueryTextListener{

    private static final String TAG = OrtoTab.class.getSimpleName();
    private ArrayList<DisplayOrto> itemList = new ArrayList<DisplayOrto>();
    private AdapterOrto adapter;
    private ListView listView;
    private TextView tvempty;
    private SQLiteHandler db;
    private ProgressBar pb;
    private TextView tvDev;
    private TextView tvTot;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    public String min = null;
    public String kir = null;
    public String minkir = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.tab_orto, container, false);

        getSetting();
        listView = (ListView)rootView.findViewById(R.id.lvitem);
        tvempty= (TextView)rootView.findViewById(R.id.tvempty);
        tvDev = (TextView) rootView.findViewById(R.id.tvDev);
        tvTot = (TextView) rootView.findViewById(R.id.tvTot);
        pb = (ProgressBar) rootView.findViewById(R.id.pb);

        db = new SQLiteHandler(rootView.getContext());
        HashMap<String, String> user = db.getUserDetails();
        final String username = user.get("username");

        itemList.clear();
        getData(username);
        getTotal(username);
        adapter = new AdapterOrto(getActivity(), itemList);
        listView.setAdapter(adapter);

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_profile, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setQueryHint(getResources().getString(R.string.type_here));
        searchView.setOnQueryTextFocusChangeListener((this));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_search:
                // Do Fragment menu item stuff here
                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            searchMenuItem.collapseActionView();
            searchView.setQuery("", false);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String text) {
        adapter.getFilter().filter(text.toString());
        return false;
    }

    public void getData(final String username) {
        pb.setVisibility(View.VISIBLE);
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LIST_ORTO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);

                pb.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.setEmptyView(tvempty);

                if (response != null && !response.equals("") && !response.equals("null")) {
                    try {
                        DecimalFormat df = new DecimalFormat("###,###");
                        JSONArray jOar = new JSONArray(response);
                        for (int i = 0; i < jOar.length(); i++) {
                            try {
                                JSONObject obj = jOar.getJSONObject(i);
                                DisplayOrto items = new DisplayOrto();
                                items.setRecord_id(obj.getString("record_id"));
                                items.setName(obj.getString("name"));
                                items.setThumbnailUrl(AppConfig.URL_IMAGE + obj.getString("image") + ".jpg");
                                items.setPrice(String.valueOf(df.format(obj.getInt("price"))).replaceAll(",", "."));
                                items.setQty(obj.getString("quantity"));
                                items.setTotal(String.valueOf(df.format(obj.getInt("total"))).replaceAll(",", "."));

                                // adding movie to movies array
                                itemList.add(items);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(View.GONE);
                Log.e(TAG, "Data Error: " + error.getMessage());
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
                        String total = jObj.getString("total");
                        if (total != null && !total.isEmpty() && !total.equals("null") && !total.equals("")
                                && min != null && kir != null && minkir != null) {
                            double tot = Double.parseDouble(total);
                            tvTot.setText(String.valueOf(df.format(tot).replaceAll(",", ".")));
                            double minkir2 = Double.parseDouble(minkir);
                            double kir2 = Double.parseDouble(kir);
                            if (tot >= minkir2) {
                                tvDev.setText(" Gratis");
                            } else {
                                double shipping = kir2 - (tot * 0.2);
                                tvDev.setText(String.valueOf(df.format(shipping).replaceAll(",", ".")));
                            }
                        }

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

    public void getSetting() {

        JsonArrayRequest movieReq = new JsonArrayRequest(AppConfig.URL_SETTING,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                min = obj.getString("pmp");
                                kir = obj.getString("mbk");
                                minkir = obj.getString("pmk");
                                Log.e(TAG, "Values : " + min + " | " + kir);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
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
}



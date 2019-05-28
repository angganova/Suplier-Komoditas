package sastroman.suplierkomoditas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import adapter.AdapterSyarat;
import app.AppConfig;
import app.AppController;
import model.DisplayProducts;
import model.DisplaySyarat;

/**
 * Created by A10 on 03/02/2017.
 */
public class CompanyTab extends Fragment {
    // Log tag
    private static final String TAG = TabProducts.class.getSimpleName();

    //  json url
    private static final String url = (AppConfig.URL_SYARAT);
    private Button btSyarat;
    private ListView listView;
    private AdapterSyarat adapter;
    private ArrayList<DisplaySyarat> itemList = new ArrayList<DisplaySyarat>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_company, container, false);
        setHasOptionsMenu(true);

        listView = (ListView)rootView.findViewById(R.id.listView);

        Button btnLoadMore = new Button(getActivity());
        btnLoadMore.setText(R.string.tutup);
        btnLoadMore.setTextColor(Color.parseColor("#26ae90"));
        btnLoadMore.setBackgroundResource(R.color.btn_login_bg);

        itemList.clear();
        getData();

        adapter = new AdapterSyarat(getActivity(), itemList);
        listView.addFooterView(btnLoadMore);
        listView.setAdapter(adapter);

        btnLoadMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                listView.setVisibility(View.GONE);
            }
        });

        btSyarat = (Button)rootView.findViewById(R.id.btSyarat);
        btSyarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_profile2, menu);

    }

    public void getData() {

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                DisplaySyarat items = new DisplaySyarat();
                                items.setId(obj.getString("terms_id"));
                                items.setName(obj.getString("data"));

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

}
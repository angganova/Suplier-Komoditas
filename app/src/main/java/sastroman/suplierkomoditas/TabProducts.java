package sastroman.suplierkomoditas;

/**
 * Created by A10 on 01/02/2017.
 */

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import adapter.AdapterProducts;
import app.AppConfig;
import app.AppController;
import model.DisplayProducts;


public class TabProducts extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, SearchView.OnQueryTextListener{
    // Log tag
    private static final String TAG = TabProducts.class.getSimpleName();

    //  json url
    private static final String url = (AppConfig.URL_PRODUCTS);

    private ArrayList<DisplayProducts> itemList = new ArrayList<DisplayProducts>();
    private ArrayList<DisplayProducts> orderList = new ArrayList<>();
    private AdapterProducts adapter;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private ListView listView;
    private ProgressBar pb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.tab_products, container, false);

        ImageButton btVeg = (ImageButton)rootView.findViewById(R.id.btVeg);
        ImageButton btFru = (ImageButton)rootView.findViewById(R.id.btFru);
        ImageButton btMeat = (ImageButton)rootView.findViewById(R.id.btMeat);
        ImageButton btAll = (ImageButton)rootView.findViewById(R.id.btAll);
        ImageButton btHot = (ImageButton)rootView.findViewById(R.id.btHot);
        listView = (ListView)rootView.findViewById(R.id.lvitem);
        pb = (ProgressBar) rootView.findViewById(R.id.pb);

        itemList.clear();
        getData();

        adapter = new AdapterProducts(getActivity(), itemList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                DisplayProducts dv = (DisplayProducts) parent.getItemAtPosition(position);
                CheckBox chk = (CheckBox) view.findViewById(R.id.chk);
                if (chk.isChecked()) {
                    dv.setIsSelected(false);
                    chk.setChecked(false);
                } else {
                    chk.setChecked(true);
                    dv.setIsSelected(true);
                }
            }
        });

        btHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getSale().filter("hot");
            }
        });

        btVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getType().filter("vegetables");
            }
        });

        btFru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getType().filter("fruits");
            }
        });

        btMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getType().filter("lauk");
            }
        });

        btAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getType().filter("pokok");
            }
        });


        return rootView;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
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
            case R.id.action_order:

                getSelectedItems();
                if (orderList.isEmpty()) {
                    Toast.makeText(getActivity(), "Pilih minimal satu item",
                            Toast.LENGTH_SHORT).show();
                } else {
                    adapter.getAllData();

                    getSelectedItems();

                    Intent intent = new Intent(getActivity(), Order.class);
                    intent.putParcelableArrayListExtra("array_list", orderList);
                    startActivity(intent);
                    Log.d(TAG, "SelectedItems:" + orderList.toString());
                }

                // Do Activity menu item stuff here
                return true;

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



    public void getData() {

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pb.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                DecimalFormat df = new DecimalFormat("###,###");
                                JSONObject obj = response.getJSONObject(i);
                                DisplayProducts items = new DisplayProducts();
                                items.setId(obj.getString("id"));
                                items.setName(obj.getString("name"));
                                items.setThumbnailUrl(AppConfig.URL_IMAGE + obj.getString("image") + ".jpg");
                                items.setPrice1(String.valueOf(df.format(obj.getInt("retail_price"))).replaceAll(",", "."));
                                items.setPrice2(String.valueOf(df.format(obj.getInt("wholesaler_price"))).replaceAll(",", "."));
                                items.setKet(obj.getString("details"));
                                items.setUnit(obj.getString("unit"));
                                items.setType(obj.getString("type"));
                                items.setSale(obj.getString("sale"));

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

    private void getSelectedItems() {
        // TODO Auto-generated method stub

        orderList.clear();
        itemList= adapter.getSelectedList();

        int selectedListSize=itemList.size();
        for (int i = 0; i < selectedListSize; i++) {
            DisplayProducts dv = itemList.get(i);
            if (dv.isSelected()) {
                orderList.add(dv);
            }
        }
    }

}



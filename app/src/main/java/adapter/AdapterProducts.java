package adapter;

/**
 * Created by A10 on 29/01/2017.
 */

import android.content.Context;
import android.net.ParseException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import app.AppController;
import model.DisplayProducts;
import sastroman.suplierkomoditas.R;

public class AdapterProducts extends BaseAdapter implements Filterable{
    private LayoutInflater inflater;
    private ArrayList<DisplayProducts> Items = null;
    private ArrayList<DisplayProducts> filteredItems = null;
    private ItemFilter mFilter = new ItemFilter();
    private TypeFilter tFilter = new TypeFilter();
    private SaleFilter sFilter = new SaleFilter();

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AdapterProducts(Context context, ArrayList<DisplayProducts> data) {
        this.filteredItems = data;
        this.Items = data;
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<DisplayProducts> getSelectedList() {
        return filteredItems;
    }

    @Override
    public int getCount() {
            return filteredItems.size();
    }

    @Override
    public DisplayProducts getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list, null);
            holder = new ViewHolder();

            holder.thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.tvItem);
            holder.price1 = (TextView) convertView.findViewById(R.id.tvharga1);
            holder.price2 = (TextView) convertView.findViewById(R.id.tvprice);
            holder.ket = (TextView) convertView.findViewById(R.id.tvket);
            holder.chk = (CheckBox) convertView.findViewById(R.id.chk);

            holder.chk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox) view;
                    DisplayProducts displayProducts = (DisplayProducts) checkBox.getTag();
                    displayProducts.setIsSelected(checkBox.isChecked());
                }
            });
            convertView.setTag(holder);
        }else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        // getting data for the row
        DisplayProducts m = filteredItems.get(position);
        holder.thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        holder.title.setText(m.getName());
        holder.price1.setText(m.getPrice1());
        holder.price2.setText(m.getPrice2());
        holder.ket.setText(" Minimal " + m.getKet() + " " + m.getUnit());
        holder.chk.setChecked(m.isSelected());
        holder.chk.setTag(m);

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        NetworkImageView thumbnail;
        TextView price1;
        TextView price2;
        TextView ket;
        CheckBox chk;
    }



    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<DisplayProducts> filterItems = new ArrayList<DisplayProducts>();

                synchronized (this) {
                    for (int i = 0; i < Items.size(); i++) {
                        DisplayProducts dp = Items.get(i);
                        String item = dp.name;
                        if (item.toLowerCase().contains(filterString)) {
                            filterItems.add(dp);
                        }
                    }
                    results.count = filterItems.size();
                    results.values = filterItems;
                    Log.e("VALUES", results.values.toString());
                }
            } else {
                synchronized (this) {
                    results.values = Items;
                    results.count = Items.size();
                    Log.e("VALUES", results.values.toString());

                }
            }

            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<DisplayProducts>) results.values;
            notifyDataSetChanged();
        }

    }

    public Filter getType() {
        return tFilter;
    }

    private class TypeFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<DisplayProducts> filterItems = new ArrayList<DisplayProducts>();

                synchronized (this) {
                    for (int i = 0; i < Items.size(); i++) {
                        DisplayProducts dp = Items.get(i);
                        String item = dp.type;
                        if (item.toLowerCase().contains(filterString)) {
                            filterItems.add(dp);
                        }
                    }
                    results.count = filterItems.size();
                    results.values = filterItems;
                    Log.e("VALUES", results.values.toString());
                }
            } else {
                synchronized (this) {
                    results.values = Items;
                    results.count = Items.size();
                    Log.e("VALUES", results.values.toString());

                }
            }

            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<DisplayProducts>) results.values;
            notifyDataSetChanged();
        }

    }

    public Filter getSale() {
        return sFilter;
    }

    private class SaleFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<DisplayProducts> filterItems = new ArrayList<DisplayProducts>();

                synchronized (this) {
                    for (int i = 0; i < Items.size(); i++) {
                        DisplayProducts dp = Items.get(i);
                        String item = dp.sale;
                        if (item.toLowerCase().contains(filterString)) {
                            filterItems.add(dp);
                        }
                    }
                    results.count = filterItems.size();
                    results.values = filterItems;
                    Log.e("VALUES", results.values.toString());
                }
            } else {
                synchronized (this) {
                    results.values = Items;
                    results.count = Items.size();
                    Log.e("VALUES", results.values.toString());

                }
            }

            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<DisplayProducts>) results.values;
            notifyDataSetChanged();
        }

    }

    public void getAllData() {
        filteredItems = Items;
        notifyDataSetChanged();
    }

}

package adapter;

/**
 * Created by A10 on 29/01/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import app.AppController;
import model.DisplayPoin;
import sastroman.suplierkomoditas.R;

public class AdapterPoin extends BaseAdapter implements Filterable{
    private LayoutInflater inflater;
    private ArrayList<DisplayPoin> Items = null;
    private ArrayList<DisplayPoin> filteredItems = null;
    private ItemFilter mFilter = new ItemFilter();

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AdapterPoin(Context context, ArrayList<DisplayPoin> data) {
        this.filteredItems = data;
        this.Items = data;
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<DisplayPoin> getSelectedList() {
        return filteredItems;
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public DisplayPoin getItem(int position) {
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
            convertView = inflater.inflate(R.layout.list_poin, null);
            holder = new ViewHolder();

            holder.thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.tvItem);
            holder.ket = (TextView) convertView.findViewById(R.id.tvharga1);
            holder.qty = (TextView) convertView.findViewById(R.id.tvprice);
            holder.poin = (TextView) convertView.findViewById(R.id.tvket);
            convertView.setTag(holder);
        }else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        // getting data for the row
        DisplayPoin m = filteredItems.get(position);
        holder.thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        holder.title.setText(m.getName());
        holder.poin.setText(m.getPoin() + " Poin");
        holder.ket.setText(m.getKet() + " " + m.getUnit());
        holder.qty.setText("Harga : ");

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        NetworkImageView thumbnail;
        TextView poin;
        TextView qty;
        TextView ket;
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
                ArrayList<DisplayPoin> filterItems = new ArrayList<DisplayPoin>();

                synchronized (this) {
                    for (int i = 0; i < Items.size(); i++) {
                        DisplayPoin dp = Items.get(i);
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
            filteredItems = (ArrayList<DisplayPoin>) results.values;
            notifyDataSetChanged();
        }
    }
}

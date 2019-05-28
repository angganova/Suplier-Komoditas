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
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import app.AppController;
import model.DisplayOrto;
import sastroman.suplierkomoditas.R;

public class AdapterOrto extends BaseAdapter implements Filterable{
    private LayoutInflater inflater;
    private ArrayList<DisplayOrto> Items = null;
    private ArrayList<DisplayOrto> filteredItems = null;
    private ItemFilter mFilter = new ItemFilter();

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AdapterOrto(Context context, ArrayList<DisplayOrto> data) {
        this.filteredItems = data;
        this.Items = data;
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<DisplayOrto> getSelectedList() {
        return filteredItems;
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public DisplayOrto getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_orto, null);
            holder = new ViewHolder();

            holder.thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.tvItem);
            holder.price = (TextView) convertView.findViewById(R.id.tvharga);
            holder.qty = (TextView) convertView.findViewById(R.id.tvQty);
            holder.total = (TextView) convertView.findViewById(R.id.tvTotal);

            holder.chk = (CheckBox) convertView.findViewById(R.id.chk);

            holder.chk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox) view;
                    DisplayOrto displayOrto = (DisplayOrto) checkBox.getTag();
                    displayOrto.setIsSelected(checkBox.isChecked());
                }
            });
            convertView.setTag(holder);
        }else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }



        // getting data for the row
        final DisplayOrto m = filteredItems.get(position);
        holder.thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        holder.title.setText(m.getName());
        holder.price.setText(m.getPrice());
        holder.qty.setText(m.getQty());
        holder.total.setText(m.getTotal());
        holder.chk.setChecked(m.isSelected());
        holder.chk.setTag(m);


        return convertView;
    }


    static class ViewHolder {
        TextView title;
        NetworkImageView thumbnail;
        TextView price;
        TextView qty;
        TextView total;
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
                ArrayList<DisplayOrto> filterItems = new ArrayList<DisplayOrto>();

                synchronized (this) {
                    for (int i = 0; i < Items.size(); i++) {
                        DisplayOrto dp = Items.get(i);
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
            filteredItems = (ArrayList<DisplayOrto>) results.values;
            notifyDataSetChanged();
        }

    }

}

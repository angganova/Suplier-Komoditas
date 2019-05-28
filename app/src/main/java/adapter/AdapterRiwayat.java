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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import app.AppController;
import model.DisplayRiwayat;
import sastroman.suplierkomoditas.R;

public class AdapterRiwayat extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<DisplayRiwayat> Items = null;
    public int num = 2;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AdapterRiwayat(Context context, ArrayList<DisplayRiwayat> data) {
        this.Items = data;
        this.Items = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(num*10 > Items.size()){
            return Items.size();
        }else{
            return num*10;
        }
    }


    @Override
    public DisplayRiwayat getItem(int position) {
        return Items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_riwayat, null);
            holder = new ViewHolder();

            holder.status = (TextView) convertView.findViewById(R.id.tvStatus);
            holder.created = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.total = (TextView) convertView.findViewById(R.id.tvTotal);

            convertView.setTag(holder);
        }else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }



        // getting data for the row
        final DisplayRiwayat m = Items.get(position);
        holder.status.setText(m.getStatus());
        holder.created.setText(m.getCreated());
        holder.total.setText(m.getTotal());

        return convertView;
    }


    static class ViewHolder {
        TextView status;
        TextView created;
        TextView total;
    }

}

package adapter;

/**
 * Created by A10 on 29/01/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import app.AppController;
import model.DisplaySyarat;
import sastroman.suplierkomoditas.R;

public class AdapterSyarat extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<DisplaySyarat> Items = null;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AdapterSyarat(Context context, ArrayList<DisplaySyarat> data) {
        this.Items = data;
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<DisplaySyarat> getSelectedList() {
        return Items;
    }

    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public DisplaySyarat getItem(int position) {
        return Items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_syarat, null);
            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.tvName);
            holder.id = (TextView) convertView.findViewById(R.id.tvId);

            convertView.setTag(holder);
        }else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        // getting data for the row
        DisplaySyarat m = Items.get(position);
        holder.id.setText(m.getId());
        holder.title.setText(m.getName());

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView id;
    }

}

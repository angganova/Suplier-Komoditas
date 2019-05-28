package adapter;

/**
 * Created by A10 on 29/01/2017.
 */

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import app.AppController;
import model.DisplayOrder;
import sastroman.suplierkomoditas.R;

public class AdapterOrder extends BaseAdapter{
    private LayoutInflater inflater;
    private List<DisplayOrder> Items = null;
    private String[] arrTemp;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public AdapterOrder(Context context, List<DisplayOrder> data) {
        this.Items = data;
        inflater = LayoutInflater.from(context);
        arrTemp = new String[Items.size()];
    }

    @Override
    public int getCount() {

        if(Items != null && Items.size() != 0){
            return Items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_order, null);

            holder.thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.tvItem);
            holder.price1 = (TextView) convertView.findViewById(R.id.tvharga1);
            holder.price2 = (TextView) convertView.findViewById(R.id.tvprice);
            holder.ket = (TextView) convertView.findViewById(R.id.tvket);
            holder.qty = (EditText) convertView.findViewById(R.id.etQty);

            convertView.setTag(holder);
        }else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;
        // getting data for the row
        final DisplayOrder m = Items.get(position);
        holder.thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        holder.title.setText(m.getName());
        holder.price1.setText(m.getPrice1());
        holder.price2.setText(m.getPrice2());
        holder.ket.setText(" Minimal " + m.getKet() + " " + m.getUnit());
        holder.qty.setText(arrTemp[position]);


        holder.qty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                arrTemp[holder.ref] = arg0.toString();
                m.setQty(arrTemp[position]);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        NetworkImageView thumbnail;
        TextView price1;
        TextView price2;
        TextView ket;
        EditText qty;
        int ref;
    }
}


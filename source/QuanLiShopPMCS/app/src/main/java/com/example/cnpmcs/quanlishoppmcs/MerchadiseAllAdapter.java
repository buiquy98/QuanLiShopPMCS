package com.example.cnpmcs.quanlishoppmcs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class MerchadiseAllAdapter extends BaseAdapter {
    private List<Merchadise> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public MerchadiseAllAdapter(Context aContext, List<Merchadise> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public List<Merchadise> getListData(){
        return listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Merchadise getItem(int position) {return listData.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_pro_list_all, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.mitextView1);
            holder.sellView = (TextView) convertView.findViewById(R.id.mitextView2);
            holder.sumView = (TextView) convertView.findViewById(R.id.mitextView3);
            holder.priceView = (TextView) convertView.findViewById(R.id.mitextView4);
            holder.typeView = (TextView) convertView.findViewById(R.id.mitextView5);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        Merchadise merchadise = this.listData.get(position);
        holder.nameView.setText(merchadise.getName());
        if (merchadise.getSell()==1)
            holder.sellView.setText("Đang bán");
        else
            holder.sellView.setText("Chưa bắt đầu bán");
        holder.sumView.setText(String.valueOf(merchadise.getSum() + " " + merchadise.getCount()));
        //String.format("%,d", Long.parseLong(view.toString()));
        String d = String.valueOf(merchadise.getPrice());
        holder.priceView.setText(String.format("%,d", Long.parseLong(d)));
        holder.typeView.setText(String.valueOf(merchadise.getType()));
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView sellView;
        TextView sumView;
        TextView priceView;
        TextView typeView;
    }
}

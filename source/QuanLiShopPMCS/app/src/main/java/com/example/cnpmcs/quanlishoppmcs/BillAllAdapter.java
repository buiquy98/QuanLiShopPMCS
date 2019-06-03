package com.example.cnpmcs.quanlishoppmcs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BillAllAdapter extends BaseAdapter {
    private List<Bill> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public BillAllAdapter(Context aContext, List<Bill> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public List<Bill> getListData(){
        return listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Bill getItem(int position) {return listData.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_bill_item, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.item_bill_name);
            holder.pnameView = (TextView) convertView.findViewById(R.id.item_bill_personname);
            holder.priceView = (TextView) convertView.findViewById(R.id.item_bill_price);
            holder.timeView = (TextView) convertView.findViewById(R.id.item_bill_time);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        Bill bill = this.listData.get(position);
        holder.nameView.setText("Hóa đơn số " +String.valueOf(bill.getId()));
        holder.pnameView.setText(bill.getPname());
        //String.format("%,d", Long.parseLong(view.toString()));
        String d = String.valueOf(bill.getBillpr());
        holder.priceView.setText(String.format("%,d", Long.parseLong(d)));
        holder.timeView.setText(bill.getBilltime());
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView pnameView;
        TextView priceView;
        TextView timeView;
    }

}



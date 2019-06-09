package com.example.cnpmcs.quanlishoppmcs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class BillDetailAdapter extends BaseAdapter {
    private List<BillItem> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public BillDetailAdapter(Context aContext, List<BillItem> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public List<BillItem> getListData(){
        return listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public BillItem getItem(int position) {return listData.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_bill_detail_item, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.item_billdetailtemp_name);
            holder.amountView = (TextView) convertView.findViewById(R.id.item_billdetailtemp_amount);
            holder.miniprView = (TextView) convertView.findViewById(R.id.item_billdt_minipr);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        BillItem billItem = this.listData.get(position);
        DatabaseManager db = DatabaseManager.getInstance(convertView.getContext().getApplicationContext());
        Merchadise merchadise = db.getMerbyId(billItem.codem);
        holder.nameView.setText(merchadise.getName());
        holder.amountView.setText(String.valueOf(billItem.amountb));
        String d = String.valueOf(billItem.amountb* merchadise.getPrice());
        holder.miniprView.setText(String.format("%,d", Long.parseLong(d))+ " đ");
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView amountView;
        TextView miniprView;
    }
}

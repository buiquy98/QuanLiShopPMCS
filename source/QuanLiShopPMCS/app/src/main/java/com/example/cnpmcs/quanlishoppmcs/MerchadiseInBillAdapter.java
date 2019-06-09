package com.example.cnpmcs.quanlishoppmcs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class MerchadiseInBillAdapter extends BaseAdapter {
    private List<Merchadise> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private SomeInterface responder;

    public MerchadiseInBillAdapter(Context aContext, List<Merchadise> listData, SomeInterface test) {
        this.context = aContext;
        this.listData = listData;
        this.responder = test;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_mer_in_bill, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.bi_pro_name);
            holder.sumView = (TextView) convertView.findViewById(R.id.bi_pro_sum);
            holder.priceView = (TextView) convertView.findViewById(R.id.bi_pro_price);
            holder.amountView = (TextView) convertView.findViewById(R.id.bi_pro_amount);
            holder.outputView = (TextView) convertView.findViewById(R.id.bi_pro_output);
            holder.btndel = (Button) convertView.findViewById(R.id.btn_del_pro_in_bill);
            holder.upam = (Button) convertView.findViewById(R.id.btn_up_amount);
            holder.downam = (Button) convertView.findViewById(R.id.btn_down_amount);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        final Merchadise merchadise = this.listData.get(position);

        holder.btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.remove(position);
                notifyDataSetChanged();
                responder.dellBDTcodem(merchadise.getId());
            }
        });


        holder.nameView.setText(merchadise.getName());
        holder.amountView.setText(String.valueOf(merchadise.amount));
        holder.sumView.setText(String.valueOf((merchadise.getSum()- merchadise.amount) + " " + merchadise.getCount()));
        //String.format("%,d", Long.parseLong(view.toString()));
        String d = String.valueOf(merchadise.getPrice());
        holder.priceView.setText(String.format("%,d", Long.parseLong(d)));

        d = String.valueOf(merchadise.getPrice()* merchadise.amount);
        holder.outputView.setText(String.format("%,d", Long.parseLong(d))+" đ");

        holder.downam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (merchadise.amount>1) {
                    merchadise.amount--;
                    holder.amountView.setText(String.valueOf(merchadise.amount));
                    holder.sumView.setText(String.valueOf((merchadise.getSum()- merchadise.amount) + " " + merchadise.getCount()));
                    String dd = String.valueOf(merchadise.getPrice()* merchadise.amount);
                    holder.outputView.setText(String.format("%,d", Long.parseLong(dd))+" đ");
                    responder.updtoBDT(merchadise.getId(), merchadise.amount);
                }
            }
        });

        holder.upam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (merchadise.amount< merchadise.getSum()) {
                    merchadise.amount++;
                    holder.amountView.setText(String.valueOf(merchadise.amount));
                    holder.sumView.setText(String.valueOf((merchadise.getSum() - merchadise.amount) + " " + merchadise.getCount()));
                    String dd = String.valueOf(merchadise.getPrice() * merchadise.amount);
                    holder.outputView.setText(String.format("%,d", Long.parseLong(dd))+" đ");
                    responder.updtoBDT(merchadise.getId(), merchadise.amount);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView sumView;
        TextView priceView;
        TextView amountView;
        TextView outputView;
        Button btndel;
        Button upam;
        Button downam;
    }
}

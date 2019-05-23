package com.example.quanlishoppmcs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;



public class ProductInBillAdapter extends BaseAdapter {
    private List<Product> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private SomeInterfaces responder;

    public ProductInBillAdapter(Context aContext, List<Product> listData, SomeInterfaces test) {
        this.context = aContext;
        this.listData = listData;
        this.responder = test;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public List<Product> getListData(){
        return listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Product getItem(int position) {return listData.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_pro_in_bill, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.bi_mer_name);
            holder.sumView = (TextView) convertView.findViewById(R.id.bi_mer_sum);
            holder.priceView = (TextView) convertView.findViewById(R.id.bi_mer_price);
            holder.amountView = (TextView) convertView.findViewById(R.id.bi_mer_amount);
            holder.outputView = (TextView) convertView.findViewById(R.id.bi_mer_output);
            holder.btnDel = (Button) convertView.findViewById(R.id.btn_del_pro_in_bill);
            holder.upAmount = (Button) convertView.findViewById(R.id.btn_up_amount);
            holder.downAmount = (Button) convertView.findViewById(R.id.btn_down_amount);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        final Product pro = this.listData.get(position);

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.remove(position);
                notifyDataSetChanged();
                responder.delBillDetailTempCodeProduct(pro.getId());
            }
        });


        holder.nameView.setText(pro.getName());
        holder.amountView.setText(String.valueOf(pro.amount));
        holder.sumView.setText(String.valueOf((pro.getSum()-pro.amount) + " " + pro.getCount()));
        //String.format("%,d", Long.parseLong(view.toString()));
        String d = String.valueOf(pro.getPrice());
        holder.priceView.setText(String.format("%,d", Long.parseLong(d)));

        d = String.valueOf(pro.getPrice()*pro.amount);
        holder.outputView.setText(String.format("%,d", Long.parseLong(d))+" đ");

        holder.downAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pro.amount>1) {
                    pro.amount--;
                    holder.amountView.setText(String.valueOf(pro.amount));
                    holder.sumView.setText(String.valueOf((pro.getSum()-pro.amount) + " " + pro.getCount()));
                    String dd = String.valueOf(pro.getPrice()*pro.amount);
                    holder.outputView.setText(String.format("%,d", Long.parseLong(dd))+" đ");
                    responder.updateToBillDetailTemp(pro.getId(),pro.amount);
                }
            }
        });

        holder.upAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pro.amount<pro.getSum()) {
                    pro.amount++;
                    holder.amountView.setText(String.valueOf(pro.amount));
                    holder.sumView.setText(String.valueOf((pro.getSum() - pro.amount) + " " + pro.getCount()));
                    String dd = String.valueOf(pro.getPrice() * pro.amount);
                    holder.outputView.setText(String.format("%,d", Long.parseLong(dd))+" đ");
                    responder.updateToBillDetailTemp(pro.getId(),pro.amount);
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
        Button btnDel;
        Button upAmount;
        Button downAmount;
    }
}

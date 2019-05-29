package com.example.cnpmcs.quanlishoppmcs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Product_add extends Activity {
    protected DatabaseManager db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_edit_add);

        //set up toolbar phu cho activity, toolbar khong lien ket vs toolbar chinh cua ung dung
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_mer_add);
        toolbar.setTitle("Thêm Sản phẩm");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        db = DatabaseManager.getInstance(getBaseContext());

        final TextView Mername = (TextView) findViewById(R.id.mer_add_name);
        final TextView Mersum = (TextView) findViewById(R.id.mer_add_sum);
        final TextView Merbuy = (TextView) findViewById(R.id.mer_add_buy);
        final TextView Merprice = (TextView) findViewById(R.id.mer_add_price);
        final TextView Mercount = (TextView) findViewById(R.id.mer_add_count);
        final TextView Mernote = (TextView) findViewById(R.id.mer_add_note);

        Button btnt = (Button) findViewById(R.id.btn_second_mer);
        btnt.setVisibility(btnt.GONE);

        Button tempthing = (Button) findViewById(R.id.btn_cancel_mer);
        tempthing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product_add.super.onBackPressed();
            }
        });

        final Spinner Mersell = (Spinner) findViewById(R.id.spinner_mer_sell);
        List<String> sell = new ArrayList<String>();
        sell.add("Có"); sell.add("Không");
        Mersell.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,sell));

        final Spinner Mertype = (Spinner) findViewById(R.id.spinner_mer_type);
        List<String> type = new ArrayList<String>();
        type.addAll(db.getProductType());
        if (type.size()==0) type.add("");
        Mertype.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,type));

        Button btnadd = (Button) findViewById(R.id.btn_mer_add_ok);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Mername.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Tên để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Mersum.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Số lượng để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Merbuy.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Giá vốn để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Merprice.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Giá bán để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Mercount.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Đơn vị để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else
                {
                    int check = 0;
                    List<Product> listMer = db.getAllProduct();
                    for (Product item : listMer){
                        if (item.getName().toUpperCase().equals(Mername.getText().toString().toUpperCase())){ check=1; break;}
                    }
                    if (check==0)
                    {
                        Product temp = new Product();

                        temp.setName(Mername.getText().toString());
                        if (!Mersum.getText().toString().equals(""))
                            temp.setSum(Integer.valueOf(Mersum.getText().toString()));
                        if (Mersell.getSelectedItem().toString().equals("Có"))
                            temp.setSell(1);
                        else temp.setSell(0);
                        if (!Merbuy.getText().toString().equals(""))
                            temp.setBuy(Integer.valueOf(Merbuy.getText().toString()));
                        if (!Merprice.getText().toString().equals(""))
                            temp.setPrice(Integer.valueOf(Merprice.getText().toString()));
                        temp.setType(Mertype.getSelectedItem().toString());
                        temp.setCount(Mercount.getText().toString());
                        temp.setNote(Mernote.getText().toString());

                        db.addProduct(temp);
                        db.close();
                        setResult(100);
                        finish();
                    }
                    else{
                        Toast.makeText(view.getContext(), "Đã tồn tại tên sản phẩm trong database", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        finish();
    }
}

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


public class ProductAdd extends Activity {
    protected DatabaseManager db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_edit_add);

        //set up toolbar phu cho activity, toolbar khong lien ket vs toolbar chinh cua ung dung
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_pro_add);
        toolbar.setTitle("Thêm Sản phẩm");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        db = DatabaseManager.getInstance(getBaseContext());

        final TextView Proname = (TextView) findViewById(R.id.pro_add_name);
        final TextView Prosum = (TextView) findViewById(R.id.pro_add_sum);
        final TextView Probuy = (TextView) findViewById(R.id.pro_add_buy);
        final TextView Proprice = (TextView) findViewById(R.id.pro_add_price);
        final TextView Procount = (TextView) findViewById(R.id.pro_add_count);
        final TextView Pronote = (TextView) findViewById(R.id.pro_add_note);

        Button btnt = (Button) findViewById(R.id.btn_second_pro);
        btnt.setVisibility(btnt.GONE);

        Button tempthing = (Button) findViewById(R.id.btn_cancel_pro);
        tempthing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductAdd.super.onBackPressed();
            }
        });

        final Spinner Prosell = (Spinner) findViewById(R.id.spinner_pro_sell);
        List<String> sell = new ArrayList<String>();
        sell.add("Có"); sell.add("Không");
        Prosell.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,sell));

        final Spinner Protype = (Spinner) findViewById(R.id.spinner_pro_type);
        List<String> type = new ArrayList<String>();
        type.addAll(db.getProductType());
        if (type.size()==0) type.add("");
        Protype.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,type));

        Button btnadd = (Button) findViewById(R.id.btn_pro_add_ok);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Proname.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Tên để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Prosum.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Số lượng để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Probuy.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Giá vốn để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Proprice.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Giá bán để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Procount.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Đơn vị để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else
                {
                    int check = 0;
                    List<Product> listProduct = db.getAllProduct();
                    for (Product item : listProduct){
                        if (item.getName().toUpperCase().equals(Proname.getText().toString().toUpperCase())){ check=1; break;}
                    }
                    if (check==0)
                    {
                        Product temp = new Product();

                        temp.setName(Proname.getText().toString());
                        if (!Prosum.getText().toString().equals(""))
                        temp.setSum(Integer.valueOf(Prosum.getText().toString()));
                        if (Prosell.getSelectedItem().toString().equals("Có"))
                           temp.setSell(1);
                        else temp.setSell(0);
                        if (!Probuy.getText().toString().equals(""))
                        temp.setBuy(Integer.valueOf(Probuy.getText().toString()));
                        if (!Proprice.getText().toString().equals(""))
                        temp.setPrice(Integer.valueOf(Proprice.getText().toString()));
                        temp.setType(Protype.getSelectedItem().toString());
                        temp.setCount(Procount.getText().toString());
                        temp.setNote(Pronote.getText().toString());

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

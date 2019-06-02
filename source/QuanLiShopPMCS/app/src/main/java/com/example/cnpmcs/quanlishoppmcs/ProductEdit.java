package com.example.cnpmcs.quanlishoppmcs;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ProductEdit extends Activity {
    protected DatabaseManager db;
    protected int idt=0;
    protected Product mainpro;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_edit_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_pro_add);
        toolbar.setTitle("Thông tin Sản phẩm");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        db = DatabaseManager.getInstance(getBaseContext());

        final TextView Proname = (TextView) findViewById(R.id.pro_add_name);
        final TextView Prosum = (TextView) findViewById(R.id.pro_add_sum);
        final TextView Probuy = (TextView) findViewById(R.id.pro_add_buy);
        final TextView Proprice = (TextView) findViewById(R.id.pro_add_price);
        final TextView Procount = (TextView) findViewById(R.id.pro_add_count);
        final TextView Pronote = (TextView) findViewById(R.id.pro_add_note);
        final Spinner Prosell = (Spinner) findViewById(R.id.spinner_pro_sell);
        final Spinner Protype = (Spinner) findViewById(R.id.spinner_pro_type);
        List<String> selllist = new ArrayList<String>();
        selllist.add("Có");
        selllist.add("Không");
        Prosell.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,selllist));

        List<String> typelist = new ArrayList<String>();
        typelist.addAll(db.getProductType());
        typelist.add("");
        Protype.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,typelist));

        Button btnt = (Button) findViewById(R.id.btn_second_pro);
        btnt.setText("Xóa");

        Button btnadd = (Button) findViewById(R.id.btn_pro_add_ok);
        btnadd.setText("Cập nhật");
        Button tempthing = (Button) findViewById(R.id.btn_cancel_pro);

        tempthing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductEdit.super.onBackPressed();
            }
        });



        if (getIntent().getExtras() != null){
            idt = getIntent().getIntExtra("ID",0);
            mainpro = db.getProductById(idt);
            Proname.setText(mainpro.getName());
            Prosum.setText(String.valueOf(mainpro.getSum()));
            if (mainpro.getSell()==0)
                Prosell.setSelection(((ArrayAdapter)Prosell.getAdapter()).getPosition("Không"));
            else
                Prosell.setSelection(((ArrayAdapter)Prosell.getAdapter()).getPosition("Có"));
            Probuy.setText(String.valueOf(mainpro.getBuy()));
            Proprice.setText(String.valueOf(mainpro.getPrice()));
            Protype.setSelection(((ArrayAdapter)Protype.getAdapter()).getPosition(mainpro.getType()));
            Procount.setText(mainpro.getCount());
            Pronote.setText(mainpro.getNote());
        }

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Proname.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(), "Tên để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
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
                    if (check==1&&mainpro.getName().equals(Proname.getText().toString())) check=0;
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

                       // db.addPro(temp);
                        //Toast.makeText(view.getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        db.updateProduct(idt,temp);
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

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        db.delProduct(idt);
                        setResult(200);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        btnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Bạn có muốn xóa sản phẩm này?").setPositiveButton("Có", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        finish();
    }
}

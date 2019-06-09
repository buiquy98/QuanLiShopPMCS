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


public class MerchadiseEdit extends Activity {
    protected DatabaseManager db;
    protected int idt=0;
    protected Merchadise mainmer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mer_edit_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_pro_add);
        toolbar.setTitle("Thông tin Sản phẩm");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        db = DatabaseManager.getInstance(getBaseContext());

        final TextView Mername = (TextView) findViewById(R.id.pro_add_name);
        final TextView Mersum = (TextView) findViewById(R.id.pro_add_sum);
        final TextView Merbuy = (TextView) findViewById(R.id.pro_add_buy);
        final TextView Merprice = (TextView) findViewById(R.id.pro_add_price);
        final TextView Mercount = (TextView) findViewById(R.id.pro_add_count);
        final TextView Mernote = (TextView) findViewById(R.id.pro_add_note);
        final Spinner Mersell = (Spinner) findViewById(R.id.spinner_pro_sell);
        final Spinner Mertype = (Spinner) findViewById(R.id.spinner_pro_type);
        List<String> selllist = new ArrayList<String>();
        selllist.add("Có");
        selllist.add("Không");
        Mersell.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,selllist));

        List<String> typelist = new ArrayList<String>();
        typelist.addAll(db.getMerType());
        typelist.add("");
        Mertype.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,typelist));

        Button btnt = (Button) findViewById(R.id.btn_second_pro);
        btnt.setText("Xóa");

        Button btnadd = (Button) findViewById(R.id.btn_pro_add_ok);
        btnadd.setText("Cập nhật");
        Button tempthing = (Button) findViewById(R.id.btn_cancel_pro);

        tempthing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MerchadiseEdit.super.onBackPressed();
            }
        });



        if (getIntent().getExtras() != null){
            idt = getIntent().getIntExtra("ID",0);
            mainmer = db.getMerbyId(idt);
            Mername.setText(mainmer.getName());
            Mersum.setText(String.valueOf(mainmer.getSum()));
            if (mainmer.getSell()==0)
                Mersell.setSelection(((ArrayAdapter)Mersell.getAdapter()).getPosition("Không"));
            else
                Mersell.setSelection(((ArrayAdapter)Mersell.getAdapter()).getPosition("Có"));
            Merbuy.setText(String.valueOf(mainmer.getBuy()));
            Merprice.setText(String.valueOf(mainmer.getPrice()));
            Mertype.setSelection(((ArrayAdapter)Mertype.getAdapter()).getPosition(mainmer.getType()));
            Mercount.setText(mainmer.getCount());
            Mernote.setText(mainmer.getNote());
        }

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Mername.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(), "Tên để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Mersum.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Số lượng để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Merbuy.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Giá vốn để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Merprice.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Giá bán để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else if (Mercount.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Đơn vị để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else
                {
                    int check = 0;
                    List<Merchadise> listMerchadise = db.getAllMer();
                    for (Merchadise item : listMerchadise){
                        if (item.getName().toUpperCase().equals(Mername.getText().toString().toUpperCase())){ check=1; break;}
                    }
                    if (check==1&&mainmer.getName().equals(Mername.getText().toString())) check=0;
                    if (check==0)
                    {
                        Merchadise temp = new Merchadise();
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

                       // db.addMer(temp);
                        //Toast.makeText(view.getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        db.updMer(idt,temp);
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
                        db.delMer(idt);
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

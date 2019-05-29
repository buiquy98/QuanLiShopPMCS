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


public class PersonAdd extends Activity {
    protected DatabaseManager db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.per_edit_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_per_add);
        toolbar.setTitle("Thêm Khách Hàng");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        db = DatabaseManager.getInstance(getBaseContext());

        final TextView Pername = (TextView) findViewById(R.id.per_add_name);
        final TextView Perphone = (TextView) findViewById(R.id.per_add_phone);
        final TextView Peradd = (TextView) findViewById(R.id.per_add_add);
        final TextView Permail = (TextView) findViewById(R.id.per_add_mail);
        final TextView Pernote = (TextView) findViewById(R.id.per_add_note);

        Button btnt = (Button) findViewById(R.id.btn_second_per);
        btnt.setVisibility(btnt.GONE);

        Button tempthing = (Button) findViewById(R.id.btn_cancel_per);
        tempthing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonAdd.super.onBackPressed();
            }
        });


        final Spinner Pertype = (Spinner) findViewById(R.id.spinner_per_type);
        List<String> type = new ArrayList<String>();
        type.addAll(db.getPersonType());
        if (type.size()==0) type.add("");
        Pertype.setAdapter(new ArrayAdapter(this, R.layout.item_spinner,type));

        Button btnadd = (Button) findViewById(R.id.btn_per_add_ok);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Pername.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(), "Tên để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else {
                    int check = 0;
                    List<Person> listPer = db.getAllPerson();
                    for (Person item : listPer){
                        if (item.getName().toUpperCase().equals(Pername.getText().toString().toUpperCase())){ check=1; break;}
                    }
                    if (check==0){
                        Person temp = new Person();

                        temp.setName(Pername.getText().toString());
                        temp.setPhone(Perphone.getText().toString());
                        temp.setAdd(Peradd.getText().toString());
                        temp.setMail(Permail.getText().toString());
                        temp.setType(Pertype.getSelectedItem().toString());
                        temp.setNote(Pernote.getText().toString());

                        db.addPerson(temp);
                        db.close();
                        setResult(100);
                        finish();
                    }
                    else{
                        Toast.makeText(view.getContext(), "Đã tồn tại tên khách hàng trong database", Toast.LENGTH_SHORT).show();
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

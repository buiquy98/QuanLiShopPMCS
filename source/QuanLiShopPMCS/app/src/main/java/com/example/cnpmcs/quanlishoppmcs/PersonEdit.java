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



public class PersonEdit extends Activity {
    protected DatabaseManager db;
    protected Person mainper;
    private int idt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.per_edit_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_per_add);
        toolbar.setTitle("Thông tin Khách Hàng");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        db = DatabaseManager.getInstance(getBaseContext());

        final TextView Pername = (TextView) findViewById(R.id.per_add_name);
        final TextView Perphone = (TextView) findViewById(R.id.per_add_phone);
        final TextView Peradd = (TextView) findViewById(R.id.per_add_add);
        final TextView Permail = (TextView) findViewById(R.id.per_add_mail);
        final TextView Pernote = (TextView) findViewById(R.id.per_add_note);

        Button btnback = (Button) findViewById(R.id.btn_cancel_per);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonEdit.super.onBackPressed();
            }
        });

        final Spinner Pertype = (Spinner) findViewById(R.id.spinner_per_type);
        List<String> type = new ArrayList<String>();
        type.addAll(db.getPersonType());
        type.add("");
        Pertype.setAdapter(new ArrayAdapter(this,R.layout.item_spinner,type));

        Button btndelete = (Button) findViewById(R.id.btn_second_per);
        btndelete.setText("Xóa");

        if (getIntent().getExtras() != null){
            idt = getIntent().getIntExtra("ID",0);
            mainper = db.getPersonById(idt);
            Pername.setText(mainper.getName());
            Perphone.setText(mainper.getPhone());
            Peradd.setText(mainper.getAdd());
            Permail.setText(mainper.getMail());
            Pertype.setSelection(((ArrayAdapter)Pertype.getAdapter()).getPosition(mainper.getType()));
            Pernote.setText(mainper.getNote());
        }

        Button btnupdate = (Button) findViewById(R.id.btn_per_add_ok);
        btnupdate.setText("Cập nhật");
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Pername.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Tên để trống, xin nhập", Toast.LENGTH_SHORT).show(); }
                else {
                    int check = 0;
                    List<Person> listPer = db.getAllPerson();
                    for (Person item : listPer){
                        if (item.getName().toUpperCase().equals(Pername.getText().toString().toUpperCase())){ check=1; break;}
                    }
                    if (check==1&&mainper.getName().equals(Pername.getText().toString())) check=0;
                    if (check==0){
                        Person temp = new Person();

                        temp.setName(Pername.getText().toString());
                        temp.setPhone(Perphone.getText().toString());
                        temp.setAdd(Peradd.getText().toString());
                        temp.setMail(Permail.getText().toString());
                        temp.setType(Pertype.getSelectedItem().toString());
                        temp.setNote(Pernote.getText().toString());

                        db.updatePerson(idt,temp);
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

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        db.delPerson(idt);
                        setResult(200);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Bạn có muốn xóa khách hàng này?").setPositiveButton("Có", dialogClickListener)
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

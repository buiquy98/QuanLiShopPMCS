package com.example.cnpmcs.quanlishoppmcs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



public class PersonType extends Activity {
    DatabaseManager db;
    protected List<String> listType;
    protected ArrayAdapter adapter;
    protected ListView listView;
    protected TextView sltType;
    protected EditText newTypename;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_type_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_type);
        toolbar.setTitle("Thông tin Loại Khách Hàng");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        db = DatabaseManager.getInstance(getBaseContext());

         listType= db.getPerType();

        listView = (ListView) findViewById(R.id.list_all_type);
        adapter = new ArrayAdapter(this,R.layout.item_auto_complete,listType);
        listView.setAdapter(adapter);


        Button btnaddType = (Button) findViewById(R.id.btn_add_type);
        final EditText newType = (EditText) findViewById(R.id.new_type);

        btnaddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newType.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Tên kiểu cần thêm không được để trống", Toast.LENGTH_SHORT).show();}
                else {
                    int check = 0;
                    List<String> list = db.getPerType();
                    for (String item : list){
                        if (item.toUpperCase().equals(newType.getText().toString().toUpperCase())){ check=1; break;}
                    }
                    if (check==0){
                        listType.clear();
                        db.addPerType(newType.getText().toString());
                        listType= db.getPerType();
                        adapter = new ArrayAdapter(view.getContext(),R.layout.item_auto_complete,listType);
                        listView.setAdapter(adapter);
                        Toast.makeText(view.getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        newType.setText("");
                    } else
                        Toast.makeText(view.getContext(), "Đã tồn tại tên kiểu trong Database", Toast.LENGTH_SHORT).show();

                }
            }
        });

        sltType = (TextView) findViewById(R.id.type_selected);
        sltType.setText("Chưa chọn (Bấm vào tên loại để chọn)");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sltType.setText(adapter.getItem(i).toString());
            }
        });

        Button btndel = (Button) findViewById(R.id.btn_del_type);

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sltType.getText().toString().equals("Chưa chọn (Bấm vào tên loại để chọn)")) {
                    db.delPerType(sltType.getText().toString());
                    listType.clear();
                    listType = db.getPerType();
                    adapter = new ArrayAdapter(view.getContext(), R.layout.item_auto_complete, listType);
                    listView.setAdapter(adapter);
                    Toast.makeText(view.getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    sltType.setText("Chưa chọn (Bấm vào tên loại để chọn)");
                }else
                    Toast.makeText(view.getContext(), "Chưa chọn kiểu cần xóa", Toast.LENGTH_SHORT).show();
            }
        });

        newTypename = (EditText) findViewById(R.id.new_type_name);

        Button changeType = (Button) findViewById(R.id.btn_upd_type);

        changeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sltType.getText().toString().equals("Chưa chọn (Bấm vào tên loại để chọn)")) {Toast.makeText(view.getContext(), "Chưa chọn kiểu cần cập nhật", Toast.LENGTH_SHORT).show();}
                else if (newTypename.getText().toString().equals("")) {Toast.makeText(view.getContext(), "Chưa nhập tên mới cho kiểu", Toast.LENGTH_SHORT).show();}
                else
                {
                    int check = 0;
                    List<String> list = db.getPerType();
                    for (String item : list){
                        if (item.toUpperCase().equals(newTypename.getText().toString().toUpperCase())){ check=1; break;}
                    }
                    if (check==0){
                        //Toast.makeText(view.getContext(),"|"+ sltType.getText().toString()+"|\n|"+newTypename.getText().toString()+"|", Toast.LENGTH_SHORT).show();
                       // Toast.makeText(view.getContext(), String.valueOf(db.getMerTypeId(sltType.getText().toString())) , Toast.LENGTH_SHORT).show();

                         db.changePerType(sltType.getText().toString(),newTypename.getText().toString());
                        listType.clear();
                        listType = db.getPerType();
                        adapter = new ArrayAdapter(view.getContext(), R.layout.item_auto_complete, listType);
                        listView.setAdapter(adapter);
                        Toast.makeText(view.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        sltType.setText("Chưa chọn (Bấm vào tên loại để chọn)");
                        newTypename.setText("");
                    }
                    else Toast.makeText(view.getContext(), "Đã tồn tại tên kiểu trong Database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnback = (Button) findViewById(R.id.btn_cancel_type);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonType.super.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

package com.example.cnpmcs.quanlishoppmcs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class FragmentPerson extends Fragment{
    View myView;
    DatabaseManager db;
    protected ListView listView;
    protected List<Person> show = new ArrayList<Person>();
    protected PersonAllAdapter adapter;
    protected List<String> listtype = new ArrayList<String>();
    protected Spinner spinnertype;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_frag_per_all, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myView = view;
        getActivity().setTitle("Danh sách Khách hàng");
        db = new DatabaseManager(getActivity());
        listView = (ListView) view.findViewById(R.id.list_all_per);
        spinnertype = (Spinner) view.findViewById(R.id.spinner_per_all_type);

        final EditText searchname = (EditText) view.findViewById(R.id.per_name_search);
        final EditText searchphone = (EditText) view.findViewById(R.id.per_phone_search);

        updatelistitem(db.getAllPer());
        updatespinner();

        Button btnadd = (Button) view.findViewById(R.id.btn_add_per);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PersonAdd.class);
                startActivityForResult(intent,10);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchname.setText("");
                searchphone.setText("");
                Intent intent = new Intent(myView.getContext(), PersonEdit.class);
                intent.putExtra("ID",adapter.getItem(i).getId());
                startActivityForResult(intent,20);
            }
        });

        Button typeedit = (Button) view.findViewById(R.id.btn_per_type_edit);
        typeedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myView.getContext(), PersonType.class);
                startActivityForResult(intent,30);
            }
        });

        searchname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = searchname.getText().toString();
                if (temp.equals("")){
                    updatelistitem(db.getAllPer());
                }
                else{
                    searchphone.setText("");
                    updatelistitem(db.getPerbyPartName(temp));
                }
            }
        });

        searchphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = searchphone.getText().toString();
                if (temp.equals("")){
                    updatelistitem(db.getAllPer());
                }
                else{
                    searchname.setText("");
                    updatelistitem(db.getPerbyPartPhone(temp));
                }
            }
        });

        //filter san pham theo loai
        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchname.setText("");
                searchphone.setText("");
                if (spinnertype.getSelectedItem().toString().equals("Tất cả")){
                    updatelistitem(db.getAllPer());
                } else {
                    updatelistitem(db.getPerbyType(spinnertype.getSelectedItem().toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == 100) {
                updatelistitem(db.getAllPer());
                Toast.makeText(myView.getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else if (resultCode == 0)
                Toast.makeText(myView.getContext(), "Đã hủy thêm", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 20)
        {
            if (resultCode == 100){
                updatelistitem(db.getAllPer());
                Toast.makeText(myView.getContext(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == 200){
                updatelistitem(db.getAllPer());
                Toast.makeText(myView.getContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == 0)
                Toast.makeText(myView.getContext(),"Đã hủy cập nhật sản phẩm",Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 30){
            updatelistitem(db.getAllPer());
            updatespinner();
        }

    }

    protected void updatelistitem(List<Person> listtemp)
    {
        show.clear();
        show=listtemp;
        adapter = new PersonAllAdapter(myView.getContext(),show);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected void updatespinner(){
        listtype.clear();
        listtype.add("Tất cả");
        listtype.addAll(db.getPerType());
        spinnertype.setAdapter(new ArrayAdapter(myView.getContext(),R.layout.item_spinner,listtype));
    }
}

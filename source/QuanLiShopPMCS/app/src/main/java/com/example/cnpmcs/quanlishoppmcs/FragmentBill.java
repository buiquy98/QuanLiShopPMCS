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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FragmentBill extends Fragment {
    protected View myView;
    protected DatabaseManager db;
    protected List<Bill> show = new ArrayList<Bill>();
    protected ListView listView;
    protected BillAllAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_frag_bill_all, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myView = view;

        getActivity().setTitle("Danh sách Hóa đơn");
        db = new DatabaseManager(getActivity());

        listView = (ListView) view.findViewById(R.id.list_all_bill);
        updatelist(db.getAllBill());

        final EditText codesearch = (EditText) view.findViewById(R.id.bill_search_code);

        Button btncodesearch = (Button) view.findViewById(R.id.btn_search_code);
        btncodesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!codesearch.getText().toString().equals("")) {
                    int t = Integer.valueOf(codesearch.getText().toString());
                    List<Bill> listtemp = new ArrayList<Bill>();
                    listtemp = db.getAllBill();
                    Bill bill = new Bill();
                    for (Bill item : listtemp){
                        if (item.getId()==t)
                        {
                            bill = item;
                            break;
                        }
                    }
                    listtemp = new ArrayList<Bill>();

                    if (bill.getId()!=0){
                        listtemp.add(bill);
                        updatelist(listtemp);
                    }
                    else
                        updatelist(listtemp);
                } else{
                    updatelist(db.getAllBill());
                }
            }
        });

        final EditText pernamesearch = (EditText) view.findViewById(R.id.bill_search_name);
        pernamesearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!pernamesearch.getText().toString().equals("")){
                    List<Bill> listtemp = new ArrayList<Bill>();
                    listtemp = db.getBillbyPartName(pernamesearch.getText().toString());
                    updatelist(listtemp);
                }else
                    updatelist(db.getAllBill());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(myView.getContext(), BillDetail.class);
                intent.putExtra("ID",adapter.getItem(i).getId());
                startActivityForResult(intent,20);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 20){
            if (resultCode==100){
                updatelist(db.getAllBill());
                Toast.makeText(myView.getContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void updatelist(List<Bill> templist){
        show.clear();
        show.addAll(templist);
        adapter = new BillAllAdapter(myView.getContext(),show);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

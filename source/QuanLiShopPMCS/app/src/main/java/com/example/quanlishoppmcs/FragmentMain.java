package com.example.quanlishoppmcs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentMain extends Fragment implements SomeInterfaces {
    protected View myView;
    protected TabHost tab;//tab chinh cua lap hoa don
    protected DatabaseManager db;//co so du lieu
    protected TextView maintext;//title hoa don
    protected List<Product> listpro = new ArrayList<Product>();//danh sach san pham thuoc hoa don
    protected ListView listView;//list view cac san pham
    protected ProductInBillAdapter adapter;//adapter cua listview
    protected int main_bill_code;//ma hoa don
    protected int main_bill_pcode;//ma khach hang
    protected int main_bill_offPrii=0;//giam gia luong tien
    protected int main_bill_offPer=0;//giam gia %
    protected int main_bill_price=0;//tong tien hoa don
    protected String main_bill_time;//thoi gian hoa don
    protected TextView billPrice;//gia tien hoa don
    protected EditText billTime;//thoi gian lap hoa don

    protected LinearLayout billMore;
    protected Button btnBillOffPri;
    protected TextView offPri;
    protected Button btnBillOffPer;
    protected TextView offPer;
    protected Button btnBillLess;
    protected Button btnbillMore;

    protected TextView perName;
    protected TextView perPhone;
    protected TextView perAdd;
    protected TextView perMail;
    protected TextView perType;
    protected TextView perNote;

    //cập nhật số lượng của sản phẩm thuộc bill temp
    @Override
    public void updateToBillDetailTemp(int codepro, int billamount) {
        DatabaseManager ac = new DatabaseManager(myView.getContext());
        ac.updBillDetailTempAmount(codepro,billamount);
        ac.close();
        priceOfBill();
    }

    //xóa mã bill khỏi table bill temp
    @Override
    public void delBillDetailTempCodeBill(int codebill) {
        DatabaseManager ac = new DatabaseManager(myView.getContext());
        ac.delBillDetailTempCodeBi(codebill);
        ac.close();
        priceOfBill();
    }

    //xóa masp khỏi table sản phẩm temp
    @Override
    public void delBillDetailTempCodeProduct(int codepro) {
        DatabaseManager ac = new DatabaseManager(myView.getContext());
        ac.delBillDetailCodePr(codepro);
        ac.close();
        priceOfBill();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_frag_main, container,false);
        return myView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseManager(getActivity());//goi lai database tu main activity
        getActivity().setTitle("Lập hóa đơn");//set title
        main_bill_code = db.maxBillId() + 1;//lay so hoa don moi nhat
        //khoi tao text view title hoa don
        maintext = (TextView) view.findViewById(R.id.text_bill_name);

        //khoi tao cac text view ve thong tin khach hang
        perName = (TextView) view.findViewById(R.id.per_name);
        perPhone = (TextView) view.findViewById(R.id.per_phone);
        perAdd = (TextView) view.findViewById(R.id.per_add);
        perAdd.setMovementMethod(new ScrollingMovementMethod());//set scroll bar neu du lieu dai hon view
        perMail = (TextView) view.findViewById(R.id.per_mail);
        perType = (TextView) view.findViewById(R.id.per_type);
        perNote = (TextView) view.findViewById(R.id.per_note);
        perNote.setMovementMethod(new ScrollingMovementMethod());//tuong tu nhu tren
        personInBillReset();

        listView = (ListView) view.findViewById(R.id.mer_list);//list item thuoc hoa don

        billMoreLess();//xu ly khoi tao va thao tac voi nut bam chi tiet hoa don

        tabSetup(view);//set up tab

        suggestProduct(view);//xu ly suggest san pham va add san pham vao danh sach san pham

        suggestPerson(view);//xu ly suggest khach hang va cap nhat thong tin khach hang

        Button btnclean = (Button) view.findViewById(R.id.btn_bill_clear);
        btnclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delBillDetailTempCodeBill(0);
                listpro.clear();
                List<BillItem> billItemList = db.getBillDetailTempItem(0);
                for (BillItem item : billItemList) {
                    Product tada = db.getProductById(item.codepr);
                    tada.amount = item.amountbi;
                    listpro.add(tada);
                }
                adapter.notifyDataSetChanged();
                main_bill_code = db.maxBillId() + 1;
                main_bill_pcode = 0;
                main_bill_offPrii = 0;
                main_bill_offPer = 0;
                main_bill_time = "";
                //priceOfBill();
                maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - ");
                perName.setText("");
                perPhone.setText("");
                perAdd.setText("");
                perMail.setText("");
                perType.setText("");
                perNote.setText("");
                offPer.setText("");
                offPri.setText("");
                SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
                Date currentTime = Calendar.getInstance().getTime();
                billTime.setText(format.format(currentTime));
            }
        });
    }

    //khởi tạo và xử lí nút bấm giá tiền
    protected void billMoreLess(){
        billPrice = (TextView) myView.findViewById(R.id.text_bill_price);//Hiển thị giá tiền

        billTime = (EditText) myView.findViewById(R.id.bill_time);//thời gian bill

        billMore = (LinearLayout) myView.findViewById(R.id.bill_detail_container);
        billMore.setVisibility(billMore.GONE);//khởi tạo và set ẩn layout chi tiết phụ

        btnBillLess = (Button) myView.findViewById(R.id.btn_bill_detail_less);
        btnBillLess.setVisibility(btnBillLess.GONE);//khoi tao va set ẩn nút ẩn chi tiết phụ

        offPri = (TextView) myView.findViewById(R.id.bill_offPri);
        offPer = (TextView) myView.findViewById(R.id.bill_offPercent);
        SimpleDateFormat format= new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
        Date currentTime = Calendar.getInstance().getTime();
        billTime.setText(format.format(currentTime));

        btnbillMore = (Button) myView.findViewById(R.id.btn_bill_detail_more);
        btnbillMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnbillMore.setVisibility(btnbillMore.GONE);
                btnBillLess.setVisibility(btnBillLess.VISIBLE);
                billMore.setVisibility(billMore.VISIBLE);
            }
        });

        btnBillLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBillLess.setVisibility(btnBillLess.GONE);
                btnbillMore.setVisibility(btnbillMore.VISIBLE);
                billMore.setVisibility(billMore.GONE);
            }
        });

        btnBillOffPri = (Button) myView.findViewById(R.id.btn_offPri);
        btnBillOffPri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = 0;
                if (!offPri.getText().toString().equals(""))
                    t = Integer.valueOf(offPri.getText().toString());
                main_bill_offPrii=t;
                priceOfBill();
            }
        });

        btnBillOffPer = (Button) myView.findViewById(R.id.btn_offPer);
        btnBillOffPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = 0;
                if (!offPer.getText().toString().equals(""))
                    t=Integer.valueOf(offPer.getText().toString());
                main_bill_offPer=t;
                priceOfBill();
            }
        });
    }

    //khởi tạo và setup tab
    protected void tabSetup(View view){
        //khởi tạo tab qua viewid
        tab = (TabHost) view.findViewById(R.id.tabhost);
        tab.setup();

        //thêm tab con vào tab host
        TabHost.TabSpec spec = tab.newTabSpec("Sản Phẩm");
        spec.setContent(R.id.tab);
        spec.setIndicator("Sản Phẩm");
        tab.addTab(spec);

        spec = tab.newTabSpec("Khách Hàng");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Khách Hàng");
        tab.addTab(spec);
        //set chiều cao của tab điều khiển
        tab.getTabWidget().getChildAt(0).getLayoutParams().height = 80;
        tab.getTabWidget().getChildAt(1).getLayoutParams().height = 80;
    }
    // xử lí cập nhật thêm sản phẩm
    protected void suggestProduct(View view){

        listpro = new ArrayList<Product>(){};
        List<BillItem> billItemList = db.getBillDetailTempItem(0);
        for (BillItem item :billItemList){
            Product tada = db.getProductById(item.codepr);
            tada.amount = item.amountbi;
            listpro.add(tada);
        }
        priceOfBill();

        adapter = new ProductInBillAdapter(view.getContext(),listpro,this);
        listView.setAdapter(adapter);



        //xử lí text suggest vào cập nhật danh sách sản phẩm của bill
        final List<String> proname2 = new ArrayList<String>(){};
        final List<Product> listtemp = db.getProductSelling();
        for (Product item : listtemp){
            proname2.add(item.getName().toString());
        }
        final AutoCompleteTextView text = (AutoCompleteTextView) view.findViewById(R.id.pro_auto);
        text.setAdapter(new ArrayAdapter(view.getContext(),R.layout.item_auto_complete,proname2));



        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int check =0;
                int id =-1;
                String d = adapterView.getItemAtPosition(i).toString();
                for (Product item : listpro){
                    if (item.getName().equals(d)) {check=1; id=listpro.indexOf(item); break;}
                }
                if (check ==0) {
                    Product temp = db.getProductByName(d);
                    temp.amount = 1;
                    listpro.add(temp);
                    adapter.notifyDataSetChanged();
                    text.setText("");
                    db.addBillDetailTemp(0,temp.getId(),temp.amount);
                    //priceOfBill();
                }
                else {
                    if (listpro.get(id).getSum() > 0) {
                        listpro.get(id).amount++;
                        adapter.notifyDataSetChanged();
                        text.setText("");
                        updateToBillDetailTemp(listpro.get(id).getId(),listpro.get(id).amount);
                        //priceOfBill();
                    }
                }
            }
        });
    }
    //xử lí cập nhật khách hàng
    protected void suggestPerson(View view){
        //xử lí text suggest vào cập nhật danh sách sản phẩm của bill
        final List<String> perlist = new ArrayList<String>(){};
        List<Person> list = db.getAllPerson();
        for (Person item : list){
            perlist.add(item.getName().toString());
        }
        final AutoCompleteTextView text = (AutoCompleteTextView) view.findViewById(R.id.per_auto);
        text.setAdapter(new ArrayAdapter(view.getContext(),R.layout.item_auto_complete,perlist));

        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ten = adapterView.getItemAtPosition(i).toString();
                Person person = db.getPersonByName(ten.toString());
                perName.setText(person.getName());
                perPhone.setText(person.getPhone());
                perAdd.setText(person.getAdd());
                perMail.setText(person.getMail());
                perType.setText(person.getType());
                perNote.setText(person.getNote());
                text.setText("");
                text.clearFocus();
                maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - " +person.getName());
                main_bill_pcode = person.getId();
            }
        });

        Button btngetphone = (Button) view.findViewById(R.id.btn_bill_get_phone);
        btngetphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+perPhone.getText().toString()));
                startActivity(callIntent);
            }
        });
    }


    //reset các trường thông tin của khách hàng
    public void personInBillReset(){
        maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - ");
        main_bill_pcode = 0;
        perName.setText("");
        perPhone.setText("");
        perAdd.setText("");
        perMail.setText("");
        perType.setText("");
        perNote.setText("");
    }


    //cập nhật tổng tiền trong bill
    protected void priceOfBill(){
        int temp =0;
        for (Product item : listpro){
            temp=temp+item.amount*item.getPrice();
        }

        double ttt = (main_bill_offPer/100.00000)*temp;
        int dto = (int) ttt;
        temp=temp-dto;

        temp=temp-main_bill_offPer;

        main_bill_price=temp;
        String dd = String.valueOf(temp);

        billPrice.setText(String.format("%,d", Long.parseLong(dd))+" đ");
        if (temp==0)
            billPrice.setText("0 đ");
    }

    //cập nhật số lượng của sản phẩm thuộc bill temp

}

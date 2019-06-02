package com.example.cnpmcs.quanlishoppmcs;

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
import android.widget.Toast;

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
    protected int main_bill_offpr=0;//giam gia luong tien
    protected int main_bill_offpe=0;//giam gia %
    protected int main_bill_price=0;//tong tien hoa don
    protected String main_bill_time;//thoi gian hoa don
    protected TextView billprice;//gia tien hoa don
    protected EditText billTime;//thoi gian lap hoa don

    protected LinearLayout billmore;
    protected Button btnbilloffpr;
    protected TextView offPrice;
    protected Button btnbilloffpe;
    protected TextView offPercent;
    protected Button btnbillless;
    protected Button btnbillmore;

    protected TextView pername;
    protected TextView perphone;
    protected TextView peradd;
    protected TextView permail;
    protected TextView pertype;
    protected TextView pernote;
    @Nullable
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
        main_bill_code = db.maxBillId()+1;//lay so hoa don moi nhat
        //khoi tao text view title hoa don
        maintext = (TextView) view.findViewById(R.id.text_bill_name);

        //khoi tao cac text view ve thong tin khach hang
        pername =(TextView) view.findViewById(R.id.per_name);
        perphone =(TextView) view.findViewById(R.id.per_phone);
        peradd =(TextView) view.findViewById(R.id.per_add);
        peradd.setMovementMethod(new ScrollingMovementMethod());//set scroll bar neu du lieu dai hon view
        permail =(TextView) view.findViewById(R.id.per_mail);
        pertype =(TextView) view.findViewById(R.id.per_type);
        pernote =(TextView) view.findViewById(R.id.per_note);
        pernote.setMovementMethod(new ScrollingMovementMethod());//tuong tu nhu tren
        personInBillReset();

        listView = (ListView) view.findViewById(R.id.pro_list);//list item thuoc hoa don

        billmoreless();//xu ly khoi tao va thao tac voi nut bam chi tiet hoa don

        tabsetup(view);//set up tab

        suggestPro(view);//xu ly suggest san pham va add san pham vao danh sach san pham

        suggestPerson(view);//xu ly suggest khach hang va cap nhat thong tin khach hang

        Button btnclean = (Button) view.findViewById(R.id.btn_bill_clear);
        btnclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delBillDetailTempCodeBill(0);
                listpro.clear();
                List<BillItem> billItemList = db.getBillDetailTempItem(0);
                for (BillItem item :billItemList){
                    Product tada = db.getProductById(item.codepro);
                    tada.amount = item.amountb;
                    listpro.add(tada);
                }
                adapter.notifyDataSetChanged();
                main_bill_code = db.maxBillId() + 1;
                main_bill_pcode = 0;
                main_bill_offpr = 0;
                main_bill_offpe = 0;
                main_bill_time = "";
                priceOfBill();
                maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - ");
                pername.setText("");
                perphone.setText("");
                peradd.setText("");
                permail.setText("");
                pertype.setText("");
                pernote.setText("");
                offPercent.setText("");
                offPrice.setText("");
                SimpleDateFormat format= new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
                Date currentTime = Calendar.getInstance().getTime();
                billTime.setText(format.format(currentTime));
            }
        });

        Button btnsave = (Button) view.findViewById(R.id.btn_bill_save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_bill_pcode==0) {Toast.makeText(myView.getContext(),"Chưa nhập thông tin khách hàng",Toast.LENGTH_SHORT).show();}
                else if (listpro.size()==0) {Toast.makeText(myView.getContext(),"Hóa đơn chưa có sản phẩm",Toast.LENGTH_SHORT).show();}
                else {
                    main_bill_time = billTime.getText().toString();
                    main_bill_code = db.maxBillId() + 1;
                    String pName = db.getPersonById(main_bill_pcode).getName();
                    db.addBill(main_bill_pcode,pName,main_bill_offpe,main_bill_offpr,main_bill_price,main_bill_time);
                    List<BillItem> listitem = db.getBillDetailTempItem(0);
                    for (BillItem item : listitem) {
                        db.addBillDetail(main_bill_code, item.codepro, item.amountb);
                        Product product = db.getProductById(item.codepro);
                        db.updateProductSum(item.codepro, product.getSum()-item.amountb);
                        Toast.makeText(myView.getContext(), "Lưu hóa đơn thành công", Toast.LENGTH_SHORT).show();
                    }
                    delBillDetailTempCodeBill(0);
                    listpro.clear();
                    adapter.notifyDataSetChanged();
                    main_bill_code = db.maxBillId() + 1;
                    main_bill_pcode = 0;
                    main_bill_offpr = 0;
                    main_bill_offpe = 0;
                    main_bill_time = "";
                    maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - ");
                    priceOfBill();
                    pername.setText("");
                    perphone.setText("");
                    peradd.setText("");
                    permail.setText("");
                    pertype.setText("");
                    pernote.setText("");
                    offPercent.setText("");
                    offPrice.setText("");
                    SimpleDateFormat format= new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
                    Date currentTime = Calendar.getInstance().getTime();
                    billTime.setText(format.format(currentTime));
                }
            }
        });
    }

    //thao tac khoi tao va xy lu nut bam ben canh gia tien
    protected void billmoreless(){
        billprice = (TextView) myView.findViewById(R.id.text_bill_price);//<hien thi> gia tien hoa don

        billTime = (EditText) myView.findViewById(R.id.bill_time);//thoi gian cua hoa don

        billmore = (LinearLayout) myView.findViewById(R.id.bill_detail_container);
        billmore.setVisibility(billmore.GONE);//khoi tao va set ẩn layout chi tiết phụ

        btnbillless = (Button) myView.findViewById(R.id.btn_bill_detail_less);
        btnbillless.setVisibility(btnbillless.GONE);//khoi tao va set ẩn nút ẩn chi tiết phụ

        offPrice = (TextView) myView.findViewById(R.id.bill_offprice);
        offPercent = (TextView) myView.findViewById(R.id.bill_offpercent);
        SimpleDateFormat format= new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
        Date currentTime = Calendar.getInstance().getTime();
        billTime.setText(format.format(currentTime));

        btnbillmore = (Button) myView.findViewById(R.id.btn_bill_detail_more);
        btnbillmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnbillmore.setVisibility(btnbillmore.GONE);
                btnbillless.setVisibility(btnbillless.VISIBLE);
                billmore.setVisibility(billmore.VISIBLE);
            }
        });

        btnbillless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnbillless.setVisibility(btnbillless.GONE);
                btnbillmore.setVisibility(btnbillmore.VISIBLE);
                billmore.setVisibility(billmore.GONE);
            }
        });

        btnbilloffpr = (Button) myView.findViewById(R.id.btn_offprice);
        btnbilloffpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = 0;
                if (!offPrice.getText().toString().equals(""))
                    t = Integer.valueOf(offPrice.getText().toString());
                main_bill_offpr=t;
                priceOfBill();
            }
        });

        btnbilloffpe = (Button) myView.findViewById(R.id.btn_offpercent);
        btnbilloffpe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = 0;
                if (!offPercent.getText().toString().equals(""))
                    t=Integer.valueOf(offPercent.getText().toString());
                main_bill_offpe=t;
                priceOfBill();
            }
        });
    }

    //khoi tao va set up tab
    protected void tabsetup(View view){
        //khoi tao tab qua viewid
        tab = (TabHost) view.findViewById(R.id.tabhost);
        tab.setup();

        //them cac tab con vao tab host
        TabHost.TabSpec spec = tab.newTabSpec("Sản Phẩm");
        spec.setContent(R.id.tab);
        spec.setIndicator("Sản Phẩm");
        tab.addTab(spec);

        spec = tab.newTabSpec("Khách Hàng");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Khách Hàng");
        tab.addTab(spec);
        //set chieu cao cua tab dieu khien
        tab.getTabWidget().getChildAt(0).getLayoutParams().height = 80;
        tab.getTabWidget().getChildAt(1).getLayoutParams().height = 80;

    }

    //xu ly cap nhat them san pham
    protected void suggestPro(View view){

        listpro = new ArrayList<Product>(){};
        List<BillItem> billItemList = db.getBillDetailTempItem(0);
        for (BillItem item :billItemList){
            Product tada = db.getProductById(item.codepro);
            tada.amount = item.amountb;
            listpro.add(tada);
        }
        priceOfBill();

        adapter = new ProductInBillAdapter(view.getContext(),listpro,this);
        listView.setAdapter(adapter);



        //xu ly text suggest vao cap nhat danh sach san pham cua hoa don
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
                    priceOfBill();
                }
                else {
                    if (listpro.get(id).getSum() > 0) {
                        listpro.get(id).amount++;
                        adapter.notifyDataSetChanged();
                        text.setText("");
                        updateToBillDetailTemp(listpro.get(id).getId(),listpro.get(id).amount);
                        priceOfBill();
                    }
                }
            }
        });
    }

    //xu ly cap nhat khach hang
    protected void suggestPerson(View view){
        //xu ly text suggest vao cap nhat danh sach san pham cua hoa don
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
                pername.setText(person.getName());
                perphone.setText(person.getPhone());
                peradd.setText(person.getAdd());
                permail.setText(person.getMail());
                pertype.setText(person.getType());
                pernote.setText(person.getNote());
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
                callIntent.setData(Uri.parse("tel:"+perphone.getText().toString()));
                startActivity(callIntent);
            }
        });
    }

    //reset cac truong cua thong tin khach hang thuoc hoa don
    public void personInBillReset(){
        maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - ");
        main_bill_pcode = 0;
        pername.setText("");
        perphone.setText("");
        peradd.setText("");
        permail.setText("");
        pertype.setText("");
        pernote.setText("");
    }

    //cap nhat tong tien cua bill
    protected void priceOfBill(){
        int temp =0;
        for (Product item : listpro){
            temp=temp+item.amount*item.getPrice();
        }

        double ttt = (main_bill_offpe/100.00000)*temp;
        int dto = (int) ttt;
        temp=temp-dto;

        temp=temp-main_bill_offpr;

        main_bill_price=temp;
        String dd = String.valueOf(temp);

        billprice.setText(String.format("%,d", Long.parseLong(dd))+" đ");
        if (temp==0)
            billprice.setText("0 đ");
    }

    //cap nhat amount cua san pham thuoc bill tam thoi
    @Override
    public void updateToBillDetailTemp(int codemer,int bamount){
        DatabaseManager ac = new DatabaseManager(myView.getContext());
        ac.updateBillDetailTempAmount(codemer,bamount);
        ac.close();
        priceOfBill();
    }

    //phuong thuc xoa ma bill khoi table bill tam thoi, thuong la 0, sau nay se update nhieu bill 1 luc
    @Override
    public void delBillDetailTempCodeBill(int codebill){
        DatabaseManager ac = new DatabaseManager(myView.getContext());
        ac.delBillDetailTempCodeBill(codebill);
        ac.close();
        priceOfBill();
    }

    //phuong thuc xoa ma sp khoi table bill tam thoi
    @Override
    public void delBillDetailTempCodeProduct(int codepro){
        DatabaseManager ac = new DatabaseManager(myView.getContext());
        ac.delBillDetailCodeProduct(codepro);
        ac.close();
        priceOfBill();
    }
}


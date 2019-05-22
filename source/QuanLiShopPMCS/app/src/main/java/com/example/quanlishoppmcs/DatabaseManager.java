package com.example.quanlishoppmcs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static DatabaseManager sInstance;
    public static final String DATABASE_NAME = "shop_manager";

    private static final String TABLE_PERSON = "person"; //bang ve khach hang
    private static final String TABLE_PRODUCT = "product"; // bang ve san pham
    private static final String TABLE_PERSONTYE = "persontype"; // bang ve loai khach
    private static final String TABLE_PRODUCTTYPE = "producttype"; // bang ve loai sp
    private static final String TABLE_BILL = "bill"; // bang ve hoa don
    private static final String TABLE_BILLDETAIL = "billdetail"; // chi tiet hoa don
    private static final String TABLE_BILLDETAILTEMP = "billdetailtemp"; //chi tiet hoa don tam thoi

    private static final String ID = "id"; ///stt <khoa chinh cho moi bang>

    //Person tag
    private static final String CODEPe = "codeperson"; //code kh
    private static final String PeNAME = "personname";
    private static final String PePHONE = "personphone"; //sdt
    private static final String PeADD = "personadd"; //dia chi
    private static final String PeMAIL = "personmail"; //mail
    private static final String PeTYPE = "persontype";
    private static final String PeNOTE = "personnote";

    //Merchandise tag
    private static final String CODEPr = "codepro"; //code sp
    private static final String PrNAME = "proname";//ten
    private static final String PrSUM = "prosum"; //tong sl 1 mat hang
    private static final String PrSELL = "prosell"; //con ban hay ko(0,1)
    private static final String PrBUY = "probuy"; //gia mua vao
    private static final String PrPRICE = "proprice"; //gia ban ra
    private static final String PrTYPE = "protype";//loai
    private static final String PrCOUNT = "procount"; //don vi tinh
    private static final String PrNOTE = "pronote";//ghi chu cho kh, sp, bill

    //Bill tag
    private static final String CODEBi = "billcode"; //code bill
    private static final String BiTIME = "billtime"; //ngay bill
    private static final String BiAMOUNT = "billamount"; //so luong sp trong chi tiet hoa don
    private static final String BiOFFPER = "billoffpercent";
    private static final String BiOFFPRI = "billoffprice";
    private static final String BiPRICE = "billprice";


    private static Context contextt;




    //constructor
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d("DBManager", "DBManager: ");
        this.contextt = context;
    }

    //tra ve database tuong ung voi man hinh goc
    //toan bo ung dung chi su dung 1 database tu day
    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(contextt);
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_PERSON + " (" +
                ID + " integer primary key, " +
                PeNAME + " TEXT, " +
                PePHONE + " TEXT, " +
                PeADD + " TEXT, " +
                PeMAIL + " TEXT, " +
                PeTYPE + " TEXT, " +
                PeNOTE + " TEXT)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_PERSONTYE + " (" +
                ID + " integer primary key, " +
                PeTYPE + " TEXT)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_PRODUCT + " (" +
                ID + " integer primary key, " +
                PrNAME + " TEXT, " +
                PrSUM + " integer, " +
                PrSELL + " integer, " +
                PrBUY + " integer, " +
                PrPRICE + " integer, " +
                PrTYPE + " TEXT, " +
                PrCOUNT + " TEXT, " +
                PrNOTE + " TEXT)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_PRODUCTTYPE + " (" +
                ID + " integer primary key, " +
                PrTYPE + " TEXT)";
        db.execSQL(sqlQuery);


        sqlQuery = "CREATE TABLE " + TABLE_BILLDETAILTEMP + " (" +
                ID + " integer primary key, " +
                CODEBi + " integer, " +
                CODEPr + " integer, " +
                BiAMOUNT + " integer)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_BILL + " (" +
                ID + " integer primary key, " +
                CODEPe + " integer, " +
                PeNAME + " TEXT, " +
                BiOFFPER + " integer, " +
                BiOFFPRI + " integer, " +
                BiPRICE + " integer, " +
                BiTIME + " TEXT)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_BILLDETAIL + " (" +
                ID + " integer primary key, " +
                CODEBi + " integer, " +
                CODEPr + " integer, " +
                BiAMOUNT + " integer)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONTYE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLDETAILTEMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLDETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        onCreate(db);
    }

    //bill chi tiet chinh
    public int maxBillDetail(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BILLDETAIL, new String[]{ID}, null, null, null, null, null);
        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();
            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    public void addBillDetail(int codebill, int codemer, int amountb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxBillDetail()+1);
        values.put(CODEBi,codebill);
        values.put(CODEPr,codemer);
        values.put(BiAMOUNT,amountb);

        db.insert(TABLE_BILLDETAIL,null,values);
        db.close();
    }

    public void delBillDetailCodeBi(int codebill){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        db.delete(TABLE_BILLDETAIL, CODEBi + " = ?", s);
        db.close();
    }

    public void delBillDetailCodePr(int codepro){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codepro)};
        db.delete(TABLE_BILLDETAIL, CODEPr + " = ?", s);
        db.close();
    }

    public List<BillItem> getBillDetailItem(int codebill){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        Cursor cursor = db.query(TABLE_BILLDETAIL, new String[]{CODEPr, BiAMOUNT}, CODEBi + "=?", s, null, null, null);
        List<BillItem> list = new ArrayList<BillItem>();
        if (cursor.moveToFirst()) {
            do {
                BillItem billItem = new BillItem(cursor.getInt(0),cursor.getInt(1));
                list.add(billItem);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;

    }

    //bill chinh
    public int maxBillId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BILL, new String[]{ID}, null, null, null, null, null);
        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();
            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    public void addBill(int codeper, String pname,int offpe,int offpr,int billpr, String billtime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxBillId()+1);
        values.put(CODEPe,codeper);
        values.put(PeNAME,pname);
        values.put(BiOFFPER,offpe);
        values.put(BiOFFPRI,offpr);
        values.put(BiPRICE,billpr);
        values.put(BiTIME,billtime);

        db.insert(TABLE_BILL,null,values);
        db.close();
    }

    public void delBill(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        db.delete(TABLE_BILL, ID + " = ?", s);
        db.close();
        delBillDetailCodeBi(id);
    }

    public Bill getBillById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_BILL, new String[]{ID, CODEPe, PeNAME, BiOFFPER, BiOFFPRI, BiPRICE, BiTIME}, ID + "=?", s, null, null, null);
        Bill bill = new Bill();
        if (cursor != null) {
            cursor.moveToFirst();
            bill.setId(cursor.getInt(0));
            bill.setCodePe(cursor.getInt(1));
            bill.setPeName(cursor.getString(2));
            bill.setOffPer(cursor.getInt(3));
            bill.setOffPri(cursor.getInt(4));
            bill.setBillPri(cursor.getInt(5));
            bill.setBillTime(cursor.getString(6));
        }
        else
        {
            bill.setId(0);
            bill.setCodePe(0);
            bill.setPeName("");
            bill.setOffPri(0);
            bill.setOffPer(0);
            bill.setBillPri(0);
            bill.setBillTime("");
        }
        cursor.close();
        db.close();
        return bill;
    }

    public List<Bill> getBillbyPartName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BILL + " WHERE " +PeNAME+" LIKE '%" + name + "%'", null);
        List<Bill> list = new ArrayList<Bill>();
        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setId(cursor.getInt(0));
                bill.setCodePe(cursor.getInt(1));
                bill.setPeName(cursor.getString(2));
                bill.setOffPer(cursor.getInt(3));
                bill.setOffPri(cursor.getInt(4));
                bill.setBillPri(cursor.getInt(5));
                bill.setBillTime(cursor.getString(6));
                list.add(bill);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Bill> getAllBill(){
        List<Bill> list = new ArrayList<Bill>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BILL, new String[]{ID, CODEPe, PeNAME, BiOFFPER, BiOFFPRI, BiPRICE, BiTIME}, null,null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setId(cursor.getInt(0));
                bill.setCodePe(cursor.getInt(1));
                bill.setPeName(cursor.getString(2));
                bill.setOffPer(cursor.getInt(3));
                bill.setOffPri(cursor.getInt(4));
                bill.setBillPri(cursor.getInt(5));
                bill.setBillTime(cursor.getString(6));
                list.add(bill);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    //bill chi tiet phu
    public int maxBillDetailId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BILLDETAILTEMP, new String[]{ID}, null, null, null, null, null);
        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();
            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    public void addBillDetailTemp(int codebill, int codepro, int amountbi){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxBillDetailId()+1);
        values.put(CODEBi,codebill);
        values.put(CODEPr,codepro);
        values.put(BiAMOUNT,amountbi);

        db.insert(TABLE_BILLDETAILTEMP,null,values);
        db.close();
    }

    public List<BillItem> getBillDetailTempItem(int codebill){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        Cursor cursor = db.query(TABLE_BILLDETAILTEMP, new String[]{CODEPr, BiAMOUNT}, CODEBi + "=?", s, null, null, null);
        List<BillItem> list = new ArrayList<BillItem>();
        if (cursor.moveToFirst()) {
            do {
                BillItem billItem = new BillItem(cursor.getInt(0),cursor.getInt(1));
                list.add(billItem);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;

    }

    public void updBillDetailTempAmount(int cobemer,int amountb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(cobemer)};
        values.put(BiAMOUNT,amountb);
        db.update(TABLE_BILLDETAILTEMP, values, CODEPr + "=?", s);
        db.close();
    }

    public void delBillDetailTempCodePr(int codepro){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codepro)};
        db.delete(TABLE_BILLDETAILTEMP, CODEPr + " = ?", s);
        db.close();
    }

    public void delBillDetailTempCodeBi(int codebill){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        db.delete(TABLE_BILLDETAILTEMP, CODEBi + " = ?", s);
        db.close();
    }

    //person
    public void addPerson(Person person) {
        // Toast.makeText(context,"cha`",Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, maxPersonId()+1);
        values.put(PeNAME, person.getName());
        values.put(PePHONE, person.getPhone());
        values.put(PeADD, person.getAdd());
        values.put(PeMAIL, person.getMail());
        values.put(PeTYPE, person.getType());
        values.put(PeNOTE, person.getNote());

        db.insert(TABLE_PERSON, null, values);
        db.close();
    }

    public void delPerson(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        db.delete(TABLE_PERSON, ID + " = ?", s);
        db.close();
    }

    public void updatePerson(int id, Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(id)};

        values.put(PeNAME, person.getName());
        values.put(PePHONE, person.getPhone());
        values.put(PeADD, person.getAdd());
        values.put(PeMAIL, person.getMail());
        values.put(PeTYPE, person.getType());
        values.put(PeNOTE, person.getNote());
        db.update(TABLE_PERSON, values, ID + "=?", s);
        db.close();
    }

    public Person getPersonById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PeNAME, PePHONE, PeADD, PeMAIL, PeTYPE, PeNOTE}, ID + "=?", s, null, null, null);
        Person person = null;
        if (cursor != null) {
            cursor.moveToFirst();
            person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        }
        else { person = new Person(0,"","","","","","");}
        cursor.close();
        db.close();
        return person;
    }

    public Person getPersonByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{name};
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PeNAME, PePHONE, PeADD, PeMAIL, PeTYPE, PeNOTE}, PeNAME + "=?", s, null, null, null);
        Person person = null;
        if (cursor != null) {
            cursor.moveToFirst();
            person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        }
        cursor.close();
        db.close();
        return person;
    }

    public List<Person> getPersonByPartName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSON + " WHERE " +PeNAME+" LIKE '%" + name + "%'", null);
        List<Person> list = new ArrayList<Person>();
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                list.add(person);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Person> getPersonByPartPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSON + " WHERE " + PePHONE +" LIKE '%" + phone + "%'", null);

        List<Person> list = new ArrayList<Person>();
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                list.add(person);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Person> getPersonByPartMail(String mail) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSON + " WHERE "+ PeMAIL + " LIKE '%" + mail + "%'", null);

        List<Person> list = new ArrayList<Person>();
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                list.add(person);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Person> getPersonByType(String pertype) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{pertype};
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PeNAME, PePHONE, PeADD, PeMAIL, PeTYPE, PeNOTE}, PeTYPE + "=?", s, null, null, null);
        List<Person> list = new ArrayList<Person>();
        Person person = null;
        if (cursor.moveToFirst()) {
            do {
                person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                list.add(person);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Person> getAllPerson() {
        List<Person> list = new ArrayList<Person>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PeNAME, PePHONE, PeADD, PeMAIL, PeTYPE, PeNOTE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setId(cursor.getInt(0));
                person.setName(cursor.getString(1));
                person.setPhone(cursor.getString(2));
                person.setAdd(cursor.getString(3));
                person.setMail(cursor.getString(4));
                person.setType(cursor.getString(5));
                person.setNote(cursor.getString(6));
                list.add(person);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int maxPersonId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID}, null, null, null, null, null);

        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();

            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    public int countPerson() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID}, null, null, null, null, null);
        cursor.moveToFirst();
        int t = cursor.getCount();
        cursor.close();
        return t;
    }

    //Per Type
    public int maxPersonTypeId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSONTYE, new String[]{ID}, null, null, null, null, null);

        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();

            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    public void addPersonType(String pertype){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxPersonTypeId()+1);
        values.put(PeTYPE,pertype);
        db.insert(TABLE_PERSONTYE, null, values);
        db.close();
    }

    public List<String> getPersonType() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PERSONTYE, new String[]{PeTYPE}, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do{
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void delPersonType(String pertype) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{pertype};
        String temp = "";
        db.delete(TABLE_PERSONTYE, PeTYPE + " = ?", s);
        db.close();

        List<Person> list = getPersonByType(pertype);
        for (Person per :list){
            per.setType("");
            updatePerson(per.getId(),per);
        }
    }

    public void changePersonType(String pertypeold,String pertypenew){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{pertypeold};
        ContentValues contentValues = new ContentValues();
        contentValues.put(PeTYPE,pertypenew);
        db.update(TABLE_PERSONTYE,contentValues,PeTYPE+"=?",s);
        db.close();

        List<Person> list = getPersonByType(pertypeold);
        for (Person per :list){
            per.setType(pertypenew);
            updatePerson(per.getId(),per);
        }
    }

    //Product
    public void addProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxProductId()+1);
        values.put(PrNAME,product.getName());
        values.put(PrSUM,product.getSum());
        values.put(PrSELL,product.getSell());
        values.put(PrBUY,product.getBuy());
        values.put(PrPRICE,product.getPrice());
        values.put(PrTYPE,product.getType());
        values.put(PrCOUNT,product.getCount());
        values.put(PrNOTE,product.getNote());

        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }

    public void delProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        db.delete(TABLE_PRODUCT, ID + " = ?", s);
        db.close();
        delBillDetailTempCodePr(id);
        delBillDetailCodePr(id);
    }

    public void updateProduct(int id, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(id)};

        values.put(PrNAME,product.getName());
        values.put(PrSUM,product.getSum());
        values.put(PrSELL,product.getSell());
        values.put(PrBUY,product.getBuy());
        values.put(PrPRICE,product.getPrice());
        values.put(PrTYPE,product.getType());
        values.put(PrCOUNT,product.getCount());
        values.put(PrNOTE,product.getNote());
        db.update(TABLE_PRODUCT, values, ID + "=?", s);
        db.close();
    }

    public void updateProductSum(int id,int newsum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(id)};
        values.put(PrSUM,newsum);
        db.update(TABLE_PRODUCT, values, ID + "=?", s);
        db.close();
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{ID, PrNAME, PrSUM, PrSELL, PrBUY, PrPRICE, PrTYPE, PrCOUNT, PrNOTE}, ID + "=?", s, null, null, null);
        Product product = null;
        if (cursor != null) {
            cursor.moveToFirst();
            product = new Product();
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));
            product.setSum(cursor.getInt(2));
            product.setSell(cursor.getInt(3));
            product.setBuy(cursor.getInt(4));
            product.setPrice(cursor.getInt(5));
            product.setType(cursor.getString(6));
            product.setCount(cursor.getString(7));
            product.setNote(cursor.getString(8));
        }
        cursor.close();
        db.close();
        return product;
    }



    public List<Product> getProductByType(String mertype) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{mertype};
        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{ID, PrNAME, PrSUM, PrSELL, PrBUY, PrPRICE, PrTYPE, PrCOUNT, PrNOTE}, PrTYPE + "=?", s, null, null, null);
        List<Product> list = new ArrayList<Product>();
        Product product = null;
        if (cursor.moveToFirst()) {
            do {
                product = new Product(cursor.getInt(0),cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6),cursor.getString(7),cursor.getString(8));
                list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public Product getProductByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{name};
        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{ID, PrNAME, PrSUM, PrSELL, PrBUY, PrPRICE, PrTYPE, PrCOUNT, PrNOTE}, PrNAME + "=?", s, null, null, null);
        Product product = null;
        if (cursor != null) {
            cursor.moveToFirst();
            product = new Product();
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));
            product.setSum(cursor.getInt(2));
            product.setSell(cursor.getInt(3));
            product.setBuy(cursor.getInt(4));
            product.setPrice(cursor.getInt(5));
            product.setType(cursor.getString(6));
            product.setCount(cursor.getString(7));
            product.setNote(cursor.getString(8));
        }
        cursor.close();
        db.close();
        return product;
    }

    public List<Product> getProductByPartName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, CODEP, NAME, PHONE, ADD, MAIL, TYPE, NOTE}, NAME + "=?", name, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT + " WHERE " +PrNAME+" LIKE '%" + name + "%'", null);
        List<Product> list = new ArrayList<Product>();
        Product product = null;
        if (cursor.moveToFirst()) {
            do {
                product = new Product(cursor.getInt(0),cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6),cursor.getString(7),cursor.getString(8));
                list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Product> getAllProduct() {
        List<Product> list = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{ID, PrNAME, PrSUM, PrSELL, PrBUY, PrPRICE, PrTYPE, PrCOUNT, PrNOTE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setSum(cursor.getInt(2));
                product.setSell(cursor.getInt(3));
                product.setBuy(cursor.getInt(4));
                product.setPrice(cursor.getInt(5));
                product.setType(cursor.getString(6));
                product.setCount(cursor.getString(7));
                product.setNote(cursor.getString(8));
                list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Product> getProductSelling() {
        List<Product> list = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(1)};
        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{ID, PrNAME, PrSUM, PrSELL, PrBUY, PrPRICE, PrTYPE, PrCOUNT, PrNOTE}, PrSELL + "=?",s, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setSum(cursor.getInt(2));
                product.setSell(cursor.getInt(3));
                product.setBuy(cursor.getInt(4));
                product.setPrice(cursor.getInt(5));
                product.setType(cursor.getString(6));
                product.setCount(cursor.getString(7));
                product.setNote(cursor.getString(8));
                if (cursor.getInt(2)>0)
                    list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int countProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{ID}, null, null, null, null, null);
        cursor.moveToFirst();
        int t = cursor.getCount();
        cursor.close();
        return t;
    }

    public int maxProductId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, new String[]{ID}, null, null, null, null, null);

        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();

            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    //Mer Type
    public int maxProductTypeId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTTYPE, new String[]{ID}, null, null, null, null, null);

        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();
            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    public List<String> getProductType() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTTYPE, new String[]{PrTYPE}, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do{
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void addProductType(String producttype){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxProductTypeId()+1);
        values.put(PrTYPE,producttype);
        db.insert(TABLE_PRODUCTTYPE, null, values);
        db.close();
    }

    public void delProductType(String producttype) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{producttype};
        String temp = "";
        db.delete(TABLE_PRODUCTTYPE, PrTYPE + " = ?", s);
        db.close();

        List<Product> list = getProductByType(producttype);
        for (Product mer :list){
            mer.setType("");
            updateProduct(mer.getId(),mer);
        }
    }

    public void changeMerType(String mertypeold,String mertypenew){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{mertypeold};
        ContentValues contentValues = new ContentValues();
        contentValues.put(PrTYPE,mertypenew);
        db.update(TABLE_PRODUCTTYPE,contentValues,PrTYPE+"=?",s);
        db.close();

        List<Product> list = getProductByType(mertypeold);
        for (Product mer :list){
            mer.setType(mertypenew);
            updateProduct(mer.getId(),mer);
        }
    }











}

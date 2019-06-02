package com.example.cnpmcs.quanlishoppmcs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class DatabaseManager extends SQLiteOpenHelper {

    private static DatabaseManager sInstance;
    public static final String DATABASE_NAME = "shop_manager";

    private static final String TABLE_PERSON = "person"; //bang ve khach hang
    private static final String TABLE_PRO = "product"; // bang ve san pham
    private static final String TABLE_PT = "persontype"; // bang ve loai khach
    private static final String TABLE_PRODUCTTYPE = "protype"; // bang ve loai sp
    private static final String TABLE_BILL = "bill"; // bang ve hoa don
    private static final String TABLE_BD = "billdetail"; // chi tiet hoa don
    private static final String TABLE_BDTEMP = "billdetailtemp"; //chi tiet hoa don tam thoi

    private static final String ID = "id"; ///stt <khoa chinh cho moi bang>

    //Person tag
    private static final String CODEP = "codeperson"; //code kh
    private static final String PNAME = "pName";
    private static final String PPHONE = "pphone"; //sdt
    private static final String PADD = "padd"; //dia chi
    private static final String PMAIL = "pmail"; //mail
    private static final String PTYPE = "ptype";
    private static final String PNOTE = "pnote";

    //Productchandise tag
    private static final String CODEPRO = "codepro"; //code sp
    private static final String PRONAME = "proname";//ten
    private static final String PROSUM = "prosum"; //tong sl 1 mat hang
    private static final String PROSELL = "prosell"; //con ban hay ko(0,1)
    private static final String PROBUY = "probuy"; //gia mua vao
    private static final String PROPRICE = "proprice"; //gia ban ra
    private static final String PROTYPE = "protype";//loai
    private static final String PROCOUNT = "procount"; //don vi tinh
    private static final String PRONOTE = "pronote";//ghi chu cho kh, sp, bill

    //Bill tag
    private static final String CODEB = "billcode"; //code bill
    private static final String BTIME = "btime"; //ngay bill
    private static final String BAMOUNT = "bamount"; //so luong sp trong chi tiet hoa don
    private static final String BOFFPE = "boffpercent";
    private static final String BOFFPR = "boffprice";
    private static final String BPRICE = "bprice";


    private static Context contextt;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d("DatabaseManager", "DatabaseManager: ");
        this.contextt = context;
    }

    //tra ve database tuong ung voi man hinh goc
    //toan bo ung dung chi su dung 1 database tu day
    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            //sInstance = new DatabaseManager(context.getApplicationContext());
            sInstance = new DatabaseManager(contextt);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_PERSON + " (" +
                ID + " integer primary key, " +
                PNAME + " TEXT, " +
                PPHONE + " TEXT, " +
                PADD + " TEXT, " +
                PMAIL + " TEXT, " +
                PTYPE + " TEXT, " +
                PNOTE + " TEXT)";
        db.execSQL(sqlQuery);
       // Toast.makeText(context,"cha`",Toast.LENGTH_SHORT).show();
        sqlQuery = "CREATE TABLE " + TABLE_PT + " (" +
                ID + " integer primary key, " +
                PTYPE + " TEXT)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_PRO + " (" +
                ID + " integer primary key, " +
                PRONAME + " TEXT, " +
                PROSUM + " integer, " +
                PROSELL + " integer, " +
                PROBUY + " integer, " +
                PROPRICE + " integer, " +
                PROTYPE + " TEXT, " +
                PROCOUNT + " TEXT, " +
                PRONOTE + " TEXT)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_PRODUCTTYPE + " (" +
                ID + " integer primary key, " +
                PROTYPE + " TEXT)";
        db.execSQL(sqlQuery);


        sqlQuery = "CREATE TABLE " + TABLE_BDTEMP + " (" +
                ID + " integer primary key, " +
                CODEB + " integer, " +
                CODEPRO + " integer, " +
                BAMOUNT + " integer)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_BILL + " (" +
                ID + " integer primary key, " +
                CODEP + " integer, " +
                PNAME + " TEXT, " +
                BOFFPE + " integer, " +
                BOFFPR + " integer, " +
                BPRICE + " integer, " +
                BTIME + " TEXT)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_BD + " (" +
                ID + " integer primary key, " +
                CODEB + " integer, " +
                CODEPRO + " integer, " +
                BAMOUNT + " integer)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BDTEMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        onCreate(db);
    }

    //bill chi tiet chinh
        public int maxBillDetailId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BD, new String[]{ID}, null, null, null, null, null);
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
        values.put(ID,maxBillDetailId()+1);
        values.put(CODEB,codebill);
        values.put(CODEPRO,codemer);
        values.put(BAMOUNT,amountb);

        db.insert(TABLE_BD,null,values);
        db.close();
    }

    public void delBillDetailCodeBill(int codebill){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        db.delete(TABLE_BD, CODEB + " = ?", s);
        db.close();
    }

    public void delBillDetailCodeProduct(int codemer){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codemer)};
        db.delete(TABLE_BD, CODEPRO + " = ?", s);
        db.close();
    }

    public List<BillItem> getBillDetailItem(int codebill){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        Cursor cursor = db.query(TABLE_BD, new String[]{CODEPRO, BAMOUNT}, CODEB + "=?", s, null, null, null);
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

    public void addBill(int codeper, String pName,int offPercent,int offPrice,int billPrice,String billTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxBillId()+1);
        values.put(CODEP,codeper);
        values.put(PNAME,pName);
        values.put(BOFFPE,offPercent);
        values.put(BOFFPR,offPrice);
        values.put(BPRICE,billPrice);
        values.put(BTIME,billTime);

        db.insert(TABLE_BILL,null,values);
        db.close();
    }

    public void delBill(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        db.delete(TABLE_BILL, ID + " = ?", s);
        db.close();
        delBillDetailCodeBill(id);
    }

    public Bill getBillById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_BILL, new String[]{ID, CODEP, PNAME, BOFFPE, BOFFPR, BPRICE,BTIME}, ID + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
        Bill bill = new Bill();
        if (cursor != null) {
            cursor.moveToFirst();
            bill.setId(cursor.getInt(0));
            bill.setCodep(cursor.getInt(1));
            bill.setPname(cursor.getString(2));
            bill.setOffpe(cursor.getInt(3));
            bill.setOffpr(cursor.getInt(4));
            bill.setBillpr(cursor.getInt(5));
            bill.setBilltime(cursor.getString(6));
        }
        else
        {
            bill.setId(0);
            bill.setCodep(0);
            bill.setPname("");
            bill.setOffpr(0);
            bill.setOffpe(0);
            bill.setBillpr(0);
            bill.setBilltime("");
        }
        cursor.close();
        db.close();
        return bill;
    }

    public List<Bill> getBillByPartName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PNAME, PPHONE, PADD, PMAIL, PTYPE, PNOTE}, PNAME + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BILL + " WHERE " +PNAME+" LIKE '%" + name + "%'", null);
        List<Bill> list = new ArrayList<Bill>();
        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setId(cursor.getInt(0));
                bill.setCodep(cursor.getInt(1));
                bill.setPname(cursor.getString(2));
                bill.setOffpe(cursor.getInt(3));
                bill.setOffpr(cursor.getInt(4));
                bill.setBillpr(cursor.getInt(5));
                bill.setBilltime(cursor.getString(6));
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
        Cursor cursor = db.query(TABLE_BILL, new String[]{ID, CODEP, PNAME, BOFFPE, BOFFPR, BPRICE,BTIME}, null,null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setId(cursor.getInt(0));
                bill.setCodep(cursor.getInt(1));
                bill.setPname(cursor.getString(2));
                bill.setOffpe(cursor.getInt(3));
                bill.setOffpr(cursor.getInt(4));
                bill.setBillpr(cursor.getInt(5));
                bill.setBilltime(cursor.getString(6));
                list.add(bill);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    //bill chi tiet phu
    public int maxBillDetailTempId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BDTEMP, new String[]{ID}, null, null, null, null, null);
        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();
            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    public void addBillDetailTemp(int codebill, int codemer, int amountb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxBillDetailTempId()+1);
        values.put(CODEB,codebill);
        values.put(CODEPRO,codemer);
        values.put(BAMOUNT,amountb);

        db.insert(TABLE_BDTEMP,null,values);
        db.close();
    }

    public List<BillItem> getBillDetailTempItem(int codebill){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        Cursor cursor = db.query(TABLE_BDTEMP, new String[]{CODEPRO, BAMOUNT}, CODEB + "=?", s, null, null, null);
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

    public void updateBillDetailTempAmount(int cobemer,int amountb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(cobemer)};
        values.put(BAMOUNT,amountb);
        db.update(TABLE_BDTEMP, values, CODEPRO + "=?", s);
        db.close();
    }

    public void delBillDetailTempCodeProduct(int codemer){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codemer)};
        db.delete(TABLE_BDTEMP, CODEPRO + " = ?", s);
        db.close();
    }

    public void delBillDetailTempCodeBill(int codebill){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        db.delete(TABLE_BDTEMP, CODEB + " = ?", s);
        db.close();
    }

    //person
    public void addPerson(Person person) {
       // Toast.makeText(context,"cha`",Toast.LENGTH_SHORT).show();
       SQLiteDatabase db = this.getReadableDatabase();
       ContentValues values = new ContentValues();
         values.put(ID, maxPersonId()+1);
        values.put(PNAME, person.getName());
        values.put(PPHONE, person.getPhone());
        values.put(PADD, person.getAdd());
        values.put(PMAIL, person.getMail());
        values.put(PTYPE, person.getType());
        values.put(PNOTE, person.getNote());

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

        values.put(PNAME, person.getName());
        values.put(PPHONE, person.getPhone());
        values.put(PADD, person.getAdd());
        values.put(PMAIL, person.getMail());
        values.put(PTYPE, person.getType());
        values.put(PNOTE, person.getNote());
        db.update(TABLE_PERSON, values, ID + "=?", s);
        db.close();
    }

    public Person getPersonById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PNAME, PPHONE, PADD, PMAIL, PTYPE, PNOTE}, ID + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
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
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PNAME, PPHONE, PADD, PMAIL, PTYPE, PNOTE}, PNAME + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
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
       // Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PNAME, PPHONE, PADD, PMAIL, PTYPE, PNOTE}, PNAME + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSON + " WHERE " +PNAME+" LIKE '%" + name + "%'", null);
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

        //Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, CODEP, NAME, PHONE, ADD, MAIL, TYPE, NOTE}, NAME + "=?", name, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSON + " WHERE " + PPHONE +" LIKE '%" + phone + "%'", null);

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

        //Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, CODEP, NAME, PHONE, ADD, MAIL, TYPE, NOTE}, NAME + "=?", name, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSON + " WHERE "+ PMAIL + " LIKE '%" + mail + "%'", null);

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
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PNAME, PPHONE, PADD, PMAIL, PTYPE, PNOTE}, PTYPE + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
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
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, PNAME, PPHONE, PADD, PMAIL, PTYPE, PNOTE}, null, null, null, null, null);
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
        Cursor cursor = db.query(TABLE_PT, new String[]{ID}, null, null, null, null, null);

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
        values.put(PTYPE,pertype);
        db.insert(TABLE_PT, null, values);
        db.close();
    }

    public List<String> getPersonType() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PT, new String[]{PTYPE}, null, null, null, null, null);
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
        db.delete(TABLE_PT, PTYPE + " = ?", s);
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
        contentValues.put(PTYPE,pertypenew);
        db.update(TABLE_PT,contentValues,PTYPE+"=?",s);
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
        values.put(PRONAME, product.getName());
        values.put(PROSUM, product.getSum());
        values.put(PROSELL, product.getSell());
        values.put(PROBUY, product.getBuy());
        values.put(PROPRICE, product.getPrice());
        values.put(PROTYPE, product.getType());
        values.put(PROCOUNT, product.getCount());
        values.put(PRONOTE, product.getNote());

        db.insert(TABLE_PRO, null, values);
        db.close();
    }

    public void delProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        db.delete(TABLE_PRO, ID + " = ?", s);
        db.close();
        delBillDetailTempCodeProduct(id);
        delBillDetailCodeProduct(id);
    }

    public void updateProduct(int id, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(id)};

        values.put(PRONAME, product.getName());
        values.put(PROSUM, product.getSum());
        values.put(PROSELL, product.getSell());
        values.put(PROBUY, product.getBuy());
        values.put(PROPRICE, product.getPrice());
        values.put(PROTYPE, product.getType());
        values.put(PROCOUNT, product.getCount());
        values.put(PRONOTE, product.getNote());
        db.update(TABLE_PRO, values, ID + "=?", s);
        db.close();
    }

    public void updateProductSum(int id,int newsum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(id)};
        values.put(PROSUM,newsum);
        db.update(TABLE_PRO, values, ID + "=?", s);
        db.close();
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_PRO, new String[]{ID, PRONAME, PROSUM, PROSELL, PROBUY, PROPRICE, PROTYPE, PROCOUNT, PRONOTE}, ID + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
        Product product = null;
        if (cursor != null) {
            cursor.moveToFirst();
            product = new Product();
            //product = new Product(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
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
        Cursor cursor = db.query(TABLE_PRO, new String[]{ID, PRONAME, PROSUM, PROSELL, PROBUY, PROPRICE, PROTYPE, PROCOUNT, PRONOTE}, PROTYPE + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
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
        Cursor cursor = db.query(TABLE_PRO, new String[]{ID, PRONAME, PROSUM, PROSELL, PROBUY, PROPRICE, PROTYPE, PROCOUNT, PRONOTE}, PRONAME + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
       // Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRO + "WHERE name LIKE '%" + name + "%'", null);
        Product product = null;
        if (cursor != null) {
            cursor.moveToFirst();
            product = new Product();
            //product = new Product(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRO + " WHERE " +PRONAME+" LIKE '%" + name + "%'", null);
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
        Cursor cursor = db.query(TABLE_PRO, new String[]{ID, PRONAME, PROSUM, PROSELL, PROBUY, PROPRICE, PROTYPE, PROCOUNT, PRONOTE}, null, null, null, null, null);
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
        Cursor cursor = db.query(TABLE_PRO, new String[]{ID, PRONAME, PROSUM, PROSELL, PROBUY, PROPRICE, PROTYPE, PROCOUNT, PRONOTE}, PROSELL + "=?",s, null, null, null);
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
        Cursor cursor = db.query(TABLE_PRO, new String[]{ID}, null, null, null, null, null);
        cursor.moveToFirst();
        int t = cursor.getCount();
        cursor.close();
        return t;
    }

    public int maxProductId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRO, new String[]{ID}, null, null, null, null, null);

        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();

            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    //Product Type
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
        Cursor cursor = db.query(TABLE_PRODUCTTYPE, new String[]{PROTYPE}, null, null, null, null, null);
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

    public void addProductType(String mertype){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxProductTypeId()+1);
        values.put(PROTYPE,mertype);
        db.insert(TABLE_PRODUCTTYPE, null, values);
        db.close();
    }

    public void delProductType(String mertype) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{mertype};
        String temp = "";
        db.delete(TABLE_PRODUCTTYPE, PROTYPE + " = ?", s);
        db.close();

        List<Product> list = getProductByType(mertype);
        for (Product product :list){
            product.setType("");
            updateProduct(product.getId(), product);
        }
    }

    public void changeProductType(String mertypeold,String mertypenew){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{mertypeold};
       ContentValues contentValues = new ContentValues();
        contentValues.put(PROTYPE,mertypenew);
        db.update(TABLE_PRODUCTTYPE,contentValues,PROTYPE+"=?",s);
        db.close();

        List<Product> list = getProductByType(mertypeold);
        for (Product product :list){
            product.setType(mertypenew);
            updateProduct(product.getId(), product);
        }
    }
}

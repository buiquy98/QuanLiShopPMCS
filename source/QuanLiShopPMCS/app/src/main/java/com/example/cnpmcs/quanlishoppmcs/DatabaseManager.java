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
    private static final String TABLE_MER = "merchandise"; // bang ve san pham
    private static final String TABLE_PT = "persontype"; // bang ve loai khach
    private static final String TABLE_PRODUCTTYPE = "mertype"; // bang ve loai sp
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

    //Merchandise tag
    private static final String CODEM = "codemer"; //code sp
    private static final String MNAME = "mname";//ten
    private static final String MSUM = "msum"; //tong sl 1 mat hang
    private static final String MSELL = "msell"; //con ban hay ko(0,1)
    private static final String MBUY = "mbuy"; //gia mua vao
    private static final String MPRICE = "mprice"; //gia ban ra
    private static final String MTYPE = "mtype";//loai
    private static final String MCOUNT = "mcount"; //don vi tinh
    private static final String MNOTE = "mnote";//ghi chu cho kh, sp, bill

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

        sqlQuery = "CREATE TABLE " + TABLE_MER + " (" +
                ID + " integer primary key, " +
                MNAME + " TEXT, " +
                MSUM + " integer, " +
                MSELL + " integer, " +
                MBUY + " integer, " +
                MPRICE + " integer, " +
                MTYPE + " TEXT, " +
                MCOUNT + " TEXT, " +
                MNOTE + " TEXT)";
        db.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TABLE_PRODUCTTYPE + " (" +
                ID + " integer primary key, " +
                MTYPE + " TEXT)";
        db.execSQL(sqlQuery);


        sqlQuery = "CREATE TABLE " + TABLE_BDTEMP + " (" +
                ID + " integer primary key, " +
                CODEB + " integer, " +
                CODEM + " integer, " +
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
                CODEM + " integer, " +
                BAMOUNT + " integer)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BDTEMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        onCreate(db);
    }

    //bill chi tiet chinh
    public int maxBDid(){
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

    public void addBD(int codebill, int codemer, int amountb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxBDid()+1);
        values.put(CODEB,codebill);
        values.put(CODEM,codemer);
        values.put(BAMOUNT,amountb);

        db.insert(TABLE_BD,null,values);
        db.close();
    }

    public void delBDcodeb(int codebill){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        db.delete(TABLE_BD, CODEB + " = ?", s);
        db.close();
    }

    public void delBDcodem(int codemer){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codemer)};
        db.delete(TABLE_BD, CODEM + " = ?", s);
        db.close();
    }

    public List<BillItem> getBDitem(int codebill){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        Cursor cursor = db.query(TABLE_BD, new String[]{CODEM, BAMOUNT}, CODEB + "=?", s, null, null, null);
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
    public int maxBillid(){
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
        values.put(ID,maxBillid()+1);
        values.put(CODEP,codeper);
        values.put(PNAME,pName);
        values.put(BOFFPE,offPercent);
        values.put(BOFFPR,offPrice);
        values.put(BPRICE,billPrice);
        values.put(BTIME,billTime);

        db.insert(TABLE_BILL,null,values);
        db.close();
    }

    public void dellBill(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        db.delete(TABLE_BILL, ID + " = ?", s);
        db.close();
        delBDcodeb(id);
    }

    public Bill getBillbyId(int id) {
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

    public List<Bill> getBillbyPartName(String name){
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
    public int maxBDTid(){
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

    public void addBDT(int codebill, int codemer, int amountb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxBDTid()+1);
        values.put(CODEB,codebill);
        values.put(CODEM,codemer);
        values.put(BAMOUNT,amountb);

        db.insert(TABLE_BDTEMP,null,values);
        db.close();
    }

    public List<BillItem> getBDTitem(int codebill){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        Cursor cursor = db.query(TABLE_BDTEMP, new String[]{CODEM, BAMOUNT}, CODEB + "=?", s, null, null, null);
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

    public void updBDTamount(int cobemer,int amountb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(cobemer)};
        values.put(BAMOUNT,amountb);
        db.update(TABLE_BDTEMP, values, CODEM + "=?", s);
        db.close();
    }

    public void delBDTcodem(int codemer){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codemer)};
        db.delete(TABLE_BDTEMP, CODEM + " = ?", s);
        db.close();
    }

    public void delBDTcodeb(int codebill){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(codebill)};
        db.delete(TABLE_BDTEMP, CODEB + " = ?", s);
        db.close();
    }

    //person
    public void addPer(Person person) {
       // Toast.makeText(context,"cha`",Toast.LENGTH_SHORT).show();
       SQLiteDatabase db = this.getReadableDatabase();
       ContentValues values = new ContentValues();
         values.put(ID, maxPerid()+1);
        values.put(PNAME, person.getName());
        values.put(PPHONE, person.getPhone());
        values.put(PADD, person.getAdd());
        values.put(PMAIL, person.getMail());
        values.put(PTYPE, person.getType());
        values.put(PNOTE, person.getNote());

        db.insert(TABLE_PERSON, null, values);
        db.close();
    }

    public void delPer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        db.delete(TABLE_PERSON, ID + " = ?", s);
        db.close();
    }

    public void updPer(int id, Person person) {
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

    public Person getPerbyId(int id) {
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

    public Person getPerbyName(String name){
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

    public List<Person> getPerbyPartName(String name){
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

    public List<Person> getPerbyPartPhone(String phone) {
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

    public List<Person> getPerbyPartMail(String mail) {
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

    public List<Person> getPerbyType(String pertype) {
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

    public List<Person> getAllPer() {
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

    public int maxPerid(){
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

    public int countPer() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON, new String[]{ID}, null, null, null, null, null);
        cursor.moveToFirst();
        int t = cursor.getCount();
        cursor.close();
        return t;
    }

    //Per Type
    public int maxPerTypeid() {
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

    public void addPerType(String pertype){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxPerTypeid()+1);
        values.put(PTYPE,pertype);
        db.insert(TABLE_PT, null, values);
        db.close();
    }

    public List<String> getPerType() {
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

    public void delPerType(String pertype) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{pertype};
        String temp = "";
        db.delete(TABLE_PT, PTYPE + " = ?", s);
        db.close();

        List<Person> list = getPerbyType(pertype);
        for (Person per :list){
            per.setType("");
            updPer(per.getId(),per);
        }
    }

    public void changePerType(String pertypeold,String pertypenew){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{pertypeold};
        ContentValues contentValues = new ContentValues();
        contentValues.put(PTYPE,pertypenew);
        db.update(TABLE_PT,contentValues,PTYPE+"=?",s);
        db.close();

        List<Person> list = getPerbyType(pertypeold);
        for (Person per :list){
            per.setType(pertypenew);
            updPer(per.getId(),per);
        }
    }

    //Product
    public void addMer(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxMerid()+1);
        values.put(MNAME, product.getName());
        values.put(MSUM, product.getSum());
        values.put(MSELL, product.getSell());
        values.put(MBUY, product.getBuy());
        values.put(MPRICE, product.getPrice());
        values.put(MTYPE, product.getType());
        values.put(MCOUNT, product.getCount());
        values.put(MNOTE, product.getNote());

        db.insert(TABLE_MER, null, values);
        db.close();
    }

    public void delMer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        db.delete(TABLE_MER, ID + " = ?", s);
        db.close();
        delBDTcodem(id);
        delBDcodem(id);
    }

    public void updMer(int id, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(id)};

        values.put(MNAME, product.getName());
        values.put(MSUM, product.getSum());
        values.put(MSELL, product.getSell());
        values.put(MBUY, product.getBuy());
        values.put(MPRICE, product.getPrice());
        values.put(MTYPE, product.getType());
        values.put(MCOUNT, product.getCount());
        values.put(MNOTE, product.getNote());
        db.update(TABLE_MER, values, ID + "=?", s);
        db.close();
    }

    public void updMerSum(int id,int newsum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] s = new String[]{String.valueOf(id)};
        values.put(MSUM,newsum);
        db.update(TABLE_MER, values, ID + "=?", s);
        db.close();
    }

    public Product getMerbyId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_MER, new String[]{ID, MNAME, MSUM, MSELL, MBUY, MPRICE, MTYPE, MCOUNT, MNOTE}, ID + "=?", s, null, null, null);
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



    public List<Product> getMerbyType(String mertype) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{mertype};
        Cursor cursor = db.query(TABLE_MER, new String[]{ID, MNAME, MSUM, MSELL, MBUY, MPRICE, MTYPE, MCOUNT, MNOTE}, MTYPE + "=?", s, null, null, null);
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

    public Product getMerbyName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] s = new String[]{name};
        Cursor cursor = db.query(TABLE_MER, new String[]{ID, MNAME, MSUM, MSELL, MBUY, MPRICE, MTYPE, MCOUNT, MNOTE}, MNAME + "=?", s, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
       // Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MER + "WHERE name LIKE '%" + name + "%'", null);
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

    public List<Product> getMerbyPartName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.query(TABLE_PERSON, new String[]{ID, CODEP, NAME, PHONE, ADD, MAIL, TYPE, NOTE}, NAME + "=?", name, null, null, null);
        // query (String table, String[] columns, String selection, String[]selectionArgs, String groupBy, String having, String orderBy)
        // rawquery ("SELECT * FROM "+TABLE_NAME +" WHERE id = '1'",null)
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MER + " WHERE " +MNAME+" LIKE '%" + name + "%'", null);
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

    public List<Product> getAllMer() {
        List<Product> list = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MER, new String[]{ID, MNAME, MSUM, MSELL, MBUY, MPRICE, MTYPE, MCOUNT, MNOTE}, null, null, null, null, null);
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

    public List<Product> getMerselling() {
        List<Product> list = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{String.valueOf(1)};
        Cursor cursor = db.query(TABLE_MER, new String[]{ID, MNAME, MSUM, MSELL, MBUY, MPRICE, MTYPE, MCOUNT, MNOTE}, MSELL + "=?",s, null, null, null);
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

    public int countMer() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MER, new String[]{ID}, null, null, null, null, null);
        cursor.moveToFirst();
        int t = cursor.getCount();
        cursor.close();
        return t;
    }

    public int maxMerid(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MER, new String[]{ID}, null, null, null, null, null);

        int t = 0;
        if (cursor.getCount()!=0) {
            cursor.moveToLast();

            t = cursor.getInt(0);
        }
        cursor.close();
        return t;
    }

    //Product Type
    public int maxMerTypeid() {
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

    public List<String> getMerType() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTTYPE, new String[]{MTYPE}, null, null, null, null, null);
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

    public void addMerType(String mertype){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,maxMerTypeid()+1);
        values.put(MTYPE,mertype);
        db.insert(TABLE_PRODUCTTYPE, null, values);
        db.close();
    }

    public void delMerType(String mertype) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{mertype};
        String temp = "";
        db.delete(TABLE_PRODUCTTYPE, MTYPE + " = ?", s);
        db.close();

        List<Product> list = getMerbyType(mertype);
        for (Product product :list){
            product.setType("");
            updMer(product.getId(), product);
        }
    }

    public void changeMerType(String mertypeold,String mertypenew){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] s = new String[]{mertypeold};
       ContentValues contentValues = new ContentValues();
        contentValues.put(MTYPE,mertypenew);
        db.update(TABLE_PRODUCTTYPE,contentValues,MTYPE+"=?",s);
        db.close();

        List<Product> list = getMerbyType(mertypeold);
        for (Product product :list){
            product.setType(mertypenew);
            updMer(product.getId(), product);
        }
    }
}

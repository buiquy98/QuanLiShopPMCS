package com.example.cnpmcs.quanlishoppmcs;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Fragment> listfr;//list fragment cua toan bo ung dung, luu tru lai de de xu ly
    DatabaseManager db; // co so du lieu cua toan bo ung dung

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Quản Lý Shop");//set tilte cho main acti tuong ung
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Quản Lý Shop"); //set title cho toan bo cac man hinh cung 1 title
        setSupportActionBar(toolbar);

        //xu ly navigation view
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //khoi tao cac fragment
        listfr = new ArrayList<Fragment>();
        //listfr.add(new FragmentMain());
        listfr.add(new FragmentPerson());
        listfr.add(new FragmentBill());

        db = new DatabaseManager(this);

        db.close();

        //set up fragment lap hoa don khi bat dau ung dung
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.area,listfr.get(0));
        ft.commit();


    }

    //xu ly nut back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //menu item tren thanh toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //action khi bam vao menu icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     //   if (id == R.id.action_reload) {
     //       return true;
      //  }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//--
        if (id == R.id.nav_bill_detail) {

            //check neu khong o main bill thi bam vao ko tu reload va add vao back stack de de dang out ung dung
            if (!MainActivity.this.getTitle().toString().equals("Quản Lý Shop")){
                ft.replace(R.id.area,listfr.get(0)).addToBackStack("mainfrag");
                ft.commit();
            }
        }
        else if (id == R.id.nav_person) {

            //reset thong tin khach hang trong hoa don, de tranh tinh trang khach hang bi xoa, se cap nhat
            FragmentMain abc = (FragmentMain) listfr.get(0);
            abc.personInBillReset();

            // load fragment danh sach khach hang
            ft.replace(R.id.area,listfr.get(1)).addToBackStack("");
            ft.commit();
        } else if (id == R.id.nav_bill) {
            // load fragment danh sach hoa don
            ft.replace(R.id.area,listfr.get(3)).addToBackStack("");
            ft.commit();
        }
        //--


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

package com.example.cnpmcs.quanlishoppmcs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<Fragment> listfr;//list fragment cua toan bo ung dung, luu tru lai de de xu ly
    DatabaseManager db; // co so du lieu cua toan bo ung dung

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
        this.setTitle("Ứng dụng Quản Lý Shop");//set tilte cho activity_main tương ứng
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //xử lí navigation view
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //khoi tao cac fragment
        listfr = new ArrayList<Fragment>();
        listfr.add(new FragmentMain());

        //khoi tao database
        db = new DatabaseManager(this);

        db.close();

        // set up frangment main khi start app
       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.area,listfr.get(0));
        ft.commit();
    }
    //xử lí nút back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //menu item trên thanh toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //action khi bấm vào menu icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bill_detail) {
            // Handle the camera action
            //check nếu không ở main bill thì bấm vào không tự reload và add vào back stack để dễ dàng exit ứng dụng
            if (!MainActivity.this.getTitle().toString().equals("Quản Lý Shop")){
                ft.replace(R.id.area,listfr.get(0)).addToBackStack("mainfrag");
                ft.commit();
        } else if (id == R.id.nav_product) {
                // load fragment danh sách sản phẩm
                ft.replace(R.id.area,listfr.get(1)).addToBackStack("");
                ft.commit();
            }

        } else if (id == R.id.nav_person) {
            //reset thông tin khách hàng trong bill, đễ tránh tình trạng khách hàng bị xóa
            FragmentMain abc = (FragmentMain) listfr.get(0);
            //abc.personInBillReset();

            // load fragment danh sách khách hàng
            ft.replace(R.id.area,listfr.get(2)).addToBackStack("");
            ft.commit();

        } else if (id == R.id.nav_bill) {
            // load fragment danh sách hóa đơn
            ft.replace(R.id.area, listfr.get(3)).addToBackStack("");
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

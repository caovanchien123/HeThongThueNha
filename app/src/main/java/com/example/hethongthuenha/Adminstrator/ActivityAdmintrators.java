package com.example.hethongthuenha.Adminstrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.hethongthuenha.Adminstrator.Fragment.fragment_account;
import com.example.hethongthuenha.Adminstrator.Fragment.fragment_ads;
import com.example.hethongthuenha.Adminstrator.Fragment.fragment_commission;
import com.example.hethongthuenha.Adminstrator.Fragment.fragment_pay;
import com.example.hethongthuenha.Adminstrator.Fragment.fragment_refund;
import com.example.hethongthuenha.Adminstrator.Fragment.fragment_add_point;
import com.example.hethongthuenha.Adminstrator.Fragment.fragment_report;
import com.example.hethongthuenha.Adminstrator.Fragment.fragmment_history_credit;
import com.example.hethongthuenha.MainActivity.MainActivity;
import com.example.hethongthuenha.R;
import com.example.hethongthuenha.Adminstrator.Fragment.fragment_setting_account;
import com.google.android.material.navigation.NavigationView;

public class ActivityAdmintrators extends AppCompatActivity {


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admintrators);

        Init();
        setEvent();
        SetFragment(R.id.mnCommission);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open_drawer,
                R.string.navigation_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //dung cai nay de get id tu drawlayout
        //View view=navigationView.getHeaderView(0);
        //bt_person=view.findViewById(R.id.bt_person);
    }

    private void setEvent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                SetFragment(menuItem.getItemId());
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void SetFragment(int id) {
        switch (id) {
            case R.id.mnCommission:
                fragment = new fragment_commission();
                toolbar.setTitle("Hoa hồng");
                break;
            case R.id.mnPay:
                fragment = new fragment_pay();
                toolbar.setTitle("Thanh Toán");
                break;
            case R.id.mnMember:
                fragment = new fragment_account();
                toolbar.setTitle("Tài khoản thành viên");
                break;
            case R.id.mnSettingMember:
                fragment = new fragment_setting_account();
                toolbar.setTitle("Quản lý tài khoản");
                break;
            case R.id.mnAds:
                fragment = new fragment_ads();
                toolbar.setTitle("Quảng cáo");
                break;
            case R.id.mnRepay:
                fragment = new fragment_add_point();
                toolbar.setTitle("Nạp tiền");
                break;
            case R.id.mnReport:
                fragment = new fragment_report();
                toolbar.setTitle("Báo cáo");
                break;
            case R.id.mnRefund:
                fragment = new fragment_refund();
                toolbar.setTitle("Hoàn tiền");
                break;
            case R.id.mnHistory:
                fragment = new fragmment_history_credit();
                toolbar.setTitle("Lịch sử giao dịch");
                break;
                case R.id.mnExit:
                startActivity(new Intent(ActivityAdmintrators.this, MainActivity.class));
                finish();
                break;
        }

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();

        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameAdminstrator, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            startActivity(new Intent(ActivityAdmintrators.this, MainActivity.class));
            finish();
        }

        super.onBackPressed();
    }
}
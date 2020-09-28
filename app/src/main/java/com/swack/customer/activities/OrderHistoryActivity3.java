package com.swack.customer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.swack.customer.R;
import com.swack.customer.adapter.ViewPagerOrderAdapter2;
import com.swack.customer.adapter.ViewPagerOrderAdapter3;

import es.dmoral.toasty.Toasty;

public class OrderHistoryActivity3 extends BaseActivity {

    private ViewPager viewPagerCart;
    private ViewPagerOrderAdapter3 adapterCart;
    private TabLayout tabLayoutCart;
    Intent intent;
    private String lattti,lonnngi;
    private SwipeRefreshLayout swipeContainer;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        getSupportActionBar().setTitle("Ongoing Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try{
            lattti = intent.getStringExtra("lattti");
            lonnngi = intent.getStringExtra("lonnngi");
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        tabLayoutCart = (TabLayout) findViewById(R.id.tabLayoutCart);
        tabLayoutCart.addTab(tabLayoutCart.newTab().setText("Breakdown Request"));
        tabLayoutCart.addTab(tabLayoutCart.newTab().setText("Transport Request"));
        tabLayoutCart.setTabMode(TabLayout.GRAVITY_CENTER);
        viewPagerCart = (ViewPager) findViewById(R.id.viewPagerCart);
        viewPagerCart.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutCart));
        adapterCart = new ViewPagerOrderAdapter3(getSupportFragmentManager(), tabLayoutCart.getTabCount(),
                OrderHistoryActivity3.this,lattti,lonnngi);
        viewPagerCart.setAdapter(adapterCart);
        tabLayoutCart.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerCart.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeMainActivity);
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!isNetworkAvailable()) {
                    swipeContainer.setRefreshing(false);
                    Toasty.error(OrderHistoryActivity3.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    swipeContainer.setRefreshing(false);

                    adapterCart = new ViewPagerOrderAdapter3(getSupportFragmentManager(), tabLayoutCart.getTabCount(),
                            OrderHistoryActivity3.this,lattti,lonnngi);
                    viewPagerCart.setAdapter(adapterCart);
                }
            }
        });
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        ImageView locButton = (ImageView) menu.findItem(R.id.action_refresh).getActionView();
        if (locButton != null) {
            locButton.setImageResource(R.drawable.ic_refresh);
            locButton.setPadding(8,8,8,8);
            // need some resize
            locButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation rotation = AnimationUtils.loadAnimation(OrderHistoryActivity3.this, R.anim.rotate);
                    view.startAnimation(rotation);
                    // create and use new data set
                    adapterCart = new ViewPagerOrderAdapter3(getSupportFragmentManager(), tabLayoutCart.getTabCount(),
                            OrderHistoryActivity3.this,lattti,lonnngi);
                    viewPagerCart.setAdapter(adapterCart);
                }
            });
        }
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

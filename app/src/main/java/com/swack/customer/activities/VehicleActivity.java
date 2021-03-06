package com.swack.customer.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swack.customer.R;
import com.swack.customer.adapter.RecyclerViewAdapter;
import com.swack.customer.adapter.ViewPagerOrderAdapter;
import com.swack.customer.data.APIUrl;
import com.swack.customer.model.MyVehicleList;
import com.swack.customer.model.ResponseResult;
import com.swack.customer.model.VehicleDetailsList;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleActivity extends BaseActivity {
    
    private final static String TAG = "VehicleActivity";
    private RecyclerView recyclerView_list;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout error_layout;
   // private Button btnRetry;
    private ProgressBar progressBar;
    private TextView textError;
    private LinearLayout errorLayout;
    private ArrayList<MyVehicleList> templistview;
    private String cus_id,cus_name;
    private FloatingActionButton fab;
    private RecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Vehicle");

        prefManager.connectDB();
        cus_id = prefManager.getString("cus_id");
        cus_name = prefManager.getString("cus_name");
        prefManager.closeDB();

        inti();
    }

    private void inti() {
        progressBar = (ProgressBar) findViewById(R.id.main_progress1);
        textError = (TextView) findViewById(R.id.textError);
//        vehicle_list.setVisibility(View.GONE);

       // errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        recyclerView_list = findViewById(R.id.vehicle_list);
        recyclerView_list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_list.setLayoutManager(linearLayoutManager);


        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(VehicleActivity.this,AddVehicleActivity.class),200);
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
                    Toasty.error(VehicleActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    loadVehicle();
                }
            }
        });

        loadVehicle();
    }

    private void loadVehicle() {
        swipeContainer.setRefreshing(false);
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCancelable(false);
        templistview = new ArrayList<>();


        callCategory().enqueue(new Callback<MyVehicleList>() {
            @Override
            public void onResponse(Call<MyVehicleList> call, Response<MyVehicleList> response) {
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        textError.setVisibility(View.GONE);

                        System.out.println("response vehical Activity Response :"+response.body().getResponse());

                        recyclerViewAdapter = new RecyclerViewAdapter(VehicleActivity.this,cus_id);
                        recyclerView_list.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.setListArray(response.body().getVehicleDetailsList());

                        templistview = response.body().getVehicleDetailsList();
                        System.out.println("response vehical Arraylist :"+templistview);

                        Log.d("order list: ", String.valueOf(templistview.size()));

                    } else {
                        progressDialog.dismiss();
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("Hi "+cus_name+" vehicals not available you can add click on below button");
                        System.out.println(TAG + " Else Close");
                        //Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                    textError.setVisibility(View.VISIBLE);
                    textError.setText("Hi "+cus_name+" vehicals not available you can add click on below button");
                }
            }

            @Override
            public void onFailure(Call<MyVehicleList> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
                textError.setVisibility(View.VISIBLE);
                textError.setText("Hi "+cus_name+" vehicals not available you can add click on below button");
            }
        });
    }

    //categories
    private Call<MyVehicleList> callCategory() {
        System.out.println("API KEY VEHICAL" + APIUrl.KEY + " : CUS ID " + cus_id);
        return apiService.callVehicleLists(
                APIUrl.KEY,
                cus_id
        );
    }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == Activity.RESULT_OK){
                loadVehicle();
            }
        }
    }

}

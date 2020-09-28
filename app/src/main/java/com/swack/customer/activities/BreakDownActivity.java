package com.swack.customer.activities;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.swack.customer.R;
import com.swack.customer.data.APIUrl;
import com.swack.customer.data.GPSTracker;
import com.swack.customer.data.LocationAddress;
import com.swack.customer.model.ResponseResult;
import com.swack.customer.model.ServiceTypeList;
import com.swack.customer.model.VehicleDetailsList;
import com.swack.customer.view.TextInputAutoCompleteTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class BreakDownActivity extends BaseActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private static final String TAG = "BreakDownActivity";
    private TextInputAutoCompleteTextView spinner_vehicle;
    private TextInputEditText edt_address;
    private Button btnSubmit;
    private ArrayList<VehicleDetailsList> vehicleLists;
    private ArrayList<ServiceTypeList> serviceLists;
    private ArrayList<String> vehicleName,serviceName;
    private String cus_id,service,vehicle,address;
    private Double lat,log;
    private GPSTracker gpsTracker;
    private AutoCompleteTextView spinner_service;
    //update location
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;   //10 secs
    private long FASTEST_INTERVAL = 1000;  //2 sec
    private LocationManager locationManager;
    private String state = "";
    public Geocoder geocoder;
    String latitude,longitude2;
    private ProgressDialog progressDialog;
    public String lati, longi,gar_id,gar_list_id,gar_name,gar_ids;
    public GPSTracker gps;
    public TextView result, latitudeLL, longitudeLL, textView_CustomerName, textView_CustomerEmail;
    public String latme,logme;
    public double latitude1,longitude;

    private List<Address> addressList;
    String lat12, lon12;
    String city, area;
    private RadioGroup mRadioGroup;
    private String position,fullAddress;
    Location location;
    Intent intent;
    private TextInputEditText edt_transport_address;
    private Button btn_add_vehical;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(BreakDownActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_down);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Breakdown Request");

        inti();
        onClickListener();
        getVehicleLists();
    }

    private void inti() {
       try{
           address = intent.getStringExtra("fullAddress");

       }catch (NullPointerException e){
           e.printStackTrace();
       }
        spinner_vehicle = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_vehicle);
        btn_add_vehical = (Button) findViewById(R.id.btn_add_vehical);
        edt_transport_address = (TextInputEditText) findViewById(R.id.edt_transport_address2);

        prefManager.connectDB();
        latme = prefManager.getString("latitude");
        logme = prefManager.getString("longitude");
        cus_id = prefManager.getString("cus_id");
        prefManager.closeDB();

        if (latme != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        Double.parseDouble(latme),
                        Double.parseDouble(logme),
                        1);
                if (addresses == null || addresses.size()  == 0) {
                    edt_transport_address.setHint("Enter Address Manually");
                } else {
                    Address address = addresses.get(0);
                    edt_transport_address.setText(address.getAddressLine(0));
                }
            } catch (Exception ioException) {
                edt_transport_address.setHint("Enter Address Manually");
            }


        }else {
            edt_transport_address.setHint("Enter Address Manually");
        }


        intent = getIntent();
        mRadioGroup = findViewById(R.id.radio);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                // Toast.makeText(MainActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                position = String.valueOf(radioButton.getText());
                if (position.equals("On Workshop")){
                   // Toast.makeText(BreakDownActivity.this, "1", Toast.LENGTH_SHORT).show();
                    position = "1";
                }
                else{
                    //Toast.makeText(BreakDownActivity.this, "2", Toast.LENGTH_SHORT).show();
                    position = "2";
                }
                System.out.println("position"+position);
            }
        });


        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btn_add_vehical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BreakDownActivity.this,AddVehicleActivity.class));
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(BreakDownActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(BreakDownActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(BreakDownActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(BreakDownActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void onClickListener() {
       /* spinner_service.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                service = serviceLists.get(position).getService_typ_id();
            }
        });*/

      /*  spinner_service.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < serviceLists.size(); i++) {
                    if (spinner_service.getText().toString().equals(serviceLists.get(i).getService_typ_name())) {
                        service = serviceLists.get(position).getService_typ_id();
                        //edit_state.setText("");
                        //getState(country);
                        //spinner_vehicle.setText("");
                        //state = "";
                        //taluka = ""
                        System.out.println("Service Id " + service);
                        //Toast.makeText(gpsTracker, "Vehical Id "+vehicle, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });*/
         /* spinner_vehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vehicle = vehicleLists.get(position).getVehicled_id();
            }
        });*/


        spinner_vehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < vehicleLists.size(); i++) {
                    if (spinner_vehicle.getText().toString().equals(vehicleLists.get(i).getVehicle_name())) {
                        vehicle = vehicleLists.get(position).getVehicled_id();
                        //edit_state.setText("");
                        //getState(country);
                        //spinner_vehicle.setText("");
                         //state = "";
                         //taluka = ""
                         System.out.println("Vehical Id " + vehicle);
                         //Toast.makeText(gpsTracker, "Vehical Id "+vehicle, Toast.LENGTH_SHORT).show();
                         return;
                     }
                 }
            }
        }) ;
        
        btnSubmit.setOnClickListener(this);
    }

    private void getVehicleLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        vehicleLists = new ArrayList<>();
        System.out.println("VEHICAL KEY : "+APIUrl.KEY+" CUST ID : "+cus_id);

        apiService.callVehicleListsBreak(APIUrl.KEY,cus_id).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    vehicleLists = response.body().getVehicleDetailsList();
                    vehicleSpinner(vehicleLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    vehicleSpinner(vehicleLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    vehicleSpinner(vehicleLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    vehicleSpinner(vehicleLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    vehicleSpinner(vehicleLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    vehicleSpinner(vehicleLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    vehicleSpinner(vehicleLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                vehicleSpinner(vehicleLists);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //spinner for country
    private void vehicleSpinner(ArrayList<VehicleDetailsList> vehicleLists) {
        vehicleName = new ArrayList<>();
        for (VehicleDetailsList data : vehicleLists) {
            vehicleName.add(data.getVehicle_name());
            if (data.getVehicled_id().equals(vehicleName)) {
                spinner_vehicle.setText(data.getVehicle_name());
                //getState(country);
                Toast.makeText(this, "#vehical Id "+vehicleName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, vehicleName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_vehicle.setThreshold(1);
        // attaching data adapter to spinner
        spinner_vehicle.setAdapter(dataAdapter);
    }
    

  /*  private void getServiceLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        serviceLists = new ArrayList<>();
        System.out.println("VEHICAL KEY : "+APIUrl.KEY+" CUST ID : "+cus_id);

        apiService.callServiceLists(APIUrl.KEY).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    serviceLists = response.body().getServiceTypeList();
                    serviceSpinner(serviceLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    serviceSpinner(serviceLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    serviceSpinner(serviceLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    serviceSpinner(serviceLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    serviceSpinner(serviceLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    serviceSpinner(serviceLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    serviceSpinner(serviceLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                serviceSpinner(serviceLists);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
*/
   /* private void serviceSpinner(ArrayList<ServiceTypeList> serviceLists) {
        serviceName = new ArrayList<>();
        for (ServiceTypeList data: serviceLists){
            serviceName.add(data.getService_typ_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, serviceName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_service.setThreshold(1);
        // attaching data adapter to spinner
        spinner_service.setAdapter(dataAdapter);
    }*/



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit){
            address = edt_transport_address.getText().toString();
            if(!isNetworkAvailable()){
                Toasty.error(this, "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(vehicle)){
                Toasty.error(BreakDownActivity.this,"Enter Vehicle Name",Toasty.LENGTH_SHORT).show();
                return;
            } if(TextUtils.isEmpty(position)){
                Toasty.error(BreakDownActivity.this,"Select Service Name",Toasty.LENGTH_SHORT).show();
                return;
            } if(TextUtils.isEmpty(address)){
                Toasty.error(BreakDownActivity.this,"Enter Address",Toasty.LENGTH_SHORT).show();
                return;
            }
            breakDown(vehicle, position, address);
        }
    }


//location

    public void location() {
//        swipeContainer.setRefreshing(false);
        //swipeContainer.setRefreshing(false);
        //for get current employee address
        if (ContextCompat.checkSelfPermission(BreakDownActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED

                && ContextCompat.checkSelfPermission(BreakDownActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)

                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BreakDownActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        } else {
            gps = new GPSTracker(BreakDownActivity.this,BreakDownActivity.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude1 = gps.getLatitude();
                longitude = gps.getLongitude();
               /* lat = String.valueOf(gps.getLatitude());
                log = String.valueOf(gps.getLongitude());


                // \n is for new line
                lat = Double.toString(latitude);
                log = Double.toString(longitude);*/

                address();

            } else {
                if (area == null && city == null) {
                    //  Toast.makeText(getApplicationContext(),"null value solved..",Toast.LENGTH_LONG).show();
                    if (gps.canGetLocation()) {

                        latitude1 = gps.getLatitude();
                        longitude = gps.getLongitude();

                        // \n is for new line
                        //lat = Double.toString(latitude);
                        //log = Double.toString(longitude);
                        System.out.println("lat : "+lat+" log : "+log);

                        latitudeLL.setText(lati);
                        longitudeLL.setText(longi);

                        address();

                    } else {
                        gps.showSettingsAlert();
                    }
                }
            }
        }
    }

    public void address() {

        try {

            addressList = geocoder.getFromLocation(latitude1, longitude, 1);

            city = addressList.get(0).getSubAdminArea();
            area = addressList.get(0).getSubLocality();
            //pincode = addressList.get(0).getPostalCode();

            fullAddress = area + ", " + city + "";


            System.out.println("Location " + fullAddress);
            //System.out.println("Location " + templistviewNew.isEmpty());


        } catch (NullPointerException e) {
            //Toast.makeText(this, "IOException Null", Toast.LENGTH_SHORT).show();
            location();

            e.printStackTrace();

        } catch (IndexOutOfBoundsException e) {
            // Toast.makeText(this, "IndexOutOfBoundsException Null", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            if (result.equals("Locating..")) {
                location();
            }
        } catch (IOException e) {
            location();
            e.printStackTrace();
        }
    }

    public boolean checkPermission() {

        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result10 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result11 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result1 == PackageManager.PERMISSION_GRANTED
                && result5 == PackageManager.PERMISSION_GRANTED
                && result10 == PackageManager.PERMISSION_GRANTED
                && result11 == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(BreakDownActivity.this, new
                String[]{CALL_PHONE, READ_SMS,
                WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            //Toast.makeText(this, "Location is detedcting..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        System.out.println("#Updated Location in each second"+msg);
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //Toast.makeText(getApplicationContext(), ""+latLng, Toast.LENGTH_SHORT).show();
        LatLng latlang = latLng;

        lat = location.getLatitude();
        log = location.getLongitude();


    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(BreakDownActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        requestPermission();
                        checkPermission();
                        location();
                    }
                }else{
                    //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
//

    private void breakDown(String vehicle, String position, String address) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        System.out.println("Breakdown KEY "+APIUrl.KEY+" vehical "+vehicle+" position "+position+" address "+address+" lat "+latme+
                " long "+logme);
        apiService.callBreakDown(APIUrl.KEY,vehicle,position,address,cus_id,latme,logme).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                System.out.println(TAG+" response : " + response.body().getResponse());
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    Toasty.success(BreakDownActivity.this,"Successfully added ",Toasty.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toasty.error(BreakDownActivity.this,"Sever Error, Try after sometime ",Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                errorOut(t);
            }
        });
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

}

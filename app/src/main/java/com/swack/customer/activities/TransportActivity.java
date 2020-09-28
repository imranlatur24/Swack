package com.swack.customer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.swack.customer.R;
import com.swack.customer.data.APIUrl;
import com.swack.customer.model.DistrictList;
import com.swack.customer.model.LoadList;
import com.swack.customer.model.ProductList;
import com.swack.customer.model.ResponseResult;
import com.swack.customer.model.TalukaList;
import com.swack.customer.model.VehicleDetailsList;
import com.swack.customer.model.VillageList;
import com.swack.customer.view.TextInputAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "TransportActivity";
    private AutoCompleteTextView spinner_to_village,spinner_from_village;
    private TextInputAutoCompleteTextView spinner_to,spinner_product_unit,spinner_from,spinner_from_taluka,spinner_product_name,spinner_to_taluka;
    private TextInputEditText edt_price,edt_transport_address,edt_area,edt_product;
    private ArrayList<DistrictList> districtLists;
    private ArrayList<TalukaList> talukaLists;
    private ArrayList<VillageList> villageLists;
    private ArrayList<LoadList> loadLists;
    private ArrayList<ProductList> productLists;
    private ArrayList<String> districtName,talukaName,villageName,loadName,productName;
    private Button btnSubmitTransport;
    private String cus_id,from,to,from_taluka,to_taluka,
            load,product,address,price,area,myproduct;
    private boolean from_taluka_boolean, to_taluka_boolean, from_village_boolean , to_village_boolean;
    private String longitude,latitude;
    private String lattti,lonnngi;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transport Request");

        inti();
        onClickListener();
        getDistrictLists();
        getLoadLists();
      //  getProductLists();
    }

    private void inti() {

        spinner_to = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_to);
        spinner_from = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_from);
        spinner_from_taluka = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_from_taluka);
        spinner_to_taluka = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_to_taluka);
        spinner_from_village = (AutoCompleteTextView) findViewById(R.id.spinner_from_village);
        spinner_to_village = (AutoCompleteTextView) findViewById(R.id.spinner_to_village);
       // spinner_product_name = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_product_name);
        spinner_product_unit = (TextInputAutoCompleteTextView) findViewById(R.id.spinner_product_unit);
        edt_price = (TextInputEditText) findViewById(R.id.edt_price);
        edt_transport_address = (TextInputEditText) findViewById(R.id.edt_transport_address);
        edt_area = (TextInputEditText) findViewById(R.id.edt_area);
        edt_product = (TextInputEditText) findViewById(R.id.edt_product);
        btnSubmitTransport = (Button) findViewById(R.id.btnSubmitTransport);

        prefManager.connectDB();
        latitude = prefManager.getString("latitude");
        longitude = prefManager.getString("longitude");
        cus_id = prefManager.getString("cus_id");
        prefManager.closeDB();
        if (latitude != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        Double.parseDouble(latitude),
                        Double.parseDouble(longitude),
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
    }
    private void onClickListener() {
        btnSubmitTransport.setOnClickListener(this);
        spinner_product_unit.setOnClickListener(this);
        spinner_from.setOnClickListener(this);
        spinner_to.setOnClickListener(this);
        spinner_from_taluka.setOnClickListener(this);
        spinner_from_village.setOnClickListener(this);
        spinner_from_village.setOnClickListener(this);
       /* spinner_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                from = districtLists.get(position).getD_ID();
                from_taluka_boolean = true;
                getTalukaLists(from);
            }
        });*/

        spinner_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < districtLists.size(); i++) {
                    if (spinner_from.getText().toString().equals(districtLists.get(i).getDistrict_Name())) {
                        from = districtLists.get(i).getD_ID();
                        from_taluka_boolean = true;
                        getTalukaLists(from);
                        System.out.println("from Id " + from);
                      //  Toast.makeText(TransportActivity.this, "District From Id "+from, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
        spinner_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < districtLists.size(); i++) {
                    if (spinner_to.getText().toString().equals(districtLists.get(i).getDistrict_Name())) {
                        to = districtLists.get(i).getD_ID();
                        from_taluka_boolean = false;
                        getTalukaLists(to);
                        System.out.println("To Taluka " + to);
                     //   Toast.makeText(TransportActivity.this, "To Taluka Id "+to, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
        spinner_from_taluka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < talukaLists.size(); i++) {
                    if (spinner_from_taluka.getText().toString().equals(talukaLists.get(i).getTaluka_name())) {
                        from_taluka = talukaLists.get(i).getT_ID();
                        from_taluka_boolean = false;
                        // getVillageLists(to_taluka);
                        System.out.println("from_taluka Id " + from_taluka);
                      //  Toast.makeText(TransportActivity.this, "from_taluka Id "+from_taluka, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
        spinner_to_taluka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < talukaLists.size(); i++) {
                    if (spinner_to_taluka.getText().toString().equals(talukaLists.get(i).getTaluka_name())) {
                        to_taluka = talukaLists.get(i).getT_ID();
                        from_taluka_boolean = false;
                       // getVillageLists(to_taluka);
                        System.out.println("to_taluka Id " + to_taluka);
                     //   Toast.makeText(TransportActivity.this, "To Taluka Id "+to_taluka, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
        spinner_from_village.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //from_village = villageLists.get(position).getV_ID();
            }
        });
        spinner_to_village.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //to_village = villageLists.get(position).getV_ID();
            }
        });
        /*spinner_product_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                product = productLists.get(position).getTraPro();
            }
        });*/

      /*  spinner_product_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < productLists.size(); i++) {
                    if (spinner_product_name.getText().toString().equals(productLists.get(i).getTraProductName())) {
                        product = productLists.get(i).getTraPro();
                        //edit_state.setText("");
                        //getState(country);
                        //spinner_vehicle.setText("");
                        //state = "";
                        //taluka = ""
                        System.out.println("product Id " + product);
                        //Toast.makeText(gpsTracker, "Vehical Id "+vehicle, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });*/


        /*spinner_product_unit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                load = loadLists.get(position).getLoadrang_id();
            }
        });*/
        spinner_product_unit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < loadLists.size(); i++) {
                    if (spinner_product_unit.getText().toString().equals(loadLists.get(i).getLoadrang_name())) {
                        load = loadLists.get(i).getLoadrang_id();
                        //edit_state.setText("");
                        //getState(country);
                        //spinner_vehicle.setText("");
                        //state = "";
                        //taluka = ""
                        System.out.println("load Id " + load);
                        //Toast.makeText(gpsTracker, "Vehical Id "+vehicle, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });


    }

    private void getDistrictLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        districtLists = new ArrayList<>();

        apiService.callDistrictLists(APIUrl.KEY).enqueue(new Callback<DistrictList>() {
            @Override
            public void onResponse(Call<DistrictList> call, Response<DistrictList> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response District "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    districtLists = response.body().getDistrictList();
                    districtAutoCompleteTextView(districtLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    districtAutoCompleteTextView(districtLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    districtAutoCompleteTextView(districtLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    districtAutoCompleteTextView(districtLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    districtAutoCompleteTextView(districtLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    districtAutoCompleteTextView(districtLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    districtAutoCompleteTextView(districtLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<DistrictList> call, Throwable t) {
                progressDialog.dismiss();
                districtAutoCompleteTextView(districtLists);
                Toasty.error(getApplicationContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
            }
        });
    }

/*    private void districtAutoCompleteTextView(ArrayList<DistrictList> districtLists) {
        districtName = new ArrayList<>();
        for (DistrictList data: districtLists){
            districtName.add(data.getDistrict_Name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, districtName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_to.setThreshold(1);
        spinner_to.setAdapter(dataAdapter);
        spinner_from.setThreshold(1);
        spinner_from.setAdapter(dataAdapter);
    }*/

    private void districtAutoCompleteTextView(ArrayList<DistrictList> vehicleLists) {
        districtName = new ArrayList<>();
        for (DistrictList data : vehicleLists) {
            districtName.add(data.getDistrict_Name());
            if (data.getD_ID().equals(districtName)) {
                spinner_from.setText(data.getDistrict_Name());
                //getState(country);
                Toast.makeText(this, "#from Id "+districtName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, districtName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
       /* spinner_from.setThreshold(1);
        // attaching data adapter to spinner
        spinner_from.setAdapter(dataAdapter);*/
        spinner_to.setThreshold(1);
        spinner_to.setAdapter(dataAdapter);
        spinner_from.setThreshold(1);
        spinner_from.setAdapter(dataAdapter);
    }

    private void getTalukaLists(String taluka) {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        talukaLists = new ArrayList<>();
        System.out.println(TAG+" Response taluka"+ taluka);
        apiService.callTalukaLists(APIUrl.KEY,taluka).enqueue(new Callback<TalukaList>() {
            @Override
            public void onResponse(Call<TalukaList> call, Response<TalukaList> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response taluka"+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    talukaLists = response.body().getTalukaList();
                    talukaAutoCompleteTextView(talukaLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    talukaAutoCompleteTextView(talukaLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    talukaAutoCompleteTextView(talukaLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    talukaAutoCompleteTextView(talukaLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    talukaAutoCompleteTextView(talukaLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    talukaAutoCompleteTextView(talukaLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    talukaAutoCompleteTextView(talukaLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<TalukaList> call, Throwable t) {
                progressDialog.dismiss();
                talukaAutoCompleteTextView(talukaLists);
                Toasty.error(getApplicationContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
            }
        });
    }

    private void talukaAutoCompleteTextView(ArrayList<TalukaList> talukaLists) {
        talukaName = new ArrayList<>();
        for (TalukaList data : talukaLists) {
            talukaName.add(data.getTaluka_name());
            if (data.getT_ID().equals(talukaName)) {
                spinner_to_taluka.setText(data.getTaluka_name());
                //getState(country);
                Toast.makeText(this, "#talukaName Id "+talukaName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, talukaName);
        // Drop down layout style - list view with radio button
        if(from_taluka_boolean) {
            spinner_from_taluka.setThreshold(1);
            spinner_from_taluka.setAdapter(dataAdapter);
        }else {
            spinner_to_taluka.setThreshold(1);
            spinner_to_taluka.setAdapter(dataAdapter);
        }
    }


 /*   private void getVillageLists(String taluka) {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.show();

        villageLists = new ArrayList<>();
        System.out.println(TAG+" Response village"+ taluka);
        apiService.callVillageLists(APIUrl.KEY).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response village"+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    villageLists = response.body().getVillageList();
                    villageAutoCompleteTextView(villageLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    villageAutoCompleteTextView(villageLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    villageAutoCompleteTextView(villageLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    villageAutoCompleteTextView(villageLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    villageAutoCompleteTextView(villageLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    villageAutoCompleteTextView(villageLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    villageAutoCompleteTextView(villageLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                villageAutoCompleteTextView(villageLists);
                Toasty.error(getApplicationContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
            }
        });
    }

   *//* private void villageAutoCompleteTextView(ArrayList<VillageList> villageLists) {
        villageName = new ArrayList<>();
        for (VillageList data: villageLists){
            villageName.add(data.getVillage_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, villageName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        if(from_village_boolean) {
            spinner_from_village.setThreshold(1);
            spinner_from_village.setAdapter(dataAdapter);
        }else {
            spinner_to_village.setThreshold(1);
            spinner_to_village.setAdapter(dataAdapter);
        }
    }*/
   private void villageAutoCompleteTextView(ArrayList<VillageList> villageLists) {
       villageName = new ArrayList<>();
       for (VillageList data: villageLists){
           villageName.add(data.getVillage_name());
       }
       ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, villageName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        // attaching data adapter to spinner
        if(from_village_boolean) {
            spinner_from_village.setThreshold(1);
            spinner_from_village.setAdapter(dataAdapter);
        }else {
            spinner_to_village.setThreshold(1);
            spinner_to_village.setAdapter(dataAdapter);
        }
    }

    private void getLoadLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadLists = new ArrayList<>();

        apiService.callLoadLists(APIUrl.KEY).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    loadLists = response.body().getLoadList();
                    loadAutoCompleteTextView(loadLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    loadAutoCompleteTextView(loadLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    loadAutoCompleteTextView(loadLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                loadAutoCompleteTextView(loadLists);
                Toasty.error(getApplicationContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
            }
        });
    }

   /* private void loadAutoCompleteTextView(ArrayList<LoadList> loadLists) {
        loadName = new ArrayList<>();
        for (LoadList data: loadLists){
            loadName.add(data.getLoadrang_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, loadName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_product_unit.setThreshold(1);
        spinner_product_unit.setAdapter(dataAdapter);
    }*/

    private void loadAutoCompleteTextView(ArrayList<LoadList> loadLists) {
        loadName = new ArrayList<>();
        for (LoadList data : loadLists) {
            loadName.add(data.getLoadrang_name());
            if (data.getLoadrang_id().equals(loadName)) {
                spinner_product_unit.setText(data.getLoadrang_name());
                //getState(country);
                Toast.makeText(this, "#load Id "+loadName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, loadName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_product_unit.setThreshold(1);
        // attaching data adapter to spinner
        spinner_product_unit.setAdapter(dataAdapter);
    }


   /* private void getProductLists() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("producting...");
        progressDialog.show();

        productLists = new ArrayList<>();

        apiService.callProductLists(APIUrl.KEY).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG+" Response "+ response.body().getResponse());
                if(Integer.parseInt(response.body().getResponse()) == 101) {
                    productLists = response.body().getTraProductList();
                    productAutoCompleteTextView(productLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 102){
                    productAutoCompleteTextView(productLists);
                }else if(Integer.parseInt(response.body().getResponse()) == 103){
                    productAutoCompleteTextView(productLists);
                    System.out.println(TAG+ " Required Parameter Missing");
                }else if(Integer.parseInt(response.body().getResponse()) == 104){
                    productAutoCompleteTextView(productLists);
                    System.out.println(TAG+ " Invalid Key");
                }else if(Integer.parseInt(response.body().getResponse()) == 105){
                    productAutoCompleteTextView(productLists);
                    System.out.println(TAG+ " Login failed");
                }else if(Integer.parseInt(response.body().getResponse()) == 106){
                    productAutoCompleteTextView(productLists);
                    System.out.println(TAG+ " Page Not Found");
                }else {
                    productAutoCompleteTextView(productLists);
                    System.out.println(TAG+ " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                productAutoCompleteTextView(productLists);
                Toasty.error(getApplicationContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
            }
        });
    }*/

   /* private void productAutoCompleteTextView(ArrayList<ProductList> productLists) {
        productName = new ArrayList<>();
        for (ProductList data: productLists){
            productName.add(data.getTraProductName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_custom_text, productName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_product_name.setThreshold(1);
        // attaching data adapter to spinner
        spinner_product_name.setAdapter(dataAdapter);
    }
*/

   /* private void productAutoCompleteTextView(ArrayList<ProductList> productLists) {
        productName = new ArrayList<>();
        for (ProductList data : productLists) {
            productName.add(data.getTraProductName());
            if (data.getTraPro().equals(productName)) {
                spinner_product_name.setText(data.getTraProductName());
                //getState(country);
                Toast.makeText(this, "#productName Id "+productName, Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, productName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        spinner_product_name.setThreshold(1);
        // attaching data adapter to spinner
        spinner_product_name.setAdapter(dataAdapter);
    }*/

    private void transportRequest(String from, String to, String load, String myproduct, String address, String price,
                                  String cus_id,String from_taluka, String to_taluka, String area, String lat, String log) {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("producting...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        System.out.println(TAG +"Tranport Latitude "+ lat+" Longitude "+log);
        System.out.println("##from : "+from);
        System.out.println("##from_taluka : "+from_taluka);
        System.out.println("##to : "+to);
        System.out.println("##load : "+load);
        System.out.println("##product : "+myproduct);
        System.out.println("##price : "+price);
        System.out.println("##address : "+address);
        System.out.println("##to_taluka : "+to_taluka);
        System.out.println("##area : "+area);
        System.out.println("##lat : "+lat);
        System.out.println("##log : "+log);
        apiService.callTransportRequest(APIUrl.KEY,
                from,
                to,
                load,
                myproduct,
                price,
                address,
                cus_id,
                from_taluka,
                to_taluka,
                area,latitude,longitude).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                System.out.println(TAG+" response : " + response.body().getResponse());
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    Toasty.success(TransportActivity.this,"Transport request sent successfully",Toasty.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toasty.error(TransportActivity.this,"Sever Error, Try after sometime ",Toasty.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmitTransport){
            address = edt_transport_address.getText().toString().trim();
            price = edt_price.getText().toString().trim();
            area = edt_area.getText().toString().trim();
            myproduct = edt_product.getText().toString().trim();
            if(!isNetworkAvailable()){
                Toasty.error(this, "No internet connection", Toasty.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(from)){
                Toasty.error(this, "Select From District", Toasty.LENGTH_SHORT).show();
                return;
            }if(TextUtils.isEmpty(from_taluka)){
                Toasty.error(this, "Select From Taluka", Toasty.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(to)){
                Toasty.error(this, "Select To District", Toasty.LENGTH_SHORT).show();
                return;
            }if(TextUtils.isEmpty(to_taluka)){
                Toasty.error(this, "Select To Taluka", Toasty.LENGTH_SHORT).show();
                return;
            } if(TextUtils.isEmpty(load)){
                Toasty.error(this, "Select Load", Toasty.LENGTH_SHORT).show();
                return;
            }if(TextUtils.isEmpty(myproduct)){
                Toasty.error(this, "Select Product", Toasty.LENGTH_SHORT).show();
                return;
            }if(TextUtils.isEmpty(address)){
                Toasty.error(this, "Enter Address", Toasty.LENGTH_SHORT).show();
                return;
            }if(TextUtils.isEmpty(area)){
                Toasty.error(this, "Enter Area", Toasty.LENGTH_SHORT).show();
                return;
            }if(TextUtils.isEmpty(price)){
                Toasty.error(this, "Enter Price per Km", Toasty.LENGTH_SHORT).show();
                return;
            }
            System.out.println("from_taluka : "+from_taluka);
            transportRequest(from,to,load,myproduct,address,price,cus_id,from_taluka,to_taluka,area,latitude,longitude);
        }
    }
    
}

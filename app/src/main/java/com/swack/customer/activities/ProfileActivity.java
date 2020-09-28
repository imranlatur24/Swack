package com.swack.customer.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;
import com.swack.customer.BuildConfig;
import com.swack.customer.R;
import com.swack.customer.data.APIUrl;
import com.swack.customer.data.FileUtil;
import com.swack.customer.model.ResponseResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ProfileActivity";
    private Toolbar toolbar;
    private static final int REQUEST_CAMERA = 101;
    private static final int SELECT_FILE = 102;
    private String cus_address,cus_name,cus_email,cus_mob,cus_id,cus_pic,cus_aadhar,cus_pan;
    private boolean profile,aadhar,pan,logo;
    private ImageView imageView, add_aadhar, add_pan,add_image,profile_aadhar,profile_pan;
    private EditText profile_name,profile_email,profile_mobile,profile_address;
    private File destination,newFile,profile_file = null,aadhar_file = null,pan_file = null;
    private CircleImageView profile_image;
    private Button fabProfileEdit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        init();
        onClickListener();
    }

    private void init()
    {
        prefManager.connectDB();
        cus_id = prefManager.getString("cus_id");
        cus_name = prefManager.getString("cus_name");
        cus_address = prefManager.getString("cus_address");
        cus_email = prefManager.getString("cus_email");
        cus_mob = prefManager.getString("cus_mob");
        cus_pic = prefManager.getString("cus_pic");
        cus_aadhar = prefManager.getString("cus_aadhar");
        cus_pan = prefManager.getString("cus_pan");
        prefManager.closeDB();
        System.out.println("PROFILE CUS PIC PATH"+cus_pic);
        System.out.println("PROFILE ADHAAR PIC PATH"+cus_aadhar);
        System.out.println("PROFILE PAN PIC PATH"+cus_pan);


        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        profile_mobile = findViewById(R.id.profile_mobile);
        profile_address = findViewById(R.id.profile_address);
        imageView = findViewById(R.id.imageView);
        add_aadhar = findViewById(R.id.add_aadhar);
        add_pan = findViewById(R.id.add_pan);
        profile_image = findViewById(R.id.profile_image);
        profile_aadhar = findViewById(R.id.profile_aadhar);
        profile_pan = findViewById(R.id.profile_pan);
        fabProfileEdit = findViewById(R.id.fabProfileEdit);

        profile_name.setText(cus_name);
        profile_email.setText(cus_email);
        profile_address.setText(cus_address);
        profile_mobile.setText(cus_mob);

        if(!TextUtils.isEmpty(cus_pic)) {
            Picasso.with(ProfileActivity.this)
                    .load(cus_pic)
                    .placeholder(R.drawable.pic)
                    .error(R.drawable.pic)
                    .into(profile_image);
        }
        if(!TextUtils.isEmpty(cus_aadhar)) {
            Picasso.with(ProfileActivity.this)
                    .load(cus_aadhar)
                    .placeholder(R.drawable.adhar)
                    .error(R.drawable.adhar)
                    .into(profile_aadhar);
        }
        if(!TextUtils.isEmpty(cus_pan)) {
            Picasso.with(ProfileActivity.this)
                    .load(cus_pan)
                    .placeholder(R.drawable.pan)
                    .error(R.drawable.pan)
                    .into(profile_pan);
        }
    }

    private void onClickListener() {
        profile_image.setOnClickListener(this);
        profile_pan.setOnClickListener(this);
        profile_aadhar.setOnClickListener(this);
        fabProfileEdit.setOnClickListener(this);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            }
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    destination = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);

                }
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
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
        switch (v.getId()){
            case R.id.fabProfileEdit:
                cus_name = profile_name.getText().toString();
                cus_address = profile_address.getText().toString();
                cus_email = profile_email.getText().toString();
                cus_mob = profile_mobile.getText().toString();
                MultipartBody.Part cusprofilepic = null;
                MultipartBody.Part cusdoc_aadhar = null;
                MultipartBody.Part cusdoc_pan = null;
                MultipartBody.Part cusshopact = null;
                if (!isNetworkAvailable()) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
                    return;
                }
                try{
                    profile_file.isFile();
                    RequestBody requestFileProfile =RequestBody.create(MediaType.parse("*/*"), profile_file);
                    cusprofilepic = MultipartBody.Part.createFormData("cus_profile", profile_file.getName(), requestFileProfile);
                }catch (NullPointerException e){
                    // cus_pic = cus_pic.replace("http://swack.in/swack/ProfileCus/","");
                    cus_pic = cus_pic.replace("http://swack.in/swack/UploadDocs/","");
                }
                if(TextUtils.isEmpty(cus_name)){
                    profile_name.setError("Enter full name");
                    return;
                }
                if(TextUtils.isEmpty(cus_email)){
                profile_name.setError("Enter email");
                return;
                }
                if(TextUtils.isEmpty(cus_mob)){
                profile_name.setError("Enter Mobile number");
                return;
                }
                if(TextUtils.isEmpty(cus_address)){
                    profile_name.setError("Enter full address");
                    return;
                }
                try{
                    aadhar_file.isFile();
                    RequestBody requestFileAadhar =RequestBody.create(MediaType.parse("*/*"), aadhar_file);
                    cusdoc_aadhar = MultipartBody.Part.createFormData("doc_aadhar", aadhar_file.getName(), requestFileAadhar);
                }catch (NullPointerException e){
                    //cus_aadhar = cus_aadhar.replace("http://swack.in/swack/CusAadharPhoto/","");
                    cus_aadhar = cus_aadhar.replace("http://swack.in/swack/UploadDocs/","");
                }
                try{
                    pan_file.isFile();
                    RequestBody requestFilePan =RequestBody.create(MediaType.parse("*/*"), pan_file);
                    cusdoc_pan = MultipartBody.Part.createFormData("doc_pan", pan_file.getName(), requestFilePan);
                }catch (NullPointerException e){
                    //cus_pan = cus_pan.replace("http://swack.in/swack/CusPanPhoto/","");
                    cus_pan = cus_pan.replace("http://swack.in/swack/UploadDocs/","");
                }
                updateProfile(RequestBody.create(MediaType.parse("text/plain"), APIUrl.KEY),
                        RequestBody.create(MediaType.parse("text/plain"),cus_id),
                        RequestBody.create(MediaType.parse("text/plain"),cus_name),
                        RequestBody.create(MediaType.parse("text/plain"),cus_email),
                        RequestBody.create(MediaType.parse("text/plain"),cus_mob),
                        RequestBody.create(MediaType.parse("text/plain"),cus_address),
                        RequestBody.create(MediaType.parse("text/plain"),cus_pic),
                        RequestBody.create(MediaType.parse("text/plain"),cus_aadhar),
                        RequestBody.create(MediaType.parse("text/plain"),cus_pan),
                        cusprofilepic,cusdoc_aadhar,cusdoc_pan);


                break;
            case R.id.profile_aadhar:
                profile = false;
                aadhar = true;
                pan = false;
                selectImage();
                break;
            case R.id.profile_pan:
                profile = false;
                aadhar = false;
                pan = true;
                selectImage();
                break;
            case R.id.profile_image:
                profile = true;
                aadhar = false;
                pan = false;
                selectImage();
                break;
        }
    }

    private void updateProfile(RequestBody key, RequestBody cus_id, final RequestBody cus_name, RequestBody cus_email, RequestBody cus_mobi, RequestBody cus_address, RequestBody profilePhoto, RequestBody aadharPhoto, RequestBody panPhoto, MultipartBody.Part profile_file, MultipartBody.Part aadhar_file, MultipartBody.Part pan_file) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        System.out.println("PROFILE API KEY : "+key+" CUS ID "+cus_id+" cus name "+cus_name+" CUS EMAIL "+cus_email+
                " mob "+cus_mob+" address "+cus_address+" profile photo "+profilePhoto+" addhar photo "+aadharPhoto+
                " pan photo "+panPhoto+"profile path "+profile_file+" adhar path "+aadhar_file+" pan path "+pan_file);
        apiService.updateProfile(key,cus_id,cus_name,cus_email,cus_mobi,cus_address,profilePhoto,aadharPhoto,panPhoto,
                profile_file,aadhar_file,pan_file).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                Log.d(TAG, "response login : " + response.body().getResponse());
                if (Integer.parseInt(response.body().getResponse()) == 101) {

                    Toasty.success(getApplicationContext(),"Profile Updated", Toast.LENGTH_SHORT).show();
                    prefManager.connectDB();
                    prefManager.setBoolean("isLogin", true);
                    prefManager.setString("cus_id", response.body().getCusProfileList().get(0).getCus_id());
                    prefManager.setString("cus_name", response.body().getCusProfileList().get(0).getCus_name());
                    prefManager.setString("cus_address", response.body().getCusProfileList().get(0).getCus_address());
                    prefManager.setString("cus_email", response.body().getCusProfileList().get(0).getCus_email());
                    prefManager.setString("cus_mob", response.body().getCusProfileList().get(0).getCus_mob());
                    prefManager.setString("cus_pic", response.body().getCusProfileList().get(0).getCus_profile());
                    prefManager.setString("cus_aadhar", response.body().getCusProfileList().get(0).getDoc_aadhar());
                    prefManager.setString("cus_pan", response.body().getCusProfileList().get(0).getDoc_pan());
                    prefManager.closeDB();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                    progressDialog.dismiss();
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toast.LENGTH_LONG).show();
                }  else {
                    progressDialog.dismiss();
                    System.out.println(TAG + " Else Close");
                    Toasty.error(getApplicationContext(), "Profile updated failed", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == this.RESULT_CANCELED) {
                //Toasty.error(this, "Image pick cancel", Toasty.LENGTH_SHORT).show();
                return;
            } else {
                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == SELECT_FILE) {
                        try {
                            destination = FileUtil.from(ProfileActivity.this,data.getData());
                            newFile = new Compressor(this)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setQuality(50)
                                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                    .compressToFile(destination);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else {
                        try {
                            newFile = new Compressor(this)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setQuality(50)
                                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                    .compressToFile(destination);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(profile){
                        Picasso.with(ProfileActivity.this)
                                .load(destination)
                                .placeholder(R.drawable.pic)
                                .error(R.drawable.pic)
                                .into(profile_image);
                        profile_file = newFile;
                    }if(pan){
                        Picasso.with(ProfileActivity.this)
                                .load(destination)
                                .placeholder(R.drawable.pan)
                                .error(R.drawable.pan)
                                .into(profile_pan);
                        pan_file = newFile;
                    }if(aadhar){
                        Picasso.with(ProfileActivity.this)
                                .load(destination)
                                .placeholder(R.drawable.adhar)
                                .error(R.drawable.adhar)
                                .into(profile_aadhar);
                        aadhar_file = newFile;
                    }
            }
        }
    }
}

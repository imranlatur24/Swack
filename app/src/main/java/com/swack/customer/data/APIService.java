package com.swack.customer.data;


import com.swack.customer.model.CityListModel;
import com.swack.customer.model.DistrictList;
import com.swack.customer.model.DistrictModel;
import com.swack.customer.model.MyVehicleList;
import com.swack.customer.model.MyVehicleListDetails;
import com.swack.customer.model.ResponseResult;
import com.swack.customer.model.TalukaList;
import com.swack.customer.model.TalukaListModel;
import com.swack.customer.model.VehicleDetailsList;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService
{
    //The all State call
    @FormUrlEncoded
    @POST("MyVehicle.php")
    Call<MyVehicleListDetails> getVehical(
            @Field("key") String key,
            @Field("cus_id") String cus_id,
            @Field("vehicle_id") String vehicle_id
                                 );

    @FormUrlEncoded
    @POST("state.php")
    Call<DistrictModel> getState(@Field("key") String key);

    //The city call
    @FormUrlEncoded
    @POST("city.php")
    Call<CityListModel> getCity(@Field("key") String key,
                                @Field("state_id") String state_id);

    //The taluka call
    @FormUrlEncoded
    @POST("taluka.php")
    Call<TalukaListModel> getTaluka(@Field("key") String key,
                                    @Field("city_id") String city_id);

    //The OTP Varification Password call
    @FormUrlEncoded
    @POST("otp_verification.php")
    Call<ResponseResult> otpVerificationPassword(
            @Field("key") String key,
            @Field("cus_id") String id,
            @Field("cus_mob") String mobile,
            @Field("new_password") String password,
            @Field("OTP") String otp);


    //The forgot password call
    @FormUrlEncoded
    @POST("ForgotPass.php")
    Call<ResponseResult> forgotPassword(
            @Field("key") String key,
            @Field("cus_mob") String mobile_no);

 @FormUrlEncoded
    @POST("pendingbreack.php")
    Call<ResponseResult> pending(
            @Field("key") String key,
            @Field("cus_id") String mobile_no);

 @FormUrlEncoded
    @POST("breakdownprocess.php")
    Call<ResponseResult> breakdownprocess(
            @Field("key") String key,
            @Field("cus_id") String mobile_no,
            @Field("from_lat") String from_lat,
            @Field("from_long") String from_long
            );

 @FormUrlEncoded
    @POST("transprocess.php")
    Call<ResponseResult> transprocess(
            @Field("key") String key,
            @Field("trnreqcusid") String mobile_no,
            @Field("from_lat") String from_lat,
            @Field("from_long") String from_long);

 @FormUrlEncoded
    @POST("pendingtrans.php")
    Call<ResponseResult> pendingTrans(
            @Field("key") String key,
            @Field("trnreqcusid") String mobile_no
 );


    //The login call
    @FormUrlEncoded
    @POST("Login.php")
    Call<ResponseResult> callLoginApi(
            @Field("key") String key,
            @Field("fcmcode") String fcmcode,
            @Field("cus_mob") String gar_mobi,
            @Field("cus_password") String gar_password);


    @FormUrlEncoded
    @POST("confirmorder.php")
    Call<ResponseResult> confirmOrder(
            @Field("key") String key,
            @Field("cus_id") String cus_id,
            @Field("order_id") String ordrequniq_id,
            @Field("cus_id") String gar_id
    );

    //check version code
    @FormUrlEncoded
    @POST("showjobitem.php")
    Call<ResponseResult> showJobItemDetails(
            @Field("key") String key,
            @Field("job_ord_id") String job_ord_id
    );


 //check version code
    @FormUrlEncoded
    @POST("dashoOrd.php")
    Call<ResponseResult> dashboard(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("support.php")
    Call<ResponseResult> getSupport(
            @Field("key") String key);


    @FormUrlEncoded
    @POST("show_slider.php")
    Call<ResponseResult> callSlider(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("get_work_city_list")
    Call<ResponseResult> callCityList(
            @Field("key") String key,
            @Field("language_id") String language_id
    );

    @FormUrlEncoded
    @POST("get_category_wise_product")
    Call<ResponseResult> callProductWList(
            @Field("key") String key,
            @Field("category_id") String category_id,
            @Field("language_id") String language_id
    );

    @FormUrlEncoded
    @POST("get_language_list")
    Call<ResponseResult> getLanguageList(
            @Field("key") String key
    );

 @FormUrlEncoded
    @POST("order_list")
    Call<ResponseResult> getOrderList(
         @Field("key") String key,
         @Field("user_id") String user_id
 );


   /* @FormUrlEncoded
    @POST("update_profile")
    Call<UpdateProfile> updateProfile(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no,
            @Field("gcm_code") String gcm_code,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("country") String country,
            @Field("state") String state,
            @Field("city") String city,
            @Field("address") String address,
            @Field("pincode") String pincode,
            @Field("email_id") String email_id,
            @Field("dob") String dob,
            @Field("reg_id") String reg_id
    );
*/


    //category list
 @FormUrlEncoded
    @POST("category_list")
    Call<ResponseResult> callCategories(
         @Field("key") String key,
         @Field("language_id") String language_id);



 //sub category list
 @FormUrlEncoded
    @POST("get_product_categorywise")
    Call<ResponseResult> callProductList(
         @Field("key") String key,
         @Field("catergory_id") String catergory_id
 );

 //sub category list
 @FormUrlEncoded
    @POST("version_update.php")
    Call<ResponseResult> card_Versioncode(
         @Field("key") String key,
         @Field("version_code") String version_code
 );

   @FormUrlEncoded
   @POST("RegUser.php")
   Call<ResponseResult> callRegister(
           @Field("key") String key,
           @Field("cus_name") String gar_name,
           @Field("cus_email") String gar_email,
           @Field("cus_mob") String gar_mobi,
           @Field("cus_password") String gar_password,
           @Field("cus_address") String cus_address,
           @Field("cus_lat") String gar_lat,
           @Field("cus_long") String gar_long,
           @Field("cus_district") String city,
           @Field("cus_taluka") String taluka,
           @Field("cus_state") String state
   );

   //The filters_fragment call


    //The OTP verification for new filters_fragment call
    @FormUrlEncoded
    @POST("otp_varification")
    Call<ResponseResult> callOtpVerificationRegister(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no,
            @Field("password") String password,
            @Field("otp") String otp);

    //The OTP verification for new password call
    @FormUrlEncoded
    @POST("forgot_otp_varification")
    Call<ResponseResult> callOtpVerification(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no,
            @Field("password") String password,
            @Field("otp") String otp);

    //The OTP resend call
    @FormUrlEncoded
    @POST("resend_otp")
    Call<ResponseResult> callResendOTP(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no);

    //The forgot password call
    @FormUrlEncoded
    @POST("forgot_change_password")
    Call<ResponseResult> callForgotPassword(
            @Field("key") String key,
            @Field("reg_id") String reg_id,
            @Field("new_password") String new_password
    );

    //The change password call
    @FormUrlEncoded
    @POST("ChangePass.php")
    Call<ResponseResult> callChangePassword(
            @Field("key") String key,
            @Field("cus_id") String cus_id,
            @Field("cus_password") String cus_password);

    //
    //working city list
    @FormUrlEncoded
    @POST("get_work_city_list")
    Call<ResponseResult> callWorkingCityList(
            @Field("key") String key,
            @Field("language_id") String language_id);



    //The city locality call
    @FormUrlEncoded
    @POST("get_locality")
    Call<ResponseResult> callLocalityList(
            @Field("key") String key,
            @Field("work_city_id") String workspaceCityId,
            @Field("language_id") String language_id
    );

    //The category call
    @FormUrlEncoded
    @POST("get_category")
    Call<ResponseResult> callCategoryTab(
            @Field("key") String key);

    //The feedback call
    @FormUrlEncoded
    @POST("customer_feedback")
    Call<ResponseResult> callSendFeedback(
            @Field("key") String key,
            @Field("rating") String strRating,
            @Field("suggesstion") String strFeedBack,
            @Field("login_id") String id);


    //The workspace call
    @FormUrlEncoded
    @POST("get_workspace_list")
    Call<ResponseResult> callWorkspaceList(
            @Field("key") String key,
            @Field("work_city_id") String intCityId,
            @Field("locality_id") String intLocalityId,
            @Field("category_id") String intCategoryId,
            @Field("page") String pages);

    //The user booking history call
    @FormUrlEncoded
    @POST("get_booking_history_by_id")
    Call<ResponseResult> callBookingHistoryList(
            @Field("key") String key,
            @Field("login_id") String userId);

    //The user book now  call
    @FormUrlEncoded
    @POST("add_booking")
    Call<ResponseResult> callSubmitBooking(
            @Field("key") String key,
            @Field("work_space_id") String work_space_id,
            @Field("work_space_name") String work_space_name,
            @Field("customer_id") String customer_id,
            @Field("customer_name") String customer_name,
            @Field("customer_email_id") String customer_email_id,
            @Field("customer_mobile_no") String customer_mobile_no,
            @Field("desk_id") String desk_id,
            @Field("desk_qty") String desk_qty,
            @Field("book_start_time") String book_start_time,
            @Field("book_end_time") String book_end_time,
            @Field("book_hours") String book_hours,
            @Field("book_day") String book_day,
            @Field("book_month") String book_month,
            @Field("book_three_month") String book_three_month,
            @Field("book_six_month") String book_six_month,
            @Field("book_year") String book_year,
            @Field("booking_type") String booking_type,
            @Field("booking_date") String booking_date,
            @Field("total_amount") String total_amount,
            @Field("gst_percentage") String gst_percentage,
            @Field("gst_amount") String gst_amount,
            @Field("owner_email") String owner_email,
            @Field("owner_mobile_no") String owner_mobile_no,
            @Field("owner_name") String owner_name,
            @Field("final_total_amount") String final_total_amount,
            @Field("booking_status") String booking_status);

    //The all State call
  /*  @FormUrlEncoded
    @POST("get_state")
    Call<DistrictModel> getState(@Field("key") String key,
                                 @Field("country_id") String country_id);

    //The city call
    @FormUrlEncoded
    @POST("get_cities")
    Call<CityListModel> getCity(@Field("key") String key,
                                @Field("state_id") String state_id);

*/
    //The get booking by id call
    @FormUrlEncoded
    @POST("get_booking_by_id")
    Call<ResponseResult> callLastBookingHistory(
            @Field("key") String key,
            @Field("work_space_id") String work_space_id,
            @Field("current_date") String current_date);

    @FormUrlEncoded
    @POST("add_order")
    Call<ResponseResult>callOrderPlace(
            @Field("key") String key,
            @Field("user_id") String user_id,
            @Field("total_qty") String total_qty,
            @Field("final_total_amount") String final_total_amount,
            @Field("delivery_address") String delivery_address,
            @Field("product_list") String product_list
    );

    //The Submit Order call
    @Multipart
    @POST("submitOrder.php")
    Call<ResponseResult> callSubmitBill(
            @Part("key") RequestBody key,
            @Part("cusage_id") RequestBody garage_id,
            @Part("cus_id") RequestBody customer_id,
            @Part("ordreq_id") RequestBody order_id,
            @Part MultipartBody.Part file);

    //The update profile call
    @Multipart
    @POST("cusProfile.php")
    Call<ResponseResult> updateProfile(
            @Part("key") RequestBody key,
            @Part("cus_id") RequestBody gar_id,
            @Part("cus_name") RequestBody gar_name,
            @Part("cus_email") RequestBody gar_email,
            @Part("cus_mob") RequestBody gar_mobi,
            @Part("cus_address") RequestBody gar_address,
            @Part("ProfilePhoto") RequestBody profilePhoto,
            @Part("AadharPhoto") RequestBody aadharPhoto,
            @Part("PanPhoto") RequestBody panPhoto,
            @Part MultipartBody.Part profile_file,
            @Part MultipartBody.Part aadhar_file,
            @Part MultipartBody.Part pan_file);

    @FormUrlEncoded
    @POST("MobileVerify.php")
    Call<ResponseResult> mobileVerification(
            @Field("key") String key,
            @Field("cus_mob") String gar_mobi);

    @FormUrlEncoded
    @POST("SelectVehicleDetails.php")
    Call<MyVehicleList> callVehicleLists(
            @Field("key") String key,
            @Field("cus_id")String cus_id);
    @FormUrlEncoded
    @POST("SelectVehicleDetails.php")
    Call<ResponseResult> callVehicleListsBreak(
            @Field("key") String key,
            @Field("cus_id")String cus_id);

    @FormUrlEncoded
    @POST("SelectVehicleType.php")
    Call<ResponseResult> callVehicleLists(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("SelectVehicleModalyear.php")
    Call<ResponseResult> callMolarYearLists(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("SelectServiceType.php")
    Call<ResponseResult> callServiceLists(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("SelectDistrict.php")
    Call<DistrictList> callDistrictLists(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("taluka1.php")
    Call<TalukaList> callTalukaLists(
            @Field("key")String key,
            @Field("district_id")String district_id);

   /* @FormUrlEncoded
    @POST("taluka1.php")
    Call<ResponseResult> callVillageLists(
            @Field("key")String key);*/

    @FormUrlEncoded
    @POST("SelectLoad.php")
    Call<ResponseResult> callLoadLists(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("Selecttyers.php")
    Call<ResponseResult> callTyersLists(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("SelectProduct.php")
    Call<ResponseResult> callProductLists(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("TransportRequest.php")
    Call<ResponseResult> callTransportRequest(
            @Field("key") String key,
            @Field("trnreqfromid") String trnreqfromid,
            @Field("trnreqtoid") String trnreqtoid,
            @Field("trnreqloadid") String trnreqloadid,
            @Field("trnreqprodid") String trnreqprodid,
            @Field("trnreqprice") String trnreqprice,
            @Field("trnreqaddress") String trnreqaddress,
            @Field("trnreqcusid") String trnreqcusid,
            @Field("trnreqfromtaluka") String trnreqfromtaluka,
            @Field("trnreqtotaluka") String trnreqtotaluka,
            @Field("trnreqarea") String trnreqarea,
            @Field("trnreqlat") String trnreqlat,
            @Field("trnreqlong") String trnreqlong);



    @FormUrlEncoded
    @POST("BreakDown.php")
    Call<ResponseResult> callBreakDown(
            @Field("key") String key,
            @Field("vehicled_id") String vehicle,
            @Field("ordreq_service_type") String service,
            @Field("ordreq_location")String address,
            @Field("cus_id")String cus_id,
            @Field("ordlat") String ordlat,
            @Field("ordlong") String ordlong);

    @FormUrlEncoded
    @POST("breakdowndetails.php")
    Call<ResponseResult> finaList(
            @Field("key") String key,
            @Field("cus_id") String cus_id,
            @Field("from_lat") String ordlat,
            @Field("from_long") String ordlong);

    @FormUrlEncoded
    @POST("TransportListDetails.php")
    Call<ResponseResult> transportList(
            @Field("key") String key,
            @Field("cus_id") String cus_id,
            @Field("from_lat") String from_lat,
            @Field("from_long") String from_long
    );

    @FormUrlEncoded
    @POST("SelectVehicleCategory.php")
    Call<ResponseResult> callCatTypeLists(
            @Field("key") String key,
            @Field("vehicle_id") String vehicleId);

    @Multipart
    @POST("vehicle_details.php")
    Call<ResponseResult> callAddVehicle(
            @Part("key") RequestBody key,
            @Part("cus_id") RequestBody cus_id,
            @Part("vehicle_id") RequestBody vehicle_id,
            @Part("vehicle_cat_id") RequestBody vehicle_cat_id,
            @Part("loadrang_id") RequestBody loadrang_id,
            @Part("vehicle_no_tyres") RequestBody vehicle_no_tyres,
            @Part("vehicle_mody_id") RequestBody vehicle_mody_id,
            @Part("vehicled_regno") RequestBody vehicled_regno,
            @Part("vehicled_kmrd") RequestBody vehicled_kmrd,
            @Part("vehicled_ins_expdate") RequestBody vehicled_ins_expdate,
            @Part("vehicle_fc_expdate") RequestBody vehicle_fc_expdate,
            @Part("CusVehiclePhoto") RequestBody doc_vehicle,
            @Part("doc_insPhoto") RequestBody doc_ins,
            @Part("doc_rcPhoto") RequestBody doc_rc,
            @Part MultipartBody.Part vehicle,
            @Part MultipartBody.Part doc_insurance,
            @Part MultipartBody.Part rc);

}

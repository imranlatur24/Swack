package com.swack.customer.model;

import java.util.ArrayList;

public class DistrictList {

    public String response;

    public ArrayList<DistrictList> DistrictList;


    private String District_Name;

    private String D_ID;

    public DistrictList(String district_Name, String d_ID) {
        District_Name = district_Name;
        D_ID = d_ID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<com.swack.customer.model.DistrictList> getDistrictList() {
        return DistrictList;
    }

    public void setDistrictList(ArrayList<com.swack.customer.model.DistrictList> districtList) {
        DistrictList = districtList;
    }

    public String getDistrict_Name ()
    {
        return District_Name;
    }

    public void setDistrict_Name (String District_Name)
    {
        this.District_Name = District_Name;
    }

    public String getD_ID ()
    {
        return D_ID;
    }

    public void setD_ID (String D_ID)
    {
        this.D_ID = D_ID;
    }

}

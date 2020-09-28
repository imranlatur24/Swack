package com.swack.customer.model;

import java.util.ArrayList;

public class TalukaList {

    public String response;

    private ArrayList<TalukaList> TalukaList;

    private String taluka_name;

    private String T_ID;


    public TalukaList(String taluka_name, String t_ID) {
        this.taluka_name = taluka_name;
        T_ID = t_ID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<com.swack.customer.model.TalukaList> getTalukaList() {
        return TalukaList;
    }

    public void setTalukaList(ArrayList<com.swack.customer.model.TalukaList> talukaList) {
        TalukaList = talukaList;
    }

    public String getTaluka_name() {
        return taluka_name;
    }

    public void setTaluka_name(String taluka_name) {
        this.taluka_name = taluka_name;
    }

    public String getT_ID() {
        return T_ID;
    }

    public void setT_ID(String t_ID) {
        T_ID = t_ID;
    }
}

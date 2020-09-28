package com.swack.customer.model;

import java.util.ArrayList;

public class ResponseResult {

    String response;
    private ArrayList<SliderList> slider_list;
    private ArrayList<SupportListModel> SupportList;
    private ArrayList<ServiceTypeList> ServiceTypeList;
    private ArrayList<VehicleDetailsList> VehicleDetailsList;
    private ArrayList<VehicleDetailsList> VehicleList;
    private ArrayList<DistrictList> DistrictList;
    private ArrayList<TalukaList> TalukaList;
    private ArrayList<VillageList> VillageList;
    private ArrayList<LoadList> LoadList;
    private ArrayList<ProductList> TraProductList;
    private ArrayList<Order> OrderList;
    private ArrayList<TransportList> transport_detail_list;
    private ArrayList<ModalYearList> ModalYearList;
    private ArrayList<VehicleCatList> VehicleCatList;
    private ArrayList<TyerList> TyerList;
    private ArrayList<CusProfileList> CusProfileList;
    private ArrayList<CusProfileList> login;
    public ArrayList<FinalListModel> JobItemFinalList;
    public ArrayList<OLPending> OrderListPend;
    public ArrayList<TrasnportPending> transport_detail_list_pend;
    public ArrayList<TrasnportPending> transport_detail_list_pro;
    public ArrayList<OLPending> OrderListProcess;


    private String userMsgList;

    public ArrayList<TrasnportPending> getTransport_detail_list_pro() {
        return transport_detail_list_pro;
    }

    public void setTransport_detail_list_pro(ArrayList<TrasnportPending> transport_detail_list_pro) {
        this.transport_detail_list_pro = transport_detail_list_pro;
    }

    public ArrayList<OLPending> getOrderListProcess() {
        return OrderListProcess;
    }

    public void setOrderListProcess(ArrayList<OLPending> orderListProcess) {
        OrderListProcess = orderListProcess;
    }

    public String getUserMsgList() {
        return userMsgList;
    }

    public void setUserMsgList(String userMsgList) {
        this.userMsgList = userMsgList;
    }

    public ArrayList<OLPending> getOrderListPend() {
        return OrderListPend;
    }

    public void setOrderListPend(ArrayList<OLPending> orderListPend) {
        OrderListPend = orderListPend;
    }

    public ArrayList<TrasnportPending> getTransport_detail_list_pend() {
        return transport_detail_list_pend;
    }

    public void setTransport_detail_list_pend(ArrayList<TrasnportPending> transport_detail_list_pend) {
        this.transport_detail_list_pend = transport_detail_list_pend;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<com.swack.customer.model.CusProfileList> getLogin() {
        return login;
    }

    public void setLogin(ArrayList<com.swack.customer.model.CusProfileList> login) {
        this.login = login;
    }

    public ArrayList<SliderList> getSlider_list() {
        return slider_list;
    }

    public void setSlider_list(ArrayList<SliderList> slider_list) {
        this.slider_list = slider_list;
    }

    public ArrayList<com.swack.customer.model.TyerList> getTyerList() {
        return TyerList;
    }

    public void setTyerList(ArrayList<com.swack.customer.model.TyerList> tyerList) {
        TyerList = tyerList;
    }

    public ArrayList<SupportListModel> getSupportList() {
        return SupportList;
    }

    public void setSupportList(ArrayList<SupportListModel> supportList) {
        SupportList = supportList;
    }

    public ArrayList<com.swack.customer.model.ServiceTypeList> getServiceTypeList() {
        return ServiceTypeList;
    }

    public void setServiceTypeList(ArrayList<com.swack.customer.model.ServiceTypeList> serviceTypeList) {
        ServiceTypeList = serviceTypeList;
    }

    public ArrayList<com.swack.customer.model.VehicleDetailsList> getVehicleDetailsList() {
        return VehicleDetailsList;
    }

    public void setVehicleDetailsList(ArrayList<com.swack.customer.model.VehicleDetailsList> vehicleDetailsList) {
        VehicleDetailsList = vehicleDetailsList;
    }

    public ArrayList<com.swack.customer.model.DistrictList> getDistrictList() {
        return DistrictList;
    }

    public void setDistrictList(ArrayList<com.swack.customer.model.DistrictList> districtList) {
        DistrictList = districtList;
    }

    public ArrayList<com.swack.customer.model.LoadList> getLoadList() {
        return LoadList;
    }

    public void setLoadList(ArrayList<com.swack.customer.model.LoadList> loadList) {
        LoadList = loadList;
    }

    public ArrayList<ProductList> getTraProductList() {
        return TraProductList;
    }

    public void setTraProductList(ArrayList<ProductList> traProductList) {
        TraProductList = traProductList;
    }

    public ArrayList<Order> getOrderList() {
        return OrderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        OrderList = orderList;
    }

    public ArrayList<TransportList> getTransport_detail_list() {
        return transport_detail_list;
    }

    public void setTransport_detail_list(ArrayList<TransportList> transport_detail_list) {
        this.transport_detail_list = transport_detail_list;
    }

    public ArrayList<com.swack.customer.model.ModalYearList> getModalYearList() {
        return ModalYearList;
    }

    public void setModalYearList(ArrayList<com.swack.customer.model.ModalYearList> modalYearList) {
        ModalYearList = modalYearList;
    }

    public ArrayList<com.swack.customer.model.VehicleCatList> getVehicleCatList() {
        return VehicleCatList;
    }

    public void setVehicleCatList(ArrayList<com.swack.customer.model.VehicleCatList> vehicleCatList) {
        VehicleCatList = vehicleCatList;
    }

    public ArrayList<com.swack.customer.model.CusProfileList> getCusProfileList() {
        return CusProfileList;
    }

    public void setCusProfileList(ArrayList<com.swack.customer.model.CusProfileList> cusProfileList) {
        CusProfileList = cusProfileList;
    }

    public ArrayList<com.swack.customer.model.VehicleDetailsList> getVehicleList() {
        return VehicleList;
    }

    public void setVehicleList(ArrayList<com.swack.customer.model.VehicleDetailsList> vehicleList) {
        VehicleList = vehicleList;
    }

    public ArrayList<FinalListModel> getJobItemFinalList() {
        return JobItemFinalList;
    }

    public void setJobItemFinalList(ArrayList<FinalListModel> jobItemFinalList) {
        JobItemFinalList = jobItemFinalList;
    }

    public ArrayList<TalukaList> getTalukaList() {
        return TalukaList;
    }

    public void setTalukaList(ArrayList<com.swack.customer.model.TalukaList> talukaList) {
        TalukaList = talukaList;
    }

    public ArrayList<com.swack.customer.model.VillageList> getVillageList() {
        return VillageList;
    }

    public void setVillageList(ArrayList<com.swack.customer.model.VillageList> villageList) {
        VillageList = villageList;
    }
}

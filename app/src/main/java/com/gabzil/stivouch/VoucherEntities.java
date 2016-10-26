package com.gabzil.stivouch;

/**
 * Created by Yogesh on 06-Oct-16.
 */
public class VoucherEntities {
    int VoucherID;
    String VoucherName;
    String UserName;
    String Password;
    String DOB;
    String MobileNo;
    int CityID;
    String City;
    int StatesID;
    String States;
    String EMailID;
    String CompanyName;
    int CompanyID;

    public int getVoucherID() {
        return VoucherID;
    }

    public void setVoucherID(int voucherID) {
        VoucherID = voucherID;
    }

    public String getVoucherName() {
        return VoucherName;
    }

    public void setVoucherName(String voucherName) {
        VoucherName = voucherName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public int getStatesID() {
        return StatesID;
    }

    public void setStatesID(int statesID) {
        StatesID = statesID;
    }

    public String getStates() {
        return States;
    }

    public void setStates(String states) {
        States = states;
    }

    public String getEMailID() {
        return EMailID;
    }

    public void setEMailID(String EMailID) {
        this.EMailID = EMailID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public int getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }
}

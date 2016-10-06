package com.gabzil.stivouch;

/**
 * Created by Yogesh on 06-Oct-16.
 */
public class VoucherEntities {
    int VoucherID;
    String VoucherName;
    String VoucherMail;
    String DOB;
    String MobileNo;
    String State;
    String Country;
    String Password;
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

    public String getVoucherMail() {
        return VoucherMail;
    }

    public void setVoucherMail(String voucherMail) {
        VoucherMail = voucherMail;
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

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

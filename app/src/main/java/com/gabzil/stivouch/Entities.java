package com.gabzil.stivouch;

/**
 * Created by Yogesh on 30-Sep-16.
 */
public class Entities {
    int ID;
    String LandingPage;
    String OTP;
    String Login;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLandingPage() {
        return LandingPage;
    }

    public void setLandingPage(String landingPage) {
        LandingPage = landingPage;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }
}

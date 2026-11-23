package com.example.allinoneapp;

public class LoginLocation {

    String LoginID;
    String Longtitude;
    String Latitude;
    String TimeStamp;
    String Address;
    String LaborerName;
    String MobileNumber;

    public  LoginLocation() {}

    public LoginLocation(String loginID, String longtitude, String latitude,
                         String timeStamp, String address, String laborerName,
                         String mobileNumber){
        this.LoginID = loginID;
        this.Longtitude = longtitude;
        this.Latitude = latitude;
        this.TimeStamp = timeStamp;
        this.Address = address;
        this.LaborerName = laborerName;
        this.MobileNumber = mobileNumber;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(String longtitude) {
        Longtitude = longtitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLaborerName() {
        return LaborerName;
    }

    public void setLaborerName(String laborerName) {
        LaborerName = laborerName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }
}



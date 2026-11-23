package com.example.allinoneapp;

public class LoginModel {
    int LoginID;
    String EmailAddress;
    String Password;
    String LastName;
    String FirstName;
    String MiddleName;
    int MobileNumber;
    int UserType;
    String Address;

    public LoginModel(){}

    public LoginModel(int loginID, String emailAddress, String password, String lastName, String firstName,
                      String middleName, int mobileNumber, int userType, String address) {
        LoginID = loginID;
        EmailAddress = emailAddress;
        Password = password;
        LastName = lastName;
        FirstName = firstName;
        MiddleName = middleName;
        MobileNumber = mobileNumber;
        UserType = userType;
        Address = address;
    }

    public int getLoginID() {
        return LoginID;
    }

    public void setLoginID(int loginID) {
        LoginID = loginID;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public int getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(int mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

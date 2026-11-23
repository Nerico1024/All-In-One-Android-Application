package com.example.allinoneapp.ui.home;

public class ServiceModel {
    String  service;

    public ServiceModel(String service){
        this.service = service;
    }

    public void ChangeServiceText(String  text){
        service = text;
    }

    public String getService() { return  service;}
}

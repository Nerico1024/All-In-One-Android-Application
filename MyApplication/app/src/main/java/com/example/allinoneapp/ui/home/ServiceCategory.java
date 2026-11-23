package com.example.allinoneapp.ui.home;

public class ServiceCategory {
    Integer langLogo;
    String  langName;

    public ServiceCategory(Integer langLogo,String langName){
        this.langLogo = langLogo;
        this.langName = langName;
    }

    public  Integer getLangLogo(){
        return langLogo;
    }

    public String getLangName() {
        return langName;
    }

    public void ChangeLangName(String  text){
        langName = text;
    }
}

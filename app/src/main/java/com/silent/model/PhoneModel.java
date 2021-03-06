package com.silent.model;

import android.graphics.drawable.Drawable;

public class PhoneModel {

    private String imgSrc;
    private String name;
    private String sortLetters;
    private String phone;
    private String companytype;
    private Drawable comicon;

    public String getCompanytype() {
        return companytype;
    }

    public void setCompanytype(String companytype) {
        this.companytype = companytype;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public Drawable getComicon() {
        return comicon;
    }

    public void setComicon(Drawable comicon) {
        this.comicon = comicon;
    }
}

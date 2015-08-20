package com.example.express.bean;

import java.util.ArrayList;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/19 19:51
 * 修改人：xutework
 * 修改时间：2015/8/19 19:51
 * 修改备注：
 */
public class CouriorDetailBean extends BaseBean {
    private String name;
    private String servertime;
    private String phone;
    private String ename;
    private String courierIcon;
    private String isFavourite;//0表示已关注，1表示未关注
    private ArrayList<ScopeBean> delivery;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCourierIcon() {
        return courierIcon;
    }

    public void setCourierIcon(String courierIcon) {
        this.courierIcon = courierIcon;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    public ArrayList<ScopeBean> getDelivery() {
        return delivery;
    }

    public void setDelivery(ArrayList<ScopeBean> delivery) {
        this.delivery = delivery;
    }
}

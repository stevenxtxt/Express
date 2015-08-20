package com.example.express.bean;

import com.db.annotation.Table;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/19 10:51
 * 修改人：xutework
 * 修改时间：2015/8/19 10:51
 * 修改备注：
 */
@Table(name = "T_RecycleItemBean")
public class RecycleItemBean {
    private String _id;
    private String companytype;
    private String company;
    private String nu;
    private String ico;
    private String latestContext;
    private String latestTime;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompanytype() {
        return companytype;
    }

    public void setCompanytype(String companytype) {
        this.companytype = companytype;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getLatestContext() {
        return latestContext;
    }

    public void setLatestContext(String latestContext) {
        this.latestContext = latestContext;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }
}

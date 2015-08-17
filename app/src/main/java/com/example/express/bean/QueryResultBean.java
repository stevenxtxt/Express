package com.example.express.bean;

import java.util.ArrayList;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/12 15:09
 * 修改人：xutework
 * 修改时间：2015/8/12 15:09
 * 修改备注：
 */
public class QueryResultBean extends BaseBean {
    //运单号
    private String nu;
    //快递公司简码
    private String companytype;
    //快递公司名称
    private String company;
    //快递公司logo图片地址
    private String ico;
    //结果列表
    private ArrayList<QueryResultItemBean> data;

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
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

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public ArrayList<QueryResultItemBean> getData() {
        return data;
    }

    public void setData(ArrayList<QueryResultItemBean> data) {
        this.data = data;
    }
}

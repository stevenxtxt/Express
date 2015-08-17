package com.example.express.bean;

import java.util.ArrayList;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/12 15:28
 * 修改人：xutework
 * 修改时间：2015/8/12 15:28
 * 修改备注：
 */
public class CompanyListBean extends BaseBean {
    private ArrayList<CompanyBean> data;

    public ArrayList<CompanyBean> getData() {
        return data;
    }

    public void setData(ArrayList<CompanyBean> data) {
        this.data = data;
    }
}

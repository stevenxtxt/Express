package com.example.express.bean;

import com.db.annotation.Table;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/5 14:42
 * 修改人：xutework
 * 修改时间：2015/8/5 14:42
 * 修改备注：
 */

@Table(name = "T_SearchRecordBean")
public class SearchRecordBean extends BaseBean {
    private String _id;
    private String name;
    private String district;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}

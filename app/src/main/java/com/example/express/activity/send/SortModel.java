package com.example.express.activity.send;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/10 14:49
 * 修改人：xutework
 * 修改时间：2015/8/10 14:49
 * 修改备注：
 */
public class SortModel {
    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

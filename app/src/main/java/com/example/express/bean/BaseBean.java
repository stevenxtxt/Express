package com.example.express.bean;

import com.boredream.volley.BDVolleyUtils;

/**
 * Created by xutework on 2015/7/23.
 */
public class BaseBean implements BDVolleyUtils.BDSmartParserable {
    private String returnMsg;

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}

package com.example.express.bean;

import com.boredream.volley.BDVolleyUtils;

import java.io.Serializable;

/**
 * Created by xutework on 2015/7/23.
 */
public class BaseBean implements BDVolleyUtils.BDSmartParserable, Serializable {
    private boolean result;
    private String reason;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

package com.example.express.bean;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 16:15
 * 修改人：xutework
 * 修改时间：2015/7/29 16:15
 * 修改备注：
 */
public class MessageBean extends BaseBean {
    private String title;
    private String time;
    private String context;
    private String isRead;//0已读，1未读

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}

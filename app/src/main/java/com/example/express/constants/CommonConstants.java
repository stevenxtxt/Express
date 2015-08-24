package com.example.express.constants;

public class CommonConstants {

    public static final String SP_NAME = "sp_config";
    public static final boolean isShowLog = true;

    public static boolean isSaveLog2File = false;

    public static final int COUNT_PER_PAGE = 20;
    public static final String DIVIDER_IMAGE_URLS = "#boredream-image-urls#";

    public static final String DB_NAME = "Express.db";

    public static final String URLConstant = "http://www.kuaidi.com";

    public static final String HTML = ".html";

    public static final String HOT_LINE = "025-68022102";

    public static final String HOME_PAGE = "http://www.kuaidi.com";
    public static final String WEIXIN_PAGE = "http://www.kuaidi.com/weixin.html";
    public static final String WEIBO_PAGE = "http://m.weibo.cn/5620577606";

    /**
     * 查询快递接口
     */
    public static final String QUERY_EXPRESS_RESULT = "/api-appajaxselectcourierinfo-";

    /**
     * 查询快递公司接口
     */
    public static final String QUERY_EXPRESS_COMPANY = "/api-appselectcominfo";

    /**
     * 自动匹配快递公司接口
     */
    public static final String MATCH_COMPANY = "/api-appajaxselectinfo-";

    /**
     * 登录接口
     */
    public static final String LOGIN = "/api-applogin";

    /**
     * 修改真实姓名
     */
    public static final String CHANGE_TRUENAME = "/api-appupdatatruename";

    /**
     * 修改密码
     */
    public static final String CHANGE_PASSWORD = "/api-appchangepsw";

    /**
     * 获取验证码
     */
    public static final String GET_VERIFY_CODE = "/api-appgetyzm-";

    /**
     * 验证手机号
     */
    public static final String VERIFY_PHONE = "/api-apptestphone";

    /**
     * 修改手机号
     */
    public static final String CHANGE_PHONE = "/api-appchangephone";

    /**
     * 修改性别
     */
    public static final String CHANGE_GENDER = "/api-appchangesex";

    /**
     * 修改所在地
     */
    public static final String CHANGE_AREA = "/api-appchangearea";

    /**
     * 忘记密码
     */
    public static final String FORGET_PASSWORD = "/api-apprepassword";

    /**
     * 查询个人信息
     */
    public static final String QUERY_USER = "/api-appgetuserinfo-";

    /**
     * 提交反馈建议
     */
    public static final String FEEDBACK = "/api-apppostmastake";

    /**
     * 查询附件的快递员列表
     */
    public static final String QUERY_NEARBY_COURIER = "/api-appgetnearbypm";

    /**
     * 查询快递员详情
     */
    public static final String QUERY_COURIOR_DETAIL = "/api-appgetpminfo-";

    /**
     * 查看用户收藏的快递员
     */
    public static final String QUERY_FAV_COURIER = "/api-appgetcollectpm-";

    /**
     * 关注和取消关注快递员
     */
    public static final String FOLLOW_OR_UNFOlLOW = "/api-appiscollectpm";

    /**
     * 注册
     */
    public static final String REGISTER = "/api-appregister";

}

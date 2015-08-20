package com.example.express.activity.more;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.constants.CommonConstants;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/20 18:14
 * 修改人：xutework
 * 修改时间：2015/8/20 18:14
 * 修改备注：
 */
public class ShowWebActivity extends BaseActivity {
    private WebView wv_web;
    private String flag;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_web);
        intent = getIntent();
        flag = intent.getStringExtra("flag");

        initTop();

        wv_web = (WebView) findViewById(R.id.wv_web);

        if (flag.equals("homepage")) {
            setTitle("访问官网");
            showPage(CommonConstants.HOME_PAGE);
        } else if (flag.equals("weibo")) {
            setTitle("关注微博");
            showPage(CommonConstants.WEIBO_PAGE);
        } else if (flag.equals("weixin")) {
            setTitle("关注微信");
            showPage(CommonConstants.WEIXIN_PAGE);
        }
    }

    private void showPage(String address) {
        wv_web.loadUrl(address);
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}

package com.example.express.activity.more;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.bean.MessageBean;
import com.example.express.utils.Density;

import java.util.ArrayList;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/29 13:52
 * 修改人：xutework
 * 修改时间：2015/7/29 13:52
 * 修改备注：
 */
public class AboutActivity extends BaseActivity {

    private RelativeLayout rl_view_homepage;
    private RelativeLayout rl_weibo;
    private RelativeLayout rl_weixin;
    private RelativeLayout rl_about_us;
    private TextView tv_version_no;

    public static final int WEIXIN = 1;
    public static final int WEIBO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        initTop();
        setTitle("关于");

        initViews();
    }

    private void initViews() {
        rl_view_homepage = (RelativeLayout) findViewById(R.id.rl_view_homepage);
        rl_weibo = (RelativeLayout) findViewById(R.id.rl_weibo);
        rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
        rl_about_us = (RelativeLayout) findViewById(R.id.rl_about_us);
        tv_version_no = (TextView) findViewById(R.id.tv_version_no);
        rl_view_homepage.setOnClickListener(this);
        rl_weibo.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);

        tv_version_no.setText("V" + getVersionName());
    }

    /*
    获取版本号
     */
    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "exception";
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_view_homepage:
                intent.setClass(this, ShowWebActivity.class);
                intent.putExtra("flag", "homepage");
                startActivity(intent);
                break;

            case R.id.rl_weibo:
//                intent.setClass(this, ShowWeiboPicActivity.class);
//                startActivity(intent);
                showCopyDialog(WEIBO);
                break;

            case R.id.rl_weixin:
//                intent.setClass(this, ShowWebActivity.class);
//                intent.putExtra("flag", "weixin");
//                startActivity(intent);
                showCopyDialog(WEIXIN);
                break;

            case R.id.rl_about_us:
                intent.setClass(this, AboutUsActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void showCopyDialog(final int type) {
        LayoutInflater inflater = LayoutInflater.from(AboutActivity.this);
        View view = inflater.inflate(R.layout.delete_dialog_layout, null);
        TextView tv_zhuyi = (TextView) view.findViewById(R.id.tv_zhuyi);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
        btn_delete.setText("复制");
        if (type == WEIBO) {
            tv_zhuyi.setText("请在微博中搜索关注南京追影网络科技有限公司");
        } else if (type == WEIXIN) {
            tv_zhuyi.setText("请在微信中搜索关注公众号kuaidicom");
        }

        final Dialog delete_dialog = new Dialog(AboutActivity.this, R.style.call_dialog);
        delete_dialog.setContentView(view);
        delete_dialog.setCanceledOnTouchOutside(true);
        delete_dialog.show();
        WindowManager.LayoutParams lp = delete_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(AboutActivity.this)); // 设置宽度
        delete_dialog.getWindow().setAttributes(lp);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_dialog.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                delete_dialog.dismiss();
                ClipboardManager copy = (ClipboardManager) AboutActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                if (type == WEIBO) {
                    copy.setText("南京追影网络科技有限公司");
                } else if (type == WEIXIN) {
                    copy.setText("kuaidicom");
                }
                showToast("已复制公众号到粘贴板");
            }
        });
    }
}

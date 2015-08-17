package com.example.express.activity.more;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.express.R;
import com.example.express.activity.BaseActivity;
import com.example.express.utils.Density;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/7/27 10:01
 * 修改人：xutework
 * 修改时间：2015/7/27 10:01
 * 修改备注：
 */
public class PersonalInfoActivity extends BaseActivity {
    private TextView tv_change_password;
    private RelativeLayout rl_username;
    private TextView tv_username;
    private RelativeLayout rl_true_name;
    private TextView tv_true_name;
    private RelativeLayout rl_gender;
    private TextView tv_gender;
    private RelativeLayout rl_phone;
    private TextView tv_phone;
    private RelativeLayout rl_address;
    private TextView tv_address;
    private Button btn_logout;

    private String province;
    private String city;
    private String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        initTop();
        setTitle("个人信息");
        initViews();
    }

    private void initViews() {
        tv_change_password = (TextView) findViewById(R.id.tv_change_password);
        rl_username = (RelativeLayout) findViewById(R.id.rl_username);
        tv_username = (TextView) findViewById(R.id.tv_username);
        rl_true_name = (RelativeLayout) findViewById(R.id.rl_true_name);
        tv_true_name = (TextView) findViewById(R.id.tv_true_name);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        tv_address = (TextView) findViewById(R.id.tv_address);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        tv_change_password.setOnClickListener(this);
        rl_username.setOnClickListener(this);
        rl_true_name.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_change_password:
                launch(ChangePasswordActivity.class);
                break;

            case R.id.rl_username:
                break;

            case R.id.rl_true_name:
                launch(TrueNameActivity.class);
                break;

            case R.id.rl_gender:
                showGenderDialog();
                break;

            case R.id.rl_phone:
                launch(ChangePhoneOneActivity.class);
                break;

            case R.id.rl_address:
                Intent address_intent = new Intent(PersonalInfoActivity.this, ChooseAddressActivity.class);
                startActivityForResult(address_intent, 1001);
                break;

            case R.id.btn_logout:
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            city = data.getStringExtra("city");
            province = data.getStringExtra("province");
            tv_address.setText(province + "-" + city);
        }
    }

    private void showGenderDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.choose_gender_dialog, null);
        final TextView tv_privacy = (TextView) view.findViewById(R.id.tv_privacy);
        final TextView tv_male = (TextView) view.findViewById(R.id.tv_male);
        final TextView tv_female = (TextView) view.findViewById(R.id.tv_female);

        final Dialog call_dialog = new Dialog(this, R.style.call_dialog);
        call_dialog.setCanceledOnTouchOutside(true);
        call_dialog.setContentView(view);
        call_dialog.show();
        WindowManager.LayoutParams lp = call_dialog.getWindow().getAttributes();
        lp.width = (int) (Density.getSceenWidth(this)); // 设置宽度
        call_dialog.getWindow().setAttributes(lp);

        tv_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_dialog.dismiss();
                gender = tv_privacy.getText().toString();
                tv_gender.setText(gender);
            }
        });
        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_dialog.dismiss();
                gender = tv_male.getText().toString();
                tv_gender.setText(gender);
            }
        });
        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_dialog.dismiss();
                gender = tv_female.getText().toString();
                tv_gender.setText(gender);
            }
        });
    }
}

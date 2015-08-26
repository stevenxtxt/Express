package com.example.express.activity.more;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.express.BaseApplication;
import com.example.express.activity.BaseFragment;
import com.example.express.R;
import com.example.express.activity.my.LoginActivity;
import com.example.express.bean.LoginUser;
import com.example.express.view.RoundImageView;

import org.w3c.dom.Text;

public class MoreFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private RoundImageView riv_user_photo;
    private RelativeLayout rl_username;
    private TextView tv_username;
    private TextView tv_login_status;
    private RelativeLayout rl_recycler;
    private RelativeLayout rl_share;
    private RelativeLayout rl_message;
    private RelativeLayout rl_favourite;
    private RelativeLayout rl_feedback;
    private RelativeLayout rl_about;

    private BaseApplication app;
    private LoginUser loginUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = BaseApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_more, null);
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loginUser = app.getLoginUser();
        if (loginUser != null) {
            tv_username.setText(loginUser.getUsername());
            tv_login_status.setVisibility(View.GONE);
            rl_username.setClickable(true);
            riv_user_photo.setBackgroundResource(R.drawable.user_login_icon);
        } else {
            tv_username.setText("个人信息");
            tv_login_status.setVisibility(View.VISIBLE);
            rl_username.setClickable(false);
            riv_user_photo.setBackgroundResource(R.drawable.user_logout_icon);
        }
    }

    private void initViews() {
        riv_user_photo = (RoundImageView) view.findViewById(R.id.riv_user_photo);
        rl_username = (RelativeLayout) view.findViewById(R.id.rl_username);
        tv_username = (TextView) view.findViewById(R.id.tv_username);
        tv_login_status = (TextView) view.findViewById(R.id.tv_login_status);
        rl_recycler = (RelativeLayout) view.findViewById(R.id.rl_recycler);
        rl_share = (RelativeLayout) view.findViewById(R.id.rl_share);
        rl_message = (RelativeLayout) view.findViewById(R.id.rl_message);
        rl_favourite = (RelativeLayout) view.findViewById(R.id.rl_favourite);
        rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
        rl_about = (RelativeLayout) view.findViewById(R.id.rl_about);

        riv_user_photo.setOnClickListener(this);
        rl_username.setOnClickListener(this);
        tv_login_status.setOnClickListener(this);
        rl_recycler.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_favourite.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_user_photo:
                break;

            case R.id.tv_login_status:
                launch(LoginActivity.class);
                break;

            case R.id.rl_username:
                launch(PersonalInfoActivity.class);
                break;

            case R.id.rl_recycler:
                if (loginUser == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                launch(RecyclerActivity.class);
                break;

            case R.id.rl_share:
                if (loginUser == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;

            case R.id.rl_message:
                if (loginUser == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                launch(MessageActivity.class);
                break;

            case R.id.rl_favourite:
                if (loginUser == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                launch(FavouriteCourierActivity.class);
                break;

            case R.id.rl_feedback:
                launch(FeedbackActivity.class);
                break;

            case R.id.rl_about:
                launch(AboutActivity.class);
                break;

            default:
                break;
        }
    }
}

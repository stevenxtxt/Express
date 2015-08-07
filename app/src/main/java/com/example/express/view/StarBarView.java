package com.example.express.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 评星
 * 
 * @author afei
 * 
 *         use sbv_store_star = (StarBarView) findViewById(R.id.sbv_store_star);
 *         sbv_store_star.setBgImgResId(R.drawable.icon_stat_blue, R.drawable.icon_stat_white); try {
 *         sbv_store_star.setImgCount(5, 4); } catch (AppException e) { //can't reach e.printStackTrace(); }
 *         //flag可以设置成FLAG_MODE_SHOW_NO_SELECTED 显示灰色的星或全部星等...
 *         sbv_store_star.setFlag(StarBarView.FLAG_CLICK_NO_CHANGE_IMG|StarBarView.FLAG_MODE_ONLY_SHOW_SELECTED);
 *         sbv_store_star.show(this,40,40);
 * 
 */
public class StarBarView extends LinearLayout implements OnClickListener {

    private int bgSelectedImgResId; // 选中的 亮的星或之类的图片

    private int bgNoSelectedImgResId; // 未先中 灰的星或之类的图片
    private int bgHalfSelectedImgResId;// 选中亮的半颗心

    private int imgCount; // 显示图片的个数

    private int imgSelectedCount; // 被选中图片的个数
    private Boolean isHalf = false;
    private int flag;

    private int clickNum;

    public static final int FLAG_CLICK_CHANGE_IMG = 0x1; // 点击某个星图片后，可选择星级，改变图片 ---可去选择
    public static final int FLAG_CLICK_NO_CHANGE_IMG = 0x2; // 点击某个星图片后，不可选择星级 ---默认的选择
    public static final int FLAG_MODE_SHOW_NO_SELECTED = 0x4; // 显示全部的图片 --默认的选择
    public static final int FLAG_MODE_ONLY_SHOW_SELECTED = 0x8; // 只显示选择中的图片，不显示灰的图片 ---可去选择

    private OnStarBarListener onStatBarListener;

    private static final int IMG_GAP_WIDTH = 10;

    private static final int IMG_STAR_WIDTH = LayoutParams.WRAP_CONTENT;
    private static final int IMG_STAR_HEIGHT = LayoutParams.WRAP_CONTENT;

    /*
     * public StarBarView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
     */

    public StarBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StarBarView(Context context) {
        super(context);
    }

    public void setBgImgResId(int bgSelectedImgResId, int bgNoSelectedImgResId) {
        this.bgNoSelectedImgResId = bgNoSelectedImgResId;
        this.bgSelectedImgResId = bgSelectedImgResId;
    }

    public void setBgImgResId(int bgSelectedImgResId, int bgNoSelectedImgResId, int bgHalfSelectedImgResId) {
        this.bgNoSelectedImgResId = bgNoSelectedImgResId;
        this.bgSelectedImgResId = bgSelectedImgResId;
        this.bgHalfSelectedImgResId = bgHalfSelectedImgResId;
    }

    public void setImgCount(int imgCount, int imgSelectedCount, boolean isHalf) throws Exception {
        this.imgCount = imgCount;
        this.imgSelectedCount = imgSelectedCount;
        this.isHalf = isHalf;
        if (imgSelectedCount >= imgCount) {
            this.imgSelectedCount = imgCount;
            this.isHalf = false;
            // throw new
            // AppException("imgSelectedCount "+imgSelectedCount+" can't no bigger than imgCount "+imgCount+"!");
        } else {
            if (isHalf)
                this.imgSelectedCount = imgSelectedCount + 1;
        }
    }

    public void setImgCount(int imgCount, int imgSelectedCount) throws Exception {
        this.imgCount = imgCount;
        this.imgSelectedCount = imgSelectedCount;
        if (imgSelectedCount > imgCount) {
            this.imgSelectedCount = imgCount;
            // throw new
            // AppException("imgSelectedCount "+imgSelectedCount+" can't no bigger than imgCount "+imgCount+"!");
        }
    }

    public int getSelectedNum() {
        return imgSelectedCount;
    }

    public void show(Context context, int gapWidth, int starWidth, int starHeight) {
        removeAllViews();
        ArrayList<View> starViews = new ArrayList<View>();
        for (int i = 0; i < imgCount; i++) {
            View starView = new View(context);
            starView.setBackgroundResource(bgNoSelectedImgResId);
            starView.setId(i);
            if ((flag & FLAG_CLICK_CHANGE_IMG) == FLAG_CLICK_CHANGE_IMG) {
                starView.setOnClickListener(this);
            }
            starViews.add(starView);
        }

        for (int i = 0; i < imgSelectedCount; i++) {
            View starView = starViews.get(i);
            if ((i == (imgSelectedCount - 1)) && isHalf) {
                starView.setBackgroundResource(bgHalfSelectedImgResId);
            } else
                starView.setBackgroundResource(bgSelectedImgResId);
        }

        setOrientation(HORIZONTAL);// 设置水平排列
        // 将图片加入进去
        for (int i = 0; i < imgCount; i++) {
            LayoutParams lp = new LayoutParams(starWidth, starHeight);
            lp.rightMargin = gapWidth;
            if ((flag & FLAG_MODE_ONLY_SHOW_SELECTED) == FLAG_MODE_ONLY_SHOW_SELECTED) { // 只要选中的呢
                if (i < imgSelectedCount) {
                    addView(starViews.get(i), lp);
                }
            } else {
                addView(starViews.get(i), lp);
            }
        }
    }

    public void show(Context context, int starWidth, int starHeight) {
        show(context, IMG_GAP_WIDTH, starWidth, starHeight);
    }

    public void show(Context context, int gapWidth) {
        show(context, gapWidth, IMG_STAR_WIDTH, IMG_STAR_HEIGHT);
    }

    public void show(Context context) {
        show(context, IMG_GAP_WIDTH);
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setOnStatBarListener(OnStarBarListener onStatBarListener) {
        this.onStatBarListener = onStatBarListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        for (int i = 0; i < imgCount; i++) {
            View starView = getChildAt(i);
            if (i <= id) {
                starView.setBackgroundResource(bgSelectedImgResId);
            } else {
                starView.setBackgroundResource(bgNoSelectedImgResId);
            }
        }
        if (null != onStatBarListener) {
            onStatBarListener.onClickNum(id + 1);// ID是从0开始的。
            this.setClickNum(id + 1);
        }

    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public interface OnStarBarListener {
        void onClickNum(int clickNum); // 点击的哪个位置的图片
    }

}

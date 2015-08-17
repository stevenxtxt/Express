package com.example.express.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 
 * 〈不可滑动的gridview〉<br> 
 * 〈功能详细描述〉
 *
 * @author yinzl
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MyGridView extends GridView {
	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

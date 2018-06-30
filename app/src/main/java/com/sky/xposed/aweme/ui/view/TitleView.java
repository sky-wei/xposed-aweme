/*
 * Copyright (c) 2018 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.aweme.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.xposed.aweme.ui.util.LayoutUtil;
import com.sky.xposed.aweme.ui.util.ViewUtil;
import com.sky.xposed.aweme.util.DisplayUtil;

/**
 * Created by sky on 18-4-17.
 */

public class TitleView extends FrameLayout implements View.OnClickListener {

    private ImageButton ivClose;
    private TextView tvTitle;
    private ImageButton ivMore;
    private OnTitleEventListener mOnTitleEventListener;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int height = DisplayUtil.dip2px(getContext(), 45);

        setLayoutParams(LayoutUtil.newViewGroupParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        setBackgroundColor(0xFF161823);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(6);
        }

        LinearLayout tLayout = new LinearLayout(getContext());
        tLayout.setGravity(Gravity.CENTER_VERTICAL);
        tLayout.setOrientation(LinearLayout.HORIZONTAL);

        ivClose = new ImageButton(getContext());
        ivClose.setLayoutParams(LayoutUtil.newViewGroupParams(height, height));
        ivClose.setTag("close");
        ivClose.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        ivClose.setBackground(newBackgroundDrawable());
        ivClose.setOnClickListener(this);

        tvTitle = new TextView(getContext());
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(18);

        tLayout.addView(ivClose);
        tLayout.addView(tvTitle);

        ivMore = new ImageButton(getContext());
        ivMore.setTag("more");
        ivMore.setImageResource(android.R.drawable.ic_menu_more);
        ivMore.setBackground(newBackgroundDrawable());
        ivMore.setOnClickListener(this);

        FrameLayout.LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;

        FrameLayout.LayoutParams imageParams = LayoutUtil.newFrameLayoutParams(height, height);
        imageParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

        addView(tLayout, params);
        addView(ivMore, imageParams);

        hideClose();
        hideMore();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void showMore() {
        ViewUtil.setVisibility(ivMore, View.VISIBLE);
    }

    public void hideMore() {
        ViewUtil.setVisibility(ivMore, View.GONE);
    }

    public void showClose() {
        ViewUtil.setVisibility(ivClose, View.VISIBLE);
        tvTitle.setPadding(0, 0, 0, 0);
    }

    public void hideClose() {
        ViewUtil.setVisibility(ivClose, View.GONE);
        tvTitle.setPadding(DisplayUtil.dip2px(getContext(), 15), 0, 0, 0);
    }

    public OnTitleEventListener getOnTitleEventListener() {
        return mOnTitleEventListener;
    }

    public void setOnTitleEventListener(OnTitleEventListener onTitleEventListener) {
        mOnTitleEventListener = onTitleEventListener;
    }

    @Override
    public void onClick(View v) {

        if (mOnTitleEventListener == null) return ;

        String tag = (String) v.getTag();

        if ("close".equals(tag)) {
            mOnTitleEventListener.onCloseEvent(v);
        } else {
            mOnTitleEventListener.onMoreEvent(v);
        }
    }

    public interface OnTitleEventListener {

        void onCloseEvent(View view);

        void onMoreEvent(View view);
    }

    private StateListDrawable newBackgroundDrawable() {

        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(0x66666666));
        drawable.addState(new int[] {}, new ColorDrawable(0x00000000));

        return drawable;
    }
}

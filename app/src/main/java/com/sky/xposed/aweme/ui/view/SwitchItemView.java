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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sky.xposed.aweme.ui.interfaces.TrackViewStatus;
import com.sky.xposed.aweme.ui.util.LayoutUtil;
import com.sky.xposed.aweme.ui.util.ViewUtil;
import com.sky.xposed.aweme.util.DisplayUtil;

/**
 * Created by sky on 18-3-12.
 */

public class SwitchItemView extends FrameLayout implements View.OnClickListener, TrackViewStatus<Boolean> {

    private TextView tvName;
    private Switch mSwitch;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public SwitchItemView(Context context) {
        this(context, null);
    }

    public SwitchItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    private void initView() {

        int left = DisplayUtil.dip2px(getContext(), 15);

        setPadding(left, 0, left, 0);
        setBackground(ViewUtil.newBackgroundDrawable());
        setLayoutParams(LayoutUtil.newViewGroupParams(
                LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getContext(), 40)));

        tvName = new TextView(getContext());
        tvName.setTextColor(Color.BLACK);
        tvName.setTextSize(15);

        FrameLayout.LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;

        addView(tvName, params);

        mSwitch = new Switch(getContext());
        mSwitch.setClickable(false);
        mSwitch.setFocusable(false);
        mSwitch.setFocusableInTouchMode(false);

        params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

        addView(mSwitch, params);

        setOnClickListener(this);
    }

    public void setName(String title) {
        tvName.setText(title);
    }

    public String getName() {
        return tvName.getText().toString();
    }

    public void setChecked(boolean checked) {
        mSwitch.setChecked(checked);
    }

    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    @Override
    public void onClick(View v) {

        setChecked(!isChecked());

        if (mOnCheckedChangeListener != null)
            mOnCheckedChangeListener.onCheckedChanged(this, isChecked());
    }

    @Override
    public void bind(final SharedPreferences preferences,
                     final String key, Boolean defValue, final StatusChangeListener<Boolean> listener) {
        // 设置状态
        setChecked(preferences.getBoolean(key, defValue));
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean isChecked) {

                if (listener.onStatusChange(view, key, isChecked)) {
                    // 保存状态信息
                    preferences.edit().putBoolean(key, isChecked).apply();
                }
            }
        });
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(View view, boolean isChecked);
    }
}

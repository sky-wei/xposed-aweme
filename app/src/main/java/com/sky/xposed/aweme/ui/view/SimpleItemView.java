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
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sky.xposed.aweme.ui.util.LayoutUtil;
import com.sky.xposed.aweme.ui.util.ViewUtil;
import com.sky.xposed.aweme.util.DisplayUtil;

/**
 * Created by sky on 18-5-21.
 */

public class SimpleItemView extends FrameLayout {

    private TextView tvName;
    private TextView tvExtend;

    public SimpleItemView(Context context) {
        this(context, null);
    }

    public SimpleItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackground(ViewUtil.newBackgroundDrawable());
        setLayoutParams(LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getContext(), 40)));

        tvName = new TextView(getContext());
        tvName.setTextColor(Color.BLACK);
        tvName.setTextSize(16);

        tvExtend = new TextView(getContext());
        tvExtend.setTextColor(Color.GRAY);
        tvExtend.setGravity(Gravity.RIGHT);
        tvExtend.setTextSize(15);

        LayoutParams params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = DisplayUtil.dip2px(getContext(), 15);

        addView(tvName, params);

        params = LayoutUtil.newWrapFrameLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        params.rightMargin = DisplayUtil.dip2px(getContext(), 15);

        addView(tvExtend, params);
    }

    public void setName(String title) {
        tvName.setText(title);
    }

    public String getName() {
        return tvName.getText().toString();
    }

    public void setExtend(String value) {
        tvExtend.setText(value);
    }

    public String getExtend() {
        return tvExtend.getText().toString();
    }

    public void setExtendHint(String value) {
        tvExtend.setHint(value);
    }
}

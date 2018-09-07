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

package com.sky.xposed.aweme.ui.dialog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.R;
import com.sky.xposed.aweme.ui.base.BaseDialog;
import com.sky.xposed.aweme.ui.util.UriUtil;
import com.sky.xposed.common.ui.interfaces.TrackViewStatus;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.SwitchItemView;
import com.sky.xposed.common.ui.view.TitleView;
import com.squareup.picasso.Picasso;

/**
 * Created by sky on 18-6-9.
 */
public class OtherDialog extends BaseDialog implements TitleView.OnBackEventListener {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;

    private SwitchItemView sivRemoveAd;
    private SwitchItemView sivDisableUpdate;
    private SwitchItemView sivCopyVideoDesc;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {

        // 不显示默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCommonFrameLayout = new CommonFrameLayout(getContext());
        mToolbar = mCommonFrameLayout.getTitleView();

        sivRemoveAd = ViewUtil.newSwitchItemView(getContext(), "移除抖音广告");
        sivDisableUpdate = ViewUtil.newSwitchItemView(getContext(), "禁用抖音更新");
        sivCopyVideoDesc = ViewUtil.newSwitchItemView(getContext(), "复制视频描述");

        mCommonFrameLayout.addContent(sivRemoveAd);
        mCommonFrameLayout.addContent(sivDisableUpdate);
        mCommonFrameLayout.addContent(sivCopyVideoDesc);

        return mCommonFrameLayout;
    }

    @Override
    protected void initView(View view, Bundle args) {

        mToolbar.setTitle("其他设置");
        mToolbar.showBack();
        mToolbar.setOnBackEventListener(this);

        // 设置图标
        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_clear))
                .into(mToolbar.getBackView());

        trackBind(sivRemoveAd, Constant.Preference.REMOVE_AD, false, mBooleanChangeListener);
        trackBind(sivDisableUpdate, Constant.Preference.DISABLE_UPDATE, false, mBooleanChangeListener);
        trackBind(sivCopyVideoDesc, Constant.Preference.COPY_VIDEO_DESC, false, mBooleanChangeListener);
    }

    @Override
    public void onEvent(View view) {
        // 退出
        dismiss();
    }

    private TrackViewStatus.StatusChangeListener<Boolean> mBooleanChangeListener = new TrackViewStatus.StatusChangeListener<Boolean>() {
        @Override
        public boolean onStatusChange(View view, String key, Boolean value) {

            switch (key) {
                case Constant.Preference.DISABLE_UPDATE:
                    if (value) {
                        // 清除更新数据(抖音自带的)
                        SharedPreferences preferences = getSharedPreferences("update_info");
                        preferences.edit().clear().apply();
                    }
                    break;
            }
            sendRefreshPreferenceBroadcast(key, value);
            return true;
        }
    };
}

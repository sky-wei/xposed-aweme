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
import com.sky.xposed.aweme.ui.base.BaseDialog;
import com.sky.xposed.common.ui.interfaces.TrackViewStatus;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.EditTextItemView;
import com.sky.xposed.common.ui.view.SwitchItemView;
import com.sky.xposed.common.ui.view.TitleView;
import com.sky.xposed.common.util.ConversionUtil;
import com.sky.xposed.common.util.ToastUtil;

/**
 * Created by sky on 18-6-9.
 */
public class MoreSettingsDialog extends BaseDialog {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;

    private EditTextItemView mRecordVideoTime;
    private SwitchItemView sivRemoveAd;
    private SwitchItemView sivDisableUpdate;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {

        // 不显示默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCommonFrameLayout = new CommonFrameLayout(getContext());
        mToolbar = mCommonFrameLayout.getTitleView();

        mRecordVideoTime = new EditTextItemView(getContext());
        mRecordVideoTime.setName("视频最大限制时间");
        mRecordVideoTime.setExtendHint("未设置");
        mRecordVideoTime.setUnit("秒");
        mRecordVideoTime.setInputType(com.sky.xposed.common.Constant.InputType.NUMBER_SIGNED);

        sivRemoveAd = ViewUtil.newSwitchItemView(getContext(), "移除抖音广告");
        sivDisableUpdate = ViewUtil.newSwitchItemView(getContext(), "禁用抖音更新");

        mCommonFrameLayout.addContent(mRecordVideoTime, true);
        mCommonFrameLayout.addContent(sivRemoveAd, true);
        mCommonFrameLayout.addContent(sivDisableUpdate);

        return mCommonFrameLayout;
    }

    @Override
    protected void initView(View view, Bundle args) {

        mToolbar.setTitle("更多设置");

        trackBind(mRecordVideoTime, Constant.Preference.RECORD_VIDEO_TIME,
                Integer.toString(Constant.DefaultValue.RECORD_VIDEO_TIME), mStringChangeListener);
        trackBind(sivRemoveAd, Constant.Preference.REMOVE_AD, false, mBooleanChangeListener);
        trackBind(sivDisableUpdate, Constant.Preference.DISABLE_UPDATE, false, mBooleanChangeListener);
    }

    private TrackViewStatus.StatusChangeListener mStringChangeListener = new TrackViewStatus.StatusChangeListener<String>() {

        @Override
        public boolean onStatusChange(View view, String key, String value) {

            switch (key) {
                case Constant.Preference.RECORD_VIDEO_TIME:

                    int recordTime = ConversionUtil.parseInt(value);

                    if (recordTime <= 0) {
                        ToastUtil.show("设置的最大录制视频时间无效，请重新设置");
                        return false;
                    }

                    if (recordTime > 60) {
                        ToastUtil.show("设置时间值过大，请慎重！");
                    }
                    break;
            }
            sendRefreshPreferenceBroadcast(key, value);
            return true;
        }
    };

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

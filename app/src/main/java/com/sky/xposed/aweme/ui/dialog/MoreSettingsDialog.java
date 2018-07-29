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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.ui.base.BaseDialogFragment;
import com.sky.xposed.aweme.ui.interfaces.TrackViewStatus;
import com.sky.xposed.aweme.ui.util.ViewUtil;
import com.sky.xposed.aweme.ui.view.CommonFrameLayout;
import com.sky.xposed.aweme.ui.view.EditTextItemView;
import com.sky.xposed.aweme.ui.view.SwitchItemView;
import com.sky.xposed.aweme.ui.view.TitleView;
import com.sky.xposed.aweme.util.ConversionUtil;
import com.sky.xposed.aweme.util.VToast;

/**
 * Created by sky on 18-6-9.
 */
public class MoreSettingsDialog extends BaseDialogFragment {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;

    private EditTextItemView mRecordVideoTime;
    private SwitchItemView sivRemoveAd;

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
        mRecordVideoTime.setInputType(Constant.InputType.NUMBER_SIGNED);

        sivRemoveAd = ViewUtil.newSwitchItemView(getContext(), "移除列表广告");

        mCommonFrameLayout.addContent(mRecordVideoTime);
        mCommonFrameLayout.addContent(sivRemoveAd);

        return mCommonFrameLayout;
    }

    @Override
    protected void initView(View view, Bundle args) {

        mToolbar.setTitle("更多设置");

        trackBind(mRecordVideoTime, Constant.Preference.RECORD_VIDEO_TIME,
                Integer.toString(Constant.DefaultValue.RECORD_VIDEO_TIME), mStringChangeListener);
        trackBind(sivRemoveAd, Constant.Preference.REMOVE_AD, false, mBooleanChangeListener);
    }

    private TrackViewStatus.StatusChangeListener mStringChangeListener = new TrackViewStatus.StatusChangeListener<String>() {

        @Override
        public boolean onStatusChange(View view, String key, String value) {

            switch (key) {
                case Constant.Preference.RECORD_VIDEO_TIME:

                    int recordTime = ConversionUtil.parseInt(value);

                    if (recordTime <= 0) {
                        VToast.show("设置的最大录制视频时间无效，请重新设置");
                        return false;
                    }

                    if (recordTime > 60) {
                        VToast.show("设置时间值过大，请慎重！");
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
            sendRefreshPreferenceBroadcast(key, value);
            return true;
        }
    };
}

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
import com.sky.xposed.aweme.R;
import com.sky.xposed.aweme.ui.base.BaseDialog;
import com.sky.xposed.aweme.ui.util.UriUtil;
import com.sky.xposed.aweme.util.PlayUtil;
import com.sky.xposed.common.ui.interfaces.TrackViewStatus;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.EditTextItemView;
import com.sky.xposed.common.ui.view.SpinnerItemView;
import com.sky.xposed.common.ui.view.TitleView;
import com.sky.xposed.common.util.ConversionUtil;
import com.sky.xposed.common.util.ToastUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by sky on 2018/8/22.
 */
public class PlayDialog extends BaseDialog implements TitleView.OnBackEventListener {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;

    private SpinnerItemView mAutoPlayType;
    private EditTextItemView mAutoPlaySleepTime;

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {

        // 不显示默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCommonFrameLayout = new CommonFrameLayout(getContext());
        mToolbar = mCommonFrameLayout.getTitleView();

        mAutoPlayType = ViewUtil.newSpinnerItemView(getContext(),
                "播放方式", "",
                PlayUtil.getPlayTypeName(Constant.PlayType.TDEFAULT),
                PlayUtil.getPlayTypeName(Constant.PlayType.TIMING));

        mAutoPlaySleepTime = new EditTextItemView(getContext());
        mAutoPlaySleepTime.setName("播放休眠时间");
        mAutoPlaySleepTime.setExtendHint("未设置");
        mAutoPlaySleepTime.setUnit("秒");
        mAutoPlaySleepTime.setInputType(com.sky.xposed.common.Constant.InputType.NUMBER_SIGNED);

        mCommonFrameLayout.addContent(mAutoPlayType);
        mCommonFrameLayout.addContent(mAutoPlaySleepTime);

        return mCommonFrameLayout;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mToolbar.setTitle("播放设置");
        mToolbar.showBack();
        mToolbar.setOnBackEventListener(this);

        // 设置图标
        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_clear))
                .into(mToolbar.getBackView());

        String value = trackBind(mAutoPlayType, Constant.Preference.AUTO_PLAY_TYPE_NAME,
                PlayUtil.getPlayTypeName(Constant.PlayType.TDEFAULT), mStringChangeListener);
        trackBind(mAutoPlaySleepTime, Constant.Preference.AUTO_PLAY_SLEEP_TIME,
                Integer.toString(Constant.DefaultValue.AUTO_PLAY_SLEEP_TIME), mStringChangeListener);

        // 获取类型
        int tValue = PlayUtil.getPlayType(value);

        // 设置状态
        ViewUtil.setVisibility(mAutoPlaySleepTime,
                tValue == Constant.PlayType.TIMING ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEvent(View view) {
        // 退出
        dismiss();
    }

    private TrackViewStatus.StatusChangeListener mStringChangeListener = new TrackViewStatus.StatusChangeListener<String>() {

        @Override
        public boolean onStatusChange(View view, String key, String value) {

            switch (key) {
                case Constant.Preference.AUTO_PLAY_TYPE_NAME:

                    String tKey = Constant.Preference.AUTO_PLAY_TYPE;
                    int tValue = PlayUtil.getPlayType(value);

                    // 单独设置下
                    getDefaultSharedPreferences().edit().putInt(tKey, tValue).apply();
                    // 发送更新信息
                    sendRefreshPreferenceBroadcast(tKey, tValue);

                    // 设置状态
                    ViewUtil.setVisibility(mAutoPlaySleepTime,
                            tValue == Constant.PlayType.TIMING ? View.VISIBLE : View.GONE);
                    break;
                case Constant.Preference.AUTO_PLAY_SLEEP_TIME:

                    int sleepTime = ConversionUtil.parseInt(value);

                    if (sleepTime <= 5) {
                        ToastUtil.show("设置的休眠数不能少于5秒，请重新设置");
                        return false;
                    }
                    break;
            }
            sendRefreshPreferenceBroadcast(key, value);
            return true;
        }
    };
}

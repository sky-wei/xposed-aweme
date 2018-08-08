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

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.ui.base.BaseDialog;
import com.sky.xposed.aweme.ui.util.DialogUtil;
import com.sky.xposed.aweme.util.DonateUtil;
import com.sky.xposed.common.ui.interfaces.TrackViewStatus;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.SimpleItemView;
import com.sky.xposed.common.ui.view.SwitchItemView;
import com.sky.xposed.common.ui.view.TitleView;

public class SettingsDialog extends BaseDialog {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;

    private SwitchItemView sivAutoPlay;
    private SwitchItemView sivAutoAttention;
    private SwitchItemView sivAutoLike;
    private SwitchItemView sivAutoComment;
    private SimpleItemView etiAutoCommentList;
    private SwitchItemView sivAutoSaveVideo;
    private SwitchItemView sivRemoveLimit;
    private SimpleItemView sivMoreSettings;
    private SimpleItemView sivDonate;
    private SimpleItemView sivAliPayHb;
    private SimpleItemView sivAbout;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {

        // 不显示默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCommonFrameLayout = new CommonFrameLayout(getContext());
        mToolbar = mCommonFrameLayout.getTitleView();

        sivAutoPlay = ViewUtil.newSwitchItemView(getContext(), "自动播放");
        sivAutoAttention = ViewUtil.newSwitchItemView(getContext(), "自动关注");
        sivAutoLike = ViewUtil.newSwitchItemView(getContext(), "自动点赞");
        sivAutoComment = ViewUtil.newSwitchItemView(getContext(), "自动评论");
        etiAutoCommentList = ViewUtil.newSimpleItemView(getContext(), "评论内容");
        sivRemoveLimit = ViewUtil.newSwitchItemView(getContext(), "解除视频时间限制");
        sivMoreSettings = ViewUtil.newSimpleItemView(getContext(), "更多设置");
        sivDonate = ViewUtil.newSimpleItemView(getContext(), "支持我们");
        sivAliPayHb = ViewUtil.newSimpleItemView(getContext(), "领取红包(每日一次)");
        sivAbout = ViewUtil.newSimpleItemView(getContext(), "关于");

        sivAutoSaveVideo = ViewUtil.newSwitchItemView(getContext(), "自动保存视频");

        mCommonFrameLayout.addContent(sivAutoPlay, true);
        mCommonFrameLayout.addContent(sivAutoAttention, true);
        mCommonFrameLayout.addContent(sivAutoLike, true);
        mCommonFrameLayout.addContent(sivAutoComment, true);
        mCommonFrameLayout.addContent(etiAutoCommentList, true);
        mCommonFrameLayout.addContent(sivAutoSaveVideo, true);
        mCommonFrameLayout.addContent(sivRemoveLimit, true);
        mCommonFrameLayout.addContent(sivMoreSettings, true);
        mCommonFrameLayout.addContent(sivDonate, true);
        mCommonFrameLayout.addContent(sivAliPayHb, true);
        mCommonFrameLayout.addContent(sivAbout);

        return mCommonFrameLayout;
    }

    @Override
    protected void initView(View view, Bundle args) {

        mToolbar.setTitle(Constant.Name.TITLE);

        // 绑定事件
        trackBind(sivAutoPlay, Constant.Preference.AUTO_PLAY, false, mBooleanChangeListener);
        trackBind(sivAutoAttention, Constant.Preference.AUTO_ATTENTION, false, mBooleanChangeListener);
        trackBind(sivAutoLike, Constant.Preference.AUTO_LIKE, false, mBooleanChangeListener);
        trackBind(sivAutoComment, Constant.Preference.AUTO_COMMENT, false, mBooleanChangeListener);
        trackBind(sivAutoSaveVideo, Constant.Preference.AUTO_SAVE_VIDEO, false, mBooleanChangeListener);
        trackBind(sivRemoveLimit, Constant.Preference.REMOVE_LIMIT, false, mBooleanChangeListener);

        sivAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示关于
                DialogUtil.showAboutDialog(getContext());
            }
        });

        etiAutoCommentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示评论列表
                CommentListDialog commonListDialog = new CommentListDialog();
                commonListDialog.show(getFragmentManager(), "commonList");
            }
        });

        sivMoreSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更多设置
                MoreSettingsDialog moreSettingsDialog = new MoreSettingsDialog();
                moreSettingsDialog.show(getFragmentManager(), "moreSettings");
            }
        });

        sivDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 捐赠
                DonateDialog donateDialog = new DonateDialog();
                donateDialog.show(getFragmentManager(), "donate");
            }
        });

        sivAliPayHb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 领取红包
                DonateUtil.receiveAliPayHb(getContext());
            }
        });


        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                // 显示红包提示
                DonateUtil.showHbDialog(getContext(), getDefaultSharedPreferences());
            }
        });
    }



    private TrackViewStatus.StatusChangeListener<Boolean> mBooleanChangeListener = new TrackViewStatus.StatusChangeListener<Boolean>() {
        @Override
        public boolean onStatusChange(View view, String key, Boolean value) {
            sendRefreshPreferenceBroadcast(key, value);
            return true;
        }
    };
}

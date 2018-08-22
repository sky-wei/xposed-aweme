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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.R;
import com.sky.xposed.aweme.ui.base.BaseDialog;
import com.sky.xposed.aweme.ui.util.DialogUtil;
import com.sky.xposed.aweme.ui.util.UriUtil;
import com.sky.xposed.aweme.util.DonateUtil;
import com.sky.xposed.common.ui.interfaces.TrackViewStatus;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.SimpleItemView;
import com.sky.xposed.common.ui.view.SwitchItemView;
import com.sky.xposed.common.ui.view.TitleView;
import com.squareup.picasso.Picasso;

public class SettingsDialog extends BaseDialog implements View.OnClickListener {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;
    private ImageButton mMoreButton;

    private SwitchItemView sivAutoPlay;
    private SimpleItemView sivAutoPlaySettings;
    private SwitchItemView sivAutoAttention;
    private SwitchItemView sivAutoLike;
    private SwitchItemView sivAutoComment;
    private SimpleItemView etiAutoCommentList;
    private SwitchItemView sivAutoSaveVideo;
    private SwitchItemView sivRemoveLimit;
    private SimpleItemView sivRemoveLimitSettings;
    private SimpleItemView sivMoreSettings;
    private SimpleItemView sivDonate;
    private SimpleItemView sivAliPayHb;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {

        // 不显示默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCommonFrameLayout = new CommonFrameLayout(getContext());
        mToolbar = mCommonFrameLayout.getTitleView();
        mMoreButton = mToolbar.addMoreImageButton();

        sivAutoPlay = ViewUtil.newSwitchItemView(getContext(), "自动播放", "提供两种播放形式");
        sivAutoPlaySettings = ViewUtil.newSimpleItemView(getContext(), "播放设置");
        sivAutoAttention = ViewUtil.newSwitchItemView(getContext(), "自动关注", "切换视频时自动关注");
        sivAutoLike = ViewUtil.newSwitchItemView(getContext(), "自动点赞", "切换视频时自动点赞");
        sivAutoComment = ViewUtil.newSwitchItemView(getContext(), "自动评论", "切换视频时自动评论");
        etiAutoCommentList = ViewUtil.newSimpleItemView(getContext(), "评论内容");
        sivAutoSaveVideo = ViewUtil.newSwitchItemView(getContext(), "自动保存视频", "切换视频时自动保存视频");
        sivRemoveLimit = ViewUtil.newSwitchItemView(getContext(), "解除视频限制", "解除上传与录制15秒限制");
        sivRemoveLimitSettings = ViewUtil.newSimpleItemView(getContext(), "视频时间设置");
        sivMoreSettings = ViewUtil.newSimpleItemView(getContext(), "其他设置");
        sivDonate = ViewUtil.newSimpleItemView(getContext(), "支持我们");
        sivAliPayHb = ViewUtil.newSimpleItemView(getContext(), "领取红包(每日一次)");

        mCommonFrameLayout.addContent(sivAutoPlay);
        mCommonFrameLayout.addContent(sivAutoPlaySettings);
        mCommonFrameLayout.addContent(sivAutoAttention);
        mCommonFrameLayout.addContent(sivAutoLike);
        mCommonFrameLayout.addContent(sivAutoComment);
        mCommonFrameLayout.addContent(etiAutoCommentList);
        mCommonFrameLayout.addContent(sivAutoSaveVideo);
        mCommonFrameLayout.addContent(sivRemoveLimit);
        mCommonFrameLayout.addContent(sivRemoveLimitSettings);
        mCommonFrameLayout.addContent(sivMoreSettings);
        mCommonFrameLayout.addContent(sivDonate);
        mCommonFrameLayout.addContent(sivAliPayHb);

        return mCommonFrameLayout;
    }

    @Override
    protected void initView(View view, Bundle args) {

        mToolbar.setTitle(Constant.Name.TITLE);

        Picasso.get()
                .load(UriUtil.getResource(R.drawable.ic_action_more_vert))
                .into(mMoreButton);

        // 绑定事件
        boolean autoPlay = trackBind(sivAutoPlay,
                Constant.Preference.AUTO_PLAY, false, mBooleanChangeListener);
        trackBind(sivAutoAttention, Constant.Preference.AUTO_ATTENTION, false, mBooleanChangeListener);
        trackBind(sivAutoLike, Constant.Preference.AUTO_LIKE, false, mBooleanChangeListener);
        boolean autoComment = trackBind(sivAutoComment,
                Constant.Preference.AUTO_COMMENT, false, mBooleanChangeListener);
        trackBind(sivAutoSaveVideo, Constant.Preference.AUTO_SAVE_VIDEO, false, mBooleanChangeListener);
        boolean removeLimit = trackBind(sivRemoveLimit,
                Constant.Preference.REMOVE_LIMIT, false, mBooleanChangeListener);

        // 设置显示或隐藏
        ViewUtil.setVisibility(sivAutoPlaySettings, autoPlay ? View.VISIBLE : View.GONE);
        ViewUtil.setVisibility(etiAutoCommentList, autoComment ? View.VISIBLE : View.GONE);
        ViewUtil.setVisibility(sivRemoveLimitSettings, removeLimit ? View.VISIBLE : View.GONE);

        // 添加事件监听
        mMoreButton.setOnClickListener(this);
        sivAutoPlaySettings.setOnClickListener(this);
        etiAutoCommentList.setOnClickListener(this);
        sivMoreSettings.setOnClickListener(this);
        sivRemoveLimitSettings.setOnClickListener(this);
        sivDonate.setOnClickListener(this);
        sivAliPayHb.setOnClickListener(this);

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                // 显示红包提示
                DonateUtil.showHbDialog(getContext(), getDefaultSharedPreferences());
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (mMoreButton == v) {
            // 显示更多菜单
            showMoreMenu();
        } else if (sivAutoPlaySettings == v) {
            // 自动播放设置
            PlayDialog playDialog = new PlayDialog();
            playDialog.show(getFragmentManager(), "playDialog");
        } else if (etiAutoCommentList == v) {
            // 显示评论列表
            CommentListDialog commonListDialog = new CommentListDialog();
            commonListDialog.show(getFragmentManager(), "commonList");
        } else if (sivMoreSettings == v) {
            // 更多设置
            OtherDialog moreSettingsDialog = new OtherDialog();
            moreSettingsDialog.show(getFragmentManager(), "otherSettings");
        } else if (sivDonate == v) {
            // 捐赠
            DonateDialog donateDialog = new DonateDialog();
            donateDialog.show(getFragmentManager(), "donate");
        } else if (sivAliPayHb == v) {
            // 领取红包
            DonateUtil.receiveAliPayHb(getContext());
        } else if (sivRemoveLimitSettings == v) {
            // 视频设置
            LimitDialog limitSettingsDialog = new LimitDialog();
            limitSettingsDialog.show(getFragmentManager(), "limitSettings");
        }
    }

    /**
     * 显示更多菜单
     */
    private void showMoreMenu() {

        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), mMoreButton, Gravity.RIGHT);
        Menu menu = popupMenu.getMenu();

        menu.add(1, 1, 1, "关于");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 显示关于
                DialogUtil.showAboutDialog(getContext());
                return true;
            }
        });

        popupMenu.show();
    }

    private TrackViewStatus.StatusChangeListener<Boolean> mBooleanChangeListener = new TrackViewStatus.StatusChangeListener<Boolean>() {
        @Override
        public boolean onStatusChange(View view, String key, Boolean value) {

            if (Constant.Preference.AUTO_PLAY.equals(key)) {
                // 设置显示或隐藏
                ViewUtil.setVisibility(sivAutoPlaySettings, value ? View.VISIBLE : View.GONE);
            } else if (Constant.Preference.AUTO_COMMENT.equals(key)) {
                // 设置显示或隐藏
                ViewUtil.setVisibility(etiAutoCommentList, value ? View.VISIBLE : View.GONE);
            } else if (Constant.Preference.REMOVE_LIMIT.equals(key)) {
                // 设置显示或隐藏
                ViewUtil.setVisibility(sivRemoveLimitSettings, value ? View.VISIBLE : View.GONE);
            }
            sendRefreshPreferenceBroadcast(key, value);
            return true;
        }
    };
}

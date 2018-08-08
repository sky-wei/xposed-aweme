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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.ui.base.BaseDialog;
import com.sky.xposed.common.ui.adapter.CommentListAdapter;
import com.sky.xposed.common.ui.util.LayoutUtil;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.TitleView;
import com.sky.xposed.common.util.DisplayUtil;
import com.sky.xposed.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sky on 18-6-9.
 */
public class CommentListDialog extends BaseDialog implements
        SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    private TitleView mToolbar;
    private CommonFrameLayout mCommonFrameLayout;
    private Button mAddCommonButton;
    private SwipeMenuListView mSwipeMenuListView;

    private CommentListAdapter mCommentListAdapter;
    private ArrayList<String> mCommentList = new ArrayList<>();
    private boolean mSaveCommentList = false;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {

        // 不显示默认标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mCommonFrameLayout = new CommonFrameLayout(getContext());
        mToolbar = mCommonFrameLayout.getTitleView();

        LinearLayout layout = LayoutUtil.newCommonLayout(getContext());

        FrameLayout headLayout = new FrameLayout(getContext());
        headLayout.setLayoutParams(LayoutUtil.newFrameLayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        TextView tvTips = new TextView(getContext());
        tvTips.setTextColor(0xffafafaf);
        tvTips.setTextSize(10f);
        tvTips.setText("提示:单击编辑左滑可删除");

        mAddCommonButton = new Button(getContext());
        mAddCommonButton.setText("添加");
        mAddCommonButton.setTextSize(14f);
        mAddCommonButton.setTextColor(0xFFF93F25);
        mAddCommonButton.setBackgroundColor(0x00000000);

        FrameLayout.LayoutParams tipsParams = LayoutUtil.newWrapFrameLayoutParams();
        tipsParams.leftMargin = DisplayUtil.dip2px(getContext(), 15f);
        tipsParams.gravity = Gravity.CENTER_VERTICAL;

        FrameLayout.LayoutParams params = LayoutUtil.newFrameLayoutParams(
                DisplayUtil.dip2px(getContext(), 70f), DisplayUtil.dip2px(getContext(), 40f));
        params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        headLayout.addView(tvTips, tipsParams);
        headLayout.addView(mAddCommonButton, params);

        mSwipeMenuListView = new SwipeMenuListView(getContext());
        mSwipeMenuListView.setCacheColorHint(0x00000000);
        mSwipeMenuListView.setDividerHeight(0);
        mSwipeMenuListView.setMenuCreator(newMenuCreator());
        mSwipeMenuListView.setCloseInterpolator(new BounceInterpolator());
        mSwipeMenuListView.setLayoutParams(LayoutUtil.newMatchLinearLayoutParams());

        layout.addView(headLayout);
        layout.addView(mSwipeMenuListView);

        mCommonFrameLayout.setContent(layout);

        return mCommonFrameLayout;
    }

    @Override
    protected void initView(View view, Bundle args) {

        mToolbar.setTitle("评论内容列表");

        mCommentListAdapter = new CommentListAdapter(getContext());
        mCommentListAdapter.setItems(mCommentList);

        mAddCommonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 添加
                showEditDialog("新添加评论", "", new OnEditListener() {
                    @Override
                    public void onTextChange(String value) {

                        if (TextUtils.isEmpty(value)) {
                            // 异常情况
                            ToastUtil.show("无法添加空评论!");
                        } else {
                            // 添加到列表中
                            mSaveCommentList = true;
                            mCommentList.add(value);
                            mCommentListAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        mSwipeMenuListView.setOnItemClickListener(this);
        mSwipeMenuListView.setOnMenuItemClickListener(this);
        mSwipeMenuListView.setAdapter(mCommentListAdapter);

        // 设置加载的评论信息
        mCommentList.clear();
        mCommentList.addAll(loadUserComment());
        mCommentListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mSaveCommentList) {
            // 保存评论列表
            saveUserComment(mCommentList);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        showEditDialog("编辑评论", mCommentList.get(position), new OnEditListener() {
            @Override
            public void onTextChange(String value) {

                if (TextUtils.isEmpty(value)) {
                    // 异常情况
                    ToastUtil.show("编辑的评论不能为空!");
                } else {
                    // 添加到列表中
                    mSaveCommentList = true;
                    mCommentList.set(position, value);
                    mCommentListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

        // 删除指定评论
        mSaveCommentList = true;
        mCommentList.remove(position);
        mCommentListAdapter.notifyDataSetChanged();

        return true;
    }

    /**
     * 加载用户评论
     */
    private List<String> loadUserComment() {

        Set<String> commentSet = getDefaultSharedPreferences()
                .getStringSet(Constant.Preference.AUTO_COMMENT_LIST, new HashSet<String>());

        return new ArrayList<>(commentSet);
    }

    /**
     * 保存用户评论
     */
    private void saveUserComment(List<String> commentList) {

        Set<String> commentSet = new HashSet<>(commentList);

        getDefaultSharedPreferences()
                .edit()
                .putStringSet(Constant.Preference.AUTO_COMMENT_LIST, commentSet)
                .apply();

        // 发送修改广播
        sendRefreshPreferenceBroadcast(Constant.Preference.AUTO_COMMENT_LIST, commentSet);
    }

    /**
     * 显示编辑的Dialog提示框
     */
    private void showEditDialog(String title, String content, final OnEditListener listener) {

        int top = DisplayUtil.dip2px(getContext(), 10f);
        int left = DisplayUtil.dip2px(getContext(), 24f);

        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(LayoutUtil.newFrameLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.setPadding(left, top, left, top);

        final EditText editText = new EditText(getContext());
        editText.setText(content);
        editText.setLayoutParams(LayoutUtil.newViewGroupParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewUtil.setInputType(editText, com.sky.xposed.common.Constant.InputType.TEXT);
        frameLayout.addView(editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setView(frameLayout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 返回文本的内容
                listener.onTextChange(editText.getText().toString());
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 创建左滑菜单
     */
    private SwipeMenuCreator newMenuCreator() {

        return new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());

                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(DisplayUtil.dip2px(getContext(), 80f));
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(14);
                deleteItem.setTitleColor(Color.WHITE);

                menu.addMenuItem(deleteItem);
            }
        };
    }

    private interface OnEditListener {

        void onTextChange(String value);
    }
}

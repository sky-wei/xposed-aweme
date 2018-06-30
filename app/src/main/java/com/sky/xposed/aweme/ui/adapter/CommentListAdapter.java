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

package com.sky.xposed.aweme.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.xposed.aweme.ui.base.BaseListAdapter;
import com.sky.xposed.aweme.ui.view.CommentItemView;

/**
 * Created by sky on 18-6-10.
 */
public class CommentListAdapter extends BaseListAdapter<String> {

    public CommentListAdapter(Context context) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return new CommentItemView(getContext());
    }

    @Override
    public ViewHolder<String> onCreateViewHolder(View view, int viewType) {
        return new CommentViewHolder(view, this);
    }

    private final class CommentViewHolder extends ViewHolder<String> {

        CommentItemView mCommentItemView;

        public CommentViewHolder(View itemView, BaseListAdapter<String> baseListAdapter) {
            super(itemView, baseListAdapter);
        }

        @Override
        public void onInitialize() {
            super.onInitialize();

            mCommentItemView = (CommentItemView) mItemView;
        }

        @Override
        public void onBind(int position, int viewType) {

            // 设置内容
            mCommentItemView.setContent(getItem(position));
        }
    }
}

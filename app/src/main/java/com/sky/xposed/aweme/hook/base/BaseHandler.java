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

package com.sky.xposed.aweme.hook.base;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.sky.xposed.aweme.data.ObjectManager;
import com.sky.xposed.aweme.data.UserConfigManager;
import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.aweme.hook.VersionManager;
import com.sky.xposed.common.data.CachePreferences;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.ResourceUtil;

public abstract class BaseHandler implements Runnable {

    private HookManager mHookManager;

    protected Context mContext;
    protected CachePreferences mCachePreferences;
    protected UserConfigManager mUserConfigManager;
    protected ObjectManager mObjectManager;
    protected VersionManager mVersionManager;
    protected Handler mHandler;

    public BaseHandler(HookManager hookManager) {
        mHookManager = hookManager;
        mContext = mHookManager.getContext();
        mCachePreferences = mHookManager.getCachePreferences();
        mUserConfigManager = mHookManager.getUserConfigManager();
        mObjectManager = mHookManager.getObjectManager();
        mVersionManager = mHookManager.getVersionManager();
        mHandler = mHookManager.getHandler();
    }

    @Override
    public void run() {

        try {
            // 开始处理
            onHandler();
        } catch (Throwable tr) {
            Alog.e("处理异常", tr);
        }
    }

    public abstract void onHandler() throws Exception;

    public View findViewById(View view, String id) {

        if (view == null) return null;

        return view.findViewById(ResourceUtil.getId(mContext, id));
    }

    public void mainPerformClick(final View viewGroup, final String id) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                performClick(viewGroup, id);
            }
        });
    }

    public void mainPerformClick(final View view) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                performClick(view);
            }
        });
    }

    public void performClick(View viewGroup, String id) {

        if (viewGroup == null) return;

        Context context = viewGroup.getContext();
        performClick(viewGroup.findViewById(ResourceUtil.getId(context, id)));
    }

    public void performClick(View view) {

        if (view != null && view.isShown()) {
            // 点击
            view.performClick();
        }
    }
}

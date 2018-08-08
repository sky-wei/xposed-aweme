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

package com.sky.xposed.aweme.hook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.sky.xposed.aweme.BuildConfig;
import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.data.ObjectManager;
import com.sky.xposed.aweme.data.UserConfigManager;
import com.sky.xposed.common.data.CachePreferences;
import com.sky.xposed.common.helper.ReceiverHelper;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.Pair;
import com.sky.xposed.common.util.ToastUtil;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookManager implements ReceiverHelper.ReceiverCallback {

    private Context mContext;
    private Handler mHandler;
    private XC_LoadPackage.LoadPackageParam mLoadPackageParam;
    private CachePreferences mCachePreferences;
    private UserConfigManager mUserConfigManager;
    private ObjectManager mObjectManager;
    private VersionManager mVersionManager;
    private ReceiverHelper mReceiverHelper;

    private AweMeHook mAweMeHook;

    private static final HookManager HOOK_MANAGER = new HookManager();

    private HookManager() {
    }

    public static HookManager getInstance() {
        return HOOK_MANAGER;
    }

    public HookManager initialization(Context context, XC_LoadPackage.LoadPackageParam param) {

        if (mContext != null) {
            // 不需要再初始化了
            return this;
        }

        // 调试开关
        Alog.setDebug(BuildConfig.DEBUG);

        mContext = context;
        mHandler = new AppHandler();
        mLoadPackageParam = param;
        mCachePreferences = new CachePreferences(context, Constant.Name.AWE_ME);
        mUserConfigManager = new UserConfigManager(this);
        mObjectManager = new ObjectManager();
        mVersionManager = new VersionManager(this);
        mReceiverHelper = new ReceiverHelper(context,
                this, com.sky.xposed.common.Constant.Action.REFRESH_PREFERENCE);

        ToastUtil.getInstance().init(context);
        Picasso.setSingletonInstance(new Picasso.Builder(context).build());

        // 添加统计
        CrashReport.initCrashReport(mContext, "5a1f1ac8fe", BuildConfig.DEBUG);
        CrashReport.setAppChannel(mContext, BuildConfig.FLAVOR);

        // 注册事件
        mReceiverHelper.registerReceiver();

        return this;
    }

    public void handleLoadPackage() {

        if (mAweMeHook != null) return;

        if (mVersionManager.isSupportVersion()) {
            // 处理
            mAweMeHook = new AweMeHook();
            mAweMeHook.handleLoadPackage(getLoadPackageParam());
        }
    }

    public void release() {

        // 释放事件
        mReceiverHelper.unregisterReceiver();
    }

    public Context getContext() {
        return mContext;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public XC_LoadPackage.LoadPackageParam getLoadPackageParam() {
        return mLoadPackageParam;
    }

    public CachePreferences getCachePreferences() {
        return mCachePreferences;
    }

    public UserConfigManager getUserConfigManager() {
        return mUserConfigManager;
    }

    public ObjectManager getObjectManager() {
        return mObjectManager;
    }

    public VersionManager getVersionManager() {
        return mVersionManager;
    }

    @Override
    public void onReceive(String action, Intent intent) {

        if (com.sky.xposed.common.Constant.Action.REFRESH_PREFERENCE.equals(action)) {

            // 获取刷新的值
            List<Pair<String, Object>> data = (ArrayList<Pair<String, Object>>)
                    intent.getSerializableExtra(com.sky.xposed.common.Constant.Key.DATA);

            if (data == null) return ;

            for (Pair<String, Object> pair : data) {
                // 重新设置值
                mCachePreferences.putObject(pair.first, pair.second);
                // 通知值修改了
                mAweMeHook.onModifyValue(pair.first, pair.second);
            }
        }
    }

    private final class AppHandler extends Handler {
    }
}

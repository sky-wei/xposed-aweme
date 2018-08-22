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

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.xposed.aweme.BuildConfig;
import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.R;
import com.sky.xposed.aweme.hook.base.BaseHook;
import com.sky.xposed.aweme.hook.handler.AutoAttentionHandler;
import com.sky.xposed.aweme.hook.handler.AutoCommentHandler;
import com.sky.xposed.aweme.hook.handler.AutoDownloadHandler;
import com.sky.xposed.aweme.hook.handler.AutoLikeHandler;
import com.sky.xposed.aweme.hook.handler.AutoPlayHandler;
import com.sky.xposed.aweme.ui.dialog.SettingsDialog;
import com.sky.xposed.aweme.util.ToStringUtil;
import com.sky.xposed.common.ui.util.LayoutUtil;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.DisplayUtil;
import com.sky.xposed.common.util.ResourceUtil;
import com.sky.xposed.javax.MethodHook;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class AweMeHook extends BaseHook {

    private AutoPlayHandler mAutoPlayHandler;
    private AutoLikeHandler mAutoLikeHandler;
    private AutoAttentionHandler mAutoAttentionHandler;
    private AutoCommentHandler mAutoCommentHandler;
    private AutoDownloadHandler mAutoDownloadHandler;

    private XC_MethodHook.Unhook mCutVideoUnhook;

    private VersionManager.Config mVersionConfig = mVersionManager.getSupportConfig();

    @Override
    public void onHandleLoadPackage(XC_LoadPackage.LoadPackageParam param) {

        mAutoPlayHandler = new AutoPlayHandler(mHookManager);
        mAutoLikeHandler = new AutoLikeHandler(mHookManager);
        mAutoAttentionHandler = new AutoAttentionHandler(mHookManager);
        mAutoCommentHandler = new AutoCommentHandler(mHookManager);
        mAutoDownloadHandler = new AutoDownloadHandler(mHookManager);

        if (BuildConfig.DEBUG) testHook();

        // 自动播放Hook
        autoPlayHook();

        // 视频切换Hook
        videoSwitchHook();

        // 设置入口Hook
        settingsHook();

        // 保存视频Hook
        saveVideoHook();

        // 移除视频限制
        removeLimitHook();

        // 移除广告Hook
        removeAdHook();

        // 更新Hook
        updateHook();
    }

    public void onModifyValue(String key, Object value) {

        if (Constant.Preference.AUTO_PLAY.equals(key)) {
            // 设置自动播放
//            ToastUtil.show("播放完当前视频后将自动播放下一个视频！");
            mAutoPlayHandler.setAutoPlay((Boolean) value);
        } else if (Constant.Preference.AUTO_PLAY_TYPE.equals(key)) {
            // 切换播放类型
            mAutoPlayHandler.switchPlayType((Integer) value);
        }
    }

    /**
     * 自动播放Hook方法
     */
    private void autoPlayHook() {

        findMethod(
                mVersionConfig.classBaseListFragment,
                mVersionConfig.methodOnResume)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam param) {
                        // 保存当前对象
                        mObjectManager.setViewPager(XposedHelpers.getObjectField(
                                param.thisObject, mVersionConfig.fieldMViewPager));

                        // 开始播放
                        mAutoPlayHandler.start();
                    }
                });

        findMethod(
                mVersionConfig.classBaseListFragment,
                mVersionConfig.methodOnPause)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam methodHookParam) {
                        // 重置对象
                        mObjectManager.setViewPager(null);

                        // 停止播放
                        mAutoPlayHandler.stop();
                    }
                });

        findMethod(
                mVersionConfig.classHomeChange,
                mVersionConfig.methodHomeChange,
                String.class)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam param) {
                        // 获取Tab切换的名称
                        String name = (String) param.args[0];
                        mAutoPlayHandler.setAutoPlay("HOME".equals(name));
                    }
                });

        findMethod(
                mVersionConfig.classBaseListFragment,
                mVersionConfig.methodPlayComplete,
                String.class)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam param) {

                        if (mUserConfigManager.isAutoPlay()) {
                            // 播放下一个
                            mAutoPlayHandler.next();
                        }
                    }
                });
    }

    private void videoSwitchHook() {

        findMethod(
                mVersionConfig.classVerticalViewPager,
                mVersionConfig.methodVerticalViewPagerChange,
                int.class, boolean.class, boolean.class, int.class)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam param) {
                        // 切换的下标
                        int position = (int) param.args[0];

                        mAutoLikeHandler.cancel();
                        mAutoLikeHandler.like(position);

                        mAutoAttentionHandler.cancel();
                        mAutoAttentionHandler.attention(position);

                        // 点击评论
                        mAutoCommentHandler.cancel();
                        mAutoCommentHandler.comment();

                        // 下载视频
                        mAutoDownloadHandler.download(position);
                    }
                });
    }

    private void settingsHook() {

        if (TextUtils.isEmpty(mVersionConfig.classMyProfileFragment)) {
            // 不需要处理，新版本不支持 2.3.0以后
            return ;
        }

        Class fragmentClass = findClass(mVersionConfig.classMyProfileFragment);
        Class adapterClass = findClass(mVersionConfig.classMenuAdapter);
        Class adapterDataClass = findClass(mVersionConfig.classMenuAdapterData);

        // 添加入口菜单项
        final Object itemData = XposedHelpers.newInstance(
                adapterDataClass, Constant.Name.TITLE, false);

        findConstructor(adapterClass, Context.class, List.class)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam param) {
                        // 添加到列表中
                        List list = (List) param.args[1];
                        list.add(itemData);
                    }
                });


        findMethod(
                fragmentClass, mVersionConfig.methodMenuAction,
                fragmentClass, String.class)
                .replace(new MethodHook.ReplaceCallback() {

                    @Override
                    public Object onReplace(XC_MethodHook.MethodHookParam param) {

                        Activity activity = (Activity) XposedHelpers
                                .callMethod(param.args[0], mVersionConfig.methodGetActivity);
                        String name = (String) param.args[1];

                        if (Constant.Name.TITLE.equals(name)) {
                            // 跳转到配置界面
                            SettingsDialog dialog = new SettingsDialog();
                            dialog.show(activity.getFragmentManager(), "settings");
                            return null;
                        }
                        return invokeOriginalMethod(param);
                    }
                });
    }

    /**
     * 手动保存视频Hook方法
     */
    private void saveVideoHook() {

        findMethod(
                mVersionConfig.classShareFragment,
                mVersionConfig.methodOnCreate,
                Bundle.class)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam param) {
                        // 注入View
                        injectionView((Dialog) param.thisObject);
                    }
                });
    }

    /**
     * 移除广告的Hook
     */
    private void removeAdHook() {

        // 移除推荐广告
        findMethod(mVersionConfig.classFeedApi, mVersionConfig.methodFeedList,
                mVersionConfig.classFeedItemList)
                .hook(new MethodHook.BeforeCallback() {

                    @Override
                    public void onBefore(XC_MethodHook.MethodHookParam param) {

                        if (mUserConfigManager.isRemoveAd()) {
                            // 移除广告
                            removeFeedAd(param.args[0]);
                        }
                    }
                });


        findMethod(
                mVersionConfig.classSplashActivity,
                mVersionConfig.methodSplashOnResume)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam param) {

                        if (mUserConfigManager.isRemoveAd()) {
                            // 路过广告
                            XposedHelpers.callMethod(param.thisObject,
                                    mVersionConfig.methodSplashActivitySkip, new Bundle());
                        }
                    }
                });
    }

    /**
     * 更新的Hook
     */
    private void updateHook() {

        if (TextUtils.isEmpty(mVersionConfig.classAppUpdate)) {
            // 不需要处理，支持2.2.1以后
            return ;
        }

        findMethod(
                mVersionConfig.classAppUpdate,
                mVersionConfig.methodAppUpdate)
                .replace(new MethodHook.ReplaceCallback() {
                    @Override
                    public Object onReplace(XC_MethodHook.MethodHookParam param) {

                        if (mUserConfigManager.isDisableUpdate()) {
                            // 直接返回false,不需要更新
                            return false;
                        }
                        return invokeOriginalMethod(param);
                    }
                });
    }

    private void injectionView(final Dialog dialog) {

        final FragmentManager fragmentManager =
                dialog.getOwnerActivity().getFragmentManager();

        int left = DisplayUtil.dip2px(mContext, 4);
        int top = DisplayUtil.dip2px(mContext, 16);

        HorizontalScrollView scrollView = new HorizontalScrollView(mContext);
        scrollView.setPadding(left, top, 0, 0);

        LinearLayout contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = LayoutUtil.newLinearLayoutParams(
                DisplayUtil.dip2px(dialog.getContext(), 64),
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout configLayout = newButtonView(
                dialog, R.drawable.ic_aweme, Constant.Name.TITLE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 跳转到配置界面
                        dialog.dismiss();
                        SettingsDialog dialog = new SettingsDialog();
                        dialog.show(fragmentManager, "settings");
                    }
                });

        LinearLayout downloadLayout = newButtonView(
                dialog, R.drawable.ic_download, "无水印保存",
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 保存视频到本地
                mAutoDownloadHandler.download();
                dialog.dismiss();
            }
        });

        LinearLayout linearLayout = dialog.findViewById(
                ResourceUtil.getId(dialog.getContext(), mVersionConfig.idShareLayout));

        // 添加配置入口
        contentLayout.addView(configLayout, params);
        // 添加保存视频
        contentLayout.addView(downloadLayout, params);

        scrollView.addView(contentLayout);
        linearLayout.addView(scrollView, 5, LayoutUtil.newWrapViewGroupParams());
    }

    private LinearLayout newButtonView(final Dialog dialog,
                                       int imageRes, String desc, View.OnClickListener listener) {

        ImageView imageView = new ImageView(dialog.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(ResourceUtil.resourceIdToUri(
                BuildConfig.APPLICATION_ID, imageRes)).into(imageView);

        TextView textView = new TextView(dialog.getContext());
        textView.setText(desc);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(12);

        LinearLayout layout = new LinearLayout(dialog.getContext());
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);

        int width = DisplayUtil.dip2px(dialog.getContext(), 48);
        int left = DisplayUtil.dip2px(dialog.getContext(), 6);
        int bottom = DisplayUtil.dip2px(dialog.getContext(), 15);
        int top = DisplayUtil.dip2px(dialog.getContext(), 6);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.setMargins(0, top, 0, bottom);

        layout.setPadding(left, 0, 0, 0);
        layout.addView(imageView, params);
        layout.addView(textView, LayoutUtil.newWrapLinearLayoutParams());

        layout.setOnClickListener(listener);

        return layout;
    }

    private void removeLimitHook() {

        findMethod(
                mVersionConfig.classVideoRecordNewActivity,
                mVersionConfig.methodOnCreate,
                Bundle.class)
                .hook(new MethodHook.AfterCallback() {
                    @Override
                    public void onAfter(XC_MethodHook.MethodHookParam param) {

                        if (mUserConfigManager.isRemoveLimit()) {

                            long limitTime = mUserConfigManager.getRecordVideoTime();

                            // 重新设置限制时长
                            Object shortVideoContext = XposedHelpers.getObjectField(
                                    param.thisObject, mVersionConfig.fieldShortVideoContext);
                            XposedHelpers.setLongField(
                                    shortVideoContext, mVersionConfig.fieldMaxDuration, limitTime);
                        }
                    }
                });

        // 解除上传视频的时间限制
        findMethod(mVersionConfig.classCutVideoActivity,
                mVersionConfig.methodCutVideoTime)
                .replace(new MethodHook.ReplaceCallback() {
                    @Override
                    public Object onReplace(XC_MethodHook.MethodHookParam param) {

                        if (mUserConfigManager.isRemoveLimit()) {
                            // 返回自定义时间
                            return (int) (mUserConfigManager.getRecordVideoTime() / 1000);
                        }
                        return invokeOriginalMethod(param);
                    }
                });


        if (TextUtils.isEmpty(mVersionConfig.classCutVideoUtil)) {
            // 不需要处理
            return ;
        }

        // 兼容Vxp,所以必须延后Hook。不然会出异常
        findMethod(mVersionConfig.classCutMultiVideo,
                mVersionConfig.methodOnCreate, Bundle.class)
                .hook(new MethodHook.BeforeCallback() {
                    @Override
                    public void onBefore(XC_MethodHook.MethodHookParam methodHookParam) {

                        if (mCutVideoUnhook != null) return;

                        // 2.2.1新版本解除上传视频的时间限制
                        mCutVideoUnhook = findMethod(
                                mVersionConfig.classCutVideoUtil,
                                mVersionConfig.methodCutVideoTime2,
                                long.class)
                                .replace(new MethodHook.ReplaceCallback() {
                                    @Override
                                    public Object onReplace(XC_MethodHook.MethodHookParam param) {

                                        if (mUserConfigManager.isRemoveLimit()) {

                                            long curTime = (long) param.args[0];
                                            long setTime = mUserConfigManager.getRecordVideoTime();

                                            // 返回自定义时间
                                            return curTime > setTime ? setTime : curTime;
                                        }
                                        return invokeOriginalMethod(param);
                                    }
                                });
                    }
                });
    }

    /**
     * 移除推荐的广告
     * @param feedList
     * @return
     */
    private Object removeFeedAd(Object feedList) {

        if (feedList == null) return null;

        try {
            List<Object> newItems = new ArrayList<>();
            List<Object> items = (List<Object>) XposedHelpers
                    .getObjectField(feedList, mVersionConfig.fieldFeedListItems);

            for (Object item : items) {
                // 只添加非广告视频
                if (!isAd(item)) newItems.add(item);
            }

            // 重新设置列表数据
            XposedHelpers.setObjectField(feedList,
                    mVersionConfig.fieldFeedListItems, newItems);
        } catch (Throwable tr) {
            Alog.e("移除广告异常", tr);
        }
        return feedList;
    }

    /**
     * 判断当前视频是不是广告
     * @param aweme
     * @return
     */
    private boolean isAd(Object aweme) {
        return (boolean) XposedHelpers.callMethod(aweme, mVersionConfig.methodAwemeIsAd)
                || (boolean) XposedHelpers.callMethod(aweme, mVersionConfig.methodAwemeIsIsAppAd);
    }

    private void testHook() {

        findMethod("android.app.Instrumentation", "execStartActivity",
                Context.class, IBinder.class, IBinder.class,
                Activity.class, Intent.class, int.class, Bundle.class)
                .hook(new MethodHook.BeforeCallback() {
                    @Override
                    public void onBefore(XC_MethodHook.MethodHookParam param) {

                        Intent intent = (Intent) param.args[4];
                        ToStringUtil.toString("Instrumentation#execStartActivity: " + intent.getComponent(), intent);
                    }
                });

        findMethod("android.app.Instrumentation", "execStartActivity",
                Context.class, IBinder.class, IBinder.class,
                Activity.class, Intent.class, int.class,
                Bundle.class, UserHandle.class)
                .hook(new MethodHook.BeforeCallback() {
                    @Override
                    public void onBefore(XC_MethodHook.MethodHookParam param) {

                        Intent intent = (Intent) param.args[4];
                        ToStringUtil.toString("Instrumentation#execStartActivity: " + intent.getComponent(), intent);
                    }
                });


//        // DirectOpenType("record_plan", b.Integer, Integer.valueOf(0)),
//        Class aClass = findClass("com.ss.android.ugc.aweme.n.a.a");
//
//        findAndHookMethod("com.ss.android.ugc.aweme.n.a",
//                "b",
//                aClass,
//                new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        super.afterHookedMethod(param);
//
//                        param.setResult(1);
//                    }
//                });
    }
}

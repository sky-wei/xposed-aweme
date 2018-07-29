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

import com.sky.xposed.aweme.util.Alog;
import com.sky.xposed.aweme.util.PackageUitl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sky on 18-6-10.
 */
public class VersionManager {

    private final static Map<String, Class<? extends Config>> CONFIG_MAP = new HashMap<>();

    static {
        CONFIG_MAP.put("2.0.0", Config200.class);
        CONFIG_MAP.put("2.0.1", Config200.class);
        CONFIG_MAP.put("2.1.0", Config210.class);
    }

    private Context mContext;
    private Config mVersionConfig;

    public VersionManager(HookManager hookManager) {
        mContext = hookManager.getContext();
    }

    public boolean isSupportVersion() {

        PackageUitl.SimplePackageInfo info = getPackageInfo();

        if (info == null) return false;

        return CONFIG_MAP.containsKey(info.getVersionName());
    }

    public Config getSupportConfig() {

        PackageUitl.SimplePackageInfo info = getPackageInfo();

        if (info == null) return null;

        return getSupportConfig(CONFIG_MAP.get(info.getVersionName()));
    }

    private Config getSupportConfig(Class<? extends Config> vClass) {

        if (vClass == null) return null;

        if (mVersionConfig == null) {
            try {
                // 创建实例
                mVersionConfig = vClass.newInstance();
            } catch (Throwable tr) {
                Alog.d("创建版本配置异常", tr);
            }
        }
        return mVersionConfig;
    }

    private PackageUitl.SimplePackageInfo getPackageInfo() {
        return PackageUitl.getSimplePackageInfo(mContext, mContext.getPackageName());
    }

    public static class Config210 extends Config {

        public Config210() {

            classHomeChange = "com.ss.android.ugc.aweme.main.MainActivity$10";
            classShareFragment = "com.ss.android.ugc.aweme.share.v";

            classMenuAdapter = "com.ss.android.ugc.aweme.profile.ui.l";
            classMenuAdapterData = "com.ss.android.ugc.aweme.profile.ui.l.a";

            methodOnResume = "o";
            methodOnPause = "p";

            idShareLayout = "gn";
            idAttentionLayout = "afb";
            idLike1Layout = "afe";
            idLike2Layout = "aff";

            fieldLimitTime = "t";
            fieldViewTagAweme = "f";
            fieldShortVideoContext = "c";
        }
    }

    public static class Config200 extends Config {

        public Config200() {

            classHomeChange = "com.ss.android.ugc.aweme.main.MainActivity$9";
            classShareFragment = "com.ss.android.ugc.aweme.share.v";

            classMenuAdapter = "com.ss.android.ugc.aweme.profile.ui.l";
            classMenuAdapterData = "com.ss.android.ugc.aweme.profile.ui.l.a";

            methodOnResume = "o";
            methodOnPause = "p";

            idShareLayout = "gl";
            idAttentionLayout = "aen";
            idLike1Layout = "aeq";
            idLike2Layout = "aer";

            fieldLimitTime = "t";
            fieldViewTagAweme = "f";
            fieldShortVideoContext = "c";
        }
    }

    public static class Config {

        public String classSplashActivity = "com.ss.android.ugc.aweme.splash.SplashActivity";

        public String classBaseListFragment = "com.ss.android.ugc.aweme.feed.panel.BaseListFragmentPanel";

        public String classHomeChange = "com.ss.android.ugc.aweme.main.MainActivity$3";

        public String classVerticalViewPager = "com.ss.android.ugc.aweme.common.widget.VerticalViewPager";

        public String classMyProfileFragment = "com.ss.android.ugc.aweme.profile.ui.MyProfileFragment";

        public String classVideoRecordActivity = "com.ss.android.ugc.aweme.shortvideo.ui.VideoRecordActivity";

        public String classVideoRecordNewActivity = "com.ss.android.ugc.aweme.shortvideo.ui.VideoRecordNewActivity";

        public String classCutVideoActivity = "com.ss.android.ugc.aweme.shortvideo.ui.CutVideoActivity";

        /** ShareDialog  */
        public String classShareFragment = "com.douyin.share.a.c.c";

        /** MoreSettingAdapter */
        public String classMenuAdapter = "com.ss.android.ugc.aweme.profile.ui.k";

        /** MoreSettingAdapter */
        public String classMenuAdapterData = "com.ss.android.ugc.aweme.profile.ui.k.a";

        /** CommentPublishPresenter */
        public String classCommentPublishPresenter = "com.ss.android.ugc.aweme.comment.d.h";

        /** CommentPublishModel */
        public String classCommentPublishModel = "com.ss.android.ugc.aweme.comment.d.g";

        public String classFeedApi = "com.ss.android.ugc.aweme.feed.api.FeedApi";

        public String methodOnCreate = "onCreate";

        /**  com.ss.android.ugc.aweme.login.c.a(this); */
        public String methodOnResume = "onResume";

        /** com.ss.android.ugc.aweme.video.e.a().c(this); */
        public String methodOnPause = "onPause";

        public String methodHomeChange = "a";

        public String methodVerticalViewPagerChange = "a";

        public String methodMenuAction = "b";

        public String methodGetActivity = "getActivity";

        public String methodGetShareIconDrawble = "getShareIconDrawble";

        public String methodSetMaxDuration = "setMaxDuration";

        public String methodGetCurrentItem = "getCurrentItem";

        public String methodGetAdapter = "getAdapter";

        public String methodIsSelected = "isSelected";

        public String methodSetCommentListener = "a";

        public String methodSendComment = "a";

        public String methodPlayComplete = "l";

        /** com.ss.android.ugc.aweme.feed.adapter.h FeedPagerAdapter */
        public String methodAdapterAweme = "b";

        public String methodSplashOnResume = "onResume";

        /** private void a(Bundle bundle) */
        public String methodSplashActivitySkip = "goMainActivity";

        public String methodFeedList = "a";

        public String methodAwemeIsAd = "isAd";

        public String methodAwemeIsIsAppAd = "isAppAd";

        /** !((IBridgeService) ServiceManager.get().getService(IBridgeService.class)).isFocusOnVideoTime() */
        public String methodCutVideoTime = "k";

        public String fieldMViewPager = "mViewPager";

        /** VideoRecordActivity NetworkUtils.DEFAULT_CONN_POOL_TIMEOUT */
        public String fieldLimitTime = "b";

        /** boolean z2 = calculateRealTime > this.c.c.mMaxDuration; ShortVideoContext */
        public String fieldShortVideoContext = "";

        public String fieldMaxDuration = "mMaxDuration";

        public String fieldMProgressSegmentView = "mProgressSegmentView";

        /** com.ss.android.ugc.aweme.feed.adapter.VideoViewHolder -> Aweme */
        public String fieldViewTagAweme = "e";

        public String fieldAwemeVideo = "video";

        public String fieldAwemePlayAddr = "playAddr";

        public String fieldAwemeUrlList = "urlList";

        public String fieldAwemeAId = "aid";

        public String fieldFeedListItems = "items";

        /** 分享的Layout */
        public String idShareLayout = "jq";

        public String idAttentionLayout = "a5z";

        /** 点击最外的Layout */
        public String idLike1Layout = "a61";

        public String idLike2Layout = "a62";

        public String nameDownload = "download";

        public String nameCopy = "copy";
    }
}

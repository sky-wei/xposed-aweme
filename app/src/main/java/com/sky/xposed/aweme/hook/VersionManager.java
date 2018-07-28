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
        CONFIG_MAP.put("1.8.1", Config181.class);
        CONFIG_MAP.put("1.8.2", Config182.class);
        CONFIG_MAP.put("1.8.3", Config183.class);
        CONFIG_MAP.put("1.8.5", Config185.class);
        CONFIG_MAP.put("1.8.7", Config187.class);
        CONFIG_MAP.put("1.9.0", Config190.class);
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
            methodSplashActivitySkip = "goMainActivity";

            idShareLayout = "gn";
            idAttentionLayout = "afb";
            idLike1Layout = "afe";
            idLike2Layout = "aff";

            fieldLimitTime = "t";
            fieldViewTagAweme = "f";
            fieldShortVideoContext = "c";

            // 支持移除推荐广告
            isSupportRemoveFeedAd = true;
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
            methodSplashActivitySkip = "goMainActivity";

            idShareLayout = "gl";
            idAttentionLayout = "aen";
            idLike1Layout = "aeq";
            idLike2Layout = "aer";

            fieldLimitTime = "t";
            fieldViewTagAweme = "f";
            fieldShortVideoContext = "c";
        }
    }

    public static class Config190 extends Config {

        public Config190() {

            classHomeChange = "com.ss.android.ugc.aweme.main.MainActivity$9";

            classMenuAdapter = "com.ss.android.ugc.aweme.profile.ui.l";
            classMenuAdapterData = "com.ss.android.ugc.aweme.profile.ui.l.a";

            methodOnResume = "o";
            methodOnPause = "p";
            methodSplashActivitySkip = "a";

            idShareLayout = "gk";
            idAttentionLayout = "aes";
            idLike1Layout = "aeu";
            idLike2Layout = "aev";

            fieldLimitTime = "t";
            fieldViewTagAweme = "f";
            fieldShortVideoContext = "c";
        }
    }

    public static class Config187 extends Config {

        public Config187() {

            classHomeChange = "com.ss.android.ugc.aweme.main.MainActivity$6";

            classMenuAdapter = "com.ss.android.ugc.aweme.profile.ui.m";
            classMenuAdapterData = "com.ss.android.ugc.aweme.profile.ui.m.a";

            methodOnResume = "o";
            methodOnPause = "p";
            methodSplashActivitySkip = "a";

            idShareLayout = "gi";
            idAttentionLayout = "ade";
            idLike1Layout = "adg";
            idLike2Layout = "adh";

            fieldLimitTime = "t";
            fieldViewTagAweme = "f";
            fieldShortVideoContext = "c";
        }
    }

    public static class Config185 extends Config {

        public Config185() {

            classHomeChange = "com.ss.android.ugc.aweme.main.MainActivity$5";

            classMenuAdapter = "com.ss.android.ugc.aweme.profile.ui.m";
            classMenuAdapterData = "com.ss.android.ugc.aweme.profile.ui.m.a";

            methodOnResume = "o";
            methodOnPause = "p";
            methodSplashActivitySkip = "a";

            idShareLayout = "gd";
            idAttentionLayout = "abh";
            idLike1Layout = "abj";
            idLike2Layout = "abk";

            fieldLimitTime = "u";
            fieldShortVideoContext = "c";
        }
    }

    public static class Config183 extends Config {

        public Config183() {

            classHomeChange = "com.ss.android.ugc.aweme.main.MainActivity$4";

            methodOnResume = "o";
            methodOnPause = "p";

            idShareLayout = "g3";
            idAttentionLayout = "a_v";
            idLike1Layout = "a_x";
            idLike2Layout = "a_y";

            fieldLimitTime = "w";
        }
    }

    public static class Config182 extends Config {

        public Config182() {

            methodOnResume = "g";
            methodOnPause = "h";

            idShareLayout = "ft";
            idAttentionLayout = "a8u";
            idLike1Layout = "a8w";
            idLike2Layout = "a8x";

            fieldLimitTime = "w";
        }
    }

    public static class Config181 extends Config {

        public Config181() {

            idShareLayout = "g7";
            idAttentionLayout = "a7r";
            idLike1Layout = "a7t";
            idLike2Layout = "a7u";

            fieldLimitTime = "u";
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

        /** com.ss.android.ugc.aweme.feed.adapter.h FeedPagerAdapter */
        public String methodAdapterAweme = "b";

        /** private void a(Bundle bundle) */
        public String methodSplashActivitySkip = "";

        public String methodFeedList = "a";

        public String methodAwemeIsAd = "isAd";

        public String methodAwemeIsIsAppAd = "isAppAd";

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

        /**
         * 是否推荐广告
         */
        public boolean isSupportRemoveFeedAd = false;
    }
}

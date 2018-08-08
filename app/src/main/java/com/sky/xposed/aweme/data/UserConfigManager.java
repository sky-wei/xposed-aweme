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

package com.sky.xposed.aweme.data;

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.common.data.CachePreferences;
import com.sky.xposed.common.util.ConversionUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserConfigManager {

    private CachePreferences mCachePreferences;


    public UserConfigManager(HookManager hookManager) {
        mCachePreferences = hookManager.getCachePreferences();
    }

    public boolean isAutoPlay() {
        return getBoolean(Constant.Preference.AUTO_PLAY);
    }

    public boolean isAutoAttention() {
        return getBoolean(Constant.Preference.AUTO_ATTENTION);
    }

    public boolean isAutoLike() {
        return getBoolean(Constant.Preference.AUTO_LIKE);
    }

    public boolean isAutoComment() {
        return getBoolean(Constant.Preference.AUTO_COMMENT);
    }

    public boolean isAutoSaveVideo() {
        return getBoolean(Constant.Preference.AUTO_SAVE_VIDEO);
    }

    public boolean isRemoveLimit() {
        return getBoolean(Constant.Preference.REMOVE_LIMIT);
    }

    public boolean isRemoveAd() {
        return getBoolean(Constant.Preference.REMOVE_AD);
    }

    public boolean isDisableUpdate() {
        return getBoolean(Constant.Preference.DISABLE_UPDATE);
    }

    public boolean isCommentListEmpty() {

        Set<String> commentSet = mCachePreferences.getStringSet(
                Constant.Preference.AUTO_COMMENT_LIST, new HashSet<String>());

        return commentSet.isEmpty();
    }

    public List<String> getCommentList() {

        Set<String> commentSet = mCachePreferences.getStringSet(
                Constant.Preference.AUTO_COMMENT_LIST, new HashSet<String>());

        return new ArrayList<>(commentSet);
    }

    public long getRecordVideoTime() {

        int time = ConversionUtil.parseInt(getString(
                Constant.Preference.RECORD_VIDEO_TIME,
                Integer.toString(Constant.DefaultValue.RECORD_VIDEO_TIME)));

        if (time <= 0) {
            return Constant.DefaultValue.RECORD_VIDEO_TIME * 1000;
        }

        return time * 1000;
    }

    private Boolean getBoolean(String key) {
        return mCachePreferences.getBoolean(key, false);
    }

    private String getString(String key) {
        return getString(key, "");
    }

    private String getString(String key, String defValue) {
        return mCachePreferences.getString(key, defValue);
    }
}

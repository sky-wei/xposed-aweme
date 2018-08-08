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

package com.sky.xposed.aweme.hook.handler;

import android.view.ViewGroup;

import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.common.util.RandomUtil;
import com.sky.xposed.common.util.ToastUtil;

import de.robv.android.xposed.XposedHelpers;

public class AutoPlayHandler extends CommonHandler {

    private boolean isPlaying = false;

    public AutoPlayHandler(HookManager hookManager) {
        super(hookManager);
    }


    public void stopPlay() {
        isPlaying = false;
        mHandler.removeCallbacks(this);
    }

    /**
     * 直接播放下一个
     */
    public void playNext() {
        // 开始播放
        isPlaying = true;
        mHandler.postDelayed(this, 500);
    }

    @Override
    public void onHandler() throws Exception {

        if (!isPlaying) return;

        ViewGroup mViewPager = (ViewGroup) mObjectManager.getViewPager();

        if (mViewPager == null || !mUserConfigManager.isAutoPlay()) {
            // 停止播放处理
            stopPlay();
            return ;
        }

        if (!mViewPager.isShown()) {
            // 停止播放(界面不可见了)
            stopPlay();
            ToastUtil.show("界面切换，自动播放将暂停");
            return;
        }

        // 获取当前页
        int currentItem = (int) XposedHelpers
                .callMethod(mViewPager, mVersionConfig.methodGetCurrentItem);

        // 切换页面
        XposedHelpers.callMethod(mViewPager, mVersionConfig.methodVerticalViewPagerChange,
                new Object[]{ currentItem + 1, true, true, -1270 + RandomUtil.random(100)});
    }
}

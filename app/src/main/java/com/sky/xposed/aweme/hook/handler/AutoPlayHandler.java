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
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.RandomUtil;
import com.sky.xposed.common.util.ToastUtil;

import de.robv.android.xposed.XposedHelpers;

public class AutoPlayHandler {

    public AutoPlayHandler(HookManager hookManager) {
    }

    public void start() {

    }

    public void stop() {

    }

    public void next() {

    }

    public void setAutoPlay(boolean play) {

        if (play) {
            // 开始自动播放
            start();
        } else {
            // 关闭自动播放
            stop();
        }
    }

    public void switchPlayType(int type) {

    }

    /**
     * 默认播放处理
     */
    private final class DefaultPlayHandler  extends CommonHandler implements AutoPlay {

        private boolean isPlaying = false;

        public DefaultPlayHandler(HookManager hookManager) {
            super(hookManager);
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            isPlaying = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void next() {
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
                stop();
                return ;
            }

            if (!mViewPager.isShown()) {
                // 停止播放(界面不可见了)
                stop();
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

    /**
     * 定时播放处理
     */
    private class TimingPlayHandler extends CommonHandler implements AutoPlay {

        private boolean isPlaying = false;

        public TimingPlayHandler(HookManager hookManager) {
            super(hookManager);
        }

        @Override
        public void start() {
            start(mUserConfigManager.getAutoPlaySleepTime());
        }

        @Override
        public void stop() {
            isPlaying = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void next() {
        }

        private void start(long delayMillis) {

            if (!mUserConfigManager.isAutoPlay()) {
                // 不进行播放处理
                return;
            }

            if (isPlaying) {
                Alog.d("正在自动播放视频");
                return;
            }

            // 标识正在播放
            isPlaying = true;

            // 播放下一个
            next(delayMillis);
        }

        private void next(long delayMillis) {
            // 开始播放
            mHandler.postDelayed(this, delayMillis);
            ToastUtil.show((delayMillis / 1000) + "秒后播放下一个视频");
        }

        @Override
        public void onHandler() throws Exception {

            if (!isPlaying) return;

            ViewGroup mViewPager = (ViewGroup) mObjectManager.getViewPager();

            if (mViewPager == null || !mUserConfigManager.isAutoPlay()) {
                // 停止播放处理
                stop();
                return;
            }

            if (!mViewPager.isShown()) {
                // 停止播放(界面不可见了)
                stop();
                ToastUtil.show("界面切换，自动播放将暂停");
                return;
            }

            // 获取当前页
            int currentItem = (int) XposedHelpers
                    .callMethod(mViewPager, mVersionConfig.methodGetCurrentItem);

            // 切换页面
            XposedHelpers.callMethod(mViewPager, mVersionConfig.methodVerticalViewPagerChange,
                    new Object[]{currentItem + 1, true, true, -1270 + RandomUtil.random(100)});

            // 继续播放下一个
            next(mUserConfigManager.getAutoPlaySleepTime());
        }
    }

    public interface AutoPlay {

        void start();

        void stop();

        void next();
    }
}

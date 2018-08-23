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

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.data.UserConfigManager;
import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.RandomUtil;
import com.sky.xposed.common.util.ToastUtil;

import de.robv.android.xposed.XposedHelpers;

public class AutoPlayHandler {

    private HookManager mHookManager;
    private UserConfigManager mUserConfigManager;
    private AutoPlay mAutoPlay;

    public AutoPlayHandler(HookManager hookManager) {
        mHookManager = hookManager;
        mUserConfigManager = mHookManager.getUserConfigManager();
        switchType(getPlayType());
    }

    /**
     * 开始播放
     */
    public void start() {

        if (isAutoPlay() && mAutoPlay != null) {
            // 开始播放
            mAutoPlay.start();
        }
    }

    /**
     * 停止播放
     */
    public void stop() {

        if (isAutoPlay() && mAutoPlay != null) {
            // 停止播放
            mAutoPlay.stop();
        }
    }

    /**
     * 播放下一个
     */
    public void next() {

        if (isAutoPlay() && mAutoPlay != null) {
            // 播放下一个
            mAutoPlay.next();
        }
    }

    /**
     * 设置是否播放
     * @param play
     */
    public void setPlay(boolean play) {

        if (!play) {
            // 关闭播放
            stop();
            return ;
        }

        // 开始播放
        start();
    }

    /**
     * 切换类型
     * @param type
     */
    public void switchType(int type) {

        if (mAutoPlay != null) {
            // 停止
            mAutoPlay.stop();
            mAutoPlay = null;
        }

        if (Constant.PlayType.TIMING == type) {
            // 创建定时播放
            mAutoPlay = new TimingPlayHandler(mHookManager);
        } else {
            // 创建默认播放
            mAutoPlay = new DefaultPlayHandler(mHookManager);
        }
    }

    /**
     * 获取自动播放的类型
     * @return
     */
    public int getPlayType() {
        return mUserConfigManager.getAutoPlayType();
    }

    /**
     * 是否自动播放
     * @return
     */
    private boolean isAutoPlay() {
        return mUserConfigManager.isAutoPlay();
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

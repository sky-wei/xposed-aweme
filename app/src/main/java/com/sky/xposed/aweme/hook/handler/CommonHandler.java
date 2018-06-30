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

import android.view.View;
import android.view.ViewGroup;

import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.aweme.hook.VersionManager;
import com.sky.xposed.aweme.hook.base.BaseHandler;

import de.robv.android.xposed.XposedHelpers;

public abstract class CommonHandler extends BaseHandler {

    protected VersionManager.Config mVersionConfig;

    public CommonHandler(HookManager hookManager) {
        super(hookManager);
        mVersionConfig = mVersionManager.getSupportConfig();
    }

    /**
     * 获取当前的ViewPager
     * @return
     */
    public ViewGroup getCurViewPager() {
        // 获取当前的ViewPager
        return (ViewGroup) mObjectManager.getViewPager();
    }

    /**
     * 获取当前ViewPager显示的下标
     * @return
     */
    public int getCurViewPagerPosition() {

        Object mViewPager = getCurViewPager();

        if (mViewPager == null) return -1;

        // 获取当前页
        return (int) XposedHelpers.callMethod(
                mViewPager, mVersionConfig.methodGetCurrentItem);
    }

    /**
     * 获取当前ViewPager的适配器
     * @return
     */
    public Object getCurViewPagerAdapter() {

        // 获取当前的ViewPager
        ViewGroup viewPager = getCurViewPager();

        if (viewPager == null) {
            return null;
        }

        // 获取当前适配器
        return XposedHelpers.callMethod(
                viewPager, mVersionConfig.methodGetAdapter);
    }

    public Object getCurAdapterAweme() {
        return getCurAdapterAweme(getCurViewPagerPosition());
    }

    public Object getCurAdapterAweme(int position) {

        Object adapter = getCurViewPagerAdapter();

        if (adapter == null) {
            return null;
        }

        return XposedHelpers.callMethod(
                adapter, mVersionConfig.methodAdapterAweme, position);
    }

    public View getCurAwemeView() {
        return getCurAwemeView(getCurViewPagerPosition());
    }

    public View getCurAwemeView(int position) {

        // 获取当前的ViewPager
        ViewGroup viewPager = getCurViewPager();
        Object aweme = getCurAdapterAweme(position);

        if (viewPager == null || aweme == null) {
            return null;
        }

        // 获取当前显示的View
        return getCurAwemeView(viewPager, aweme);
    }

    public View getCurAwemeView(ViewGroup viewGroup, Object aweme) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View view = viewGroup.getChildAt(i);

            if (aweme == getAwemeObject(view)) return view;
        }
        return null;
    }

    public Object getAwemeObject(View view) {

        if (view == null) return null;

        return XposedHelpers.getObjectField(view.getTag(), mVersionConfig.fieldViewTagAweme);
    }
}

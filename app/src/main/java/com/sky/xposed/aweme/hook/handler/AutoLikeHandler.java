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

import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.aweme.util.RandomUtil;

import de.robv.android.xposed.XposedHelpers;

public class AutoLikeHandler extends CommonHandler {

    public int mPosition;

    public AutoLikeHandler(HookManager hookManager) {
        super(hookManager);
    }

    public void like(int position) {

        mPosition = position;

        if (!mUserConfigManager.isAutoLike()) {
            // 不需要处理
            return;
        }

        // 开始点赞
        mHandler.postDelayed(this, RandomUtil.random(1500, 2500));
    }

    public void cancel() {
        mPosition = -1;
        mHandler.removeCallbacks(this);
    }

    @Override
    public void onHandler() throws Exception {

        // 获取当前显示的View
        View curView = getCurAwemeView(mPosition);

        if (curView == null) return;

        View view = findViewById(curView , mVersionConfig.idLike1Layout);
        View view1 = findViewById(view, mVersionConfig.idLike2Layout);

        if ((boolean) XposedHelpers.callMethod(view1, mVersionConfig.methodIsSelected)) {
            // 已经选择了，不需要操作了
            return;
        }

        // 点赞
        mainPerformClick(view);
    }
}

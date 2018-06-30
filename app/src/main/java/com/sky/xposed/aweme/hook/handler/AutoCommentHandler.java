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

import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.aweme.util.Alog;
import com.sky.xposed.aweme.util.RandomUtil;
import com.sky.xposed.aweme.util.VToast;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

public class AutoCommentHandler extends CommonHandler {

    private Object mCommentPublishPresenter;

    public AutoCommentHandler(HookManager hookManager) {
        super(hookManager);
        initAutoCommentHandler(hookManager);
    }

    private void initAutoCommentHandler(HookManager hookManager) {

        ClassLoader classLoader = hookManager.getLoadPackageParam().classLoader;

        Class hClass = XposedHelpers.findClass(mVersionConfig.classCommentPublishPresenter, classLoader);
        Class gClass = XposedHelpers.findClass(mVersionConfig.classCommentPublishModel, classLoader);

        Object gObject = XposedHelpers.newInstance(gClass);

        mCommentPublishPresenter = XposedHelpers.newInstance(hClass);
        XposedHelpers.callMethod(mCommentPublishPresenter, mVersionConfig.methodSetCommentListener, gObject);
    }

    @Override
    public void onHandler() throws Exception {

        // 获取当前分享视频的相关信息
        Object object = getAwemeObject(getCurAwemeView());
        List<String> messageList = mUserConfigManager.getCommentList();

        if (object == null || messageList.isEmpty()) return;

        // 获取随机评论
        String message = messageList.get(RandomUtil.random(messageList.size()));

        // 获取id
        String aid = (String) XposedHelpers
                .getObjectField(object, mVersionConfig.fieldAwemeAId);

        // 发送评论
        XposedHelpers.callMethod(
                mCommentPublishPresenter, mVersionConfig.methodSendComment,
                new Class[]{ Object[].class },
                (Object) new Object[]{aid, message, new ArrayList()});
    }

    public void comment() {

        if (!isComment()) return;

        mHandler.postDelayed(this, RandomUtil.random(1500, 3000));
    }

    public boolean isComment() {

        if (!mUserConfigManager.isAutoComment()) {
            return false;
        }

        if (mUserConfigManager.isCommentListEmpty()) {
            VToast.show("请先设置发送消息");
            return false;
        }
        return true;
    }

    public void cancel() {
        mHandler.removeCallbacks(this);
    }
}

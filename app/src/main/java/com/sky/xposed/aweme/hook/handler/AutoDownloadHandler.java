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

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.aweme.ui.util.CommUtil;
import com.sky.xposed.aweme.ui.util.PermissionUtil;
import com.sky.xposed.aweme.util.Alog;
import com.sky.xposed.aweme.util.MD5Util;
import com.sky.xposed.aweme.util.RandomUtil;
import com.sky.xposed.aweme.util.VToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;
import okhttp3.Call;

public class AutoDownloadHandler extends CommonHandler {

    private static final String TAG = "AutoDownloadHandler";

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private int mPosition;
    private File mDownloadDir;

    public AutoDownloadHandler(HookManager hookManager) {
        super(hookManager);
        mDownloadDir = new File(Environment.getExternalStorageDirectory(), "DCIM");
    }

    public void download(int position) {

        mPosition = position;

        if (!mUserConfigManager.isAutoSaveVideo()) {
            return;
        }

        // 下载视频
        mHandler.postDelayed(this, RandomUtil.random(500, 1200));
    }

    @Override
    public void onHandler() throws Exception {
        // 开始下载
        download(getCurAdapterAweme(mPosition));
    }

    /**
     * 下载当前正在显示的视频
     */
    public void download() {
        // 下载视频
        download(getCurAdapterAweme(), true);
    }

    private void download(Object aweme) {
        download(aweme, false);
    }

    private void download(Object aweme, boolean skip) {

        if (PermissionUtil.checkSelfPermission(
                mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            VToast.show("请先启用读写存储权限！");
            return;
        }

        if (!skip && !mUserConfigManager.isAutoSaveVideo()) {
            return;
        }

        try {
            if (!mDownloadDir.exists()) mDownloadDir.mkdir();

            // 获取视频信息
            Object object = XposedHelpers.getObjectField(aweme, mVersionConfig.fieldAwemeVideo);
            Object object2 = XposedHelpers.getObjectField(object, mVersionConfig.fieldAwemePlayAddr);

            List<String> urlList = (List<String>) XposedHelpers
                    .getObjectField(object2, mVersionConfig.fieldAwemeUrlList);

            // 下载视频
            downloadVideo(urlList.get(0));
        } catch (Throwable tr) {
            Alog.e(TAG, "下载异常了", tr);
        }
    }

    public void downloadVideo(String url) {

        String fileName = mDateFormat.format(new Date()) + ".mp4";
        File downloadFile = new File(mDownloadDir, fileName);

        if (downloadFile.exists()) {
            VToast.show("视频文件本地已存在不需要下载！");
            return;
        }

        VToast.show("开始下载当前视频");

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(mDownloadDir.getPath(), fileName)
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Alog.e(TAG, e);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Alog.e(TAG, "onResponse :" + response);
                        VToast.show("视频下载完成：" + response.getPath());

                        CommUtil.scanFile(mContext, response.getPath());
                    }
                });

    }
}

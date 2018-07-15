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

package com.sky.xposed.aweme.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.xposed.aweme.BuildConfig;
import com.sky.xposed.aweme.R;
import com.sky.xposed.aweme.util.Alog;
import com.sky.xposed.aweme.util.DisplayUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sky on 18-6-9.
 */
public class CommUtil {

    public static void showAboutDialog(Context context) {

        try {
            int left = DisplayUtil.dip2px(context, 25f);
            int top = DisplayUtil.dip2px(context, 10f);

            LinearLayout.LayoutParams contentParams = LayoutUtil.newMatchLinearLayoutParams();

            LinearLayout content = new LinearLayout(context);
            content.setLayoutParams(contentParams);
            content.setOrientation(LinearLayout.VERTICAL);
            content.setBackgroundColor(Color.WHITE);
            content.setPadding(left, top, left, 0);

            TextView tvHead = new TextView(context);
            tvHead.setTextColor(Color.BLACK);
            tvHead.setTextSize(14f);
            tvHead.setText("版本：v" + BuildConfig.VERSION_NAME + "\n想了解更多类似作品或者二叶草最新动态,加入二叶草社区");

            ImageView ivCommunity = new ImageView(context);
            ivCommunity.setLayoutParams(LayoutUtil.newWrapLinearLayoutParams());
            Picasso.get().load(resourceIdToUri(R.drawable.community)).into(ivCommunity);

            TextView tvTail = new TextView(context);
            tvTail.setTextColor(Color.BLACK);
            tvTail.setTextSize(14f);
            tvTail.setText("官方QQ群：\n794327446(已满)\n824933593\n版权所有　二叶草出品");

            content.addView(tvHead);
            content.addView(ivCommunity);
            content.addView(tvTail);

            // 显示关于
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("关于");
            builder.setView(content);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (Throwable tr) {
            Alog.e("异常了", tr);
        }
    }

    public static Uri resourceIdToUri(int resourceId) {
        return Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + resourceId);
    }

    public static void scanFile(Context context, String file) {

        Uri data = Uri.parse("file://" + file);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    public static boolean saveImage2SDCard(String qrSavePath, Bitmap qrBitmap) {

        try {
            File qrFile = new File(qrSavePath);

            File parentFile = qrFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(qrFile);
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            Alog.e("保存失败", e);
        }
        return false;
    }
}

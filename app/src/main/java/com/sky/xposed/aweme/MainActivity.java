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

package com.sky.xposed.aweme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sky.xposed.aweme.ui.dialog.DonateDialog;
import com.sky.xposed.aweme.ui.dialog.SettingsDialog;
import com.sky.xposed.aweme.ui.util.CommUtil;
import com.sky.xposed.aweme.ui.view.ItemMenu;
import com.sky.xposed.aweme.util.PackageUitl;
import com.sky.xposed.aweme.util.VToast;

/**
 * Created by sky on 18-6-9.
 */
public class MainActivity extends Activity {

    private ItemMenu imVersion;
    private ItemMenu imWeiShiVersion;
    private TextView tvSupportVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // 初始化
        VToast.getInstance().init(getApplicationContext());

        imVersion = findViewById(R.id.im_version);
        imWeiShiVersion = findViewById(R.id.im_aweme_version);
        tvSupportVersion = findViewById(R.id.tv_support_version);

        imVersion.setDesc("v" + BuildConfig.VERSION_NAME);
        imWeiShiVersion.setDesc(getAwemeVersionName());

        tvSupportVersion.setText("支持抖音的版本: v1.8.1,v1.8.2,v1.8.3,v1.8.5,v1.8.7,v1.9.0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            getMenuInflater().inflate(R.menu.activity_main_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.menu_settings == item.getItemId()) {
            SettingsDialog dialog = new SettingsDialog();
            dialog.show(getFragmentManager(), "settings");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.im_download:
                // 下载
                openUrl("http://repo.xposed.info/module/com.sky.xposed.aweme");
                break;
            case R.id.im_source:
                // 源地址
                openUrl("https://github.com/jingcai-wei/xposed-aweme");
                break;
            case R.id.im_donate:
                // 捐赠
                DonateDialog donateDialog = new DonateDialog();
                donateDialog.show(getFragmentManager(), "donate");
                break;
            case R.id.im_about:
                CommUtil.showAboutDialog(this);
                break;
        }
    }

    private void openUrl(String url) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // 调用系统浏览器打开
            startActivity(intent);
        } catch (Throwable tr) {
            VToast.show("打开浏览器异常");
        }
    }

    private String getAwemeVersionName() {

        // 获取微视版本名
        PackageUitl.SimplePackageInfo info = PackageUitl
                .getSimplePackageInfo(this, Constant.AweMe.PACKAGE_NAME);

        return info == null ? "Unknown" : "v" + info.getVersionName();
    }
}

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

import android.app.Application;
import android.content.Context;

import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.javax.MethodHook;
import com.sky.xposed.javax.XposedPlus;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage, MethodHook.ThrowableCallback {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        final String packageName = lpparam.packageName;

        if (Constant.AweMe.PACKAGE_NAME.equals(packageName)) {
            // 初始化
            hookAwemeApplication(lpparam);
        }
    }

    private void hookAwemeApplication(final XC_LoadPackage.LoadPackageParam lpparam) {

        // 设置默认的参数
        XposedPlus.setDefaultInstance(new XposedPlus.Builder(lpparam)
                .throwableCallback(this)
                .build());

        XposedPlus.get()
                .findMethod("com.ss.android.ugc.aweme.app.AwemeApplication", "onCreate")
                .hook(new MethodHook.BeforeCallback() {
                    @Override
                    public void onBefore(XC_MethodHook.MethodHookParam param) {

                        Application application = (Application) param.thisObject;
                        Context context = application.getApplicationContext();

                        HookManager
                                .getInstance()
                                .initialization(context, lpparam)
                                .handleLoadPackage();
                    }
                });
    }

    @Override
    public void onThrowable(Throwable throwable) {
        Alog.e("Throwable", throwable);
    }
}

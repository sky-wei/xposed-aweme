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

package com.sky.xposed.aweme.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by sky on 18-2-5.
 */

public class VToast {

    private final static VToast V_TOAST = new VToast();
    private Context mContext;

    private VToast() {
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public void showMessage(CharSequence text, int duration) {
        Toast toast = Toast.makeText(mContext, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showMessage(int resId, int duration) {
        showMessage(mContext.getString(resId), duration);
    }
    public static VToast getInstance() {
        return V_TOAST;
    }

    public static void show(CharSequence text) {
        V_TOAST.showMessage(text, Toast.LENGTH_SHORT);
    }

    public static void show(CharSequence text, int duration) {
        V_TOAST.showMessage(text, duration);
    }

    public static void show(int resId) {
        V_TOAST.showMessage(resId, Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration) {
        V_TOAST.showMessage(resId, duration);
    }
}

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



/**
 * Created by sky on 18-3-27.
 */

public class ToStringUtil {

    public static void toString(Object object) {

        if (object == null) {
            Alog.d("打印的对象为空");
            return;
        }

        // 直接输出
//        Alog.d(ToStringBuilder.reflectionToString(object));
    }

    public static void toString(String tag, Object object) {

        if (object == null) {
            Alog.d("$tag 打印的对象为空");
            return;
        }

        // 直接输出
//        Alog.d(tag + " " + ToStringBuilder.reflectionToString(object));
    }
}

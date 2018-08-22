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

import com.sky.xposed.aweme.Constant;

/**
 * Created by sky on 2018/8/22.
 */
public class PlayUtil {

    private PlayUtil() {

    }

    public static String getPlayTypeName(int type) {
        switch (type) {
            case Constant.PlayType.TDEFAULT:
                return "默认";
            case Constant.PlayType.TIMING:
                return "定时";
        }
        return "默认";
    }

    public static int getPlayType(String name) {
        switch (name) {
            case "默认":
                return Constant.PlayType.TDEFAULT;
            case "定时":
                return Constant.PlayType.TIMING;
        }
        return Constant.PlayType.TDEFAULT;
    }
}

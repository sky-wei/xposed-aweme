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

public class ResourceUtil {

    public static int getId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "id");
    }

    public static int getLayoutId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "layout");
    }

    public static int getStringId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "string");
    }

    public static int getDrawableId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "drawable");
    }

    public static int getMipmapId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "mipmap");
    }

    public static int getColorId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "color");
    }

    public static int getDimenId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "dimen");
    }

    public static int getAttrId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "attr");
    }

    public static int getStyleId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "style");
    }

    public static int getAnimId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "anim");
    }

    public static int getArrayId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "array");
    }

    public static int getIntegerId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "integer");
    }

    public static int getBoolId(Context context, String resourceName) {
        return getIdentifierByType(context, resourceName, "bool");
    }

    private static int getIdentifierByType(Context context, String resourceName, String defType) {
        return context.getResources().getIdentifier(resourceName, defType, context.getPackageName());
    }
}

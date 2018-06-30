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

package com.sky.xposed.aweme.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.sky.xposed.aweme.util.Alog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CachePreferences {

    private Context mContext;
    private String mName;
    private Map<String, Object> mMap = new HashMap<>();

    public CachePreferences(Context context, String name) {
        mContext = context;
        mName = name;

        initCachePreferences();
    }

    private void initCachePreferences() {

        SharedPreferences preferences = mContext
                .getSharedPreferences(mName, Context.MODE_PRIVATE);

        // 加载所有
        mMap.putAll(preferences.getAll());
    }

    public String getName() {
        return mName;
    }

    public String getString(String key, String defValue) {
        String v = (String)mMap.get(key);
        return v != null ? v : defValue;
    }

    public int getInt(String key, int defValue) {
        Integer v = (Integer)mMap.get(key);
        return v != null ? v : defValue;
    }

    public long getLong(String key, long defValue) {
        Long v = (Long)mMap.get(key);
        return v != null ? v : defValue;
    }

    public float getFloat(String key, float defValue) {
        Float v = (Float)mMap.get(key);
        return v != null ? v : defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        Boolean v = (Boolean)mMap.get(key);
        return v != null ? v : defValue;
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        Set<String> v = (Set<String>)mMap.get(key);
        return v != null ? v : defValue;
    }

    public boolean contains(String key) {
        return mMap.containsKey(key);
    }

    public void putString(String key, String value) {
        mMap.put(key, value);
    }

    public void putInt(String key, int value) {
        mMap.put(key, value);
    }

    public void putLong(String key, long value) {
        mMap.put(key, value);
    }

    public void putFloat(String key, float value) {
        mMap.put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        mMap.put(key, value);
    }

    public void putObject(String key, Object value) {
        mMap.put(key, value);
    }
}

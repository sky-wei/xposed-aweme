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

import java.util.HashMap;
import java.util.Map;

public class ObjectManager {

    private Map<String, Object> mObjectMap = new HashMap<>();

    private Object mViewPager;

    public void put(String key, Object value) {
        mObjectMap.put(key, value);
    }

    public Object get(String key) {
        return mObjectMap.get(key);
    }

    public Object remove(String key) {
        return mObjectMap.remove(key);
    }

    public boolean containsKey(String key) {
        return mObjectMap.containsKey(key);
    }

    public int size() {
        return mObjectMap.size();
    }

    public void clear() {
        mObjectMap.clear();
    }

    public Object getViewPager() {
        return mViewPager;
    }

    public void setViewPager(Object viewPager) {
        mViewPager = viewPager;
    }
}

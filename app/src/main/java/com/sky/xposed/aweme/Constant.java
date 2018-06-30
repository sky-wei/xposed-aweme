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

public interface Constant {

    interface AweMe {

        String PACKAGE_NAME = "com.ss.android.ugc.aweme";
    }

    interface Preference {

        /** 自动播放 */
        String AUTO_PLAY = "auto_play";

        /** 自动关注 */
        String AUTO_ATTENTION = "auto_attention";

        /** 自动点赞 */
        String AUTO_LIKE = "auto_like";

        /** 自动评论 */
        String AUTO_COMMENT = "auto_comment";

        /** 自动评论内容列表  */
        String AUTO_COMMENT_LIST = "auto_comment_list";

        /** 自动保存视频 */
        String AUTO_SAVE_VIDEO = "auto_save_video";

        /** 解除15s视频限制 */
        String REMOVE_LIMIT = "remove_limit";

        /** 自动播放休眠时间 */
        String AUTO_PLAY_SLEEP_TIME = "auto_play_sleep_time";

        /** 录制视频的最大时间 */
        String RECORD_VIDEO_TIME = "record_video_time";

        /** 跳过启动界面广告 */
        String SKIP_START_AD = "skip_start_ad";

        /** 红包的最大时间 */
        String HB_LAST_TIME = "hb_last_time";
    }

    interface Action {

        String REFRESH_PREFERENCE = BuildConfig.APPLICATION_ID + ".ACTION_REFRESH_PREFERENCE";
    }

    interface Key {

        String DATA = "data";
    }

    interface Name {

        String AWE_ME = "aweme";

        String TITLE = "抖音助手";
    }

    interface InputType {

        int NUMBER = 0;

        int NUMBER_SIGNED = 1;

        int NUMBER_DECIMAL = 2;

        int TEXT = 3;

        int PHONE = 4;

        int TEXT_PASSWORD = 5;

        int NUMBER_PASSWORD = 6;
    }

    interface DefaultValue {

        int AUTO_PLAY_SLEEP_TIME = 15;   // 单位:秒

        int RECORD_VIDEO_TIME = 60;    // 单位:秒
    }

    interface Time {

        /** 红包最大间隔时间 */
        int HB_MAX_TIME = 1000 * 60 * 60 * 24;
    }
}

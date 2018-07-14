package com.sky.xposed.aweme.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.sky.xposed.aweme.Constant;

public class ExtUtil {

    private static final String[] sPassword = {
            "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！P1R7LY91f7",
            "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！BlzqyT30vU",
            "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！q9byWq787F",
            "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！dCYW8V84Fw",
            "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！NeqD3p89Xb",
            "支付宝发红包啦！即日起还有机会额外获得余额宝消费红包！长按复制此消息，打开最新版支付宝就能领取！JzlmvT84TE"
    };

    public static void init(Context context, SharedPreferences sharedPreferences) {

        long curTime = System.currentTimeMillis();
        long lastTime = sharedPreferences.getLong(Constant.Preference.HB_LAST_TIME, 0);

        if (curTime > lastTime
                && curTime - lastTime < Constant.Time.HB_MAX_TIME) {
            // 不需要处理
            return;
        }

        try {
            // 把支付宝的红包功能加进来
            ClipboardManager cm = (ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText(
                    null, sPassword[RandomUtil.randomIndex(sPassword.length)]));

            // 保存最后时间
            sharedPreferences
                    .edit()
                    .putLong(Constant.Preference.HB_LAST_TIME, curTime)
                    .apply();
        } catch (Throwable tr) {
            Alog.e("出异常了", tr);
        }
    }
}

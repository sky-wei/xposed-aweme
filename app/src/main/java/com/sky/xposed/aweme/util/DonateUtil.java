package com.sky.xposed.aweme.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.sky.xposed.aweme.Constant;

public class DonateUtil {

    static final String ALI_PAY_URI = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=";

    private static final String[] sPassword = {
            "https://qr.alipay.com/c1x04363ygoxzzemfcnw998",
            "https://qr.alipay.com/c1x06511qhtctpjl7i53dc1",
            "https://qr.alipay.com/c1x09864q6dvxcl3j5sbe59",
            "https://qr.alipay.com/c1x045169bydeurgmjyqo5f",
            "https://qr.alipay.com/c1x021348atoeiqk8s4728a",
            "https://qr.alipay.com/c1x0520266gznqkclmw1n07"
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

    /**
     * 一键领取支付宝红包
     * @param context
     */
    public static void receiveAliPayHb(Context context) {

        if (!startAlipay(context,
                sPassword[RandomUtil.randomIndex(sPassword.length)])) {
            VToast.show("启动支付宝失败");
        }
    }

    /**
     * 启动支付宝
     * @param context
     * @param payUrl
     * @return
     */
    public static boolean startAlipay(Context context, String payUrl) {

        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(ALI_PAY_URI + payUrl));

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                intent.setData(Uri.parse(payUrl));
                context.startActivity(intent);
            }
            return true;
        } catch (Throwable tr) {
            Alog.e("启动失败", tr);
            return false;
        }
    }
}

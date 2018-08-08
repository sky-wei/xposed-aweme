package com.sky.xposed.aweme.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.common.util.Alog;
import com.sky.xposed.common.util.RandomUtil;
import com.sky.xposed.common.util.ToastUtil;

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

    /**
     * 显示红包提示框
     * @param context
     * @param sharedPreferences
     */
    public static void showHbDialog(final Context context, SharedPreferences sharedPreferences) {

        long curTime = System.currentTimeMillis();
        long lastTime = sharedPreferences.getLong(Constant.Preference.HB_LAST_TIME, 0);

        if (curTime > lastTime
                && curTime - lastTime < Constant.Time.HB_MAX_TIME) {
            // 不需要处理
            return;
        }

        // 显示提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("感谢您的使用，如果觉得助手好用，每天领个红包也是对作者最好的支持！ 谢谢！");
        builder.setCancelable(false);
        builder.setPositiveButton("去领取", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 去领取红包
                receiveAliPayHb(context);
            }
        });
        builder.setNegativeButton("残忍拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 保存最后时间
                saveLastHbTime(context);
            }
        });
        builder.show();
    }

    /**
     * 保存最后时间
     * @param context
     */
    private static void saveLastHbTime(Context context) {

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constant.Name.AWE_ME, Context.MODE_PRIVATE);

        long curTime = System.currentTimeMillis();
        // 保存最后时间
        sharedPreferences
                .edit()
                .putLong(Constant.Preference.HB_LAST_TIME, curTime)
                .apply();
    }

    /**
     * 一键领取支付宝红包
     * @param context
     */
    public static void receiveAliPayHb(Context context) {

        saveLastHbTime(context);

        if (!startAlipay(context,
                sPassword[RandomUtil.randomIndex(sPassword.length)])) {
            ToastUtil.show("启动支付宝失败");
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

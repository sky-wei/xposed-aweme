package com.sky.xposed.aweme.ui.base;

import android.content.SharedPreferences;

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.common.ui.base.BaseDialogFragment;

/**
 * Created by sky on 2018/8/8.
 */
public abstract class BaseDialog extends BaseDialogFragment {

    @Override
    public SharedPreferences getDefaultSharedPreferences() {
        return getSharedPreferences(Constant.Name.AWE_ME);
    }
}

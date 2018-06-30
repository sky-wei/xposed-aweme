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

package com.sky.xposed.aweme.ui.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sky.xposed.aweme.Constant;
import com.sky.xposed.aweme.ui.view.SimpleItemView;
import com.sky.xposed.aweme.ui.view.SwitchItemView;

/**
 * Created by starrysky on 16-8-2.
 */
public class ViewUtil {

    public static void setVisibility(View view, int visibility) {

        if (view == null || view.getVisibility() == visibility) return ;

        view.setVisibility(visibility);
    }

    public static void setVisibility(int visibility, View... views) {

        if (views == null) return ;

        for (View view : views) {
            setVisibility(view, visibility);
        }
    }

    public static void setTypeface(Typeface typeface, TextView... textViews) {

        if (typeface == null || textViews == null) return ;

        for (TextView textView : textViews) {
            textView.setTypeface(typeface);
        }
    }

    public static String getText(TextView textView) {
        return textView != null ? charSequenceToString(textView.getText()) : null;
    }

    public static String charSequenceToString(CharSequence charSequence) {
        return charSequence != null ? charSequence.toString() : null;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    public static boolean canVerticalScroll(EditText editText) {

        if (editText == null) return false;

        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    public static StateListDrawable newBackgroundDrawable() {

        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(0xffe5e5e5));
        drawable.addState(new int[] {}, new ColorDrawable(Color.WHITE));

        return drawable;
    }

    public static void setInputType(EditText editText, int inputType) {

        switch (inputType) {
            case Constant.InputType.NUMBER:
                editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                break;
            case Constant.InputType.NUMBER_DECIMAL:
                editText.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL | EditorInfo.TYPE_CLASS_NUMBER);
                break;
            case Constant.InputType.NUMBER_SIGNED:
                editText.setInputType(EditorInfo.TYPE_NUMBER_FLAG_SIGNED | EditorInfo.TYPE_CLASS_NUMBER);
                break;
            case Constant.InputType.PHONE:
                editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                break;
            case Constant.InputType.TEXT:
                editText.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case Constant.InputType.TEXT_PASSWORD:
                editText.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case Constant.InputType.NUMBER_PASSWORD:
                editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
        }
    }

    public static View newLineView(Context context) {

        View lineView = new View(context);
        lineView.setBackgroundColor(0xFFDFDFDF);
        lineView.setLayoutParams(LayoutUtil.newViewGroupParams(
                FrameLayout.LayoutParams.MATCH_PARENT, 2));
        return lineView;
    }

    public static SimpleItemView newSimpleItemView(Context context, String name) {

        SimpleItemView itemView = new SimpleItemView(context);
        itemView.setName(name);

        return itemView;
    }

    public static SwitchItemView newSwitchItemView(Context context, String name) {

        SwitchItemView itemView = new SwitchItemView(context);
        itemView.setName(name);

        return itemView;
    }
}

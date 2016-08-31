package com.su.scott.slibrary.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/**
 * @类名 DialogUtil
 * @描述 对话框创建工具类
 * @作者 Su
 * @时间 2015年12月
 */
public class DialogUtil {

    public static AlertDialog showDialog(@NonNull Activity activity, @Nullable CharSequence title, @NonNull CharSequence msg, @Nullable CharSequence textPos, @Nullable DialogInterface.OnClickListener listenerPos, @Nullable CharSequence textNeg, @Nullable DialogInterface.OnClickListener listenerNeg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (title != null) {
            builder.setTitle(title);
        }/*else {
            builder.setTitle("");
        }*/
        builder.setMessage((msg == null ? "" : msg));
        builder.setPositiveButton((textPos == null ? "确定" : textPos), (listenerPos == null ? new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } : listenerPos));
        builder.setNegativeButton((textNeg == null ? "取消" : textNeg), (listenerNeg == null ? new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } : listenerNeg));
        AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }

}

package com.miri.launcher.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miri.launcher.R;

/**
 * 自定义样式的Toast弹出框
 */
public class CustomToast extends Toast {

    int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

    int mX, mY;

    public enum MsgType {
        INFO, WARN, SUCCESS, ERROR
    }

    public CustomToast(Context context) {
        super(context);
        mY = context.getResources().getDimensionPixelSize(R.dimen.toast_y_offset);
    }

    @Override
    public void show() {
        super.show();
        setGravity(mGravity, mX, mY);
    }

    /**
     * 生成一个包含字符串text的自定义Toast对话框，并指定消息类型（包括警告、成功、失败）
     * @param context The context to use. Usually your {@link android.app.Application} or
     *            {@link android.app.Activity} object.
     * @param text The text to show. Can be formatted text.
     * @param duration How long to display the message. Either {@link #LENGTH_SHORT} or
     *            {@link #LENGTH_LONG}
     * @param msgType The msg type to show. Different type with show different style{@link #MsgType}
     */
    public static CustomToast makeText(Context context, CharSequence text, int duration,
            MsgType msgType) {
        CustomToast result = new CustomToast(context);

        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.custom_toast, null);
        TextView tv = (TextView) v.findViewById(R.id.toast_text);
        tv.setText(text);
        if (msgType != MsgType.INFO) {
            int left = context.getResources().getDimensionPixelSize(R.dimen.toast_padding_left);
            tv.setPadding(left, 0, 0, 0);
            if (msgType == MsgType.WARN) {
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toast_warning, 0, 0, 0);
            } else if (msgType == MsgType.SUCCESS) {
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toast_success, 0, 0, 0);
            } else if (msgType == MsgType.ERROR) {
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toast_error, 0, 0, 0);
            }
        }

        result.setView(v);
        result.setDuration(duration);

        return result;
    }

    /**
     * 生成一个包含资源字符串resId的自定义Toast对话框，并指定消息类型（包括警告、成功、失败）
     * @param context The context to use. Usually your {@link android.app.Application} or
     *            {@link android.app.Activity} object.
     * @param resId The resource id of the string resource to use. Can be formatted text.
     * @param duration How long to display the message. Either {@link #LENGTH_SHORT} or
     *            {@link #LENGTH_LONG}
     * @param msgType The msg type to show. Different type with show different style{@link #MsgType}
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static CustomToast makeText(Context context, int resId, int duration, MsgType msgType)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration, msgType);
    }

    /**
     * 生成一个包含字符串text的自定义Toast对话框
     * @param context The context to use. Usually your {@link android.app.Application} or
     *            {@link android.app.Activity} object.
     * @param text The text to show. Can be formatted text.
     * @param duration How long to display the message. Either {@link #LENGTH_SHORT} or
     *            {@link #LENGTH_LONG}
     */
    public static CustomToast makeText(Context context, CharSequence text, int duration) {
        return makeText(context, text, duration, MsgType.INFO);
    }

    /**
     * 生成一个包含资源字符串resId的自定义Toast对话框
     * @param context The context to use. Usually your {@link android.app.Application} or
     *            {@link android.app.Activity} object.
     * @param resId The resource id of the string resource to use. Can be formatted text.
     * @param duration How long to display the message. Either {@link #LENGTH_SHORT} or
     *            {@link #LENGTH_LONG}
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static CustomToast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

}

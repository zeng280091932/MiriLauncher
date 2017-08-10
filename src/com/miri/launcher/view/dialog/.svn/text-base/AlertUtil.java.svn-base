package com.miri.launcher.view.dialog;

import android.content.Context;

public class AlertUtil {

	/**
	 * Show Alert Dialog
	 * 
	 * @param context
	 * @param titleId
	 * @param messageId
	 */
	public static void showAlert(Context context, int titleId, int messageId) {
		showAlert(context, context.getResources().getString(titleId), context
				.getResources().getString(messageId));
	}

	/**
	 * Show Alert Dialog
	 * 
	 * @param context
	 * @param titleId
	 * @param messageId
	 */
	public static void showAlert(Context context, String title, String message) {
		AlertDialog alertDlg = new AlertDialog(context);
		alertDlg.setTitle(title);
		alertDlg.setMessage(message);
		alertDlg.show();
	}

	/**
	 * 显示提示对话框
	 * 
	 * @param context
	 * @param messageId
	 * @param okBtnText
	 * @param okBtnClickListener
	 *            确定按钮的监听器
	 * @param cancelText
	 * @param cancelBtnListener
	 *            取消按钮的监听器
	 * @see [类、类#方法、类#成员]
	 * @since [产品/模块版本]
	 */
	public static void showAlert(Context context, int titleId, int messageId,
			String okBtnText,
			AlertDialog.OnOkBtnClickListener okBtnClickListener,
			String cancelText,
			AlertDialog.OnCancelBtnClickListener cancelBtnListener) {
		showAlert(context, context.getResources().getString(titleId), context
				.getResources().getString(messageId), okBtnText,
				okBtnClickListener, cancelText, cancelBtnListener);
	}

	/**
	 * 显示提示对话框
	 * @param context
	 * @param titleId
	 * @param messageId
	 * @param okBtnTextId
	 * @param okBtnClickListener
	 * @param cancelTextId
	 * @param cancelBtnListener
	 */
	public static void showAlert(Context context, int titleId, int messageId,
			int okBtnTextId,
			AlertDialog.OnOkBtnClickListener okBtnClickListener,
			int cancelTextId,
			AlertDialog.OnCancelBtnClickListener cancelBtnListener) {
		showAlert(context, context.getResources().getString(titleId), context
				.getResources().getString(messageId), context.getResources()
				.getString(okBtnTextId), okBtnClickListener, context
				.getResources().getString(cancelTextId), cancelBtnListener);
	}

	/**
	 * 显示提示对话框
	 * 
	 * @param context
	 * @param msg
	 * @param okBtnText
	 * @param okBtnClickListener
	 * @param cancelText
	 * @param cancelBtnListener
	 */
	public static void showAlert(Context context, String title, String msg,
			String okBtnText,
			AlertDialog.OnOkBtnClickListener okBtnClickListener,
			String cancelText,
			AlertDialog.OnCancelBtnClickListener cancelBtnListener) {
		AlertDialog alertDlg = new AlertDialog(context);
		alertDlg.setTitle(title);
		alertDlg.setMessage(msg);
		alertDlg.addOkBtn(okBtnText, okBtnClickListener);
		alertDlg.addCancelBtn(cancelText, cancelBtnListener);
		alertDlg.show();
	}

}

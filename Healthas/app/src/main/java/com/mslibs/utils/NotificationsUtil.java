package com.mslibs.utils;

import android.content.Context;
import android.widget.Toast;

public class NotificationsUtil {
	private static final String TAG = "NotificationsUtil";
	private static final boolean DEBUG = Preferences.DEBUG;

	public static void ToastMessage(Context context, String message) {
		if (message != null)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	public static void ToastLongMessage(Context context, String message) {
		if (message != null)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}

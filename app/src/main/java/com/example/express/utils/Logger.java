package com.example.express.utils;

import java.io.IOException;

import android.util.Log;

import com.example.express.constants.CommonConstants;

public class Logger {
	
	public static void show(String TAG, String msg) {
		if (!CommonConstants.isShowLog) {
			return;
		}
		show(TAG, msg, Log.INFO);
	}

	public static void show(String TAG, String msg, int level) {
		if (!CommonConstants.isShowLog) {
			return;
		}
		switch (level) {
		case Log.VERBOSE:
			Log.v(TAG, msg);
			break;
		case Log.DEBUG:
			Log.d(TAG, msg);
			break;
		case Log.INFO:
			Log.i(TAG, msg);
			break;
		case Log.WARN:
			Log.w(TAG, msg);
			break;
		case Log.ERROR:
			Log.e(TAG, msg);
			break;
		default:
			Log.i(TAG, msg);
			break;
		}
	}

	static void saveToFile(String log, String fileName) {
		if(!CommonConstants.isSaveLog2File) {
			return;
		}
		try {
			Runtime.getRuntime().exec("logcat -v time -f" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package cn.carl.communicationLib.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyLog {
	/** 日志输出目录的名称，一般用项目名 */
	private static String logPath = "guided";
	/** 是否调试，调试时输出日志到Logcat和文件 */
	public static Boolean isDebug = true;
	/** 是否输出日志到文件，如果为false只输出到logcat */
	public static Boolean isPrintingLog = true;
	public static String Second_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "carl"
			+ File.separator + logPath + File.separator;

	private final static DateFormat format = new SimpleDateFormat(
			"MM-dd HH:mm:ss.SSS", Locale.CHINA);
	private final static DateFormat format2 = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINA);

	private static String TAG = "lifei";

	public static String getLogPath() {
		return logPath;
	}

	public static void setLogPath(String logPath) {
		MyLog.logPath = logPath;
		Second_PATH = Environment.getExternalStorageDirectory()
				+ File.separator + "carl" + File.separator + logPath
				+ File.separator;
	}

	public static void d(String msg){
		if (isPrintingLog) {
			printingLog("D:" + TAG, msg);
		}
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isPrintingLog) {
			printingLog("D:" + tag, msg);
		}
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable throwable) {
		if (isPrintingLog) {
			printingLog("D:" + tag, msg, throwable);
		}
		if (isDebug) {
			Log.d(tag, msg, throwable);
		}
	}

	public static void e(String tag, String msg) {
		if (isPrintingLog) {
			printingLog("E:" + tag, msg);
		}
		if (isDebug) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable throwable) {
		if (isPrintingLog) {
			printingLog("E:" + tag, msg, throwable);
		}
		if (isDebug) {
			Log.e(tag, msg, throwable);
		}
	}

	public static void i(String tag, String msg) {
		if (isPrintingLog) {
			printingLog("I:" + tag, msg);
		}
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable throwable) {
		if (isPrintingLog) {
			printingLog("I:" + tag, msg, throwable);
		}
		if (isDebug) {
			Log.i(tag, msg, throwable);
		}
	}

	public static void v(String tag, String msg) {
		if (isPrintingLog) {
			printingLog("V:" + tag, msg);
		}
		if (isDebug) {
			Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable throwable) {
		if (isPrintingLog) {
			printingLog("V:" + tag, msg, throwable);
		}
		if (isDebug) {
			Log.v(tag, msg, throwable);
		}
	}

	public static void w(String tag, String msg) {
		if (isPrintingLog) {
			printingLog("W:" + tag, msg);
		}
		if (isDebug) {
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable throwable) {
		if (isPrintingLog) {
			printingLog("W:" + tag, msg, throwable);
		}
		if (isDebug) {
			Log.w(tag, msg, throwable);
		}
	}

	public static void w(String tag, Throwable throwable) {
		if (isPrintingLog) {
			printingLog("W:" + tag, "", throwable);
		}
		if (isDebug) {
			Log.w(tag, throwable);
		}
	}

	private static void printingLog(String tag, String msg) {
		printing(tag + "\t" + msg);
	}

	private static void printingLog(String tag, String msg, Throwable throwable) {
		if (throwable != null) {
			printing(tag + "\t" + msg);
			printing(tag + "\t55555555555555555" + msg + "-getMessage():"
					+ throwable.getMessage() + "-cause():"
					+ throwable.getCause() + "-throwable:");
			for (StackTraceElement temp : throwable.getStackTrace()) {
				printing(tag + "\t" + msg + temp.getFileName() + "  "
						+ temp.getClassName() + "  " + temp.getLineNumber()
						+ "  " + temp.getMethodName());
				printing(tag + "\t666666666666666" + msg + temp.toString());
			}
		} else {
			printing(tag + "\t" + msg);
		}
	}

	private static Date t = new Date();
	private static File file;
	private static BufferedReader in;
	private static PrintWriter out;

	private synchronized static void printing(String date) {
		try {
			t = new Date();
			in = new BufferedReader(new StringReader(date));
			out = new PrintWriter(new BufferedWriter(new FileWriter(Second_PATH
					+ "log" + format2.format(t) + ".txt", true)));
			String currentLineData = "";
			while ((currentLineData = in.readLine()) != null) {
				out.println(format.format(t) + " " + currentLineData);
				out.write("\r\n");
				// out.flush();
			}
		} catch (EOFException e) {
			Log.e("MyLog", "printing(): End of stream");
		} catch (Exception e1) {
			Log.e("MyLog", "printing(): " + e1.getMessage());
			e1.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	public static void onExit() {
		System.out.println("输出用时： 退出");
		if (out != null)
			out.close();
	}

	public static void init() {
		try {
			file = new File(Second_PATH + "log" + format2.format(t) + ".txt");
			// 父目录是否存在
			if (!file.getParentFile().exists()) {
				System.out.println("路径:" + file.getParentFile().getPath()
						+ " 不存在！");
				// 父的父目录是否存在
				if (!file.getParentFile().getParentFile().exists()) {
					System.out.println("路径:"
							+ file.getParentFile().getParentFile().getPath()
							+ " 不存在！");
					// 不存在就建立此目录
					file.getParentFile().getParentFile().mkdir();
				}
				// 不存在就建立此目录
				file.getParentFile().mkdir();
			}
			if (!file.getParentFile().exists()) {
				System.out.println("路径2:" + file.getParentFile().getPath()
						+ " 不存在！");
				// 父的父目录是否存在
				if (!file.getParentFile().getParentFile().exists()) {
					System.out.println("路径2:"
							+ file.getParentFile().getParentFile().getPath()
							+ " 不存在！");
					// 不存在就建立此目录
					file.getParentFile().getParentFile().mkdir();
				}
				// 不存在就建立此目录
				file.getParentFile().mkdir();
			}
			if (!file.exists()) {
				System.out.println("路径3:" + file.getPath() + " 不存在！");
				file.createNewFile();
			}
			out = new PrintWriter(new BufferedWriter(new FileWriter(Second_PATH
					+ "log" + format2.format(t) + ".txt", true)));
		} catch (EOFException e) {
			Log.e("MyLog", "file: printing(): End of stream");
		} catch (Exception e1) {
			Log.e("MyLog", "file: printing(): " + e1.getMessage());
			e1.printStackTrace();
		}
	}
}

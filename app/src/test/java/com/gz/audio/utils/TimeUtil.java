package com.gz.audio.utils;


import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具
 */
public class TimeUtil {

	// 时间格式模板
	//check
	public static final String TIME_FORMAT_CHECK= "yyyyMMddHHmm";
	/** yyyy-MM-dd */
	public static final String TIME_FORMAT_ONE = "yyyy-MM-dd";
	/** yyyy-MM-dd HH:mm */
	public static final String TIME_FORMAT_TWO = "yyyy-MM-dd HH:mm";
	/** yyyy-MM-dd HH:mmZ */
	public static final String TIME_FORMAT_THREE = "yyyy-MM-dd HH:mmZ";
	/** yyyy-MM-dd HH:mm:ss */
	public static final String TIME_FORMAT_FOUR = "yyyy-MM-dd HH:mm:ss";
	/** yyyy-MM-dd HH:mm:ss.SSSZ */
	public static final String TIME_FORMAT_FIVE = "yyyy-MM-dd HH:mm:ss.SSSZ";
	/** yyyy-MM-dd'T'HH:mm:ss.SSSZ */
	public static final String TIME_FORMAT_SIX = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	/** HH:mm:ss */
	public static final String TIME_FORMAT_SEVEN = "HH:mm:ss";
	/** HH:mm:ss.SS */
	public static final String TIME_FORMAT_EIGHT = "HH:mm:ss.SS";
	/** yyyy.MM.dd */
	public static final String TIME_FORMAT_9 = "yyyy.MM.dd";
	/** MM月dd日 */
	public static final String TIME_FORMAT_10 = "MM月dd日";
	public static final String TIME_FORMAT_11 = "MM-dd HH:mm";
	public static final String TIME_FORMAT_12 = "yyMM";
	public static final String TIME_FORMAT_13 = "yyyyMMdd-HH";
	/** HH:mm */
	public static final String TIME_FORMAT_14 = "HH:mm";
	public static final String TIME_FORMAT_15 = "MM-dd";
	public static final String TIME_FORMAT_16 = "yy-MM-dd";
	public static final String TIME_FORMAT_17="dd/MM E HH:mm";
	public static final String TIME_FORMAT_18="yyyy/MM/dd";
	public static final String TIME_FORMAT_19="yyyyMMdd";
	public static final String TIME_FORMAT_20="yyyy年MM月dd日";
	public static final String TIME_FORMAT_21= "yyyyMMddHHmmss";
	// 时间常量
	private static final int SECOUND_OF_HOUR = 3600;
	private static final int SECOUND_OF_MIN = 60;
	/**
	 * 年
	 */

	/**
	 * 根据时间格式获得当前时间
	 */
	public static String getCurrentTime(String formater) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(formater,
				Locale.SIMPLIFIED_CHINESE);
		return dateFormat.format(date);
	}

	/** 格式化时间 */
	public static String formatTime(long time, String format) {
		return new SimpleDateFormat(format).format(new Date(time));
	}

	/** 判断是否是合法的时间 */
	public static boolean isValidDate(String dateString, String format) {
		return parseTime(dateString, format) > -1;
	}

	/** 日期转换 */
	public static long parseTime(String dateString, String format) {
		if (dateString == null || dateString.length() == 0) {
			return -1;
		}
		try {
			return new SimpleDateFormat(format).parse(dateString).getTime();
		} catch (ParseException e) {

		}
		return -1;
	}

	public static int getDaysBetween(String date1, String date2, String format) {
		return getDaysBetween(parseTime(date1, format),
				parseTime(date2, format));
	}

	public static int getDaysBetween(long date1, long date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(date1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(date2);
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);

		return (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / (24 * 3600 * 1000));
	}

	/**
	 * Unix时间戳转换成日期
	 */
	public static String TimeStamp2Date(String timestampString, String formater) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new SimpleDateFormat(formater, Locale.SIMPLIFIED_CHINESE)
				.format(new Date(timestamp));
		return date;
	}

	public static long getTodayTimeMillis() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static String getTimeByLong4Msg(long tLong) {
		String strDate = "";
		tLong = tLong * 1000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tLong);
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
		// strDate = cal.getTime().toLocaleString();
		strDate = sdf.format(cal.getTime());
		return strDate;
	}

	/**
	 * 获取默认格式的时间
	 * 
	 * @return "MM-dd HH:mm"
	 */
	public static String getDefaultTime() {
		String strDate = "";
		long currentTime = System.currentTimeMillis();
		currentTime = currentTime * 1000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(currentTime);
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		// strDate = cal.getTime().toLocaleString();
		strDate = sdf.format(cal.getTime());
		return strDate;
	}

	/**
	 * 用户自己设置指定时间格式的日期
	 * 
	 * @param format
	 *            日期格式
	 * @return 日期
	 */
	public static String getTimeByLong(String format) {
		String strDate = "";
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		strDate = sdf.format(cal.getTime());
		return strDate;
	}

	/**
	 * 去除字符串中的空格、回车、换行符、制表符
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * String转long
	 */
	public static long parseLong(String str, long defaultValue) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 判断字符串是否有效
	 * 
	 * @deprecated
	 */
	public static boolean isValidate(String str) {
		if (str != null && str.length() > 0) {
			return true;
		}
		return false;
	}


	/**
	 * 
	* <p>Title: stringToDate</p>
	* <p>Description:  时间排序</p>
	* @param dateString
	* @return
	 */
	
	 public static Date stringToDate(long dateString) {
		 	String datalong = String.valueOf(dateString);
	        ParsePosition position = new ParsePosition(0);
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Date dateValue = simpleDateFormat.parse(datalong, position);
	        return dateValue;  
	    }

	/**
	 * 返回指定日期距离19700101的时间差（毫秒）
	 */
	public static String get15TimeStap() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -16);
		return Long.toString(c.getTimeInMillis());
	}

	/**
	 * 给定秒钟时间转成小时，分钟，秒
	 * 
	 * @param secStr
	 * @return
	 */
	public static String durationTime(String secStr) {
		if (secStr != null && secStr != "") {
			try {
				int second = Integer.parseInt(secStr);
				int h = 0;
				int d = 0;
				int s = 0;
				int temp = second % 3600;
				if (second > 3600) {
					h = second / 3600;
					if (temp != 0) {
						if (temp > 60) {
							d = temp / 60;
							if (temp % 60 != 0) {
								s = temp % 60;
							}
						} else {
							s = temp;
						}
					}
				} else {
					d = second / 60;
					if (second % 60 != 0) {
						s = second % 60;
					}
				}
				if (h == 0 && d != 0) {
					return d + "分" + s + "秒";
				} else if (h == 0 && d == 0) {
					return s + "秒";
				}
				return h + "时" + d + "分" + s + "秒";
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return "0秒";
	}

	/**
	 * 毫秒转换为时分秒
	 */
	public static String toHMS(Object s) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		return formatter.format(s);
	}

	/**
	 * 日期转换为毫秒
	 */
	public static long toSS(String s) {
		long timeMS = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			timeMS = sdf.parse(s).getTime();
		} catch (ParseException e) {
		}
		return timeMS;
	}

	/**
	 * 毫秒转换为日期
	 */
	public static String toStringDate(long s) {
		Date date = new Date(s);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 毫秒转换为日期
	 */
	public static Date toDate(long s) {
		Date d = null;
		Date date = new Date(s);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = format.format(date);
		try {
			format.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static String getDateStr(String day,int Num) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = null;
		try {
			nowDate = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//如果需要向后计算日期 -改为+
		Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}



	public static boolean isDateOneBigger(String str1, String str2) {
		boolean isBigger = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt1 = null;
		Date dt2 = null;
		try {
			dt1 = sdf.parse(str1);
			dt2 = sdf.parse(str2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (dt1.getTime() > dt2.getTime()) {
			isBigger = true;
		} else if (dt1.getTime() < dt2.getTime()) {
			isBigger = false;
		}
		return isBigger;
	}
}

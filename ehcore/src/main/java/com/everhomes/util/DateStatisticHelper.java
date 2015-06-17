package com.everhomes.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atomikos.datasource.ResourceException;

public class DateStatisticHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(DateStatisticHelper.class);
	
	/**
	 * 获取当前UTC日期时间。
	 * 
	 * @return 当前UTC日期时间
	 */
	public static Date getCurrentUTCTime()
	{
		Calendar cal = Calendar.getInstance();

		// cal.setTimeInMillis(cal.getTimeInMillis() -
		// TimeZone.getDefault().getRawOffset());

		return cal.getTime();
	}

	/**
	 * 获取当天0点时间 （0时0分0秒）。
	 * 
	 * @return 当前0时0分0秒时间
	 */
	public static Date getCurrent0Hour()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTime();
	}
	
	/**
	 * 获取指定日期往后数（过去）<code>dayCount</code>天的那一天的开始时间（0时0分0秒）
	 * 
	 * @param date 指定日期
	 * @param dayCount 往后数（过去）多少天
	 * @param isInclude 是否包含指定日期当天
	 * @return 开始时间
	 */
	public static Date getStartDateOfLastNDays(Date date, int dayCount, boolean isInclude)
	{
		// 如果包含当天则减少一天
		if (isInclude)
		{
			dayCount--;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, (day - dayCount));
		calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
		calendar.set(GregorianCalendar.MINUTE, 0);
		calendar.set(GregorianCalendar.SECOND, 0);
		calendar.set(GregorianCalendar.MILLISECOND, 0);

		return calendar.getTime();
	}
	
	
}

package com.everhomes.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateUtils {
	//取当前时间，只取到小时
	public static final String HOUR_FORMAT_STRING = "yyyy-MM-dd HH";
	
	public static Date getCurrentHour(){
    	return formatHour(DateHelper.currentGMTTime());
	}
	
	public static Date formatHour(Date srcDate){
		SimpleDateFormat format = new SimpleDateFormat(HOUR_FORMAT_STRING);
    	// 不要用这个方法来转时间，会错位
//    	Date now = DateHelper.parseDataString(format.format(DateHelper.currentGMTTime()), formatString);
    	Date date = null;
    	try {
			date = format.parse(format.format(srcDate));
		} catch (Exception e) {
			date = new Date();
		}
    	return date;
	}

	public static Timestamp getLaterTime(Timestamp time1, Timestamp time2) {
		if(time1 != null) {
			if(time2 == null) {
				return time1;
			}
			if (time1.after(time2)){
				return time1;
			}
		}
		return time2;
	}

	public static Timestamp currentTimestamp() {
		return Timestamp.from(Instant.now());
	}
}

package com.everhomes.util;

import com.everhomes.util.excel.ExcelUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.regex.Pattern;

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


	static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	static DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	static DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	/**
	 * 将不明格式的日期转给指定格式的日期，如果所提供日期不是有效输入，返回null by wentian 2018/3/29
	 * @param dateStr 不明格式的日期
	 * @param format 想要获得的日期格式
	 */
    public static String guessDateTimeFormatAndFormatIt(String dateStr, String format) {
		String regex1 = "^\\d{4}/\\d{2}/\\d{2}\\s?\\d{2}:\\d{2}$";   // 2018/02/12 12:12
		String regex2 = "^\\d{4}-\\d{2}-\\d{2}\\s?\\d{2}:\\d{2}$";   // 2018-02-12 12:12
		String regex3 = "^\\d{4}-\\d{2}-\\d{2}\\s?$";				 // 2018-02-12
		String regex4 = "^\\d{4}/\\d{2}/\\d{2}\\s?$";                // 2018/12/31
        String regex5 = "\\d+";                                      // 时间戳
		Pattern pattern1 = Pattern.compile(regex1);
		Pattern pattern2 = Pattern.compile(regex2);
		Pattern pattern3 = Pattern.compile(regex3);
		Pattern pattern4 = Pattern.compile(regex4);
		Pattern pattern5 = Pattern.compile(regex5);
		DateTimeFormatter desired_format = DateTimeFormatter.ofPattern(format);
		TemporalAccessor q = null;
		try{
            if(pattern1.matcher(dateStr).matches()){
                q = formatter1.parse(dateStr);
            }else if(pattern2.matcher(dateStr).matches()){
                q =formatter2.parse(dateStr);
            }else if(pattern3.matcher(dateStr).matches()){
                q = formatter3.parse(dateStr);
            }else if(pattern4.matcher(dateStr).matches()){
                q = formatter4.parse(dateStr);
            }else if(pattern5.matcher(dateStr).matches()){
                Long l = Long.parseLong(dateStr);
                Date date = new Date(l);
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format(date);
            }
            String result = desired_format.format(q);
            return result;
        }catch (Exception e){
		    e.printStackTrace();
        }
        return null;
    }
}

package com.everhomes.util;

import com.everhomes.util.excel.ExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
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




//	/**
//	 * 将不明格式的日期转给指定格式的日期，如果所提供日期不是有效输入，返回null by wentian 2018/3/29
//	 * @param dateStr 不明格式的日期
//	 * @param format 想要获得的日期格式
//	 */
//    public static String guessDateTimeFormatAndFormatIt(String dateStr, String format) {
//		// excel的写入和获取没有考虑日期格式问题，以恶心代码来应对恶心代码
//		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy/MM/dd");
//		SimpleDateFormat formatter5 = new SimpleDateFormat("yyyy/MM");
//		SimpleDateFormat mdyy = new SimpleDateFormat("M/d/yy");
//
//
//		String regex1 = "^\\d{4}/\\d{2}/\\d{2}\\s?\\d{2}:\\d{2}$";   // 2018/02/12 12:12
//		String regex2 = "^\\d{4}-\\d{2}-\\d{2}\\s?\\d{2}:\\d{2}$";   // 2018-02-12 12:12
//		String regex3 = "^\\d{4}-\\d{2}-\\d{2}\\s?$";				 // 2018-02-12
//		String regex4 = "^\\d{4}/\\d{2}/\\d{2}\\s?$";                // 2018/12/31
//        String regex5 = "\\d+";                                      // 时间戳
//		String regex6 = "\\d{2}/\\d+/\\d{2}";                      // 12/1/18
////		String regex7 = "\\d{2}-\\d+-\\d{2}";                      // 12-1-18
//		Pattern pattern1 = Pattern.compile(regex1);
//		Pattern pattern2 = Pattern.compile(regex2);
//		Pattern pattern3 = Pattern.compile(regex3);
//		Pattern pattern4 = Pattern.compile(regex4);
//		Pattern pattern5 = Pattern.compile(regex5);
//		Pattern pattern6 = Pattern.compile(regex6);
////		Pattern pattern7 = Pattern.compile(regex7);
//		SimpleDateFormat desired_format = new SimpleDateFormat(format);
//		Date q = null;
//		try{
//            if(pattern1.matcher(dateStr).matches()){
//                q = formatter1.parse(dateStr);
//            }else if(pattern2.matcher(dateStr).matches()){
//                q =formatter2.parse(dateStr);
//            }else if(pattern3.matcher(dateStr).matches()){
//                q = formatter3.parse(dateStr);
//            }else if(pattern4.matcher(dateStr).matches()){
//                q = formatter4.parse(dateStr);
//            }else if(pattern5.matcher(dateStr).matches()){
//                Long l = Long.parseLong(dateStr);
//                Date date = new Date(l);
//                SimpleDateFormat sdf = new SimpleDateFormat(format);
//                return sdf.format(date);
//            }else if(dateStr.contains("月")){
//            	String rephrased = "20" + dateStr.substring(dateStr.lastIndexOf("月")+2) + "/" + getNumber(dateStr.substring(0, dateStr.indexOf("月")));
//				q = formatter5.parse(rephrased);
//			}else if(pattern6.matcher(dateStr).matches()){
//				q = mdyy.parse(dateStr);
//			}
//
//            String result = desired_format.format(q);
//            return result;
//        }catch (Exception e){
//		    e.printStackTrace();
//        }
//        return null;
//    }

	/**
	 * 将不明格式的日期转给指定格式的日期，如果所提供日期不是有效输入，返回null by wentian 2018/3/29
	 * @param dateStr 不明格式的日期
	 */
	public static String guessDateTimeFormatAndFormatIt(String dateStr, String desired_format) {
		if(StringUtils.isEmpty(dateStr)) return null;
		// excel的写入和获取没有考虑日期格式问题，以恶心代码来应对恶心代码
		SimpleDateFormat yyyyMMddDash = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat yyyyMMDash = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy/MM");
		SimpleDateFormat mdyy = new SimpleDateFormat("M/d/yy");
		SimpleDateFormat myy = new SimpleDateFormat("M/yy");
		SimpleDateFormat request = new SimpleDateFormat(desired_format);


		String yyyyMMddDash_ = "^\\d{4}-\\d{2}-\\d{2}$";				 // 2018-02-12
		String yyyyMMdd_ = "^\\d{4}/\\d{2}/\\d{2}$";                // 2018/12/31
		String yyyyMMDash_ = "^\\d{4}-\\d{2}$";                      // 2018-02
		String yyyyMM_ = "^\\d{4}/\\d{2}$";                   // 2018/12
		String mdyy_ = "^\\d{1,2}/\\d{1,2}/\\d{2}$";            // 3/1/18
		String myy_ = "^\\d{1,2}/\\d{2}$";            // 3/18
		Pattern pattern1 = Pattern.compile(yyyyMMddDash_);
		Pattern pattern2 = Pattern.compile(yyyyMMdd_);
		Pattern pattern3 = Pattern.compile(yyyyMMDash_);
		Pattern pattern4 = Pattern.compile(yyyyMM_);
		Pattern pattern5 = Pattern.compile(mdyy_);
		Pattern pattern6 = Pattern.compile(myy_);

		Date q = null;
		String formatted = dateStr;
		try{
			if(pattern1.matcher(dateStr).matches()){
				q = yyyyMMddDash.parse(dateStr);
			}
			else if(pattern2.matcher(dateStr).matches()){
				q =yyyyMMdd.parse(dateStr);
			}
			else if(pattern3.matcher(dateStr).matches()){
				q = yyyyMMDash.parse(dateStr);
			}
			else if(pattern4.matcher(dateStr).matches()){
				q = yyyyMM.parse(dateStr);
			}
			else if(pattern5.matcher(dateStr).matches()){
				q = mdyy.parse(dateStr);
			}
			else if(pattern6.matcher(dateStr).matches()){
				q = myy.parse(dateStr);
			}
			else{
				return null;
			}
			formatted = request.format(q);
			return formatted;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将不明格式的日期转给指定格式的日期，如果所提供日期不是有效输入，返回null by wentian 2018/3/29
	 * @param dateStr 不明格式的日期
	 */
	public static Calendar guessDateTimeFormatAndParse(String dateStr) {
		if(StringUtils.isEmpty(dateStr)) return null;
		// excel的写入和获取没有考虑日期格式问题，以恶心代码来应对恶心代码
		SimpleDateFormat yyyyMMddDash = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat yyyyMMDash = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy/MM");
		SimpleDateFormat mdyy = new SimpleDateFormat("M/d/yy");
		SimpleDateFormat myy = new SimpleDateFormat("M/yy");


		String yyyyMMddDash_ = "^\\d{4}-\\d{2}-\\d{2}$";				 // 2018-02-12
		String yyyyMMdd_ = "^\\d{4}/\\d{2}/\\d{2}$";                // 2018/12/31
		String yyyyMMDash_ = "^\\d{4}-\\d{2}$";                      // 2018-02
		String yyyyMM_ = "^\\d{4}/\\d{2}$";                   // 2018/12
		String mdyy_ = "^\\d{1,2}/\\d{1,2}/\\d{2}$";            // 3/1/18
		String myy_ = "^\\d{1,2}/\\d{2}$";            // 3/18
		Pattern pattern1 = Pattern.compile(yyyyMMddDash_);
		Pattern pattern2 = Pattern.compile(yyyyMMdd_);
		Pattern pattern3 = Pattern.compile(yyyyMMDash_);
		Pattern pattern4 = Pattern.compile(yyyyMM_);
		Pattern pattern5 = Pattern.compile(mdyy_);
		Pattern pattern6 = Pattern.compile(myy_);
		Calendar calendar = Calendar.getInstance();
		Date q = null;
		String formatted = dateStr;
		try{
			if(pattern1.matcher(dateStr).matches()){
				q = yyyyMMddDash.parse(dateStr);
			}
			else if(pattern2.matcher(dateStr).matches()){
				q =yyyyMMdd.parse(dateStr);
			}
			else if(pattern3.matcher(dateStr).matches()){
				q = yyyyMMDash.parse(dateStr);
			}
			else if(pattern4.matcher(dateStr).matches()){
				q = yyyyMM.parse(dateStr);
			}
			else if(pattern5.matcher(dateStr).matches()){
				q = mdyy.parse(dateStr);
			}
			else if(pattern6.matcher(dateStr).matches()){
				q = myy.parse(dateStr);
			}
			else{
				return null;
			}
			calendar.setTime(q);
			return calendar;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static String getNumber(String c) {
    	switch (c){
    		case "一":
    			return "01";
			case "二":
				return "02";
			case "三":
				return "03";
			case "四":
				return "04";
			case "五":
				return "05";
			case "六":
				return "06";
			case "七":
				return "07";
			case "八":
				return "08";
			case "九":
				return "09";
			case "十一":
				return "11";
			case "十二":
				return "12";
			case "十":
				return "10";
			default:
				return "";
		}
	}


	/**
	 * 比较两日期相差的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(Date date1 , Date date2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if(year2 < year1){ //设置year2在year1之后
			int year = year2 ;
			year2 = year1 ;
			year1 = year ;
			int day = day1;
			day1 = day2 ;
			day2 = day;
		}
		if(year1 != year2)   //不同一年
		{
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++)
			{
				if(i%4==0 && i%100!=0 || i%400==0)    //闰年
				{
					timeDistance += 366;
				}
				else    //不是闰年
				{
					timeDistance += 365;
				}
			}

			return timeDistance + (day2-day1) ;
		}
		else    //同年
		{
			//System.out.println("判断day2 - day1 : " + (day2-day1));
			return day2-day1;
		}
	}

	/**
	 * 获取指定日期几天前或后的日期
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date dateAfterOrBeforeDays( Date date , int days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE,days);
		return cal.getTime();

	}
}

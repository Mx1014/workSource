package com.everhomes.asset.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author created by ycx
 * @date 下午7:19:23
 */
public class TimeUtils {
	
	/* 
     * 将时间转换为时间戳
     */    
    public static Long dateToTimestamp(String s) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        Long ts = date.getTime();
        return ts;
    }
    
    /* 
     * 将时间戳转换为时间
     */
    public static String timestampToDate(Long time){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    public static void main(String[] args) {
		try {
			System.out.println(dateToTimestamp("2018-11-09 19:30:00"));
			System.out.println(timestampToDate(1541763000000L));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}

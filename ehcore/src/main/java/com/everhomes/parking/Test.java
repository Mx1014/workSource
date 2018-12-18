package com.everhomes.parking;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sw on 2017/10/26.
 */
public class Test {
    public static void main(String[] args) {
    	DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			Date temp = dft.parse("2018-10-01 00:00:00");
			Long e = temp.getTime();
	        System.out.println(e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        System.out.println();
        System.out.println(new Date(1512057599999L));
        System.out.println("test auto compile");
        System.out.println("111111111111");
    }
}

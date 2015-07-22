package com.everhomes.organization;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class BillingAccountHelper {
	
	public static String getAccountNumberByBillingAccountTypeCode(byte b){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date();
		String dateStr = format.format(date);
		Long mills = date.getTime()%1000;
		Random random = new Random();
		int randomNumber = random.nextInt(89)+10;
		return b+dateStr+mills+randomNumber;
	}
	
	

}

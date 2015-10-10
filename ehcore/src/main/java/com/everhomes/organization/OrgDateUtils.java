package com.everhomes.organization;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDateUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrgDateUtils.class);
	final private static int [] randomHours = {8,9,10,11,12,13,14,15,16,17};
	
	public static Date getRadomTime(Date createDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(createDate);
		int hourInt = (int)(Math.random()*10-1);
		int hour = randomHours[hourInt];
		
		if(LOGGER.isDebugEnabled())
			LOGGER.info("getRadomTime-hour="+hour);
		
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE,(int)(Math.random()*60));
		cal.set(Calendar.SECOND,(int)(Math.random()*60));
		
		if(LOGGER.isDebugEnabled())
			LOGGER.info("getRadomTime-time="+cal.getTime());
		return cal.getTime();
	}
}

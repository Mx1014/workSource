
package com.everhomes.asset.calculate;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetCalculateUtil{
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetCalculateUtil.class);
	
	public int getNatualQuarterMonthOffset(Calendar d) {
		int monthOffset;//自然季的月份偏移量
		int array[][] = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};   
        int season = 1;  
        int month = d.get(Calendar.MONTH) + 1;
        if(month >=1 && month <= 3){
            season = 1; 
        } 
        if(month >=4 && month <= 6){   
            season = 2;  
        }   
        if(month >=7 && month <= 9){   
            season = 3;
        }   
        if(month >=10 && month <= 12){   
            season = 4;   
        }
        int end_month = array[season-1][2];
        monthOffset = end_month - month;
        return monthOffset;
	}
	
	
}
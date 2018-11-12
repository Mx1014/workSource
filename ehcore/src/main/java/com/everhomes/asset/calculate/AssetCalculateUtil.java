
package com.everhomes.asset.calculate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.asset.calculate.NatualQuarterMonthDTO;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetCalculateUtil{
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetCalculateUtil.class);
	
	/**
	 * 自然季的月份偏移量
	 */
	public NatualQuarterMonthDTO getNatualQuarterMonthOffset(Calendar d) {
		NatualQuarterMonthDTO dto = new NatualQuarterMonthDTO();
		int array[][] = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};   
        int season = 1;  
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH) + 1;
        if(month >=1 && month <= 3){
            season = 1; 
            dto.setDateStrBegin(year + "-" + "01-01");
            dto.setDateStrEnd(year + "-" + "03-31");
        }
        if(month >=4 && month <= 6){
            season = 2;  
            dto.setDateStrBegin(year + "-" + "04-01");
            dto.setDateStrEnd(year + "-" + "06-30");
        }   
        if(month >=7 && month <= 9){   
            season = 3;
            dto.setDateStrBegin(year + "-" + "07-01");
            dto.setDateStrEnd(year + "-" + "09-30");
        }   
        if(month >=10 && month <= 12){   
            season = 4;   
            dto.setDateStrBegin(year + "-" + "10-01");
            dto.setDateStrEnd(year + "-" + "12-31");
        }
        int end_month = array[season-1][2];
        int monthOffset = end_month - month;
        dto.setMonthOffset(monthOffset);
        return dto;
	}
	
	public static void main(String[] args) throws ParseException {
		Calendar d = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		d.setTime(sdf.parse("2018-02-06"));
		AssetCalculateUtil assetCalculateUtil = new AssetCalculateUtil();
		assetCalculateUtil.getNatualQuarterMonthOffset(d);
	}
	
	
}
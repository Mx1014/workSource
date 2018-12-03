
package com.everhomes.asset.calculate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.asset.calculate.NatualQuarterMonthDTO;
import com.everhomes.util.RuntimeErrorException;

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
	
	/**
	 * 自然季的计算系数
	 * 2018-08-30至2018-09-30 ： 计算系数r = (1 + 2/31) / 3
	 * 2018-10-01至2018-12-31 ：计算系数r = 1
	 */
	public String getReductionFactor(Calendar d, Calendar a) {
		Calendar beginDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		beginDate.setTime(a.getTime());
		endDate.setTime(d.getTime());
		StringBuilder reductionFactor = new StringBuilder();
		reductionFactor.append("(");
		Calendar aEnd = Calendar.getInstance();
		while(endDate.compareTo(beginDate) >= 0) {
			aEnd.setTime(beginDate.getTime());
			aEnd.set(Calendar.DAY_OF_MONTH, aEnd.getActualMaximum(Calendar.DAY_OF_MONTH));//获取当前月份的最后一天
			//如果计算月份的最后一天大于该自然季的范围，那么以自然季结束日期作为计算参数
			if(endDate.compareTo(aEnd) > 0) {
				int dayOfMonth = beginDate.getActualMaximum(Calendar.DAY_OF_MONTH);//获取当前月份的总天数：如31
				int daysBetween = daysBetween(aEnd, beginDate);//获取当前日期到月底的天数：如2
				reductionFactor.append(daysBetween + "/" + dayOfMonth);
				//把时间定位到下一个月的第一天
				beginDate.setTime(aEnd.getTime());
				beginDate.add(Calendar.DAY_OF_MONTH, 1);
				if(endDate.compareTo(beginDate) >= 0) {
					reductionFactor.append("+");
				}
			}else {
				int dayOfMonth = beginDate.getActualMaximum(Calendar.DAY_OF_MONTH);//获取当前月份的总天数：如31
				int daysBetween = daysBetween(endDate, beginDate);//获取当前日期到月底的天数：如2
				reductionFactor.append(daysBetween + "/" + dayOfMonth);
				break;
			}
		}
		reductionFactor.append(")");
		reductionFactor.append("/3");
		return reductionFactor.toString();
	}
	
	private int daysBetween(Calendar c1, Calendar c2) {
        SimpleDateFormat ez = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ez.parse(ez.format(c2.getTime()));
            return daysBetween_date(ez.parse(ez.format(c2.getTime())), ez.parse(ez.format(c1.getTime())));
        }catch (Exception e){

            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE, "no way to lose");
        }
    }
	
    private int daysBetween_date(Date c1, Date c2) {
        long time1 = c1.getTime();
        long time2 = c2.getTime();
        Long between_days=Math.abs(time2-time1)/(1000*3600*24);
        return between_days.intValue() + 1;
    }
	
	public static void main(String[] args) throws ParseException {
		Calendar d = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		d.setTime(sdf.parse("2018-02-06"));
		AssetCalculateUtil assetCalculateUtil = new AssetCalculateUtil();
		assetCalculateUtil.getNatualQuarterMonthOffset(d);
		
		Calendar a = Calendar.getInstance();
		a.setTime(sdf.parse("2018-08-30"));
		d.setTime(sdf.parse("2018-09-30"));
		System.out.println(assetCalculateUtil.getReductionFactor(d, a));
		
		String formula = "9000 * [(1 + 2/31) / 3]";
		ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
		try {
			System.out.println(jse.eval(formula));
			BigDecimal result2 = new BigDecimal(jse.eval(formula).toString());
			result2 = result2.setScale(2,BigDecimal.ROUND_HALF_UP);
	        System.out.println(result2);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	
	
}
package com.everhomes.asset.ExpressionParse;

import java.util.Calendar;
import java.util.Date;

/**
 * 自定义自然周期
 */
public class CustomNaturalPeriod extends ExpressionParseProcess {
    @Override
    public void calculatePeriod(ExpressionParseConfig config, Date startTime) {
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        int year = startTimeCalendar.get(Calendar.YEAR);
        //因为是自然周期所以要对整年进行分割
        for (int i=0; i<12/config.getBillingCustomCycle();i++){
            CycleRange range = new CycleRange();
            //设置一个周期的开始时间
            Calendar cycleStartTime = Calendar.getInstance();
            cycleStartTime.set(Calendar.YEAR,year);
            //当BillingCustomStartDate为-1时，表示月底
            if (config.getBillingCustomStartDate()==-1){
                cycleStartTime.set(Calendar.MONTH,i*config.getBillingCustomCycle()+config.getBillingCustomCycleOffset());
                cycleStartTime.set(Calendar.DAY_OF_MONTH,1);
                cycleStartTime.add(Calendar.DAY_OF_MONTH,-1);
            }else {
                cycleStartTime.set(Calendar.MONTH,i*config.getBillingCustomCycle()+config.getBillingCustomCycleOffset()-1);
                cycleStartTime.set(Calendar.DAY_OF_MONTH,config.getBillingCustomStartDate());
            }

            range.setStartTime(cycleStartTime.getTime());
            //设置一个周期的结束时间
            if (config.getBillingCustomStartDate()==-1){
                cycleStartTime.add(Calendar.MONTH,config.getBillingCustomCycle()+1);
                cycleStartTime.set(Calendar.DAY_OF_MONTH,1);
                cycleStartTime.add(Calendar.DAY_OF_MONTH,-2);
            }else {
                cycleStartTime.add(Calendar.MONTH,config.getBillingCustomCycle());
                cycleStartTime.add(Calendar.DAY_OF_MONTH,-1);
            }
            range.setStopTime(cycleStartTime.getTime());
            super.getRanges().add(range);
        }
        //将最后一个周期的年份减1，将范围扩大，周期补全。
        int size = super.getRanges().size();
        CycleRange range = super.getRanges().get(size-1);
        CycleRange newRange = new CycleRange();
        Calendar calendarStartTime = Calendar.getInstance();
        calendarStartTime.setTime(range.getStartTime());
        calendarStartTime.add(Calendar.YEAR,-1);
        newRange.setStartTime(calendarStartTime.getTime());
        Calendar calendarStopTime = Calendar.getInstance();
        calendarStopTime.setTime(range.getStopTime());
        calendarStopTime.add(Calendar.YEAR,-1);
        newRange.setStopTime(calendarStopTime.getTime());
        super.getRanges().add(0,newRange);
    }

    public  String show(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.YEAR)+"年")+
                (calendar.get(Calendar.MONTH)+1+"月")+
                (calendar.get(Calendar.DAY_OF_MONTH)+"日")+
                (calendar.get(Calendar.HOUR)+"时")+
                (calendar.get(Calendar.MINUTE)+"分"+"\n");
    }

    public static void main(String[] args) {
        ExpressionParseProcess expressionParseProcess = new CustomNaturalPeriod();
        CustomNaturalPeriod CNP = new CustomNaturalPeriod();
        Date dateStrBegin = new Date();
        ExpressionParseCommand cmd = new ExpressionParseCommand();
        cmd.setBillingCycleExpression("{\"billingCustomStartDate\":\"-1\",\"billingCustomCycle\":1,\"billingCustomCycleOffset\":\"1\"}");
        ExpressionParseConfig expressionParseConfig = new ExpressionParseConfig();
        expressionParseConfig.init(ExpressionParseUtil.parse(cmd.getBillingCycleExpression()));
        expressionParseProcess.calculatePeriod(expressionParseConfig,dateStrBegin);
        for (CycleRange range:expressionParseProcess.getRanges()){
            System.out.print("开始时间:"+CNP.show(range.getStartTime()));
            System.out.println("结束时间:"+CNP.show(range.getStopTime()));
        }
    }
}

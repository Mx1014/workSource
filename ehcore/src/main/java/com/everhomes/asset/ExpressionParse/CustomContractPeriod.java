package com.everhomes.asset.ExpressionParse;

import java.util.Calendar;
import java.util.Date;

/**
 * 自定义合同周期
 */
public class CustomContractPeriod extends ExpressionParseProcess {

    @Override
    public void calculatePeriod(ExpressionParseConfig config, Date startTime) {
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        int year = startTimeCalendar.get(Calendar.YEAR);
        int month = startTimeCalendar.get(Calendar.MONTH);
        //因为是合同周期，所以只需列出当前周期，上一个周期，下一个周期即可覆盖账单有效期开始时间
        for (int i= -1;i<2;i++ ){
            CycleRange range = new CycleRange();
            //设置一个周期的开始时间
            Calendar cycleStartTime = Calendar.getInstance();
            cycleStartTime.set(Calendar.YEAR,year);
            //当BillingCustomStartDate为-1时，表示月底
            if (config.getBillingCustomStartDate()==-1){
                cycleStartTime.set(Calendar.MONTH,month+i*config.getBillingCustomCycle()+1);
                cycleStartTime.set(Calendar.DAY_OF_MONTH,1);
                cycleStartTime.add(Calendar.DAY_OF_MONTH,-1);
            }else {
                cycleStartTime.set(Calendar.MONTH,month+i*config.getBillingCustomCycle());
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

       ExpressionParseProcess expressionParseProcess = new CustomContractPeriod();
       CustomContractPeriod CCP = new CustomContractPeriod();
       Date dateStrBegin = new Date();
       ExpressionParseCommand cmd = new ExpressionParseCommand();
       cmd.setBillingCycleExpression("{\"billingCustomStartDate\":\"2\",\"billingCustomCycle\":\"12\"}");
       ExpressionParseConfig expressionParseConfig = new ExpressionParseConfig();
       expressionParseConfig.init(ExpressionParseUtil.parse(cmd.getBillingCycleExpression()));
       expressionParseProcess.calculatePeriod(expressionParseConfig,dateStrBegin);
       for (CycleRange range:expressionParseProcess.getRanges()){
           System.out.print("开始时间:"+CCP.show(range.getStartTime()));
           System.out.println("结束时间:"+CCP.show(range.getStopTime()));
       }
    }
}

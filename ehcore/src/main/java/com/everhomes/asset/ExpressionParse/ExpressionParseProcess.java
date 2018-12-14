package com.everhomes.asset.ExpressionParse;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public abstract class ExpressionParseProcess {
    Logger LOGGER = Logger.getLogger(ExpressionParseProcess.class);

    //存储账单每个周期的时间范围
    private List<CycleRange> ranges = new ArrayList<>();
    //默认有效期开始时间为当前
    private Date startTime = new Date();

    public List<CycleRange> getRanges() {
        return ranges;
    }

    public void setRanges(List<CycleRange> ranges) {
        this.ranges = ranges;
    }

    //计算账单周期
    abstract public void calculatePeriod(ExpressionParseConfig config, Date startTime);

    /**
     *
     * @param stopTime 上个周期结束时间
     * @return
     */
    //计算下个周期的开始时间
    public Date calculateNextPeriodStartTime(Date stopTime){
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(stopTime);
        startTimeCalendar.add(Calendar.DAY_OF_MONTH,1);
        return startTimeCalendar.getTime();
    }

    /**
     *
     * @param startTime 下个周期开始时间
     * @param startDay 每个周期开始日期，用来判断月底情况
     * @param cycle 周期
     * @return
     */
    //计算下个周期的结束时间()
    public Date calculateNextPeriodStopTime(Date startTime,Integer startDay,Integer cycle){
        Calendar stopTimeCalendar = Calendar.getInstance();
        stopTimeCalendar.setTime(startTime);
        if (startDay == -1){
            stopTimeCalendar.add(Calendar.MONTH,cycle+1);
            stopTimeCalendar.set(Calendar.DAY_OF_MONTH,1);
            stopTimeCalendar.add(Calendar.DAY_OF_MONTH,-2);
        }else {
            stopTimeCalendar.add(Calendar.MONTH,cycle);
            stopTimeCalendar.add(Calendar.DAY_OF_MONTH,-1);
        }
        return stopTimeCalendar.getTime();
    }

    //计算第一个周期的结束时间
    public Date calculateFirstPeriodStoptTime(Date startTime) {
        if (startTime==null){
            startTime = this.startTime;
        }
        for (CycleRange range:ranges){
            if (startTime.compareTo(range.getStartTime())>=0&&
                startTime.compareTo(range.getStopTime())<=0){
                return range.getStopTime();
            }
        }
        LOGGER.error("Can't find first period stopTime");
        return null;
    }

}

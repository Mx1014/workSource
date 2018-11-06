package com.everhomes.techpark.punch;

import com.everhomes.rest.techpark.punch.PunchTimeIntervalDTO;
import com.everhomes.uniongroup.UniongroupVersion;

import java.util.Date;
import java.util.List;

public interface PunchBaseService {

    PunchTimeRule getPunchTimeRuleGuDing(PunchRule pr, Date date);

    List<PunchTimeIntervalDTO> findPunchTimeIntervals(PunchTimeRule ptr);

    UniongroupVersion getPunchGroupVersion(Long enterpriseId);

    PunchHoliday checkHoliday(PunchRule pr, java.sql.Date punchDate);

    Integer getWeekDayInt(java.sql.Date punchDate);

}

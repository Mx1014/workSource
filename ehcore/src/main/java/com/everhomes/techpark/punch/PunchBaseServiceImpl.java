package com.everhomes.techpark.punch;

import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.techpark.punch.NormalFlag;
import com.everhomes.rest.techpark.punch.PunchDayType;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchRuleStatus;
import com.everhomes.rest.techpark.punch.PunchServiceErrorCode;
import com.everhomes.rest.techpark.punch.PunchTimeIntervalDTO;
import com.everhomes.rest.uniongroup.UniongroupType;
import com.everhomes.uniongroup.UniongroupVersion;
import com.everhomes.uniongroup.UniongroupVersionProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PunchBaseServiceImpl implements PunchBaseService {
    @Autowired
    private PunchProvider punchProvider;
    @Autowired
    private UniongroupVersionProvider uniongroupVersionProvider;
    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public PunchTimeRule getPunchTimeRuleGuDing(PunchRule pr, Date date) {
        //看是否为特殊日期
        PunchSpecialDay specialDay = punchProvider.findSpecialDayByDateAndOrgId(pr.getPunchOrganizationId(), date);
        if (null != specialDay) {
            if (specialDay.getStatus().equals(NormalFlag.YES.getCode())) {
                return new PunchTimeRule(0L, PunchDayType.RESTDAY);
            } else {
                return punchProvider.getPunchTimeRuleById(specialDay.getTimeRuleId());
            }
        }
        //如果为节假日则返回null  如果是节假调休日,用调休日期代替
        java.sql.Date punchDate = new java.sql.Date(date.getTime());
        PunchHoliday punchHoliday = checkHoliday(pr, punchDate);
        if (punchHoliday != null) {
            if (punchHoliday.getStatus().equals(NormalFlag.YES.getCode())) {
                return new PunchTimeRule(0L, NormalFlag.YES == NormalFlag.fromCode(punchHoliday.getLegalFlag()) ? PunchDayType.HOLIDAY : PunchDayType.RESTDAY);
            } else {
                punchDate = punchHoliday.getExchangeFromDate();
            }
        }
        if (punchDate == null) {
            return new PunchTimeRule(0L, PunchDayType.RESTDAY);
        }
        //2018-8-28:删除中未删除的状态的timeRule也要被查询
        List<Byte> statusList = new ArrayList<>();
        statusList.add(PunchRuleStatus.ACTIVE.getCode());
        statusList.add(PunchRuleStatus.DELETING.getCode());
        //看是循环timerule找当天的timeRule
        List<PunchTimeRule> timeRules = punchProvider.listActivePunchTimeRuleByOwnerAndStatusList(PunchOwnerType.ORGANIZATION.getCode(), pr.getPunchOrganizationId(), statusList);
        if (null != timeRules)
            for (PunchTimeRule timeRule : timeRules) {
                Integer openWeek = Integer.parseInt(timeRule.getOpenWeekday(), 2);
                Integer weekDayInt = getWeekDayInt(punchDate);
                if (weekDayInt.equals(openWeek & weekDayInt)) {
                    return punchProvider.getPunchTimeRuleById(timeRule.getId());
                }
            }
        return new PunchTimeRule(0L, PunchDayType.RESTDAY);
    }

    @Override
    public Integer getWeekDayInt(java.sql.Date punchDate) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(punchDate);
        int CalendarWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
        switch (CalendarWeekDay) {
            case Calendar.SUNDAY:
                return PunchConstants.SUNDAY_INT;
            case Calendar.MONDAY:
                return PunchConstants.MONDAY_INT;
            case Calendar.TUESDAY:
                return PunchConstants.TUESDAY_INT;
            case Calendar.WEDNESDAY:
                return PunchConstants.WEDNESDAY_INT;
            case Calendar.THURSDAY:
                return PunchConstants.THURSDAY_INT;
            case Calendar.FRIDAY:
                return PunchConstants.FRIDAY_INT;
            case Calendar.SATURDAY:
                return PunchConstants.SATURDAY_INT;
        }
        return 0;
    }

    @Override
    public PunchHoliday checkHoliday(PunchRule pr, java.sql.Date punchDate) {
        if (pr == null) {
            return null;
        }
        if (null != pr.getChinaHolidayFlag() && pr.getChinaHolidayFlag().equals(NormalFlag.YES.getCode())) {
            return punchProvider.findHolidayByDate(punchDate);
        }
        return null;
    }

    @Override
    public List<PunchTimeIntervalDTO> findPunchTimeIntervals(PunchTimeRule ptr) {
        if (null == ptr || ptr.getId() == null || ptr.getId() == 0) {
            return null;
        }
        List<PunchTimeIntervalDTO> timeIntervals = new ArrayList<>();
        if (ptr.getPunchTimesPerDay().intValue() == 2) {
            PunchTimeIntervalDTO timeInterval = new PunchTimeIntervalDTO();
            timeInterval.setArriveTime(ptr.getStartEarlyTimeLong());
            timeInterval.setLeaveTime(ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong());
            timeIntervals.add(timeInterval);
            return timeIntervals;
        }
        if (ptr.getPunchTimesPerDay().intValue() == 4) {
            PunchTimeIntervalDTO timeInterval1 = new PunchTimeIntervalDTO();
            timeInterval1.setArriveTime(ptr.getStartEarlyTimeLong());
            timeInterval1.setLeaveTime(ptr.getNoonLeaveTimeLong());
            PunchTimeIntervalDTO timeInterval2 = new PunchTimeIntervalDTO();
            timeInterval2.setArriveTime(ptr.getAfternoonArriveTimeLong());
            timeInterval2.setLeaveTime(ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong());
            timeIntervals.add(timeInterval1);
            timeIntervals.add(timeInterval2);
            return timeIntervals;
        }

        List<PunchTimeInterval> punchTimeIntervals = punchProvider.listPunchTimeIntervalByTimeRuleId(ptr.getId());
        if (CollectionUtils.isEmpty(punchTimeIntervals)) {
            return Collections.emptyList();
        }
        return punchTimeIntervals.stream().map(punchTimeInterval -> {
            PunchTimeIntervalDTO dto = ConvertHelper.convert(punchTimeInterval, PunchTimeIntervalDTO.class);
            dto.setArriveTime(punchTimeInterval.getArriveTimeLong());
            dto.setLeaveTime(punchTimeInterval.getLeaveTimeLong());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取公司当前的version,如果没有,则新建一个从1开始
     */
    @Override
    public UniongroupVersion getPunchGroupVersion(Long enterpriseId) {
        UniongroupVersion uv = uniongroupVersionProvider.findUniongroupVersion(enterpriseId, UniongroupType.PUNCHGROUP.getCode());
        if (null == uv) {
            Organization org = organizationProvider.findOrganizationById(enterpriseId);
            if (null == org) {

                throw RuntimeErrorException.errorWith(
                        PunchServiceErrorCode.SCOPE,
                        PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_FOUND,
                        "didnt find enterprise");
            }
            uv = new UniongroupVersion();
            uv.setNamespaceId(org.getNamespaceId());
            uv.setGroupType(UniongroupType.PUNCHGROUP.getCode());
            uv.setCurrentVersionCode(1);
            uv.setEnterpriseId(enterpriseId);
            uniongroupVersionProvider.createUniongroupVersion(uv);
        }
        return uv;
    }
}

// @formatter:off
package com.everhomes.techpark.punch.recordmapper;

import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.techpark.punch.PunchStatistic;
import com.everhomes.util.ConvertHelper;

public class PunchStatisticRecordMapper implements RecordMapper<Record, PunchStatistic> {    
    @SuppressWarnings("unchecked")
    @Override
    public PunchStatistic map(Record r) {
        PunchStatistic result = new PunchStatistic();
        result.setId(r.getValue((Field<Long>)r.field("id")));
        result.setPunchMonth(r.getValue((Field<String>)r.field("punch_month")));
        result.setOwnerType(r.getValue((Field<String>)r.field("owner_type")));
        result.setOwnerId(r.getValue((Field<Long>)r.field("owner_id")));
        result.setUserId(r.getValue((Field<Long>)r.field("user_id")));
        result.setUserName(r.getValue((Field<String>)r.field("user_name")));
        result.setDeptId(r.getValue((Field<Long>)r.field("dept_id")));
        result.setDeptName(r.getValue((Field<String>)r.field("dept_name")));
        result.setWorkDayCount(r.getValue((Field<Integer>)r.field("work_day_count")));
        result.setWorkCount(r.getValue((Field<Double>)r.field("work_count")));
        result.setBelateCount(r.getValue((Field<Integer>)r.field("belate_count")));
        result.setLeaveEarlyCount(r.getValue((Field<Integer>)r.field("leave_early_count")));
        result.setBlandleCount(r.getValue((Field<Integer>)r.field("blandle_count")));
        result.setUnpunchCount(r.getValue((Field<Double>)r.field("unpunch_count")));
        result.setAbsenceCount(r.getValue((Field<Double>)r.field("absence_count")));
        result.setSickCount(r.getValue((Field<Double>)r.field("sick_count")));
        result.setExchangeCount(r.getValue((Field<Double>)r.field("exchange_count")));
        result.setOutworkCount(r.getValue((Field<Double>)r.field("outwork_count")));
        result.setRestDayCount(r.getValue((Field<Integer>)r.field("rest_day_count")));
        result.setFullNormalFlag(r.getValue((Field<Byte>)r.field("full_normal_flag")));
        result.setAskForLeaveRequestCount(r.getValue((Field<Integer>)r.field("ask_for_leave_request_count")));
        result.setGoOutRequestCount(r.getValue((Field<Integer>)r.field("go_out_request_count")));
        result.setBusinessTripRequestCount(r.getValue((Field<Integer>)r.field("business_trip_request_count")));
        result.setOvertimeRequestCount(r.getValue((Field<Integer>)r.field("overtime_request_count")));
        result.setPunchExceptionRequestCount(r.getValue((Field<Integer>)r.field("punch_exception_request_count")));
        result.setOverTimeSum(r.getValue((Field<Long>)r.field("over_time_sum")));
        result.setPunchTimesPerDay(r.getValue((Field<Byte>)r.field("punch_times_per_day")));
        result.setExceptionStatus(r.getValue((Field<Byte>)r.field("exception_status")));
        result.setDescription(r.getValue((Field<String>)r.field("description")));
        result.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid")));
        result.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time")));
        result.setExts(r.getValue((Field<String>)r.field("exts")));
        result.setUserStatus(r.getValue((Field<Byte>)r.field("user_status")));
        result.setPunchOrgName(r.getValue((Field<String>)r.field("punch_org_name")));
        result.setDetailId(r.getValue((Field<Long>)r.field("detail_id")));
        result.setExceptionDayCount(r.getValue((Field<Integer>)r.field("exception_day_count")));
        result.setAnnualLeaveBalance(r.getValue((Field<Double>)r.field("annual_leave_balance")));
        result.setOvertimeCompensationBalance(r.getValue((Field<Double>)r.field("overtime_compensation_balance")));
        result.setDeviceChangeCounts(r.getValue((Field<Integer>)r.field("device_change_counts")));
        result.setExceptionRequestCounts(r.getValue((Field<Integer>)r.field("exception_request_counts")));
        result.setBelateTime(r.getValue((Field<Long>)r.field("belate_time")));
        result.setLeaveEarlyTime(r.getValue((Field<Long>)r.field("leave_early_time")));
        result.setForgotPunchCountOffDuty(r.getValue((Field<Integer>)r.field("forgot_punch_count_off_duty")));
        result.setForgotPunchCountOnDuty(r.getValue((Field<Integer>)r.field("forgot_punch_count_on_duty")));
        result.setStatusList(r.getValue((Field<String>)r.field("status_list")));
        result.setOvertimeTotalWorkday(r.getValue((Field<Long>)r.field("overtime_total_workday")));
        result.setOvertimeTotalRestday(r.getValue((Field<Long>)r.field("overtime_total_restday")));
        result.setOvertimeTotalLegalHoliday(r.getValue((Field<Long>)r.field("overtime_total_legal_holiday")));

        return result;
    }
}

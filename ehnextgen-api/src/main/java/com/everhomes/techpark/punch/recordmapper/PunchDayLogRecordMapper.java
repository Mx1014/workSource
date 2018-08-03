// @formatter:off
package com.everhomes.techpark.punch.recordmapper;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.techpark.punch.PunchDayLog;
import com.everhomes.techpark.punch.PunchStatistic;
import com.everhomes.util.ConvertHelper;

public class PunchDayLogRecordMapper implements RecordMapper<Record, PunchDayLog> {    
    @SuppressWarnings("unchecked")
    @Override
    public PunchDayLog map(Record r) {
    	PunchDayLog result = new PunchDayLog();
    	result.setId(r.getValue((Field<Long>)r.field("id")));
    	result.setUserId(r.getValue((Field<Long>)r.field("user_id")));
    	result.setDetailId(r.getValue((Field<Long>)r.field("detail_id")));
    	result.setEnterpriseId(r.getValue((Field<Long>)r.field("enterprise_id")));
    	result.setDeptId(r.getValue((Field<Long>)r.field("dept_id")));
    	result.setPunchDate(r.getValue((Field<Date>)r.field("punch_date")));
    	result.setArriveTime(r.getValue((Field<Time>)r.field("arrive_time")));
    	result.setLeaveTime(r.getValue((Field<Time>)r.field("leave_time")));
    	result.setWorkTime(r.getValue((Field<Time>)r.field("work_time")));
    	result.setStatus(r.getValue((Field<Byte>)r.field("status")));
    	result.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid")));
    	result.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time")));
    	result.setViewFlag(r.getValue((Field<Byte>)r.field("view_flag")));
    	result.setMorningStatus(r.getValue((Field<Byte>)r.field("morning_status")));
    	result.setAfternoonStatus(r.getValue((Field<Byte>)r.field("afternoon_status")));
    	result.setPunchTimesPerDay(r.getValue((Field<Byte>)r.field("punch_times_per_day")));
    	result.setNoonLeaveTime(r.getValue((Field<Time>)r.field("noon_leave_time")));
    	result.setAfternoonArriveTime(r.getValue((Field<Time>)r.field("afternoon_arrive_time")));
    	result.setExceptionStatus(r.getValue((Field<Byte>)r.field("exception_status")));
    	result.setDeviceChangeFlag(r.getValue((Field<Byte>)r.field("device_change_flag")));
    	result.setStatusList(r.getValue((Field<String>)r.field("status_list")));
    	result.setPunchCount(r.getValue((Field<Integer>)r.field("punch_count")));
    	result.setPunchOrganizationId(r.getValue((Field<Long>)r.field("punch_organization_id")));
    	result.setRuleType(r.getValue((Field<Byte>)r.field("rule_type")));
    	result.setTimeRuleName(r.getValue((Field<String>)r.field("time_rule_name")));
    	result.setTimeRuleId(r.getValue((Field<Long>)r.field("time_rule_id")));
    	result.setApprovalStatusList(r.getValue((Field<String>)r.field("approval_status_list")));
    	result.setSmartAlignment(r.getValue((Field<String>)r.field("smart_alignment")));
    	result.setBelateTimeTotal(r.getValue((Field<Long>)r.field("belate_time_total")));
    	result.setLeaveEarlyTimeTotal(r.getValue((Field<Long>)r.field("leave_early_time_total")));
    	result.setOvertimeTotalWorkday(r.getValue((Field<Long>)r.field("overtime_count_workday")));
    	result.setOvertimeTotalRestday(r.getValue((Field<Long>)r.field("overtime_count_restday")));
    	result.setOvertimeTotalLegalHoliday(r.getValue((Field<Long>)r.field("overtime_count_legal_holiday")));
    	result.setRestFlag(r.getValue((Field<Byte>)r.field("rest_flag")));
    	result.setAbsentFlag(r.getValue((Field<Byte>)r.field("absent_flag")));
    	result.setNormalFlag(r.getValue((Field<Byte>)r.field("normal_flag")));
    	result.setBelateCount(r.getValue((Field<Integer>)r.field("belate_count")));
    	result.setLeaveEarlyCount(r.getValue((Field<Integer>)r.field("leave_early_count")));
    	result.setForgotPunchCountOnDuty(r.getValue((Field<Integer>)r.field("forgot_punch_count_on_duty")));
    	result.setForgotPunchCountOffDuty(r.getValue((Field<Integer>)r.field("forgot_punch_count_off_duty")));
    	result.setAskForLeaveRequestCount(r.getValue((Field<Integer>)r.field("ask_for_leave_request_count")));
    	result.setGoOutRequestCount(r.getValue((Field<Integer>)r.field("go_out_request_count")));
    	result.setBusinessTripRequestCount(r.getValue((Field<Integer>)r.field("business_trip_request_count")));
    	result.setOvertimeRequestCount(r.getValue((Field<Integer>)r.field("overtime_request_count")));
    	result.setPunchExceptionRequestCount(r.getValue((Field<Integer>)r.field("punch_exception_request_count")));
    	result.setSplitDateTime(r.getValue((Field<Timestamp>)r.field("split_date_time")));

        return result;
    }
}

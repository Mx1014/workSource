package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhPunchDayLogsRecord;

public class EhPunchDayLogMapper implements RecordMapper<Record,  EhPunchDayLogsRecord> {    
    @SuppressWarnings("unchecked")
    @Override
    public EhPunchDayLogsRecord map(Record r) {
    	EhPunchDayLogsRecord post = new EhPunchDayLogsRecord();  
        post.setId(r.getValue((Field<Long>)r.field("id"))); 
        post.setEnterpriseId(r.getValue((Field<Long>)r.field("company_id")));
        post.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time"))); 
        post.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid"))); 
        post.setArriveTime(r.getValue((Field<Time>)r.field("arrive_time"))); 
        post.setLeaveTime(r.getValue((Field<Time>)r.field("leave_time"))); 
        post.setWorkTime(r.getValue((Field<Time>)r.field("work_time"))); 
        post.setPunchDate(r.getValue((Field<Date>)r.field("punch_date"))); 
        post.setStatus(r.getValue((Field<Byte>)r.field("status"))); 
        post.setUserId(r.getValue((Field<Long>)r.field("user_id"))); 
 
        return post;
    }
}

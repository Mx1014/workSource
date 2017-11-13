package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
 

import com.everhomes.server.schema.tables.records.EhPunchExceptionRequestsRecord;

public class EhPunchExceptionRequestMapper implements RecordMapper<Record, EhPunchExceptionRequestsRecord> {    
    @SuppressWarnings("unchecked")
    @Override
    public EhPunchExceptionRequestsRecord map(Record r) {
    	EhPunchExceptionRequestsRecord post = new EhPunchExceptionRequestsRecord();  
        post.setId(r.getValue((Field<Long>)r.field("id"))); 
        post.setEnterpriseId(r.getValue((Field<Long>)r.field("enterprise_id")));
        post.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time"))); 
        post.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid"))); 
        post.setDescription(r.getValue((Field<String>)r.field("description"))); 
        post.setOperateTime(r.getValue((Field<Timestamp>)r.field("operate_time"))); 
        post.setOperatorUid(r.getValue((Field<Long>)r.field("operator_uid")));  
        post.setApprovalStatus(r.getValue((Field<Byte>)r.field("approval_status"))); 
        post.setMorningApprovalStatus(r.getValue((Field<Byte>)r.field("morning_approval_status"))); 
        post.setAfternoonApprovalStatus(r.getValue((Field<Byte>)r.field("afternoon_approval_status"))); 
        post.setProcessDetails(r.getValue((Field<String>)r.field("process_details"))); 
        post.setPunchDate(r.getValue((Field<Date>)r.field("punch_date"))); 
        post.setRequestType(r.getValue((Field<Byte>)r.field("request_type"))); 
        post.setStatus(r.getValue((Field<Byte>)r.field("status")));
        post.setUserId(r.getValue((Field<Long>)r.field("user_id")));
        post.setRequestId(r.getValue((Field<Long>)r.field("request_id")));
        post.setPunchIntervalNo(r.getValue((Field<Integer>)r.field("punch_interval_no")));
        post.setPunchType(r.getValue((Field<Byte>)r.field("punch_type")));
        post.setBeginTime(r.getValue((Field<Timestamp>)r.field("begin_time")));
        post.setEndTime(r.getValue((Field<Timestamp>)r.field("end_time")));
        post.setDuration(r.getValue((Field<Double>)r.field("duration")));
        post.setCategoryId(r.getValue((Field<Long>)r.field("category_id")));
        post.setApprovalAttribute(r.getValue((Field<String>)r.field("approval_attribute")));
        return post;
    }
}

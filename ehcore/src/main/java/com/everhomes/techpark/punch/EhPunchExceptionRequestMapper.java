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
        post.setCompanyId(r.getValue((Field<Long>)r.field("company_id")));
        post.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time"))); 
        post.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid"))); 
        post.setDescription(r.getValue((Field<String>)r.field("description"))); 
        post.setOperateTime(r.getValue((Field<Timestamp>)r.field("operate_time"))); 
        post.setOperatorUid(r.getValue((Field<Long>)r.field("operator_uid"))); 
        post.setProcessCode(r.getValue((Field<Byte>)r.field("process_code"))); 
        post.setProcessDetails(r.getValue((Field<String>)r.field("process_details"))); 
        post.setPunchDate(r.getValue((Field<Date>)r.field("punch_date"))); 
        post.setRequestType(r.getValue((Field<Byte>)r.field("request_type"))); 
        post.setStatus(r.getValue((Field<Byte>)r.field("status"))); 
        post.setUserId(r.getValue((Field<Long>)r.field("user_id"))); 
        return post;
    }
}

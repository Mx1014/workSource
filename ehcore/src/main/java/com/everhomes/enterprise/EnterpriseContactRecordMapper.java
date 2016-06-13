package com.everhomes.enterprise;

import java.sql.Timestamp;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Field;

import com.everhomes.server.schema.tables.records.EhEnterpriseContactsRecord;

public class EnterpriseContactRecordMapper implements RecordMapper<Record, EhEnterpriseContactsRecord> {

    @SuppressWarnings("unchecked")
    @Override
    public EhEnterpriseContactsRecord map(Record r) {
        EhEnterpriseContactsRecord o = new EhEnterpriseContactsRecord();
        o.setId(r.getValue((Field<Long>)r.field("id")));
        o.setEnterpriseId(r.getValue((Field<Long>)r.field("enterprise_id")));
        o.setName(r.getValue((Field<String>)r.field("name")));
        o.setNickName(r.getValue((Field<String>)r.field("nick_name")));
        o.setAvatar(r.getValue((Field<String>)r.field("avatar")));
        o.setUserId(r.getValue((Field<Long>)r.field("user_id")));
        o.setRole(r.getValue((Field<Long>)r.field("role")));
        o.setStatus(r.getValue((Field<Byte>)r.field("status")));
        o.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid")));
        o.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time")));
        o.setIntegralTag1(r.getValue((Field<Long>)r.field("integral_tag1")));
        o.setIntegralTag2(r.getValue((Field<Long>)r.field("integral_tag2")));
        o.setIntegralTag3(r.getValue((Field<Long>)r.field("integral_tag3")));
        o.setIntegralTag4(r.getValue((Field<Long>)r.field("integral_tag4")));
        o.setIntegralTag5(r.getValue((Field<Long>)r.field("integral_tag5")));
        o.setStringTag1(r.getValue((Field<String>)r.field("string_tag1")));
        o.setStringTag2(r.getValue((Field<String>)r.field("string_tag2")));
        o.setStringTag3(r.getValue((Field<String>)r.field("string_tag3")));
        o.setStringTag4(r.getValue((Field<String>)r.field("string_tag4")));
        o.setStringTag5(r.getValue((Field<String>)r.field("string_tag5")));

        return o;
    }

}

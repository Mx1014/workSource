package com.everhomes.quality;

import com.everhomes.server.schema.tables.records.EhEnterpriseContactsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionSamplesRecord;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import java.sql.Timestamp;

/**
 * Created by ying.xiong on 2017/6/15.
 */
public class QualityInspectionSamplesRecordMapper implements RecordMapper<Record, EhQualityInspectionSamplesRecord> {

    @SuppressWarnings("unchecked")
    @Override
    public EhQualityInspectionSamplesRecord map(Record r) {
        EhQualityInspectionSamplesRecord o = new EhQualityInspectionSamplesRecord();
        o.setId(r.getValue((Field<Long>)r.field("id")));
        o.setNamespaceId(r.getValue((Field<Integer>)r.field("namespace_id")));
        o.setOwnerType(r.getValue((Field<String>)r.field("owner_type")));
        o.setOwnerId(r.getValue((Field<Long>)r.field("owner_id")));
        o.setName(r.getValue((Field<String>)r.field("name")));
        o.setSampleNumber(r.getValue((Field<String>)r.field("sample_number")));
        o.setStartTime(r.getValue((Field<Timestamp>)r.field("start_time")));
        o.setEndTime(r.getValue((Field<Timestamp>)r.field("end_time")));
        o.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time")));
        o.setDeleteTime(r.getValue((Field<Timestamp>)r.field("delete_time")));
        o.setStatus(r.getValue((Field<Byte>)r.field("status")));
        o.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid")));
        o.setDeleteUid(r.getValue((Field<Long>)r.field("delete_uid")));

        return o;
    }

}

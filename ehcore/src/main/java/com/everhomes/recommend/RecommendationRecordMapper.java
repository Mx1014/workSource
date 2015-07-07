package com.everhomes.recommend;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhRecommendationsRecord;;

public class RecommendationRecordMapper implements RecordMapper<Record, EhRecommendationsRecord> {
    @SuppressWarnings("unchecked")
    @Override
    public EhRecommendationsRecord map(Record r) {
        EhRecommendationsRecord rec = new EhRecommendationsRecord();
        
        rec.setUserId(r.getValue((Field<Long>)r.field("user_id")));
        rec.setSourceId(r.getValue((Field<Long>)r.field("source_id")));
        rec.setSourceType(r.getValue((Field<Integer>)r.field("source_type")));
        return rec;
    }

}

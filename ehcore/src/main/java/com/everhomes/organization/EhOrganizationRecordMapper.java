// @formatter:off
package com.everhomes.organization;

import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.util.ConvertHelper;

public class EhOrganizationRecordMapper implements RecordMapper<Record, Organization> {    
    @SuppressWarnings("unchecked")
    @Override
    public Organization map(Record r) {
        return ConvertHelper.convert(r, Organization.class);
    }
}

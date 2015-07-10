// @formatter:off
package com.everhomes.organization;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.util.ConvertHelper;

public class EhOrganizationRecordMapper implements RecordMapper<Record, EhOrganizationsRecord> {    
    @SuppressWarnings("unchecked")
    @Override
    public EhOrganizationsRecord map(Record r) {
        EhOrganizationsRecord record =  ConvertHelper.convert(r, EhOrganizationsRecord.class);
        
        EhOrganizationsRecord organization = new EhOrganizationsRecord();
        organization.setId(r.getValue((Field<Long>)r.field("id")));
        organization.setParentId(r.getValue((Field<Long>)r.field("parent_id")));
        organization.setOrganizationType(r.getValue((Field<String>)r.field("organization_type")));
        organization.setName(r.getValue((Field<String>)r.field("name")));
        organization.setAddressId(r.getValue((Field<Long>)r.field("address_id")));
        organization.setPath(r.getValue((Field<String>)r.field("path")));
        organization.setLevel(r.getValue((Field<Integer>)r.field("level")));
        organization.setStatus(r.getValue((Field<Byte>)r.field("status")));
        
        return organization;
    }
}

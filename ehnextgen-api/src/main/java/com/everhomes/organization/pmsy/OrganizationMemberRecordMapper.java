package com.everhomes.organization.pmsy;

import com.everhomes.server.schema.tables.records.EhOrganizationMembersRecord;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

/**
 * Created by Lei Lv on 2017/5/25.
 *
 */
public class OrganizationMemberRecordMapper implements RecordMapper<Record, EhOrganizationMembersRecord> {
    @SuppressWarnings("unchecked")
    @Override
    public EhOrganizationMembersRecord map(Record r) {
        EhOrganizationMembersRecord member = new EhOrganizationMembersRecord();

//        member.setNamespaceId(r.getValue((Field<Integer>) r.field("namespace_id")));
//        member.setOrganizationId(r.getValue((Field<Long>) r.field("organization_id")));
        member.setContactName(r.getValue((Field<String>) r.field("contact_name")));
        member.setContactType(r.getValue((Field<Byte>) r.field("contact_type")));
        member.setContactToken(r.getValue((Field<String>) r.field("contact_token")));
        member.setContactDescription(r.getValue((Field<String>) r.field("contact_description")));
        member.setEmployeeNo(r.getValue((Field<String>) r.field("employee_no")));
        member.setAvatar(r.getValue((Field<String>) r.field("avatar")));
        member.setGender(r.getValue((Field<Byte>) r.field("gender")));
        member.setStatus(r.getValue((Field<Byte>) r.field("status")));
        return member;
    }
}

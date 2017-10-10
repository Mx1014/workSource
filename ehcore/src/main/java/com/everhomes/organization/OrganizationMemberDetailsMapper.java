package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberDetails;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import java.sql.Date;

public class OrganizationMemberDetailsMapper implements RecordMapper<Record, EhOrganizationMemberDetails> {

    @SuppressWarnings("unchecked")
    @Override
    public OrganizationMemberDetails map(Record r) {
        OrganizationMemberDetails detail = new OrganizationMemberDetails();
        detail.setId(r.getValue((Field<Long>) r.field("id")));
        detail.setNamespaceId(r.getValue((Field<Integer>) r.field("namespace_id")));
        detail.setTargetType(r.getValue((Field<String>) r.field("target_type")));
        detail.setTargetId(r.getValue((Field<Long>) r.field("target_id")));
        detail.setBirthday(r.getValue((Field<Date>) r.field("birthday")));
        detail.setOrganizationId(r.getValue((Field<Long>) r.field("organization_id")));
        detail.setContactName(r.getValue((Field<String>) r.field("contact_name")));
        detail.setContactType(r.getValue((Field<Byte>) r.field("contact_type")));
        detail.setContactToken(r.getValue((Field<String>) r.field("contact_token")));
        detail.setContactDescription(r.getValue((Field<String>) r.field("contact_description")));
        detail.setEmployeeNo(r.getValue((Field<String>) r.field("employee_no")));
        detail.setAvatar(r.getValue((Field<String>) r.field("avatar")));
        detail.setGender(r.getValue((Field<Byte>) r.field("gender")));
        detail.setMaritalFlag(r.getValue((Field<Byte>) r.field("marital_flag")));
        detail.setPoliticalStatus(r.getValue((Field<String>) r.field("political_status")));
        detail.setNativePlace(r.getValue((Field<String>) r.field("native_place")));
        detail.setEnName(r.getValue((Field<String>) r.field("en_name")));
        detail.setRegResidence(r.getValue((Field<String>) r.field("reg_residence")));
        detail.setIdNumber(r.getValue((Field<String>) r.field("id_number")));
        detail.setEmail(r.getValue((Field<String>) r.field("email")));
        detail.setWechat(r.getValue((Field<String>) r.field("wechat")));
        detail.setQq(r.getValue((Field<String>) r.field("qq")));
        detail.setEmergencyName(r.getValue((Field<String>) r.field("emergency_name")));
        detail.setEmergencyContact(r.getValue((Field<String>) r.field("emergency_contact")));
        detail.setAddress(r.getValue((Field<String>) r.field("address")));
        detail.setEmployeeType(r.getValue((Field<Byte>) r.field("employee_type")));
        detail.setEmployeeStatus(r.getValue((Field<Byte>) r.field("employee_status")));
        detail.setEmploymentTime(r.getValue((Field<Date>) r.field("employment_time")));
        detail.setDismissTime(r.getValue((Field<Date>) r.field("dimission_time")));
        detail.setSalaryCardNumber(r.getValue((Field<String>) r.field("salary_card_number")));
        detail.setSocialSecurityNumber(r.getValue((Field<String>) r.field("social_security_number")));
        detail.setProvidentFundNumber(r.getValue((Field<String>) r.field("provident_fund_number")));
        detail.setProfileIntegrity(r.getValue((Field<Integer>) r.field("profile_integrity")));
        detail.setCheckInTime(r.getValue((Field<Date>) r.field("check_in_time")));

        return detail;
    }
}

package com.everhomes.organization.pmsy;

import com.everhomes.organization.OrganizationMember;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhOrganizationMemberDetails;
import com.everhomes.server.schema.tables.EhOrganizationMembers;
import org.jooq.Record;
import org.jooq.RecordMapper;

/**
 * <ul>
 * <li>t1: t1</li>
 * <li>t2: t2</li>
 * </ul>
 */
public class OrganizationMemberRecordMapper implements RecordMapper<Record, OrganizationMember> {
    private static final EhOrganizationMembers t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
    private static final EhOrganizationMemberDetails t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");

    @SuppressWarnings("unchecked")
    @Override
    public OrganizationMember map(Record r) {
        OrganizationMember member = new OrganizationMember();
        //以下信息来自于members表
        member.setId(r.getValue(t1.ID));
        member.setOrganizationId(r.getValue(t1.ORGANIZATION_ID));
//        member.setTargetId(r.getValue(t1.TARGET_ID));
//        member.setTargetType(r.getValue(t1.TARGET_TYPE));
        member.setMemberGroup(r.getValue(t1.MEMBER_GROUP));
        member.setStatus(r.getValue(t1.STATUS));
        member.setGroupId(r.getValue(t1.GROUP_ID));
        member.setUpdateTime(r.getValue(t1.UPDATE_TIME));
        member.setCreateTime(r.getValue(t1.CREATE_TIME));
        member.setIntegralTag1(r.getValue(t1.INTEGRAL_TAG1));
        member.setIntegralTag2(r.getValue(t1.INTEGRAL_TAG2));
        member.setIntegralTag3(r.getValue(t1.INTEGRAL_TAG3));
        member.setIntegralTag4(r.getValue(t1.INTEGRAL_TAG4));
        member.setIntegralTag5(r.getValue(t1.INTEGRAL_TAG5));
        member.setStringTag1(r.getValue(t1.STRING_TAG1));
        member.setStringTag2(r.getValue(t1.STRING_TAG2));
        member.setStringTag3(r.getValue(t1.STRING_TAG3));
        member.setStringTag4(r.getValue(t1.STRING_TAG4));
        member.setStringTag5(r.getValue(t1.STRING_TAG5));
        member.setNamespaceId(r.getValue(t1.NAMESPACE_ID));
        member.setVisibleFlag(r.getValue(t1.VISIBLE_FLAG));
        member.setGroupPath(r.getValue(t1.GROUP_PATH));
        member.setGroupType(r.getValue(t1.GROUP_TYPE));
        member.setCreatorUid(r.getValue(t1.CREATOR_UID));
        member.setOperatorUid(r.getValue(t1.OPERATOR_UID));
        member.setDetailId(r.getValue(t1.DETAIL_ID));
        //以下信息来自于detail表
        member.setTargetId(r.getValue(t2.TARGET_ID));
        member.setTargetType(r.getValue(t2.TARGET_TYPE));
        member.setContactName(r.getValue(t2.CONTACT_NAME));
        member.setContactEnName(r.getValue(t2.EN_NAME));
        member.setContactType(r.getValue(t2.CONTACT_TYPE));
        member.setContactToken(r.getValue(t2.CONTACT_TOKEN));
        member.setContactDescription(r.getValue(t2.CONTACT_DESCRIPTION));
        member.setEmployeeNo(r.getValue(t2.EMPLOYEE_NO));
//        member.setAvatar(r.getValue(t2.AVATAR));
        member.setGender(r.getValue(t2.GENDER));
        member.setEmployeeStatus(r.getValue(t2.EMPLOYEE_STATUS));
        member.setEmploymentTime(r.getValue(t2.EMPLOYMENT_TIME));
//        member.setProfileIntegrity(r.getValue(t2.PROFILE_INTEGRITY));
        member.setCheckInTime(r.getValue(t2.CHECK_IN_TIME));
        member.setEmail(r.getValue(t2.EMAIL));
        member.setWorkEmail(r.getValue(t2.WORK_EMAIL));
        member.setRegionCode(r.getValue(t2.REGION_CODE));
        member.setContractEndTime(r.getValue(t2.CONTRACT_END_TIME));
        member.setContactShortToken(r.getValue(t2.CONTACT_SHORT_TOKEN));
        member.setAccount(r.getValue(t2.ACCOUNT));

        return member;
    }
}

package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>memberJobRecords: 对应的人员变动信息，参考{@link MemberRecordChangesByJobDTO}</li>
 * </ul>
 */
public class ListMemberRecordChangesByJobCommandResponse {

    @ItemType(MemberRecordChangesByJobDTO.class)
    private List<MemberRecordChangesByJobDTO> memberJobRecords;

    public ListMemberRecordChangesByJobCommandResponse() {
    }

    public List<MemberRecordChangesByJobDTO> getMemberJobRecords() {
        return memberJobRecords;
    }

    public void setMemberJobRecords(List<MemberRecordChangesByJobDTO> memberJobRecords) {
        this.memberJobRecords = memberJobRecords;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}

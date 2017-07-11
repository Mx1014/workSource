package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>memberProfileRecords: 对应的档案修改记录，参考{@link MemberRecordChangesByProfileDTO}</li>
 * </ul>
 */
public class ListMemberProfileRecordsCommandResponse {

    @ItemType(MemberRecordChangesByProfileDTO.class)
    private List<MemberRecordChangesByProfileDTO> memberProfileRecords;

    public ListMemberProfileRecordsCommandResponse() {
    }

    public List<MemberRecordChangesByProfileDTO> getMemberProfileRecords() {
        return memberProfileRecords;
    }

    public void setMemberProfileRecords(List<MemberRecordChangesByProfileDTO> memberProfileRecords) {
        this.memberProfileRecords = memberProfileRecords;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

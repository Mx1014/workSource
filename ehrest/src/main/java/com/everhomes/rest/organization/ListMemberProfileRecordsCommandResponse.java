package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>memberProfileRecords: 对应的档案修改记录，参考{@link MemberProfileRecordsDTO}</li>
 * </ul>
 */
public class ListMemberProfileRecordsCommandResponse {

    @ItemType(MemberProfileRecordsDTO.class)
    private List<MemberProfileRecordsDTO> memberProfileRecords;

    public ListMemberProfileRecordsCommandResponse() {
    }

    public List<MemberProfileRecordsDTO> getMemberProfileRecords() {
        return memberProfileRecords;
    }

    public void setMemberProfileRecords(List<MemberProfileRecordsDTO> memberProfileRecords) {
        this.memberProfileRecords = memberProfileRecords;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

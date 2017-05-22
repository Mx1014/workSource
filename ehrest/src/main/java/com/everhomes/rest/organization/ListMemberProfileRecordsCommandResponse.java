package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by R on 2017/5/22.
 * <ul>
 * <li>memberProfileRecords: 对应的档案修改记录，参考{@link com.everhomes.rest.organization.MemberProfileRecords}</li>
 * </ul>
 */
public class ListMemberProfileRecordsCommandResponse {

    @ItemType(MemberProfileRecords.class)
    private List<MemberProfileRecords> memberProfileRecords;

    public ListMemberProfileRecordsCommandResponse() {
    }

    public List<MemberProfileRecords> getMemberProfileRecords() {
        return memberProfileRecords;
    }

    public void setMemberProfileRecords(List<MemberProfileRecords> memberProfileRecords) {
        this.memberProfileRecords = memberProfileRecords;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

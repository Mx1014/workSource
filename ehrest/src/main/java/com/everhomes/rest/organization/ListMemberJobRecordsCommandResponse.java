package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Ryan on 2017/5/19.
 * <ul>
 * <li>memberJobRecords: 对应的人员变动信息，参考{@link com.everhomes.rest.organization.MemberJobRecordsDTO}</li>
 * </ul>
 */
public class ListMemberJobRecordsCommandResponse {

    @ItemType(MemberJobRecordsDTO.class)
    private List<MemberJobRecordsDTO> memberJobRecords;

    public ListMemberJobRecordsCommandResponse() {
    }

    public List<MemberJobRecordsDTO> getMemberJobRecords() {
        return memberJobRecords;
    }

    public void setMemberJobRecords(List<MemberJobRecordsDTO> memberJobRecords) {
        this.memberJobRecords = memberJobRecords;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}

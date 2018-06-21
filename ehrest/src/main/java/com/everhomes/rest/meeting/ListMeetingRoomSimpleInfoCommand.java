package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID，必填</li>
 * </ul>
 */
public class ListMeetingRoomSimpleInfoCommand {
    private Long organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static void main(String[] args){
        System.out.println(System.currentTimeMillis());
    }
}

package com.everhomes.rest.profile;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页起始锚点</li>
 * <li>contacts: 成员信息，参考{@link com.everhomes.rest.profile.ProfileContactsDTO}</li>
 * </ul>
 */
public class listProfileContactsResponse {

    private Long nextPageAnchor;

    @ItemType(ProfileContactsDTO.class)
    private List<ProfileContactsDTO> contacts;

    public listProfileContactsResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ProfileContactsDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ProfileContactsDTO> contacts) {
        this.contacts = contacts;
    }
}

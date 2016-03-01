package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * <ul>
 * <li>contacts : 联系人列表，{@link com.everhomes.rest.user.ContactV2DTO}</li>
 * <li>nextPageAnchor : 下一页页码</li>
 * </ul>
 */
public class ListContactV2Respose {
    @ItemType(ContactV2DTO.class)
    private List<ContactV2DTO> contacts;

    private Long nextPageAnchor;

    public ListContactV2Respose() {
    }

    public ListContactV2Respose(List<ContactV2DTO> contacts, Long nextPageAnchor) {
        super();
        this.contacts = contacts;
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ContactV2DTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactV2DTO> contacts) {
        this.contacts = contacts;
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}

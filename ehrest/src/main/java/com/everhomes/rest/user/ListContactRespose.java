package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * <ul>
 * <li>nextPageAnchor : 下一页页码</li>
 *	<li>contacts : com.everhomes.util.StringHelper.ContactDTO</li>
 *</ul>
 *
 */

public class ListContactRespose {
    @ItemType(ContactDTO.class)
    private List<ContactDTO> contacts;

    private Long nextPageAnchor;

    public ListContactRespose() {
    }

    public ListContactRespose(List<ContactDTO> contacts, Long nextPageAnchor) {
        super();
        this.contacts = contacts;
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

    

}

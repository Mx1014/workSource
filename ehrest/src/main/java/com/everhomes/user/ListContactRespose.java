package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListContactRespose {
    @ItemType(ContactDTO.class)
    private List<ContactDTO> contacts;

    private Long pageAnchor;

    public ListContactRespose() {
    }

    public ListContactRespose(List<ContactDTO> contacts, Long nextAnchor) {
        super();
        this.contacts = contacts;
        this.pageAnchor = nextAnchor;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long nextAnchor) {
        this.pageAnchor = nextAnchor;
    }

}

package com.everhomes.user;

import java.util.List;

public class ListContactRespose {
    private List<ContactDTO> contacts;

    private Long nextAnchor;

    public ListContactRespose() {
    }

    public ListContactRespose(List<ContactDTO> contacts, Long nextAnchor) {
        super();
        this.contacts = contacts;
        this.nextAnchor = nextAnchor;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }

    public Long getNextAnchor() {
        return nextAnchor;
    }

    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }

}

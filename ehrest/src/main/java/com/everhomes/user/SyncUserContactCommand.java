package com.everhomes.user;

import com.everhomes.util.StringHelper;

public class SyncUserContactCommand {
    // private Contact contact;
    //
    // public Contact getContact() {
    // return contact;
    // }
    //
    // public void setContact(Contact contact) {
    // this.contact = contact;
    // }

    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

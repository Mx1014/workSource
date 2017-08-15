package com.everhomes.profile;

import com.everhomes.rest.profile.*;

public interface ProfileService {

    AddProfileContactsResponse addProfileContacts(AddProfileContactsCommand cmd);

    void transferProfileContacts(TransferProfileContactsCommand cmd);

    void deleteProfileContacts(DeleteProfileContactsCommand cmd);

    void stickProfileContacts(StickProfileContactsCommand cmd);

    listProfileContactsResponse listProfileContacts(listProfileContactsCommand cmd);
}

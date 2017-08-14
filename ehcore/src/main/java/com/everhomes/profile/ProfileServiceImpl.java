package com.everhomes.profile;

import com.everhomes.rest.profile.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Autowired ProfileProvider profileProvider;

    @Override
    public AddProfileContactsResponse addProfileContacts(AddProfileContactsCommand cmd) {
        return null;
    }

    @Override
    public void deleteProfileContacts(DeleteProfileContactsCommand cmd) {

    }

    @Override
    public void stickProfileContacts(StickProfileContactsCommand cmd) {

    }

    @Override
    public listProfileContactsResponse listProfileContacts(listProfileContactsCommand cmd) {
        return null;
    }
}

package com.everhomes.profile;

import com.everhomes.rest.profile.AddProfileContactsCommand;
import com.everhomes.rest.profile.AddProfileContactsResponse;
import org.springframework.stereotype.Component;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Override
    public AddProfileContactsResponse addProfileContacts(AddProfileContactsCommand cmd) {
        return null;
    }
}

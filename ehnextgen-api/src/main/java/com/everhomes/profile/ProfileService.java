package com.everhomes.profile;

import com.everhomes.rest.profile.AddProfileContactsPersonnelCommand;
import com.everhomes.rest.profile.AddProfileContactsPersonnelResponse;

public interface ProfileService {

    AddProfileContactsPersonnelResponse addProfileContactsPersonnel(AddProfileContactsPersonnelCommand cmd);
}

package com.everhomes.profile;

import java.util.List;

public interface ProfileProvider {

    void createProfileContactsSticky(ProfileContactsSticky profileContactsSticky);

    void deleteProfileContactsSticky(ProfileContactsSticky profileContactsSticky);

    ProfileContactsSticky findProfileContactsStickyById(Long id);

    List<Long> listProfileContactsStickyIds(Integer namespaceId, Long organizationId);

    ProfileContactsSticky findProfileContactsStickyByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId);
}

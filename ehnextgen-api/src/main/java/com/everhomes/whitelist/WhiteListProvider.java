// @formatter:off
package com.everhomes.whitelist;

import java.util.List;

public interface WhiteListProvider {

    void createWhiteList(PhoneWhiteList phoneWhiteList);

    void batchCreateWhiteList(List<PhoneWhiteList> phoneWhiteLists);

    void deleteWhiteList(PhoneWhiteList phoneWhiteList);

    void updateWhiteList(PhoneWhiteList phoneWhiteList);

    PhoneWhiteList getWhiteList(Long id);

    List<PhoneWhiteList> listWhiteList(String phoneNumber, Integer namespaceId, Long pageAnchor, Integer pageSize);

    List<String> listAllWhiteList(Integer namespaceId);

    PhoneWhiteList checkPhoneIsExists(Integer namespaceId, String phoneNumber);
}

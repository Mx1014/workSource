// @formatter:off
package com.everhomes.whitelist;

import java.util.List;

public interface WhiteListProvider {

    void createWhiteList(PhoneWhiteList phoneWhiteList);

    void batchCreateWhiteList(List<PhoneWhiteList> phoneWhiteLists);

    void deleteWhiteList(PhoneWhiteList phoneWhiteList);

    void updateWhiteList(PhoneWhiteList phoneWhiteList);

    PhoneWhiteList getWhiteList(Long id);

    List<PhoneWhiteList> listWhiteList(String phoneNumber, Long pageAnchor, Integer pageSize);
}

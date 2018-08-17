// @formatter:off
package com.everhomes.express;

import com.everhomes.rest.order.ListBizPayeeAccountDTO;

import java.util.List;

public interface ExpressPayeeAccountProvider {

	void createExpressPayeeAccount(ExpressPayeeAccount expressPayeeAccount);

	void updateExpressPayeeAccount(ExpressPayeeAccount expressPayeeAccount);

	ExpressPayeeAccount findExpressPayeeAccountById(Long id);

	List<ExpressPayeeAccount> listExpressPayeeAccount();

    List<ExpressPayeeAccount> findRepeatBusinessPayeeAccounts(Long id, Integer namespaceId, String ownerType, Long ownerId);

	ExpressPayeeAccount getPayeeAccountByOwner(Integer namespaceId, String ownerType, Long ownerId);

	ListBizPayeeAccountDTO createPersonalPayUserIfAbsent(String userId, String accountCode, String userIdenify, String tag1, String tag2, String tag3);

}
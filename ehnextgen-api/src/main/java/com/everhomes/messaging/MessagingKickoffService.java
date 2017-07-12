package com.everhomes.messaging;

import com.everhomes.rest.user.LoginToken;

public interface MessagingKickoffService {

    String getKickoffMessageKey(Integer namespaceId, LoginToken loginToken);

    void kickoff(Integer namespaceId, LoginToken loginToken);

    boolean isKickoff(Integer namespaceId, LoginToken loginToken);

	void remoteKickoffTag(Integer namespaceId, LoginToken loginToken);

}

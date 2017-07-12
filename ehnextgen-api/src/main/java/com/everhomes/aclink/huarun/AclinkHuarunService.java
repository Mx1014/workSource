package com.everhomes.aclink.huarun;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public interface AclinkHuarunService {

	ResponseEntity<String> restCall(String cmd,
			Object params);

	AclinkHuarunVerifyUserResp verifyUser(AclinkHuarunVerifyUser user);

	AclinkGetSimpleQRCodeResp getSimpleQRCode(AclinkGetSimpleQRCode getCode);

	AclinkHuarunSyncUserResp syncUser(AclinkHuarunSyncUser syncUser);

}

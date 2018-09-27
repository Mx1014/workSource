package com.everhomes.core.sdk.user;

import com.everhomes.core.sdk.CoreSdkSettings;
import com.everhomes.rest.RestResponse;
import com.everhomes.tachikoma.commons.sdk.SdkClient;
import org.springframework.stereotype.Service;

@Service
public class SdkPrintService {

	private final SdkClient sdkClient = new SdkClient(new CoreSdkSettings());

	public RestResponse mfpLogNotification(String jobData) {
		return sdkClient.restCall("post", "/evh/siyinprint/mfpLogSdkNotification", jobData, RestResponse.class);
	}
}
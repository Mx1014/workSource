package com.everhomes.appurl;

public interface AppUrlProvider {
	 AppUrls findByNamespaceIdAndOSType(Integer namespaceId, Byte osType);

}

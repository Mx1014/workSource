// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhAppNamespaceMappings;
import com.everhomes.util.StringHelper;

public class AppNamespaceMapping extends EhAppNamespaceMappings {

	private static final long serialVersionUID = -4330491807080000401L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
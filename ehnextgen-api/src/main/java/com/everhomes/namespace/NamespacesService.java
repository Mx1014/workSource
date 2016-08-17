package com.everhomes.namespace;

import com.everhomes.rest.namespace.ConfigNamespaceCommand;
import com.everhomes.rest.namespace.CreateNamespaceCommand;
import com.everhomes.rest.namespace.CreateNamespaceResponse;

public interface NamespacesService {

	CreateNamespaceResponse createNamespace(CreateNamespaceCommand cmd);

	void configNamespace(ConfigNamespaceCommand cmd);

}

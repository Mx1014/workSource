package com.everhomes.namespace;

import java.util.List;

import com.everhomes.rest.namespace.admin.CreateNamespaceCommand;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.namespace.admin.UpdateNamespaceCommand;

public interface NamespacesService {

	List<NamespaceInfoDTO> listNamespace();

	NamespaceInfoDTO createNamespace(CreateNamespaceCommand cmd);

	NamespaceInfoDTO updateNamespace(UpdateNamespaceCommand cmd);

}

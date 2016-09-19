package com.everhomes.namespace;

import java.util.List;

import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;

public interface NamespacesProvider {

	void createNamespace(Namespace namespace);
	void createNamespaceDetail(NamespaceDetail namespaceDetail);
	NamespaceInfoDTO findNamespaceByid(Integer id);
	List<NamespaceInfoDTO> listNamespace();
	void updateNamespace(Namespace namespace);
	void updateNamespaceDetail(NamespaceDetail namespaceDetail);
	NamespaceDetail findNamespaceDetailByNamespaceId(Integer id);

}

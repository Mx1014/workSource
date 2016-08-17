package com.everhomes.namespace;

public interface NamespacesProvider {

	void createNamespace(Namespace namespace);
	void createNamespaceDetail(NamespaceDetail namespaceDetail);
	Namespace findNamespaceByid(Integer id);

}

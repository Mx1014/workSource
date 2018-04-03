package com.everhomes.namespace;

import java.util.List;

import com.everhomes.rest.namespace.MaskDTO;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;

public interface NamespacesProvider {

	void createNamespace(Namespace namespace);
	void createNamespaceDetail(NamespaceDetail namespaceDetail);
	NamespaceInfoDTO findNamespaceByid(Integer id);
	List<NamespaceInfoDTO> listNamespace();
	void updateNamespace(Namespace namespace);
	void updateNamespaceDetail(NamespaceDetail namespaceDetail);
	NamespaceDetail findNamespaceDetailByNamespaceId(Integer id);
	List<NamespaceResource> listNamespaceResources(Integer namespaceId, String resourceType);

	//获取域空间默认配置蒙版
	List<MaskDTO> listNamespaceMasks(Integer namespaceId);

}

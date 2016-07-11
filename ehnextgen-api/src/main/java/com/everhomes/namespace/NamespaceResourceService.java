// @formatter:off
package com.everhomes.namespace;

import com.everhomes.rest.namespace.GetNamespaceDetailCommand;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommand;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.namespace.NamespaceDetailDTO;




public interface NamespaceResourceService {
    ListCommunityByNamespaceCommandResponse listCommunityByNamespace(ListCommunityByNamespaceCommand cmd);
    NamespaceDetailDTO getNamespaceDetail(GetNamespaceDetailCommand cmd);
}

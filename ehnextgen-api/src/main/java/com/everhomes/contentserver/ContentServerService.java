package com.everhomes.contentserver;

import java.util.List;

import com.everhomes.rest.contentserver.AddConfigItemCommand;
import com.everhomes.rest.contentserver.AddContentServerCommand;
import com.everhomes.rest.contentserver.ContentServerDTO;
import com.everhomes.rest.contentserver.UpdateContentServerCommand;

public interface ContentServerService {

    ContentServer addContentServer(AddContentServerCommand cmd);

    void removeContentServer(Long contentServerId);

    void updateContentServer(UpdateContentServerCommand cmd);

    List<ContentServerDTO> listContservers();

    void addConfigItem(AddConfigItemCommand item);
    void removeConfigItem(String itemName);
    ContentServer selectContentServer() throws Exception;

    List<String> parserUri(List<String> uris, String ownerType, Long ownerId);

    String parserUri(String uri, String ownerType, Long ownerId);

    ContentServerResource findResourceByUri(String uri);
}

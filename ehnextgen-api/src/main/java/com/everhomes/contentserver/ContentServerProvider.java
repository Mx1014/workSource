package com.everhomes.contentserver;

import java.util.List;

public interface ContentServerProvider {
    void addResource(ContentServerResource contentServerResource);

    void deleteResource(ContentServerResource contentServerResource);

    void updateResource(ContentServerResource contentServerResource);

    List<ContentServerResource> findByUid(Long uid);

    ContentServerResource findByMD5(String md5);

    ContentServerResource findByResourceId(String resourceId);

    ContentServerResource findByUidAndMD5(Long uid, String md5);

    void addContentServer(ContentServer contentServer);

    void deleteContentServer(ContentServer contentServer);

    void updateContentServer(ContentServer contentServer);

    void deleteByUidAndResourceId(Long uid, String resourceId);

    List<ContentServer> listContentServers();

    ContentServer findContentServerById(Long serverId);

    void cleanAll(long serverId);

}

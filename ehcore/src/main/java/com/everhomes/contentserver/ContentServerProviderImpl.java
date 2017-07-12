package com.everhomes.contentserver;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContentServerDao;
import com.everhomes.server.schema.tables.daos.EhContentServerResourcesDao;
import com.everhomes.server.schema.tables.pojos.EhContentServer;
import com.everhomes.server.schema.tables.pojos.EhContentServerResources;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContentServerProviderImpl implements ContentServerProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    //private ShardingProvider shardingProvider;
    private SequenceProvider sequenceProvider;

    @Override
    public void addResource(ContentServerResource contentServerResource) {
        //Long id = shardingProvider.allocShardableContentId(EhContentServerResources.class).second();
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContentServerResources.class));
        contentServerResource.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhContentServerResources.class, id));
        EhContentServerResourcesDao dao = new EhContentServerResourcesDao(context.configuration());
        dao.insert(contentServerResource);
    }

    @Override
    public void deleteResource(ContentServerResource contentServerResource) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhContentServerResources.class,
                contentServerResource.getId()));
        EhContentServerResourcesDao dao = new EhContentServerResourcesDao(context.configuration());
        dao.delete(contentServerResource);
    }

    @Override
    public void updateResource(ContentServerResource contentServerResource) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhContentServerResources.class,
                contentServerResource.getId()));
        EhContentServerResourcesDao dao = new EhContentServerResourcesDao(context.configuration());
        dao.update(contentServerResource);
    }

    @Override
    public List<ContentServerResource> findByUid(Long uid) {
        List<ContentServerResource> resources = new ArrayList<ContentServerResource>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhContentServerResources.class), null, (context, object) -> {
            EhContentServerResourcesDao dao = new EhContentServerResourcesDao(context.configuration());
            dao.fetchByOwnerId(uid).forEach(r -> {
                resources.add(ConvertHelper.convert(r, ContentServerResource.class));
            });
            return true;
        });
        return resources;
    }

    @Cacheable(value = "ContentServerResource", key = "#md5", unless = "#result == null")
    @Override
    public ContentServerResource findByMD5(String md5) {
        ContentServerResource[] resources = new ContentServerResource[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhContentServerResources.class), null, (context, object) -> {
            EhContentServerResourcesDao dao = new EhContentServerResourcesDao(context.configuration());
            dao.fetchByResourceMd5(md5).forEach(r -> {
                resources[0] = ConvertHelper.convert(r, ContentServerResource.class);
            });
            if (resources[0] != null) {
                return false;
            }
            return true;
        });
        return resources[0];
    }

    @Cacheable(value = "findByResourceId", key = "#resourceId", unless = "#result == null")
    @Override
    public ContentServerResource findByResourceId(String resourceId) {
        ContentServerResource[] resources = new ContentServerResource[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhContentServerResources.class), null, (context, object) -> {
            resources[0] = context.selectFrom(Tables.EH_CONTENT_SERVER_RESOURCES)
                    .where(Tables.EH_CONTENT_SERVER_RESOURCES.RESOURCE_ID.eq(resourceId))
                    .fetchAnyInto(ContentServerResource.class);
            return resources[0] == null;
        });
        return resources[0];
    }

    @Override
    public void addContentServer(ContentServer contentServer) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhContentServer.class));
        EhContentServerDao dao = new EhContentServerDao(context.configuration());
        dao.insert(contentServer);

    }

    @Override
    public void deleteContentServer(ContentServer contentServer) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhContentServer.class));
        EhContentServerDao dao = new EhContentServerDao(context.configuration());
        dao.delete(contentServer);

    }

    @Override
    public void updateContentServer(ContentServer contentServer) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhContentServer.class));
        EhContentServerDao dao = new EhContentServerDao(context.configuration());
        dao.update(contentServer);

    }

    @Cacheable(value = "findByUidAndMD5", key = "{#uid,#md5}", unless = "#result == null")
    @Override
    public ContentServerResource findByUidAndMD5(Long uid, String md5) {
        ContentServerResource[] resources = new ContentServerResource[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhContentServerResources.class),null,(context, object) -> {
                    context.select().from(Tables.EH_CONTENT_SERVER_RESOURCES)
                            .where(Tables.EH_CONTENT_SERVER_RESOURCES.OWNER_ID.eq(uid))
                            .and(Tables.EH_CONTENT_SERVER_RESOURCES.RESOURCE_MD5.eq(md5)).fetch().forEach(r -> {
                                resources[0] = ConvertHelper.convert(r, ContentServerResource.class);
                            });
                    if (resources[0] != null) {
                        return false;
                    }
                    return true;
                });
        return resources[0];
    }

    @Override
    public void deleteByUidAndResourceId(Long uid, String resourceId) {
        dbProvider.mapReduce(
                AccessSpec.readWriteWith(EhContentServerResources.class),
                null,
                (context, obj) -> {
                    context.delete(Tables.EH_CONTENT_SERVER_RESOURCES)
                            .where(Tables.EH_CONTENT_SERVER_RESOURCES.OWNER_ID.eq(uid))
                            .and(Tables.EH_CONTENT_SERVER_RESOURCES.RESOURCE_ID.eq(resourceId)).execute();
                    return true;
                });

    }

    @Override
    public List<ContentServer> listContentServers() {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhContentServer.class));
        EhContentServerDao dao = new EhContentServerDao(cxt.configuration());
        return dao.findAll().stream().map(r -> ConvertHelper.convert(r, ContentServer.class))
                .collect(Collectors.toList());
    }

    @Override
    public ContentServer findContentServerById(Long serverId) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhContentServer.class));
        EhContentServerDao dao = new EhContentServerDao(cxt.configuration());
        EhContentServer server = dao.findById(serverId);
        if (server == null) {
            return null;
        }
        return ConvertHelper.convert(server, ContentServer.class);
    }

    @Override
    public void cleanAll(long serverId) {
        // TODO Auto-generated method stub

    }

}

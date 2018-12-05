// @formatter:off
package com.everhomes.module;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.module.ServiceModuleStatus;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.everhomes.server.schema.tables.EhReflectionServiceModuleApps.EH_REFLECTION_SERVICE_MODULE_APPS;

@Component
public class ServiceModuleEntryProviderImpl implements ServiceModuleEntryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleEntryProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<ServiceModuleEntry> listServiceModuleEntries(Long moduleId, Long appCategoryId, Byte terminalType, Byte locationType, Byte sceneType) {
        List<ServiceModuleEntry> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModuleEntriesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ENTRIES);
        if(moduleId != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.MODULE_ID.eq(moduleId));
        }

        if(appCategoryId != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.APP_CATEGORY_ID.eq(appCategoryId));
        }

        if(terminalType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.TERMINAL_TYPE.eq(terminalType));
        }
        if(locationType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.LOCATION_TYPE.eq(locationType));
        }
        if(sceneType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.SCENE_TYPE.eq(sceneType));
        }

        //查询某个分类的话，就不按照模块排序了。查询所有的
        if(appCategoryId != null){
            query.addOrderBy(Tables.EH_SERVICE_MODULE_ENTRIES.DEFAULT_ORDER);
        }else {
            query.addOrderBy(Tables.EH_SERVICE_MODULE_ENTRIES.MODULE_ID);
        }

        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleEntry.class));
            return null;
        });
        return results;
    }


    @Override
    public List<ServiceModuleEntry> listServiceModuleEntries(List<Long> moduleIds, Byte locationType, Byte sceneType) {
        List<ServiceModuleEntry> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModuleEntriesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ENTRIES);
        if(moduleIds != null && moduleIds.size() > 0){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.MODULE_ID.in(moduleIds));
        }

        if(locationType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.LOCATION_TYPE.eq(locationType));
        }
        if(sceneType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.SCENE_TYPE.eq(sceneType));
        }

        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleEntry.class));
            return null;
        });
        return results;
    }



    @Override
    public void delete(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleEntries.class, id));
        EhServiceModuleEntriesDao dao = new EhServiceModuleEntriesDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleEntries.class, id);
    }

    @Override
    public void create(ServiceModuleEntry serviceModuleEntry) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleEntries.class));
        serviceModuleEntry.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleEntries.class, id));
        EhServiceModuleEntriesDao dao = new EhServiceModuleEntriesDao(context.configuration());
        dao.insert(serviceModuleEntry);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleEntries.class, id);
    }

    @Override
    public ServiceModuleEntry findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhServiceModuleEntriesDao dao = new EhServiceModuleEntriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ServiceModuleEntry.class);
    }


    @Override
    public void udpate(ServiceModuleEntry serviceModuleEntry) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleEntries.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleEntries.class, id));
        EhServiceModuleEntriesDao dao = new EhServiceModuleEntriesDao(context.configuration());
        dao.update(serviceModuleEntry);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleEntries.class, id);
    }

    @Override
    public List<ServiceModuleAppEntry> listServiceModuleAppEntries(Long appId, Long appCategoryId, Byte terminalType, Byte locationType, Byte sceneType) {
        List<ServiceModuleAppEntry> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModuleAppEntriesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_APP_ENTRIES);
        if(appId != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_ENTRIES.APP_ID.eq(appId));
        }

        if(appCategoryId != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_ENTRIES.APP_CATEGORY_ID.eq(appCategoryId));
        }

        if(terminalType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_ENTRIES.TERMINAL_TYPE.eq(terminalType));
        }
        if(locationType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_ENTRIES.LOCATION_TYPE.eq(locationType));
        }
        if(sceneType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_ENTRIES.SCENE_TYPE.eq(sceneType));
        }

        //查询某个分类的话，就不按照模块排序了。查询所有的
        if(appCategoryId != null){
            query.addOrderBy(Tables.EH_SERVICE_MODULE_APP_ENTRIES.DEFAULT_ORDER);
        }else {
            query.addOrderBy(Tables.EH_SERVICE_MODULE_APP_ENTRIES.APP_ID);
        }

        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleAppEntry.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ServiceModuleAppEntry> listServiceModuleAppEntries(List<Long> appIds, Byte locationType, Byte sceneType) {
        List<ServiceModuleAppEntry> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModuleAppEntriesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_APP_ENTRIES);
        if(appIds != null && appIds.size() > 0){
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_ENTRIES.APP_ID.in(appIds));
        }

        if(locationType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_ENTRIES.LOCATION_TYPE.eq(locationType));
        }
        if(sceneType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_APP_ENTRIES.SCENE_TYPE.eq(sceneType));
        }

        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleAppEntry.class));
            return null;
        });
        return results;
    }

    @Override
    public void deleteAppEntry(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppEntries.class, id));
        EhServiceModuleAppEntriesDao dao = new EhServiceModuleAppEntriesDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleAppEntries.class, id);
    }

    @Override
    public void createAppEntry(ServiceModuleAppEntry serviceModuleAppEntry) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAppEntries.class));
        serviceModuleAppEntry.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppEntries.class, id));
        EhServiceModuleAppEntriesDao dao = new EhServiceModuleAppEntriesDao(context.configuration());
        dao.insert(serviceModuleAppEntry);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleAppEntries.class, id);
    }

    @Override
    public ServiceModuleAppEntry findAppEntryById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhServiceModuleAppEntriesDao dao = new EhServiceModuleAppEntriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ServiceModuleAppEntry.class);
    }

    @Override
    public void udpateAppEntry(ServiceModuleAppEntry serviceModuleAppEntry) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAppEntries.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppEntries.class, id));
        EhServiceModuleAppEntriesDao dao = new EhServiceModuleAppEntriesDao(context.configuration());
        dao.update(serviceModuleAppEntry);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleAppEntries.class, id);
    }

    @Override
    public void batchCreateAppEntry(List<ServiceModuleAppEntry> serviceModuleAppEntries) {
        List<EhServiceModuleAppEntries> list = new ArrayList<>();
        long id = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleAppEntries.class), Long.valueOf(serviceModuleAppEntries.size()));
        for (ServiceModuleAppEntry serviceModuleAppEntry : serviceModuleAppEntries) {
            serviceModuleAppEntry.setId(id);
            id++;
            list.add(ConvertHelper.convert(serviceModuleAppEntry, EhServiceModuleAppEntries.class));
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppEntries.class));
        EhServiceModuleAppEntriesDao dao = new EhServiceModuleAppEntriesDao(context.configuration());
        dao.insert(list);
    }

    @Override
    public void batchDeleteAppEntry(List<ServiceModuleAppEntry> serviceModuleAppEntries) {
        List<EhServiceModuleAppEntries> list = new ArrayList<>();
        for (ServiceModuleAppEntry serviceModuleAppEntry : serviceModuleAppEntries) {
            list.add(ConvertHelper.convert(serviceModuleAppEntry, EhServiceModuleAppEntries.class));
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppEntries.class));
        EhServiceModuleAppEntriesDao dao = new EhServiceModuleAppEntriesDao(context.configuration());
        dao.delete(list);
    }

    @Override
    public void batchUpdateAppEntry(List<ServiceModuleAppEntry> serviceModuleAppEntries) {
        List<EhServiceModuleAppEntries> list = new ArrayList<>();
        for (ServiceModuleAppEntry serviceModuleAppEntry : serviceModuleAppEntries) {
            list.add(ConvertHelper.convert(serviceModuleAppEntry, EhServiceModuleAppEntries.class));
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleAppEntries.class));
        EhServiceModuleAppEntriesDao dao = new EhServiceModuleAppEntriesDao(context.configuration());
        dao.update(list);
    }

}

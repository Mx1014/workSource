// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWorkPlatformAppsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.pojos.EhWorkPlatformApps;
import com.everhomes.util.ConvertHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkPlatformAppProviderImpl implements WorkPlatformAppProvider{
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public WorkPlatformApp getWorkPlatformApp(Long appOriginId, Long scopeId, Long entryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWorkPlatformApps.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_WORK_PLATFORM_APPS);
        Condition condition = Tables.EH_WORK_PLATFORM_APPS.APP_ID.eq(appOriginId)
                .and(Tables.EH_WORK_PLATFORM_APPS.SCOPE_ID.eq(scopeId))
                .and(Tables.EH_WORK_PLATFORM_APPS.SCOPE_TYPE.eq(ScopeType.ORGANIZATION.getCode()))
                .and(Tables.EH_WORK_PLATFORM_APPS.ENTRY_ID.eq(entryId));

        return ConvertHelper.convert(step.where(condition).fetchAny(), WorkPlatformApp.class);
    }

    @Override
    public void deleteWorkPlatformApp(WorkPlatformApp workPlatformApp) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhWorkPlatformApps.class));
        EhWorkPlatformAppsDao dao = new EhWorkPlatformAppsDao(context.configuration());

        dao.delete(workPlatformApp);
    }

    @Override
    public void createWorkPlatformApp(WorkPlatformApp workPlatformApp) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhWorkPlatformApps.class));

        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkPlatformApps.class));
        workPlatformApp.setId(id);
        EhWorkPlatformAppsDao dao = new EhWorkPlatformAppsDao(context.configuration());
        dao.insert(workPlatformApp);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkPlatformApps.class, id);

    }

    @Override
    public void updateWorkPlatformApp(WorkPlatformApp workPlatformApp) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhWorkPlatformApps.class));
        EhWorkPlatformAppsDao dao = new EhWorkPlatformAppsDao(context.configuration());

        dao.update(workPlatformApp);
    }

    @Override
    public List<WorkPlatformApp> listWorkPlatformApp(Long appOriginId, Long scopeId, Integer sortNum) {
        List<WorkPlatformApp> list = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWorkPlatformApps.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_WORK_PLATFORM_APPS);
        Condition condition = Tables.EH_WORK_PLATFORM_APPS.APP_ID.eq(appOriginId)
                .and(Tables.EH_WORK_PLATFORM_APPS.SCOPE_ID.eq(scopeId))
                .and(Tables.EH_WORK_PLATFORM_APPS.SCOPE_TYPE.eq(ScopeType.ORGANIZATION.getCode()))
                .and(Tables.EH_WORK_PLATFORM_APPS.ORDER.gt(sortNum));
        step.where(condition).orderBy(Tables.EH_WORK_PLATFORM_APPS.ORDER.asc()).fetch().map((r) ->{
            list.add(ConvertHelper.convert(r, WorkPlatformApp.class));
            return null;
        });

        return list;
    }

    @Override
    public List<WorkPlatformApp> listWorkPlatformApp(Long appOriginId, Long scopeId) {
        List<WorkPlatformApp> list = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWorkPlatformApps.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_WORK_PLATFORM_APPS);
        Condition condition = Tables.EH_WORK_PLATFORM_APPS.APP_ID.eq(appOriginId)
                .and(Tables.EH_WORK_PLATFORM_APPS.SCOPE_ID.eq(scopeId))
                .and(Tables.EH_WORK_PLATFORM_APPS.SCOPE_TYPE.eq(ScopeType.ORGANIZATION.getCode()));
        step.where(condition).orderBy(Tables.EH_WORK_PLATFORM_APPS.ORDER.asc()).fetch().map((r) ->{
            list.add(ConvertHelper.convert(r, WorkPlatformApp.class));
            return null;
        });
        return list;
    }

    @Override
    public List<WorkPlatformApp> listWorkPlatformAppByScopeId(Long scopeId) {
        List<WorkPlatformApp> list = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWorkPlatformApps.class));
        SelectJoinStep<Record> step = context.select().from(Tables.EH_WORK_PLATFORM_APPS);
        Condition condition = Tables.EH_WORK_PLATFORM_APPS.SCOPE_ID.eq(scopeId)
                .and(Tables.EH_WORK_PLATFORM_APPS.SCOPE_TYPE.eq(ScopeType.ORGANIZATION.getCode()));
        step.where(condition).orderBy(Tables.EH_WORK_PLATFORM_APPS.ORDER.asc()).fetch().map((r) ->{
            list.add(ConvertHelper.convert(r, WorkPlatformApp.class));
            return null;
        });
        return list;
    }

    @Override
    public Integer getMaxSort(Long scopeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWorkPlatformApps.class));
        Condition condition = Tables.EH_WORK_PLATFORM_APPS.SCOPE_ID.eq(scopeId)
                .and(Tables.EH_WORK_PLATFORM_APPS.SCOPE_TYPE.eq(ScopeType.ORGANIZATION.getCode()));
         return context.select(DSL.max(Tables.EH_WORK_PLATFORM_APPS.ORDER)).from(Tables.EH_WORK_PLATFORM_APPS).where(condition).fetchOneInto(Integer.class);
    }

    @Override
    public WorkPlatformApp findWorkPlatformById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWorkPlatformApps.class));
        EhWorkPlatformAppsDao dao = new EhWorkPlatformAppsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkPlatformApp.class);
    }
}

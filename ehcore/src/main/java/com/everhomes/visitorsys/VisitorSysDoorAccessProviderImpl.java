package com.everhomes.visitorsys;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhVisitorSysDoorAccessDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysDoorAccess;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class VisitorSysDoorAccessProviderImpl implements VisitorSysDoorAccessProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public VisitorSysDoorAccess createVisitorSysDoorAccess(VisitorSysDoorAccess bean) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysDoorAccess.class));
        bean.setId(id);
        Long userId = UserContext.currentUserId();
        if(null != userId)
            bean.setCreatorUid(userId);
        bean.setCreateTime(new Timestamp(System.currentTimeMillis()));
        bean.setStatus((byte)2);
        EhVisitorSysDoorAccessDao dao = getDao(getReadWriteContext());
        dao.insert(bean);
        return bean;
    }

    @Override
    public VisitorSysDoorAccess updateVisitorSysDoorAccess(VisitorSysDoorAccess bean) {
        Long userId = UserContext.currentUserId();
        if(null != userId)
            bean.setOperatorUid(userId);
        bean.setOperateTime(new Timestamp(System.currentTimeMillis()));
        EhVisitorSysDoorAccessDao dao = getDao(getReadWriteContext());
        dao.update(bean);
        return bean;
    }

    @Override
    public void deleteVisitorSysDoorAccesss(Long id) {
        EhVisitorSysDoorAccessDao dao = getDao(getReadWriteContext());
        EhVisitorSysDoorAccess bean = dao.findById(id);
        bean.setStatus((byte)0);
        dao.update(bean);
    }

    @Override
    public VisitorSysDoorAccess findVisitorSysDoorAccess(Long id) {
        assert(null != id);
        EhVisitorSysDoorAccessDao dao = getDao(getReadOnlyContext());
        return ConvertHelper.convert(dao.findById(id),VisitorSysDoorAccess.class);
    }

    @Override
    public List<VisitorSysDoorAccess> listVisitorSysDoorAccessByOwner(Integer namespaceId, String ownerType, Long ownerId) {
        return this.listVisitorSysDoorAccess(namespaceId,ownerType,ownerId,null);
    }

    @Override
    public List<VisitorSysDoorAccess> listVisitorSysDoorAccess(Integer namespaceId, String ownerType, Long ownerId, Long doorAccessId) {
        SelectQuery query = getReadOnlyContext().selectQuery(Tables.EH_VISITOR_SYS_DOOR_ACCESS);
        if(null != namespaceId)
            query.addConditions(Tables.EH_VISITOR_SYS_DOOR_ACCESS.NAMESPACE_ID.eq(namespaceId));
        if(StringUtils.isNotEmpty(ownerType) && null != ownerId){
            query.addConditions(Tables.EH_VISITOR_SYS_DOOR_ACCESS.OWNER_TYPE.eq(ownerType));
            query.addConditions(Tables.EH_VISITOR_SYS_DOOR_ACCESS.OWNER_ID.eq(ownerId));
        }
        if(null != doorAccessId)
            query.addConditions(Tables.EH_VISITOR_SYS_DOOR_ACCESS.DOOR_ACCESS_ID.eq(doorAccessId));
        query.addConditions(Tables.EH_VISITOR_SYS_DOOR_ACCESS.STATUS.eq((byte)2));
        return query.fetch().map(r -> ConvertHelper.convert(r,VisitorSysDoorAccess.class));
    }

    private EhVisitorSysDoorAccessDao getDao(DSLContext context) {
        return new EhVisitorSysDoorAccessDao(context.configuration());
    }

    private DSLContext getReadWriteContext() {
        return getContext(AccessSpec.readWrite());
    }

    private DSLContext getReadOnlyContext() {
        return getContext(AccessSpec.readOnly());
    }

    private DSLContext getContext(AccessSpec accessSpec) {
        return dbProvider.getDslContext(accessSpec);
    }
}

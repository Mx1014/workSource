// @formatter:off
package com.everhomes.pm;

import static com.everhomes.server.schema.Tables.EH_COMMUNITY_PM_MEMBERS;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import org.jooq.Condition;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SortField;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.group.Group;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhCommunityPmMembersRecord;
import com.everhomes.server.schema.tables.records.EhGroupsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.jooq.JooqHelper;
import com.everhomes.region.Region;
import com.everhomes.region.RegionAdminStatus;
import com.everhomes.region.RegionScope;
import com.everhomes.server.schema.tables.daos.EhCommunityAddressMappingsDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmBillsDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmMembersDao;
import com.everhomes.server.schema.tables.daos.EhRegionsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunityAddressMappings;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmBills;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmMembers;
import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.server.schema.tables.records.EhCommunityAddressMappingsRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmBillsRecord;
import com.everhomes.server.schema.tables.records.EhRegionsRecord;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;


@Component
public class PropertyMgrProviderImpl implements PropertyMgrProvider {

    @Autowired
    private DbProvider dbProvider;
    

    // ??? How to set cache if there is more than one parameters? 
    //@Cacheable(value="Region", key="#regionId")
    @Override
    public List<PmMember> findPmMemberByTargetTypeAndId(String targetType, long targetId) {
    	final List<PmMember> groups = new ArrayList<PmMember>();
    	
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_TYPE.eq(targetType));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(targetId));
        
        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, PmMember.class));
            return null;
        });
        
        return groups;
    }
    
    @Override
    public List<PmMember> findPmMemberByCommunityAndTarget(long communityId, String targetType, long targetId) {
    	final List<PmMember> groups = new ArrayList<PmMember>();
    	
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_TYPE.eq(targetType));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(targetId));
        
        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, PmMember.class));
            return null;
        });
        
        return groups;
    }

    @CacheEvict(value = "CommunityPmMember", key="#communityPmMember.id")
    @Override
    public void createPropMember(CommunityPmMember communityPmMember) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhCommunityPmMembersRecord record = ConvertHelper.convert(communityPmMember, EhCommunityPmMembersRecord.class);
        InsertQuery<EhCommunityPmMembersRecord> query = context.insertQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        query.setRecord(record);
        query.setReturning(Tables.EH_COMMUNITY_PM_MEMBERS.ID);
        query.execute();
        
        communityPmMember.setId(query.getReturnedRecord().getId());
        
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCommunityPmMembers.class, null);
    }
    
    @CacheEvict(value = "CommunityPmMember", key="#communityPmMember.id")
    @Override
    public void updatePropMember(CommunityPmMember communityPmMember){
    	assert(communityPmMember.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmMembersDao dao = new EhCommunityPmMembersDao(context.configuration());
    	dao.update(communityPmMember);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmMembers.class, communityPmMember.getId());
    }
    
    @CacheEvict(value = "CommunityPmMember", key="#communityPmMember.id")
    @Override
    public void deletePropMember(CommunityPmMember communityPmMember){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmMembersDao dao = new EhCommunityPmMembersDao(context.configuration());
    	dao.deleteById(communityPmMember.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmMembers.class, communityPmMember.getId());
    }
    
    @Cacheable(value="CommunityPmMember", key="#id")
    @Override
    public void deletePropMember(long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmMembersDao dao = new EhCommunityPmMembersDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmMembers.class, id);
    }
     
    @Cacheable(value="CommunityPmMember", key="#id")
    @Override
    public CommunityPmMember findPropMemberById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityPmMembersDao dao = new EhCommunityPmMembersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CommunityPmMember.class);
    }
    
    @Cacheable(value = "listCommunityPmMembers")
    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public List<CommunityPmMember> listCommunityPmMembers(Long communityId, Long userId,String contactToken, Tuple<String, SortOrder>... orderBy) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_COMMUNITY_PM_MEMBERS, orderBy);
        List<CommunityPmMember> result;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_COMMUNITY_PM_MEMBERS);
        Condition condition = null;
        if(communityId != null)
            condition = Tables.EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId);
        
        if(userId != null) {
            if(condition != null)
                condition = condition.and(Tables.EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(userId));
            else
                condition = Tables.EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(userId);
        }
        
        if(contactToken != null) {
            if(condition != null)
                condition = condition.and(Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_TOKEN.eq(contactToken));
            else
                condition = Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_TOKEN.eq(contactToken);
        }
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_COMMUNITY_PM_MEMBERS.recordType(), CommunityPmMember.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_COMMUNITY_PM_MEMBERS.recordType(), CommunityPmMember.class)
            );
        }
        
        return result;
    }
    
    @CacheEvict(value = "CommunityAddressMapping", key="#communityAddressMapping.id")
    @Override
    public void createPropAddressMapping(CommunityAddressMapping communityAddressMapping) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhCommunityAddressMappingsRecord record = ConvertHelper.convert(communityAddressMapping, EhCommunityAddressMappingsRecord.class);
        InsertQuery<EhCommunityAddressMappingsRecord> query = context.insertQuery(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS);
        query.setRecord(record);
        query.setReturning(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.ID);
        query.execute();
        
        communityAddressMapping.setId(query.getReturnedRecord().getId());
        
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCommunityAddressMappingsRecord.class, null);
    }
    
    @CacheEvict(value = "CommunityAddressMapping", key="#communityAddressMapping.id")
    @Override
    public void updatePropAddressMapping(CommunityAddressMapping communityAddressMapping){
    	assert(communityAddressMapping.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityAddressMappingsDao dao = new EhCommunityAddressMappingsDao(context.configuration());
    	dao.update(communityAddressMapping);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityAddressMappings.class, communityAddressMapping.getId());
    }
    
    @CacheEvict(value = "CommunityAddressMapping", key="#communityAddressMapping.id")
    @Override
    public void deletePropAddressMapping(CommunityAddressMapping communityAddressMapping){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityAddressMappingsDao dao = new EhCommunityAddressMappingsDao(context.configuration());
    	dao.deleteById(communityAddressMapping.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityAddressMappings.class, communityAddressMapping.getId());
    }
    
    @Cacheable(value="CommunityAddressMapping", key="#id")
    @Override
    public void deletePropAddressMapping(long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityAddressMappingsDao dao = new EhCommunityAddressMappingsDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityAddressMappings.class, id);
    }
     
    @Cacheable(value="CommunityAddressMapping", key="#id")
    @Override
    public CommunityAddressMapping findPropAddressMappingById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityAddressMappingsDao dao = new EhCommunityAddressMappingsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CommunityAddressMapping.class);
    }
    
    @Cacheable(value = "listCommunityAddressMappings")
    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId,Tuple<String, SortOrder>... orderBy) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS, orderBy);
        List<CommunityAddressMapping> result;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS);
        Condition condition = null;
        if(communityId != null)
            condition = Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId);
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.recordType(), CommunityAddressMapping.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.recordType(), CommunityAddressMapping.class)
            );
        }
        
        return result;
    }
   
    @CacheEvict(value = "CommunityPmBill", key="#communityPmBill.id")
    @Override
    public void createPropBill(CommunityPmBill communityPmBill) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhCommunityPmBillsRecord record = ConvertHelper.convert(communityPmBill, EhCommunityPmBillsRecord.class);
        InsertQuery<EhCommunityPmBillsRecord> query = context.insertQuery(Tables.EH_COMMUNITY_PM_BILLS);
        query.setRecord(record);
        query.setReturning(Tables.EH_COMMUNITY_PM_BILLS.ID);
        query.execute();
        
        communityPmBill.setId(query.getReturnedRecord().getId());
        
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCommunityPmBills.class, null);
    }
    
    @CacheEvict(value = "CommunityPmBill", key="#communityPmBill.id")
    @Override
    public void updatePropBill(CommunityPmBill communityPmBill){
    	assert(communityPmBill.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmBillsDao dao = new EhCommunityPmBillsDao(context.configuration());
    	dao.update(communityPmBill);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmBills.class, communityPmBill.getId());
    }
    
    @CacheEvict(value = "CommunityPmBill", key="#communityPmBill.id")
    @Override
    public void deletePropBill(CommunityPmBill communityPmBill){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmBillsDao dao = new EhCommunityPmBillsDao(context.configuration());
    	dao.deleteById(communityPmBill.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmBills.class, communityPmBill.getId());
    }
    
    @CacheEvict(value = "CommunityPmBill", key="#id")
    @Override
    public void deletePropBill(long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmBillsDao dao = new EhCommunityPmBillsDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmBills.class, id);
    }
     
    @CacheEvict(value = "CommunityPmBill", key="id")
    @Override
    public CommunityPmBill findPropBillById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityPmBillsDao dao = new EhCommunityPmBillsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CommunityPmBill.class);
    }
    
    @Cacheable(value = "listCommunityPmBills")
    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr,String address, Tuple<String, SortOrder>... orderBy) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_COMMUNITY_PM_BILLS, orderBy);
        List<CommunityPmBill> result;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_COMMUNITY_PM_BILLS);
        Condition condition = null;
        if(communityId != null)
            condition = Tables.EH_COMMUNITY_PM_BILLS.COMMUNITY_ID.eq(communityId);
        
        if(dateStr != null) {
            if(condition != null)
                condition = condition.and(Tables.EH_COMMUNITY_PM_BILLS.DATE_STR.eq(dateStr));
            else
                condition = Tables.EH_COMMUNITY_PM_BILLS.DATE_STR.eq(dateStr);
        }
        
        if(address != null) {
            if(condition != null)
                condition = condition.and(Tables.EH_COMMUNITY_PM_BILLS.ADDRESS.eq(address));
            else
                condition = Tables.EH_COMMUNITY_PM_BILLS.ADDRESS.eq(address);
        }
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_COMMUNITY_PM_BILLS.recordType(), EhCommunityPmBills.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_COMMUNITY_PM_BILLS.recordType(), EhCommunityPmBills.class)
            );
        }
        
        return result;
    }
    

}

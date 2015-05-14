// @formatter:off
package com.everhomes.pm;

import static com.everhomes.server.schema.Tables.EH_COMMUNITY_PM_MEMBERS;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupOpRequest;
import com.everhomes.jooq.JooqHelper;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCommunityAddressMappingsDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmBillsDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmMembersDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmOwnersDao;
import com.everhomes.server.schema.tables.daos.EhGroupsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunityAddressMappings;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmBills;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmMembers;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmOwners;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.records.EhCommunityAddressMappingsRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmBillsRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmMembersRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmOwnersRecord;
import com.everhomes.util.ConvertHelper;
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
        
       EhCommunityPmMembersDao dao = new EhCommunityPmMembersDao(context.configuration());
        dao.insert(communityPmMember);
        
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
    @Override
    public List<CommunityPmMember> listCommunityPmMembers(Long communityId, Long userId, String contactToken,Integer pageOffset,Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmMember> result  = new ArrayList<CommunityPmMember>();
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        if(communityId != null)
           query.addConditions(Tables.EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId));
        if(userId != null) {
            query.addConditions(Tables.EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(userId));
        }
       
        if(contactToken != null) {
        	query.addConditions(Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_TOKEN.eq(contactToken));
        }
        
        Integer offset = (pageOffset - 1 ) * pageSize;
        query.addOrderBy(Tables.EH_COMMUNITY_PM_MEMBERS.ID.asc());
        query.addLimit(offset, pageSize);
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, CommunityPmMember.class));
            return null;
        });
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
    @Override
    public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

         List<CommunityAddressMapping> result  = new ArrayList<CommunityAddressMapping>();
         SelectQuery<EhCommunityAddressMappingsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS);
         if(communityId != null)
            query.addConditions(Tables.EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId));
         Integer offset = (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.ID.asc());
         query.addLimit(offset, pageSize);
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, CommunityAddressMapping.class));
             return null;
         });
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
    @Override
    public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr,String address, Integer pageOffset,Integer pageSize) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmBill> result  = new ArrayList<CommunityPmBill>();
        SelectQuery<EhCommunityPmBillsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_BILLS);
        if(communityId != null)
           query.addConditions(Tables.EH_COMMUNITY_PM_BILLS.COMMUNITY_ID.eq(communityId));
        if(dateStr != null) {
            query.addConditions(Tables.EH_COMMUNITY_PM_BILLS.DATE_STR.eq(dateStr));
        }
       
        if(address != null) {
        	query.addConditions(Tables.EH_COMMUNITY_PM_BILLS.ADDRESS.eq(address));
        }
        
        Integer offset = (pageOffset - 1 ) * pageSize;
        query.addOrderBy(Tables.EH_COMMUNITY_PM_BILLS.ID.asc());
        query.addLimit(offset, pageSize);
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, CommunityPmBill.class));
            return null;
        });
        return result;
    }
    
    @CacheEvict(value = "CommunityPmOwner", key="#communityPmOwner.id")
    @Override
    public void createPropOwner(CommunityPmOwner communityPmOwner) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
       EhCommunityPmOwnersDao dao = new EhCommunityPmOwnersDao(context.configuration());
        dao.insert(communityPmOwner);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCommunityPmOwners.class, null);
    }
    
    @CacheEvict(value = "CommunityPmOwner", key="#communityPmOwner.id")
    @Override
    public void updatePropOwner(CommunityPmOwner communityPmOwner){
    	assert(communityPmOwner.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmOwnersDao dao = new EhCommunityPmOwnersDao(context.configuration());
    	dao.update(communityPmOwner);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmOwners.class, communityPmOwner.getId());
    }
    
    @CacheEvict(value = "CommunityPmOwner", key="#communityPmOwner.id")
    @Override
    public void deletePropOwner(CommunityPmOwner communityPmOwner){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmOwnersDao dao = new EhCommunityPmOwnersDao(context.configuration());
    	dao.deleteById(communityPmOwner.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmOwners.class, communityPmOwner.getId());
    }
    
    @Cacheable(value="CommunityPmOwner", key="#id")
    @Override
    public void deletePropOwner(long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmOwnersDao dao = new EhCommunityPmOwnersDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmOwners.class, id);
    }
     
    @Cacheable(value="CommunityPmOwner", key="#id")
    @Override
    public CommunityPmOwner findPropOwnerById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityPmOwnersDao dao = new EhCommunityPmOwnersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CommunityPmOwner.class);
    }
    
    @Cacheable(value = "listCommunityPmOwners")
    @Override
    public List<CommunityPmOwner> listCommunityPmOwners(Long communityId, String address, String contactToken,Integer pageOffset,Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
        SelectQuery<EhCommunityPmOwnersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_OWNERS);
        if(communityId != null)
           query.addConditions(Tables.EH_COMMUNITY_PM_OWNERS.COMMUNITY_ID.eq(communityId));
        if(address != null) {
            query.addConditions(Tables.EH_COMMUNITY_PM_OWNERS.ADDRESS.eq(address));
        }
       
        if(contactToken != null) {
        	query.addConditions(Tables.EH_COMMUNITY_PM_OWNERS.CONTACT_TOKEN.eq(contactToken));
        }
        
        Integer offset = (pageOffset - 1 ) * pageSize;
        query.addOrderBy(Tables.EH_COMMUNITY_PM_OWNERS.ID.asc());
        query.addLimit(offset, pageSize);
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
            return null;
        });
        return result;
    }
    

}

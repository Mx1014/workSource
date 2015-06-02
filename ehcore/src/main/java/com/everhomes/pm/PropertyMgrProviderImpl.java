// @formatter:off
package com.everhomes.pm;

import static com.everhomes.server.schema.Tables.EH_COMMUNITY_PM_MEMBERS;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCommunityAddressMappingsDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmBillItemsDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmBillsDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmMembersDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmOwnersDao;
import com.everhomes.server.schema.tables.daos.EhCommunityPmTasksDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhCommunityAddressMappings;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmBillItems;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmBills;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmMembers;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmOwners;
import com.everhomes.server.schema.tables.pojos.EhCommunityPmTasks;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhCommunityAddressMappingsRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmBillItemsRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmBillsRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmMembersRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmOwnersRecord;
import com.everhomes.server.schema.tables.records.EhCommunityPmTasksRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.Tuple;


@Component
public class PropertyMgrProviderImpl implements PropertyMgrProvider {

    @Autowired
    private DbProvider dbProvider;
    

    // ??? How to set cache if there is more than one parameters? 
    //@Cacheable(value="Region", key="#regionId")
    @Cacheable(value = "listUserCommunityPmMembers")
    @Override
    public List<CommunityPmMember> listUserCommunityPmMembers(Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<CommunityPmMember> result  = new ArrayList<CommunityPmMember>();
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        if(userId != null)
           query.addConditions(Tables.EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(userId));
       
        query.addOrderBy(Tables.EH_COMMUNITY_PM_MEMBERS.ID.asc());
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, CommunityPmMember.class));
            return null;
        });
        return result;
    }
    
    @Override
    public List<CommunityPmMember> findPmMemberByTargetTypeAndId(String targetType, long targetId) {
    	final List<CommunityPmMember> groups = new ArrayList<CommunityPmMember>();
    	
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_TYPE.eq(targetType));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(targetId));
        
        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, CommunityPmMember.class));
            return null;
        });
        
        return groups;
    }
    
    @Override
    public List<CommunityPmMember> findPmMemberByCommunityAndTarget(long communityId, String targetType, long targetId) {
    	final List<CommunityPmMember> groups = new ArrayList<CommunityPmMember>();
    	
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_TYPE.eq(targetType));
        query.addConditions(EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(targetId));
        
        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, CommunityPmMember.class));
            return null;
        });
        
        return groups;
    }

    @CacheEvict(value="CommunityPmMember", key="#communityPmMember.id")
    //@Cache(value = "CommunityPmMember", key="#communityPmMember.id")
    @Override
    public void createPropMember(CommunityPmMember communityPmMember) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhCommunityPmMembersDao dao = new EhCommunityPmMembersDao(context.configuration());
        dao.insert(communityPmMember);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCommunityPmMembers.class, null);
    }
    
    @CacheEvict(value="CommunityPmMember", key="#communityPmMember.id")
    @Override
    public void updatePropMember(CommunityPmMember communityPmMember){
    	assert(communityPmMember.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmMembersDao dao = new EhCommunityPmMembersDao(context.configuration());
    	dao.update(communityPmMember);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmMembers.class, communityPmMember.getId());
    }
    
    @CacheEvict(value="CommunityPmMember", key="#communityPmMember.id")
    @Override
    public void deletePropMember(CommunityPmMember communityPmMember){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmMembersDao dao = new EhCommunityPmMembersDao(context.configuration());
    	dao.deleteById(communityPmMember.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmMembers.class, communityPmMember.getId());
    }
    
    //@CacheEvict(value="CommunityPmMember", key="#id")
    @CacheEvict(value="CommunityPmMember", key="#id")
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
    
    //@Cacheable(value = "CommunityPmMemberList", key="#communityId")
    @Override
    public List<CommunityPmMember> listCommunityPmMembers(Long communityId, String contactToken,Integer pageOffset,Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmMember> result  = new ArrayList<CommunityPmMember>();
        SelectQuery<EhCommunityPmMembersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_MEMBERS);
        if(communityId != null)
           query.addConditions(Tables.EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId));
       
        if(contactToken != null && !"".equals(contactToken)) {
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
    
    @Override
    public int countCommunityPmMembers(long communityId, String contactToken) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_COMMUNITY_PM_MEMBERS);
        Condition condition = Tables.EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId);
        if(contactToken != null && !"".equals(contactToken)) {
            condition.and(Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_TOKEN.eq(contactToken));
        }

        return step.where(condition).fetchOneInto(Integer.class);
    }
    
    @CacheEvict(value="CommunityAddressMapping", key="#communityAddressMapping.id")
    //@Cacheable(value = "CommunityAddressMapping", key="#communityAddressMapping.id")
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
    
   
    @Caching(evict = { @CacheEvict(value="CommunityAddressMapping", key="#communityAddressMapping.id"), 
            @CacheEvict(value="CommunityAddressMappingByAddressId", key="{#communityAddressMapping.communityId, #communityAddressMapping.addressId}"),
            @CacheEvict(value = "CommunityAddressMappingsList", key="#communityAddressMapping.communityId")})
    @Override
    public void updatePropAddressMapping(CommunityAddressMapping communityAddressMapping){
    	assert(communityAddressMapping.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityAddressMappingsDao dao = new EhCommunityAddressMappingsDao(context.configuration());
    	dao.update(communityAddressMapping);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityAddressMappings.class, communityAddressMapping.getId());
    }
    
    @Caching(evict = { @CacheEvict(value="CommunityAddressMapping", key="#communityAddressMapping.id"), 
            @CacheEvict(value="CommunityAddressMappingByAddressId", key="{#communityAddressMapping.communityId,#communityAddressMapping.addressId}"),
            @CacheEvict(value = "CommunityAddressMappingsList", key="#communityAddressMapping.communityId")})
    @Override
    public void deletePropAddressMapping(CommunityAddressMapping communityAddressMapping){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityAddressMappingsDao dao = new EhCommunityAddressMappingsDao(context.configuration());
    	dao.deleteById(communityAddressMapping.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityAddressMappings.class, communityAddressMapping.getId());
    }
    
    @Caching(evict = { @CacheEvict(value="CommunityAddressMapping", key="#communityAddressMapping.id"), 
            @CacheEvict(value="CommunityAddressMappingByAddressId", key="{#communityAddressMapping.communityId,#communityAddressMapping.addressId}"),
            @CacheEvict(value = "CommunityAddressMappingsList", key="#communityAddressMapping.communityId")})
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
    
    @Cacheable(value="CommunityAddressMappingByAddressId", key="{#communityId,#addressId}")
    @Override
    public CommunityAddressMapping findPropAddressMappingByAddressId(Long communityId,Long addressId){
        final CommunityAddressMapping[] result = new CommunityAddressMapping[1];
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhCommunityAddressMappingsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS);
        if(communityId != null)
            query.addConditions(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId));
       query.addConditions(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId));
       query.fetch().map((r) -> {
    	   if(r != null)
           result[0] = ConvertHelper.convert(r, CommunityAddressMapping.class);
    	   return null;
       });
       return result[0];
    }
    
    @Override
    public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

         List<CommunityAddressMapping> result  = new ArrayList<CommunityAddressMapping>();
         SelectQuery<EhCommunityAddressMappingsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS);
         if(communityId != null)
            query.addConditions(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId));
         Integer offset = (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.ID.asc());
         query.addLimit(offset, pageSize);
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, CommunityAddressMapping.class));
             return null;
         });
         return result;
    }
    
    @Override
    public int countCommunityAddressMappings(long communityId) {
        
         DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

         SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS);
         Condition condition = Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId);
         
         return step.where(condition).fetchOneInto(Integer.class);
    }
    
    @Cacheable(value = "CommunityAddressMappingsList", key="#communityId")
    @Override
    public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

         List<CommunityAddressMapping> result  = new ArrayList<CommunityAddressMapping>();
         SelectQuery<EhCommunityAddressMappingsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS);
         if(communityId != null)
            query.addConditions(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId));
         query.addOrderBy(Tables.EH_COMMUNITY_ADDRESS_MAPPINGS.ID.asc());
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, CommunityAddressMapping.class));
             return null;
         });
         return result;
    }
   
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
    
    @CacheEvict(value = "CommunityPmBill", key="#id")
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
     
    @Cacheable(value = "CommunityPmBill", key="#id")
    @Override
    public CommunityPmBill findPropBillById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityPmBillsDao dao = new EhCommunityPmBillsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CommunityPmBill.class);
    }
    
    @Override
    public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr,String address, Integer pageOffset,Integer pageSize) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmBill> result  = new ArrayList<CommunityPmBill>();
        SelectQuery<EhCommunityPmBillsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_BILLS);
        if(communityId != null){
           query.addConditions(Tables.EH_COMMUNITY_PM_BILLS.COMMUNITY_ID.eq(communityId));
        }
        if(dateStr != null && !"".equals(dateStr)) {
            query.addConditions(Tables.EH_COMMUNITY_PM_BILLS.DATE_STR.eq(dateStr));
        }
       
        if(address != null && !"".equals(address)) {
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
    
    @Cacheable(value = "listCommunityPmBills")
    @Override
    public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmBill> result  = new ArrayList<CommunityPmBill>();
        SelectQuery<EhCommunityPmBillsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_BILLS);
        if(communityId != null){
           query.addConditions(Tables.EH_COMMUNITY_PM_BILLS.COMMUNITY_ID.eq(communityId));
        }
        if(dateStr != null && !"".equals(dateStr)) {
            query.addConditions(Tables.EH_COMMUNITY_PM_BILLS.DATE_STR.eq(dateStr));
        }
        query.addOrderBy(Tables.EH_COMMUNITY_PM_BILLS.ID.asc());
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, CommunityPmBill.class));
            return null;
        });
        return result;
    }

    @Override
    public int countCommunityPmBills(long communityId, String dateStr, String address) {
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_COMMUNITY_PM_BILLS);
        Condition condition = Tables.EH_COMMUNITY_PM_BILLS.COMMUNITY_ID.eq(communityId);
        if(address != null && !"".equals(address)) {
            condition.and(Tables.EH_COMMUNITY_PM_BILLS.ADDRESS.eq(address));
        }
        
        if(dateStr != null && !"".equals(dateStr)) {
            condition.and(Tables.EH_COMMUNITY_PM_BILLS.DATE_STR.eq(dateStr));
        }
        
        return step.where(condition).fetchOneInto(Integer.class);
    }
    
    @Override
    public void createPropOwner(CommunityPmOwner communityPmOwner) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
       EhCommunityPmOwnersDao dao = new EhCommunityPmOwnersDao(context.configuration());
        dao.insert(communityPmOwner);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCommunityPmOwners.class, null);
    }
    
    @CacheEvict(value = "CommunityPmOwner", key="communityPmOwner.#id")
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
    
    @CacheEvict(value="CommunityPmOwner", key="#id")
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
    
    @Override
    public List<CommunityPmOwner> listCommunityPmOwners(Long communityId, String address, String contactToken,Integer pageOffset,Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
        SelectQuery<EhCommunityPmOwnersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_OWNERS);
        if(communityId != null)
           query.addConditions(Tables.EH_COMMUNITY_PM_OWNERS.COMMUNITY_ID.eq(communityId));
        if(address != null && !"".equals(address)) {
            query.addConditions(Tables.EH_COMMUNITY_PM_OWNERS.ADDRESS.eq(address));
        }
       
        if(contactToken != null && !"".equals(contactToken)) {
        	query.addConditions(Tables.EH_COMMUNITY_PM_OWNERS.CONTACT_TOKEN.eq(contactToken));
        }
        
        Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
        query.addOrderBy(Tables.EH_COMMUNITY_PM_OWNERS.ID.asc());
        query.addLimit(offset, pageSize);
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
            return null;
        });
        return result;
    }
    
    @Override
    public List<CommunityPmOwner> listCommunityPmOwners(Long communityId, Long addressId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
        SelectQuery<EhCommunityPmOwnersRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_OWNERS);
        if(communityId != null)
           query.addConditions(Tables.EH_COMMUNITY_PM_OWNERS.COMMUNITY_ID.eq(communityId));
        if(addressId != null && !"".equals(addressId)) {
            query.addConditions(Tables.EH_COMMUNITY_PM_OWNERS.ADDRESS_ID.eq(addressId));
        }
        query.addOrderBy(Tables.EH_COMMUNITY_PM_OWNERS.ID.desc());
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
            return null;
        });
        return result;
    }
    
    @Override
    public int countCommunityPmOwners(long communityId, String address, String contactToken) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_COMMUNITY_PM_OWNERS);
        Condition condition = Tables.EH_COMMUNITY_PM_OWNERS.COMMUNITY_ID.eq(communityId);
        if(address != null && !"".equals(address)) {

            condition.and(Tables.EH_COMMUNITY_PM_OWNERS.ADDRESS.eq(address));
        }
       
        if(contactToken != null && !"".equals(contactToken)) {
            condition.and(Tables.EH_COMMUNITY_PM_OWNERS.CONTACT_TOKEN.eq(contactToken));
        }
        
        return step.where(condition).fetchOneInto(Integer.class);
    }
    
    @Override
    public void createPmTask(CommunityPmTasks task) {
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhCommunityPmTasksDao dao = new EhCommunityPmTasksDao(context.configuration());
         dao.insert(task);
         
         DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCommunityPmTasks.class, null);
    }

    @Override
    public List<CommunityPmTasks> findPmTaskEntityIdAndTargetId(Long communityId, Long entityId,String entityType,
            Long targetId, String targetType, Byte status) {
        final List<CommunityPmTasks> groups = new ArrayList<CommunityPmTasks>();
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityPmTasksRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_TASKS);
        query.addConditions(Tables.EH_COMMUNITY_PM_TASKS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_COMMUNITY_PM_TASKS.ENTITY_ID.eq(entityId));
        query.addConditions(Tables.EH_COMMUNITY_PM_TASKS.ENTITY_TYPE.eq(entityType));
        query.addConditions(Tables.EH_COMMUNITY_PM_TASKS.TARGET_TYPE.eq(targetType));
        query.addConditions(Tables.EH_COMMUNITY_PM_TASKS.TARGET_ID.eq(targetId));
        
        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, CommunityPmTasks.class));
            return null;
        });
        
        return groups;
    }
    @Override
    public ListPropInvitedUserCommandResponse listInvitedUsers(Long communityId,String contactToken, Long pageOffset, Long pageSize) {

        final List<PropInvitedUserDTO> results = new ArrayList<>();
        ListPropInvitedUserCommandResponse response = new ListPropInvitedUserCommandResponse();
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        long size = pageSize;
        contactToken = contactToken == null ? "" : contactToken;
        String likeVal = contactToken + "%";
        Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
        if(offset > 0) {
            final List<Long> countsInShards = new ArrayList<>();
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, 
                    (DSLContext context, Object reducingContext)-> {
                        
                    Long count = context.selectCount().from(Tables.EH_COMMUNITY_PM_MEMBERS)
                            .leftOuterJoin(Tables.EH_USERS)
                            .on(Tables.EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.ID)
                                    .and(Tables.EH_COMMUNITY_PM_MEMBERS.TARGET_TYPE.eq(EntityType.USER.getCode()))
                                    .and(Tables.EH_COMMUNITY_PM_MEMBERS.STATUS.eq(PmMemberStatus.ACTIVE.getCode())))
                            .where(Tables.EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId))
                            .and(Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_TOKEN.like(likeVal)
                                    .or(Tables.EH_USERS.ACCOUNT_NAME.like(likeVal)))
                            .fetchOne(0, Long.class);
                    
                    countsInShards.add(count);
                    return true;
                });
            
            targetShard = PaginationHelper.offsetFallsAt(countsInShards, offset);
        }
        if(targetShard.first() < 0){
            response.setMembers(results);
            return response;
        }

        final int[] currentShard = new int[1];
        currentShard[0] = 0;
        final Tuple<Integer, Long> fallingShard = targetShard;
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), currentShard, 
            (DSLContext context, Object reducingContext)-> {
                int[] current = (int[])reducingContext;
                if(current[0] < fallingShard.first()) {
                    current[0] += 1;
                    return true;
                }
                
                long off = 0;
                if(current[0] == fallingShard.first())
                    off = fallingShard.second();
                
                context.select().from(Tables.EH_COMMUNITY_PM_MEMBERS)
                .leftOuterJoin(Tables.EH_USERS)
                .on(Tables.EH_COMMUNITY_PM_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.INVITOR_UID)
                        .and(Tables.EH_COMMUNITY_PM_MEMBERS.TARGET_TYPE.eq(EntityType.USER.getCode()))
                        .and(Tables.EH_COMMUNITY_PM_MEMBERS.STATUS.eq(PmMemberStatus.ACTIVE.getCode())))
                .where(Tables.EH_COMMUNITY_PM_MEMBERS.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_TOKEN.like(likeVal)
                                    .or(Tables.EH_USERS.ACCOUNT_NAME.like(likeVal)))
                    .limit((int)size).offset((int)off)
                    .fetch().map((r) -> {
                        //PropInvitedUserDTO user = ConvertHelper.convert(r, PropInvitedUserDTO.class);
                        PropInvitedUserDTO user = new PropInvitedUserDTO();
                        user.setUserId(r.getValue(Tables.EH_USERS.ID));
                        user.setUserName(r.getValue(Tables.EH_USERS.ACCOUNT_NAME));
                        user.setInviteType(r.getValue(Tables.EH_USERS.INVITE_TYPE));
                        user.setRegisterTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
                        user.setContactType(r.getValue(Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_TYPE));
                        user.setContactToken(r.getValue(Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_TOKEN));
                        user.setInvitorId(r.getValue(Tables.EH_USERS.INVITOR_UID));
                        user.setInvitorName(r.getValue(Tables.EH_COMMUNITY_PM_MEMBERS.CONTACT_NAME));
                        if(results.size() <= pageSize + 1)
                            results.add(user);
                        return null;
                });
                
            return true;
        });
        
        if(results.size() > pageSize) {
            results.remove(results.size() - 1);
            response.setMembers(results);
            return response;
        }
        return response;
        
    }
    
    @Override
    public void createPropBillItem(CommunityPmBillItem communityPmBillItem) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmBillItemsDao dao = new EhCommunityPmBillItemsDao(context.configuration());
    	dao.insert(communityPmBillItem);
    	
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCommunityPmBillItems.class, null);
    }
    
    @Caching(evict = { @CacheEvict(value="CommunityPmBillItem", key="#communityPmBillItem.id"),
            @CacheEvict(value="CommunityPmBillItemsList", key="#communityPmBillItem.billId")})
    @Override
    public void updatePropBillItem(CommunityPmBillItem communityPmBillItem){
    	assert(communityPmBillItem.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmBillItemsDao dao = new EhCommunityPmBillItemsDao(context.configuration());
    	dao.update(communityPmBillItem);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmBillItems.class, communityPmBillItem.getId());
    }
    
    @Caching(evict = { @CacheEvict(value="CommunityPmBillItem", key="#communityPmBillItem.id"),
            @CacheEvict(value="CommunityPmBillItemsList", key="#communityPmBillItem.billId")})
    @Override
    public void deletePropBillItem(CommunityPmBillItem communityPmBillItem){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmBillItemsDao dao = new EhCommunityPmBillItemsDao(context.configuration());
    	dao.deleteById(communityPmBillItem.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmBillItems.class, communityPmBillItem.getId());
    }
    
    @Caching(evict = { @CacheEvict(value="CommunityPmBillItem", key="#id"),
            @CacheEvict(value="CommunityPmBillItemsList", key="#billId")})
    @Override
    public void deletePropBillItem(long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhCommunityPmBillItemsDao dao = new EhCommunityPmBillItemsDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityPmBillItems.class, id);
    }
     
    @Cacheable(value = "CommunityPmBillItem", key="#id")
    @Override
    public CommunityPmBillItem findPropBillItemById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityPmBillItemsDao dao = new EhCommunityPmBillItemsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CommunityPmBillItem.class);
    }
    
    @Cacheable(value = "CommunityPmBillItemsList", key="#billId")
    @Override
    public List<CommunityPmBillItem> listCommunityPmBillItems(Long billId) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        List<CommunityPmBillItem> result  = new ArrayList<CommunityPmBillItem>();
        SelectQuery<EhCommunityPmBillItemsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_BILL_ITEMS);
        query.addConditions(Tables.EH_COMMUNITY_PM_BILL_ITEMS.BILL_ID.eq(billId));
        query.addOrderBy(Tables.EH_COMMUNITY_PM_BILL_ITEMS.ID.asc());
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, CommunityPmBillItem.class));
            return null;
        });
        return result;
    }
    
	@Override
	public List<String> listPropBillDateStr(Long communityId) {
		List<String> dateList = new ArrayList<String>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		context.selectDistinct(Tables.EH_COMMUNITY_PM_BILLS.DATE_STR).from(Tables.EH_COMMUNITY_PM_BILLS)
			.where(Tables.EH_COMMUNITY_PM_BILLS.COMMUNITY_ID.eq(communityId))
			.fetch().map((r) -> {
				 dateList.add(r.getValue(Tables.EH_COMMUNITY_PM_BILLS.DATE_STR));
				 return null;
		});
		
		return dateList;
	}

    @Override
    public List<CommunityPmTasks> findPmTaskEntityId(long communityId, long entityId, String entityType) {
        
        final List<CommunityPmTasks> tasks = new ArrayList<CommunityPmTasks>();
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityPmTasksRecord> query = context.selectQuery(Tables.EH_COMMUNITY_PM_TASKS);
        query.addConditions(Tables.EH_COMMUNITY_PM_TASKS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_COMMUNITY_PM_TASKS.ENTITY_ID.eq(entityId));
        query.addConditions(Tables.EH_COMMUNITY_PM_TASKS.ENTITY_TYPE.eq(entityType));
        
        query.fetch().map((r) -> {
            tasks.add(ConvertHelper.convert(r, CommunityPmTasks.class));
            return null;
        });
        
        return tasks;
    }

    @Override
    public void updatePmTaskListStatus(List<CommunityPmTasks> tasks) {
        assert(tasks != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityPmTasksDao dao = new EhCommunityPmTasksDao(context.configuration());
        List<EhCommunityPmTasks> pmTasks = new ArrayList<EhCommunityPmTasks>();
        tasks.stream().map((r) ->{
            pmTasks.add(ConvertHelper.convert(r, EhCommunityPmTasks.class));
            return null;
        });
        dao.update(pmTasks);
    }

}

// @formatter:off
package com.everhomes.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.forum.Post;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOrganizationCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationMembersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationsDao;
import com.everhomes.server.schema.tables.pojos.EhOrganizationCommunities;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.records.EhOrganizationAddressMappingsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationMembersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.util.ConvertHelper;
@Component
public class OrganizationProviderImpl implements OrganizationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void createOrganization(Organization department) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationsRecord record = ConvertHelper.convert(department, EhOrganizationsRecord.class);
        InsertQuery<EhOrganizationsRecord> query = context.insertQuery(Tables.EH_ORGANIZATIONS);
        query.setRecord(record);
        query.setReturning(Tables.EH_ORGANIZATIONS.ID);
        query.execute();
        
        department.setId(query.getReturnedRecord().getId());
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizations.class, null); 
        
    }

    
    @Override
    public void updateOrganization(Organization department){
    	assert(department.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
    	dao.update(department);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, department.getId());
    }
    
    
    @Override
    public void deleteOrganization(Organization department){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
    	dao.deleteById(department.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, department.getId());
    }
    
    
    @Override
    public void deleteOrganizationById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, id);
    }
     

    @Override
    public Organization findOrganizationById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Organization.class);
    }
    
    @Override
    public List<Organization> findOrganizationByCommunityId(Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addJoin(Tables.EH_ORGANIZATION_COMMUNITIES, JoinType.LEFT_OUTER_JOIN, 
            Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));
        query.setDistinct(true);
        
        List<EhOrganizationsRecord> records = query.fetch().map(new EhOrganizationRecordMapper());
        List<Organization> organizations = records.stream().map((r) -> {
            return ConvertHelper.convert(r, Organization.class);
        }).collect(Collectors.toList());
        
        return organizations;
    }
    
    @Override
    public List<Organization> listOrganizations(String organizationType,String name,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<Organization> result  = new ArrayList<Organization>();
         SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
         if(organizationType != null && !"".equals(organizationType)) {
          	query.addConditions(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(organizationType));
          }
         if(name != null && !"".equals(name)) {
         	query.addConditions(Tables.EH_ORGANIZATIONS.NAME.eq(name));
         }
         
         Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());
         query.addLimit(offset, pageSize);
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, Organization.class));
             return null;
         });
         return result;
    }

    @Override
    public void createOrganizationMember(OrganizationMember departmentMember) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationMembersRecord record = ConvertHelper.convert(departmentMember, EhOrganizationMembersRecord.class);
        InsertQuery<EhOrganizationMembersRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.setRecord(record);
        query.setReturning(Tables.EH_ORGANIZATIONS.ID);
        query.execute();
        
        departmentMember.setId(query.getReturnedRecord().getId());
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationMembers.class, null); 
        
    }

    
    @Override
    public void updateOrganizationMember(OrganizationMember departmentMember){
    	assert(departmentMember.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
    	dao.update(departmentMember);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, departmentMember.getId());
    }
    
    
    @Override
    public void deleteOrganizationMember(OrganizationMember departmentMember){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
    	dao.deleteById(departmentMember.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, departmentMember.getId());
    }
    
    
    @Override
    public void deleteOrganizationMemberById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, id);
    }
     

    @Override
    public OrganizationMember findOrganizationMemberById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), OrganizationMember.class);
    }
    
    @Override
    public List<OrganizationMember> listOrganizationMembers(Long departmentId, Long memberUid,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<OrganizationMember> result  = new ArrayList<OrganizationMember>();
         SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
         if(departmentId != null && departmentId > 0){
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(departmentId));
         }
         if(memberUid != null && memberUid > 0) {
         	query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
         }
         
         Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
         query.addLimit(offset, pageSize);
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, OrganizationMember.class));
             return null;
         });
         return result;
    }
    
    @Override
    public List<OrganizationMember> listOrganizationMembers(Long memberUid) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<OrganizationMember> result  = new ArrayList<OrganizationMember>();
         SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
         if(memberUid != null && memberUid > 0) {
         	query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
         }
         query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, OrganizationMember.class));
             return null;
         });
         return result;
    }
    
    @Override
    public void createOrganizationCommunity(OrganizationCommunity departmentCommunity) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationCommunitiesRecord record = ConvertHelper.convert(departmentCommunity, EhOrganizationCommunitiesRecord.class);
        InsertQuery<EhOrganizationCommunitiesRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
        query.setRecord(record);
        query.setReturning(Tables.EH_ORGANIZATION_COMMUNITIES.ID);
        query.execute();
        
        departmentCommunity.setId(query.getReturnedRecord().getId());
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationCommunities.class, null); 
        
    }

    
    @Override
    public void updateOrganizationCommunity(OrganizationCommunity departmentCommunity){
    	assert(departmentCommunity.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
    	dao.update(departmentCommunity);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, departmentCommunity.getId());
    }
    
    
    @Override
    public void deleteOrganizationCommunity(OrganizationCommunity departmentCommunity){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
    	dao.deleteById(departmentCommunity.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, departmentCommunity.getId());
    }
    
    
    @Override
    public void deleteOrganizationCommunityById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, id);
    }
     
    @Override
    public OrganizationCommunity findOrganizationCommunityByOrgIdAndCmmtyId(Long orgId, Long cmmtyId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_ORGANIZATION_COMMUNITIES);
        Condition condition = Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId.longValue());
        condition = condition.and(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(cmmtyId.longValue()));
        final OrganizationCommunity[] result = new OrganizationCommunity[1];
        step.where(condition).fetch().map(r -> {
            result[0] = ConvertHelper.convert(r,OrganizationCommunity.class);
            return null;
        });
        return result[0];
    }

    @Override
    public OrganizationCommunity findOrganizationCommunityById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), OrganizationCommunity.class);
    }
    
    @Override
    public List<OrganizationCommunity> listOrganizationCommunities(Long departmentId,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<OrganizationCommunity> result  = new ArrayList<OrganizationCommunity>();
         SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
         if(departmentId != null && departmentId > 0){
             query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(departmentId));
          }
         
         Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
         query.addLimit(offset, pageSize);
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, OrganizationCommunity.class));
             return null;
         });
         return result;
    }
    
    @Override
    public List<OrganizationCommunity> listOrganizationCommunities(Long departmentId) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<OrganizationCommunity> result  = new ArrayList<OrganizationCommunity>();
         SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
         if(departmentId != null && departmentId > 0){
             query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(departmentId));
          }
         query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, OrganizationCommunity.class));
             return null;
         });
         return result;
    }
    
    @Override
    public List<OrganizationCommunity> listOrganizationByCommunityId(Long communityId) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationCommunity> result  = new ArrayList<OrganizationCommunity>();
        SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
        if(communityId != null && communityId > 0){
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId));
         }
        query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, OrganizationCommunity.class));
            return null;
        });
        return result;
    }
    
    @Override
	public OrganizationCommunity findOrganizationProperty(Long communityId) {
	   final OrganizationCommunity[] result = new OrganizationCommunity[1];
       DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
       SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
       if(communityId != null && communityId > 0){
           query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId));
       }
       query.fetch().map((r) -> {
   	   if(r != null)
   		   	result[0] = ConvertHelper.convert(r, OrganizationCommunity.class);
   	   		return null;
       });
       return result[0];
	}
    
    @Override
	public OrganizationCommunity findOrganizationPropertyCommunity(Long organizationId) {
	   final OrganizationCommunity[] result = new OrganizationCommunity[1];
       DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
       SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
       if(organizationId != null && organizationId > 0){
           query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId));
       }
       query.fetch().map((r) -> {
   	   if(r != null)
   		   	result[0] = ConvertHelper.convert(r, OrganizationCommunity.class);
   	   		return null;
       });
       return result[0];
	}
    
    @Override
    public int countOrganizations(String name) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATIONS);
        Condition condition = Tables.EH_ORGANIZATIONS.ID.greaterOrEqual(0L);
        if(!StringUtils.isEmpty(name))
        	condition = condition.and(Tables.EH_ORGANIZATIONS.NAME.eq(name));
        return step.where(condition).fetchOneInto(Integer.class);
    }
    
   @Override
    public int countOrganizationMembers(Long departmentId, Long memberUid) {
	   DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

       SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_MEMBERS);
       Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(departmentId);
       if(memberUid != null && memberUid > 0)
       		condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
       return step.where(condition).fetchOneInto(Integer.class);
    }
   
   @Override
	public int countOrganizationCommunitys(Long organizationId) {
	   DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

       SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_COMMUNITIES);
       Condition condition = Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId);
       return step.where(condition).fetchOneInto(Integer.class);
	}

}

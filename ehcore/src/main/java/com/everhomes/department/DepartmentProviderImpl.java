// @formatter:off
package com.everhomes.department;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
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
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOrganizationCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationMembersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationsDao;
import com.everhomes.server.schema.tables.pojos.EhOrganizationCommunities;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.records.EhOrganizationCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationMembersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.util.ConvertHelper;
@Component
public class DepartmentProviderImpl implements DepartmentProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void createDepartment(Department department) {
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
    public void updateDepartment(Department department){
    	assert(department.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
    	dao.update(department);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, department.getId());
    }
    
    
    @Override
    public void deleteDepartment(Department department){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
    	dao.deleteById(department.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, department.getId());
    }
    
    
    @Override
    public void deleteDepartmentById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, id);
    }
     

    @Override
    public Department findDepartmentById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Department.class);
    }
    
    @Override
    public List<Department> listDepartments(String name,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<Department> result  = new ArrayList<Department>();
         SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
         if(name != null && !"".equals(name)) {
         	query.addConditions(Tables.EH_ORGANIZATIONS.NAME.eq(name));
         }
         
         Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());
         query.addLimit(offset, pageSize);
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, Department.class));
             return null;
         });
         return result;
    }

    @Override
    public void createDepartmentMember(DepartmentMember departmentMember) {
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
    public void updateDepartmentMember(DepartmentMember departmentMember){
    	assert(departmentMember.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
    	dao.update(departmentMember);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, departmentMember.getId());
    }
    
    
    @Override
    public void deleteDepartmentMember(DepartmentMember departmentMember){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
    	dao.deleteById(departmentMember.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, departmentMember.getId());
    }
    
    
    @Override
    public void deleteDepartmentMemberById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, id);
    }
     

    @Override
    public DepartmentMember findDepartmentMemberById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), DepartmentMember.class);
    }
    
    @Override
    public List<DepartmentMember> listDepartmentMembers(Long departmentId, Long memberUid,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<DepartmentMember> result  = new ArrayList<DepartmentMember>();
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
         	result.add(ConvertHelper.convert(r, DepartmentMember.class));
             return null;
         });
         return result;
    }
    
    @Override
    public List<DepartmentMember> listDepartmentMembers(Long memberUid) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<DepartmentMember> result  = new ArrayList<DepartmentMember>();
         SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
         if(memberUid != null && memberUid > 0) {
         	query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
         }
         query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, DepartmentMember.class));
             return null;
         });
         return result;
    }
    
    @Override
    public void createDepartmentCommunity(DepartmentCommunity departmentCommunity) {
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
    public void updateDepartmentCommunity(DepartmentCommunity departmentCommunity){
    	assert(departmentCommunity.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
    	dao.update(departmentCommunity);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, departmentCommunity.getId());
    }
    
    
    @Override
    public void deleteDepartmentCommunity(DepartmentCommunity departmentCommunity){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
    	dao.deleteById(departmentCommunity.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, departmentCommunity.getId());
    }
    
    
    @Override
    public void deleteDepartmentCommunityById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, id);
    }
     

    @Override
    public DepartmentCommunity findDepartmentCommunityById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), DepartmentCommunity.class);
    }
    
    @Override
    public List<DepartmentCommunity> listDepartmentCommunities(Long departmentId,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<DepartmentCommunity> result  = new ArrayList<DepartmentCommunity>();
         SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
         if(departmentId != null && departmentId > 0){
             query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(departmentId));
          }
         
         Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
         query.addLimit(offset, pageSize);
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, DepartmentCommunity.class));
             return null;
         });
         return result;
    }
    
    @Override
    public int countDepartments(String name) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATIONS);
        Condition condition = Tables.EH_ORGANIZATIONS.ID.greaterOrEqual(0L);
        if(!StringUtils.isEmpty(name))
        	condition = condition.and(Tables.EH_ORGANIZATIONS.NAME.eq(name));
        return step.where(condition).fetchOneInto(Integer.class);
    }
    
   @Override
    public int countDepartmentMembers(Long departmentId, Long memberUid) {
	   DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

       SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_MEMBERS);
       Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(departmentId);
       if(memberUid != null && memberUid > 0)
       		condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
       return step.where(condition).fetchOneInto(Integer.class);
    }
}

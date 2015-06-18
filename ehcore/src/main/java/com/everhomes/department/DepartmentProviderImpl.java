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
import com.everhomes.server.schema.tables.daos.EhDepartmentCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhDepartmentMembersDao;
import com.everhomes.server.schema.tables.daos.EhDepartmentsDao;
import com.everhomes.server.schema.tables.pojos.EhDepartmentCommunities;
import com.everhomes.server.schema.tables.pojos.EhDepartmentMembers;
import com.everhomes.server.schema.tables.pojos.EhDepartments;
import com.everhomes.server.schema.tables.records.EhDepartmentCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhDepartmentMembersRecord;
import com.everhomes.server.schema.tables.records.EhDepartmentsRecord;
import com.everhomes.util.ConvertHelper;
@Component
public class DepartmentProviderImpl implements DepartmentProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void createDepartment(Department department) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDepartmentsRecord record = ConvertHelper.convert(department, EhDepartmentsRecord.class);
        InsertQuery<EhDepartmentsRecord> query = context.insertQuery(Tables.EH_DEPARTMENTS);
        query.setRecord(record);
        query.setReturning(Tables.EH_DEPARTMENTS.ID);
        query.execute();
        
        department.setId(query.getReturnedRecord().getId());
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDepartments.class, null); 
        
    }

    
    @Override
    public void updateDepartment(Department department){
    	assert(department.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentsDao dao = new EhDepartmentsDao(context.configuration());
    	dao.update(department);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartments.class, department.getId());
    }
    
    
    @Override
    public void deleteDepartment(Department department){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentsDao dao = new EhDepartmentsDao(context.configuration());
    	dao.deleteById(department.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartments.class, department.getId());
    }
    
    
    @Override
    public void deleteDepartmentById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentsDao dao = new EhDepartmentsDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartments.class, id);
    }
     

    @Override
    public Department findDepartmentById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhDepartmentsDao dao = new EhDepartmentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Department.class);
    }
    
    @Override
    public List<Department> listDepartments(String name,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<Department> result  = new ArrayList<Department>();
         SelectQuery<EhDepartmentsRecord> query = context.selectQuery(Tables.EH_DEPARTMENTS);
         if(name != null && !"".equals(name)) {
         	query.addConditions(Tables.EH_DEPARTMENTS.NAME.eq(name));
         }
         
         Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_DEPARTMENTS.ID.desc());
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
        EhDepartmentMembersRecord record = ConvertHelper.convert(departmentMember, EhDepartmentMembersRecord.class);
        InsertQuery<EhDepartmentMembersRecord> query = context.insertQuery(Tables.EH_DEPARTMENT_MEMBERS);
        query.setRecord(record);
        query.setReturning(Tables.EH_DEPARTMENTS.ID);
        query.execute();
        
        departmentMember.setId(query.getReturnedRecord().getId());
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDepartmentMembers.class, null); 
        
    }

    
    @Override
    public void updateDepartmentMember(DepartmentMember departmentMember){
    	assert(departmentMember.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentMembersDao dao = new EhDepartmentMembersDao(context.configuration());
    	dao.update(departmentMember);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartmentMembers.class, departmentMember.getId());
    }
    
    
    @Override
    public void deleteDepartmentMember(DepartmentMember departmentMember){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentMembersDao dao = new EhDepartmentMembersDao(context.configuration());
    	dao.deleteById(departmentMember.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartmentMembers.class, departmentMember.getId());
    }
    
    
    @Override
    public void deleteDepartmentMemberById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentMembersDao dao = new EhDepartmentMembersDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartmentMembers.class, id);
    }
     

    @Override
    public DepartmentMember findDepartmentMemberById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhDepartmentMembersDao dao = new EhDepartmentMembersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), DepartmentMember.class);
    }
    
    @Override
    public List<DepartmentMember> listDepartmentMembers(Long departmentId, Long memberUid,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<DepartmentMember> result  = new ArrayList<DepartmentMember>();
         SelectQuery<EhDepartmentMembersRecord> query = context.selectQuery(Tables.EH_DEPARTMENT_MEMBERS);
         if(departmentId != null && departmentId > 0){
            query.addConditions(Tables.EH_DEPARTMENT_MEMBERS.DEPARTMENT_ID.eq(departmentId));
         }
         if(memberUid != null && memberUid > 0) {
         	query.addConditions(Tables.EH_DEPARTMENT_MEMBERS.MEMBER_UID.eq(memberUid));
         }
         
         Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_DEPARTMENT_MEMBERS.ID.desc());
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
         SelectQuery<EhDepartmentMembersRecord> query = context.selectQuery(Tables.EH_DEPARTMENT_MEMBERS);
         if(memberUid != null && memberUid > 0) {
         	query.addConditions(Tables.EH_DEPARTMENT_MEMBERS.MEMBER_UID.eq(memberUid));
         }
         query.addOrderBy(Tables.EH_DEPARTMENT_MEMBERS.ID.desc());
         query.fetch().map((r) -> {
         	result.add(ConvertHelper.convert(r, DepartmentMember.class));
             return null;
         });
         return result;
    }
    
    @Override
    public void createDepartmentCommunity(DepartmentCommunity departmentCommunity) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDepartmentCommunitiesRecord record = ConvertHelper.convert(departmentCommunity, EhDepartmentCommunitiesRecord.class);
        InsertQuery<EhDepartmentCommunitiesRecord> query = context.insertQuery(Tables.EH_DEPARTMENT_COMMUNITIES);
        query.setRecord(record);
        query.setReturning(Tables.EH_DEPARTMENT_COMMUNITIES.ID);
        query.execute();
        
        departmentCommunity.setId(query.getReturnedRecord().getId());
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDepartmentCommunities.class, null); 
        
    }

    
    @Override
    public void updateDepartmentCommunity(DepartmentCommunity departmentCommunity){
    	assert(departmentCommunity.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentCommunitiesDao dao = new EhDepartmentCommunitiesDao(context.configuration());
    	dao.update(departmentCommunity);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartmentCommunities.class, departmentCommunity.getId());
    }
    
    
    @Override
    public void deleteDepartmentCommunity(DepartmentCommunity departmentCommunity){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentCommunitiesDao dao = new EhDepartmentCommunitiesDao(context.configuration());
    	dao.deleteById(departmentCommunity.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartmentCommunities.class, departmentCommunity.getId());
    }
    
    
    @Override
    public void deleteDepartmentCommunityById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhDepartmentCommunitiesDao dao = new EhDepartmentCommunitiesDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDepartmentCommunities.class, id);
    }
     

    @Override
    public DepartmentCommunity findDepartmentCommunityById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhDepartmentCommunitiesDao dao = new EhDepartmentCommunitiesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), DepartmentCommunity.class);
    }
    
    @Override
    public List<DepartmentCommunity> listDepartmentCommunities(Long departmentId,Integer pageOffset,Integer pageSize) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

         List<DepartmentCommunity> result  = new ArrayList<DepartmentCommunity>();
         SelectQuery<EhDepartmentCommunitiesRecord> query = context.selectQuery(Tables.EH_DEPARTMENT_COMMUNITIES);
         if(departmentId != null && departmentId > 0){
             query.addConditions(Tables.EH_DEPARTMENT_COMMUNITIES.DEPARTMENT_ID.eq(departmentId));
          }
         
         Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
         query.addOrderBy(Tables.EH_DEPARTMENT_COMMUNITIES.ID.desc());
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

        SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_DEPARTMENTS);
        Condition condition = Tables.EH_DEPARTMENTS.ID.greaterOrEqual(0L);
        if(!StringUtils.isEmpty(name))
        	condition = condition.and(Tables.EH_DEPARTMENTS.NAME.eq(name));
        return step.where(condition).fetchOneInto(Integer.class);
    }
    
   @Override
    public int countDepartmentMembers(Long departmentId, Long memberUid) {
	   DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

       SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_DEPARTMENT_MEMBERS);
       Condition condition = Tables.EH_DEPARTMENT_MEMBERS.DEPARTMENT_ID.eq(departmentId);
       if(memberUid != null && memberUid > 0)
       		condition = condition.and(Tables.EH_DEPARTMENT_MEMBERS.MEMBER_UID.eq(memberUid));
       return step.where(condition).fetchOneInto(Integer.class);
    }
}

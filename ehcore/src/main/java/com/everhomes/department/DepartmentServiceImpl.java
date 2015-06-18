// @formatter:off
package com.everhomes.department;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.family.FamilyDTO;
import com.everhomes.pm.PmMemberStatus;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class DepartmentServiceImpl implements DepartmentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);
   
    
    @Autowired
    private DepartmentProvider departmentProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    
    private int getPageCount(int totalCount, int pageSize){
        int pageCount = totalCount/pageSize;
        
        if(totalCount % pageSize != 0){
            pageCount ++;
        }
        return pageCount;
    }
    
    @Override
    public void createDepartment(CreateDepartmentCommand cmd) {
    	User user  = UserContext.current().getUser();
    	
    	//权限控制
    	//先判断，后台管理员才能创建。状态直接设为正常
    	Department department  = ConvertHelper.convert(cmd, Department.class);
    	if(cmd.getLevel() == null){
    		department.setLevel(0);
    	}
    	department.setStatus(DepartmentStatus.ACTIVE.getCode());
    	departmentProvider.createDepartment(department);
    	
    	
    }
    
    @Override
    public void createDepartmentMember(CreateDepartmentMemberCommand cmd) {
    	User user  = UserContext.current().getUser();
    	
    	//权限控制
    	//先判断，后台管理员才能创建。状态直接设为正常
    	Long addUserId =  cmd.getMemberUid();
    	//添加已注册用户为管理员。
    	if(addUserId != null && addUserId != 0){
	    	List<DepartmentMember> list = departmentProvider.listDepartmentMembers(cmd.getDepartmentId(), addUserId, 1, 20);
	    	if(list == null || list.size() == 0)
	    	{
	    		DepartmentMember departmentMember = ConvertHelper.convert(cmd, DepartmentMember.class);
	    		departmentMember.setStatus(PmMemberStatus.ACTIVE.getCode());
	    		departmentProvider.createDepartmentMember(departmentMember);
	    	}
    	}
    	
    }
    
    @Override
    public void createDepartmentCommunity(CreateDepartmentCommunityCommand cmd) {
    	User user  = UserContext.current().getUser();
    	
    	//权限控制
    	//先判断，后台管理员才能创建。状态直接设为正常
    	List<Long> communityIds = cmd.getCommunityIds();
    	if(communityIds != null && communityIds.size() > 0){
    		for (Long id : communityIds) {
				Community community = communityProvider.findCommunityById(id);
				if(community == null) {
					LOGGER.error("Unable to find the community.communityId=" + id);
		    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
		                     "Unable to find the community.");
				}
				DepartmentCommunity departmentCommunity = new DepartmentCommunity();
				departmentCommunity.setCommunityId(id);
				departmentCommunity.setDepartmentId(cmd.getDepartmentId());
				departmentProvider.createDepartmentCommunity(departmentCommunity);
			}
    	}
    	
    }
    
    @Override
    public ListDepartmentsCommandResponse listDepartments(ListDepartmentsCommand cmd) {
    	User user  = UserContext.current().getUser();
    	//权限控制
    	ListDepartmentsCommandResponse response = new ListDepartmentsCommandResponse();
    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
    	int totalCount = departmentProvider.countDepartments(cmd.getParentId(),cmd.getName());
    	if(totalCount == 0) return response;
    	int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
    	int pageCount = getPageCount(totalCount, pageSize);
    	List<Department> result = departmentProvider.listDepartments(cmd.getParentId(), cmd.getName(), cmd.getPageOffset(), pageSize);
    	response.setMembers( result.stream()
                .map(r->{ return ConvertHelper.convert(r,DepartmentDTO.class); })
                .collect(Collectors.toList()));
    	response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
    	return response;
    }
    
}

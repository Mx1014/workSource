// @formatter:off
package com.everhomes.department;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.pm.PmMemberStatus;
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
    
    @Override
    public void createDepartment(CreateDepartmentCommand cmd) {
    	User user  = UserContext.current().getUser();
    	
    	//权限控制
    	//先判断，后台管理员才能创建。状态直接设为正常
    	Department department  = ConvertHelper.convert(cmd, Department.class);
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
    
    
}

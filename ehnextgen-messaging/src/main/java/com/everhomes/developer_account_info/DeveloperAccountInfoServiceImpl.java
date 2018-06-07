// @formatter:off
package com.everhomes.developer_account_info;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.user.UserContext;
import com.everhomes.util.DateUtils;

@Component
public class DeveloperAccountInfoServiceImpl implements DeveloperAccountInfoService {

	@Autowired
    DeveloperAccountInfoProvider developerAccountInfoProvider;
	
	@Override
	public void createDeveloperAccountInfo(DeveloperAccountInfo bo) {
		//查看是否已存在
		DeveloperAccountInfo resultBo = developerAccountInfoProvider.getDeveloperAccountInfoByBundleId(bo.getBundleIds());
		//存在则先删除再新增
		if(resultBo != null )
		{
			developerAccountInfoProvider.deleteDeveloperAccountInfo(resultBo);
		}
		Timestamp operatorTime = DateUtils.currentTimestamp();
		bo.setCreateTime(operatorTime);
		Long uid = UserContext.currentUserId();
		bo.setCreateName("userId :"+uid);
		developerAccountInfoProvider.createDeveloperAccountInfo(bo);
	}}

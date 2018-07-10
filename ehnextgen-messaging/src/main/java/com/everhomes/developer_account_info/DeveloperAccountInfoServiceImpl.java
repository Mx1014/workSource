// @formatter:off
package com.everhomes.developer_account_info;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.messaging.PusherService;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateUtils;

@Component
public class DeveloperAccountInfoServiceImpl implements DeveloperAccountInfoService {

	@Autowired
   private DeveloperAccountInfoProvider developerAccountInfoProvider;
	
	@Autowired
	private PusherService pusherService;
	
	@Override
	public void createDeveloperAccountInfo(DeveloperAccountInfo bo) {
		//测试异常用
		/*if("com.techpark.ios.zuolin03".equals(bo.getBundleIds())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"I mean it !");
		}*/
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
		//如果原来有，可能已经建了连接，那么就要停掉
		if(resultBo != null){
			pusherService.stophttp2Client(bo.getBundleIds());
		}
		
	}}

package com.everhomes.pushmessagelog;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.configurations.ConfigurationsServiceImpl;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.pushmessagelog.PushMessageListCommand;
import com.everhomes.rest.pushmessagelog.PushMessageLogDTO;
import com.everhomes.rest.pushmessagelog.PushMessageLogReturnDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class PushMessageLogServiceImpl implements PushMessageLogService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationsServiceImpl.class);
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private PushMessageLogProvider pushMessageLogProvider;
	
	@Autowired
	private UserProvider userProvider;

	@Override
	public PushMessageLogReturnDTO listPushMessageLogByNamespaceIdAndOperator(PushMessageListCommand cmd) {
		//如果传参对象为空，抛出异常
				if(cmd == null ) {
					String msg = "cmd  cannot be null.";
					throwSelfDefNullException(msg);
				}
				//若前台无每页数量传来则取默认配置的
				int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
				//创建锚点分页所需的对象
				CrossShardListingLocator locator = new CrossShardListingLocator();
				locator.setAnchor(cmd.getPageAnchor());
				//主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
				Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
				//Provider 层传进行查询并返回对象
				List<PushMessageLog> boList = pushMessageLogProvider
						    .listPushMessageLogByNamespaceIdAndOperator(namespaceId, cmd.getOperatorId(), pageSize, locator);
				 //对象转换
				PushMessageLogReturnDTO returnDto = new PushMessageLogReturnDTO ();

				returnDto.setDtos(boList.stream().map(r->{
					//copy 相同属性下的值
					PushMessageLogDTO dto = ConvertHelper.convert(r, PushMessageLogDTO.class);
					//转换出操作人
					if(r.getOperatorId() != null){
						String name = userProvider.getNickNameByUid(r.getOperatorId()==null?null:r.getOperatorId().longValue());
						String mobile = userProvider.findMobileByUid(r.getOperatorId()==null?null:r.getOperatorId().longValue()) ;
						if(StringUtils.isNotBlank(name)&& StringUtils.isNotBlank(mobile) ){
							dto.setOperator(name + "（"+mobile+"）");
						}else if(StringUtils.isNotBlank(name)){
							dto.setOperator(name );
						}else if(StringUtils.isNotBlank(mobile)){
							dto.setOperator(mobile );
						}else{
							dto.setOperator("Not Found");
						}
					}
					//转换推送对象
					if(StringUtils.isNotBlank(r.getReceivers())){
						String[] receiver = r.getReceivers().split(",");
						//asList方法在参数为空的情况下会报指针异常
						if(receiver != null){
							dto.setReceivers(Arrays.asList(receiver));
						}					
					}
						return dto;
					}).collect(Collectors.toList()));
				
				//设置下一页开始锚点
				returnDto.setNextPageAnchor(locator.getAnchor());
				return returnDto;
	}

	@Override
	public PushMessageLog getPushMessageLogById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void crteatePushMessageLog(PushMessageLog bo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePushMessageLog(PushMessageLog bo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletePushMessageLog(PushMessageLog bo) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 抛出对象或其ID属性为空的异常
	 * @param msg 报错信息
	 */
	private void throwSelfDefNullException(String msg ){
		LOGGER.error(msg);
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,msg);
	}

}

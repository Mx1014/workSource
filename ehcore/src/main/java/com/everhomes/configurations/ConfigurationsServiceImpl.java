package com.everhomes.configurations;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.configurations.admin.ConfigurationsAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsAdminDTO;
import com.everhomes.rest.configurations.admin.ConfigurationsCreateAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminDTO;
import com.everhomes.rest.configurations.admin.ConfigurationsUpdateAdminCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

/**
 * configurations management Service
 * @author huanglm
 *
 */
@Component
public class ConfigurationsServiceImpl implements  ConfigurationsService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationsServiceImpl.class);
	
	@Autowired
	private ConfigurationsProvider	configurationsProvider ;
	
	@Autowired
	private ConfigurationProvider configProvider;

	@Override
	public ConfigurationsAdminDTO listConfigurations(ConfigurationsAdminCommand cmd) {
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
		List<Configurations> boList = configurationsProvider.listConfigurations(namespaceId, 
														cmd.getName(), cmd.getValue(), pageSize, locator,true);
		 //对象转换
		ConfigurationsAdminDTO returnDto = new ConfigurationsAdminDTO ();

		returnDto.setDtos(boList.stream().map(r->{
			//copy 相同属性下的值
			ConfigurationsIdAdminDTO dto = ConvertHelper.convert(r, ConfigurationsIdAdminDTO.class);				
				return dto;
			}).collect(Collectors.toList()));
		
		//设置下一页开始锚点
		returnDto.setNextPageAnchor(locator.getAnchor());
		return returnDto;
	}

	@Override
	public ConfigurationsIdAdminDTO getConfigurationById(
			ConfigurationsIdAdminCommand cmd) {
		
		//如果传参对象为空或ID必填项为空，抛出异常
		if(cmd == null || cmd.getId() == null) {
				String msg = "cmd or cmd.id cannot be null.";
				throwSelfDefNullException(msg);
		}
		//Provider 层传进行查询并返回对象
		Configurations bo = configurationsProvider.getConfigurationById(cmd.getId(), cmd.getNamespaceId());		
		//对象转换
		ConfigurationsIdAdminDTO dto = ConvertHelper.convert(bo, ConfigurationsIdAdminDTO.class);
		
		return dto;
	}

	@Override
	public void crteateConfiguration(ConfigurationsCreateAdminCommand cmd) {
		
		//如果传参对象为空或name 为空或空字符串 ，抛出异常
				if(cmd == null || StringUtils.isBlank(cmd.getName())) {
						String msg = "cmd or cmd.name cannot be null.";
						throwSelfDefNullException(msg);
				}
				//主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
				Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
				cmd.setNamespaceId(namespaceId);
				//对象转换
				Configurations bo = ConvertHelper.convert(cmd , Configurations.class);
				//调Provider 层进行创建
				configurationsProvider.crteateConfiguration(bo);
				
	}

	@Override
	public void updateConfiguration(ConfigurationsUpdateAdminCommand cmd) {
		
		//如果传参对象为空或name 为空或空字符串 或 id 为空 ，抛出异常
		if(cmd == null || StringUtils.isBlank(cmd.getName()) || 
		   cmd.getId() == null) {
				String msg = "cmd or cmd.name or cmd.id cannot be null.";
				throwSelfDefNullException(msg);
		}
		//主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		cmd.setNamespaceId(namespaceId);
		//对象转换
		Configurations bo = ConvertHelper.convert(cmd , Configurations.class);
		//调Provider 层进行更新
		configurationsProvider.updateConfiguration(bo);
		
	}

	@Override
	public void deleteConfiguration(ConfigurationsIdAdminCommand cmd) {
		
		//如果传参对象为空或ID必填项为空，抛出异常
		if(cmd == null || cmd.getId() == null) {
				String msg = "cmd or cmd.id cannot be null.";
				throwSelfDefNullException(msg);
		}
		Configurations bo = configurationsProvider.getConfigurationById(cmd.getId(), null);
		//调Provider 层进行
		configurationsProvider.deleteConfiguration(bo);		
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

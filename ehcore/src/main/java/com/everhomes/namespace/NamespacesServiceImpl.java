package com.everhomes.namespace;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.joda.time.tz.NameProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.configuration.ConfigurationsService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.configuration.admin.CreateConfigurationCommand;
import com.everhomes.rest.namespace.ConfigNamespaceCommand;
import com.everhomes.rest.namespace.CreateNamespaceCommand;
import com.everhomes.rest.namespace.CreateNamespaceResponse;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.version.CreateVersionRealmCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.version.VersionService;

import freemarker.core.ReturnInstruction.Return;

@Service
public class NamespacesServiceImpl implements NamespacesService {

	@Autowired
	private NamespacesProvider namespacesProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private VersionService versionService;
	
	@Autowired
	private ConfigurationsService configurationsService;
	
	@Override
	public CreateNamespaceResponse createNamespace(CreateNamespaceCommand cmd) {
		if(StringUtils.isEmpty(cmd.getCommunityType()) || StringUtils.isEmpty(cmd.getName())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		
		NamespaceCommunityType communityType = NamespaceCommunityType.fromCode(cmd.getCommunityType());
		if (communityType == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"community type error: "+cmd.getCommunityType());
		}
		
		final CreateNamespaceResponse response = new CreateNamespaceResponse();
		dbProvider.execute(s->{
			Namespace namespace = new Namespace();
			namespace.setName(cmd.getName());
			namespacesProvider.createNamespace(namespace);
			
			NamespaceDetail namespaceDetail = new NamespaceDetail();
			namespaceDetail.setNamespaceId(namespace.getId());
			namespaceDetail.setResourceType(cmd.getCommunityType());
			namespacesProvider.createNamespaceDetail(namespaceDetail);
			
			response.setId(namespace.getId());
			response.setName(cmd.getName());
			response.setCommunityType(cmd.getCommunityType());
			
			return true;
		});
		
		return response;
	}

	@Override
	public void configNamespace(ConfigNamespaceCommand cmd) {
		//创建域空间
		CreateNamespaceCommand createNamespaceCommand = ConvertHelper.convert(cmd, CreateNamespaceCommand.class);
		CreateNamespaceResponse createNamespaceResponse = createNamespace(createNamespaceCommand);
		Integer namespaceId = createNamespaceResponse.getId();
		
		//创建android version
		if (!StringUtils.isEmpty(cmd.getAndroidRealm())) {
			CreateVersionRealmCommand createVersionRealmCommand = ConvertHelper.convert(cmd, CreateVersionRealmCommand.class);
			createVersionRealmCommand.setNamespaceId(namespaceId);
			createVersionRealmCommand.setRealm(cmd.getAndroidRealm());
			versionService.createVersionRealm(createVersionRealmCommand);
		}
		
		//创建ios version
		if(!StringUtils.isEmpty(cmd.getIosRealm())){
			CreateVersionRealmCommand createVersionRealmCommand = ConvertHelper.convert(cmd, CreateVersionRealmCommand.class);
			createVersionRealmCommand.setNamespaceId(namespaceId);
			createVersionRealmCommand.setRealm(cmd.getIosRealm());
			versionService.createVersionRealm(createVersionRealmCommand);
		}
		
		//创建app.agreements.url
		if(!StringUtils.isEmpty(cmd.getAppAgreementsUrl())){
			CreateConfigurationCommand createConfigurationCommand = new CreateConfigurationCommand();
			createConfigurationCommand.setNamespaceId(namespaceId);
			createConfigurationCommand.setName("app.agreements.url");
			createConfigurationCommand.setValue(cmd.getAppAgreementsUrl());
			configurationsService.createConfiguration(createConfigurationCommand);
		}
		
		//创建home.url
		if(!StringUtils.isEmpty(cmd.getHomeUrl())){
			CreateConfigurationCommand createConfigurationCommand = new CreateConfigurationCommand();
			createConfigurationCommand.setNamespaceId(namespaceId);
			createConfigurationCommand.setName("home.url");
			createConfigurationCommand.setValue(cmd.getHomeUrl());
			configurationsService.createConfiguration(createConfigurationCommand);
		}
	}
}

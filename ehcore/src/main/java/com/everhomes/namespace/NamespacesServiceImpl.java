package com.everhomes.namespace;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.namespace.admin.ListNamespaceByModuleIdCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.namespace.admin.CreateNamespaceCommand;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.namespace.admin.UpdateNamespaceCommand;
import com.everhomes.util.RuntimeErrorException;
import com.mysql.jdbc.StringUtils;

@Component
public class NamespacesServiceImpl implements NamespacesService {

	@Autowired
	private NamespacesProvider namespacesProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Override
	public List<NamespaceInfoDTO> listNamespace() {
		return namespacesProvider.listNamespace();
	}

	@Override
	public NamespaceInfoDTO createNamespace(final CreateNamespaceCommand cmd) {
		if (StringUtils.isEmptyOrWhitespaceOnly(cmd.getName()) || StringUtils.isEmptyOrWhitespaceOnly(cmd.getResourceType()) 
				|| NamespaceCommunityType.fromCode(cmd.getResourceType())==null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		
		final NamespaceInfoDTO namespaceInfoDTO = new NamespaceInfoDTO();
		dbProvider.execute(s->{
			Namespace namespace = new Namespace();
			namespace.setName(cmd.getName());
			namespacesProvider.createNamespace(namespace);
			
			NamespaceDetail namespaceDetail = new NamespaceDetail();
			namespaceDetail.setNamespaceId(namespace.getId());
			namespaceDetail.setResourceType(cmd.getResourceType());
			namespacesProvider.createNamespaceDetail(namespaceDetail);
			
			namespaceInfoDTO.setId(namespace.getId());
			namespaceInfoDTO.setName(cmd.getName());
			namespaceInfoDTO.setResourceType(cmd.getResourceType());
			return true;
		});
		return namespaceInfoDTO;
	}

	@Override
	public NamespaceInfoDTO updateNamespace(final UpdateNamespaceCommand cmd) {
		if (StringUtils.isEmptyOrWhitespaceOnly(cmd.getName()) || StringUtils.isEmptyOrWhitespaceOnly(cmd.getResourceType()) 
				|| NamespaceCommunityType.fromCode(cmd.getResourceType())==null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		if (namespacesProvider.findNamespaceByid(cmd.getId()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist namespace: "+cmd);
		}
		
		final NamespaceInfoDTO namespaceInfoDTO = new NamespaceInfoDTO();
		dbProvider.execute(s->{
			Namespace namespace = new Namespace();
			namespace.setId(cmd.getId());
			namespace.setName(cmd.getName());
			namespacesProvider.updateNamespace(namespace);
			
			NamespaceDetail namespaceDetail = namespacesProvider.findNamespaceDetailByNamespaceId(namespace.getId());
			if (namespaceDetail != null) {
				namespaceDetail.setResourceType(cmd.getResourceType());
				namespacesProvider.updateNamespaceDetail(namespaceDetail);
			}else {
				namespaceDetail = new NamespaceDetail();
				namespaceDetail.setNamespaceId(namespace.getId());
				namespaceDetail.setResourceType(cmd.getResourceType());
				namespacesProvider.createNamespaceDetail(namespaceDetail);
			}
			
			namespaceInfoDTO.setId(namespace.getId());
			namespaceInfoDTO.setName(cmd.getName());
			namespaceInfoDTO.setResourceType(cmd.getResourceType());
			return true;
		});
		return namespaceInfoDTO;
	}

	@Override
	public List<NamespaceInfoDTO> listNamespaceByModuleId(ListNamespaceByModuleIdCommand cmd) {

		List<NamespaceInfoDTO> dtos;
		if(cmd.getModuleId() != null && cmd.getModuleId().longValue() != 0){
			dtos = namespacesProvider.listNamespaceByModuleId(cmd.getModuleId());
		}else {
			dtos = namespacesProvider.listNamespace();
		}

		return dtos;
	}

	@Override
	public Boolean isWechatNamespace(Integer namespaceId) {


		//for test
		if(namespaceId == 999964 || namespaceId == 999957){
			return true;
		}

		return false;
	}

	@Override
	public Boolean isStdNamespace(Integer namespaceId) {


		//for test
		if(namespaceId == 2){
			return true;
		}

		return false;
	}
}

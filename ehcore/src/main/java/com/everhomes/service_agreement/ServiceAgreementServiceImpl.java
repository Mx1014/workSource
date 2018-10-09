package com.everhomes.service_agreement;

import com.everhomes.rest.service_agreement.admin.GetProtocolDetailResponse;
import com.everhomes.rest.service_agreement.admin.GetProtocolTemplateCommand;
import com.everhomes.rest.service_agreement.admin.GetProtocolCommand;
import com.everhomes.rest.service_agreement.admin.GetProtocolResponse;
import com.everhomes.rest.service_agreement.admin.GetProtocolTemplateResponse;
import com.everhomes.rest.service_agreement.admin.ProtocolTemplateStatus;
import com.everhomes.rest.service_agreement.admin.ProtocolTemplateVariableDTO;
import com.everhomes.rest.service_agreement.admin.ProtocolVariableDTO;
import com.everhomes.rest.service_agreement.admin.SaveProtocolsCommand;
import com.everhomes.rest.service_agreement.admin.SaveProtocolsTemplateCommand;
import com.everhomes.util.DateHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.service_agreement.ServiceAgreementCommand;
import com.everhomes.rest.service_agreement.ServiceAgreementDTO;
import com.everhomes.rest.service_agreement.admin.ServiceAgreementSaveCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务协议 serviceImpl
 * @author huanglm
 *
 */
@Component
public class ServiceAgreementServiceImpl implements ServiceAgreementService {

	@Autowired
    private ServiceAgreementProvider serviceAgreementprovider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Override
	public ServiceAgreementDTO getServiceAgreementByNamespaceId(ServiceAgreementCommand cmd) {
		Integer namespaceId = null ;
		if(cmd != null && cmd.getNamespaceId() != null ){
			namespaceId = cmd.getNamespaceId() ;
		}
		//主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
		 namespaceId = UserContext.getCurrentNamespaceId(namespaceId);
		return serviceAgreementprovider.getByNamespaceIdAndDef2Null(namespaceId);
	}

	@Override
	public void saveServiceAgreement(ServiceAgreementSaveCommand cmd) {
		if(cmd == null){
			return ;
		}
		Integer namespaceId = cmd.getNamespaceId() ;
		//主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
		 namespaceId = UserContext.getCurrentNamespaceId(namespaceId);
		 //不管有没有改都重新设置一次，有杀错没放过
		 cmd.setNamespaceId(namespaceId);
		 //查询该域空间的服务协议信息是否存，存在则覆盖更新，不存在则新建
		 ServiceAgreementDTO dto = serviceAgreementprovider.getServiceAgreementByNamespaceId(namespaceId);
		 if(dto != null){
			 dto.setAgreementContent(cmd.getAgreementContent());
			 ConvertHelper.convert(dto, ServiceAgreement.class);
			 serviceAgreementprovider.updateServiceAgreement(ConvertHelper.convert(dto, ServiceAgreement.class));
		 }else{
			 serviceAgreementprovider.crteateServiceAgreement(ConvertHelper.convert(cmd, ServiceAgreement.class));
		 }

	}

	@Override
	public void saveProtocolTemplate(SaveProtocolsTemplateCommand cmd) {
		ProtocolTemplates protocolTemplates = this.serviceAgreementprovider.getActiveProtocolTemplate(cmd.getType());

		if (protocolTemplates != null) {
		    protocolTemplates.setStatus(ProtocolTemplateStatus.INVALID.getCode());
		    this.serviceAgreementprovider.updateProtocolTemplate(protocolTemplates);
        }

        ProtocolTemplates newTemplate = new ProtocolTemplates();
		newTemplate.setStatus(ProtocolTemplateStatus.RUNNING.getCode());
		newTemplate.setType(cmd.getType());
		newTemplate.setContent(cmd.getContent());
		newTemplate.setCreatorUid(UserContext.currentUserId());
		newTemplate.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		Long ownerId = this.serviceAgreementprovider.createProtocolTemplate(newTemplate);

		if (!CollectionUtils.isEmpty(cmd.getVariables())) {
		    for (ProtocolTemplateVariableDTO dto : cmd.getVariables()) {
		        ProtocolTemplateVariables variable = new ProtocolTemplateVariables();
		        variable.setOwnerId(ownerId);
		        variable.setName(dto.getName());
		        variable.setType(dto.getType());
		        this.serviceAgreementprovider.createProtocolTemplateVariable(variable);
            }
        }
	}

    @Override
    public GetProtocolTemplateResponse getProtocolTemplate(GetProtocolTemplateCommand cmd) {
	    GetProtocolTemplateResponse response = new GetProtocolTemplateResponse();
        ProtocolTemplates protocolTemplates = this.serviceAgreementprovider.getActiveProtocolTemplate(cmd.getType());
        if (protocolTemplates != null) {
            List<ProtocolTemplateVariables> protocolTemplateVariablesList = this.serviceAgreementprovider.getProtocolTemplateVariables(protocolTemplates.getId());
            List<ProtocolTemplateVariableDTO> variables = new ArrayList<>();
            if (!CollectionUtils.isEmpty(protocolTemplateVariablesList)) {
                for (ProtocolTemplateVariables protocolTemplateVariables : protocolTemplateVariablesList) {
                    variables.add(ConvertHelper.convert(protocolTemplateVariables, ProtocolTemplateVariableDTO.class));
                }
            }
            response.setType(protocolTemplates.getType());
            response.setContent(protocolTemplates.getContent());
            response.setVariables(variables);
        }
        return response;
    }

    @Override
    public GetProtocolResponse getProtocolTemplateDetail(GetProtocolCommand cmd) {
        GetProtocolResponse response = new GetProtocolResponse();

        Protocols protocols = this.serviceAgreementprovider.getProtocols(cmd.getNamespaceId(), cmd.getType());
        List<ProtocolVariableDTO> variables = new ArrayList<>();

        //如果协议存在，则使用该域空间的协议，若不存在，则使用模板
        if (protocols != null) {
	        List<ProtocolVariables> protocolVariablesList = this.serviceAgreementprovider.getProtocolVariables(protocols.getId(), cmd.getNamespaceId());
	        if (!CollectionUtils.isEmpty(protocolVariablesList)) {
	            for (ProtocolVariables protocolVariables : protocolVariablesList) {
	                variables.add(ConvertHelper.convert(protocolVariables, ProtocolVariableDTO.class));
                }
            }
            response.setType(protocols.getType());
            response.setContent(protocols.getContent());
        }else {
            ProtocolTemplates protocolTemplates = this.serviceAgreementprovider.getActiveProtocolTemplate(cmd.getType());
            if (protocolTemplates != null) {
                List<ProtocolTemplateVariables> protocolTemplateVariablesList = this.serviceAgreementprovider.getProtocolTemplateVariables(protocolTemplates.getId());
                if (!CollectionUtils.isEmpty(protocolTemplateVariablesList)) {
                    for (ProtocolTemplateVariables protocolTemplateVariables : protocolTemplateVariablesList) {
                        variables.add(ConvertHelper.convert(protocolTemplateVariables, ProtocolVariableDTO.class));
                    }
                }
                response.setType(protocolTemplates.getType());
                response.setContent(protocolTemplates.getContent());
            }
        }
        response.setVariables(variables);
        return response;
    }

    @Override
    public void saveProtocol(SaveProtocolsCommand cmd) {
        Protocols oldProtocols = this.serviceAgreementprovider.getProtocols(cmd.getNamespaceId(), cmd.getType());
        if (oldProtocols != null) {
            oldProtocols.setStatus(ProtocolTemplateStatus.INVALID.getCode());
            this.serviceAgreementprovider.updateProtocol(oldProtocols);
        }

        Protocols newProtocols = new Protocols();
        newProtocols.setStatus(ProtocolTemplateStatus.RUNNING.getCode());
        newProtocols.setContent(cmd.getContent());
        newProtocols.setNamespaceId(cmd.getNamespaceId());
        newProtocols.setType(cmd.getType());
        newProtocols.setCreatorUid(UserContext.currentUserId());
        newProtocols.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        Long id = this.serviceAgreementprovider.createProtocol(newProtocols);


        if (!CollectionUtils.isEmpty(cmd.getVariables())) {
            for (ProtocolVariableDTO protocolVariableDTO : cmd.getVariables()) {
                ProtocolVariables protocolVariables = new ProtocolVariables();
                protocolVariables.setName(protocolVariableDTO.getName());
                protocolVariables.setNamespaceId(cmd.getNamespaceId());
                protocolVariables.setValue(protocolVariableDTO.getValue());
                protocolVariables.setType(protocolVariableDTO.getType());
                protocolVariables.setOwnerId(id);
                this.serviceAgreementprovider.createProtocolVariables(protocolVariables);
            }
        }
    }

    @Override
    public GetProtocolDetailResponse getProtocolDetail(GetProtocolCommand cmd) {
	    GetProtocolDetailResponse response = new GetProtocolDetailResponse();
        Protocols protocols = this.serviceAgreementprovider.getProtocols(cmd.getNamespaceId(), cmd.getType());
        //如果协议存在，则使用该域空间的协议，若不存在，则使用模板
        if (protocols != null) {
            List<ProtocolVariables> protocolVariablesList = this.serviceAgreementprovider.getProtocolVariables(protocols.getId(), cmd.getNamespaceId());
            if (!CollectionUtils.isEmpty(protocolVariablesList)) {
                String protocolText = protocols.getContent();
                for (ProtocolVariables protocolVariables : protocolVariablesList) {
                    protocolText = protocolText.replace("$"+protocolVariables.getName()+"$", protocolVariables.getValue());
                }
                protocols.setContent(protocolText);
            }
            response.setType(protocols.getType());
            response.setContent(protocols.getContent());
        }else {
            ProtocolTemplates protocolTemplates = this.serviceAgreementprovider.getActiveProtocolTemplate(cmd.getType());
            List<ProtocolTemplateVariables> protocolTemplateVariablesList = this.serviceAgreementprovider.getProtocolTemplateVariables(protocolTemplates.getId());
            if (!CollectionUtils.isEmpty(protocolTemplateVariablesList)) {
                String protocolText = protocols.getContent();
                for (ProtocolTemplateVariables protocolTemplateVariables : protocolTemplateVariablesList) {
                    protocolText = protocolText.replace("$"+protocolTemplateVariables.getName()+"$", "");
                }
                protocolTemplates.setContent(protocolText);
            }
            response.setType(protocolTemplates.getType());
            response.setContent(protocolTemplates.getContent());
        }
        return response;
    }

}

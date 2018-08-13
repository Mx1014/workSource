package com.everhomes.service_agreement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.service_agreement.ServiceAgreementCommand;
import com.everhomes.rest.service_agreement.ServiceAgreementDTO;
import com.everhomes.rest.service_agreement.admin.ServiceAgreementSaveCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

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

}

package com.everhomes.service_agreement;

import org.springframework.stereotype.Component;

import com.everhomes.rest.service_agreement.ServiceAgreementDTO;

/**
 * 服务协议 ProviderImpl
 * @author huanglm
 *
 */
@Component
public class ServiceAgreementProviderImpl implements ServiceAgreementProvider {

	@Override
	public ServiceAgreementDTO getServiceAgreementByNamespaceId(Integer namespaceId) {
		
		return null;
	}

	@Override
	public void crteateServiceAgreement(ServiceAgreement bo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateServiceAgreement(ServiceAgreement bo) {
		// TODO Auto-generated method stub

	}

}

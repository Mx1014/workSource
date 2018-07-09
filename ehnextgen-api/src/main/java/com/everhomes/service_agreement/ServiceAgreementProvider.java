package com.everhomes.service_agreement;

import com.everhomes.rest.service_agreement.ServiceAgreementDTO;



/**
 * 服务协议配置 Provider
 * @author huanglm 20180709
 *
 */
public interface ServiceAgreementProvider {
	
	/**
	 * 通过域空间查询服务协议
	 * @param cmd
	 * @return ServiceAgreementDTO
	 */ 
	ServiceAgreementDTO getServiceAgreementByNamespaceId(Integer namespaceId);
	

	/**
	 * 创建配置项信息
	 * @param bo	Configurations
	 */
	void crteateServiceAgreement(ServiceAgreement bo);
	
	/**
	 * 修改配置项信息，主键不能为空
	 * @param bo	Configurations
	 */
	void updateServiceAgreement(ServiceAgreement bo);
}

package com.everhomes.service_agreement;

import com.everhomes.rest.service_agreement.ServiceAgreementCommand;
import com.everhomes.rest.service_agreement.ServiceAgreementDTO;
import com.everhomes.rest.service_agreement.admin.ServiceAgreementSaveCommand;

/**
 * 服务协议配置 service
 * @author huanglm 20180709
 *
 */
public interface ServiceAgreementService {
	

	
	/**
	 * 通过域空间查询服务协议
	 * @param cmd
	 * @return ServiceAgreementDTO
	 */ 
	ServiceAgreementDTO getServiceAgreementByNamespaceId(ServiceAgreementCommand cmd);
	
	/**
	 * 创建配置项信息
	 * @param cmd
	 */
	void saveServiceAgreement(ServiceAgreementSaveCommand cmd);
	

	

}

package com.everhomes.service_agreement;

import com.everhomes.rest.service_agreement.ServiceAgreementCommand;
import com.everhomes.rest.service_agreement.ServiceAgreementDTO;
import com.everhomes.rest.service_agreement.admin.GetProtocolDetailResponse;
import com.everhomes.rest.service_agreement.admin.GetProtocolTemplateCommand;
import com.everhomes.rest.service_agreement.admin.GetProtocolCommand;
import com.everhomes.rest.service_agreement.admin.GetProtocolResponse;
import com.everhomes.rest.service_agreement.admin.GetProtocolTemplateResponse;
import com.everhomes.rest.service_agreement.admin.SaveProtocolsCommand;
import com.everhomes.rest.service_agreement.admin.SaveProtocolsTemplateCommand;
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

	void saveProtocolTemplate(SaveProtocolsTemplateCommand cmd);

	/**
	 * 协议条款管理获取模板
	 * @param cmd
	 * @return
	 */
	GetProtocolTemplateResponse getProtocolTemplate(GetProtocolTemplateCommand cmd);

	GetProtocolResponse getProtocolTemplateDetail(GetProtocolCommand cmd);

	void saveProtocol(SaveProtocolsCommand cmd);

	GetProtocolDetailResponse getProtocolDetail(GetProtocolCommand cmd);
}

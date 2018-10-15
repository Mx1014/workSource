package com.everhomes.service_agreement;

import com.everhomes.rest.service_agreement.ServiceAgreementDTO;

import java.util.List;


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
	 * 通过域空间查询服务协议查不到则取默认模板
	 * @param cmd
	 * @return ServiceAgreementDTO
	 */ 
	ServiceAgreementDTO getByNamespaceIdAndDef2Null(Integer namespaceId) ;

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

    /**
     * 获取生效的协议模板
     * @param type
     * @return
     */
	ProtocolTemplates getActiveProtocolTemplate(Byte type);

    /**
     * 根据ID获取协议模板
     * @param id
     * @return
     */
	ProtocolTemplates getProtocolTemplateById(Long id);

    /**
     * 更新协议模板
     * @param protocolTemplates
     */
	void updateProtocolTemplate(ProtocolTemplates protocolTemplates);

    /**
     * 创建协议模板
     * @param protocolTemplates
     * @return
     */
	Long createProtocolTemplate(ProtocolTemplates protocolTemplates);

    /**
     * 创建模板变量
     * @param protocolTemplateVariables
     */
	void createProtocolTemplateVariable(ProtocolTemplateVariables protocolTemplateVariables);

    /**
     * 根据模板ID获取模板变量
     * @param ownerId
     * @return
     */
	List<ProtocolTemplateVariables> getProtocolTemplateVariables(Long ownerId);

    /**
     * 根据域空间ID和所属协议ID获取变量和变量值
     * @param ownerId
     * @param namespaceId
     * @return
     */
	List<ProtocolVariables> getProtocolVariables(Long ownerId, Integer namespaceId);

    /**
     *
     * @param namespaceId
     * @param type
     * @return
     */
	Protocols getProtocols(Integer namespaceId, Byte type);

	Protocols getProtocolsById(Long id);

    /**
     * 更新协议
     * @param protocols
     */
	void updateProtocol(Protocols protocols);

	Long createProtocol(Protocols protocols);

	void createProtocolVariables(ProtocolVariables protocolVariables);
}

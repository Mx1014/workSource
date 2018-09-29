package com.everhomes.rest.paymentauths;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	   <li>orgnazitionId: 公司ID</li>
 *     <li>EnterprisePaymentAuthsDTO: 授权用户信息</li>
 * </ul>
 */
public class UpdateEnterpirsePaymentAuthsCommand {
	
	private Long orgnazitionId;
	private EnterprisePaymentAuthsDTO enterprisePaymentAuthsDTO;
	

	public Long getOrgnazitionId() {
		return orgnazitionId;
	}



	public void setOrgnazitionId(Long orgnazitionId) {
		this.orgnazitionId = orgnazitionId;
	}



	public EnterprisePaymentAuthsDTO getEnterprisePaymentAuthsDTO() {
		return enterprisePaymentAuthsDTO;
	}



	public void setEnterprisePaymentAuthsDTO(EnterprisePaymentAuthsDTO enterprisePaymentAuthsDTO) {
		this.enterprisePaymentAuthsDTO = enterprisePaymentAuthsDTO;
	}



	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}

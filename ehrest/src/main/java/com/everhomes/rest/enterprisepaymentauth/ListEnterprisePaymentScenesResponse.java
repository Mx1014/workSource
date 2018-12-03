// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>totalSceneCount: 场景总数</li>
 * <li>authSceneCount: 已授权场景数</li>
 * <li>sceneAtuhs: 场景授权列表 参考{@link com.everhomes.rest.enterprisepaymentauth.PaymentAuthSceneDTO}</li>
 * </ul>
 */
public class ListEnterprisePaymentScenesResponse {
	private Integer totalSceneCount;
	private Integer authSceneCount;

	private List<PaymentAuthSceneDTO> sceneAtuhs;

	public ListEnterprisePaymentScenesResponse() {

	}

	public ListEnterprisePaymentScenesResponse(Integer totalSceneCount, Integer authSceneCount, List<PaymentAuthSceneDTO> sceneAtuhs) {
		this.totalSceneCount = totalSceneCount;
		this.authSceneCount = authSceneCount;
		this.sceneAtuhs = sceneAtuhs;
	}

	public Integer getTotalSceneCount() {
		return totalSceneCount;
	}

	public void setTotalSceneCount(Integer totalSceneCount) {
		this.totalSceneCount = totalSceneCount;
	}

	public Integer getAuthSceneCount() {
		return authSceneCount;
	}

	public void setAuthSceneCount(Integer authSceneCount) {
		this.authSceneCount = authSceneCount;
	}

	public List<PaymentAuthSceneDTO> getSceneAtuhs() {
		return sceneAtuhs;
	}

	public void setSceneAtuhs(List<PaymentAuthSceneDTO> sceneAtuhs) {
		this.sceneAtuhs = sceneAtuhs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}

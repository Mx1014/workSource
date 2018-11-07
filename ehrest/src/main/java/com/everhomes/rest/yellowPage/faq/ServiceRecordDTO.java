package com.everhomes.rest.yellowPage.faq;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowCaseId : flowCaseId</li>
 * <li>serviceId : 服务id</li>
 * <li>serviceName : 服务名称</li>
 * <li>applyTime : 申请时间 format:1999-01-01 09:09</li>
 * <li>status : 2-处理中 6-暂缓 4-完成 其他详见{@link com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus}</li>
 * <li>statusName : 应用的originId</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月25日
 */

public class ServiceRecordDTO {
	
	private Long flowCaseId;
	private Long serviceId;
	private String serviceName;
	private String applyTime;
	private Byte status;
	private String statusName;
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getFlowCaseId() {
		return flowCaseId;
	}
	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
}

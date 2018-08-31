package com.everhomes.rest.yellowPage.stat;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serviceName: 服务名称</li>
 * <li>serviceTypeName: 服务类型名称</li>
 * <li>serviceTypeCount: 点击次数，以服务类型做统计时使用</li>
 * <li>serviceClickCount: 查看详情次数</li>
 * <li>supportClickCount: 咨询点击次数</li>
 * <li>shareClickCount: 分享次数</li>
 * <li>commitClickCount: 点击申请按钮次数</li>
 * <li>commitTimes: 提交申请次数</li>
 * <li>conversionPercent: 转化率</li>
 * </ul>
 */
public class ClickStatDTO {
	
	public ClickStatDTO() {
		serviceTypeCount = 0L;
		serviceClickCount = 0L;
		supportClickCount = 0L;
		shareClickCount = 0L;
		commitClickCount = 0L;
		commitTimes = 0;
		conversionPercent = "0.00";
	}

	private Long serviceId;
	private String serviceName;

	private Long serviceTypeId;// 服务类型id
	private String serviceTypeName;
	
	private Long serviceTypeCount; //当以服务类型统计
	
	private Long serviceClickCount;

	private Long supportClickCount;

	private Long shareClickCount;

	private Long commitClickCount;

	private Integer commitTimes;

	private String conversionPercent;
	
	private String conversionShowPercent; //用于导出excel时显示，比conversionPercent多了%号
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public Long getServiceClickCount() {
		return serviceClickCount;
	}

	public void setServiceClickCount(Long serviceClickCount) {
		this.serviceClickCount = serviceClickCount;
	}

	public Long getSupportClickCount() {
		return supportClickCount;
	}

	public void setSupportClickCount(Long supportClickCount) {
		this.supportClickCount = supportClickCount;
	}

	public Long getShareClickCount() {
		return shareClickCount;
	}

	public void setShareClickCount(Long shareClickCount) {
		this.shareClickCount = shareClickCount;
	}

	public Long getCommitClickCount() {
		return commitClickCount;
	}

	public void setCommitClickCount(Long commitClickCount) {
		this.commitClickCount = commitClickCount;
	}

	public String getConversionPercent() {
		return conversionPercent;
	}

	public void setConversionPercent(String conversionPercent) {
		this.conversionPercent = conversionPercent;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public Long getServiceTypeCount() {
		return serviceTypeCount;
	}

	public void setServiceTypeCount(Long serviceTypeCount) {
		this.serviceTypeCount = serviceTypeCount;
	}

	public Integer getCommitTimes() {
		return commitTimes;
	}

	public void setCommitTimes(Integer commitTimes) {
		this.commitTimes = commitTimes;
	}

	public String getConversionShowPercent() {
		return null == conversionPercent ? null : conversionPercent+"%";
	}

}

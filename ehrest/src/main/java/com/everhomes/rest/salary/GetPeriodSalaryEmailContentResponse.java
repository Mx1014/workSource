// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>emailContent: 邮件内容</li>
 * <li>salaryGroupId: 批次id</li>
 * <li>salaryGroupName: 批次名称</li>
 * <li>salaryGroupEntities: 字段项列表 参考{@link com.everhomes.rest.salary.SalaryGroupEntityDTO}</li>
 * </ul>
 */
public class GetPeriodSalaryEmailContentResponse {

	private String emailContent;
	private Long salaryGroupId;
	private String salaryGroupName;


	@ItemType(SalaryGroupEntityDTO.class)
	private List<SalaryGroupEntityDTO> salaryGroupEntities;

	public GetPeriodSalaryEmailContentResponse() {

	}

	public GetPeriodSalaryEmailContentResponse(String emailContent, List<SalaryGroupEntityDTO> salaryGroupEntities, String salaryGroupName,
											   Long salaryGroupId) {
		super();

		this.emailContent = emailContent;
		this.salaryGroupEntities = salaryGroupEntities;
		this.salaryGroupName = salaryGroupName;
		this.salaryGroupId = salaryGroupId;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public String getSalaryGroupName() {
		return salaryGroupName;
	}

	public void setSalaryGroupName(String salaryGroupName) {
		this.salaryGroupName = salaryGroupName;
	}
	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public List<SalaryGroupEntityDTO> getSalaryGroupEntities() {
		return salaryGroupEntities;
	}

	public void setSalaryGroupEntities(List<SalaryGroupEntityDTO> salaryGroupEntities) {
		this.salaryGroupEntities = salaryGroupEntities;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}

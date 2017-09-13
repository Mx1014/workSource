package com.everhomes.rest.incubator;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>teamName: teamName</li>
 *     <li>projectType: projectType</li>
 *     <li>projectName: projectName</li>
 *     <li>businessLicenceUri: businessLicenceUri</li>
 *     <li>planBookUri: planBookUri</li>
 *     <li>chargerName: chargerName</li>
 *     <li>chargerPhone: chargerPhone</li>
 *     <li>chargerEmail: chargerEmail</li>
 *     <li>chargerWechat: chargerWechat</li>
 * </ul>
 */
public class AddIncubatorCommand {

	Integer namespaceId;
	String teamName;
	String projectType;
	String projectName;
	String businessLicenceUri;
	String planBookUri;
	String chargerName;
	String chargerPhone;
	String chargerEmail;
	String chargerWechat;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBusinessLicenceUri() {
		return businessLicenceUri;
	}

	public void setBusinessLicenceUri(String businessLicenceUri) {
		this.businessLicenceUri = businessLicenceUri;
	}

	public String getPlanBookUri() {
		return planBookUri;
	}

	public void setPlanBookUri(String planBookUri) {
		this.planBookUri = planBookUri;
	}

	public String getChargerName() {
		return chargerName;
	}

	public void setChargerName(String chargerName) {
		this.chargerName = chargerName;
	}

	public String getChargerPhone() {
		return chargerPhone;
	}

	public void setChargerPhone(String chargerPhone) {
		this.chargerPhone = chargerPhone;
	}

	public String getChargerEmail() {
		return chargerEmail;
	}

	public void setChargerEmail(String chargerEmail) {
		this.chargerEmail = chargerEmail;
	}

	public String getChargerWechat() {
		return chargerWechat;
	}

	public void setChargerWechat(String chargerWechat) {
		this.chargerWechat = chargerWechat;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}

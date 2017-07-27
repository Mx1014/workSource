package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>id：id</li>
 * <li>ownerType：所属对象类型organization</li>
 * <li>ownerId：所属对象id</li>
 * <li>groupName：规则名(考勤组名)</li>
 * <li>targets：考勤组关联对象</li> 
 * <li>ruleType: 0- 排班制 ; 1- 固定班次</li>
 * <li>punchGeoPoints: 地点规则 {@link com.everhomes.rest.techpark.punch.PunchGeoPointDTO}</li>
 * <li>wifis: wifi规则{@link com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO}</li>
 * <li>timeRule: 上班时间{@link com.everhomes.rest.techpark.punch.PunchTimeRuleDTO}</li>
 * <li>specialDay: 特殊日期列表 {@link com.everhomes.rest.techpark.punch.admin.PunchSpecialDayDTO}</li>
 * <li>schedulings: 排班列表{@link com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO}</li>
 * <li>chinaHolidayFlag: 使用中国法定假日falg  0-否 1-是 </li>
 * </ul>
 */
public class PunchGroupDTO {
	private Long id;
	
	private Long punchOriganizationId;
	
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;

	private String groupName;
	
	@ItemType(UniongroupTarget.class)
	private List<UniongroupTarget> targets; 
	
	private Byte ruleType ;

	@ItemType(PunchGeoPointDTO.class)
	private  List<PunchGeoPointDTO>  punchGeoPoints;

	@ItemType(PunchWiFiDTO.class)
	private  List<PunchWiFiDTO>  wifis;
	
	private PunchTimeRuleDTO timeRule;
	  
	@ItemType(PunchSpecialDayDTO.class)
	private List<PunchSpecialDayDTO> specialDay;
	

	@ItemType(PunchSchedulingDTO.class)
	private List<PunchSchedulingDTO> schedulings;
	
	private Byte chinaHolidayFlag;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<UniongroupTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<UniongroupTarget> targets) {
		this.targets = targets;
	}

	public Byte getRuleType() {
		return ruleType;
	}

	public void setRuleType(Byte ruleType) {
		this.ruleType = ruleType;
	}

	public List<PunchGeoPointDTO> getPunchGeoPoints() {
		return punchGeoPoints;
	}

	public void setPunchGeoPoints(List<PunchGeoPointDTO> punchGeoPoints) {
		this.punchGeoPoints = punchGeoPoints;
	}

	public List<PunchWiFiDTO> getWifis() {
		return wifis;
	}

	public void setWifis(List<PunchWiFiDTO> wifis) {
		this.wifis = wifis;
	}

	public PunchTimeRuleDTO getTimeRule() {
		return timeRule;
	}

	public void setTimeRule(PunchTimeRuleDTO timeRule) {
		this.timeRule = timeRule;
	}

	public PunchSpecialDayDTO getSpecialDay() {
		return specialDay;
	}

	public void setSpecialDay(PunchSpecialDayDTO specialDay) {
		this.specialDay = specialDay;
	}

	public Byte getChinaHolidayFlag() {
		return chinaHolidayFlag;
	}

	public void setChinaHolidayFlag(Byte chinaHolidayFlag) {
		this.chinaHolidayFlag = chinaHolidayFlag;
	}

	public List<PunchSchedulingDTO> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<PunchSchedulingDTO> schedulings) {
		this.schedulings = schedulings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPunchOriganizationId() {
		return punchOriganizationId;
	}

	public void setPunchOriganizationId(Long punchOriganizationId) {
		this.punchOriganizationId = punchOriganizationId;
	}

 
}

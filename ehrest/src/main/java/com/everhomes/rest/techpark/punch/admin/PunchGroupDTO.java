package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.rest.techpark.punch.PunchOvertimeRuleDTO;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * <ul> 
 * <li>id：考勤组的id</li>
 * <li>ownerType：所属对象类型organization</li>
 * <li>ownerId：所属对象id</li>
 * <li>groupName：规则名(考勤组名)</li>
 * <li>targets：考勤组关联对象</li> 
 * <li>employees：考勤组下的所有人员</li>
 * <li>ruleType: 0- 排班制 ; 1- 固定班次</li>
 * <li>punchGeoPoints: 地点规则 {@link com.everhomes.rest.techpark.punch.PunchGeoPointDTO}</li>
 * <li>wifis: wifi规则{@link com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO}</li>
 * <li>timeRules: 上班时间{@link com.everhomes.rest.techpark.punch.PunchTimeRuleDTO}</li>
 * <li>specialDays: 特殊日期列表 {@link com.everhomes.rest.techpark.punch.admin.PunchSpecialDayDTO}</li>
 * <li>schedulings: 排班列表{@link com.everhomes.rest.techpark.punch.admin.PunchSchedulingEmployeeDTO}</li>
 * <li>punchOvertimeRules: 加班规则，参考{@link com.everhomes.rest.techpark.punch.PunchOvertimeRuleDTO}</li>
 * <li>chinaHolidayFlag: 使用中国法定假日falg  0-否 1-是 </li>
 * <li>punchRemindFlag: 打卡提醒开关 1-支持提醒 0-不支持提醒</li>
 * <li>remindMinutesOnDuty: 上班提醒提前分钟数</li>
 * <li>employeeCount: 总人数 </li>
 * <li>unSchedulingCount: 未排班人数</li>
 * <li>operatorUid: 最后操作人id </li>
 * <li>operatorName: 最后操作人姓名 </li>
 * <li>operateTime: 最后操作时间 </li>
 * <li>status: 2-正常  3-修改次日生效 4-新增次日生效 </li>
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

	@ItemType(UniongroupTarget.class)
	private List<UniongroupTarget> employees;

	private Byte ruleType ;

	@ItemType(PunchGeoPointDTO.class)
	private  List<PunchGeoPointDTO>  punchGeoPoints;

	@ItemType(PunchWiFiDTO.class)
	private  List<PunchWiFiDTO>  wifis;

	@ItemType(PunchTimeRuleDTO.class)
	private  List<PunchTimeRuleDTO> timeRules;
	  
	@ItemType(PunchSpecialDayDTO.class)
	private List<PunchSpecialDayDTO> specialDays;
	

	@ItemType(PunchSchedulingDTO.class)
	private List<PunchSchedulingDTO> schedulings;

	private List<PunchOvertimeRuleDTO> punchOvertimeRules;
	
	private Byte chinaHolidayFlag;
	private Byte punchRemindFlag;
	private Integer remindMinutesOnDuty;
	
	private Integer employeeCount;
	private Integer unSchedulingCount;
    private Long operatorUid;

    private String operatorName; 
    
    private Long operateTime;
    private Byte status;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public List<PunchTimeRuleDTO> getTimeRules() {
		return timeRules;
	}

	public void setTimeRules(List<PunchTimeRuleDTO> timeRules) {
		this.timeRules = timeRules;
	}

	public List<PunchSpecialDayDTO> getSpecialDays() {
		return specialDays;
	}

	public void setSpecialDays(List<PunchSpecialDayDTO> specialDays) {
		this.specialDays = specialDays;
	}

	public List<PunchSchedulingDTO> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<PunchSchedulingDTO> schedulings) {
		this.schedulings = schedulings;
	}

	public Byte getChinaHolidayFlag() {
		return chinaHolidayFlag;
	}

	public void setChinaHolidayFlag(Byte chinaHolidayFlag) {
		this.chinaHolidayFlag = chinaHolidayFlag;
	}

	public Integer getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(Integer employeeCount) {
		this.employeeCount = employeeCount;
	}

	public Integer getUnSchedulingCount() {
		return unSchedulingCount;
	}

	public void setUnSchedulingCount(Integer unSchedulingCount) {
		this.unSchedulingCount = unSchedulingCount;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}


	public List<UniongroupTarget> getEmployees() {
		return employees;
	}

	public void setEmployees(List<UniongroupTarget> employees) {
		this.employees = employees;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public List<PunchOvertimeRuleDTO> getPunchOvertimeRules() {
		return punchOvertimeRules;
	}

	public void setPunchOvertimeRules(List<PunchOvertimeRuleDTO> punchOvertimeRules) {
		this.punchOvertimeRules = punchOvertimeRules;
	}

	public Byte getPunchRemindFlag() {
		return punchRemindFlag;
	}

	public void setPunchRemindFlag(Byte punchRemindFlag) {
		this.punchRemindFlag = punchRemindFlag;
	}

	public Integer getRemindMinutesOnDuty() {
		return remindMinutesOnDuty;
	}

	public void setRemindMinutesOnDuty(Integer remindMinutesOnDuty) {
		this.remindMinutesOnDuty = remindMinutesOnDuty;
	}
}

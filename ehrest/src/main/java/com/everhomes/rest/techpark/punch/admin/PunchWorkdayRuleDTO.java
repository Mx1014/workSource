package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>id:id</li>
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * <li>name：名称</li>
 * <li>description:详情</li>
 * <li>workWeekDates:周几上班-从周日到周六是0123456，开放哪天就在数组传哪天</li>
 * <li>workdays: Long时间戳list 特殊上班日 </li>
 * <li>holidays: Long时间戳list 特殊休息日 </li>
 * </ul>
 */
public class PunchWorkdayRuleDTO {
	  private Long id;
	    private String ownerType;
	    private Long ownerId;
	    private String name;
	    private String description;

		@ItemType(Integer.class)
		private  List<Integer>  workWeekDates;

		@ItemType(Long.class)
		private List<Long> workdays;
		
		@ItemType(Long.class)
		private List<Long> holidays;
		
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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public List<Integer> getWorkWeekDates() {
			return workWeekDates;
		}

		public void setWorkWeekDates(List<Integer> workWeekDates) {
			this.workWeekDates = workWeekDates;
		}

		public List<Long> getWorkdays() {
			return workdays;
		}

		public void setWorkdays(List<Long> workdays) {
			this.workdays = workdays;
		}

		public List<Long> getHolidays() {
			return holidays;
		}

		public void setHolidays(List<Long> holidays) {
			this.holidays = holidays;
		}

		
		
}

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
 * <li>id：考勤组id</li>  
 * </ul>
 */
public class GetPunchGroupCommand {

 
	@NotNull
	private Long id;
 
	
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
 

 
}

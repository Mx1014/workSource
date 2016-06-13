package com.everhomes.cooperation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.cooperation.NewCooperationCommand;

public class CooperationTest extends CoreServerTestCase {
	@Autowired
	CooperationService cooperationService;

	@Test
	public void testNewCooperation() {
		NewCooperationCommand cmd = new NewCooperationCommand();
		cmd.setApplicantName("user");
		cmd.setApplicantOccupation("CTO");
		cmd.setApplicantPhone("0755110");
		cmd.setAreaName("nanshan");
		cmd.setProvinceName("guangdong"); 
		cmd.setAddress("addresstest");
		cmd.setName("testName");
		cmd.setCooperationType("PM");
		cmd.setContactToken("13322215114");
		cmd.setCityName("cityName");
		cmd.setApplicantEmail("2222#$@adsf@3");
		cmd.setCommunityNames("南山家园,南山佳苑,南山之家,南山尼玛");
		System.out.println(cmd.toString());
		cooperationService.newCooperation(cmd);
	}

}

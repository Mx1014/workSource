package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;

public class UpdateRentalRuleCommand {
	private String siteType ;
	private String rentalStartTime;
	private String rentalEndTime;
	private String payStartTime;
	private String payEndTime;
	private String payRatio;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

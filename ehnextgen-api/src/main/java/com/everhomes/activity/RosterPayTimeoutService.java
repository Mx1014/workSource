package com.everhomes.activity;

public interface RosterPayTimeoutService {
	void pushTimeout(ActivityRoster roster);
	void cancelTimeoutOrder(Long rosterId);

}

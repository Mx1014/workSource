package com.everhomes.test.junit.rental;

import org.junit.After;
import org.junit.Before;

import com.everhomes.test.core.base.BaseLoginAuthTestCase;

public class RentalAdminTest extends BaseLoginAuthTestCase {
	@Before
	public void setUp() {
		super.setUp();
	}

	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}

}

package com.everhomes.visitorsys;

import com.everhomes.junit.CoreServerTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class VisitorSysHKWSUtilTest extends CoreServerTestCase {

    @Autowired
    private VisitorSysHKWSUtil service;

    @Test
    public void addAppointment() {
    }

    @Test
    public void delAppointment() {
    }

    @Test
    public void syncHKWSUsers() {
        service.syncHKWSUsers(null);
    }
}
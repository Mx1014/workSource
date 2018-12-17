package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysThirdMappingProvider {

    void createMapping(VisitorSysThirdMapping mapping);
    void deleteMapping(Long id);
    List<VisitorSysThirdMapping> findMappingByVisitorId(Long visitorId);
}

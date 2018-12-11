package com.everhomes.search;

import com.everhomes.rest.visitorsys.ListBookedVisitorsResponse;
import com.everhomes.rest.visitorsys.ListFreqVisitorsCommand;
import com.everhomes.rest.visitorsys.ListFreqVisitorsResponse;
import com.everhomes.visitorsys.ListBookedVisitorParams;

public interface FreqVisitorSearcher {

    ListFreqVisitorsResponse searchVisitors(ListFreqVisitorsCommand cmd);

    void syncVisitorsFromDb(Integer namespaceId);
}

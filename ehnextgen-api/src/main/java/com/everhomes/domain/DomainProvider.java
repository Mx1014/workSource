package com.everhomes.domain;

import java.util.List;

/**
 * Created by sfyan on 2017/5/27.
 */
public interface DomainProvider {

    Domain findDomainByDomain(String domain);

    List<Domain> listAllDomains();
}

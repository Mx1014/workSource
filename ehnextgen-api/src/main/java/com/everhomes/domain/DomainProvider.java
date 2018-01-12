package com.everhomes.domain;

import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by sfyan on 2017/5/27.
 */
public interface DomainProvider {

    Domain findDomainByDomain(String domain);

    Domain findDomainByNamespaceId(Integer namespaceId);

    List<Domain> listAllDomains();
}

package com.everhomes.domain;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;

/**
 * Created by sfyan on 2017/5/27.
 */
public interface DomainProvider {

    Domain findDomainByDomain(String domain);

    void updateDomain(Domain domain);

    Domain findDomainById(Long id);

    Domain findDomainByNamespaceId(Integer namespaceId);

    List<Domain> listAllDomains();
}

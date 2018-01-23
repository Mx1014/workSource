package com.everhomes.domain;

import com.everhomes.rest.domain.DomainDTO;
import com.everhomes.rest.domain.GetDomainInfoCommand;
import com.everhomes.rest.domain.UpdateDomainInfoCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sfyan on 2017/5/27.
 */
public interface DomainService {

    DomainDTO getDomainInfo(GetDomainInfoCommand cmd, HttpServletRequest request);

    Domain findDomainByNamespaceId(Integer namespaceId);

    List<DomainDTO> listAllDomains();

    DomainDTO updateDomain(UpdateDomainInfoCommand cmd);
}

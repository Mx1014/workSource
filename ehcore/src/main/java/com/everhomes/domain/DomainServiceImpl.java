package com.everhomes.domain;

import com.everhomes.db.DbProvider;
import com.everhomes.rest.domain.DomainDTO;
import com.everhomes.rest.domain.GetDomainInfoCommand;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainServiceImpl implements DomainService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainServiceImpl.class);

    @Autowired
    private DomainProvider domainProvider ;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public DomainDTO getDomainInfo(GetDomainInfoCommand cmd) {
        return ConvertHelper.convert(domainProvider.findDomainByDomain(cmd.getDomain()), DomainDTO.class);
    }

}

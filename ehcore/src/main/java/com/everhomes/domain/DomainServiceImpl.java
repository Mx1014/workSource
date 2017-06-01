package com.everhomes.domain;

import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.domain.DomainDTO;
import com.everhomes.rest.domain.GetDomainInfoCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class DomainServiceImpl implements DomainService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainServiceImpl.class);

    @Autowired
    private DomainProvider domainProvider ;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public DomainDTO getDomainInfo(GetDomainInfoCommand cmd, HttpServletRequest request) {
        String domain = cmd.getDomain();
        if(StringUtils.isEmpty(domain)){
            domain = request.getHeader("host");
        }
        Domain domainInfo = domainProvider.findDomainByDomain(domain);

        //设置当前机构
        if(EntityType.ORGANIZATIONS == EntityType.fromCode(domainInfo.getOwnerType())){
            UserContext.setCurrentSceneId(domainInfo.getOwnerId());
            UserContext.setCurrentSceneType(domainInfo.getOwnerType());
        }

        return ConvertHelper.convert(domainInfo, DomainDTO.class);
    }

}

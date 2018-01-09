package com.everhomes.domain;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.domain.DomainDTO;
import com.everhomes.rest.domain.GetDomainInfoCommand;
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

    @Autowired
    private ContentServerService contentServerService;

    @Override
    public DomainDTO getDomainInfo(GetDomainInfoCommand cmd, HttpServletRequest request) {
        String domain = "";
        if(null == cmd || StringUtils.isEmpty(cmd.getDomain())){
            domain = request.getHeader("host");
        }else{
            domain = cmd.getDomain();
        }
        Domain domainInfo = domainProvider.findDomainByDomain(domain);

        DomainDTO dto = ConvertHelper.convert(domainInfo, DomainDTO.class);
        if(dto.getIconUri() != null){
            String url = contentServerService.parserUri(dto.getIconUri(), EntityType.DOMAIN.getCode(), domainInfo.getId());
            dto.setIconUrl(url);
        }
        
        return dto;
    }

}

package com.everhomes.domain;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.domain.DomainDTO;
import com.everhomes.rest.domain.GetDomainInfoCommand;
import com.everhomes.rest.domain.UpdateDomainInfoCommand;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        populateUrl(dto);

        return dto;
    }


    @Override
    public Domain findDomainByNamespaceId(Integer namespaceId){
        return domainProvider.findDomainByNamespaceId(namespaceId);
    }

    /**
     * 用于测试
     * @return
     */
    @Override
    public List<DomainDTO> listAllDomains() {

        List<Domain> domains = domainProvider.listAllDomains();

        List<DomainDTO> list = domains.stream().map(r -> {
            DomainDTO dto = ConvertHelper.convert(r, DomainDTO.class);
            //循环次数太多，导致很慢
            //populateUrl(dto);
            return dto;
        }).collect(Collectors.toList());

        return list;
    }

    private void populateUrl(DomainDTO dto){

        if(dto == null){
            return;
        }

        if (dto.getFaviconUri() != null) {
            dto.setFaviconUrl(contentServerService.parserUri(dto.getFaviconUri(), EntityType.DOMAIN.getCode(), dto.getId()));
        }

        if (dto.getLoginBgUri() != null) {
            dto.setLoginBgUrl(contentServerService.parserUri(dto.getLoginBgUri(), EntityType.DOMAIN.getCode(), dto.getId()));
        }

        if (dto.getLoginLogoUri() != null) {
            dto.setLoginLogoUrl(contentServerService.parserUri(dto.getLoginLogoUri(), EntityType.DOMAIN.getCode(), dto.getId()));
        }

        if (dto.getMenuLogoUri() != null) {
            dto.setMenuLogoUrl(contentServerService.parserUri(dto.getMenuLogoUri(), EntityType.DOMAIN.getCode(), dto.getId()));
        }

        if (dto.getMenuLogoCollapsedUri() != null) {
            dto.setMenuLogoCollapsedUrl(contentServerService.parserUri(dto.getMenuLogoCollapsedUri(), EntityType.DOMAIN.getCode(), dto.getId()));
        }
    }

    @Override
    public DomainDTO updateDomain(UpdateDomainInfoCommand cmd){
        Domain domain = domainProvider.findDomainById(cmd.getId());

        domain.setFaviconUri(cmd.getFaviconUri());
        domain.setLoginBgUri(cmd.getLoginBgUri());
        domain.setLoginLogoUri(cmd.getLoginLogoUri());
        domain.setMenuLogoUri(cmd.getMenuLogoUri());
        domain.setMenuLogoCollapsedUri(cmd.getMenuLogoCollapsedUri());

        domainProvider.updateDomain(domain);

        DomainDTO dto = ConvertHelper.convert(domain, DomainDTO.class);
        populateUrl(dto);

        return dto;

    }

}

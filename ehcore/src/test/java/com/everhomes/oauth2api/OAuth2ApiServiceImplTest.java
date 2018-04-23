package com.everhomes.oauth2api;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.oauthapi.OAuth2ApiService;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by xq.tian on 2018/4/13.
 */
public class OAuth2ApiServiceImplTest extends CoreServerTestCase {

    @Autowired
    private OAuth2ApiService oAuth2ApiService;

    @Test
    public void getAuthenticationInfo() {
        List<OrganizationMemberDTO> dtoList = oAuth2ApiService.getAuthenticationInfo(274022L);
        System.out.println(dtoList);
        Assert.notNull(dtoList);

        dtoList = oAuth2ApiService.getAuthenticationInfo(211111111122L);
        System.out.println(dtoList);
        Assert.notNull(dtoList);
    }
}
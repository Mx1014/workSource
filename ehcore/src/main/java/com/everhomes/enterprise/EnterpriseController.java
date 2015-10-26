package com.everhomes.enterprise;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Enterprise controller", site="core")
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    /**
     * <b>URL: /enterprise/listEnterpriseByCommunityId</b>
     * <p>获取小区下的所有企业 TODO: 放管理后台？</p>
     * @return {@link ListEnterpriseResponse}
     */
    @RequestMapping("listEnterpriseByCommunityId")
    @RestReturn(value=ListEnterpriseResponse.class)
    public RestResponse listEnterpriseByCommunityId(@Valid ListEnterpriseByCommunityIdCommand cmd) {
        return null;
    }
    
    /**
     * <b>URL: /enterprise/enterpriseCommunities</b>
     * <p>获取企业入驻的所有小区</p>
     * @return {@link ListEnterpriseResponse}
     */
    @RequestMapping("enterpriseCommunities")
    @RestReturn(value=EnterpriseCommunityResponse.class)
    public RestResponse getEnterpriseCommunities(@Valid GetEnterpriseInfoCommand cmd) {
        return null;
    }
    
    /**
     * <b>URL: /enterprise/enterpriseDetail</b>
     * <p>获取企业的详细信息</p>
     * @return {@link EnterpriseDTO}
     */
    @RequestMapping("enterpriseDetail")
    @RestReturn(value=EnterpriseDTO.class)
    public RestResponse getEnterpriseDetail(@Valid GetEnterpriseInfoCommand cmd) {
        return null;
    }
    
    /**
     * <b>URL: /enterprise/joinEnterpriseToCommunity</b>
     * <p>企业请求加入园区 TODO：客户端完成还是后台管理员完成？</p>
     * @return
     */
    @RequestMapping("joinEnterpriseToCommunity")
    @RestReturn(value=String.class)
    public RestResponse joinEnterpriseToCommunity(@Valid JoinEnterpriseToCommunityCommand cmd) {
        return null;
    }
    
    /**
    * <b>URL: /enterprise/inviteToJoinCommunity</b>
    * <p>邀请企业加入园区 TODO：客户端完成还是后台管理员完成？</p>
    * @return
    */
   @RequestMapping("inviteToJoinCommunity")
   @RestReturn(value=String.class)
    public RestResponse inviteToJoinCommunity(@Valid JoinEnterpriseToCommunityCommand cmd) {
       return null;
   }
   
   /**
   * <b>URL: /enterprise/joinEnterpriseToCommunity</b>
   * <p>显示拥有手机联系人的用户</p>
   * @return {@link QueryEnterpriseByPhoneResponse}
   */
  @RequestMapping("listEnterpriseByPhone")
  @RestReturn(value=QueryEnterpriseByPhoneResponse.class)
   public RestResponse listEnterpriseByPhone(@Valid ListEnterpriseByPhone cmd) {
      return null;
  }
   
   
}

package com.everhomes.enterprise;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.ConvertHelper;

/**
 * <ul>
 * <li>企业管理相关接口：</li>
 * <li>一个企业对应着一个组，相当于Group的概念</li>
 * <li>每个企业下面有多个通讯录，{@link com.everhomes.enterprise.EnterpriseContact}</li>
 * <li>每个通讯录可能绑定着一个具体的用户，也可能还没有对应着用户。</li>
 * <li>同时，每个企业可以入驻多个园区</li>
 * <li>每个园区对应着过去的Community</li>
 * <li>当然，每个园区有多家企业入驻</li>
 * </ul>
 * @author janson
 *
 */
@RestDoc(value="Enterprise controller", site="core")
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController extends ControllerBase {

    @Autowired
    EnterpriseService enterpriseService;
    
    /**
     * <b>URL: /enterprise/listEnterpriseByCommunityId</b>
     * <p>获取小区下的所有企业 TODO: 放管理后台？</p>
     * @return {@link ListEnterpriseResponse}
     */
    @RequestMapping("listEnterpriseByCommunityId")
    @RestReturn(value=ListEnterpriseResponse.class)
    public RestResponse listEnterpriseByCommunityId(@Valid ListEnterpriseByCommunityIdCommand cmd) {
        RestResponse res = new RestResponse(enterpriseService.listEnterpriseByCommunityId(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
     * <b>URL: /enterprise/enterpriseCommunities</b>
     * <p>获取企业入驻的所有小区</p>
     * @return {@link ListEnterpriseResponse}
     */
    @RequestMapping("enterpriseCommunities")
    @RestReturn(value=EnterpriseCommunityResponse.class)
    public RestResponse getEnterpriseCommunities(@Valid GetEnterpriseInfoCommand cmd) {
        RestResponse res = new RestResponse(enterpriseService.listEnterpriseEnrollCommunties(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
     * <b>URL: /enterprise/enterpriseDetail</b>
     * <p>获取企业的详细信息</p>
     * @return {@link EnterpriseDTO}
     */
    @RequestMapping("enterpriseDetail")
    @RestReturn(value=EnterpriseDTO.class)
    public RestResponse getEnterpriseDetail(@Valid GetEnterpriseInfoCommand cmd) {
        EnterpriseCommunity ec = this.enterpriseService.getEnterpriseCommunityById(cmd.getEnterpriseId());
        EnterpriseCommunityDTO dto = ConvertHelper.convert(ec, EnterpriseCommunityDTO.class);
        RestResponse res = new RestResponse(dto);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
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
   * <b>URL: /enterprise/listEnterpriseByPhone</b>
   * <p>显示拥有手机联系人的企业</p>
   * @return {@link QueryEnterpriseByPhoneResponse}
   */
  @RequestMapping("listEnterpriseByPhone")
  @RestReturn(value=QueryEnterpriseByPhoneResponse.class)
   public RestResponse listEnterpriseByPhone(@Valid ListEnterpriseByPhoneCommand cmd) {
      QueryEnterpriseByPhoneResponse resp = new QueryEnterpriseByPhoneResponse();
      List<EnterpriseDTO> dtos = new ArrayList<EnterpriseDTO>();
      for(Enterprise en : this.enterpriseService.listEnterpriseByPhone(cmd.getPhone())) {
          dtos.add(ConvertHelper.convert(en, EnterpriseDTO.class));
      }
      resp.setEnterprises(dtos);
      RestResponse res = new RestResponse();
      res.setErrorCode(ErrorCodes.SUCCESS);
      res.setErrorDescription("OK");
      
      return res;
  }
   
   
}

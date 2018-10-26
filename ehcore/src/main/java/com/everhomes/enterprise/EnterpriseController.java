// @formatter:off
package com.everhomes.enterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.everhomes.rest.enterprise.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.address.Address;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.organization.ListEnterprisesCommandResponse;
import com.everhomes.rest.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationAddressDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.search.EnterpriseSearcher;
import com.everhomes.user.UserContext;
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
    private EnterpriseService enterpriseService;
    
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EnterpriseSearcher enterpriseSearcher;
    /**
     * <b>URL: /enterprise/listEnterpriseByCommunityId</b>
     * <p>获取小区下的所有企业 TODO: 放管理后台？</p>
     * @return {@link ListEnterpriseResponse}
     */
    @RequestMapping("listEnterpriseByCommunityId")
    @RestReturn(value=ListEnterpriseResponse.class)
    public RestResponse listEnterpriseByCommunityId(@Valid ListEnterpriseByCommunityIdCommand cmd) {
        RestResponse res = new RestResponse(enterpriseService.listEnterprisesByCommunityId(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }


    /**
     * <b>URL: /enterprise/listEnterpriseNoReleaseWithCommunityId</b>
     * <p>查询该域空间下不在该项目中的所有企业</p>
     * @param cmd
     * @return
     */
    @RequestMapping("listEnterpriseNoReleaseWithCommunityId")
    @RestReturn(value = ListEnterpriseResponse.class)
    public RestResponse listEnterpriseNoReleaseWithCommunityId(listEnterpriseNoReleaseWithCommunityIdCommand cmd){
        RestResponse res = new RestResponse(enterpriseService.listEnterpriseNoReleaseWithCommunityId(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     *
     * <b>URL: /enterprise/deleteEnterpriseByOrgIdAndCommunityId</b>
     * <p>根据组织ID和项目Id来删除该项目下面的公司</p>
     * @param cmd
     * @return
     */
    @RequestMapping("deleteEnterpriseByOrgIdAndCommunityId")
    @RestReturn(value = String.class)
    public RestResponse deleteEnterpriseByOrgIdAndCommunityId(DeleteEnterpriseCommand cmd){
        RestResponse res = new RestResponse();
        enterpriseService.deleteEnterpriseByOrgIdAndCommunityId(cmd);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
     * <b>URL: /enterprise/syncOwnerIndex</b>
     * <p>搜索索引同步 TODO: 求敢哥优化</p>
     * @return {String.class}
     */
    @RequestMapping("syncIndex")
    @RestReturn(value=String.class)
    public RestResponse syncIndex() {
    	enterpriseSearcher.syncFromDb();
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    
    /**
     * <b>URL: /enterprise/setCurrentEnterprise</b>
     * <p>设置当前的企业</p>
     * @return {String.class}
     */
    @RequestMapping("setCurrentEnterprise")
    @RestReturn(value=String.class)
    public RestResponse setCurrentEnterprise(@Valid SetCurrentEnterpriseCommand cmd ) {
    	enterpriseService.setCurrentEnterprise(cmd);
        RestResponse res = new RestResponse();
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
    
    @RequestMapping("searchCommunities")
    @RestReturn(value=CommunityDoc.class, collection=true)
    public RestResponse searchCommunities(@Valid SearchEnterpriseCommunityCommand cmd) {
        List<CommunityDoc> results = this.enterpriseService.searchCommunities(cmd);
        RestResponse response =  new RestResponse(results);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("searchEnterprise")
    @RestReturn(value=ListEnterpriseResponse.class)
    public RestResponse searchEnterprise(@Valid SearchEnterpriseCommand cmd) {
    	SearchOrganizationCommand command = ConvertHelper.convert(cmd, SearchOrganizationCommand.class);

        //查出所有公司包括物业公司 by sfyan 20160921
//    	command.setOrganizationType(OrganizationType.ENTERPRISE.getCode());
        ListEnterprisesCommandResponse resp = organizationService.searchEnterprise(command);
        List<EnterpriseDTO> enterprises = resp.getDtos().stream().map((r) ->{
        	 EnterpriseDTO eDto = ConvertHelper.convert(r, EnterpriseDTO.class);
    		 eDto.setEnterpriseAddress(r.getAddress());
    		 eDto.setId(r.getOrganizationId());
             if(null != r.getMember()){
                 eDto.setContactStatus(r.getMember().getStatus());
             }
    		 if(null != r.getAttachments()){
    			 eDto.setAttachments(r.getAttachments().stream().map(n->{
    					return ConvertHelper.convert(n,EnterpriseAttachmentDTO.class); 
    			}).collect(Collectors.toList()));
    		 }
    		 eDto.setAdminMembers(r.getAdminMembers());
    		 eDto.setEmailDomain(r.getEmailDomain());
        	return eDto;
        }).collect(Collectors.toList());
        ListEnterpriseResponse r = new ListEnterpriseResponse();
        r.setEnterprises(enterprises);
        r.setNextPageAnchor(resp.getNextPageAnchor());
        RestResponse res =  new RestResponse(r);

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
     * <b>URL: /enterprise/requestToJoinCommunity</b>
     * <p>企业请求加入园区 TODO：客户端完成还是后台管理员完成？</p>
     * @return
     */
    @RequestMapping("requestToJoinCommunity")
    @RestReturn(value=String.class)
    public RestResponse joinEnterpriseToCommunity(@Valid JoinEnterpriseToCommunityCommand cmd) {
        this.enterpriseService.requestToJoinCommunity(UserContext.current().getUser(), cmd.getEnterpriseId(), cmd.getCommunityId());
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
    * <b>URL: /enterprise/inviteToJoinCommunity</b>
    * <p>邀请企业加入园区 TODO：客户端完成还是后台管理员完成？</p>
    * @return
    */
   @RequestMapping("inviteToJoinCommunity")
   @RestReturn(value=String.class)
    public RestResponse inviteToJoinCommunity(@Valid JoinEnterpriseToCommunityCommand cmd) {
       this.enterpriseService.inviteToJoinCommunity(cmd.getEnterpriseId(), cmd.getCommunityId());
       RestResponse res = new RestResponse();
       res.setErrorCode(ErrorCodes.SUCCESS);
       res.setErrorDescription("OK");
       return res;
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
      List<EnterpriseDTO> dtos = this.enterpriseService.listEnterpriseByPhone(cmd.getPhone());
      
      resp.setEnterprises(dtos);
      RestResponse res = new RestResponse();
      res.setResponseObject(resp);
      res.setErrorCode(ErrorCodes.SUCCESS);
      res.setErrorDescription("OK");
      
      return res;
  }
  
  /**
  * <b>URL: /enterprise/listUserRelatedEnterprises</b>
  * <p>列出个人相关的企业</p>
  * @return {@link EnterpriseDTO}
  */
  @RequestMapping("listUserRelatedEnterprises")
  @RestReturn(value=EnterpriseDTO.class, collection=true)
  public RestResponse listUserRelatedEnterprises(@Valid ListUserRelatedEnterprisesCommand cmd) {
	 List<EnterpriseDTO> eDtos = new ArrayList<EnterpriseDTO>();
	 List<OrganizationDetailDTO> oDtos = organizationService.listUserRelateEnterprises(cmd);
	 for (OrganizationDetailDTO oDto : oDtos) {
		 EnterpriseDTO eDto = ConvertHelper.convert(oDto, EnterpriseDTO.class);
		 eDto.setContactNickName(oDto.getMember().getContactName());
		 eDto.setContactsPhone(oDto.getMember().getContactToken());
		 eDto.setEnterpriseAddress(oDto.getAddress());
		 eDto.setId(oDto.getOrganizationId());
		 eDto.setContactStatus(oDto.getMember().getStatus());
		 if(null != oDto.getAttachments()){
			 eDto.setAttachments(oDto.getAttachments().stream().map(r->{
					return ConvertHelper.convert(r,EnterpriseAttachmentDTO.class); 
			}).collect(Collectors.toList()));
		 }
		 
		 if(null != oDto.getCommunity()){
			 eDto.setCommunityId(oDto.getCommunity().getId());
			 eDto.setCommunityName(oDto.getCommunity().getName());
			 eDto.setAreaId(oDto.getCommunity().getAreaId());
			 eDto.setAreaName(oDto.getCommunity().getAreaName());
			 eDto.setCityId(oDto.getCommunity().getCityId());
			 eDto.setCityName(oDto.getCommunity().getCityName());
			 eDto.setCommunityType(oDto.getCommunity().getCommunityType());
			 eDto.setDefaultForumId(oDto.getCommunity().getDefaultForumId());
			 eDto.setFeedbackForumId(oDto.getCommunity().getFeedbackForumId());
			 
		 }
		
		 eDtos.add(eDto);
	 }
     RestResponse res = new RestResponse(eDtos);
     res.setErrorCode(ErrorCodes.SUCCESS);
     res.setErrorDescription("OK");
     
     return res;
     }
   
  /**
   * <b>URL: /enterprise/findEnterpriseByAddress</b>
   * <p>根据门牌查询企业</p>
   * @return
   */
  @RequestMapping("findEnterpriseByAddress")
  @RestReturn(value=EnterpriseDTO.class)
   public RestResponse findEnterpriseByAddress(@Valid FindEnterpriseByAddressCommand cmd) {
      RestResponse res = new RestResponse(this.enterpriseService.findEnterpriseByAddress(cmd.getAddressId()));
      res.setErrorCode(ErrorCodes.SUCCESS);
      res.setErrorDescription("OK");
      return res;
  }

}

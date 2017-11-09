// @formatter:off
package com.everhomes.enterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprise.AddContactCommand;
import com.everhomes.rest.enterprise.AddContactGroupCommand;
import com.everhomes.rest.enterprise.ApproveContactCommand;
import com.everhomes.rest.enterprise.CreateContactByPhoneCommand;
import com.everhomes.rest.enterprise.CreateContactByUserIdCommand;
import com.everhomes.rest.enterprise.DeleteContactByIdCommand;
import com.everhomes.rest.enterprise.DeleteContactGroupByIdCommand;
import com.everhomes.rest.enterprise.EnterpriseContactDTO;
import com.everhomes.rest.enterprise.GetUserEnterpriseContactCommand;
import com.everhomes.rest.enterprise.LeaveEnterpriseCommand;
import com.everhomes.rest.enterprise.ListContactGroupNamesByEnterpriseIdCommand;
import com.everhomes.rest.enterprise.ListContactGroupNamesByEnterpriseIdCommandResponse;
import com.everhomes.rest.enterprise.ListContactGroupsByEnterpriseIdCommand;
import com.everhomes.rest.enterprise.ListContactGroupsByEnterpriseIdCommandResponse;
import com.everhomes.rest.enterprise.ListContactsByEnterpriseIdCommand;
import com.everhomes.rest.enterprise.ListContactsByPhone;
import com.everhomes.rest.enterprise.ListContactsRequestByEnterpriseIdCommand;
import com.everhomes.rest.enterprise.ListContactsRequestByEnterpriseIdCommandResponse;
import com.everhomes.rest.enterprise.ListEnterpriseContactResponse;
import com.everhomes.rest.enterprise.RejectContactCommand;
import com.everhomes.rest.enterprise.UpdateContactCommand;
import com.everhomes.rest.enterprise.importContactsCommand;
import com.everhomes.rest.organization.CreateOrganizationMemberCommand;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMemberDetailDTO;
import com.everhomes.rest.organization.UpdateOrganizationMemberCommand;
import com.everhomes.rest.organization.UpdatePersonnelsToDepartment;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;

/**
 * <ul>
 * <li>企业通讯录管理类，主要功能：</li>
 * <li>基于手机号创建企业通讯录</li>
 * <li>基于已有用户信息创建企业通讯录</li>
 * <li>创建通讯录分组</li>
 * <li>查询通讯录信息</li>
 * </ul>
 * @author janson
 *
 */
@RestDoc(value="Enterprise contact controller", site="core")
@RestController
@RequestMapping("/contact")
public class EnterpriseContactController extends ControllerBase {

    @Autowired
    private EnterpriseContactService enterpriseContactService;
    
    @Autowired
    private OrganizationService organizationService;
    
    /**
     * <b>URL: /contact/createContactByPhoneCommand</b>
     * <p>注册流程：最开始，用户未存在，根据手机创建企业用户，从而成为此企业的一个成员</p>
     * @return {@link EnterpriseContactDTO}
     */
    @RequestMapping("createContactByPhoneCommand")
    @RestReturn(value=EnterpriseContactDTO.class)
    public RestResponse createContactByPhoneCommand(@Valid CreateContactByPhoneCommand cmd) {
        return null;
    }
    /**
     * <b>URL: /contact/getUserEnterpriseContact</b>
     * <p>获取当前登录用户的通讯录信息</p>
     * @return {@link EnterpriseContactDTO}
     */
    @RequestMapping("getUserEnterpriseContact")
    @RestReturn(value=EnterpriseContactDTO.class)
    public RestResponse getUserEnterpriseContact(@Valid GetUserEnterpriseContactCommand cmd) {
    	EnterpriseContactDTO contactDTO = this.enterpriseContactService.getUserEnterpriseContact(cmd);
    	RestResponse response = new RestResponse(contactDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
    
    /**
     * <b>URL: /contact/importContacts</b>
     * <p>企业导入通讯录</p>
     * @return {@link EnterpriseContactDTO}
     */
    @RequestMapping("importContacts")
    @RestReturn(value=String.class)
    public RestResponse importContacts(@Valid importContactsCommand cmd ,@RequestParam(value = "attachment") MultipartFile[] files) {
    	this.enterpriseContactService.importContacts(cmd,files);
    	RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
    /**
     * <b>URL: /contact/addContact</b>
     * <p>企业添加通讯录</p>
     * @return {@link EnterpriseContactDTO}
     */
    @RequestMapping("addContact")
    @RestReturn(value=String.class)
    public RestResponse addContact(@Valid AddContactCommand cmd ) {
    	this.enterpriseContactService.addContact(cmd);
    	RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
    
    

    /**
     * <b>URL: /contact/addContact</b>
     * <p>企业删除通讯录</p>
     * @return {@link EnterpriseContactDTO}
     */
    @RequestMapping("deleteContactById")
    @RestReturn(value=String.class)
    public RestResponse deleteContactById(@Valid DeleteContactByIdCommand cmd ) {
    	this.enterpriseContactService.deleteContactById(cmd);
    	RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
    
    
    
    /**
     * <b>URL: /contact/createContactByUserIdCommand</b>
     * <p>注册流程，绑定已有用户到企业：根据已有用户ID创建企业用户，从而成为此企业的一个成员</p>
     * 申请加入企业
     * @return {@link EnterpriseContactDTO}
     */
    @RequestMapping("createContactByUserIdCommand")
    @RestReturn(value=EnterpriseContactDTO.class)
    public RestResponse createContactByUserIdCommand(@Valid CreateContactByUserIdCommand cmd) {
//        EnterpriseContactDTO contact = this.enterpriseContactService.applyForContact(cmd);
        CreateOrganizationMemberCommand command = new CreateOrganizationMemberCommand();
        command.setOrganizationId(cmd.getEnterpriseId());
        command.setContactName(cmd.getName());
        OrganizationDTO dto = organizationService.applyForEnterpriseContact(command);
        RestResponse res = new RestResponse();
        if (null == dto) {
            //TODO for error code
            res.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION); 
        } else {
        	 EnterpriseContactDTO contact = new EnterpriseContactDTO();
             contact.setId(dto.getId());
             contact.setEnterpriseId(dto.getId());
             contact.setStatus(dto.getMemberStatus());
            res.setResponseObject(contact);
        }
        
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }
    
    /**
     * <b>URL: /contact/updateContact</b>
     * <p>注册流程，绑定已有用户到企业：根据已有用户ID创建企业用户，从而成为此企业的一个成员</p>
     * 申请加入企业
     * @return {@link EnterpriseContactDTO}
     */
    @RequestMapping("updateContact")
    @RestReturn(value=EnterpriseContactDTO.class)
    public RestResponse updateContact(@Valid UpdateContactCommand cmd) {
        EnterpriseContactDTO contact = this.enterpriseContactService.updateContact(cmd);
        
        RestResponse res = new RestResponse();
        
        res.setResponseObject(contact);
         
        
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }
    
    /**
     * <b>URL: /contact/listContactsByEnterpriseId</b>
     * <p>显示企业联系人</p>
     * @return {@link ListEnterpriseContactResponse}
     */
    @RequestMapping("listContactsByEnterpriseId")
    @RestReturn(value=ListEnterpriseContactResponse.class)
    public RestResponse listContactsByEnterpriseId(@Valid ListContactsByEnterpriseIdCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        ListOrganizationContactCommand command = ConvertHelper.convert(cmd, ListOrganizationContactCommand.class);
        command.setKeywords(cmd.getKeyWord());
        command.setOrganizationId(cmd.getEnterpriseId());
        command.setPageSize(100000);
        ListOrganizationMemberCommandResponse response = this.organizationService.listOrganizationPersonnels(command,false);
        
        List<EnterpriseContactDTO> dtos = new ArrayList<EnterpriseContactDTO>();
        if(null != response.getMembers()){
        	dtos = response.getMembers().stream().map((r) ->{
        		EnterpriseContactDTO dto = new EnterpriseContactDTO();
        		dto.setId(r.getId());
        		dto.setAvatar(r.getAvatar());
        		dto.setEnterpriseId(r.getOrganizationId());
        		dto.setEmployeeNo(String.valueOf(r.getEmployeeNo()));
        		dto.setGroupName(r.getGroupName());
        		dto.setName(r.getContactName());
        		dto.setNickName(r.getNickName());
        		dto.setPhone(r.getContactToken());
        		dto.setStatus(r.getStatus());
        		dto.setUserId(r.getTargetId());
        		dto.setSex(String.valueOf(r.getGender()));
        		return dto;
        	}).collect(Collectors.toList());
        }
        
        ListEnterpriseContactResponse resp = new ListEnterpriseContactResponse();
        resp.setContacts(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        RestResponse res = new RestResponse();
        res.setResponseObject(resp);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }
    
    
    
    /**
     * <b>URL: /contact/listContactsRequestByEnterpriseId</b>
     * <p>显示企业 申请 联系人</p>
     * @return {@link ListContactsRequestByEnterpriseIdCommandResponse}
     */
    @RequestMapping("listContactsRequestByEnterpriseId")
    @RestReturn(value=ListContactsRequestByEnterpriseIdCommandResponse.class)
    public RestResponse listContactsRequestByEnterpriseId(@Valid ListContactsRequestByEnterpriseIdCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<EnterpriseContactDetail> details = this.enterpriseContactService.listContactsRequestByEnterpriseId(locator, cmd.getEnterpriseId(), cmd.getPageSize(),cmd.getKeyWord());
        List<EnterpriseContactDTO> dtos = new ArrayList<EnterpriseContactDTO>();
        for(EnterpriseContactDetail detail : details) {
            dtos.add(ConvertHelper.convert(detail, EnterpriseContactDTO.class));
        }
        
        ListEnterpriseContactResponse resp = new ListEnterpriseContactResponse();
        resp.setContacts(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        RestResponse res = new RestResponse();
        res.setResponseObject(resp);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }
    

    /**
     * <b>URL: /contact/listContactGroupsByEnterpriseId</b>
     * <p>显示企业组织架构-部门架构</p>
     * @return {@link ListContactGroupsByEnterpriseIdCommandResponse}
     */
    @RequestMapping("listContactGroupsByEnterpriseId")
    @RestReturn(value=ListContactGroupsByEnterpriseIdCommandResponse.class)
    public RestResponse listContactGroupsByEnterpriseId(@Valid ListContactGroupsByEnterpriseIdCommand cmd) {
    	
        ListContactGroupsByEnterpriseIdCommandResponse  response = enterpriseContactService.listContactGroupsByEnterpriseId(cmd);
        RestResponse res = new RestResponse();
        res.setResponseObject(response);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }  
    
    /**
     * <b>URL: /contact/listContactGroupNamesByEnterpriseId</b>
     * <p>列出部门名字-没有父节点</p>
     * @return {@link ListContactGroupNamesByEnterpriseIdCommandResponse}
     */
    @RequestMapping("listContactGroupNamesByEnterpriseId")
    @RestReturn(value=ListContactGroupNamesByEnterpriseIdCommandResponse.class)
    public RestResponse listContactGroupNamesByEnterpriseId(@Valid ListContactGroupNamesByEnterpriseIdCommand cmd) {
    	
        ListContactGroupNamesByEnterpriseIdCommandResponse  response = enterpriseContactService.listContactGroupNamesByEnterpriseId(cmd);
        RestResponse res = new RestResponse();
        res.setResponseObject(response);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }  
    /**
     * <b>URL: /contact/addContactGroup</b>
     * <p>添加部门</p>
     * @return {@link AddContactGroupCommandResponse}
     */
    @RequestMapping("addContactGroup")
    @RestReturn(value=String.class)
    public RestResponse addContactGroup(@Valid AddContactGroupCommand cmd) {
    	
    	 enterpriseContactService.addContactGroup(cmd);
        RestResponse res = new RestResponse(); 
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }  
    
    /**
     * <b>URL: /contact/deleteContactGroupById</b>
     * <p>删除部门-没有父节点</p>
     * @return {@link }
     */
    @RequestMapping("deleteContactGroupById")
    @RestReturn(value=String.class)
    public RestResponse deleteContactGroupById(@Valid DeleteContactGroupByIdCommand cmd) {
    	
    	 enterpriseContactService.deleteContactGroupById(cmd);
        RestResponse res = new RestResponse(); 
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }  
    
    
    /**
     * <b>URL: /contact/listContactsByPhone</b>
     * <p>通过手机好查询联系人</p>
     * @return {@link ListEnterpriseContactResponse}
     */
    @RequestMapping("listContactsByPhone")
    @RestReturn(value=EnterpriseContactDTO.class)
    public RestResponse listContactsByPhone(@Valid ListContactsByPhone cmd) {
        return null;
    }
    
    

    /**
     * <b>URL: /contact/approveContact</b>
     * <p>审批通过认证申请</p>
     * @return {@link String}
     */
    @RequestMapping("approveContact")
    @RestReturn(value=String.class)
    public RestResponse approveContactCommand(@Valid ApproveContactCommand cmd) {
//    	this.enterpriseContactService.approveContact(cmd);
    	organizationService.approveForEnterpriseContact(cmd);
    	 RestResponse res = new RestResponse();
         res.setErrorCode(ErrorCodes.SUCCESS);
         res.setErrorDescription("OK");
         
         return res;
    }

    /**
     * <b>URL: /contact/rejectContact</b>
     * <p>审批拒绝认证申请</p>
     * @return {@link String}
     */
    @RequestMapping("rejectContact")
    @RestReturn(value=String.class)
    public RestResponse rejectContact(@Valid RejectContactCommand cmd) {
//    	this.enterpriseContactService.rejectContact(cmd);
    	organizationService.rejectForEnterpriseContact(cmd);
    	 RestResponse res = new RestResponse();
         res.setErrorCode(ErrorCodes.SUCCESS);
         res.setErrorDescription("OK");
         
         return res;
    }

    /**
     * <b>URL: /contact/leave</b>
     * <p>退出指定企业</p>
     */
    @RequestMapping("leave")
    @RestReturn(value=String.class)
    public RestResponse leave(@Valid LeaveEnterpriseCommand cmd) {
    
//        this.enterpriseContactService.leaveEnterprise(cmd);
        organizationService.leaveForEnterpriseContact(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


//    /**
//     * <b>URL: /contact/listOrganizationPersonelsByOrgId</b>
//     * <p>查部门下的人</p>
//     */
//    @RequestMapping("listOrganizationPersonelsByOrgId")
//    @RestReturn(value=String.class)
//    public RestResponse listOrganizationPersonelsByOrgId(@Valid ListOrganizationContactCommand cmd) {
//        ListOrganizationMemberCommandResponse res = enterpriseContactService.listOrganizationPersonnels(cmd);
//        RestResponse response = new RestResponse(res);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

}

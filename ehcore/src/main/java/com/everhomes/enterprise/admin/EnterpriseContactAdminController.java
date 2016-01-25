package com.everhomes.enterprise.admin;

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
import com.everhomes.enterprise.EnterpriseContact;
import com.everhomes.enterprise.EnterpriseContactDetail;
import com.everhomes.enterprise.EnterpriseContactEntry;
import com.everhomes.enterprise.EnterpriseContactService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprise.CreateContactCommand;
import com.everhomes.rest.enterprise.CreateContactEntryCommand;
import com.everhomes.rest.enterprise.EnterpriseContactDTO;
import com.everhomes.rest.enterprise.EnterpriseContactEntryDTO;
import com.everhomes.rest.enterprise.ListContactCommand;
import com.everhomes.rest.enterprise.ListEnterpriseContactResponse;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.util.ConvertHelper;

@RestDoc(value="Enterprise contact admin controller", site="core")
@RestController
@RequestMapping("/admin/contact")
public class EnterpriseContactAdminController extends ControllerBase {
    @Autowired
    private EnterpriseContactService enterpriseContactService;
    
    
    @Autowired
    private OrganizationService organizationService;
    
    /**
     * <b>URL: /admin/contact/createContact</b>
     * <p>创建一个空的待真实用户绑定的通讯录</p>
     * @return String
     */
    @RequestMapping("createContact")
    @RestReturn(value=EnterpriseContactDTO.class)
    public RestResponse createContact(@Valid CreateContactCommand cmd) {
        EnterpriseContact contact = ConvertHelper.convert(cmd, EnterpriseContact.class);
        this.enterpriseContactService.createEnterpriseContact(contact);
        EnterpriseContactDTO dto = ConvertHelper.convert(contact, EnterpriseContactDTO.class);
        RestResponse res = new RestResponse(dto);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
     * <b>URL: /admin/contact/createContactEntry</b>
     * <p>审批加入园区的企业</p>
     * @return String
     */
    @RequestMapping("createContactEntry")
    @RestReturn(value=EnterpriseContactEntryDTO.class)
    public RestResponse createContactEntry(@Valid CreateContactEntryCommand cmd) {
        EnterpriseContactEntry entry = ConvertHelper.convert(cmd, EnterpriseContactEntry.class);
        this.enterpriseContactService.createEnterpriseContactEntry(entry);
        EnterpriseContactEntryDTO dto = ConvertHelper.convert(entry, EnterpriseContactEntryDTO.class);
        RestResponse res = new RestResponse(dto);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    @RequestMapping("listAprovingContact")
    @RestReturn(value=ListEnterpriseContactResponse.class)
    public RestResponse listAprovingContacts(@Valid ListContactCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<EnterpriseContactDetail> details = this.enterpriseContactService.listContactByStatus(locator, GroupMemberStatus.WAITING_FOR_APPROVAL, cmd.getPageSize());
        ListEnterpriseContactResponse o = new ListEnterpriseContactResponse();
        List<EnterpriseContactDTO> dtos = new ArrayList<EnterpriseContactDTO>();
        for(EnterpriseContactDetail detail : details) {
            EnterpriseContactDTO dto = ConvertHelper.convert(detail, EnterpriseContactDTO.class);
            dtos.add(dto);
        }
        o.setContacts(dtos);
        RestResponse res = new RestResponse(o);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;        
    }
    
    @RequestMapping("syncContact")
    @RestReturn(value=String.class)
    public RestResponse syncContact() {
        this.enterpriseContactService.syncEnterpriseContacts();
        
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;        
    }
    
}

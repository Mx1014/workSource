package com.everhomes.enterprise.admin;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.enterprise.CreateContactCommand;
import com.everhomes.enterprise.CreateContactEntryCommand;
import com.everhomes.enterprise.EnterpriseContactDTO;
import com.everhomes.enterprise.EnterpriseContactEntryDTO;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Enterprise contact admin controller", site="core")
@RestController
@RequestMapping("/admin/contact")
public class EnterpriseContactAdminController {
    /**
     * <b>URL: /admin/contact/createContact</b>
     * <p>创建一个空的待真实用户绑定的通讯录</p>
     * @return String
     */
    @RequestMapping("createContact")
    @RestReturn(value=EnterpriseContactDTO.class)
    public RestResponse createContact(@Valid CreateContactCommand cmd) {
        return null;
    }
    
    /**
     * <b>URL: /admin/contact/createContactEntry</b>
     * <p>审批加入园区的企业</p>
     * @return String
     */
    @RequestMapping("createContactEntry")
    @RestReturn(value=EnterpriseContactEntryDTO.class)
    public RestResponse createContactEntry(@Valid CreateContactEntryCommand cmd) {
        return null;
    }
    
}

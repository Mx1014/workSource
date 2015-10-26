package com.everhomes.enterprise;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Enterprise contact controller", site="core")
@RestController
@RequestMapping("/contact")
public class EnterpriseContactController {

    
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
     * <b>URL: /contact/createContactByUserIdCommand</b>
     * <p>注册流程，绑定已有用户到企业：根据已有用户ID创建企业用户，从而成为此企业的一个成员</p>
     * @return {@link EnterpriseContactDTO}
     */
    @RequestMapping("createContactByUserIdCommand")
    @RestReturn(value=EnterpriseContactDTO.class)
    public RestResponse createContactByUserIdCommand(@Valid CreateContactByUserIdCommand cmd) {
        return null;
    }
    
    /**
     * <b>URL: /contact/listContactsByEnterpriseId</b>
     * <p>显示企业联系人</p>
     * @return {@link ListEnterpriseContactResponse}
     */
    @RequestMapping("listContactsByEnterpriseId")
    @RestReturn(value=ListEnterpriseContactResponse.class)
    public RestResponse listContactsByEnterpriseId(@Valid ListContactsByEnterpriseIdCommand cmd) {
        return null;
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
}

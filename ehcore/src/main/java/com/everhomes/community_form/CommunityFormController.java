// @formatter:off
package com.everhomes.community_form;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community_form.CommunityGeneralFormDTO;
import com.everhomes.rest.community_form.CreateOrUpdateCommunityFormCommand;
import com.everhomes.rest.community_form.DeleteCommunityFormCommand;
import com.everhomes.rest.community_form.ListCommunityFormCommand;
import com.everhomes.rest.community_form.ListCommunityFormResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value="CommunityForm controller", site="core")
@RestController
@RequestMapping("/communityForm")
public class CommunityFormController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityFormController.class);

    @Autowired
    private CommunityFormService communityFormService;

    /**
     * <b>URL: /communityForm/createOrUpdateCommunityForm</b>
     * <p>创建或更新项目和表单的关系</p>
     */
    @RequestMapping("createOrUpdateCommunityForm")
    @RestReturn(value=CommunityGeneralFormDTO.class)
    public RestResponse createOrUpdateCommunityForm(@Valid CreateOrUpdateCommunityFormCommand cmd) {

        CommunityGeneralFormDTO cmdResponse = this.communityFormService.createOrUpdateCommunityGeneralForm(cmd);

        RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /communityForm/deleteCommunityForm</b>
     * <p>删除项目和表单的关系</p>
     */
    @RequestMapping("deleteCommunityForm")
    @RestReturn(value=String.class)
    public RestResponse deleteCommunityForm(@Valid DeleteCommunityFormCommand cmd) {

        this.communityFormService.deleteCommunityGeneralForm(cmd);

        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /communityForm/listCommunityForm</b>
     * <p>项目和表单的关系列表</p>
     */
    @RequestMapping("listCommunityForm")
    @RestReturn(value=ListCommunityFormResponse.class)
    public RestResponse listCommunityForm(@Valid ListCommunityFormCommand cmd) {

        ListCommunityFormResponse result = this.communityFormService.listCommunityForm(cmd);

        RestResponse response =  new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}

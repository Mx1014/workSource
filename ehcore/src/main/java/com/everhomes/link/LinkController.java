package com.everhomes.link;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.link.FindLinkByIdCommand;
import com.everhomes.rest.link.LinkDTO;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;

@RestDoc(value="Link controller", site="core")
@RestController
@RequestMapping("/link")
public class LinkController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkController.class);
    
    @Autowired
    private LinkProvider linkProvider;
    /**
     * <b>URL: /link/findLinkById</b>
     * <p>根据id查询link</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("findLinkById")
    @RestReturn(value=LinkDTO.class)
    public RestResponse findLinkById(@Valid FindLinkByIdCommand cmd) {
        Link link = this.linkProvider.findLinkById(cmd.getId());
        LinkDTO dto = ConvertHelper.convert(link, LinkDTO.class);
        RestResponse response =  new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}

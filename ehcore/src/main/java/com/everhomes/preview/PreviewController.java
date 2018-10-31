package com.everhomes.preview;

import javax.validation.Valid;

import com.everhomes.controller.XssCleaner;
import com.everhomes.controller.XssExclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.promotion.PromotionService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.preview.AddPreviewCommand;
import com.everhomes.rest.preview.GetPreviewCommand;
import com.everhomes.rest.preview.PreviewDTO;
import com.everhomes.rest.promotion.CreateOpPromotionCommand;
import com.everhomes.rest.promotion.GetOpPromotionActivityByPromotionId;
import com.everhomes.rest.promotion.ListOpPromotionActivityResponse;
import com.everhomes.rest.promotion.ListPromotionCommand;
import com.everhomes.rest.promotion.OpPromotionActivityDTO;
import com.everhomes.rest.promotion.OpPromotionOrderRangeCommand;
import com.everhomes.rest.promotion.OpPromotionSearchCommand;
import com.everhomes.rest.promotion.UpdateOpPromotionCommand;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/preview")
public class PreviewController extends ControllerBase {
    
    @Autowired
    PreviewService previewService;

    @XssExclude
    @RequestMapping("addPreview")
    @RestReturn(value=PreviewDTO.class)
    public RestResponse addPreview(@Valid AddPreviewCommand cmd) {
        String content = XssCleaner.clean(cmd.getContent());
        cmd.setContent(content);
    	PreviewDTO result = previewService.addPreview(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    @RequestMapping("getPreview")
    @RestReturn(value=PreviewDTO.class)
    @RequireAuthentication(false)
    public RestResponse getPreview(@Valid GetPreviewCommand cmd) {
    	PreviewDTO result = previewService.getPreview(cmd.getId());
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
}

package com.everhomes.richtext;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.richtext.GetRichTextByTokenCommand;
import com.everhomes.rest.richtext.GetRichTextCommand;
import com.everhomes.rest.richtext.RichTextDTO;
import com.everhomes.rest.richtext.UpdateRichTextCommand;
import com.everhomes.util.RequireAuthentication;


@RestController
@RequestMapping("/richText")
public class RichTextController extends ControllerBase {
	
	@Autowired
	private RichTextService richTextService;

	/**
   	 * <b>URL: /richText/updateRichText</b>
   	 * <p> 修改富文本 </p>
   	 */
	@RequestMapping("updateRichText")
    @RestReturn(value=String.class)
    public RestResponse updateRichText(@Valid UpdateRichTextCommand cmd) {
    	 this.richTextService.updateRichText(cmd);
    	 
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
	
	/**
   	 * <b>URL: /richText/getRichText</b>
   	 * <p> 后台获取富文本信息 </p>
   	 */
	@RequestMapping("getRichText")
	@RequireAuthentication(false)
    @RestReturn(value=RichTextDTO.class)
    public RestResponse getRichText(@Valid GetRichTextCommand cmd) {
		RichTextDTO dto = this.richTextService.getRichText(cmd);
    	 
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
	
	/**
   	 * <b>URL: /richText/getRichTextByToken</b>
   	 * <p> 客户端获取富文本信息 </p>
   	 */
	@Deprecated
	@RequestMapping("getRichTextByToken")
	@RequireAuthentication(false)
    @RestReturn(value=RichTextDTO.class)
    public RestResponse getRichTextByToken(@Valid GetRichTextByTokenCommand cmd) {
		RichTextDTO dto = this.richTextService.getRichTextByToken(cmd);
    	 
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
}

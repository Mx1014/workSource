package com.everhomes.service_agreement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.service_agreement.ServiceAgreementCommand;
import com.everhomes.rest.service_agreement.ServiceAgreementDTO;
import com.everhomes.util.RequireAuthentication;

/**
 * 服务协议 Controller
 * @author huanglm
 *
 */
@RestDoc(value="ServiceAgreement controller", site="core")
@RestController
@RequestMapping("/serviceAgreement")
public class ServiceAgreementController extends ControllerBase{
	
	@Autowired
    private ServiceAgreementService serviceAgreementService;
	



	/**
     * <b>URL: /serviceAgreement/getServiceAgreement</b>
     * <p>通过域空间查询服务协议信息</p>
     */
	@RequestMapping("getServiceAgreement")
	@RequireAuthentication(false)
    @RestReturn(value=ServiceAgreementDTO.class)
	public RestResponse getServiceAgreement(ServiceAgreementCommand  cmd) {
		
		ServiceAgreementDTO resultDTO = serviceAgreementService.getServiceAgreementByNamespaceId(cmd);
		RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
	}


	
	/**
	 * <p>设置response 成功信息</p>
	 * @param response
	 */
	private void setResponseSuccess(RestResponse response){
		if(response == null ) return ;
		
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
	}
}

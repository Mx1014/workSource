package com.everhomes.service_agreement.admin;

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
import com.everhomes.rest.service_agreement.admin.AdminServiceAgreementCommand;
import com.everhomes.rest.service_agreement.admin.ServiceAgreementSaveCommand;
import com.everhomes.service_agreement.ServiceAgreementService;
import com.everhomes.util.ConvertHelper;

/**
 * 服务协议 admin Controller
 * @author huanglm
 *
 */
@RestDoc(value="ServiceAgreementAdmin controller", site="core")
@RestController
@RequestMapping("/admin/adminServiceAgreement")
public class ServiceAgreementAdminController extends ControllerBase{
	
	@Autowired
    private ServiceAgreementService serviceAgreementService;
	



	/**
     * <b>URL: /admin/adminServiceAgreement/getAdminServiceAgreement</b>
     * <p>通过域空间查询服务协议信息</p>
     */
	@RequestMapping("getAdminServiceAgreement")
    @RestReturn(value=ServiceAgreementDTO.class)
	public RestResponse getAdminServiceAgreement(AdminServiceAgreementCommand  cmd) {
		//对象转换
		ServiceAgreementCommand newCmd = ConvertHelper.convert(cmd, ServiceAgreementCommand.class);
		ServiceAgreementDTO resultDTO = serviceAgreementService.getServiceAgreementByNamespaceId(newCmd);
		RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
	}


	/**
     * <b>URL: /admin/adminServiceAgreement/saveServiceAgreement</b>
     * <p>保存服务协议信息</p>
     */
	@RequestMapping("saveServiceAgreement")
    @RestReturn(value=String.class)
	public RestResponse saveServiceAgreement(ServiceAgreementSaveCommand  cmd) {

		serviceAgreementService.saveServiceAgreement(cmd);
		RestResponse response = new RestResponse();
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

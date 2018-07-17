// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.visitorsys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestDoc(value = "visitorsys Controller", site = "core")
@RestController
@RequestMapping("/openapi/visitorsys")
public class VisitorSysOpenApiController extends ControllerBase {

	@Autowired
	private VisitorSysService visitorSysService;

	@Override
	public void initListBinder(WebDataBinder binder) {
		super.initListBinder(binder);
		binder.registerCustomEditor(Timestamp.class,new TimestampEditor());
	}


	/**
	 * <b>URL: /openapi/visitorsys/listOrganizations</b>
	 * <p>
	 * 1.查询园区下的公司列表
	 * </p>
	 */
	@RequestMapping("listOrganizations")
	@RestReturn(OpenApiListOrganizationsResponse.class)
	public RestResponse listOrganizations(OpenApiListOrganizationsCommand cmd) {

	}

	/**
	 * <b>URL: /openapi/visitorsys/createVisitor</b>
	 * <p>
	 * 2.访客接口列表
	 * </p>
	 */
	@RequestMapping("createVisitor")
	@RestReturn(OpenApiCreateVisitorResponse.class)
	public RestResponse createVisitor(OpenApiCreateVisitorCommand cmd) {
		return null;
	}

}

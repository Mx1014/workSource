package com.everhomes.general_approval.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;

@RestDoc(value="General approval admin controller", site="core")
@RestController
@RequestMapping("/admin/general_approval")
public class GeneralApprovalAdminController extends ControllerBase {

}

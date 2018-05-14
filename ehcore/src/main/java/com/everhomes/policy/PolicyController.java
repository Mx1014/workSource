package com.everhomes.policy;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value="Policy controller", site="policy")
@RestController
@RequestMapping("/policy")
public class PolicyController extends ControllerBase {

}

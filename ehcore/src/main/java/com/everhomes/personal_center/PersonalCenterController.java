// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value="personal center form controller", site="core")
@RestController
@RequestMapping("/personal_center")
public class PersonalCenterController extends ControllerBase{
}

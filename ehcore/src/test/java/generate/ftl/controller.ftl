// @formatter:off
package ${targetPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("${controllerMapping}")
public class ${targetController} extends ControllerBase {
	
	@Autowired
	private ${targetService} ${targetService?uncap_first};
	
}
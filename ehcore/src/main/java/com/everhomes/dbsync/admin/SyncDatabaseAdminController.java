package com.everhomes.dbsync.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.dbsync.SyncDatabaseService;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.dbsync.SyncAppCreateCommand;
import com.everhomes.rest.dbsync.SyncAppDTO;
import com.everhomes.rest.enterprise.EnterpriseApproveCommand;
import com.everhomes.user.UserContext;

@RestDoc(value="DBSync Admin controller", site="core")
@RestController
@RequestMapping("/admin/dbsync")
public class SyncDatabaseAdminController extends ControllerBase {
    
    @Autowired
    private SyncDatabaseService syncDatabaseService;

    /**
     * <b>URL: /admin/dbsync/createApp</b>
     * <p>创建一个用于同步的 APP </p>
     * @return String
     */
    @RequestMapping("createApp")
    @RestReturn(value=SyncAppDTO.class)
    public RestResponse createApp(@Valid SyncAppCreateCommand cmd) {
        SyncAppDTO dto = syncDatabaseService.createApp(cmd);
        RestResponse res = new RestResponse(dto);
        
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
}

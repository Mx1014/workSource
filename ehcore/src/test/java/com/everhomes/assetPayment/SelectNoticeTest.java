//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.asset.AssetService;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.asset.SelectedNoticeCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/8/16.
 */

public class SelectNoticeTest extends CoreServerTestCase {
    @Autowired
    private AssetService assetService;
    @Test
    public void fun() {
        SelectedNoticeCommand cmd = new SelectedNoticeCommand();
        User u = new User();
        u.setId(238716l);
        UserContext.setCurrentUser(u);
        List<Long> ids = new ArrayList<>();
        ids.add(3l);
        cmd.setBillIds(ids);
        cmd.setOwnerId(240111044331055035l);
        cmd.setOwnerType("community");
        assetService.selectNotice(cmd);
    }
}

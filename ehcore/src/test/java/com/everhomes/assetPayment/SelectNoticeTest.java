//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.asset.AssetService;
import com.everhomes.community.Community;
import com.everhomes.customer.CustomerService;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.asset.SelectedNoticeCommand;
import com.everhomes.rest.customer.SyncCustomersCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.jooq.DSLContext;
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
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private CustomerService customerService;
    @Test
    public void fun() {
        SelectedNoticeCommand cmd = new SelectedNoticeCommand();
        User u = new User();
        u.setId(238716l);
        UserContext.setCurrentUser(u);
        UserContext.setCurrentNamespaceId(999985);
        List<Long> ids = new ArrayList<>();
        ids.add(3l);
//        cmd.setBillIds(ids);
        cmd.setOwnerId(240111044331055035l);
        cmd.setOwnerType("community");
        assetService.selectNotice(cmd);
        System.out.println("method over");
    }
/*    @Test
    public void sync() throws InterruptedException {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Community> communities = context.selectFrom(Tables.EH_COMMUNITIES)
                .where(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(999971))
                .fetchInto(Community.class);
        for(int i = 0; i < communities.size(); i++){
            Community c = communities.get(i);
            SyncCustomersCommand cmd = new SyncCustomersCommand();
            cmd.setCommunityId(c.getId());
            cmd.setNamespaceId(999971);
            customerService.syncIndividualCustomers(cmd);
            //customerService.syncEnterpriseCustomers(cmd);
        }
    }*/
}

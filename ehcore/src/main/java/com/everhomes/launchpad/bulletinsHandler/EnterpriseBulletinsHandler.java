// @formatter:off
package com.everhomes.launchpad.bulletinsHandler;

import com.everhomes.launchpad.BulletinsHandler;
import com.everhomes.module.RouterInfoService;
import com.everhomes.notice.EnterpriseNoticeService;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.BulletinsCard;
import com.everhomes.rest.launchpadbase.routerjson.EnterpriseBulletinsContentRouterJson;
import com.everhomes.rest.notice.EnterpriseNoticeDTO;
import com.everhomes.rest.notice.ListEnterpriseNoticeCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeResponse;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业公告
 */
@Component(BulletinsHandler.BULLETINS_HANDLER_TYPE + 57000)
public class EnterpriseBulletinsHandler implements BulletinsHandler {
    @Autowired
    private EnterpriseNoticeService enterpriseNoticeService;
    @Autowired
    private RouterInfoService routerService;

    @Override
    public List<BulletinsCard> listBulletinsCards(Long appId, AppContext context, Integer rowCount) {
        if (context.getOrganizationId() == null || context.getOrganizationId() == 0) {
            return null;
        }
        UserContext.current().setAppContext(context);

        ListEnterpriseNoticeCommand cmd = new ListEnterpriseNoticeCommand();
        cmd.setOrganizationId(context.getOrganizationId());
        cmd.setPageSize(rowCount);

        ListEnterpriseNoticeResponse response = enterpriseNoticeService.listEnterpriseNoticesByUserId(cmd);

        if (response == null || CollectionUtils.isEmpty(response.getDtos())) {
            return null;
        }

        List<BulletinsCard> cards = new ArrayList<>();

        for (EnterpriseNoticeDTO dto : response.getDtos()) {
            BulletinsCard card = new BulletinsCard();
            card.setTitle(dto.getTitle());
            card.setContent(dto.getContent());
            card.setClientHandlerType(ClientHandlerType.NATIVE.getCode());

            EnterpriseBulletinsContentRouterJson parameters = new EnterpriseBulletinsContentRouterJson();
            parameters.setBulletinId(dto.getId());
            parameters.setBulletinTitle(dto.getTitle());
            parameters.setOrganizationId(context.getOrganizationId());

            String queryStr = routerService.getQueryInDefaultWay(parameters.toString());

            card.setRouterPath("/detail");
            card.setRouterQuery(queryStr);

            String host = "enterprise-bulletin";
            String router = "zl://" + host + card.getRouterPath() + "?moduleId=57000&clientHandlerType=0&appId="+ context.getAppId()+"&" + card.getRouterQuery();
            card.setRouter(router);

            cards.add(card);
        }

        return cards;
    }

}

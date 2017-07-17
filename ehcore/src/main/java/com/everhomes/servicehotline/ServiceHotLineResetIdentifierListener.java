// @formatter:off
package com.everhomes.servicehotline;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.user.UserResetIdentifierVo;
import com.everhomes.server.schema.Tables;
import com.everhomes.techpark.servicehotline.ServiceHotline;
import com.everhomes.techpark.servicehotline.ServiceHotlinesProvider;
import com.everhomes.user.ResetUserIdentifierEvent;
import com.everhomes.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xq.tian on 2017/7/17.
 */
@Component
public class ServiceHotLineResetIdentifierListener implements ApplicationListener<ResetUserIdentifierEvent> {

    @Autowired
    private ServiceHotlinesProvider serviceHotlinesProvider;

    @Async
    @Override
    public void onApplicationEvent(ResetUserIdentifierEvent event) {
        UserResetIdentifierVo vo = (UserResetIdentifierVo) event.getSource();
        List<ServiceHotline> list = serviceHotlinesProvider.queryServiceHotlines(new ListingLocator(), 10, (locator, query) -> {
            query.addConditions(Tables.EH_SERVICE_HOTLINES.USER_ID.eq(vo.getUid()));
            query.addConditions(Tables.EH_SERVICE_HOTLINES.CONTACT.eq(vo.getOldIdentifier()));
            return query;
        });

        if (list != null && list.size() > 0) {
            for (ServiceHotline hotline : list) {
                hotline.setContact(vo.getNewIdentifier());
                hotline.setUpdateTime(DateUtils.currentTimestamp());
                serviceHotlinesProvider.updateServiceHotline(hotline);
            }
        }
    }
}

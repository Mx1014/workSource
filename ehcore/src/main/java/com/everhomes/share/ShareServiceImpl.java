package com.everhomes.share;

import com.everhomes.rest.share.ShareCommand;
import com.everhomes.rest.share.ShareType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/12/18.
 */
@Service
public class ShareServiceImpl implements ShareService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShareServiceImpl.class);

    @Autowired(required = false)
    private List<ShareTypeHandler> handlerList;

    private final Map<ShareType, ShareTypeHandler> handlerMap = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            if (handlerList != null) {
                for (ShareTypeHandler handler : handlerList) {
                    try {
                        handlerMap.put(handler.init(), handler);
                    } catch (Exception e) {
                        LOGGER.error("Share handler init error", e);
                    }
                }
            }
        }
    }

    @Override
    public void share(ShareCommand cmd) {
        ShareType shareType = ShareType.fromCode(cmd.getShareType());
        ShareTypeHandler handler = handlerMap.get(shareType);
        if (handler != null) {
            handler.execute(cmd.getShareData());
        }
    }
}

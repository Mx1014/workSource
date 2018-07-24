//@formatter:off
package com.everhomes.asset;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ExecutorUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Security;

/**
 * Created by Wentian Wang on 2018/4/3.
 */
@Component
public class AssetBillListener implements LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {
    private static Logger LOGGER = LoggerFactory.getLogger(AssetBillListener.class);

    @Autowired
    private LocalBus localBus;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private AssetService assetService;
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        Security.addProvider(new BouncyCastleProvider());
        String subcribeKey = DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhUserIdentifiers.class, null);
        localBus.subscribe(subcribeKey, this);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    /**
     *
     * @param o
     * @param s
     * @param o1 userIdentifier
     * @param s1
     * @return
     */
    @Override
    public Action onLocalBusMessage(Object o, String s, Object o1, String s1) {
        try{
            ExecutorUtil.submit(new Runnable() {
                @Override
                public void run() {
                    Long id = (Long)o1;
                    if(null == id) {
                        LOGGER.error("None of UserIdentifier");
                    } else {
                        if(LOGGER.isDebugEnabled()) {
                            LOGGER.debug("newUserAutoAuth id= " + id);
                        }
                        try {
                            newUserAutoLinked2Bill(id);
                        } catch(Exception exx) {
                            LOGGER.error("execute promotion error promotionId=" + id, exx);
                        }

                    }
                }
            });
        }catch (Exception e){

        }finally {

        }
        return Action.none;
    }

    private void newUserAutoLinked2Bill(Long identifierId) {
        UserIdentifier identifier = userProvider.findIdentifierById(identifierId);
        if(identifier.getClaimStatus().equals(IdentifierClaimStatus.CLAIMED.getCode())) {
            //如果用户的status为3，则说明已经合法，可以寻找个人用户了
            Long ownerUid = identifier.getOwnerUid();
            String identifierToken = identifier.getIdentifierToken();
            assetService.linkCustomerToBill(AssetTargetType.USER.getCode(), ownerUid, identifierToken);


        } else {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("identifier not found, identifierId=" + identifierId + " claimed=" + identifier);
            }
        }
    }
}

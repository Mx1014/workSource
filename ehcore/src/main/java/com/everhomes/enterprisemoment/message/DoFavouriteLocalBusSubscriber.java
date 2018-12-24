package com.everhomes.enterprisemoment.message;

import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.enterprisemoment.EnterpriseMoment;
import com.everhomes.enterprisemoment.EnterpriseMomentCommentProvider;
import com.everhomes.enterprisemoment.EnterpriseMomentFavouriteProvider;
import com.everhomes.enterprisemoment.EnterpriseMomentMessage;
import com.everhomes.enterprisemoment.EnterpriseMomentMessageProvider;
import com.everhomes.enterprisemoment.EnterpriseMomentProvider;
import com.everhomes.enterprisemoment.EnterpriseMomentService;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.enterprisemoment.MessageType;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentFavourites;
import com.everhomes.util.ExecutorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 点赞消息处理器
 */
@Component
public class DoFavouriteLocalBusSubscriber implements LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    EnterpriseMomentProvider momentProvider;
    @Autowired
    EnterpriseMomentMessageProvider messageProvider;
    @Autowired
    EnterpriseMomentCommentProvider commentProvider;
    @Autowired
    EnterpriseMomentFavouriteProvider favouriteProvider;
    @Autowired
    EnterpriseMomentService enterpriseMomentService;
    @Autowired
    OrganizationProvider organizationProvider;

    @Override
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        ExecutorUtil.submit(() -> {
            LocalEvent localEvent = (LocalEvent) args;
            LocalEventContext context = localEvent.getContext();
            Long enterpriseMomentId = localEvent.getEntityId();
            EnterpriseMoment moment = momentProvider.findEnterpriseMomentById(enterpriseMomentId);
            if (moment == null) {
                return;
            }
            Long favouriteId = localEvent.getObjParam("favouriteId", Long.class);
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(context.getUid(), moment.getOrganizationId());
            if (memberDetail == null) {
                return;
            }
            Set<Long> receiverUserIds = new HashSet<>();
            // 用户发布的动态被点赞了，给发布动态的人新增历史消息；
            receiverUserIds.add(moment.getCreatorUid());
            if (context.getUid().equals(moment.getCreatorUid())) {
                // 5.在用户评论的动态中，作者点赞了；
                receiverUserIds.addAll(commentProvider.findCommentUserIds(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId()));
                // 6.在用户点赞的动态中，作者点赞了；
                receiverUserIds.addAll(favouriteProvider.findFavoiriteUserIds(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId()));
            }

            // 上次该用户点赞时，已经有相关人员接收到点赞消息的用户ids，避免多次点赞重发
            List<Long> hasReceiveIds = messageProvider.findMessageReceiverListBySourceId(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId(), EhEnterpriseMomentFavourites.class.getSimpleName(), memberDetail.getTargetId(), receiverUserIds);
            for (Long receiverUid : receiverUserIds) {
                if (receiverUid.equals(memberDetail.getTargetId())) {
                    // 不需要给自己发消息
                    continue;
                }
                // 控制当用户多次点赞，取消点赞时，相关人员只会收到一次消息
                if (hasReceiveIds.contains(receiverUid)) {
                    continue;
                }
                EnterpriseMomentMessage message = new EnterpriseMomentMessage(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId(), MessageType.DO_FAVOURITE.getCode());
                message.setOperatorName(memberDetail.getContactName());
                message.setOperatorUid(memberDetail.getTargetId());
                message.setReceiverUid(receiverUid);
                message.setSourceType(EhEnterpriseMomentFavourites.class.getSimpleName());
                message.setSourceId(favouriteId);
                messageProvider.createEnterpriseMomentMessage(message);
                enterpriseMomentService.incrNewMessageCount(receiverUid, moment.getOrganizationId(), 1);
            }
        });
        return Action.none;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            LocalEventBus.subscribe(SystemEvent.ENTERPRISE_MOMENT_DO_FAVOURITE.getCode(), this);
        }
    }
}

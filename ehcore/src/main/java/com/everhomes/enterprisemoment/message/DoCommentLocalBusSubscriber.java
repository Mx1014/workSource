package com.everhomes.enterprisemoment.message;

import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.enterprisemoment.EnterpriseMoment;
import com.everhomes.enterprisemoment.EnterpriseMomentComment;
import com.everhomes.enterprisemoment.EnterpriseMomentCommentProvider;
import com.everhomes.enterprisemoment.EnterpriseMomentFavouriteProvider;
import com.everhomes.enterprisemoment.EnterpriseMomentMessage;
import com.everhomes.enterprisemoment.EnterpriseMomentMessageProvider;
import com.everhomes.enterprisemoment.EnterpriseMomentProvider;
import com.everhomes.enterprisemoment.EnterpriseMomentService;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.enterprisemoment.MessageType;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentComments;
import com.everhomes.util.ExecutorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 评论消息处理器
 */
@Component
public class DoCommentLocalBusSubscriber implements LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    EnterpriseMomentProvider momentProvider;
    @Autowired
    EnterpriseMomentMessageProvider messageProvider;
    @Autowired
    EnterpriseMomentCommentProvider commentProvider;
    @Autowired
    EnterpriseMomentFavouriteProvider favouriteProvider;
    @Autowired
    OrganizationProvider organizationProvider;
    @Autowired
    EnterpriseMomentService enterpriseMomentService;

    @Override
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        ExecutorUtil.submit(() -> {
            LocalEvent localEvent = (LocalEvent) args;
            LocalEventContext context = localEvent.getContext();
            Long enterpriseMomentId = localEvent.getEntityId();
            Long commentId = localEvent.getObjParam("commentId", Long.class);
            String messageContent = localEvent.getStringParam("messageContent");
            EnterpriseMomentComment comment = commentProvider.findEnterpriseMomentCommentById(commentId);
            if (comment == null) {
                return;
            }
            EnterpriseMoment moment = momentProvider.findEnterpriseMomentById(enterpriseMomentId);
            if (moment == null) {
                return;
            }

            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(context.getUid(), moment.getOrganizationId());
            if (memberDetail == null) {
                return;
            }
            Set<Long> receiverUserIds = new HashSet<>();
            // 2.用户发布的动态被评论了，给发布动态的人新增历史消息；
            receiverUserIds.add(moment.getCreatorUid());
            // 4.在用户评论的动态中，用户被回覆了，给被回复人新增历史消息；
            if (comment.getReplyToUserId() != null) {
                receiverUserIds.add(comment.getReplyToUserId());
            }
            if (context.getUid().equals(moment.getCreatorUid())) {
                // 5.在用户评论的动态中，作者评论了；
                receiverUserIds.addAll(commentProvider.findCommentUserIds(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId()));
                // 6.在用户点赞的动态中，作者评论了；
                receiverUserIds.addAll(favouriteProvider.findFavoiriteUserIds(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId()));
            }

            for (Long receiverUid : receiverUserIds) {
                if (receiverUid.equals(memberDetail.getTargetId())) {
                    // 不需要给自己发消息
                    continue;
                }
                EnterpriseMomentMessage message = new EnterpriseMomentMessage(moment.getNamespaceId(), moment.getOrganizationId(), moment.getId(), MessageType.DO_COMMENT.getCode());
                message.setOperatorName(memberDetail.getContactName());
                message.setOperatorUid(memberDetail.getTargetId());
                message.setReceiverUid(receiverUid);
                message.setSourceType(EhEnterpriseMomentComments.class.getSimpleName());
                message.setSourceId(commentId);
                message.setIntegralTag1(comment.getReplyToCommentId());  // 保留字段，保存被回复的评论id
                message.setIntegralTag2(comment.getReplyToUserId());     // 保留字段，保存被回复人的userId
                message.setMessage(messageContent);
                message.setOperateTime(comment.getCreateTime());
                messageProvider.createEnterpriseMomentMessage(message);
                enterpriseMomentService.incrNewMessageCount(receiverUid, moment.getOrganizationId(), 1);
            }
        });

        return Action.none;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            LocalEventBus.subscribe(SystemEvent.ENTERPRISE_MOMENT_DO_COMMENT.getCode(), this);
        }
    }
}

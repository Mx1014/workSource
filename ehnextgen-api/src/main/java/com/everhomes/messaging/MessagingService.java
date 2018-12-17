// @formatter:off
package com.everhomes.messaging;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.user.FetchMessageCommandResponse;
import com.everhomes.rest.user.FetchPastToRecentMessageCommand;
import com.everhomes.rest.user.FetchRecentToPastMessageAdminCommand;
import com.everhomes.rest.user.FetchRecentToPastMessageCommand;
import com.everhomes.user.UserLogin;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Message routing service
 * 
 * @author Kelven Yang
 *
 */
public interface MessagingService {
    FetchMessageCommandResponse fetchPastToRecentMessages(FetchPastToRecentMessageCommand cmd);
    FetchMessageCommandResponse fetchRecentToPastMessages(FetchRecentToPastMessageCommand cmd);
    long getMessageCountInLoginMessageBox(UserLogin login);
    
    void registerRoutingHandler(String channelType, MessageRoutingHandler handler);
    void unregisterRoutingHandler(String channelType);

    /**
     * <pre>
     * 发送消息
     * 1. 如果需要发送系统消息，senderLogin 请使用 {@link com.everhomes.user.User#SYSTEM_USER_LOGIN}
     * 2. 如果要发送带路由的系统消息（目前路由消息只支持系统用户发送）
     *      message 的 meta 参数设置：
     *          [1] meta-object-type {@link com.everhomes.rest.messaging.MessageMetaConstant#META_OBJECT_TYPE}
     *              的值为 message.router {@link com.everhomes.rest.messaging.MetaObjectType#MESSAGE_ROUTER}
     *          [2] meta-object {@link com.everhomes.rest.messaging.MessageMetaConstant#META_OBJECT}
     *              的值为 {@link com.everhomes.rest.messaging.RouterMetaObject} 对象实例
     *              其中 {@link com.everhomes.rest.messaging.RouterMetaObject} 对象中的 url 值可以根据
     *              {@link com.everhomes.util.RouterBuilder#build} 生成
     *
     *              路由定义在 {@link com.everhomes.rest.common.Router} 中，如果没有符合要求的需要和客户端约定好后加入到里面，
     *              请将约定好的路由写在对应的分类下。
     *              每个Router实例对应一个 ActionData 类，目的是为了方便通过{@link com.everhomes.util.RouterBuilder}拼接路由的参数
     *          [3] meta-subject {@link com.everhomes.rest.messaging.MessageMetaConstant#MESSAGE_SUBJECT}
     *              的值为消息的标题
     *
     * 具体可以参考 com.everhomes.group.GroupServiceImpl#sendRouterGroupNotificationUseSystemUser 方式
     *
     * {@code
     *
     *    // 组装消息体
     *    MessageDTO messageDto = new MessageDTO();
     *    messageDto.setBodyType(MessageBodyType.TEXT.getCode());
     *    messageDto.setBody(messageBody);
     *    ...
     *
     *    // 组装路由
     *    ThirdPartActionData actionData = new ThirdPartActionData();
     *    actionData.setUrl("xxx.zuolin.com");
     *
     *    // 拼装路由参数
     *    String url = RouterBuilder.build(Router.BROWSER_I, actionData);
     *
     *    RouterMetaObject metaObject = new RouterMetaObject();
     *    metaObject.setUrl(url);
     *
     *    // 组装消息 meta
     *    Map<String, String> meta = new HashMap<>();
     *    meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
     *    meta.put(MessageMetaConstant.MESSAGE_SUBJECT, messageSubject);
     *    meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
     *
     *    messageDto.setMeta(meta);
     *
     *    // 发送消息
     *    messagingService.routeMessage(...);
     * }
     * </pre>
     * @param senderLogin       发送方
     * @param appId             appId
     * @param dstChannelType    接收方类型
     * @param dstChannelToken   接收方token
     * @param message           消息
     * @param deliveryOption    发送方式
     */
    void routeMessage(UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
        MessageDTO message, int deliveryOption);
    
    void routeMessage(MessageRoutingContext context, UserLogin senderLogin, long appId, String dstChannelType, String dstChannelToken,
            MessageDTO message, int deliveryOption);
    FetchMessageCommandResponse fetchRecentToPastMessagesAny(FetchRecentToPastMessageAdminCommand cmd);

    DeferredResult<RestResponse> blockingEvent(String subjectId, String type, Integer timeOut, DeferredResult.DeferredResultHandler handler);

    String signalBlockingEvent(String subjectId, String message, Integer timeOut);

    FetchMessageCommandResponse fetchPastToRecentMessageWithoutUserLogin(
            FetchPastToRecentMessageCommand cmd);


}

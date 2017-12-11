// @formatter:off
package com.everhomes.point;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalEvent;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.point.PointArithmeticType;
import com.everhomes.rest.point.PointCommonStatus;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.StringHelper;
import freemarker.template.Configuration;

import java.util.HashMap;
import java.util.Map;

public class PointResultMessageAction implements PointResultAction {

    private MessagingService messagingService;
    private PointService pointService;

    private PointAction pointAction;
    private PointSystem pointSystem;
    private PointRule pointRule;
    private PointEventLog pointEventLog;

    private Configuration configuration;

    public PointResultMessageAction(PointAction pointAction, PointSystem pointSystem, PointRule pointRule, PointEventLog pointEventLog) {
        this.pointAction = pointAction;
        this.pointSystem = pointSystem;
        this.pointRule = pointRule;
        this.pointEventLog = pointEventLog;

        messagingService = PlatformContext.getComponent(MessagingService.class);
        pointService = PlatformContext.getComponent(PointService.class);

        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    }

    public void doAction() {
        TrueOrFalseFlag flag = TrueOrFalseFlag.fromCode(pointSystem.getPointExchangeFlag());
        if (flag == TrueOrFalseFlag.FALSE) {
            return;
        }

        PointCommonStatus status = PointCommonStatus.fromCode(pointRule.getStatus());
        if (status == PointCommonStatus.DISABLED) {
            return;
        }

        if (pointRule.getPoints() == 0) {
            return;
        }

        LocalEvent localEvent = (LocalEvent) StringHelper.fromJsonString(pointEventLog.getEventJson(), LocalEvent.class);

        Long points = 0L;
        PointArithmeticType arithmeticType = PointArithmeticType.fromCode(pointRule.getArithmeticType());
        switch (arithmeticType) {
            case SUBTRACT:
                points = -pointRule.getPoints();
                break;
            case ADD:
                points = pointRule.getPoints();
        }

        Map<String, String> map = new HashMap<>();
        map.put("points", String.valueOf(points));

        String content = pointAction.getContent();
        content = StringHelper.interpolate(content, map);

        MessageDTO messageDto = new MessageDTO();
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(localEvent.getContext().getUid())));
        // 组装路由
        OfficialActionData actionData = new OfficialActionData();
        actionData.setUrl(pointService.getPointSystemUrl(pointSystem.getId()));
        // 拼装路由参数
        String url = RouterBuilder.build(Router.BROWSER_I, actionData);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);
        // 组装消息 meta
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, "积分消息");
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        messageDto.setMeta(meta);

        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                MessageChannelType.USER.getCode(),
                String.valueOf(localEvent.getContext().getUid()),
                messageDto,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }
}
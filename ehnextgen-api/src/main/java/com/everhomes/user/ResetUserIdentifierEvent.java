// @formatter:off
package com.everhomes.user;

import com.everhomes.rest.user.UserResetIdentifierVo;
import org.springframework.context.ApplicationEvent;

/**
 * 对于用户修改手机号的事件，只要用户修改了手机号，系统就会发布这个事件
 * 对用户修改手机号敏感的业务需要监听此事件
 * Created by xq.tian on 2017/6/28.
 */
public class ResetUserIdentifierEvent extends ApplicationEvent {

    /**
     * Create a new ContextStartedEvent.
     * @param source the {@code ApplicationContext} that the event is raised for
     * (must not be {@code null})
     */
    public ResetUserIdentifierEvent(UserResetIdentifierVo source) {
        super(source);
    }
}

package com.everhomes.messaging;

import javax.servlet.http.HttpServletRequest;

import com.everhomes.rest.user.LoginToken;

public interface MessagingKickoffService {

    String getKickoffMessageKey(Integer namespaceId, LoginToken loginToken);

    void kickoff(Integer namespaceId, LoginToken loginToken);

    boolean isKickoff(Integer namespaceId, LoginToken loginToken);

    void removeKickoffTag(Integer namespaceId, LoginToken loginToken);
    
    /**
     * <p>游客模式是特殊做的一个功能，其只是抽取了一些游客模式需要使用的接口，把这些接口由原来的@RequireAuthentication(true)
     * 改为@RequireAuthentication(false)；但有些接口是有场景scene的，比如com.everhomes.forum.ForumService.getTopicQueryFilters，
     * 其里面调用了com.everhomes.user.UserService.checkUserScene来判断是否登录，若没有登录还需要按场景判断，若为非游客场景则会报
     * 没有权限的错误。</p>
     * <p>当使用同一个手机号，先登录A手机，然后再登录B手机，则A会被踢出；按产品设计，此时应该弹出提示框从而跳转到登录界面；
     * 但A被踢出后，若点击发现界面，则会调用getTopicQueryFilters接口，该接口由于已经改成不用登录的了，所以可以通过WebRequestInterceptor
     * 的校验，从而进入接口，在getTopicQueryFilters接口里面checkUserScene时会报没有权限的错误（与被踢出的错误码不同）；此时在界面看到的情况是
     * 报没有权限的错误，而不是跳转到登录界面的错误。</p>
     * <p>这种情况，不能直接在WebRequestInterceptor中把是否踢出的判断作为最开始的判断，原因有二：1）再次登录时，由于没有清redis中的被踢出标记，
     * 故即使是重新登录也会报被踢出的错误（因为校验是否被踢出条件永远成立）；2）如果登录过的用户，退出登录进入游客模式，由于客户端没有换cookie，
     * 即cookie仍然使用原来的token，导致也会报被踢出错误（因为校验被踢出条件仍然成立）；</p>
     * <p>为了解决上面的问题，目前（20170328）只能在有问题的接口中进行校验是否被踢出，如果是被踢出则先清掉被踢出标记，然后抛出被踢出错误码，
     * 让客户端跳转到被踢出界面，同时让下次能够正常登录；这些有问题的接口就是因为游客场景而修改@RequireAuthentication为false的接口</p>
     * <p>该接口主要用于解决上面的问题而用。</p>
     * @param request HTTP请求对象，只能在controller中调用
     */
    void checkKickoffStatus(HttpServletRequest request);

}

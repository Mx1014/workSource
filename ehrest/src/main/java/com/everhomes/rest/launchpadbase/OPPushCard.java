// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>router: 卡片路由，业务可根据此Id进行详情跳转</li>
 *     <li>subject: 卡片标题</li>
 *     <li>content: 卡片内容</li>
 *     <li>coverUrl: 卡片封面url</li>
 * </ul>
 */
public class OPPushCard {

    private Long router;
    private String subject;
    private String content;
    private String coverUrl;

    public Long getRouter() {
        return router;
    }

    public void setRouter(Long router) {
        this.router = router;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

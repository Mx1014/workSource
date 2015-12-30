package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>contentType:内容类型. 0-未知、1-本app使用轨迹、2-页面停留时长、3-设备及本app偏好设置</li>
 *         <li>content: 内容（JSON格式字符串）</li>
 *         <li>collectTimeMillis:收集时间（毫秒数）</li>
 *         </ul>
 */
public class SyncBehaviorCommand {

    private Integer contentType;
    private String content;
    private Long collectTimeMs;
    private Long reportTimeMs;

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCollectTimeMs() {
        return collectTimeMs;
    }

    public void setCollectTimeMs(Long collectTimeMs) {
        this.collectTimeMs = collectTimeMs;
    }

    public Long getReportTimeMs() {
        return reportTimeMs;
    }

    public void setReportTimeMs(Long reportTimeMs) {
        this.reportTimeMs = reportTimeMs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}

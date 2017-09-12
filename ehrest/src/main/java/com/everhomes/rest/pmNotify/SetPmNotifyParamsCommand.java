package com.everhomes.rest.pmNotify;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>notifyType: 提醒类型 参考{@link com.everhomes.rest.pmNotify.PmNotifyType}</li>
 *     <li>notifyMode: 提醒方式 参考{@link com.everhomes.rest.pmNotify.PmNotifyMode}</li>
 *     <li>repeatType: 周期提醒类型 参考{@link com.everhomes.rest.pmNotify.PmNotifyRepeatType}</li>
 *     <li>repeatId: 周期id</li>
 *     <li>receivers: 提醒接收人列表 参考{@link com.everhomes.rest.pmNotify.PmNotifyReceiver}</li>
 *     <li>notifyTickMinutes: 提前多久提醒, 都转化成分钟</li>
 * </ul>
 * Created by ying.xiong on 2017/9/11.
 */
public class SetPmNotifyParamsCommand {
    private Long id;

    private Integer namespaceId;

    private Long communityId;

    private Byte notifyType;

    private Byte notifyMode;

    private Byte repeatType;

    private Long repeatId;

    @ItemType(PmNotifyReceiver.class)
    private List<PmNotifyReceiver> receivers;

    private Integer notifyTickMinutes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getNotifyMode() {
        return notifyMode;
    }

    public void setNotifyMode(Byte notifyMode) {
        this.notifyMode = notifyMode;
    }

    public Integer getNotifyTickMinutes() {
        return notifyTickMinutes;
    }

    public void setNotifyTickMinutes(Integer notifyTickMinutes) {
        this.notifyTickMinutes = notifyTickMinutes;
    }

    public Byte getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(Byte notifyType) {
        this.notifyType = notifyType;
    }

    public List<PmNotifyReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<PmNotifyReceiver> receivers) {
        this.receivers = receivers;
    }

    public Long getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(Long repeatId) {
        this.repeatId = repeatId;
    }

    public Byte getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(Byte repeatType) {
        this.repeatType = repeatType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
